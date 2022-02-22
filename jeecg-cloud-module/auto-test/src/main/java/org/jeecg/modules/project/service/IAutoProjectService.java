package org.jeecg.modules.project.service;
import org.jeecg.modules.project.entity.AutoProject;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 项目管理
 * @Author: zll
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IAutoProjectService extends IService<AutoProject> {

    /**
     * 获取项目下拉列表
     * @return
     */
    List<AutoProject> getProjectList();

    boolean delete(String id);

    boolean edit(AutoProject autoProject);
}
