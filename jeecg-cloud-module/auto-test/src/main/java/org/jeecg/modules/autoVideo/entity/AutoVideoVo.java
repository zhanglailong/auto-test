package org.jeecg.modules.autoVideo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 视频列表
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Data
@TableName("auto_video")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="auto_video对象", description="视频列表")
public class AutoVideoVo implements Serializable {
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
	/**脚本id*/
	@Excel(name = "脚本id", width = 15)
    @ApiModelProperty(value = "脚本id")
    private String scriptId;
	/**节点名称*/
	@Excel(name = "节点名称", width = 15)
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
	/**客户端ip*/
	@Excel(name = "客户端ip", width = 15)
    @ApiModelProperty(value = "客户端ip")
    private String clientIp;
	/**视频地址-hash*/
	@Excel(name = "视频地址-hash", width = 15)
    @ApiModelProperty(value = "视频地址-hash")
    private String videoAddress;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private Integer state;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**虚拟删除*/
	@Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private Integer idel;
    /**节点id*/
    @Excel(name = "节点id", width = 15)
    @ApiModelProperty(value = "节点id")
    private String nodeId;
    /**方案id*/
    @Excel(name = "方案id", width = 15)
    @ApiModelProperty(value = "方案id")
    private String planId;
    /**方案名称*/
    @Excel(name = "方案名称", width = 15)
    @ApiModelProperty(value = "方案名称")
    private String planName;

    /**视频名称*/
    @Excel(name = "视频名称", width = 15)
    @ApiModelProperty(value = "视频名称")
    private String name;

    /**测试项名称*/
    @Excel(name = "测试项名称", width = 15)
    @ApiModelProperty(value = "测试项名称")
    private String testItemName;

    /**测试用例名称*/
    @Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private String testCaseName;

    /**项目名称*/
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**测试用例id*/
    @Excel(name = "测试用例id", width = 15)
    @ApiModelProperty(value = "测试用例id")
    private java.lang.String testCaseId;

    /**测试项id*/
    @Excel(name = "测试项id", width = 15)
    @ApiModelProperty(value = "测试项id")
    private java.lang.String testItemId;

    /**项目id*/
    @Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;
}
