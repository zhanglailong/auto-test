package org.jeecg.modules.autoVideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.autoVideo.entity.AutoVideo;
import org.jeecg.modules.autoVideo.mapper.AutoVideoMapper;
import org.jeecg.modules.autoVideo.service.IAutoVideoService;
import org.jeecg.modules.common.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 视频列表
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
public class AutoVideoServiceImpl extends ServiceImpl<AutoVideoMapper, AutoVideo> implements IAutoVideoService {

    @Resource
    AutoVideoMapper autoVideoMapper;

    @Override
    public AutoVideo getVideo(String scriptId) {
        QueryWrapper<AutoVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        queryWrapper.eq(CommonConstant.DATA_STRING_SCRIPT_ID,scriptId);
        return autoVideoMapper.selectOne(queryWrapper);
    }
}
