package com.mars.modules.system.entity;

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
 * @Description: cdn配置
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Data
@TableName("sys_cdn_config")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_cdn_config对象", description="cdn配置")
public class CdnConfig {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**空间名称*/
	@Excel(name = "空间名称", width = 15)
    @ApiModelProperty(value = "空间名称")
	private java.lang.String bucketName;
	/**accessKey*/
	@Excel(name = "accessKey", width = 15)
    @ApiModelProperty(value = "accessKey")
	private java.lang.String accessKey;
	/**secretKey*/
	@Excel(name = "secretKey", width = 15)
    @ApiModelProperty(value = "secretKey")
	private java.lang.String secretKey;
	/**云空间类型（0 七牛云）*/
	@Excel(name = "云空间类型（0 七牛云）", width = 15)
    @ApiModelProperty(value = "云空间类型（0 七牛云）")
	private java.lang.Integer type;
	/**delFlag*/
	@Excel(name = "delFlag", width = 15)
    @ApiModelProperty(value = "delFlag")
	private java.lang.Integer delFlag;
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
