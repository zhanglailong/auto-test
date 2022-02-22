package org.jeecg.modules.scriptandplan.entity;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 方案脚本对应表
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Data
@TableName("auto_script_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="auto_script_plan对象", description="方案脚本对应表")
public class AutoScriptPlan implements Serializable {
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
	/**脚本名称*/
	@Excel(name = "脚本名称", width = 15)
    @ApiModelProperty(value = "脚本名称")
    private java.lang.String scriptName;
	/**录制节点名称*/
	@Excel(name = "录制节点名称", width = 15)
    @ApiModelProperty(value = "录制节点名称")
    private java.lang.String recordNodeName;
	/**执行节点名称*/
	@Excel(name = "执行节点名称", width = 15)
    @ApiModelProperty(value = "执行节点名称")
    private java.lang.String executeNodeName;
	/**录制节点客户端ip*/
	@Excel(name = "录制节点客户端ip", width = 15)
    @ApiModelProperty(value = "录制节点客户端ip")
    private java.lang.String recordNodeIp;
	/**执行节点客户端ip*/
	@Excel(name = "执行节点客户端ip", width = 15)
    @ApiModelProperty(value = "执行节点客户端ip")
    private java.lang.String executeNodeIp;
	/**脚本内容-文本的hash*/
	@Excel(name = "脚本内容-文本的hash", width = 15)
    @ApiModelProperty(value = "脚本内容-文本的hash")
    private java.lang.String scriptContent;
	/**权重默认0*/
	@Excel(name = "权重默认0", width = 15)
    @ApiModelProperty(value = "权重默认0")
    private java.lang.Integer weight;
	/**报告文件-hash*/
	@Excel(name = "报告文件-hash", width = 15)
    @ApiModelProperty(value = "报告文件-hash")
    private java.lang.String report;
	/**方案名称*/
	@Excel(name = "方案名称", width = 15)
    @ApiModelProperty(value = "方案名称")
    private java.lang.String planName;
	/**方案主键*/
	@Excel(name = "方案主键", width = 15)
    @ApiModelProperty(value = "方案主键")
    private java.lang.String planId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String state;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**虚拟删除*/
	@Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.Integer idel;
	/**脚本id外键*/
	@Excel(name = "脚本id外键", width = 15)
    @ApiModelProperty(value = "脚本id外键")
    private java.lang.String oldScriptId;
    /**录制节点id*/
    @Excel(name = "录制节点id", width = 15)
    @ApiModelProperty(value = "录制节点id")
    private java.lang.String recordNodeId;

    /**排序字段*/
    @Excel(name = "排序字段", width = 15)
    @ApiModelProperty(value = "排序字段")
    private java.lang.Integer sort;

    /**测试用例名称*/
    @Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private java.lang.String testCaseName;
    /**测试用例编号*/
    @Excel(name = "测试用例编号", width = 15)
    @ApiModelProperty(value = "测试用例编号")
    private java.lang.String testCaseId;
}
