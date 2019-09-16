package com.mars.modules.wxapp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.common.constant.CacheConstant;
import com.mars.common.constant.CommonConstant;
import com.mars.common.system.api.ISysBaseAPI;
import com.mars.common.system.util.JwtUtil;
import com.mars.common.util.HttpUtil;
import com.mars.common.util.RedisUtil;
import com.mars.common.util.UUIDGenerator;
import com.mars.modules.system.entity.SysUser;
import com.mars.modules.system.service.ISysUserService;
import com.mars.modules.wxapp.entity.Code2SessionResponse;
import com.mars.modules.wxapp.mapper.WxUserMapper;
import com.mars.modules.wxapp.entity.WxUser;
import com.mars.modules.wxapp.service.IWxUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 微信小程序用户
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements IWxUserService {

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ISysUserService sysUserService;

    @Value("${wx.applet.appid}")
    private String appid;

    @Value("${wx.applet.appsecret}")
    private String appSecret;

    /**
     * 微信的 code2session 接口 获取微信用户信息
     * 官方说明 : https://developers.weixin.qq.com/miniprogram/dev/api/open-api/login/code2Session.html
     */
    private String code2Session(String jsCode) {
        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", appid);
        params.add("secret", appSecret);
        params.add("js_code", jsCode);
        params.add("grant_type", "authorization_code");
        URI code2Session = HttpUtil.getURIwithParams(code2SessionUrl, params);
        return restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class).getBody();
    }

    /**
     * 微信的 getAccessToken 接口 获取 AccessToken
     * 官方说明 : https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
     */
    @Override
    @Cacheable(value = CacheConstant.WXAPP_ACCESS_TOKEN,key = "'bucketNm_'+#bucketNm")
    public String getAccessToken() {
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appSecret;
        URI code2Session = HttpUtil.getURI(accessTokenUrl);
        return restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class).getBody();
    }



    /**
     * 微信小程序用户登陆，完整流程可参考下面官方地址，本例中是按此流程开发
     * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html
     *
     * @param code 小程序端 调用 wx.login 获取到的code,用于调用 微信code2session接口
     * @return 返回后端 自定义登陆态 token  基于JWT实现
     */
    @Override
    public Map<String,String> wxUserLogin(String code) {
        //1 . code2session返回JSON数据
        String resultJson = code2Session(code);
        //2 . 解析数据
        JSONObject jsonObject = JSONObject.parseObject(resultJson);
        Code2SessionResponse response = JSONObject.toJavaObject(jsonObject, Code2SessionResponse.class);
        if (!response.getErrcode().equals("0"))
            throw new AuthenticationException("code2session失败 : " + response.getErrmsg());
        else {
            String wxOpenId = response.getOpenid();
            //3 . 先从本地数据库中查找用户是否存在
            QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
            wrapper.eq("open_id",wxOpenId);
            WxUser wxUser = baseMapper.selectOne(wrapper);

            if (wxUser == null) {
                wxUser = new WxUser();
                wxUser.setWxNo(getWxNo(CommonConstant.WXAPP_USER_WXNO_SIZE));
                wxUser.setLevel(CommonConstant.WXAPP_USER_LEVEL_0);
                wxUser.setOpenId(response.getOpenid());    //不存在就新建用户
                wxUser.setSessionKey(response.getSession_key());
                wxUser.setLastLoginTime(new Date());
                baseMapper.insert(wxUser);
            }else{
                wxUser.setSessionKey(response.getSession_key());
                wxUser.setLastLoginTime(new Date());
                baseMapper.updateById(wxUser);
            }

            //检查系统用户是否存在
            SysUser user = sysUserService.getUserByName(wxUser.getWxNo());
            if(user==null){
                user = new SysUser();
                user.setId(UUIDGenerator.generate());
                user.setUsername(wxUser.getWxNo());
                user.setPassword(wxUser.getOpenId());
                user.setStatus(CommonConstant.DEL_FLAG_0);
                user.setDelFlag(CommonConstant.DEL_FLAG_0+"");
                user.setStatus(CommonConstant.USER_UNFREEZE);
                user.setCreateBy("小程序登陆添加");
                user.setCreateTime(new Date());
                sysUserService.save(user);
            }
            String token = JwtUtil.sign(wxUser.getWxNo(), wxUser.getOpenId());
            //4 . 更新sessionKey和 登陆时间
            //设置超时时间
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME);
            //5 . JWT 返回自定义登陆态 Token
            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);

            sysBaseAPI.addLog("微信小程序用户名: "+user.getId()+",登录成功！open_id :"+wxOpenId, CommonConstant.LOG_TYPE_1, null);

            Map<String,String> mapValue = new HashMap<>();
            mapValue.put("token",token);
            mapValue.put("wxOpenid",response.getOpenid());
            return mapValue;
        }
    }

    @Override
    public WxUser getUserByOpenId(String wxOpenId) {
        QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("open_id",wxOpenId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public WxUser getUserByWxNo(String username) {
        QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("wx_no",username);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 根据用户ID 查找用户名称 头像 性别
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> findUserShortInfoById(String userId) {
        try {
            QueryWrapper<WxUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(new String[]{"nick_name","avatar_url","gender"}).eq("open_id",userId);
            List<Map<String,Object>> userMapList =  baseMapper.selectMaps(queryWrapper);
            return userMapList!=null?userMapList.get(0):null;
        }catch (Exception e){
            log.error("查询用户名称，头像失败 ："+e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, String> queryMyAddress(String wxNo) {

        return baseMapper.queryMyAddress(wxNo);
    }

    /**
     * 更新 发奖者 总参与人数
     * @param wxNo
     */
    @Override
    public void addSumJoinNum(String wxNo) {
        WxUser wxUser = getUserByWxNo(wxNo);
        if(wxUser!=null){
            wxUser.setJoinNum(wxUser.getJoinNum()+1);
            baseMapper.updateById(wxUser);
        }
    }

    /**
     * 更新 发奖者 总中奖人数
     * @param wxNo
     */
    @Override
    public void addSumWinnerNum(String wxNo, int winnerSumNum) {
        WxUser wxUser = getUserByWxNo(wxNo);
        if(wxUser!=null){
            wxUser.setWinNum(wxUser.getWinNum()+winnerSumNum);
            baseMapper.updateById(wxUser);
        }
    }

    /**
     * 获取 系统微信编号 000001
     * @return
     */
    private static String getWxNo(int numLenght){
        Random random = new Random();
        String startNum = "1";
        int i = 1;
        while (i < numLenght) {
            StringBuffer sb = new StringBuffer();
            startNum = startNum + "0";
            i+=1;
        }
        int ranNum= (int)(random.nextDouble()*(Integer.parseInt(startNum)-10000 + 1))+ 10000;
        return "0"+ranNum;
    }

    /**
     * 查询排行榜列表信息
     * @param page
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryRankList(Page<Object> page) {

        return baseMapper.queryRankList(page);
    }
}
