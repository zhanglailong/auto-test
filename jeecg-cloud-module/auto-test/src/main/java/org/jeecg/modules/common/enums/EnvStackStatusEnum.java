package org.jeecg.modules.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author zlf
 */
@Getter
public enum EnvStackStatusEnum {

    /*
        CREATE_IN_PROGRESS	创建中
        CREATE_FAILED	创建失败
        CREATE_COMPLETE	创建完成
        DELETE_IN_PROGRESS	删除中
        DELETE_FAILED	删除失败
        DELETE_COMPLETE	删除完成
        UPDATE_IN_PROGRESS	更新中
        UPDATE_FAILED	更新失败
        UPDATE_COMPLETE	更新完成
        ROLLBACK_IN_PROGRESS	回滚中
        ROLLBACK_FAILED	回滚失败
        ROLLBACK_COMPLETE	回滚完成
        SUSPEND_IN_PROGRESS	挂起中
        SUSPEND_FAILED	挂起失败
        SUSPEND_COMPLETE	挂起完成
        SUSPEND_RESTORE_COMPLETE 挂起恢复成功
        RESUME_IN_PROGRESS	继续中
        RESUME_FAILED	继续失败
        RESUME_COMPLETE	继续完成
        SNAPSHOT_IN_PROGRESS	创建快照中
        SNAPSHOT_FAILED	创建快照失败
        SNAPSHOT_COMPLETE	创建快照完成
        RESTORE_IN_PROGRESS	从快照恢复中
        RESTORE_FAILED	从快照恢复失败
        RESTORE_COMPLETE	从快照恢复完成
     */
    CREATE_IN_PROGRESS("CREATE_IN_PROGRESS", "创建中"),
    CREATE_FAILED("CREATE_FAILED", "创建失败"),
    CREATE_COMPLETE("CREATE_COMPLETE", "创建完成"),
    CREATE_VIR_COMPLETE("CREATE_VIR_COMPLETE", "虚拟机创建成功"),
    DELETE_IN_PROGRESS("DELETE_IN_PROGRESS", "删除中"),
    DELETE_FAILED("DELETE_FAILED", "删除失败"),
    DELETE_COMPLETE("DELETE_COMPLETE", "删除完成"),
    UPDATE_IN_PROGRESS("UPDATE_IN_PROGRESS", "更新中"),
    UPDATE_FAILED("UPDATE_FAILED", "更新失败"),
    UPDATE_COMPLETE("UPDATE_COMPLETE", "更新完成"),
    ROLLBACK_IN_PROGRESS("ROLLBACK_IN_PROGRESS", "回滚中"),
    ROLLBACK_FAILED("ROLLBACK_FAILED", "回滚失败"),
    ROLLBACK_COMPLETE("ROLLBACK_COMPLETE", "回滚完成"),
    SUSPEND_IN_PROGRESS("SUSPEND_IN_PROGRESS", "挂起中"),
    SUSPEND_FAILED("SUSPEND_FAILED", "挂起失败"),
    SUSPEND_COMPLETE("SUSPEND_COMPLETE", "挂起完成"),
    SUSPEND_RESTORE_COMPLETE("SUSPEND_RESTORE_COMPLETE", "挂起恢复成功"),
    RESUME_IN_PROGRESS("RESUME_IN_PROGRESS", "继续中"),
    RESUME_FAILED("RESUME_FAILED", "继续失败"),
    RESUME_COMPLETE("RESUME_COMPLETE", "继续完成"),
    SNAPSHOT_IN_PROGRESS("SNAPSHOT_IN_PROGRESS", "创建快照中"),
    SNAPSHOT_FAILED("SNAPSHOT_FAILED", "创建快照失败"),
    SNAPSHOT_COMPLETE("SNAPSHOT_COMPLETE", "创建快照完成"),
    RESTORE_IN_PROGRESS("RESTORE_IN_PROGRESS", "从快照恢复中"),
    RESTORE_FAILED("RESTORE_FAILED", "从快照恢复失败"),
    RESTORE_COMPLETE("RESTORE_COMPLETE", "从快照恢复完成"),
    ;

    EnvStackStatusEnum(String state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    @EnumValue
    private final String state;
    @JsonValue
    private final String desc;

    public static EnvStackStatusEnum toEnum(String state) {
        if(StringUtils.isEmpty(state)){
            return null;
        }
        for(EnvStackStatusEnum item : EnvStackStatusEnum.values()) {
            if(item.getState().equals(state)) {
                return item;
            }
        }
        return null;
    }

}