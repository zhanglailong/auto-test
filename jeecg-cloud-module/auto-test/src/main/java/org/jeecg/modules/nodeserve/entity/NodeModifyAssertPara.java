package org.jeecg.modules.nodeserve.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "修改断言参数", description = "修改断言参数")
public class NodeModifyAssertPara implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "脚本名称")
    private String scriptName;

    @ApiModelProperty(value = "项目名称")
    private String project;

    @ApiModelProperty(value = "断言名称名称")
    private String assertName;

    @ApiModelProperty(value = "断言值")
    private String assertValue;

}
