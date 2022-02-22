package org.jeecg.modules.eval.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.eval.entity.EvalAnalysisResult;
import org.jeecg.modules.eval.vo.EvalAnalysisResultVO;

import java.util.List;

/**
 * @Description: 分析评价结果表
 * @Author: jeecg-boot
 * @Date:   2020-12-30
 * @Version: V1.0
 */
@Mapper
public interface EvalAnalysisResultMapper extends BaseMapper<EvalAnalysisResult> {
    /**
     * 查询列表
     * @param page true
     * @param uutIds true
     * @param evaluate true
     * @param projectId true
     * @return 无返回值
     * */
    List<EvalAnalysisResultVO> queryList(Page<EvalAnalysisResultVO> page, List<Object> uutIds, String evaluate, String projectId);

    @Select("SELECT\n"
            +"mar.id AS id,\n"
            +"mar.uut_id AS uutId,\n"
            +"rul.uut_name AS uutName,\n"
            +"rp.project_name AS projectName,\n"
            +"rt.task_name AS taskName,\n"
            +"rc.test_name AS testName,\n"
            +"mar.score AS score,\n"
            +"mar.evaluate AS evaluate,\n"
            +"mar.system_id AS systemId,\n"
            +"DATE_FORMAT( mar.create_time,'%Y-%m-%d %T') AS createTime\n"
            + "FROM\n"
            + "eval_analysis_result AS mar\n"
            + "LEFT JOIN running_uut_list rul ON mar.uut_id = rul.id\n"
            + "LEFT JOIN running_project rp ON mar.project_id = rp.id\n"
            +"LEFT JOIN running_task rt ON mar.task_id = rt.id\n"
            +"LEFT JOIN running_case rc ON mar.case_id = rc.id\n"
            +"WHERE mar.process_status = 1 AND mar.id = #{id}")
    EvalAnalysisResultVO evalResult(String id);

    /**
     * 查询列表
     * @param projectId true
     * @return String
     * */
    String getEvaluation(String projectId);
}
