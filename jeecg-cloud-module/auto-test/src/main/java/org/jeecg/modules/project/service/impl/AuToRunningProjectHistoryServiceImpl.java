package org.jeecg.modules.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.project.entity.RunningProjectHistory;
import org.jeecg.modules.project.mapper.AuToRunningProjectHistoryMapper;
import org.jeecg.modules.project.service.IAuToRunningProjectHistoryService;
import org.springframework.stereotype.Service;

@Service
public class AuToRunningProjectHistoryServiceImpl extends ServiceImpl<AuToRunningProjectHistoryMapper, RunningProjectHistory> implements IAuToRunningProjectHistoryService {
}
