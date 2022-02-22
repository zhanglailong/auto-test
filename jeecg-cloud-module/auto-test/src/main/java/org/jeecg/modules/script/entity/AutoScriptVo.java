package org.jeecg.modules.script.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 脚本管理
 * @Author: zll
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="auto_script对象", description="脚本管理")
public class AutoScriptVo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
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
	/**脚本名称*/
	@Excel(name = "脚本名称", width = 15)
    @ApiModelProperty(value = "脚本名称")
    private String scriptName;
	/**录制节点名称*/
	@Excel(name = "录制节点名称", width = 15)
    @ApiModelProperty(value = "录制节点名称")
    private String recordNodeName;
	/**执行节点名称*/
	@Excel(name = "执行节点名称", width = 15)
    @ApiModelProperty(value = "执行节点名称")
    private String executeNodeName;
	/**录制节点客户端ip*/
	@Excel(name = "录制节点客户端ip", width = 15)
    @ApiModelProperty(value = "录制节点客户端ip")
    private String recordNodeIp;
	/**执行节点客户端ip*/
	@Excel(name = "执行节点客户端ip", width = 15)
    @ApiModelProperty(value = "执行节点客户端ip")
    private String executeNodeIp;
	/**脚本内容-文本的hash*/
	@Excel(name = "脚本内容-文本的hash", width = 15)
    @ApiModelProperty(value = "脚本内容-文本的hash")
    private String scriptContent;
	/**权重默认0*/
	@Excel(name = "权重默认0", width = 15)
    @ApiModelProperty(value = "权重默认0")
    private Integer weight;
	/**报告文件-hash*/
	@Excel(name = "报告文件-hash", width = 15)
    @ApiModelProperty(value = "报告文件-hash")
    private String report;
	/**方案名称*/
	@Excel(name = "方案名称", width = 15)
    @ApiModelProperty(value = "方案名称")
    private String planName;
	/**方案主键*/
	@Excel(name = "方案主键", width = 15)
    @ApiModelProperty(value = "方案主键")
    private String planId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String state;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**虚拟删除*/
	@Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private Integer idel;
    /**节点id*/
    @Excel(name = "执行节点id", width = 15)
    @ApiModelProperty(value = "执行节点id")
    private String executeNodeId;
    /**录制节点id*/
    @Excel(name = "录制节点id", width = 15)
    @ApiModelProperty(value = "录制节点id")
    private String recordNodeId;
    /**节点名称*/
    @Excel(name = "节点名称", width = 15)
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    /**排序字段*/
    @Excel(name = "排序字段", width = 15)
    @ApiModelProperty(value = "排序字段")
    private java.lang.Integer sort;
    /**文件名称*/
    @Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
    private java.lang.String fileName;

    /**项目名称*/
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
    /**项目编号*/
    @Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private java.lang.String projectId;

    /**测试项名称*/
    @Excel(name = "测试项名称", width = 15)
    @ApiModelProperty(value = "测试项名称")
    private java.lang.String testItemName;
    /**测试项编号*/
    @Excel(name = "测试项编号", width = 15)
    @ApiModelProperty(value = "测试项编号")
    private java.lang.String testItemId;

    /**测试用例名称*/
    @Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private java.lang.String testCaseName;

    /**测试项编号*/
    @Excel(name = "测试用例编号", width = 15)
    @ApiModelProperty(value = "测试用例编号")
    private java.lang.String testCaseId;

    @ApiModelProperty(value = "脚本视频文件编号")
    private String videoHash;

    @ApiModelProperty(value = "视频名称")
    private String videoName;

}
