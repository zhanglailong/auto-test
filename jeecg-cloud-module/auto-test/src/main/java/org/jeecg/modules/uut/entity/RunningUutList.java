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
import java.util.List;

/**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2021-01-13
 * @Version: V1.0
 */
@Data
@TableName("running_uut_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_list对象", description="被测对象列表")
public class RunningUutList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**被测对象创建方式*/
    @Dict(dicCode ="chooseType")
    @Excel(name = "被测对象创建方式", width = 15, dicCode = "chooseType")
    @ApiModelProperty(value = "被测对象创建方式")
    private String uutChooseType;
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
    @ApiModelProperty(value = "被测对象版本")
    private String uutVersion;
	/**选择资产库*/
	@Excel(name = "选择资产库", width = 15, dicCode = "getAssetsList")
	@Dict(dicCode = "getAssetsList")
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
	/**创建人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**状态*/
	@Dict(dicCode = "uutListStatus")
	@Excel(name = "状态", width = 15, dicCode = "uutListStatus")
    @ApiModelProperty(value = "状态")
    private String uutStatus;
	/**分析状态*/
	@Dict(dicCode = "analyzeStatus")
	@Excel(name = "分析状态", width = 15, dicCode = "analyzeStatus")
    @ApiModelProperty(value = "分析状态")
    private String analyzeStatus;
	/**测试模板*/
	@Excel(name = "测试模板", width = 15 )
    @ApiModelProperty(value = "测试模板")
    private String testTemplate;
	/**需求规格说明文档*/
	@Excel(name = "需求规格说明文档", width = 15)
    @ApiModelProperty(value = "需求规格说明文档")
    private String uutStandardFile;
	/**测试依据*/
	@Excel(name = "测试依据", width = 15)
    @ApiModelProperty(value = "测试依据")
    private String uutBasisFile;
	/**测试说明*/
	@Excel(name = "测试说明", width = 15)
    @ApiModelProperty(value = "测试说明")
    private String uutExplainFile;
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
	/**回归用例*/
	@Excel(name = "回归用例", width = 15)
    @ApiModelProperty(value = "回归用例")
    private String uutReFile;
	/**其他*/
	@Excel(name = "其他", width = 15)
    @ApiModelProperty(value = "其他")
    private String uutOtherFile;
    /**版本状态*/
    @Dict(dicCode = "versionStatus")
    @Excel(name = "版本状态", width = 15, dicCode = "versionStatus")
    @ApiModelProperty(value = "状态")
    private String versionStatus;
    /**逻辑删除标识*/
    private Integer deleteFlag;

    @TableField(exist = false)
    /**编辑操作用到这个节点，保存修改过的字段名称和字段值*/
    private List<RunningUutListHistory> runningUutListHistory;
}
