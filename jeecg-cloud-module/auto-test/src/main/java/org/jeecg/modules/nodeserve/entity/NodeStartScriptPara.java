package org.jeecg.modules.nodeserve.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="启动/暂停/停止脚本参数", description="启动/暂停/停止脚本参数")
public class NodeStartScriptPara implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目名称")

    private String projectName;

    @ApiModelProperty(value = "status 状态 停STOP 暂停SUSPEND  启动START")
    private String status;

    @ApiModelProperty(value = "脚本名称")
    private String scriptName;
    /**
     * 测试项名称
     */
    private String taskName;
    /**
     * 测试用例
     */
    private String testName;
    /**
     * 脚本唯一标识
     */
    private String scriptCode;
    /**
     * 0为开始1为停止
     */
    private Integer type;

    /**
     * 用于创建脚本启动的测试结果记录信息字段
     */
    @ApiModelProperty(value = "项目id")
    String projectId;

    @ApiModelProperty(value = "测试用例名称")
    String testCaseName;

    @ApiModelProperty(value = "测试用例id ")
    String testCaseId;

    @ApiModelProperty(value = "测试项名称")
    String testItemName;

    @ApiModelProperty(value = "测试项id")
    String testItemId;

    @ApiModelProperty(value = "虚拟删除")
    private java.lang.Integer idel;


    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;


    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date endTime;

    @ApiModelProperty(value = "回放标识 0用例回放，1脚本回放")
    private String playBackCode;

    @ApiModelProperty(value = "脚本回放记录id")
    private String runRecordId;

    /**执行节点ip*/
    @ApiModelProperty(value = "执行节点ip")
    private String recordNodeIp;
}
