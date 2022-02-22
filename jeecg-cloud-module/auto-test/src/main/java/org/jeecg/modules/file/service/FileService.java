package org.jeecg.modules.file.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.ipfs.api.NamedStreamable;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.script.entity.ReturnUpload;
import org.jeecg.modules.service.IpfsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.jeecg.modules.common.FileUtils.*;

/**
 * 接口转发
 * @author yeyl
 * @version V1.0
 * @date 2021/8/254
 */
@Service
@Slf4j
public class FileService {
    @Resource
    IpfsService ipfsService;

    /**
     * ipfs脚本路径
     */
    @Value(value = "${ipfs.queryUrl}")
    private String ipfsQueryUrl;

    public long uploadFileAndUnCompress( File upFile,long filePathId) throws Exception {
            String format = upFile.getName().substring(upFile.getName().lastIndexOf(".") + 1);
            // /home/1234457677/
            String arg="/home/"+filePathId+"/";
            //不是文件夹 判断是否是压缩包zip 7z 解压
            if (CommonConstant.DATA_FORMAT_ZIP.equals(format)||CommonConstant.DATA_FORMAT_7Z.equals(format)){
                //解压 压缩包
                unCompress(upFile.getPath() , format);
                //压缩后文件路径 C:/home/2345666/123
                String filePath = noSuffixPath(upFile.getPath());
                if (StringUtils.isNotBlank(filePath)) {
                    //找到压缩后文件
                    // /home/1234457677/1111.zip
                    File file = new File(filePath);
                    arg=arg+file.getName();
                    if (ipfsService.addFileSection(file,arg ,filePathId)) {
                        return  filePathId;
                    }
                }
            }else{
                // /home/1234457677/1111
                arg=arg+upFile.getName();
                ipfsService.writeFile(new NamedStreamable.FileWrapper(upFile),arg,filePathId);
                return  filePathId;
            }
        return 0;
    }

    public ReturnUpload inportFileAndUnCompress(File upFile, long filePathId) throws Exception {
        String format = upFile.getName().substring(upFile.getName().lastIndexOf(".") + 1);
        // /home/1234457677/
        String arg="/home/"+filePathId+"/";
        //不是文件夹 判断是否是压缩包zip 7z 解压
        if (CommonConstant.DATA_FORMAT_ZIP.equals(format)||CommonConstant.DATA_FORMAT_7Z.equals(format)){
            //解压 压缩包
            unCompress(upFile.getPath() , format);
            //压缩后文件路径 C:/home/2345666/123
            String filePath = noSuffixPath(upFile.getPath());
            if (StringUtils.isNotBlank(filePath)) {
                //找到压缩后文件
                // /home/1234457677/1111.zip
                File file = new File(filePath);
                arg=arg+file.getName();
                ReturnUpload returnUpload = new ReturnUpload();
                returnUpload.setFilePathId(String.valueOf(filePathId));
                returnUpload.setFileName(file.getName());
                File[] files = file.listFiles();
                for (File f : files) {
                    if(f.getName().indexOf(" ")!=-1){
                        throw new UnsupportedOperationException("该压缩文件下，文件格式不支持!");
                    }
                }
                if (ipfsService.addFileSection(file,arg ,filePathId)) {
                    return returnUpload;
                }
            }
        }else{
            // /home/1234457677/1111
            arg=arg+upFile.getName();
            ReturnUpload returnUpload = new ReturnUpload();
            returnUpload.setFilePathId(String.valueOf(filePathId));
            returnUpload.setFileName(upFile.getName());
            ipfsService.writeFile(new NamedStreamable.FileWrapper(upFile),arg,filePathId);
            return  returnUpload;
        }
        return null;
    }


    public String uploadImg(File upFile, String scriptName, long filePathId) throws IOException {
        String arg="/home/"+filePathId+"/"+scriptName+"/"+upFile.getName();
        if (upFile.isFile()){
            //写入文件
            ipfsService.writeFile(new NamedStreamable.FileWrapper(upFile),arg,filePathId);
            String imgHash = ipfsService.filesStatHash(arg);
           return ipfsQueryUrl+imgHash;
        }else{
            return null;
        }
    }

    public List<JSONObject> imgList( String scriptName, long filePathId) throws IOException {
        String snowflakeDirectoryPath =  "/home/" + filePathId+"/"+scriptName;
        Object filesLs = ipfsService.filesLs(snowflakeDirectoryPath);
        List<JSONObject> objectList = JSONObject.parseObject(JSON.toJSONString(filesLs), new TypeReference<List<JSONObject>>() {});
        //webp,bmp,jpg,png,tif,gif,apng ,jfif
        List<JSONObject> names = objectList.stream().filter(o -> {
            String fileName = o.get("Name").toString();
            return fileName.contains(".jfif")
                    || fileName.contains(".bmp")
                    || fileName.contains(".tif")
                    || fileName.contains(".webp")
                    || fileName.contains(".gif")
                    || fileName.contains(".png")
                    || fileName.contains(".jpg")
                    || fileName.contains(".apng")
                    || fileName.contains(".jpeg");
        }).collect(Collectors.toList());
         log.info(names.toString());
        for (JSONObject name : names) {
            String imgPath= snowflakeDirectoryPath+"/"+name.get("Name").toString();
            String viewHash = ipfsService.filesStatHash(imgPath);
            name.put("Hash",viewHash);
            name.put("ImgUrl",ipfsQueryUrl+viewHash);
        }
        return names;
    }

    /**
     * 本地文件下载
     *
     * @param response
     * @param filePath
     * @throws Exception
     */
    public String download(String filePath,HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        response.setContentType("text/html;charset=UTF-8");
        String downloadFilePath = null;
        try {
            String OSName = System.getProperties().getProperty("os.name").toLowerCase();
            if (OSName.contains("windows")) {
                downloadFilePath = "c://" + filePath;
            }
            File file = new File(downloadFilePath);
            if (file.exists()) {
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //下载文件名称删除前15位时间戳加随机数
//                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1").substring(14));
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
        return downloadFilePath;
    }

}
