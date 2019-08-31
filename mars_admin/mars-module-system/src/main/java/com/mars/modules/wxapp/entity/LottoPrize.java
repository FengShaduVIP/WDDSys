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
 * @Description: 微信小程序抽奖_奖品表
 * @Author: jeecg-boot
 * @Date:   2019-08-24
 * @Version: V1.0
 */
@Data
@TableName("wx_prize")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wx_prize对象", description="微信小程序抽奖_奖品表")
public class LottoPrize {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**活动编号*/
	@Excel(name = "活动编号", width = 15)
    @ApiModelProperty(value = "活动编号")
	private java.lang.String lottoNo;
	/**活动ID*/
	@Excel(name = "活动ID", width = 15)
    @ApiModelProperty(value = "活动ID")
	private java.lang.String lottoId;
	/**奖品名称*/
	@Excel(name = "奖品名称", width = 15)
    @ApiModelProperty(value = "奖品名称")
	private java.lang.String name;
	/**奖品图片*/
	@Excel(name = "奖品图片", width = 15)
    @ApiModelProperty(value = "奖品图片")
	private java.lang.String imgUrl;
	/**抽奖概率*/
	@Excel(name = "抽奖概率", width = 15)
    @ApiModelProperty(value = "抽奖概率")
	private java.lang.String chance;
	/**奖品类型*/
	@Excel(name = "奖品类型", width = 15)
    @ApiModelProperty(value = "奖品类型")
	private java.lang.Integer type;
	/**奖品数量*/
	@Excel(name = "奖品数量", width = 15)
    @ApiModelProperty(value = "奖品数量")
	private java.lang.Integer num;
	/**奖品剩余数量*/
	@Excel(name = "奖品剩余数量", width = 15)
    @ApiModelProperty(value = "奖品剩余数量")
	private java.lang.Integer leftNum;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.lang.Integer sort;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
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
