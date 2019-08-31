package com.mars.modules.wxapp.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.mars.modules.wxapp.entity.LottoPlayer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 微信小程序参与人员表
 * @Author: jeecg-boot
 * @Date:   2019-08-25
 * @Version: V1.0
 */
public interface LottoPlayerMapper extends BaseMapper<LottoPlayer> {

    Page<Map<String,Object>> queryPlayersByLottoIdOrLottoNo(Page<Object> page,@Param("idOrNo") String idOrNo);

    Page<Map<String,Object>> queryMyCard(Page<Object> page, @Param("type") Integer type,@Param("wxNo") String wxNo);

    Page<Map<String,Object>> queryMyJoinOrWin(Page<Object> page, @Param("type") Integer cardType,@Param("wxNo") String wxNo);

    Integer checkIsHaveJoinLotto(@Param("wxNo") String wxNo,@Param("lottoId") String lottoId);
}
