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
 * @Description: 微信小程序formId
 * @Author: jeecg-boot
 * @Date:   2019-09-13
 * @Version: V1.0
 */
@Data
@TableName("wx_form_id")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wx_form_id对象", description="微信小程序formId")
public class WxFormId {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**微信编号*/
	@Excel(name = "微信编号", width = 15)
    @ApiModelProperty(value = "微信编号")
	private java.lang.String wxno;
	/**openId*/
	@Excel(name = "openId", width = 15)
    @ApiModelProperty(value = "openId")
	private java.lang.String openId;
	/**formId*/
	@Excel(name = "formId", width = 15)
    @ApiModelProperty(value = "formId")
	private java.lang.String formId;
	/**是否使用*/
	@Excel(name = "是否使用", width = 15)
    @ApiModelProperty(value = "是否使用")
	private java.lang.Integer isUsed;
	/**是否使用*/
	@Excel(name = "是否使用", width = 15)
	@ApiModelProperty(value = "是否过期")
	private java.lang.Integer state;
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
