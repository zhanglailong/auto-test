package org.jeecg.modules.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.task.entity.RunningQuestionHistory;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("running_uut_file_question")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_file_question对象", description="问题单归档")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RunningUutFileQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**测试用例主键*/
    @Excel(name = "测试用例主键", width = 15)
    @ApiModelProperty(value = "测试用例主键")
    private String caseId;
    /**任务ID，表中实际没有该字段*/
    @TableField(exist = false)
    private String taskId;
    /**任务名称，表中实际没有该字段*/
    @TableField(exist = false)
    private String taskName;
    /**项目名称，表中实际没有该字段*/
    @TableField(exist = false)
    private String projectName;
    /**测试用例名称，表中实际没有该字段*/
    @TableField(exist = false)
    private String caseName;
    /**问题标识*/
    @Excel(name = "问题标识", width = 15)
    @ApiModelProperty(value = "问题标识")
    private String questionCode;
    /**软件版本*/
    @Excel(name = "软件版本", width = 15)
    @ApiModelProperty(value = "软件版本")
    private String questionVersion;
    /**问题类别*/
    @Excel(name = "问题类别", width = 15)
    /** @Dict (dicCode = "questionType")*/
    @ApiModelProperty(value = "问题类别")
    private String questionType;
    /**问题级别*/
    @Excel(name = "问题级别", width = 15)
    /** @Dict (dicCode = "questionLevel")*/
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
    private Date reportDate;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;
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
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**是否解决*/
    @ApiModelProperty(value = "是否解决")
    private String status;


    @TableField(exist = false)
    /**编辑操作用到这个节点，保存修改过的字段名称和字段值*/
    private List<RunningQuestionHistory> modifiedList;






    @ApiModelProperty(value = "问题名称")
    private String questionName;

    @ApiModelProperty(value = "问题状态")
    private String questionState;

    @ApiModelProperty(value = "发现日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date discoverTime;

    @ApiModelProperty(value = "程序文档名")
    private String programFileName;

    @ApiModelProperty(value = "问题追踪")
    private String questionTract;

    @ApiModelProperty(value = "附注及修改建议")
    private String annotationsModify;

    @ApiModelProperty(value = "一级标识")
    private String oneLevelIdentification;

    @ApiModelProperty(value = "二级标识")
    private String twoLevelIdentification;

    @ApiModelProperty(value = "三级标识")
    private String threeLevelIdentification;

    @ApiModelProperty(value = "四级标识")
    private String fourLevelIdentification;

    @ApiModelProperty(value = "所属被测对象")
    private String uutCode;

    @ApiModelProperty(value = "审核人")
    private String reviewer;

    @ApiModelProperty(value = "审核结果")
    private String reviewResult;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    @ApiModelProperty(value = "审核状态")
    private String reviewState;

    @ApiModelProperty(value = "审核备注")
    private String reviewRemark;

    @ApiModelProperty(value = "轮次ID")
    private String turnId;

    @ApiModelProperty(value = "备用1")
    private String reserve1;

    @ApiModelProperty(value = "备用2")
    private String reserve2;

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

    @ApiModelProperty(value = "新老状态")
    private String newOldState;

    @ApiModelProperty(value = "运行载体")
    private String operationCarrier;

    @ApiModelProperty(value = "具体失效形式")
    private String specificFailureForm;

    @ApiModelProperty(value = "预留字段6")
    private String reserveField6;

    @ApiModelProperty(value = "预留字段7")
    private String reserveField7;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "问题形式")
    private String questionForm;

    @ApiModelProperty(value = "测试阶段")
    private String testStage;

    @ApiModelProperty(value = "问题子类别")
    private String questionSubtype;

    @ApiModelProperty(value = "处置人员")
    private String handlePerson;

    @ApiModelProperty(value = "审批领导")
    private String approveLeader;

    @ApiModelProperty(value = "处置日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    @ApiModelProperty(value = "审批日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    @ApiModelProperty(value = "修改状态")
    private String modifyState;

    @ApiModelProperty(value = "测试步骤序号")
    private String caseStepId;

    /** 归档状态,0表示未归档,1表示已归档 */
    @Excel(name = "归档状态", width = 15)
    @ApiModelProperty(value = "归档状态")
    private Integer fileStatus;

}
