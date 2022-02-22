package org.jeecg.modules.task.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2020-12-25
 * @Version: V1.0
 */
@Data
@TableName("running_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_task对象", description="任务管理")
public class RunningTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**项目ID*/
    /** @Excel (name = "项目ID", width = 15)*/
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private String taskName;
	/**任务标识*/
	@Excel(name = "任务标识", width = 15)
    @ApiModelProperty(value = "任务标识")
    private String taskCode;
	/**任务类型*/
    @ApiModelProperty(value = "任务类型")
    private String taskType;
	/**所属项目*/
	@Excel(name = "所属项目", width = 15, dictTable = "running_project", dicText = "project_name", dicCode = "project_name")
	@Dict(dictTable = "running_project", dicText = "id", dicCode = "project_name")
    @ApiModelProperty(value = "所属项目")
    private String projectInfo;
	/**任务负责人*/
	@Excel(name = "任务负责人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "任务负责人")
    private String taskPrincipal;
	/**测试工具*/
   /**@Excel (name = "测试工具", width = 15, dictTable = "running_tools_list", dicText = "tools_name", dicCode = "tools_name")*/
	@Dict(dicCode = "toolsName")
    @ApiModelProperty(value = "测试工具")
    private String taskTool;

	/**被测软件名称*/
	@Excel(name = "被测软件名称", width = 15)
    @ApiModelProperty(value = "被测软件名称")
    private String taskSoftName;
	/**被测软件版本*/
	@Excel(name = "被测软件版本", width = 15)
    @ApiModelProperty(value = "被测软件版本")
    private String taskSoftVersion;
	/**被测软件类型*/
	@Excel(name = "被测软件类型", width = 15)
    @ApiModelProperty(value = "被测软件类型")
    private String taskSoftType;
	/**资产库详情*/
	@Excel(name = "资产库详情", width = 15)
    @ApiModelProperty(value = "资产库详情")
    private String taskAssetsDetail;
	/**附件上传*/
	@Excel(name = "附件上传", width = 15)
    @ApiModelProperty(value = "附件上传")
    private String taskSoftFile;
	/**选择资产库*/
    @ApiModelProperty(value = "选择资产库")
    private String taskAssetsId;
	/**任务数据*/
    /**@Excel (name = "任务数据", width = 15)*/
    @ApiModelProperty(value = "任务数据")
    private String taskData;
	/**任务任务状态*/
	@Excel(name = "任务状态", width = 15,dicCode = "taskStatus")
	@Dict(dicCode = "taskStatus")
    @ApiModelProperty(value = "任务状态") 
    private String taskStatus;
	
	/**附件*/
    @ApiModelProperty(value = "附件")
    private String cuFile;
	
	/**开始时间*/
    @Excel(name = "开始时间", width = 15,format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date beginDate;
	/**结束时间*/
    @Excel(name = "结束时间", width = 15,format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date finishDate;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;
    
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    
    @TableField(exist = false)
    private String state;
    
    @TableField(exist = false)
    private List<RunningTaskHistory> modifiedList;
    @TableField(exist = false)
    private Boolean isModified;

    /**测试轮次*/
    @ApiModelProperty(value = "测试轮次")
    private String turnId;

    /**测试类型*/
    @Dict(dicCode = "testType")
    @ApiModelProperty(value = "测试类型")
    private String testType;

    @ApiModelProperty(value = "轮次编号")
    @TableField(exist = false)
    private String turnNum;

    /**测试版本*/
    @ApiModelProperty(value = "轮次编号")
    private String turnVersion;






    @ApiModelProperty(value = "简写码")
    private String abbreviatedCode;

    @ApiModelProperty(value = "测试项说明及测试要求")
    private String taskExplain;

    @ApiModelProperty(value = "测试方法")
    private String taskMethod;

    @ApiModelProperty(value = "追踪关系")
    private String trackRelationship;

    @ApiModelProperty(value = "终止条件")
    private String terminationConditions;

    @ApiModelProperty(value = "优先级")
    private String priority;

    @ApiModelProperty(value = "下属测试用例说明")
    private String taskCaseExplain;

    @ApiModelProperty(value = "所属测试类型ID")
    private String testTypeId;

    @ApiModelProperty(value = "父节点ID")
    private String parentId;

    @ApiModelProperty(value = "预计工作时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expectWorkTime;

    @ApiModelProperty(value = "测试项类型ID")
    private String taskTypeId;

    @ApiModelProperty(value = "前向ID")
    private String frontId;

    @ApiModelProperty(value = "审核人")
    private String reviewer;

    @ApiModelProperty(value = "审核结论")
    private String reviewConclusion;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    @ApiModelProperty(value = "审核状态")
    private String reviewState;

    @ApiModelProperty(value = "审核备注")
    private String reviewRemark;

    @ApiModelProperty(value = "预留字段1")
    private String reserveField1;

    @ApiModelProperty(value = "预留字段2")
    private String reserveField2;

    @ApiModelProperty(value = "预留字段3")
    private String reserveField3;

    @ApiModelProperty(value = "预留字段4")
    private String reserveField4;

    @ApiModelProperty(value = "预留字段5")
    private String reserveField5;

    @ApiModelProperty(value = "预留字段6")
    private String reserveField6;

    @ApiModelProperty(value = "前提约束")
    private String premiseConstraint;

    @ApiModelProperty(value = "评估准则")
    private String evaluationCriteria;

    @ApiModelProperty(value = "测试要求")
    private String testRequirement;

    @ApiModelProperty(value = "覆盖的测试要求")
    private String coverTestRequirement;

    /** 归档状态,0表示未归档,1表示已归档 */
    @Excel(name = "归档状态", width = 15)
    @ApiModelProperty(value = "归档状态")
    private Integer fileStatus;

    /** 归档标记0逻辑删除1想要归档 */
    @Excel(name = "归档标记", width = 15)
    @ApiModelProperty(value = "归档标记")
    private Integer fileSign;



}
