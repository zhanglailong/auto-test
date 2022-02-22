package org.jeecg.modules.uut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

@Data
@TableName("running_uut_version")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_version对象", description="被测对象版本表")
public class RunningUutVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**被测对象id*/
    @Excel(name = "被测对象id")
    @ApiModelProperty(value = "被测对象id")
    private String uutListId;
    /**被测对象版本*/
    @Excel(name = "被测对象版本")
    @ApiModelProperty(value = "被测对象版本")
    private String version;
}
