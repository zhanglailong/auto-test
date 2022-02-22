package org.jeecg.modules.uut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.uut.entity.RunningUutVersion;

import java.util.List;

@Mapper
public interface RunningUutVersionMapper extends BaseMapper<RunningUutVersion> {

	@Select(" SELECT version FROM running_uut_version WHERE id = #{versionId} ")
	/**
	 * 查询被测对象
	 * @param versionId 字段值
	 * @return RunningUutList
	 * */
	String getProjectTurnVersion(String versionId);

}
