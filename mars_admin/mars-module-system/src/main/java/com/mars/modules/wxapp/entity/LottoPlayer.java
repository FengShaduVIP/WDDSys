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
 * @Description: 微信小程序参与人员表
 * @Author: jeecg-boot
 * @Date:   2019-08-25
 * @Version: V1.0
 */
@Data
@TableName("wx_player")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wx_player对象", description="微信小程序参与人员表")
public class LottoPlayer {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**lottoId*/
	@Excel(name = "lottoId", width = 15)
	@ApiModelProperty(value = "openId")
	private java.lang.String openId;
	/**lottoId*/
	@Excel(name = "lottoId", width = 15)
	@ApiModelProperty(value = "wxNo")
	private java.lang.String wxNo;
	/**lottoId*/
	@Excel(name = "lottoId", width = 15)
    @ApiModelProperty(value = "lottoId")
	private java.lang.String lottoId;
	/**lottoNo*/
	@Excel(name = "lottoNo", width = 15)
    @ApiModelProperty(value = "lottoNo")
	private java.lang.String lottoNo;
	/**prizeId*/
	@Excel(name = "prizeId", width = 15)
    @ApiModelProperty(value = "prizeId")
	private java.lang.String prizeId;
	/**微信名称*/
	@Excel(name = "微信名称", width = 15)
	@ApiModelProperty(value = "微信名称")
	private java.lang.String nickName;
	/**头像地址*/
	@Excel(name = "头像地址", width = 15)
	@ApiModelProperty(value = "头像地址")
	private java.lang.String avatarUrl;
	/**isWin*/
	@Excel(name = "isWin", width = 15)
    @ApiModelProperty(value = "isWin")
	private java.lang.Integer isWin;
	/**isUsed*/
	@Excel(name = "isWin", width = 15)
	@ApiModelProperty(value = "是否使用卡券")
	private java.lang.Integer isUsed;
	/**卡券类型*/
	@Excel(name = "type", width = 15)
	@ApiModelProperty(value = "卡券类型 0 实体")
	private java.lang.Integer type;
	/**isRobot*/
	@Excel(name = "isRobot", width = 15)
    @ApiModelProperty(value = "isRobot")
	private java.lang.Integer isRobot;
	/**prizeName*/
	@Excel(name = "prizeName", width = 15)
	@ApiModelProperty(value = "prizeName")
	private java.lang.String prizeName;
	/**prizeImg*/
	@Excel(name = "prizeImg", width = 15)
    @ApiModelProperty(value = "prizeImg")
	private java.lang.String prizeImg;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
	private java.lang.String name;
	/**tel*/
	@Excel(name = "tel", width = 15)
    @ApiModelProperty(value = "tel")
	private java.lang.String tel;
	/**address*/
	@Excel(name = "address", width = 15)
    @ApiModelProperty(value = "address")
	private java.lang.String address;
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
