package org.jeecg.modules.template.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 模板列表
 * @Author: zll
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Data
@TableName("auto_template")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="auto_template对象", description="模板列表")
public class AutoTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;

	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**节点列表*/
	@Excel(name = "节点列表", width = 15)
    @ApiModelProperty(value = "节点列表")
    private java.lang.String nodeList;
    /**
     * 虚拟删除
     */
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private java.lang.Integer idel;

    /**模板类型
     * 0 固定模板
     * 1 自定义模板
     * */
    @Excel(name = "模板类型", width = 15)
    @ApiModelProperty(value = "模板类型")
    private java.lang.String type;
}
