package org.jeecg.modules.autoVideo.controller;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.autoVideo.entity.AutoVideo;
import org.jeecg.modules.autoVideo.service.IAutoVideoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.common.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.service.IpfsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 视频列表
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Api(tags = "视频列表")
@RestController
@RequestMapping("/autoVideo")
@Slf4j
public class AutoVideoController extends JeecgController<AutoVideo, IAutoVideoService> {
    @Autowired
    private IAutoVideoService autoVideoService;

    @Value(value = "${ipfs.queryUrl}")
    private String ipfsQueryUrl;

    @Resource
    IpfsService ipfsService;


    /**
     * 分页列表查询
     *
     * @param autoVideo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "视频列表-分页列表查询")
    @ApiOperation(value = "视频列表-分页列表查询", notes = "视频列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AutoVideo autoVideo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        QueryWrapper<AutoVideo> queryWrapper = QueryGenerator.initQueryWrapper(autoVideo, req.getParameterMap());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        if (StringUtils.isNotBlank(autoVideo.getScriptId())) {
            queryWrapper.eq(CommonConstant.DATA_STRING_SCRIPT_ID, autoVideo.getScriptId());
        }
        Page<AutoVideo> page = new Page<AutoVideo>(pageNo, pageSize);
        IPage<AutoVideo> pageList = autoVideoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param autoVideo
     * @return
     */
    @AutoLog(value = "视频列表-添加")
    @ApiOperation(value = "视频列表-添加", notes = "视频列表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AutoVideo autoVideo) {
        autoVideoService.save(autoVideo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param autoVideo
     * @return
     */
    @AutoLog(value = "视频列表-编辑")
    @ApiOperation(value = "视频列表-编辑", notes = "视频列表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AutoVideo autoVideo) {
        autoVideoService.updateById(autoVideo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "视频列表-通过id删除")
    @ApiOperation(value = "视频列表-通过id删除", notes = "视频列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        autoVideoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "视频列表-批量删除")
    @ApiOperation(value = "视频列表-批量删除", notes = "视频列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.autoVideoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "视频列表-通过id查询")
    @ApiOperation(value = "视频列表-通过id查询", notes = "视频列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AutoVideo autoVideo = autoVideoService.getById(id);
        if (autoVideo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(autoVideo);
    }

    /**
     * 通过脚本id查询
     *
     * @param autoVideo
     * @return
     */
    @AutoLog(value = "视频列表-通过脚本id查询")
    @ApiOperation(value = "视频列表-通过脚本id查询", notes = "视频列表-通过脚本id查询")
    @GetMapping(value = "/queryByScriptId")
    public Result<?> queryByScriptID(AutoVideo autoVideo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AutoVideo> queryWrapper = QueryGenerator.initQueryWrapper(autoVideo, req.getParameterMap());
        Page<AutoVideo> page = new Page<AutoVideo>(pageNo, pageSize);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        if(StringUtils.isNotBlank(autoVideo.getScriptId())){
            queryWrapper.eq(CommonConstant.SCRIPT_ID, autoVideo.getScriptId());
        }
        IPage<AutoVideo> pageList = autoVideoService.page(page, queryWrapper);
        return Result.OK(pageList);

    }

    /**
     * 导出excel
     *
     * @param request
     * @param autoVideo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AutoVideo autoVideo) {
        return super.exportXls(request, autoVideo, AutoVideo.class, "视频列表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AutoVideo.class);
    }


    @AutoLog(value = "视频播放")
    @ApiOperation(value="视频播放", notes="视频播放")
    @GetMapping(value = "/openFileById")
    public Result<?> openFileByScriptId(@RequestParam(name="id") String id) {
        try {
            if (StringUtils.isEmpty(id)){
                return Result.error("缺少Id");
            }
            AutoVideo video = autoVideoService.getById(id);
            if (video==null||StringUtils.isBlank(video.getVideoAddress())){
                return Result.error("视频不存在");
            }
            String snowflakeDirectoryPath =  "/home/" + video.getVideoAddress()+"/"+video.getName();
            Object filesLs = ipfsService.filesLs(snowflakeDirectoryPath);
            List<JSONObject> objectList = JSONObject.parseObject(JSON.toJSONString(filesLs), new TypeReference<List<JSONObject>>() {});
            String videoPath= snowflakeDirectoryPath+"/"+objectList.get(0).get("Name").toString();
            String videoHash = ipfsService.filesStatHash(videoPath);
            String fileUrl=ipfsQueryUrl+videoHash;
            return Result.OK(fileUrl);
        } catch (Exception e) {
            log.error("视频不存在异常"+e.getMessage());
            return Result.error("视频不存在异常"+e.getMessage());
        }
    }

}
