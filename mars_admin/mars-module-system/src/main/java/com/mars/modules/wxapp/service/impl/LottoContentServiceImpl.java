package com.mars.modules.wxapp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.common.util.UUIDGenerator;
import com.mars.modules.wxapp.entity.LottoContent;
import com.mars.modules.wxapp.mapper.LottoContentMapper;
import com.mars.modules.wxapp.service.ILottoContentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 微信小程序抽奖内容配置表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
@Service
public class LottoContentServiceImpl extends ServiceImpl<LottoContentMapper, LottoContent> implements ILottoContentService {

    @Override
    public void saveContents(String lottoId, String lottoNo, JSONArray contents) {
        for (int i = 0; i < contents.size(); i++) {
            JSONObject contentJson =  contents.getJSONObject(i);
            LottoContent content = new LottoContent();
            content.setId(UUIDGenerator.generate());
            content.setLottoId(lottoId);
            content.setLottoNo(lottoNo);
            String type = contentJson.getString("type");
            content.setType(contentJson.getString("type"));
            if("card".equals(type)){
                String value = contentJson.getString("title")+"$_$"+ contentJson.getString("body");
                content.setContent(value);
            }else{
                content.setContent(contentJson.getString("value"));
            }
            content.setSort(contentJson.getIntValue("sort"));
            baseMapper.insert(content);
        }
    }

    @Override
    public List<Map<String, Object>> queryContentsByLottoIdOrLottoNo(String idOrNo) {
        return baseMapper.queryContentsByLottoIdOrLottoNo(idOrNo);
    }
}
