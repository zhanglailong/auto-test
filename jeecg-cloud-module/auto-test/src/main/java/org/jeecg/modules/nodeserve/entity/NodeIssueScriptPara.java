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
@ApiModel(value="脚本下发参数", description="脚本下发参数")
public class NodeIssueScriptPara implements Serializable {

    private static final long serialVersionUID = 1L;
    /**数据文件名称*/
    @ApiModelProperty(value = "数据文件名称")
    private String dataFile;

    /**项目名称*/
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**测试项名称*/
    @ApiModelProperty(value = "测试项名称")
    private String taskName;

    /**测试用例*/
    @ApiModelProperty(value = "测试用例")
    private String testName;


    /**
     * 脚本唯一标识
     */
    @ApiModelProperty(value = "脚本唯一标识")
    private String scriptCode;


    /**
     * 脚本名称
     */
    @ApiModelProperty(value = "脚本名称")
    private String scriptName;


    /**脚本内容Id*/
    @ApiModelProperty(value = "脚本内容Id")
    private String content;

    /**
     * 脚本类型
     */
    @ApiModelProperty(value = "脚本类型")
    private String scriptType;



    @ApiModelProperty(value = "数据内容Id")
    private String dataContent;

    /**唯一标识*/
    @ApiModelProperty(value = "唯一标识")
    private String uniqueCode;

    /**执行节点ip*/
    @ApiModelProperty(value = "执行节点ip")
    private String recordNodeIp;


}
