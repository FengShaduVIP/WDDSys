package com.mars.modules.wxapp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mars.common.util.OKHttpUtil;
import com.mars.common.util.StringUtils;
import com.mars.modules.wxapp.entity.MsgInfo;
import com.mars.modules.wxapp.mapper.MsgInfoMapper;
import com.mars.modules.wxapp.service.ICommonService;
import com.mars.modules.wxapp.service.IMsgInfoService;
import com.mars.modules.wxapp.service.IWxFormIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 微信小程序发送消息记录
 * @Author: jeecg-boot
 * @Date:   2019-09-13
 * @Version: V1.0
 */
@Slf4j
@Service
public class MsgInfoServiceImpl extends ServiceImpl<MsgInfoMapper, MsgInfo> implements IMsgInfoService {


    @Resource
    private IWxFormIdService formIdService;

    @Resource
    private ICommonService commonService;

    /**
     * 发送微信小程序 模板消息
     * @param openId
     * @param templateId
     * @param pagePath
     * @param params
     */
    public void SendWxAppMsg(String openId,String templateId,String pagePath,JSONObject params){
        String formId = formIdService.findFormIdByOpenId(openId);
        if(!StringUtils.checkIsNull(formId)){
            String accessToken = commonService.getWxAccessToken();
            String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+accessToken;
            JSONObject sendData = new JSONObject();
            sendData.put("touser",openId);
            sendData.put("template_id",templateId);
            sendData.put("page",pagePath);
            sendData.put("form_id",formId);
            sendData.put("data",params);
            sendData.put("emphasis_keyword","keyword1.DATA");
            String response = OKHttpUtil.httpPost(url,sendData);
            log.info(response);
            if(response.indexOf("ok")>0){
                formIdService.changeFormId(formId);
            }else{
                log.error("formId:{} 发送消息失败",formId);
            }

        }
    }


}
