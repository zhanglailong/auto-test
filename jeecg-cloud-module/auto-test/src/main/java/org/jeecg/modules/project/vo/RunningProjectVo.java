package org.jeecg.modules.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/**
 * @Author: test
 * */
public class RunningProjectVo {

    /**
     * 开始时间
     */
    private String beginDate;

    /**
     * 结束时间
     */
    private String finishDate;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目代码
     */
    private String projectCode;

    /**
     * 被测对象名称
     */
    private String uutName;

    /**
     * 被测对象类型
     */
    private String rUutType;

    /**
     * 项目成员
     */
    private String projectMembers;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务代码
     */
    private String taskCode;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 标题
     */
    private String title;

    /**
     * 申请类型
     */
    private String mUutType;

    /**
     * 实例id
     */
    private String instanceId;

    /**
     * 审批人
     */
    private String assignee;

    /**
     * 转办人
     */
    private String owner;

    /**
     * 当前用户
     */
    private String createBy;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;
}
