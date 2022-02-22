package org.jeecg.modules.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author zlf
 */
@Getter
public enum ServerStatusEnum {

    /*
        ACTIVE	运行
        BUILD	创建
        RESIZE	调整大小或迁移
        SHUTOFF	关机
        VERIFY_RESIZE	确认调整大小
        PAUSED	暂停
        SUSPENDED	挂起
        ERROR	错误
        SUSPEND_IN_PROGRESS 挂起中
        RESUME_IN_PROGRESS 恢复挂起中

     */
//    ACTIVE("ACTIVE", "运行"),
//    BUILD("BUILD", "创建"),
//    RESIZE("RESIZE", "调整大小或迁移"),
//    SHUTOFF("SHUTOFF", "确认调整大小"),
//    PAUSED("PAUSED", "暂停"),
//    SUSPENDED("SUSPENDED", "挂起"),
//    ERROR("ERROR", "错误"),
//    SUSPEND_IN_PROGRESS("SUSPEND_IN_PROGRESS", "挂起中"),
//    RESUME_IN_PROGRESS("RESUME_IN_PROGRESS", "恢复挂起中"),
//    DESTROY("DESTROY", "销毁"),
//    ;
    ACTIVE("ACTIVE", "ACTIVE"),
    BUILD("BUILD", "BUILD"),
    RESIZE("RESIZE", "RESIZE"),
    SHUTOFF("SHUTOFF", "SHUTOFF"),
    PAUSED("PAUSED", "PAUSED"),
    SUSPENDED("SUSPENDED", "SUSPENDED"),
    ERROR("ERROR", "ERROR"),
    SUSPEND_IN_PROGRESS("SUSPEND_IN_PROGRESS", "SUSPEND_IN_PROGRESS"),
    RESUME_IN_PROGRESS("RESUME_IN_PROGRESS", "RESUME_IN_PROGRESS"),
    DESTROY("DESTROY", "DESTROY"),
            ;

    ServerStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @EnumValue
    private final String status;
    @JsonValue
    private final String desc;

    public static ServerStatusEnum toEnum(String status) {
        if (StringUtils.isEmpty(status)) {
            return null;
        }
        for (ServerStatusEnum item : ServerStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

}