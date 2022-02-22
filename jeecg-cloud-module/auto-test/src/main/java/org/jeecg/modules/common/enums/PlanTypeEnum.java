package org.jeecg.modules.common.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author zlf
 * @version V1.0
 * @date 2020/12/29
 * @Description: 用一句话描述该文件做什么)
 */
@Getter
public enum PlanTypeEnum {
    /*
        system 操作系统
        virtual 虚拟机
        net 网络
        hardWare 硬件资源
        route 路由
        childNet 子网
        testTool 测试工具
        testObject 被测对象
     */
    PlanSystem("system","操作系统"),
    PlanVirtual("virtual","虚拟机"),
    PlanNet("net","网络"),
    PlanHardWare("hardWare","硬件资源"),
    PlanRoute("route","路由"),
    PlanChildNet("childNet","子网"),
    PlanTestTool("testTool","测试工具"),
    PlanTestObject("testObject","被测对象"),
    ;
    private final String type;
    private final String constant;

    PlanTypeEnum(String type, String constant) {
        this.type = type;
        this.constant = constant;
    }

    public static String toEnum(String type) {
        if(StringUtils.isEmpty(type)){
            return null;
        }
        for(PlanTypeEnum item : PlanTypeEnum.values()) {
            if(item.getType().equals(type)) {
                return item.getConstant();
            }
        }
        return null;
    }


//    操作系统    system
//    虚拟机     virtual
//    网络    net
//    硬件资源     hardWare
//    路由      route
//    子网     childNet
//    测试工具    testTool
//    被测对象    testObject
}
