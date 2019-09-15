package com.mars.modules.wxapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.modules.wxapp.entity.LottoInfo;
import com.mars.modules.wxapp.entity.LottoPlayer;
import com.mars.modules.wxapp.entity.WxUser;
import com.mars.modules.wxapp.mapper.LottoInfoMapper;
import com.mars.modules.wxapp.service.*;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: 微信小程序活动表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
@Service
public class LottoInfoServiceImpl extends ServiceImpl<LottoInfoMapper, LottoInfo> implements ILottoInfoService {

    @Resource
    private ILottoPrizeService prizeService;
    @Resource
    private ILottoContentService contentService;
    @Resource
    private ILottoPlayerService playerService;

    @Resource
    private IWxUserService wxUserService;

    @Resource
    private IMsgInfoService msgInfoService;

    @Override
    public LottoInfo saveLottoInfo(String userId,LottoInfo lottoInfo) {
        WxUser wxUser = wxUserService.getUserByWxNo(userId);
        if(wxUser!=null){
            lottoInfo.setAvatarUrl(wxUser.getAvatarUrl());
            lottoInfo.setWxNo(wxUser.getWxNo());
        }
        wxUser.setNewNum(wxUser.getNewNum()+1);
        wxUserService.updateById(wxUser);
        lottoInfo.setIsShow(1);
        lottoInfo.setLottoNo(getLottoNo());
        baseMapper.insert(lottoInfo);
        return lottoInfo;
    }


    /**
     * 查询微信小程序 首页抽奖活动
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryLottoForIndex(Integer page, Integer pageSize) {
        Page<Map<String, Object>> listPage = baseMapper.queryLottoForIndex(new Page<>(page,pageSize));
        for (int i = 0; i < listPage.getRecords().size(); i++) {
            Map<String,Object> lottoMap = listPage.getRecords().get(i);
            String lottoId = MapUtils.getString(lottoMap,"id");
            List<Map<String,Object>> prizeList = prizeService.queryPrizesByLottoIdOrLottoNo(lottoId);
            lottoMap.put("prizes",prizeList);
        }
        return listPage;
    }

    @Override
    public IPage<Map<String, Object>> queryUserLottoForIndex(String wxNo,Integer page, Integer pageSize) {
        Page<Map<String, Object>> listPage = baseMapper.queryUserLottoForIndex(new Page<>(page,pageSize),wxNo);
        for (int i = 0; i < listPage.getRecords().size(); i++) {
            Map<String,Object> lottoMap = listPage.getRecords().get(i);
            String lottoId = MapUtils.getString(lottoMap,"id");
            List<Map<String,Object>> prizeList = prizeService.queryPrizesByLottoIdOrLottoNo(lottoId);
            lottoMap.put("prizes",prizeList);
        }
        return listPage;
    }

    /**
     * 根据活动 ID 或者 活动 lotto_no  查询详情
     * @param lottoNo
     * @return
     */
    @Override
    public Map<String, Object> queryDetailByLottoNo(String lottoNo) {
        Map<String,Object> lottoInfoMap = new HashMap<>();
        QueryWrapper<LottoInfo> queryWrapper = new QueryWrapper<LottoInfo>();
        queryWrapper.eq("lotto_no",lottoNo).or().eq("id ",lottoNo);
        LottoInfo lottoInfo = baseMapper.selectOne(queryWrapper);
        lottoInfo.setViewNum(lottoInfo.getViewNum()+1);
        baseMapper.updateById(lottoInfo);
        if(lottoInfo!=null){
            List<Map<String,Object>> prizeList = prizeService.queryPrizesByLottoIdOrLottoNo(lottoInfo.getId());
            List<Map<String,Object>> contents =  contentService.queryContentsByLottoIdOrLottoNo(lottoInfo.getId());
            Page<Map<String,Object>> players = playerService.queryPlayersByLottoIdOrLottoNo(new Page<>(1,10),lottoInfo.getId());
            if(players.getRecords().size()>0){
                lottoInfoMap.put("players",players.getRecords());
            }else{
                lottoInfoMap.put("players",new ArrayList<>());
            }
            lottoInfoMap.put("contents",contents);
            lottoInfoMap.put("prizes",prizeList);
        }

        lottoInfoMap.put("lottoNo",lottoInfo.getLottoNo());
        lottoInfoMap.put("id",lottoInfo.getId());
        lottoInfoMap.put("avatarUrl",lottoInfo.getAvatarUrl());
        lottoInfoMap.put("nickName",lottoInfo.getNickName());
        lottoInfoMap.put("banner",lottoInfo.getBanner());
        lottoInfoMap.put("wxNo",lottoInfo.getCreateBy());
        lottoInfoMap.put("isEnd",lottoInfo.getIsEnd());
        lottoInfoMap.put("isCommand",lottoInfo.getIsCommand());
        lottoInfoMap.put("command",lottoInfo.getCommand());
        lottoInfoMap.put("lottoType",lottoInfo.getLottoType());
        lottoInfoMap.put("joinNum",lottoInfo.getJoinNum());
        lottoInfoMap.put("createBy",lottoInfo.getCreateBy());
        lottoInfoMap.put("openType",lottoInfo.getOpenType());
        lottoInfoMap.put("openTime",lottoInfo.getOpenTime());
        lottoInfoMap.put("openNum",lottoInfo.getOpenNum());
        return lottoInfoMap;
    }


    /**
     * 参与抽奖活动
     * @param wxNo
     * @param lottoId
     * @return
     */
    @Override
    public boolean joinLotto(String wxNo, String lottoId) {
        if(playerService.checkIsHaveJoinLotto(wxNo,lottoId)){
            return false;
        }else {
            QueryWrapper<LottoInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",lottoId).eq("status",0);
            LottoInfo lottoInfo = baseMapper.selectOne(queryWrapper);
            if(lottoInfo==null){
                return false;
            }
            WxUser userInfo = wxUserService.getUserByWxNo(wxNo);
            lottoInfo.setJoinNum(lottoInfo.getJoinNum()+1);
            baseMapper.updateById(lottoInfo);
            LottoPlayer player = new LottoPlayer();
            player.setWxNo(wxNo);
            player.setOpenId(userInfo.getOpenId());
            player.setLottoId(lottoId);
            player.setLottoNo(lottoInfo.getLottoNo());
            player.setAvatarUrl(lottoInfo.getAvatarUrl());
            player.setNickName(lottoInfo.getNickName());
            //更新创建者 统计数据
            wxUserService.addSumJoinNum(lottoInfo.getWxNo());
            return playerService.save(player);
        }
    }

    @Override
    public boolean deleteLottoById(String lottoId) {
        return baseMapper.deleteLottoById(lottoId)>0?true:false;
    }


    /**
     * 查询待开奖的活动
     * @return
     */
    @Override
    public List<LottoInfo> queryNotOpenLotto(int type) {
        QueryWrapper<LottoInfo> queryWrapper = new QueryWrapper<>();
        if(type>1){
            queryWrapper.eq("status",0);
            queryWrapper.eq("is_end",0).apply("open_num < join_num");
            return baseMapper.selectList(queryWrapper);
        }else {
            queryWrapper.eq("open_type",0);
            queryWrapper.le("open_time",new Date());
            queryWrapper.eq("status",0);
            queryWrapper.eq("is_end",0);
            return baseMapper.selectList(queryWrapper);
        }
    }

    /**
     * 活动开奖结束 发送消息给用户
     * @param lottoId
     */
    @Override
    public void sendMsgToPlayer(String lottoId) {
        String templateId = "jnouuVFU--w4sgi7MPsozZ0u5fnD8JkqSrFfwwsoWOA";
        String pagePath = "pages/index/detail/detail?id="+lottoId;
        List<LottoPlayer> playerList = playerService.queryListByLottoId(lottoId);
        List<Map<String,Object>> prizeList = prizeService.queryPrizesByLottoIdOrLottoNo(lottoId);
        String prizesName = "";
        for (int i = 0; i <prizeList.size(); i++) {
            prizesName = prizesName +" "+prizeList.get(i).get("name");
        }
        if(playerList!=null&&playerList.size()>0){
            JSONObject params = new JSONObject();
            for (int i = 0; i < playerList.size(); i++) {
                LottoPlayer player = playerList.get(i);
                params.clear();
                String keyword1 = "{\"value\":\"中奖名单已公布\",}";
                String keyword2 = "{\"value\":\""+prizesName+"\",}";
                String keyword3 = "{\"value\":\"您参加「"+player.getNickName()+" 」发起的抽奖正在开奖，点击查看中奖名单>>>\",}";
                params.put("keyword1", JSON.parseObject(keyword1));
                params.put("keyword2",JSON.parseObject(keyword2));
                params.put("keyword3",JSON.parseObject(keyword3));
                msgInfoService.SendWxAppMsg(player.getOpenId(),templateId,pagePath,params);
            }
        }
    }

    /**
     * 获取抽奖活动 编码
     * 规则 按数量排序
     * @return
     */
    private String getLottoNo(){
        QueryWrapper<LottoInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("lotto_no");
        int count = baseMapper.selectCount(queryWrapper);
        return String.format("%08d",count);
    }


}
