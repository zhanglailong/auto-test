package org.jeecg.modules.serve.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.serve.entity.ServerUpReportPara;
import org.jeecg.modules.serve.entity.ServerUpScriptPara;
import org.jeecg.modules.serve.service.ServeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author yeyl
 */
@Api(tags = "服务接口")
@RestController
@RequestMapping("/api/server")
@Slf4j
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class ServeController {
    @Resource
    ServeService serveService;

    @Resource
    private IAutoScriptService autoScriptService;

    @AutoLog(value = "脚本-上传")
    @ApiOperation(value = "脚本-上传", notes = "脚本-上传")
    @PostMapping(value = "/uploadScript")
    public Result<?> uploadScript(@RequestBody @Valid ServerUpScriptPara serverUpScriptPara) {
        try {
            if (serveService.uploadScript(serverUpScriptPara)) {
                return Result.OK(serverUpScriptPara.getScriptName() + ": 脚本上传成功");
            }
        } catch (Exception e) {
            log.error("脚本上传异常:" + e.getMessage());
            return Result.error("脚本上传异常" + e.getMessage());
        }
        return Result.error("脚本上传失败");
    }

    @AutoLog(value = "报告-上传")
    @ApiOperation(value = "报告-上传", notes = "报告-上传")
    @PostMapping(value = "/uploadReport")
    public Result<?> uploadReport(@RequestBody @Valid ServerUpReportPara serverUpReportPara) {
        try {
            if (serveService.uploadReport(serverUpReportPara)) {
                return Result.OK(serverUpReportPara.getScriptName() + "报告上传服务器成功");
            }
        } catch (Exception e) {
            log.error("报告上传服务器异常:" + e.getMessage());
            return Result.error("报告上传异常" + e.getMessage());
        }
        return Result.error("报告上传失败");
    }

    @AutoLog(value = "脚本下发回调")
    @ApiOperation(value = "脚本下发回调", notes = "脚本下发回调")
    @PostMapping(value = "/script/callback")
    public Result<?> scriptCallback(@RequestParam(name = "scriptCode") String scriptCode) {
        try {
            if (StringUtils.isEmpty(scriptCode)) {
                return Result.error("回调唯一标识不能为空");
            }
            if (autoScriptService.playBackStartScript(scriptCode).equals(CommonConstant.DATA_STR_0)) {
                return Result.OK(scriptCode + "回调成功");
            }
        } catch (Exception e) {
            log.error("脚本下发回调异常:" + e.getMessage());
            return Result.error("脚本下发回调异常" + e.getMessage());
        }
        return Result.error("脚本下发回调失败");
    }

}
