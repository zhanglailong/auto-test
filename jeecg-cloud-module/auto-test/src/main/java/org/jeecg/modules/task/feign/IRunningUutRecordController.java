package org.jeecg.modules.task.feign;

import org.jeecg.common.api.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("jeecg-system-uut")
public interface IRunningUutRecordController {

    @RequestMapping(value = "/record/runningUutRecord/queryById/feign", method = RequestMethod.GET)
    public Result<?> queryByIdFeign(@RequestParam(name = "id", required = true) String id);

}
