package org.jeecg.modules.file.controller;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.config.SnowflakeConfig;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FileUtils;
import org.jeecg.modules.common.IscTools;
import org.jeecg.modules.file.service.FileService;
import org.jeecg.modules.script.entity.AutoScript;
import org.jeecg.modules.script.service.IAutoScriptService;
import org.jeecg.modules.service.IpfsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.Format;

import static org.jeecg.modules.common.FileUtils.*;
import static org.jeecg.modules.common.FileUtils.createFile;

/**
 * @author yeyl
 */
@Api(tags="文件接口")
@RestController
@RequestMapping("/api/file")
@Slf4j
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class FileController {
    @Resource
    IpfsService ipfsService;
    @Resource
    SnowflakeConfig snowflakeConfig;
    @Resource
    IAutoScriptService iAutoScriptService;

    @Resource
    FileService fileService;

    @AutoLog(value = "文件上传")
    @ApiOperation(value = "文件上传", notes = "文件上传")
    @ResponseBody
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file ,@RequestParam("filePathId") long filePathId) {
        long snowflakeId;
        if (filePathId>0){
            snowflakeId=filePathId;
        }else{
            snowflakeId  = snowflakeConfig.snowflakeId();
        }
        //雪花路径
        String snowflakeDirectoryPath =File.separator + "home" + File.separator + snowflakeId;
        if (IscTools.isWindowsOS()) {
            snowflakeDirectoryPath="C:"+snowflakeDirectoryPath;
        }
        try {
            //获取上传文件名称
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(originalFilename)) {
                //分割文件名成名称加后缀
                String[] fileName = originalFilename.split(fileDot);
                if (fileName.length >= 1) {
                    String path= snowflakeDirectoryPath+File.separator + fileName[0] + "." + fileName[1];
                    File upFile = new File(path);
                    createFile(upFile);
                    file.transferTo(upFile);
                    return Result.OK(fileService.uploadFileAndUnCompress(upFile,snowflakeId));
                }
            }
        } catch (Exception e) {
            log.error("文件上传异常" + e.getMessage());
            return Result.error("上传异常： "+e.getMessage());
        }finally {
           //删除本地雪花算法下的整个文件夹
            File snowFolder = new File(snowflakeDirectoryPath);
            System.gc();
            log.info( deleteFile(snowFolder)?"成功"+"删除"+snowFolder.getName()+"文件夹或者文件":"失败"+"删除"+snowFolder.getName()+"文件夹或者文件");

        }
        return Result.error("上传失败");
    }


//    /**
//     * ipfs文件下载
//     * */
//    @AutoLog(value = "文件下载")
//    @ApiOperation(value = "文件下载", notes = "文件下载")
//    @GetMapping(value = "/download")
//    public Result<?> download( @RequestParam("fileHash") String fileHash) {
//        try {
//            byte[] bytes = ipfsService.downloadFile(fileHash);
//            if (bytes != null) {
//                return   Result.OK(bytes);
//            }
//        } catch (Exception e) {
//            log.error(fileHash+"文件下载异常" + e.getMessage());
//            return Result.error(fileHash+"文件下载异常"+e.getMessage());
//        }
//        return Result.error(fileHash+"文件不存在");
//    }

    /**
     * ipfs文件下载
     * */
    @AutoLog(value = "文件下载")
    @ApiOperation(value = "文件下载", notes = "文件下载")
    @GetMapping(value = "/download")
    public Result<?> download(HttpServletResponse response,@RequestParam("fileHash") String fileHash) {
        try {
            if (StringUtils.isEmpty(fileHash)){
                return Result.error("文件id不能为空!");
            }
            String hash = ipfsService.filesStatHash("/home/"+fileHash);
            String downloadUrl = ipfsService.download("/"+fileHash, hash);
            if (StringUtils.isNotBlank(downloadUrl)) {
                //压缩文件
                FileUtils.compress(downloadUrl, CommonConstant.DATA_FORMAT_ZIP);
                //删除下载的文件夹
                deleteLocalFile(fileHash);
                if (StringUtils.isNotBlank(downloadUrl)) {
                    //下载压缩包
                    FileUtils.downloadZip(response,fileHash);
                    return  Result.OK("文件下载成功，路径是："+downloadUrl);
                }
            }
        } catch (Exception e) {
            log.error(fileHash+"文件下载异常" + e.getMessage());
            return Result.error(fileHash+"文件下载异常"+e.getMessage());
        }
        return Result.error(fileHash+"文件不存在");
    }

    @AutoLog(value = "脚本上传图片")
    @ApiOperation(value = "脚本上传图片", notes = "脚本上传图片")
    @ResponseBody
    @PostMapping("/uploadImg")
    public Result<?> uploadImg(@RequestParam("file") MultipartFile file  ,@RequestParam("scriptId") String scriptId) {
        AutoScript autoScript = iAutoScriptService.getById(scriptId);
        if (StringUtils.isBlank(scriptId)
                ||autoScript==null
                ||StringUtils.isBlank(autoScript.getScriptContent())
                ||StringUtils.isBlank(autoScript.getScriptName())
        ){
            return Result.error("脚本相关信息不能为空！");
        }
        String filePathId = autoScript.getScriptContent();
        //雪花路径
        String snowflakeDirectoryPath =File.separator + "home" + File.separator + filePathId;
        if (IscTools.isWindowsOS()) {
            snowflakeDirectoryPath="C:"+snowflakeDirectoryPath;
        }
        try {
            //获取上传文件名称
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(originalFilename)) {
                //分割文件名成名称加后缀
                String[] fileName = originalFilename.split(fileDot);
                if (fileName.length >= 1) {
                    String path= snowflakeDirectoryPath+File.separator+autoScript.getScriptName() +File.separator+ fileName[0] + "." + fileName[1];
                    File upFile = new File(path);
                    createFile(upFile);
                    file.transferTo(upFile);
                    return Result.OK(fileService.uploadImg(upFile,autoScript.getScriptName(), Long.parseLong(filePathId)));
                }
            }
        } catch (Exception e) {
            log.error("文件上传异常" + e.getMessage());
            return Result.error("上传异常： "+e.getMessage());
        }finally {
            //删除本地雪花算法下的整个文件夹
            File snowFolder = new File(snowflakeDirectoryPath);
            System.gc();
            log.info( deleteFile(snowFolder)?"成功"+"删除"+snowFolder.getName()+"文件夹或者文件":"失败"+"删除"+snowFolder.getName()+"文件夹或者文件");
        }
        return Result.error("上传失败");
    }


    @AutoLog(value = "脚本图片列表")
    @ApiOperation(value = "脚本图片列表", notes = "脚本图片列表")
    @ResponseBody
    @PostMapping("/imgList")
    public Result<?> imgList(@RequestParam("scriptId") String scriptId) {
        try {
            AutoScript autoScript = iAutoScriptService.getById(scriptId);
            if (StringUtils.isBlank(scriptId)
                    ||autoScript==null
                    ||StringUtils.isBlank(autoScript.getScriptContent())
                    ||StringUtils.isBlank(autoScript.getScriptName())
            ){
                return Result.error("脚本相关信息不能为空！");
            }
            return Result.OK(fileService.imgList(autoScript.getScriptName(),Long.parseLong(autoScript.getScriptContent())));
        }catch (Exception e){
            log.error("脚本Id:"+scriptId+"查看脚本图片列表失败" + e.getMessage());
            return Result.error("查看脚本图片列表失败" + e.getMessage());
        }
    }

    /**
     * 本地文件下载
     * */
    @AutoLog(value = "本地文件下载")
    @ApiOperation(value = "本地文件下载", notes = "本地文件下载")
    @GetMapping(value = "/downloadLocalZip")
    public void downloadZip(HttpServletResponse response, String filePath) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        response.setContentType("text/html;charset=UTF-8");
        try {
            String downloadFilePath;
            String osName = System.getProperties().getProperty("os.name").toLowerCase();
            if (osName.contains(CommonConstant.WINDOWS)) {
                downloadFilePath = "c://" + filePath;
            } else {
                if (!filePath.substring(CommonConstant.DATA_INT_0, CommonConstant.DATA_INT_5).equals(CommonConstant.DOWNLOAD_ADDRESS_Home)) {
                    downloadFilePath = "/home" + filePath;
                } else {
                    downloadFilePath = filePath;
                }
            }
            File file = new File(downloadFilePath);
            if (file.exists()) {
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //下载文件名称删除前15位时间戳加随机数
                response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
                String name = file.getName();
                System.out.println(name);
                inputStream = new BufferedInputStream(new FileInputStream(file));
                outputStream = response.getOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            } else {
                response.getWriter().write(JSON.toJSONString(Result.error("文件不存在")));
            }

        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 本地文件下载
     *
     * @param response
     * @param filePath
     * @throws Exception
     */
    public static  void scriptDownload(String filePath,HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        response.setContentType("text/html;charset=UTF-8");
        try {
            String downloadFilePath;
            String osName = System.getProperties().getProperty("os.name").toLowerCase();
            if (osName.contains(CommonConstant.WINDOWS)) {
                downloadFilePath = "c://" + filePath;
            } else {
                if (!filePath.substring(CommonConstant.DATA_INT_0, CommonConstant.DATA_INT_5).equals(CommonConstant.DOWNLOAD_ADDRESS_Home)) {
                    downloadFilePath = "/home" + filePath;
                } else {
                    downloadFilePath = filePath;
                }
            }
            File file = new File(downloadFilePath);
            if (file.exists()) {
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //下载文件名称删除前15位时间戳加随机数
                response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
//                response.setContentType("application/octet-stream; charset=utf-8");
//                response.addHeader("Access-Control-Expose-Headers","token,uid,Content-Disposition");
//                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//                response.addHeader("Access-Control-Allow-Headers", "Content-Type");
//                response.addHeader("Access-Control-Allow-Credentials","true");
                String name = file.getName();
                System.out.println(name);
                inputStream = new BufferedInputStream(new FileInputStream(file));
                outputStream = response.getOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            } else {
                response.getWriter().write(JSON.toJSONString(Result.error("文件不存在")));
            }

        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
