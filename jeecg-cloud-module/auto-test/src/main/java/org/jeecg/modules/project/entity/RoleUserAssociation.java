package org.jeecg.modules.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: role_user_association
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
@Data
@TableName("role_user_association")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="role_user_association对象", description="role_user_association")
public class RoleUserAssociation implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**项目主键*/
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**角色表id*/
	@Excel(name = "角色表id", width = 15)
    @ApiModelProperty(value = "角色表id")
    private String roleId;
	/**用户表id*/
	@Excel(name = "用户表id", width = 15)
    @ApiModelProperty(value = "用户表id")
    private String userId;
	
	
}
