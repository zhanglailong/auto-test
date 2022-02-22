package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.vo.RunningProjectInfo;

import java.util.List;


/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Mapper
public interface RunningProjectInfoMapper extends BaseMapper<RunningProjectInfo> {

	@Select("SELECT a.id,"
			+ "a.project_name,\n"
			+ "a.project_code,\n"
			+ "a.project_members,\n"
			+ "a.uut_list_id,\n"
			+ "a.cu_file,\n"
			+ "a.begin_date,\n"
			+ "a.finish_date,\n"
			+ "a.remark,\n"
			+ "a.review_file\n"
			+ "FROM `running_project` as a where a.id= #{id} ")
	/**
	 * 获取数据信息
	 * @param id true
	 * @return List<RunningProjectInfo>
	 * */
	List<RunningProjectInfo> getListDataById(String id);

	/**
	 * 获取数据信息
	 * @param projectName true
	 * @param projectId true
	 * @param createTime true
	 * @param projectCode true
	 * @return List<RunningProjectInfo>
	 * */
	List<RunningProjectInfo> getListData(@Param("projectName") String projectName, @Param("projectCode") String projectCode, @Param("createTime") String createTime, @Param("projectId") String projectId);

	/**
	 * 获取已归档数据信息
	 * @param projectName true
	 * @param projectId true
	 * @param createTime true
	 * @param projectCode true
	 * @return List<RunningProjectInfo>
	 * */
	List<RunningProjectInfo> getFileProjectListData(@Param("projectName") String projectName, @Param("projectCode") String projectCode, @Param("createTime") String createTime, @Param("projectId") String projectId);
	
	/**获取projectName
	 * @param
	 * @return
	 *
	 * */
	String getProjectName(String projectId);

	/**
	 * 根据项目名称查询项目id
	 * @param projectCode
	 * @return
	 */
	String getProjectId(String projectCode);
}
