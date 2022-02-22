package org.jeecg.modules.project.entity;


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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@TableName(value = "running_uut_file_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_file_project对象", description="项目管理归档")
public class RunningUutFileProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 项目ID,数据库不存在该字段
     */
    @TableField(exist = false)
    private String projectId;
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
    /**被测对象表id*/
    @Excel(name = "被测对象ID", width = 15)
    @ApiModelProperty(value = "被测对象ID")
    private String uutListId;
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
    /**
     * 数据统计
     */
    @Excel(name = "数据统计", width = 15)
    @ApiModelProperty(value = "数据统计")
    private String dataStatistics;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;


    /**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    /**是否发送到期未完成消息 dq add*/
    @Excel(name = "是否发送到期未完成消息", width = 15)
    @ApiModelProperty(value = "是否发送到期未完成消息")
    private Integer bNotFinishMsg;

    /**项目成员id，多个id逗号分隔，注意本表的project_members字段存的是用户名称逗号分隔，这里区分一下 dq add*/
    @ApiModelProperty(value = "项目成员id")
    private String projectMemberIds;

    /** 归档状态,0表示未归档,1表示已归档 */
    @Excel(name = "归档状态", width = 15)
    @ApiModelProperty(value = "归档状态")
    private Integer fileStatus;

    @TableField(exist = false)
    /** 编辑操作用到这个节点，保存修改过的字段名称和字段值*/
    private List<RunningProjectHistory> modifiedList;

    /**评审资料*/
    @Excel(name = "评审资料", width = 15)
    @ApiModelProperty(value = "评审资料")
    private String reviewFile;

    /**项目版本*/
    @TableField(exist = false)
    List<Map<String,String>> turnList;

}
