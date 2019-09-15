package com.mars.modules.wxapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.common.api.vo.Result;
import com.mars.common.constant.CacheConstant;
import com.mars.common.system.util.JwtUtil;
import com.mars.common.util.HttpUtil;
import com.mars.common.util.OKHttpUtil;
import com.mars.common.util.QiniuCloudUtil;
import com.mars.common.util.RedisUtil;
import com.mars.modules.wxapp.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

@Service
public class CommonServiceImpl implements ICommonService {

    @Resource
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${wx.applet.appid}")
    private String appid;

    @Value("${wx.applet.appsecret}")
    private String appSecret;

    /**
     * 获取上传七牛云上传token
     * @param bucketNm
     * @return
     */
    @Override
    public String getQiniuCloudUploadToken(String bucketNm) {
        if(redisUtil.hasKey(CacheConstant.QINIU_UPTOKEN_CACHE)){
            return redisUtil.get(CacheConstant.QINIU_UPTOKEN_CACHE).toString();
        }else{
            String uploadToken = QiniuCloudUtil.getToken(bucketNm);
            redisUtil.set(CacheConstant.QINIU_UPTOKEN_CACHE,uploadToken);
            return uploadToken;
        }
    }

    /**
     * 获取登录token
     * @param token
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.X_ACCESS_TOKEN,key = "'PREFIX_USER_TOKEN_'+#token")
    public String getXAccessToken(String token) {

        return null;
    }

    /**
     * 获取微信小程序 access_token
     * @return
     */
    @Override
    public String getWxAccessToken() {
        if(redisUtil.hasKey(CacheConstant.WXAPP_ACCESS_TOKEN)){
            return redisUtil.get(CacheConstant.WXAPP_ACCESS_TOKEN).toString();
        }else{
            String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appSecret;
            URI code2Session = HttpUtil.getURI(accessTokenUrl);
            String responseBody = restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class).getBody();
            JSONObject accessTokenJSON = JSON.parseObject(responseBody);
            String access_token = null;
            if(accessTokenJSON.containsKey("access_token")){
                access_token = accessTokenJSON.getString("access_token");
                redisUtil.set(CacheConstant.WXAPP_ACCESS_TOKEN,accessTokenJSON.getString("access_token"));
                redisUtil.expire(CacheConstant.WXAPP_ACCESS_TOKEN, JwtUtil.EXPIRE_TIME);
            }
            return access_token;
        }
    }

    /**
     * 生成微信小程序二维码 并返回二维码图片地址
     * @param bucketNm
     * @param imgName
     * @param page
     * @param scene
     * @param width
     * @param is_hyaline
     * @return
     */
    @Override
    public Result getWxQrCodeUrl(String bucketNm,String imgName,String page,String scene, String width, boolean is_hyaline) {
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+getWxAccessToken();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("scene",scene);
        jsonObject.put("width",width);
        jsonObject.put("is_hyaline",is_hyaline);
        byte response[] = OKHttpUtil.httpPostByte(url,jsonObject);
        InputStream is = new BufferedInputStream(new ByteArrayInputStream(response));
        return QiniuCloudUtil.uploadQrCode(bucketNm,is,imgName);
    }
}
