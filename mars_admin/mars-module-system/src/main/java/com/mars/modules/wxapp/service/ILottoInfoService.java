package com.mars.modules.wxapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.modules.wxapp.entity.LottoInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 微信小程序活动表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
public interface ILottoInfoService extends IService<LottoInfo> {

    LottoInfo saveLottoInfo(String userId,LottoInfo lottoInfo);

    Page<Map<String,Object>> queryLottoForIndex(Integer page, Integer pageSize);

    IPage<Map<String,Object>> queryUserLottoForIndex(String wxNo,Integer page, Integer pageSize);

    Map<String,Object> queryDetailByLottoNo(String lottoNo);

    boolean joinLotto(String wxNo, String lottoId);

    boolean deleteLottoById(String lottoId);

    List<LottoInfo> queryNotOpenLotto(int type);
}
