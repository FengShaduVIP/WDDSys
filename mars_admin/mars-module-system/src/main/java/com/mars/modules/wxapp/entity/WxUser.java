package com.mars.modules.wxapp.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 微信小程序用户
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
@Data
@TableName("wx_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wx_user对象", description="微信小程序用户")
public class WxUser {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**微信用户唯一标识*/
	@Excel(name = "微信用户唯一标识", width = 15)
    @ApiModelProperty(value = "微信用户唯一标识")
	private java.lang.String openId;
	/**微信名称*/
	@Excel(name = "微信名称", width = 15)
    @ApiModelProperty(value = "微信名称")
	private java.lang.String nickName;
	/**头像地址*/
	@Excel(name = "头像地址", width = 15)
    @ApiModelProperty(value = "头像地址")
	private java.lang.String avatarUrl;
	/**城市*/
	@Excel(name = "城市", width = 15)
    @ApiModelProperty(value = "城市")
	private java.lang.String city;
	/**国家*/
	@Excel(name = "国家", width = 15)
    @ApiModelProperty(value = "国家")
	private java.lang.String country;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	private java.lang.Integer gender;
	/**省份*/
	@Excel(name = "省份", width = 15)
    @ApiModelProperty(value = "省份")
	private java.lang.String province;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private java.lang.String tel;
	/**收货人*/
	@Excel(name = "收货人", width = 15)
    @ApiModelProperty(value = "收货人")
	private java.lang.String consignee;
	@Excel(name = "地区", width = 15)
	@ApiModelProperty(value = "地区")
	private java.lang.String region;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private java.lang.String detailAddress;
	/**用户等级（-1 机器人 0 默认用户  1商家 2运营 3管理员）*/
	@Excel(name = "用户等级（-1 机器人 0 默认用户  1商家 2运营 3管理员）", width = 15)
    @ApiModelProperty(value = "用户等级（-1 机器人 0 默认用户  1商家 2运营 3管理员）")
	private java.lang.Integer level;
	/**sessionKey*/
	@Excel(name = "sessionKey", width = 15)
    @ApiModelProperty(value = "sessionKey")
	private java.lang.String sessionKey;
	/**上次登陆时间*/
	@Excel(name = "上次登陆时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上次登陆时间")
	private java.util.Date lastLoginTime;
	/**账号状态 0正常 1删除 2禁用*/
	@Excel(name = "账号状态 0正常 1删除 2禁用", width = 15)
    @ApiModelProperty(value = "账号状态 0正常 1删除 2禁用")
	private java.lang.Integer status;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
	private java.util.Date createTime;
	/**createBy*/
	@Excel(name = "createBy", width = 15)
    @ApiModelProperty(value = "createBy")
	private java.lang.String createBy;
	/**updateBy*/
	@Excel(name = "updateBy", width = 15)
    @ApiModelProperty(value = "updateBy")
	private java.lang.String updateBy;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateTime")
	private java.util.Date updateTime;

	/**
	 * 微信小程序 系统微信号
	 */
	private java.lang.String wxNo;

}
