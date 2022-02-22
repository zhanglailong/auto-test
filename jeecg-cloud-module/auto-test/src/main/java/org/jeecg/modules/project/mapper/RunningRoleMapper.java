package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.RunningRole;
import org.jeecg.modules.project.entity.RunningRoleHistory;

import java.util.List;
import java.util.Map;

/**
 * @Description: 项目角色表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface RunningRoleMapper extends BaseMapper<RunningRole> {

	/**
	 * 获取信息列表
	 * @param page true
	 * @param createTime true
	 * @param roleId true
	 * @return List<RunningRole>
	 * */
	List<RunningRole> getListData(Page<RunningRole> page, @Param("roleId") String roleId, @Param("createTime") String createTime);


	@Select(" select id,role_name from running_role where del_flag=0 ")
	/**
	 * 获取角色信息
	 * @return List<RunningRole>
	 * */
	List<RunningRole> getRoleData();


	@Select(" select role_name from running_role where id=#{roleId} ")
	/**
	 * 获取角色名
	 * @param roleId true
	 * @return 没有返回值
	 * */
	String getRoleNameByRoleId(String roleId);

	/**
	 * 查询角色管理操作历史记录
	 * @param page true
	 * @param params true
	 * @return Page<Map<String,Object>>
	 * */
	IPage<Map<String,Object>> getOperationHistoryList(Page page, @Param("params") RunningRoleHistory params);


}
