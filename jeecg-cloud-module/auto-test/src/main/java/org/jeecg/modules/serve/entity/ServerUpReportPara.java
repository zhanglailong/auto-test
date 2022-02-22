package org.jeecg.modules.serve.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="上传报告参数", description="上传报告参数")
public class ServerUpReportPara implements Serializable {
    private static final long serialVersionUID = 1L;
    /**报告文件Id*/
    @NotBlank
    @ApiModelProperty(value = "报告文件Id")
    private String reportFile;

    /**项目名称*/
    @NotBlank
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 方案
     */
    @ApiModelProperty(value = "方案名称")
    private String scheme;


    /**
     * 脚本名称
     */
    @ApiModelProperty(value = "脚本名称")
    @NotBlank
    private String scriptName;

    /**
     * 测试项名称
     */
    @ApiModelProperty(value = "测试项名称")
    private String taskName;

    /**
     * 测试用例
     */
    @ApiModelProperty(value = "测试用例")
    private String testName;

    /**
     * 脚本唯一标识（没有值可以是0）
     */
    @ApiModelProperty(value = "脚本唯一标识（没有值可以是0）")
    @NotBlank
    private String scriptCode;

    /**
     * 生成运行脚本记录id
     */
    @ApiModelProperty(value = "生成运行脚本记录id")
    private String runRecordId;

    @ApiModelProperty(value = "回放标识 0用例回放，1脚本回放")
    private String playBackCode;

    @ApiModelProperty(value = "报告成功状态  1成功  0失败")
    private String state;


}
