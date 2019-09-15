package com.mars.modules.wxapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mars.modules.wxapp.entity.WxFormId;
import com.mars.modules.wxapp.entity.WxUser;
import com.mars.modules.wxapp.mapper.WxFormIdMapper;
import com.mars.modules.wxapp.service.IWxFormIdService;
import com.mars.modules.wxapp.service.IWxUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 微信小程序formId
 * @Author: jeecg-boot
 * @Date:   2019-09-13
 * @Version: V1.0
 */
@Service
public class WxFormIdServiceImpl extends ServiceImpl<WxFormIdMapper, WxFormId> implements IWxFormIdService {

    @Resource
    private IWxUserService wxUserService;

    /**
     * 保存FormId
     * @param wxNo
     * @param formId
     * @return
     */
    @Override
    public int saveFormId(String wxNo, String formId) {
        WxUser user = wxUserService.getUserByWxNo(wxNo);
        if(user!=null){
            WxFormId formIdObj = new WxFormId();
            formIdObj.setWxno(wxNo);
            formIdObj.setOpenId(user.getOpenId());
            formIdObj.setFormId(formId);
            formIdObj.setIsUsed(0);
            return baseMapper.insert(formIdObj);
        }
        return 0;
    }

    /**
     * 根据openid  获取最早 有效formId
     * @param openId
     * @return
     */
    @Override
    public String findFormIdByOpenId(String openId) {
        return baseMapper.findFormIdByOpenId(openId);
    }

    @Override
    public void changeFormId(String formId) {
        QueryWrapper<WxFormId> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("form_id",formId);
        WxFormId wxFormId = baseMapper.selectOne(queryWrapper);
        if(wxFormId!=null){
            wxFormId.setIsUsed(1);
            baseMapper.updateById(wxFormId);
        }
    }
}
