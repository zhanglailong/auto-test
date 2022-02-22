package org.jeecg.modules.randomData.controller;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author zlf
 */
@Api(tags = "随机数服务接口")
@RestController
@RequestMapping("/api/random")
@Slf4j
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class RandomDataController {

    @AutoLog(value = "获取随机数据")
    @ApiOperation(value = "获取随机数据", notes = "获取随机数据")
    @PostMapping(value = "/data")
    public Result<?> getData(@RequestParam(name = "type") Integer type
            ,@RequestParam(name = "startNum") Integer startNum
            ,@RequestParam(name = "endNum") Integer endNum
            ,@RequestParam(name = "num") Integer num) {
        try {
            if (type != null &&num != null && startNum != null && endNum != null){
                ArrayList<Integer> randomList = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    randomList.add(RandomUtil.randomInt(startNum, endNum));
                }
                return Result.OK(randomList);
            }
        } catch (Exception e) {
            log.error("获取随机数据异常:" + e.getMessage());
            return Result.error("获取随机数据异常" + e.getMessage());
        }
        return Result.error("获取随机数据失败");
    }
}
