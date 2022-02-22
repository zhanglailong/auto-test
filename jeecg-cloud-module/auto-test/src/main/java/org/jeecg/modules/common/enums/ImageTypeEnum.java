package org.jeecg.modules.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Getter
public enum ImageTypeEnum {
    QUEUED("QUEUED", "排队中"),
    SAVING("SAVING", "保存中"),
    UPLOADING("UPLOADING", "上传中"),
    ACTIVE("ACTIVE", "运行"),
    ;

    ImageTypeEnum(String imageStatus, String desc) {
        this.imageStatus = imageStatus;
        this.desc = desc;
    }

    @EnumValue
    private final String imageStatus;
    @JsonValue
    private final String desc;

    public static ImageTypeEnum toEnum(String imageStatus) {
        if (StringUtils.isEmpty(imageStatus)) {
            return null;
        }
        for (ImageTypeEnum item : ImageTypeEnum.values()) {
            if (item.getImageStatus().equals(imageStatus)) {
                return item;
            }
        }
        return null;
    }
}
