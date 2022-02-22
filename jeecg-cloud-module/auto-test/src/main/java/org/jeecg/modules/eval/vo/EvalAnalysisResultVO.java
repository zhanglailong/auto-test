package org.jeecg.modules.eval.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
/**
 * @Author: test
 * */
@TableName("eval_analysis_result")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Eval_analysis_result对象", description="Eval_analysis_result")
public class EvalAnalysisResultVO {

	private String id;
	
	private String uutId;
	@Excel(name = "被测对象名称", width = 20)
	@TableField(exist = false)
	private String uutName;
	@Excel(name = "测试项目名称", width = 20)
	@TableField(exist = false)
	private String projectName;
	@Excel(name = "测试项名称", width = 20)
	@TableField(exist = false)
	private String taskName;
	@Excel(name = "测试用例名称", width = 20)
	@TableField(exist = false)
	private String testName;
	@Excel(name = "得分", width = 20)
	@TableField(exist = false)
	private Double score;
	@Excel(name = "评价", width = 20)
	@TableField(exist = false)
	private String evaluate;
	@Excel(name = "评价体系id", width = 20)
	@TableField(exist = false)
	private String systemId;
	@Excel(name = "更新日期", width = 20)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private String createTime;

}
