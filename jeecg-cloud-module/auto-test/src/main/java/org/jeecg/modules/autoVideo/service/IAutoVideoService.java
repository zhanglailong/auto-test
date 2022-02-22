package org.jeecg.modules.autoVideo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.autoVideo.entity.AutoVideo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.autoVideo.mapper.AutoVideoMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 视频列表
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IAutoVideoService extends IService<AutoVideo> {

    AutoVideo getVideo(String scriptId);
}
