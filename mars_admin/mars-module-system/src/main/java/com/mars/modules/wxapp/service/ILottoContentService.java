package com.mars.modules.wxapp.service;

import com.alibaba.fastjson.JSONArray;
import com.mars.modules.wxapp.entity.LottoContent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 微信小程序抽奖内容配置表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
public interface ILottoContentService extends IService<LottoContent> {

    void saveContents(String lottoId, String lottoNo, JSONArray contents);

    List<Map<String,Object>> queryContentsByLottoIdOrLottoNo(String id);
}
