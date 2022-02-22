package org.jeecg.modules.task.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.task.entity.TaskResFile;
import org.jeecg.modules.task.mapper.TaskResFileMapper;
import org.jeecg.modules.task.service.ITaskResFileService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2020-12-25
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class TaskResFileServiceImpl extends ServiceImpl<TaskResFileMapper, TaskResFile> implements ITaskResFileService {

}
