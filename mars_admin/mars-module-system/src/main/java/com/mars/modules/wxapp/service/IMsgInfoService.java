package com.mars.modules.wxapp.service;

import com.alibaba.fastjson.JSONObject;
import com.mars.modules.wxapp.entity.MsgInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 微信小程序发送消息记录
 * @Author: jeecg-boot
 * @Date:   2019-09-13
 * @Version: V1.0
 */
public interface IMsgInfoService extends IService<MsgInfo> {

    void SendWxAppMsg(String openId,String templateId,String pagePath,JSONObject params);

}
