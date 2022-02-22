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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 项目角色操作记录历史表
 * @Author: jeecg-boot
 * @Date:   2021-04-21
 * @Version: V1.0
 */
@Data
@TableName("running_role_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_role_history对象", description="项目角色历史记录表")
public class RunningRoleHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**角色名称*/
	@Excel(name = "角色名称", width = 15)
    @ApiModelProperty(value = "角色名称")
    private String roleName;
	/**角色编码
	 * @Excel(name = "角色编码", width = 15)
	 * */
    @ApiModelProperty(value = "角色编码")
    private String roleCode;
	/**描述*/
	@Excel(name = "描述", width = 60)
    @ApiModelProperty(value = "描述")
    private String description;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	
	@ApiModelProperty(value = "删除标记")
	private Integer delFlag;
	
	
	/**主表id*/
	@ApiModelProperty(value = "主表id")
	private String mainId;
	/**排序*/
	@ApiModelProperty(value = "排序")
	private Long sort;
	/**修改字段code*/
	@ApiModelProperty(value = "修改字段code")
	private String modifyField;
	/**修改字段当前值*/
	@ApiModelProperty(value = "修改字段当前值")
	private String modifyFieldValue;
	/**修改字段旧值*/
	@ApiModelProperty(value = "修改字段旧值")
	private String modifyFieldOldValue;
	/**操作类型，0：新增  1：编辑  2：删除*/
	@ApiModelProperty(value = "操作类型")
	private Integer opType;

	  /**dq add*/
    @TableField(exist = false)
    /**查询开始日期*/
    private String startDate;
    @TableField(exist = false)
    /**查询结束日期*/
    private String endDate;
	
	
	
	
}
