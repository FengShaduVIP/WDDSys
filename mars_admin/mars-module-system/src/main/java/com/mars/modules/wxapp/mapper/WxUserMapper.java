package com.mars.modules.wxapp.mapper;

import com.mars.modules.wxapp.entity.WxUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Description: 微信小程序用户
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
public interface WxUserMapper extends BaseMapper<WxUser> {

    Map<String,String> queryMyAddress(@Param("wxNo") String wxNo);
}
