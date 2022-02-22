package org.jeecg.modules.task.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("task_res_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="task_res_file对象", description="任务结果")
/**
 * @Author: test
 * */
public class TaskResFile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**任务ID*/
	@Excel(name = "任务ID", width = 15)
    @ApiModelProperty(value = "任务ID")
    private String taskId;
	/**结果路径*/
	@Excel(name = "结果路径", width = 15)
    @ApiModelProperty(value = "结果路径")
    private String resUrl;
	
    private Date time;
    
}
