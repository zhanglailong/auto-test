package org.jeecg.modules.template.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @author zll
 * @Description: 模板json
 * @Date: 2020-12-29
 * @Version: V1.0
 */
@Data
public class TemplateJson implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String name;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 模板节点详情
     */
    @ApiModelProperty(value = "模板节点详情")
    private List<AutoNodes> nodeList;

    /**
     * 模板类型
     * 0  固定模板  1  自定义模板
     */
    @ApiModelProperty(value = "模板节点详情")
    private String type;

    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;



}
