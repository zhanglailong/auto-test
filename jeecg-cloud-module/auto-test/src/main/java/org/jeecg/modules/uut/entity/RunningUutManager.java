package org.jeecg.modules.uut.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 被测对象流程任务表
 * @Author: jeecg-boot
 * @Date:   2020-12-27
 * @Version: V1.0
 */
@Data
@TableName("running_uut_manager")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_manager对象", description="被测对象流程任务表")
public class RunningUutManager implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**
	  * 申请类型
	 * 0出库 1入库 2更动
	 * */
	@Excel(name = "申请类型", width = 15, dicCode = "uutType")
	@Dict(dicCode = "uutType")
    @ApiModelProperty(value = "申请类型")
    private String uutType;
	/**实例id*/
	@Excel(name = "实例id", width = 15)
    @ApiModelProperty(value = "实例id")
    private String instanceId;
	/**节点id*/
	@Excel(name = "节点id", width = 15)
    @ApiModelProperty(value = "节点id")
    private String nodeId;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private String title;
	/**发起人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "发起人")
    private String createBy;
	/**发起时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发起时间")
    private Date createTime;
	/**审批人*/
	@Excel(name = "审批人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "审批人")
    private String assignee;
	/**转办人*/
	@Excel(name = "转办人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "转办人")
    private String owner;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审批时间")
    private Date finishTime;
	/**审批结果*/
	@Excel(name = "审批结果", width = 15)
    @ApiModelProperty(value = "审批结果")
    private String resultType;
	/**
	 * 审批状态
	 * 0：未结 1：正常结束 2：人工终止
	 * */
	@Dict(dicCode = "uutManagerStatus")
	@Excel(name = "审批状态", width = 15)
    @ApiModelProperty(value = "审批状态")
    private Integer status;
	/**
	 * 节点类型
	 *  start：开始 copy：抄送 vote：会签 normal：普通 back：驳回 delete：删除
	 * */
	@Excel(name = "节点类型", width = 15)
    @ApiModelProperty(value = "节点类型")
    private String catalog;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;

	/**被测对象id*/
	@Excel(name = "被测对象id", width = 15)
    @ApiModelProperty(value = "被测对象id")
    private String uutListId;
	/**项目标识*/
	@Excel(name = "项目标识", width = 15)
    @ApiModelProperty(value = "项目标识")
    private String projectCode;

	/**
	 * 时长
	 */
	@TableField(exist = false)
	private String duration;

	public RunningUutManager(String uutType, String assignee, String title, Date createTime,String comment,String createBy,Date finishTime,String instanceId,String nodeId,String resultType,String uutListId,String projectCode,String catalog) {
		this.setUutType(uutType);
		this.setAssignee(assignee);
		this.setTitle(title);
		this.setCreateTime(createTime);
		this.setComment(comment);
		this.setCreateBy(createBy);
		this.setFinishTime(finishTime);
		this.setInstanceId(instanceId);
		this.setNodeId(nodeId);
		this.setResultType(resultType);
		this.setUutListId(uutListId);
		this.setProjectCode(projectCode);
		this.setCatalog(catalog);
	}

	public RunningUutManager() {
	}
}
