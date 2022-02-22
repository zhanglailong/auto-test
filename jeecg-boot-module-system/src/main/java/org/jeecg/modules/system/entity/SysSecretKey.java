package org.jeecg.modules.system.entity;

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

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 系统密钥分发表
 * @Author: jeecg-boot
 * @Date:   2021-01-29
 * @Version: V1.0
 */
@Data
@TableName("sys_secret_key")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_secret_key对象", description="系统密钥分发表")
public class SysSecretKey implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**token*/
	@Excel(name = "token", width = 15)
    @ApiModelProperty(value = "token")
    private String token;
	/**密钥*/
	@Excel(name = "私钥", width = 15)
    @ApiModelProperty(value = "私钥")
    private String secretKey;
	/**系统名称*/
	@Excel(name = "系统名称", width = 15)
    @ApiModelProperty(value = "系统名称")
    private String sysName;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**添加用户接口*/
	@Excel(name = "添加用户接口", width = 15)
    @ApiModelProperty(value = "添加用户接口")
    private String apiAddUrl;
	/**更新用户接口*/
	@Excel(name = "更新用户接口", width = 15)
    @ApiModelProperty(value = "更新用户接口")
    private String apiUpdateUrl;
	/**删除用户接口*/
	@Excel(name = "删除用户接口", width = 15)
    @ApiModelProperty(value = "删除用户接口")
    private String apiDeleteUrl;
    /**公钥*/
    @Excel(name = "公钥", width = 15)
    @ApiModelProperty(value = "公钥")
    private String publicKey;
}
