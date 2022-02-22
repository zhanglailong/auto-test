package org.jeecg.modules.uut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 被测对象人员列表
 * @Author: jeecg-boot
 * @Date:   2021-8-4
 * @Version: V1.0
 */
@ApiModel(value="running_uut_list_user对象", description="被测对象人员列表")
@Data
@TableName("running_uut_list_user")
public class RunningUutListUser implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private String id;
	/**被测对象id*/
	@ApiModelProperty(value = "被测对象id")
	private String uutId;
	/**人员id*/
	@ApiModelProperty(value = "出库单标识")
	private String userId;

}
