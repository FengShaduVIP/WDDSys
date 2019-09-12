package com.mars.modules.wxapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.modules.wxapp.entity.WxUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 微信小程序用户
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
public interface IWxUserService extends IService<WxUser> {

    String getAccessToken();

    Map<String,String> wxUserLogin(String code);

    WxUser getUserByOpenId(String openId);

    Map<String,Object> findUserShortInfoById(String userId);

    WxUser getUserByWxNo(String username);

    Map<String,String> queryMyAddress(String wxNo);

    void addSumJoinNum(String wxNo);

    void addSumWinnerNum(String wxNo, int winnerSumNum);

    Page<Map<String, Object>> queryRankList(Page<Object> page);
}
