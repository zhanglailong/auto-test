package org.jeecg.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@TableName("running_project_turn_version")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_project_turn_version对象", description="项目管理轮次版本")
public class RunningProjectTurnVersion {
    /**id*/
    @Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private String id;
    /**轮次*/
    @Excel(name = "turnId", width = 15)
    @ApiModelProperty(value = "轮次")
    private String turnId;

    /**项目版本*/
    @ApiModelProperty(value = "项目版本")
    private String versionId;

}
