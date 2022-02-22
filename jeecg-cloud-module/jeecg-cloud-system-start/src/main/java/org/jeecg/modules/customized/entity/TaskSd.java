package org.jeecg.modules.customized.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 测试环境定制与管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Data
@TableName("task_sd")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="task_sd对象", description="测试环境定制与管理")
public class TaskSd implements Serializable {
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
	/**环境名称*/
	@Excel(name = "环境名称", width = 15)
    @ApiModelProperty(value = "环境名称")
    private java.lang.String planName;
	/**环境标识*/
	@Excel(name = "环境标识", width = 15)
    @ApiModelProperty(value = "环境标识")
    private java.lang.String planSign;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private java.lang.String taskName;
	/**任务标识*/
	@Excel(name = "任务标识", width = 15)
    @ApiModelProperty(value = "任务标识")
    private java.lang.String taskSign;
	/**虚拟机名称*/
	@Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private java.lang.String vmName;
	/**cpu*/
	@Excel(name = "cpu", width = 15)
    @ApiModelProperty(value = "cpu")
    private java.lang.String cpu;
	/**内存*/
	@Excel(name = "内存", width = 15)
    @ApiModelProperty(value = "内存")
    private java.lang.String ram;
	/**磁盘*/
	@Excel(name = "磁盘", width = 15)
    @ApiModelProperty(value = "磁盘")
    private java.lang.String disk;
	/**操作系统*/
	@Excel(name = "操作系统", width = 15)
    @ApiModelProperty(value = "操作系统")
    private java.lang.String system;
	/**被测对象*/
	@Excel(name = "被测对象", width = 15)
    @ApiModelProperty(value = "被测对象")
    private java.lang.String measurand;
	/**测试工具*/
	@Excel(name = "测试工具", width = 15)
    @ApiModelProperty(value = "测试工具")
    private java.lang.String testTools;
	/**虚拟删除*/
	@Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.String idel;
	/**运行支撑软件*/
	@Excel(name = "运行支撑软件", width = 15)
    @ApiModelProperty(value = "运行支撑软件")
    private java.lang.String supportSoftware;
	/**资产库*/
	@Excel(name = "资产库", width = 15)
    @ApiModelProperty(value = "资产库")
    private java.lang.String assetPool;
	/**网络名称*/
	@Excel(name = "网络名称", width = 15)
    @ApiModelProperty(value = "网络名称")
    private java.lang.String networkName;
	/**网络类型*/
	@Excel(name = "网络类型", width = 15)
    @ApiModelProperty(value = "网络类型")
    private java.lang.String networkType;
	/**共享状态*/
	@Excel(name = "共享状态", width = 15)
    @ApiModelProperty(value = "共享状态")
    private java.lang.String sharedState;
	/**子网名称*/
	@Excel(name = "子网名称", width = 15)
    @ApiModelProperty(value = "子网名称")
    private java.lang.String subnetName;
	/**网络地址*/
	@Excel(name = "网络地址", width = 15)
    @ApiModelProperty(value = "网络地址")
    private java.lang.String networkAddress;
	/**IP版本*/
	@Excel(name = "IP版本", width = 15)
    @ApiModelProperty(value = "IP版本")
    private java.lang.String ipVersion;
	/**网关IP*/
	@Excel(name = "网关IP", width = 15)
    @ApiModelProperty(value = "网关IP")
    private java.lang.String gatewayIp;
	/**禁用网关*/
	@Excel(name = "禁用网关", width = 15)
    @ApiModelProperty(value = "禁用网关")
    private java.lang.String disableGateway;
	/**DHCP激活*/
	@Excel(name = "DHCP激活", width = 15)
    @ApiModelProperty(value = "DHCP激活")
    private java.lang.String dhcpActivation;
	/**地址池*/
	@Excel(name = "地址池", width = 15)
    @ApiModelProperty(value = "地址池")
    private java.lang.String addressPool;
	/**DNS服务器*/
	@Excel(name = "DNS服务器", width = 15)
    @ApiModelProperty(value = "DNS服务器")
    private java.lang.String dnsServer;
	/**路由名称*/
	@Excel(name = "路由名称", width = 15)
    @ApiModelProperty(value = "路由名称")
    private java.lang.String routeName;
	/**管理状态*/
	@Excel(name = "管理状态", width = 15)
    @ApiModelProperty(value = "管理状态")
    private java.lang.String manageState;
	/**外部网络*/
	@Excel(name = "外部网络", width = 15)
    @ApiModelProperty(value = "外部网络")
    private java.lang.String extranet;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String state;
	/**创建类型*/
	@Excel(name = "创建类型", width = 15)
    @ApiModelProperty(value = "创建类型")
    private java.lang.String taskType;
}
