package org.jeecg.modules.uut.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.uut.entity.RunningUutManager;

import java.util.List;
import java.util.Map;

/**
 * @Description: 被测对象流程任务表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
public interface RunningUutManagerMapper extends BaseMapper<RunningUutManager> {
	/**
	 * 获取对象
	 * @param instanceId true
	 * @return 没有返回值
	 * */
	public RunningUutManager getStartInstance(@Param("instanceId") String instanceId);
	/**
	 * 获取对象
	 * @param instanceId true
	 * @return 没有返回值
	 * */
	public RunningUutManager getCurrentInstance(@Param("instanceId") String instanceId);
	/**
	 * 获取遍历页面
	 * @param map true
	 * @return List<RunningUutManager>
	 * */
	public List<RunningUutManager> queryPageList(Map<String, Object> map);
	/**
	 * 获取页面信息
	 * @param map true
	 * @return  List<RunningUutManager
	 * */
	public List<RunningUutManager> getPageList(Map<String, Object> map);
	/**
	 * 获取页面信息
	 * @param instanceId
	 * @return  List<RunningUutManager
			* */
	public List<RunningUutManager> getList(@Param("instanceId") String instanceId);

}
