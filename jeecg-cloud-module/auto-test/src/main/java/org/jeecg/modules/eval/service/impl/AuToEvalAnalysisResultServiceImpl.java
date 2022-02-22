package org.jeecg.modules.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.eval.entity.EvalAnalysisResult;
import org.jeecg.modules.eval.mapper.AuToEvalAnalysisResultMapper;
import org.jeecg.modules.eval.service.IAuToEvalAnalysisResultService;
import org.jeecg.modules.eval.vo.EvalAnalysisResultVO;
import org.jeecg.modules.uut.entity.RunningUutList;
import org.jeecg.modules.uut.service.IAuToRunningUutListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 分析评价结果表
 * @Author: jeecg-boot
 * @Date:   2021-11-18
 * @Version: V1.0
 */
@Service
public class AuToEvalAnalysisResultServiceImpl extends ServiceImpl<AuToEvalAnalysisResultMapper, EvalAnalysisResult> implements IAuToEvalAnalysisResultService {
    @Autowired
    private AuToEvalAnalysisResultMapper evalAnalysisResultMapper;

    @Autowired
    private IAuToRunningUutListService runningUutListService;

    @Override
    public Page<EvalAnalysisResultVO> queryPageList(Page<EvalAnalysisResultVO> page, String uutName, String evaluate, String projectId) {
        QueryWrapper<RunningUutList> queryWrapper = QueryGenerator.initQueryWrapper(new RunningUutList(), null);
        queryWrapper.like("uut_name", uutName);
        List<Object> uutIds = runningUutListService.listObjs(queryWrapper);
        Page<EvalAnalysisResultVO> result = page.setRecords(evalAnalysisResultMapper.queryList(page, uutIds, evaluate, projectId));
        List<EvalAnalysisResultVO> list = new ArrayList<>();
        for (EvalAnalysisResultVO evalAnalysisResultVO : result.getRecords()) {
            RunningUutList runningUutList = runningUutListService.getById(evalAnalysisResultVO.getUutId());
            if(null != runningUutList){
                evalAnalysisResultVO.setUutName(runningUutList.getUutName());
            }
            list.add(evalAnalysisResultVO);
        }
        result.setRecords(list);
        return result;
    }

    @Override
    public EvalAnalysisResultVO evalResult(String id) {
        return evalAnalysisResultMapper.evalResult(id);
    }

    @Override
    public String getEvaluation(String projectId) {
        return this.getBaseMapper().getEvaluation(projectId);
    }
}
