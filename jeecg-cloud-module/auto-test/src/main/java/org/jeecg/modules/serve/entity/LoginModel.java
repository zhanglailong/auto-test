package org.jeecg.modules.serve.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author yeyl 2021/8/17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="服务登录对象", description="服务登录对象")
public class LoginModel {
	@ApiModelProperty(value = "账号")
    private String username;
	@ApiModelProperty(value = "密码")
    private String password;
}