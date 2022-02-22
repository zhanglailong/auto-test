package org.jeecg.modules.project.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.project.entity.RunningProjectTurn;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 项目列表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Data
public class RunningProjectInfo {

    /**id*/
    @Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private String id;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String projectName;
	/**项目标识*/
	@Excel(name = "项目标识", width = 15)
    @ApiModelProperty(value = "项目标识")
    private String projectCode;
	/**项目成员*/
	@Excel(name = "项目成员", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "项目成员")
    private String projectMembers;
	/**被测对象状态*/
	@Excel(name = "被测对象状态", width = 15)
	@Dict(dicCode = "uutListStatus")
    @ApiModelProperty(value = "被测对象状态")
    private Integer projectState;
	/**被测对象名称*/
    @ApiModelProperty(value = "被测对象名称")
    private String uutName;
    /**被测对象标识*/
    @ApiModelProperty(value = "被测对象标识")
    private String uutCode;
    /**被测对象类型*/
    @ApiModelProperty(value = "被测对象类型")
    private String uutType;
    /**被测对象版本*/
    @ApiModelProperty(value = "被测对象版本")
    private String uutVersion;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建日期*/
    @ApiModelProperty(value = "创建日期")
    private String createTime;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private String cuFile;
	/**开始时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date beginDate;
	/**结束时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date finishDate;

	/**项目状态*/
	@Excel(name = "项目状态", width = 15)
	@Dict(dicCode = "finishStatus")
    @ApiModelProperty(value = "项目状态")
    private Integer finishStatus;


	/**备注*/
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "资产库详情")
    private String uutAssetsDetail;

    /**
     * 数据统计
     */
    /**@Excel(name = "数据统计", width = 15)*/
    @ApiModelProperty(value = "数据统计")
    private String dataStatistics;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

	/**被测对象表id*/
	@Excel(name = "被测对象ID", width = 15)
    @ApiModelProperty(value = "被测对象ID")
    private String uutListId;

    /**评审资料*/
    @Excel(name = "评审资料", width = 15)
    @ApiModelProperty(value = "评审资料")
    private String reviewFile;
    /**被测件*/
    @ApiModelProperty(value = "被测件")
    private String uutFile;

    /**轮次*/
    @TableField(exist = false)
    @ApiModelProperty(value = "轮次")
    private String turnNum;

    /**备注*/
    @TableField(exist = false)
    @ApiModelProperty(value = "备注")
    private String comment;

    /**项目版本*/
    @TableField(exist = false)
    List<RunningProjectTurn> uutVersions;
    /**其他附件*/
    @TableField(exist = false)
    @ApiModelProperty(value = "其他附件")
    private String uutOtherFile;

    /** 归档状态,0表示未归档,1表示已归档 */
    @Excel(name = "归档状态", width = 15)
    @ApiModelProperty(value = "归档状态")
    private Integer fileStatus;

    /** 归档标记,0表示归档未选择,1归档时选择 */
    @Excel(name = "归档标记", width = 15)
    @ApiModelProperty(value = "归档标记")
    private Integer fileSign;

    @ApiModelProperty(value = "数据来源")
    private String type;
    /**
     * 被测对象名称
     */
    @ApiModelProperty(value = "被测对象名称")
    private String measuredName;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;

}
