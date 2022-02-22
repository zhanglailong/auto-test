package org.jeecg.modules.autoResult.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: autoTools
 * @description: vo
 * @author: ZHANG RUI
 * @create: 2021-09-02-11-22
 */
public class AutoResultVo implements Serializable {
    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**id*/
    @Excel(name = "脚本id", width = 15)
    @ApiModelProperty(value = "脚本id")
    private java.lang.String autoResultId;
    /**脚本id*/
    @Excel(name = "脚本id", width = 15)
    @ApiModelProperty(value = "脚本id")
    private java.lang.String autoScriptId;
    /**脚本名称*/
    @Excel(name = "脚本名称", width = 15)
    @ApiModelProperty(value = "脚本名称")
    private java.lang.String autoScriptName;
    /**节点id*/
    @Excel(name = "节点id", width = 15)
    @ApiModelProperty(value = "节点id")
    private java.lang.String recordNodeId;
    /**节点名称*/
    @Excel(name = "节点名称", width = 15)
    @ApiModelProperty(value = "节点名称")
    private java.lang.String nodeName;
    /**项目名称*/
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
    /**项目id*/
    @Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;
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
    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer state;
    /**测试结果*/
    @Excel(name = "测试结果", width = 15)
    @ApiModelProperty(value = "测试结果")
    private java.lang.Integer result;

    public AutoResultVo() {
    }

    public AutoResultVo(String id, String autoResultId, String autoScriptId, String autoScriptName, String recordNodeId, String nodeName, String projectName, String projectId, String createBy, Date createTime, String updateBy, Date updateTime, Integer state, Integer result) {
        this.id = id;
        this.autoResultId = autoResultId;
        this.autoScriptId = autoScriptId;
        this.autoScriptName = autoScriptName;
        this.recordNodeId = recordNodeId;
        this.nodeName = nodeName;
        this.projectName = projectName;
        this.projectId = projectId;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.state = state;
        this.result = result;
    }

    @Override
    public String toString() {
        return "AutoResultVo{" +
                "id='" + id + '\'' +
                ", autoResultId='" + autoResultId + '\'' +
                ", autoScriptId='" + autoScriptId + '\'' +
                ", autoScriptName='" + autoScriptName + '\'' +
                ", recordNodeId='" + recordNodeId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", state=" + state +
                ", result=" + result +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutoResultId() {
        return autoResultId;
    }

    public void setAutoResultId(String autoResultId) {
        this.autoResultId = autoResultId;
    }

    public String getAutoScriptId() {
        return autoScriptId;
    }

    public void setAutoScriptId(String autoScriptId) {
        this.autoScriptId = autoScriptId;
    }

    public String getAutoScriptName() {
        return autoScriptName;
    }

    public void setAutoScriptName(String autoScriptName) {
        this.autoScriptName = autoScriptName;
    }

    public String getRecordNodeId() {
        return recordNodeId;
    }

    public void setRecordNodeId(String recordNodeId) {
        this.recordNodeId = recordNodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime(Date createTime) {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy(String updateBy) {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime(Date updateTime) {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState(Integer result) {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getResult(Integer result) {
        return this.result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
