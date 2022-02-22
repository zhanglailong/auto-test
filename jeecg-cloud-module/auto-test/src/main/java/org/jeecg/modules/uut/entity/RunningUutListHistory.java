package org.jeecg.modules.uut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 被测对象操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Data
@TableName("running_uut_list_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_list_history对象", description="被测对象操作历史表")
public class RunningUutListHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    /**@Dict (dictTable = "sys_user", dicText = "realname", dicCode = "id")*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
   /**@Dict (dictTable = "sys_user", dicText = "realname", dicCode = "username")*/
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
	/**被测对象名称*/
	@Excel(name = "被测对象名称", width = 15)
    @ApiModelProperty(value = "被测对象名称")
    private String uutName;
	/**被测对象类型*/
	@Excel(name = "被测对象类型", width = 15)
    @ApiModelProperty(value = "被测对象类型")
    private String uutType;
	/**被测对象标识*/
	@Excel(name = "被测对象标识", width = 15)
    @ApiModelProperty(value = "被测对象标识")
    private String uutCode;
	/**被测对象版本*/
	@Excel(name = "被测对象版本", width = 15)
    @Dict(dictTable = "running_uut_list_history", dicText = "id", dicCode = "uut_version")
    @ApiModelProperty(value = "被测对象版本")
    private String uutVersion;
	/**选择资产库*/
	@Excel(name = "选择资产库", width = 15)
    @ApiModelProperty(value = "选择资产库")
    private String uutAssetsId;
	/**资产库详情*/
	@Excel(name = "资产库详情", width = 15)
    @ApiModelProperty(value = "资产库详情")
    private String uutAssetsDetail;
	/**被测件*/
	@Excel(name = "被测件", width = 15)
    @ApiModelProperty(value = "被测件")
    private String uutFile;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String uutStatus;
	/**分析状态*/
	@Excel(name = "分析状态", width = 15)
    @ApiModelProperty(value = "分析状态")
    private String analyzeStatus;
	/**测试模板*/
	@Excel(name = "测试模板", width = 15)
    @ApiModelProperty(value = "测试模板")
    private String testTemplate;
	/**测试说明*/
	@Excel(name = "测试说明", width = 15)
    @ApiModelProperty(value = "测试说明")
    private String uutExplainFile;
	/**测试依据*/
	@Excel(name = "测试依据", width = 15)
    @ApiModelProperty(value = "测试依据")
    private String uutBasisFile;
	/**回归用例*/
	@Excel(name = "回归用例", width = 15)
    @ApiModelProperty(value = "回归用例")
    private String uutReFile;
	/**其他*/
	@Excel(name = "其他", width = 15)
    @ApiModelProperty(value = "其他")
    private String uutOtherFile;
	/**需求规格说明文档*/
	@Excel(name = "需求规格说明文档", width = 15)
    @ApiModelProperty(value = "需求规格说明文档")
    private String uutStandardFile;
	/**历史测试用例设计单*/
	@Excel(name = "历史测试用例设计单", width = 15)
    @ApiModelProperty(value = "历史测试用例设计单")
    private String uutHteFile;
	/**执行单*/
	@Excel(name = "执行单", width = 15)
    @ApiModelProperty(value = "执行单")
    private String uutExecuteFile;
	/**报告单*/
	@Excel(name = "报告单", width = 15)
    @ApiModelProperty(value = "报告单")
    private String uutReportFile;
	/**版本状态*/
	@Excel(name = "版本状态", width = 15)
    @ApiModelProperty(value = "版本状态")
    private String versionStatus;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    private Integer delFlag;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**主表id*/
	@Excel(name = "主表id", width = 15)
    @ApiModelProperty(value = "主表id")
    private String runningUutListId;
    /**被改变的字段名称*/
    @Excel(name = "被改变的字段名称", width = 15)
    @ApiModelProperty(value = "被改变的字段名称")
    @Dict(dictTable = "running_uut_list_history", dicCode = "modify_field", dicText = "modify_field")
    private String modifyField;
    /**字段改变后的当前值*/
    @Excel(name = "字段改变后的当前值", width = 15)
    @ApiModelProperty(value = "字段改变后的当前值")
    private String modifyFieldValue;
    /**字段改变前的值*/
    @Excel(name = "字段改变前的值", width = 15)
    @ApiModelProperty(value = "字段改变前的值")
    private String modifyFieldOldValue;
    /**操作类型，0：新增  1：编辑  2：删除*/
    @Excel(name = "操作类型，0：新增  1：编辑  2：删除", width = 15)
    @ApiModelProperty(value = "操作类型，0：新增  1：编辑  2：删除")
    private Integer opType;

    /**dq add*/
    @TableField(exist = false)
    /**查询开始日期*/
    private String startDate;
    @TableField(exist = false)
    /**查询结束日期*/
    private String endDate;
}
