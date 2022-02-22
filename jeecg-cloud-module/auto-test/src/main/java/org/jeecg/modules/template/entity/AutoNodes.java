package org.jeecg.modules.template.entity;

import lombok.Data;

/**
 * @author zll
 * @description
 * @date 2021年11月02日 15:11
 */
@Data
public class AutoNodes {
    /**
     * 节点名称
     */
    private String value;
    /**
     * 节点类型
     */
    private String type;
    /**
     * 节点状态
     */
    private String state;
}
