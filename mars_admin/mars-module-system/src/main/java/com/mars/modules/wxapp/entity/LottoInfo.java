package com.mars.modules.wxapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 微信小程序活动表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
@Data
@TableName("wx_lotto_info")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wx_lotto_info对象", description="微信小程序活动表")
public class LottoInfo {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**活动编号*/
	@Excel(name = "活动编号", width = 15)
    @ApiModelProperty(value = "活动编号")
	private java.lang.String lottoNo;
	/**活动封面图片地址*/
	@Excel(name = "活动封面图片地址", width = 15)
    @ApiModelProperty(value = "活动封面图片地址")
	private java.lang.String banner;
	/**活动类型*/
	@Excel(name = "活动类型", width = 15)
    @ApiModelProperty(value = "活动类型")
	private java.lang.Integer lottoType;
	/**赞助名称*/
	@Excel(name = "赞助名称", width = 15)
    @ApiModelProperty(value = "赞助名称")
	private java.lang.String nickName;
	/**微信号*/
	@Excel(name = "微信号", width = 15)
    @ApiModelProperty(value = "微信号")
	private java.lang.String wxNo;
	/**头像地址*/
	@Excel(name = "头像地址", width = 15)
	@ApiModelProperty(value = "头像地址")
	private java.lang.String avatarUrl;
	/**微信名称*/
	@Excel(name = "微信名称", width = 15)
    @ApiModelProperty(value = "微信名称")
	private java.lang.String wxName;
	/**开奖类型 0 自动 1人数 2 手动*/
	@Excel(name = "开奖类型 0 自动 1人数 2 手动", width = 15)
    @ApiModelProperty(value = "开奖类型 0 自动 1人数 2 手动")
	private java.lang.Integer openType;
	/**是否置顶0否1 是*/
	@Excel(name = "是否置顶0否1 是", width = 15)
    @ApiModelProperty(value = "是否置顶0否1 是")
	private java.lang.Integer isTop;
	/**是否首页查询*/
	@Excel(name = "是否首页查询", width = 15)
    @ApiModelProperty(value = "是否首页查询")
	private java.lang.Integer isShow;
	/**分享次数*/
	@Excel(name = "分享次数", width = 15)
    @ApiModelProperty(value = "分享次数")
	private java.lang.Integer shareNum;
	/**参与人数*/
	@Excel(name = "参与人数", width = 15)
    @ApiModelProperty(value = "参与人数")
	private java.lang.Integer joinNum;
	/**浏览量*/
	@Excel(name = "浏览量", width = 15)
    @ApiModelProperty(value = "浏览量")
	private java.lang.Integer viewNum;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private java.lang.String tel;
	/**是否内定 0 否*/
	@Excel(name = "是否内定 0 否", width = 15)
    @ApiModelProperty(value = "是否内定 0 否")
	private java.lang.Integer isGod;

	/** 是否口令*/
	private java.lang.Integer isCommand;
	/**口令内容*/
	private java.lang.String command;


	/**开奖人数*/
	@Excel(name = "开奖人数", width = 15)
    @ApiModelProperty(value = "开奖人数")
	private java.lang.Integer openNum;
	/**是否加入机器人 0 否*/
	@Excel(name = "是否加入机器人 0 否", width = 15)
    @ApiModelProperty(value = "是否加入机器人 0 否")
	private java.lang.Integer isJoinRobot;
	/**是否担保0 否*/
	@Excel(name = "是否担保0 否", width = 15)
    @ApiModelProperty(value = "是否担保0 否")
	private java.lang.Integer isConfirm;
	/**活动结束时间*/
	@Excel(name = "活动结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动结束时间")
	private java.util.Date endDate;
	/**活动开始时间*/
	@Excel(name = "活动开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动开始时间")
	private java.util.Date startDate;
	/**开奖时间*/
	@Excel(name = "开奖时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开奖时间")
	private java.util.Date openTime;
	/*** 是否已结束*/
	private java.lang.Integer isEnd;
	/**状态 0 删除 1 正常 2 违规*/
	@Excel(name = "状态 0 删除 1 正常 2 违规", width = 15)
    @ApiModelProperty(value = "状态 0 删除 1 正常 2 违规")
	private java.lang.Integer status;
	/**createBy*/
	@Excel(name = "createBy", width = 15)
    @ApiModelProperty(value = "createBy")
	private java.lang.String createBy;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
	private java.util.Date createTime;
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
}
