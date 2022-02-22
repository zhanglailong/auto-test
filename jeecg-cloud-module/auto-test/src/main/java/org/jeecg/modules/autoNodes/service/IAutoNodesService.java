package org.jeecg.modules.autoNodes.service;

import org.jeecg.modules.autoNodes.entity.AutoNodes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 节点管理
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IAutoNodesService extends IService<AutoNodes> {

    void delete(String id);

    boolean updateNode(AutoNodes autoNodes);

    /**
     * 根据节点id集合查找几点信息
     * @param nodeIds 节点id集合
     * @return 集合
     */
    List<AutoNodes> getAutoNodesByNodeIds(String nodeIds);
}
