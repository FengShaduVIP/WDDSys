package com.mars.modules.wxapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.modules.wxapp.entity.LottoPlayer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 微信小程序参与人员表
 * @Author: jeecg-boot
 * @Date:   2019-08-25
 * @Version: V1.0
 */
public interface ILottoPlayerService extends IService<LottoPlayer> {

    Page<Map<String,Object>> queryPlayersByLottoIdOrLottoNo(Page<Object> page, String idOrNo);

    Page<Map<String,Object>> queryMyCard(Page<Object> objectPage, Integer type, String userName);

    boolean joinLotto(String wxNo, String lottoId);

    boolean checkIsHaveJoinLotto(String wxNo, String lottoId);

    Page<Map<String,Object>> queryMyJoinOrWin(Page<Object> objectPage, Integer cardType, String wxNo);

    List<LottoPlayer> queryListByLottoId(String id);

    void savePrizeInfo(LottoPlayer playerUser, String prizeId);

    Map<String,Object> findPlayerInfo(String lottoNo, String wxNo);

    String confirmCard(String id,String wxNo);

    boolean checkCardHaveAddress(String id);

    void saveCardAddress(String id, String name, String tel, String s);
}
