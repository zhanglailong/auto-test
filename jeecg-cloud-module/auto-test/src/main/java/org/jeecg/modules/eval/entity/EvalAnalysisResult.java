package org.jeecg.modules.eval.entity;

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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: Eval_analysis_result
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Data
@TableName("eval_analysis_result")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Eval_analysis_result对象", description="Eval_analysis_result")
public class EvalAnalysisResult implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**被测对象ID*/
	/**@Excel (name = "被测对象ID", width = 15)*/
    @ApiModelProperty(value = "被测对象ID")
    private String uutId;
    /**被测对象名称*/
    @Excel(name = "被测对象名称", width = 20)
    @TableField(exist = false)
    @ApiModelProperty (value = "被测对象名称")
    private String uutName;
    /**被测对象名称*/
    @Excel(name = "测试项目名称", width = 20)
    @ApiModelProperty (value = "测试项目id")
    private String projectId;
	/**分析结果得分*/
	/**@Excel (name = "分析结果得分", width = 15)*/
    @ApiModelProperty(value = "分析结果得分")
    private Double score;
	/**分析结果评价*/
	/**@Excel (name = "分析结果评价", width = 15)*/
    @ApiModelProperty(value = "分析结果评价")
    private String evaluate;
	/**处理状态 0：处理中；1处理完成*/
	/** @Excel (name = "处理状态 0：处理中；1处理完成", width = 15)*/
    @ApiModelProperty(value = "处理状态 0：处理中；1处理完成")
    private Integer processStatus;
    /**评价体系ID*/
   /** @Excel (name = "评价体系ID", width = 15)*/
    @ApiModelProperty(value = "评价体系ID")
    private String systemId;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    public EvalAnalysisResult() {
    }

    public EvalAnalysisResult(String uutId, String projectId, Integer processStatus) {
        this.uutId = uutId;
        this.projectId = projectId;
        this.processStatus = processStatus;
    }
}
