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
import java.util.Date;
import java.util.List;

/**
 * @Description: 项目角色表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Data
@TableName("running_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_role对象", description="项目角色表")
public class RunningRole implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**角色名称*/
	@Excel(name = "角色名称", width = 15)
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /** 角色编码
     * @Excel(name = "角色编码", width = 15)
     */
    @ApiModelProperty(value = "角色编码")
    private String roleCode;
	/**描述*/
	@Excel(name = "描述", width = 60)
    @ApiModelProperty(value = "描述")
    private String description;
    /** 默认角色 0普通角色1最高权限角色
     * @Excel(name = "默认角色 0普通角色1最高权限角色", width = 15)
     */
    @ApiModelProperty(value = "默认角色 0普通角色1最高权限角色")
    private Integer root;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	
	@ApiModelProperty(value = "删除标记")
	private Integer delFlag;
	
	@TableField(exist = false)
    /**编辑操作用到这个节点，保存修改过的字段名称和字段值*/
	private List<RunningRoleHistory> modifiedList;
}
