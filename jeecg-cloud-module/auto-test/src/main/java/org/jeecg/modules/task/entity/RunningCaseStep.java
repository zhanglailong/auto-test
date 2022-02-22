package org.jeecg.modules.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("running_case_step")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_case_step对象", description="测试步骤表")
/**
* @Author chop
* */
public class RunningCaseStep implements Serializable {
    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**任务id*/
    @ApiModelProperty(value = "任务id")
    private String caseId;
    /**操作步骤id*/
    /** @Excel (name = "操作步骤id", width = 15)*/
    @ApiModelProperty(value = "序号")
    private String stepId;
    /*测试步骤*/
    /**输入及操作说明*/
    /** @Excel (name = "输入及操作说明", width = 15)*/
    @ApiModelProperty(value = "输入及操作说明")
    private String operatingInstructions;
    /**期望测试结果*/
    /** @Excel (name = "期望测试结果", width = 15)*/
    @ApiModelProperty(value = "期望测试结果")
    private String expectResult;
    /**测试结果*/
    @ApiModelProperty(value = "测试结果")
    private String testResult;


    @ApiModelProperty(value = "测试激励类型ID")
    private String impelTypeId;

    @ApiModelProperty(value = "实测结果")
    private String realTestResult;

    @ApiModelProperty(value = "问题报告单ID")
    private String questionId;

    @ApiModelProperty(value = "轮次ID")
    private String turnId;

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

    @ApiModelProperty(value = "期望结果")
    private String wishResult;

    @ApiModelProperty(value = "评估标准")
    private String evaluationCriteria;






}
