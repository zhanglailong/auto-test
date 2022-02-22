package org.jeecg.modules.project.vo;

import lombok.Data;

@Data
public class FileProgressData {

    // 需归档测试项目总数
    private Integer totalProjectNum;

    // 已归档测试项目数量
    private Integer filedProjectNum;

    // 归档测试项目进度百分比
    private Double filedProjectPercent;

    // 需归档测试项总数
    private Integer totalTaskNum;

    // 已归档测试项数量
    private Integer filedTaskNum;

    // 已归测试项进度百分比
    private Double filedTaskPercent;

    // 需归档测试用例总数
    private Integer totalCaseNum;

    // 已归档测试用例数量
    private Integer filedCaseNum;

    // 已归档测试用例进度百分比
    private Double filedCasePercent;

    // 需归档问题单总数
    private Integer totalQuestionNum;

    // 已归档问题单数量
    private Integer filedQuestionNum;

    // 已归档问题单进度百分比
    private Double filedQuestionPercent;

    private String message;



}
