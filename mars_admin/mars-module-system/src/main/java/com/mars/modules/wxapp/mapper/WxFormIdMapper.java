package com.mars.modules.wxapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.mars.modules.wxapp.entity.WxFormId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 微信小程序formId
 * @Author: jeecg-boot
 * @Date:   2019-09-13
 * @Version: V1.0
 */
public interface WxFormIdMapper extends BaseMapper<WxFormId> {

    String findFormIdByOpenId(@Param("openId") String openId);
}
