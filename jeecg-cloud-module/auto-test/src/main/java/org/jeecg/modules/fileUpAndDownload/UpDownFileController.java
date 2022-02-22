package org.jeecg.modules.fileUpAndDownload;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.WebToolUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @created by zyf 2021/4/28
 * @author zyf
 * 文件上传下载
 */
@Slf4j
@Api(tags = "文件上传下载")
@Controller
@RequestMapping("/file")
@ResponseBody
public class UpDownFileController {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 本地文件上传
     */
    @PostMapping(value = "/uploadLocal")
    public Result<?> uploadLocal(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String type = StringUtils.isEmpty(request.getParameter("type")) ? "DEFAULT" : request.getParameter("type");
        String filePath = applicationName + prefixPath(type);
        // 获取上传文件对象
        List<MultipartFile> file = multipartRequest.getFiles("files");
        if (file.isEmpty()) {
            return Result.error("参数错误");
        }
        List<String> paths = new ArrayList<>();

        for (MultipartFile multipartFile : file) {
            String oName = multipartFile.getOriginalFilename();
            //获取扩展名
            if (oName==null){
                return Result.error("文件名不能为空");
            }
            String suffixName = oName.substring(oName.lastIndexOf("."));
            //随机生成文件的名称
            String fileName = System.currentTimeMillis() + (int) (Math.random() * 10) + "_" + oName.substring(0, oName.lastIndexOf(".")) + suffixName;

            //生成目录
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String format = dateFormat.format(new Date());

            String osName = System.getProperties().getProperty("os.name").toLowerCase();
            StringBuilder sb=new StringBuilder();
            if (osName.contains("windows")) {
                filePath=sb.append("c:/").append(filePath).toString();
            } else {
                filePath=sb.append("/home/").append(filePath).toString();
            }

            // 判断路径是否存在 如果不存在则需要创建
            File subPath = new File(filePath + format);
            if (!subPath.exists()) {
                subPath.mkdirs();
            }
            try {
                multipartFile.transferTo(new File(filePath + format + "/" + fileName));
                paths.add("/" + applicationName + prefixPath(type) + format + "/" + fileName);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                return Result.error("文件上传错误");
            }
            filePath = applicationName + prefixPath(type);
        }

        return Result.OK(paths);
    }

    /**
     * 本地文件下载
     * */
    @AutoLog(value = "本地文件下载")
    @ApiOperation(value = "本地文件下载/", notes = "本地文件下载")
    @GetMapping(value = "/downloadLocal")
    public void download(HttpServletResponse response, String filePath) throws Exception {
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

    @PostMapping(value = "/downloadZipLocal")
    public void downloadZip(@RequestBody JSONObject jsonObject, HttpServletResponse response) throws Exception {
        JSONArray filePaths = jsonObject.getJSONArray("filePaths");
        response.setContentType("application/force-download");
        //下载文件名称删除前15位时间戳加随机数
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String((System.currentTimeMillis() + ".zip").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        OutputStream outputStream = response.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        for (int i = 0; i < filePaths.size(); i++) {
            String path = filePaths.getString(i);
            if (WebToolUtils.isWindowsOS()) {
                path = "c:" + path;
            } else {
                path = "/home" + path;
            }
            File file = new File(path);
            if (file.exists()) {
                InputStream is;
                try {
                    String[] split = path.split("/");
                    String fileName = split[split.length - 1];
                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    is = new FileInputStream(file);
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = is.read(b)) != -1) {
                        zipOutputStream.write(b, 0, len);
                    }
                    zipOutputStream.closeEntry();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        zipOutputStream.flush();
        zipOutputStream.close();
        response.flushBuffer();
    }


    private String prefixPath(String type) {
        String filePath = "/file";
        FilePathType filePathType;
        try {
            filePathType = FilePathType.valueOf(type);
        } catch (Exception e) {
            filePathType = FilePathType.DEFAULT;
        }
        switch (filePathType) {
            case ORDER_RELATED_DOC:
                filePath = filePath + "/ntepmOrder/relatedDoc/";
                break;
            case ORDER_TEST_DOC:
                filePath = filePath + "/ntepmOrder/testDoc/";
                break;
            case INVOICE_PDF:
                filePath = filePath + "/invoice/pdf/";
                break;
            default:
                filePath = filePath + "/default/";
                break;
        }
        return filePath;
    }

    private enum FilePathType {
        //订单相关文档
        ORDER_RELATED_DOC,

        //订单测试文档
        ORDER_TEST_DOC,

        //发票PDF
        INVOICE_PDF,

        //默认
        DEFAULT
    }

}
