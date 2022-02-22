package org.jeecg.modules.script.entity;

import lombok.Data;

/**
 * @author zll
 * @description
 * @date 2021年11月17日 16:31
 */
@Data
public class ReturnUpload {
    /**
     * 文件hash
     */
    private String filePathId;
    /**
     * 文件名称
     */
    private String fileName;
//    /**
//     * 返回message
//     */
//    private String message;

}
