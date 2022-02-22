package org.jeecg.modules.uut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.uut.entity.RunningUutList;
import org.jeecg.modules.uut.vo.RunningUutListVo;

/**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
public interface IRunningUutListService extends IService<RunningUutList> {
	
	/**
	 * 根据字段查唯一实体
	 * @param fieldname true
	 * @param value true
	 * @return 没有返回值
	 */
	public RunningUutList findUniqueBy(String fieldname, String value);

	/**
	 * 根据字段查唯一实体
	 */
	public RunningUutListVo findUniqueVoBy(String fieldname, String value);

	/**
	 * 通过id查询被测对象最高版本
	 * @param id
	 * @return String
	 */
	public String queryUutVersionById(String id);
}
