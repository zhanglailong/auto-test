package org.jeecg.modules.scriptrecord.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: 运行脚本记录表
 * @Author: jeecg-boot
 * @Date:   2021-12-10
 * @Version: V1.0
 */
@Data
@TableName("run_script_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="run_script_record对象", description="运行脚本记录表")
public class RunScriptRecord implements Serializable {
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
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;
	/**虚拟删除*/
	@Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.Integer idel;
	/**脚本id*/
	@Excel(name = "脚本id", width = 15)
    @ApiModelProperty(value = "脚本id")
    private java.lang.String autoScriptId;
	/**脚本名称*/
	@Excel(name = "脚本名称", width = 15)
    @ApiModelProperty(value = "脚本名称")
    private java.lang.String autoScriptName;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endTime;
	/**测试用例id*/
	@Excel(name = "测试用例id", width = 15)
    @ApiModelProperty(value = "测试用例id")
    private java.lang.String testCaseId;
	/**测试项id*/
	@Excel(name = "测试项id", width = 15)
    @ApiModelProperty(value = "测试项id")
    private java.lang.String testItemId;
	/**测试用例名称*/
	@Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private java.lang.String testCaseName;
	/**测试项名称*/
	@Excel(name = "测试项名称", width = 15)
    @ApiModelProperty(value = "测试项名称")
    private java.lang.String testItemName;
}
