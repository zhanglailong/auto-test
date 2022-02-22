package org.jeecg.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("running_case_step_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="running_case_step对象", description="测试步骤历史表")
public class RunningCaseStepHistory /*implements Serializable*/extends RunningCaseStep {
    /**主键*/
/*    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    *//**任务id*//*
    @ApiModelProperty(value = "任务id")
    private java.lang.String caseId;
    *//**操作步骤id*//*
    @ApiModelProperty(value = "序号")
    private java.lang.String stepId;
    *//*测试步骤*//*
    *//**输入及操作说明*//*
    @ApiModelProperty(value = "输入及操作说明")
    private java.lang.String operatingInstructions;
    *//**期望测试结果*//*
    @ApiModelProperty(value = "期望测试结果")
    private java.lang.String expectResult;*/

    @ApiModelProperty(value = "操作类型")
    private String opType;


//    @ApiModelProperty(value = "测试结果")
//    private String testResult;
//
//    @ApiModelProperty(value = "测试激励类型ID")
//    private String impelTypeId;
//
//    @ApiModelProperty(value = "实测结果")
//    private String realTestResult;
//
//    @ApiModelProperty(value = "问题报告单ID")
//    private String questionId;
//
//    @ApiModelProperty(value = "轮次ID")
//    private String turnId;
//
//    @ApiModelProperty(value = "预留字段1")
//    private String reserveField1;
//
//    @ApiModelProperty(value = "预留字段2")
//    private String reserveField2;
//
//    @ApiModelProperty(value = "预留字段3")
//    private String reserveField3;
//
//    @ApiModelProperty(value = "预留字段4")
//    private String reserveField4;
//
//    @ApiModelProperty(value = "预留字段5")
//    private String reserveField5;
//
//    @ApiModelProperty(value = "期望结果")
//    private String wishResult;
//
//    @ApiModelProperty(value = "评估标准")
//    private String evaluationCriteria;

}
