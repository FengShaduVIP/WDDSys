package com.mars.modules.wxapp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.modules.wxapp.entity.LottoPrize;
import com.mars.modules.wxapp.mapper.LottoPrizeMapper;
import com.mars.modules.wxapp.service.ILottoPrizeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 微信小程序抽奖_奖品表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
@Service
public class LottoPrizeServiceImpl extends ServiceImpl<LottoPrizeMapper, LottoPrize> implements ILottoPrizeService {

    @Override
    public void savePrizes(String lottoId,String lottoNo, JSONArray prizes) {
        for (int i = 0; i < prizes.size(); i++) {
            LottoPrize prizeInfo = new LottoPrize();
            prizeInfo.setLottoId(lottoId);
            prizeInfo.setLottoNo(lottoNo);
            prizeInfo.setStatus(0);
            JSONObject prize = prizes.getJSONObject(i);
            String name = prize.getString("name");
            prizeInfo.setName(name);
            Integer sort = prize.getIntValue("sort");
            prizeInfo.setSort(sort);
            Integer type = prize.getIntValue("type");
            prizeInfo.setType(type);
            Integer num = prize.getIntValue("num");
            prizeInfo.setNum(num);
            prizeInfo.setLeftNum(num);
            if(type>1){
                Integer money = prize.getIntValue("money");
                prizeInfo.setName(money+"");
            }else {
                String img = prize.getString("img");
                prizeInfo.setImgUrl(img);
            }
            baseMapper.insert(prizeInfo);
        }
    }

    /**
     * 根据活动 id或活动编码 查询活动奖品
     * @param idOrNo
     * @return
     */
    @Override
    public List<Map<String, Object>> queryPrizesByLottoIdOrLottoNo(String idOrNo) {
        return baseMapper.queryPrizesByLottoIdOrLottoNo(idOrNo);
    }
}
