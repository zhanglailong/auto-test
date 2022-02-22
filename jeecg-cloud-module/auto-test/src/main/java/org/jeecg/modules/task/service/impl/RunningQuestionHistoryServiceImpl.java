package org.jeecg.modules.task.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.task.entity.RunningQuestionHistory;
import org.jeecg.modules.task.mapper.RunningQuestionHistoryMapper;
import org.jeecg.modules.task.service.IRunningQuestionHistoryService;
import org.springframework.stereotype.Service;

/**
 * @Description: 测试用例问题单操作历史记录
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Service
@DS("jeecbootDatabase")
public class RunningQuestionHistoryServiceImpl extends ServiceImpl<RunningQuestionHistoryMapper, RunningQuestionHistory> implements IRunningQuestionHistoryService {

//    @Autowired
//    private RunningQuestionHistoryMapper runningQuestionHistoryMapperl;
//    @Autowired
//    private ISysDictService dictService;
//
//    @Override
//    public IPage<Map<String, Object>> getOperationHistoryList(Page<RunningQuestionHistory> page, RunningQuestionHistory params) {
//        IPage<Map<String,Object>> historyList=runningQuestionHistoryMapperl.getOperationHistoryList(page,params);
//        return  historyList;
//    }
}
