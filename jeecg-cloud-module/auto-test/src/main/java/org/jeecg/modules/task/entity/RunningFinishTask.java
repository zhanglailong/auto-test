package org.jeecg.modules.task.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.modules.task.vo.AutoInfoVO;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 完成任务表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@Data
@TableName("running_finish_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_finish_task对象", description="完成任务表")
public class RunningFinishTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**任务主键*/
	@Excel(name = "任务主键", width = 15)
    @ApiModelProperty(value = "任务主键")
    private String taskId;
	/**在评价中检测缺少的功能数*/
	@Excel(name = "在评价中检测缺少的功能数", width = 15)
    @ApiModelProperty(value = "在评价中检测缺少的功能数")
    private Integer missingFunctionNumbers;
	/**在评价中检测不能正确实现的功能数*/
	@Excel(name = "在评价中检测不能正确实现的功能数", width = 15)
    @ApiModelProperty(value = "在评价中检测不能正确实现的功能数")
    private Integer cannotRealizedNumbers;
	/**按照预期合理的结果，用户得到的结果在可允许范围的差别的用例数*/
	@Excel(name = "按照预期合理的结果，用户得到的结果在可允许范围的差别的用例数", width = 15)
    @ApiModelProperty(value = "按照预期合理的结果，用户得到的结果在可允许范围的差别的用例数")
    private Integer caseNumbers;
	/**能够调用成功的接口数*/
	@Excel(name = "能够调用成功的接口数", width = 15)
    @ApiModelProperty(value = "能够调用成功的接口数")
    private Integer sucessInterfaceNumbers;
	/**不能与其他软件或系统交换数据的次数*/
	@Excel(name = "不能与其他软件或系统交换数据的次数", width = 15)
    @ApiModelProperty(value = "不能与其他软件或系统交换数据的次数")
    private Integer cannotExchangedNumbers;
	/**企图交换数据的总次数*/
	@Excel(name = "企图交换数据的总次数", width = 15)
    @ApiModelProperty(value = "企图交换数据的总次数")
    private Integer totalNumbersExchanges;
	/**测试中还未实现规定的功能性的依从性的项目*/
	@Excel(name = "测试中还未实现规定的功能性的依从性的项目", width = 15)
    @ApiModelProperty(value = "测试中还未实现规定的功能性的依从性的项目")
    private String unrealizedSubject;
	/**按规定正确实现的界面总数*/
	@Excel(name = "按规定正确实现的界面总数", width = 15)
    @ApiModelProperty(value = "按规定正确实现的界面总数")
    private Integer totalInterface;
	/**预测的潜在风险总数*/
	@Excel(name = "预测的潜在风险总数", width = 15)
    @ApiModelProperty(value = "预测的潜在风险总数")
    private String totalPotentialRisks;
	/**实际检测到的故障总数*/
	@Excel(name = "实际检测到的故障总数", width = 15)
    @ApiModelProperty(value = "实际检测到的故障总数")
    private Integer totalFailuresNumbers;
	/**代码行数*/
	@Excel(name = "代码行数", width = 15)
    @ApiModelProperty(value = "代码行数")
    private Integer codeLines;
	/**规定的可靠性的依从性还未完全实现的项数*/
	@Excel(name = "规定的可靠性的依从性还未完全实现的项数", width = 15)
    @ApiModelProperty(value = "规定的可靠性的依从性还未完全实现的项数")
    private Integer unrealizedProjectsNumbers;
	/**规定的可靠性的依从性项总数*/
	@Excel(name = "规定的可靠性的依从性项总数", width = 15)
    @ApiModelProperty(value = "规定的可靠性的依从性项总数")
    private Integer reliabilityComplianceSum;
	/**模块总数*/
	@Excel(name = "模块总数", width = 15)
    @ApiModelProperty(value = "模块总数")
    private Integer modulesTotalNumbers;
	/**模块的平均注释率*/
	@Excel(name = "模块的平均注释率", width = 15)
    @ApiModelProperty(value = "模块的平均注释率")
    private Double averageAnnotationRate;
	/**注释率小于20%的模块总数*/
	@Excel(name = "注释率小于20%的模块总数", width = 15)
    @ApiModelProperty(value = "注释率小于20%的模块总数")
    private Double annotationRateLess20;
	/**行数超过200的模块数*/
	@Excel(name = "行数超过200的模块数", width = 15)
    @ApiModelProperty(value = "行数超过200的模块数")
    private Integer modulesLinesThan200;
	/**模块的调用层数*/
	@Excel(name = "模块的调用层数", width = 15)
    @ApiModelProperty(value = "模块的调用层数")
    private Integer modulesCallLevel;
	/**所需的平均响应时间*/
	@Excel(name = "所需的平均响应时间", width = 15)
    @ApiModelProperty(value = "所需的平均响应时间")
    private Double averageResponseTime;
	/**所需的最大响应时间*/
	@Excel(name = "所需的最大响应时间", width = 15)
    @ApiModelProperty(value = "所需的最大响应时间")
    private Double maximumResponseTime;
	/**所要求的平均吞吐量*/
	@Excel(name = "所要求的平均吞吐量", width = 15)
    @ApiModelProperty(value = "所要求的平均吞吐量")
    private Double averageThroughput;
	/**所需的最大吞吐量*/
	@Excel(name = "所需的最大吞吐量", width = 15)
    @ApiModelProperty(value = "所需的最大吞吐量")
    private Double maximumThroughput;
	/**所需的平均周转时间*/
	@Excel(name = "所需的平均周转时间", width = 15)
    @ApiModelProperty(value = "所需的平均周转时间")
    private Double averageTurnaroundTime;
	/**所需的最大周转时间*/
	@Excel(name = "所需的最大周转时间", width = 15)
    @ApiModelProperty(value = "所需的最大周转时间")
    private Double maximumTurnaroundTime;
	
	
	  /**
     * 自定义填写信息
     */
    @Excel(name = "自定义填写信息", width = 15)
    @ApiModelProperty(value = "自定义填写信息")
    private String autoInfo;

    
    /**
     * 自定义填写信息
     */
    @Excel(name = "自定义填写信息", width = 15)
    @ApiModelProperty(value = "自定义填写信息")
    @TableField(exist = false)
    private List<AutoInfoVO> finishTaskInfo;
    
    
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
