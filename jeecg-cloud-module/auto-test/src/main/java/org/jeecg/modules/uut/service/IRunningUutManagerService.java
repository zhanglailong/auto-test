package org.jeecg.modules.uut.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.uut.entity.RunningUutManager;

import java.util.List;
import java.util.Map;

/**
 * @Description: 被测对象流程任务表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
public interface IRunningUutManagerService extends IService<RunningUutManager> {
	/**
	 * 根据实例id查流程发起的实体
	 * @param instanceId true
	 * @return 没有返回值
	 */
	public RunningUutManager getStartInstance(String instanceId);
	/**
	 * 根据实例id查流程当前的实体
	 * @param instanceId true
	 * @return 没有返回值
	 */
	public RunningUutManager getCurrentInstance(String instanceId);
	/**
	 * 根据实例id查流程所有的实体
	 */
	public List<RunningUutManager> getList(String instanceId);

	/**
	 * 分页查询
	 * @param page true
	 * @param map true
	 * @return 没有返回值
	 */
	Page<RunningUutManager> queryPageList(Page<RunningUutManager> page,
                                          Map<String, Object> map);

	/**
	 * 分页查询
	 * @param page true
	 * @param map true
	 * @return 没有返回值
	 */
	Page<RunningUutManager> getPageList(Page<RunningUutManager> page,
                                        Map<String, Object> map);
}
