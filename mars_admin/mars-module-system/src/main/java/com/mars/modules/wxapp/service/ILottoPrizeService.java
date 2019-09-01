package com.mars.modules.wxapp.service;

import com.alibaba.fastjson.JSONArray;
import com.mars.modules.wxapp.entity.LottoPrize;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 微信小程序抽奖_奖品表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
public interface ILottoPrizeService extends IService<LottoPrize> {

    void savePrizes(String lottoId, String lottoNo,JSONArray prizes);

    List<Map<String,Object>> queryPrizesByLottoIdOrLottoNo(String lottoId);

    List<LottoPrize> queryPrizesByLottoId(String id);
}
