package com.mars.modules.wxapp.service;

import com.mars.modules.wxapp.entity.WxFormId;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 微信小程序formId
 * @Author: jeecg-boot
 * @Date:   2019-09-13
 * @Version: V1.0
 */
public interface IWxFormIdService extends IService<WxFormId> {

    int saveFormId(String wxNo, String formId);

    String findFormIdByOpenId(String openId);

    void changeFormId(String formId);
}
