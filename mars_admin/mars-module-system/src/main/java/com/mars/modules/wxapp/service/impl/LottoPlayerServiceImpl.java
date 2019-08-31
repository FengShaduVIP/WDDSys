package com.mars.modules.wxapp.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.modules.wxapp.entity.LottoPlayer;
import com.mars.modules.wxapp.mapper.LottoPlayerMapper;
import com.mars.modules.wxapp.service.ILottoPlayerService;
import com.mars.modules.wxapp.service.ILottoPrizeService;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
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
}
