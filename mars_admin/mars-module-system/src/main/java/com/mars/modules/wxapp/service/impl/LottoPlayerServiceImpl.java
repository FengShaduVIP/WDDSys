package com.mars.modules.wxapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.modules.wxapp.entity.LottoPlayer;
import com.mars.modules.wxapp.entity.LottoPrize;
import com.mars.modules.wxapp.mapper.LottoPlayerMapper;
import com.mars.modules.wxapp.service.ILottoPlayerService;
import com.mars.modules.wxapp.service.ILottoPrizeService;
import com.mars.modules.wxapp.service.IWxUserService;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 微信小程序参与人员表
 * @Author: jeecg-boot
 * @Date:   2019-08-25
 * @Version: V1.0
 */
@Service
public class LottoPlayerServiceImpl extends ServiceImpl<LottoPlayerMapper, LottoPlayer> implements ILottoPlayerService {

    @Resource
    private ILottoPrizeService prizeService;


    @Override
    public Page<Map<String, Object>> queryPlayersByLottoIdOrLottoNo(Page<Object> page,String idOrNo) {
        return baseMapper.queryPlayersByLottoIdOrLottoNo(page,idOrNo);
    }

    @Override
    public Page<Map<String, Object>> queryMyCard(Page<Object> page, Integer type, String wxNo) {
        return baseMapper.queryMyCard(page,type,wxNo);
    }

    /**
     * 参与活动
     * @param wxNo
     * @param lottoId
     * @return
     */
    @Override
    public boolean joinLotto(String wxNo, String lottoId) {
        if(checkIsHaveJoinLotto(wxNo,lottoId)){
            return false;
        }else{
            LottoPlayer player = new LottoPlayer();
            player.setLottoId(lottoId);
            player.setWxNo(wxNo);
            return false;
        }
    }

    /**
     * 检查是否已参与抽奖
     * @param wxNo
     * @param lottoId
     * @return
     */
    @Override
    public boolean checkIsHaveJoinLotto(String wxNo, String lottoId) {
        Integer count = baseMapper.checkIsHaveJoinLotto(wxNo,lottoId);
        boolean flag = count > 0 ? true:false;
        return flag;
    }

    /**
     * 查询我参与的或者我中奖的抽奖活动
     * @param page
     * @param cardType 1 我中奖的
     * @param wxNo
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryMyJoinOrWin(Page<Object> page, Integer cardType, String wxNo) {
        Page<Map<String, Object>> listPage =baseMapper.queryMyJoinOrWin(page,cardType,wxNo);
        for (int i = 0; i < listPage.getRecords().size(); i++) {
            Map<String,Object> lottoMap = listPage.getRecords().get(i);
            String lottoId = MapUtils.getString(lottoMap,"id");
            List<Map<String,Object>> prizeList = prizeService.queryPrizesByLottoIdOrLottoNo(lottoId);
            lottoMap.put("prizes",prizeList);
        }
        return listPage;
    }

    /**
     * 根据活动ID 查询所有参与人员
     * @param id
     * @return
     */
    @Override
    public List<LottoPlayer> queryListByLottoId(String id) {
        QueryWrapper<LottoPlayer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lotto_id",id);
        queryWrapper.eq("status",0);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 保存获奖着 奖品信息
     * @param playerUser
     * @param prizeId
     */
    @Override
    public void savePrizeInfo(LottoPlayer playerUser, String prizeId) {
        try {
            LottoPrize prizeInfo = prizeService.getById(prizeId);
            playerUser.setPrizeId(prizeId);
            playerUser.setPrizeName(prizeInfo.getName());
            playerUser.setPrizeImg(prizeInfo.getImgUrl());
            playerUser.setIsWin(1);
            playerUser.setType(prizeInfo.getType());
            baseMapper.updateById(playerUser);
        }catch (Exception e){
            log.error("保存中奖者奖品信息出错");
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> findPlayerInfo(String lottoNo, String wxNo) {
        List<Map<String,Object>> cardList = baseMapper.findPlayerInfo(lottoNo,wxNo);
        if(cardList==null||cardList.size()<1){
            return null;
        }
        return cardList.get(0);
    }

    /**
     * 核销卡券
     * @param id
     * @return
     */
    @Override
    public String confirmCard(String id,String wxNo) {
        LottoPlayer player = baseMapper.selectById(id);
        if(player.getIsUsed()>0){
            return "该卡券已使用";
        }
        if(wxNo.equals(player.getWxNo())){
            player.setIsUsed(1);
            baseMapper.updateById(player);
            return "核销成功";
        }else{
            return "无权核销该卡券";
        }
    }

    @Override
    public void saveCardAddress(String id, String name, String tel, String address) {
        LottoPlayer player = baseMapper.selectById(id);
        player.setName(name);
        player.setTel(tel);
        player.setAddress(address);
        player.setStatus(1);
        baseMapper.updateById(player);
    }

    @Override
    public boolean checkCardHaveAddress(String id) {
        LottoPlayer player = baseMapper.selectById(id);
        if(player.getAddress()!=null&&player.getTel()!=null&&player.getName()!=null){
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> queryWinnerList(String lottoId,String prizeId) {
        return baseMapper.queryWinnerList(lottoId,prizeId);
    }
}
