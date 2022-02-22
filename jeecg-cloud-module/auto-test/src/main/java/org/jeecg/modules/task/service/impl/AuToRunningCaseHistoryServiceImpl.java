package org.jeecg.modules.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningCaseHistory;
import org.jeecg.modules.task.mapper.AuToRunningCaseHistoryMapper;
import org.jeecg.modules.task.service.IAuToRunningCaseHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuToRunningCaseHistoryServiceImpl extends ServiceImpl<AuToRunningCaseHistoryMapper, RunningCaseHistory> implements IAuToRunningCaseHistoryService {
    @Autowired
    private AuToRunningCaseHistoryMapper auToRunningCaseHistoryMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IAuToRunningCaseHistoryService auToRunningCaseHistoryService;

    @Override
    public Page<RunningCaseHistory> queryHistoryListData(Page page, RunningCaseHistory params) {
        return page.setRecords(auToRunningCaseHistoryMapper.getRunningCaseHistoryList(params));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInsert(RunningCase runningCase, String operationType) {
        RunningCaseHistory runningCaseHistory = new RunningCaseHistory();
        BeanUtils.copyProperties(runningCase,runningCaseHistory);
        //获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        runningCaseHistory.setUpdateBy(sysUser.getId());
        runningCaseHistory.setUpdateTime(new Date());
        runningCaseHistory.setTestTaskId(runningCase.getId());
        runningCaseHistory.setOperationType(operationType);
        runningCaseHistory.setId("");
        runningCaseHistory.setSort(getMaxSortByTaskId(runningCase.getTestTaskId()));
        //添加历史记录
        auToRunningCaseHistoryMapper.insert(runningCaseHistory);

        return true;
    }

//    @Override
//    public IPage<Map<String, Object>> getOperationHistoryList(Page page, RunningCaseHistory params) {
//        IPage<Map<String, Object>> historyList = runningCaseHistoryMapper.getOperationHistoryList(page,params);
//        List<Map<String, Object>> recordList = historyList.getRecords();
//        return historyList;
//    }

    @Override
    public String getUsernamesByIds(Object userIds) {
        if(StringUtils.isEmpty(userIds)) {
            return "";
        }
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.in("id",((String)userIds).split(","));
        List<SysUser> users = sysUserService.list(sysUserQueryWrapper);
        String userNames = users.stream().map(SysUser::getRealname).collect(Collectors.joining(","));
        return userNames;
    }


    private Integer getMaxSortByTaskId(String testTaskId){
        QueryWrapper<RunningCaseHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("test_task_id", testTaskId);
        queryWrapper.orderByDesc("create_time");
        List<RunningCaseHistory> list = auToRunningCaseHistoryService.list(queryWrapper);
        if (list == null || list.size() == 0) {
            return 0;
        } else {
            Integer currentSort = list.get(0).getSort();
            return currentSort + 1;
        }
    }
}
