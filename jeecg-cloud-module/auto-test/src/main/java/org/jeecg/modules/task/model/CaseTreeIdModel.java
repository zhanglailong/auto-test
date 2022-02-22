package org.jeecg.modules.task.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Tree表 封装测试用例类型树结构名称的实体类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
@Data
public class CaseTreeIdModel implements Serializable {

    private static final long serialVersionUID = 1L;
    List<CaseTreeIdModel> children = new ArrayList<>();
    /**
     * 主键ID
     */
    private String key;
    /**
     * 主键ID
     */
    private String value;
    /**部门名称*/
    private String title;

    public static long getSerialVersionUid() {
        return serialVersionUID;
    }

}
