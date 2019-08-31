package com.mars.modules.wxapp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.mars.modules.wxapp.entity.LottoContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 微信小程序抽奖内容配置表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
public interface LottoContentMapper extends BaseMapper<LottoContent> {

    List<Map<String,Object>> queryContentsByLottoIdOrLottoNo(@Param("idOrNo") String idOrNo);
}
