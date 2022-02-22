package org.jeecg.modules.task.vo;

import java.io.Serializable;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/**
 * @author Administrator
 *
 * */
public class RunningQuestionVO implements Serializable {
	

    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**测试用例主键*/
    /**@Excel (name = "测试用例主键", width = 15)*/
    @ApiModelProperty(value = "测试用例主键")
    private String caseId;
	/**问题标识*/
	@Excel(name = "问题标识", width = 15)
    @ApiModelProperty(value = "问题标识")
    private String questionCode;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private String taskName;
	/**测试用例名称*/
	@Excel(name = "所属项目", width = 15)
    @ApiModelProperty(value = "所属项目")
    private String projectName;
	/**测试用例名称*/
	@Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private String caseName;
	/**软件版本*/
	@Excel(name = "软件版本", width = 15)
    @ApiModelProperty(value = "软件版本")
    private String questionVersion;
	/**问题类别*/
	@Excel(name = "问题类别", width = 15)
	@Dict(dicCode = "questionType")
    @ApiModelProperty(value = "问题类别")
    private String questionType;
	/**问题级别*/
	@Excel(name = "问题级别", width = 15)
	@Dict(dicCode = "questionLevel")
    @ApiModelProperty(value = "问题级别")
    private String questionLevel;
	/**问题个数*/
	@Excel(name = "问题个数", width = 15)
    @ApiModelProperty(value = "问题个数")
    private String questionNumber;
	/**问题位置*/
	@Excel(name = "问题位置", width = 15)
    @ApiModelProperty(value = "问题位置")
    private String questionLocation;
	/**问题描述*/
	@Excel(name = "问题描述", width = 15)
    @ApiModelProperty(value = "问题描述")
    private String questionDescription;
	/**设计师意见*/
	@Excel(name = "设计师意见", width = 15)
    @ApiModelProperty(value = "设计师意见")
    private String opinion;
	/**报告人*/
	@Excel(name = "报告人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "报告人")
    private String reporter;
	/**报告日期*/
	@Excel(name = "报告日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "报告日期")
    private java.util.Date reportDate;
	
	@ApiModelProperty(value = "删除标记")
	private Integer delFlag;
	
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	
    @TableField(exist = false)
    private Boolean createrFalg;

    @ApiModelProperty(value = "测试步骤序号")
    private String caseStepId;

    /** 归档状态,0表示未归档,1表示已归档 */
    @Excel(name = "归档状态", width = 15)
    @ApiModelProperty(value = "归档状态")
    private Integer fileStatus;
}