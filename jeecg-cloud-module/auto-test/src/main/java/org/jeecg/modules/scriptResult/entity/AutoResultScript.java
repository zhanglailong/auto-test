package org.jeecg.modules.scriptResult.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 方案下脚本报告
 * @Author: jeecg-boot
 * @Date:   2021-09-02
 * @Version: V1.0
 */
@Data
@TableName("auto_result_script")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="auto_result_script对象", description="方案下脚本报告")
public class AutoResultScript implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**方案名称*/
	@Excel(name = "方案名称", width = 15)
    @ApiModelProperty(value = "方案名称")
    private java.lang.String planName;
	/**方案id*/
	@Excel(name = "方案id", width = 15)
    @ApiModelProperty(value = "方案id")
    private java.lang.String planId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String state;
	/**测试结果*/
	@Excel(name = "测试结果", width = 15)
    @ApiModelProperty(value = "测试结果")
    private java.lang.String result;
	/**报告文件-hash*/
	@Excel(name = "报告文件-hash", width = 15)
    @ApiModelProperty(value = "报告文件-hash")
    private java.lang.String report;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**虚拟删除*/
	@Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.String idel;
	/**脚本id*/
	@Excel(name = "脚本id", width = 15)
    @ApiModelProperty(value = "脚本id")
    private java.lang.String autoScriptId;
	/**脚本名称*/
	@Excel(name = "脚本名称", width = 15)
    @ApiModelProperty(value = "脚本名称")
    private java.lang.String autoScriptName;
    /**方案报告id*/
    @Excel(name = "方案报告id", width = 15)
    @ApiModelProperty(value = "方案报告id")
    private java.lang.String planResultId;
    /**脚本报告文件的url*/
    @Excel(name = "脚本报告文件的url", width = 15)
    @ApiModelProperty(value = "脚本报告文件的url")
    private java.lang.String scriptResultUrl;


}
