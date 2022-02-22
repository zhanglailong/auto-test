package org.jeecg.modules.task.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.task.entity.RunningTaskReport;
import org.jeecg.modules.task.mapper.RunningTaskReportMapper;
import org.jeecg.modules.task.service.IRunningTaskReportService;
import org.springframework.stereotype.Service;


/**
 * @Description: 测试报告表
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningTaskReportServiceImpl extends ServiceImpl<RunningTaskReportMapper, RunningTaskReport> implements IRunningTaskReportService {

}
