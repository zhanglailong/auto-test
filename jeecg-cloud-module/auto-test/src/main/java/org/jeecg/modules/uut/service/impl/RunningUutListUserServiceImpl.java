package org.jeecg.modules.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.uut.entity.RunningUutListUser;
import org.jeecg.modules.uut.mapper.RunningUutListUserMapper;
import org.jeecg.modules.uut.service.IRunningUutListUserService;
import org.springframework.stereotype.Service;


/**
 * @Description: 被测对象出库
 * @Author: jeecg-boot
 * @Date:   2020-12-17
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutListUserServiceImpl extends ServiceImpl<RunningUutListUserMapper, RunningUutListUser> implements IRunningUutListUserService {
	
}
