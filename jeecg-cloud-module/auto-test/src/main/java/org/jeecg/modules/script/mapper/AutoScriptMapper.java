package org.jeecg.modules.script.mapper;
import java.util.List;
import org.jeecg.modules.script.entity.AutoScript;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 脚本管理
 * @Author: zll
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface AutoScriptMapper extends BaseMapper<AutoScript> {
    /**
     * 获取绑定节点树结构
     * @return
     */
    List<AutoScript> treeScript();

    /**
     * 下移，获取下一个对象
     * @param  autoScript 脚本对象
     * @return
     */
    AutoScript moveDown(AutoScript autoScript);

    /**
     * 下移，获取下一个对象
     * @param  autoScript 脚本对象
     * @return
     */
    AutoScript moveUp(AutoScript autoScript);


}
