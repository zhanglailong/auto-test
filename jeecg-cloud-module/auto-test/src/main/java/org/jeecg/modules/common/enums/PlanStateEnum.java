package org.jeecg.modules.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author zlf
 */
@Getter
public enum PlanStateEnum {

    /*
     */
    PLAN_STATE_0(0, "环境栈创建中"),
    PLAN_STATE_1(1, "环境栈创建成功"),
    PLAN_STATE_2(2, "环境栈创建失败"),
    PLAN_STATE_3(3, "虚拟机创建中");

    PlanStateEnum(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    @EnumValue
    private final int state;
    @JsonValue
    private final String desc;
}