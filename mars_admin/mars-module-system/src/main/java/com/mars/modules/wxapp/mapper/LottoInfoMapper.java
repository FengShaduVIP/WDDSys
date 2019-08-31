package com.mars.modules.wxapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.modules.wxapp.entity.LottoInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Description: 微信小程序活动表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
public interface LottoInfoMapper extends BaseMapper<LottoInfo> {

    Page<Map<String,Object>> queryLottoForIndex(Page<Object> objectPage);

    Page<Map<String,Object>> queryUserLottoForIndex(Page<Object> objectPage,@Param("wxNo") String wxNo);

    Integer deleteLottoById(@Param("lottoId") String lottoId);
}
