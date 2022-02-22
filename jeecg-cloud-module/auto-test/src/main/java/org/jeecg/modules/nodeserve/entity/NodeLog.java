package org.jeecg.modules.nodeserve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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


/**
 * @author yeyl
 */
@Data
@TableName("node_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="node_log对象", description="调用统一接口日志表")
public class NodeLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**ip*/
	@Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private String ip;
	/**请求地址*/
	@Excel(name = "请求地址", width = 15)
    @ApiModelProperty(value = "请求地址")
    private String url;
	/**发送数据*/
	@Excel(name = "发送数据", width = 15)
    @ApiModelProperty(value = "发送数据")
    private String data;
	/**请求方式*/
	@Excel(name = "请求方式", width = 15)
    @ApiModelProperty(value = "请求方式")
    private String method;
	/**保存状态*/
	@Excel(name = "保存状态", width = 15)
    @ApiModelProperty(value = "保存状态")
    @Dict(dicCode = "openstack_log_state")
    private Integer state;
	/**消息*/
	@Excel(name = "消息", width = 15)
    @ApiModelProperty(value = "消息")
    private String msg;
}
