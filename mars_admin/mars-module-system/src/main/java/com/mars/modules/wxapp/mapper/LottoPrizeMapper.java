package com.mars.modules.wxapp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.mars.modules.wxapp.entity.LottoPrize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 微信小程序抽奖_奖品表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
public interface LottoPrizeMapper extends BaseMapper<LottoPrize> {

    List<Map<String,Object>> queryPrizesByLottoIdOrLottoNo(@Param("idOrNo") String idOrNo);
}
