package org.jeecg.modules.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.service.IpfsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.*;


/**
 * @author yeyl
 * 2021/08/24
 */
@Slf4j
public class FileUtils {

    @Autowired
    IpfsService ipfsService;
    public static String fileDot = "\\.";


    public static List<String> imgFormatList = new ArrayList<>(8);

    static {
        imgFormatList.add(".jfif");
        imgFormatList.add(".tif");
        imgFormatList.add(".webp");
        imgFormatList.add(".gif");
        imgFormatList.add(".png");
        imgFormatList.add(".jpg");
        imgFormatList.add(".apng");
        imgFormatList.add(".jpeg");
    }

    /**
     * 强制删除被占用文件
     *
     * @param file file
     * @return true 或者 false
     */
    public static boolean forceDelete(File file) {
        boolean result = file.delete();
        int tryCount = 0;
        while (!result && tryCount++ < 20) {
            System.gc();
            //回收资源
            result = file.delete();
        }
        return result;
    }


    /**
     * 删除文件成功或者失败 删除整个文件夹 包括里面内容
     *
     * @param file 文件
     * @return true 或者 false
     */
    public static boolean deleteFile(File file) {
        File[] files = file.listFiles();
        if (!IscTools.isEmptyArray(files)) {
            for (File deleteFile : files) {
                if (deleteFile.isDirectory()) {
                    //如果是文件夹，则递归删除下面的文件后再删除该文件夹
                    if (!deleteFile(deleteFile)) {
                        //如果失败则返回
                        return false;
                    }
                } else {
                    if (!forceDelete(deleteFile)) {
                        //如果失败则返回
                        return false;
                    }
                }
            }
        }
        return forceDelete(file);
    }

    /**
     * 文件压缩成7z
     *
     * @param inputFile  原本文件路径 C:/home/123
     * @param outputFile 压缩到的文件路径 C:/home/123.7z
     * @throws Exception 异常
     */
    public static void compress7z(String inputFile, String outputFile) throws Exception {
        if (!CommonConstant.DATA_STR_7Z.equals(outputFile.substring(outputFile.length() - CommonConstant.DATA_INT_3))) {
            throw new Exception(outputFile + " :压缩格式不正确");
        }
        File input = new File(inputFile);
        if (!input.exists() || !input.isDirectory()) {
            throw new Exception(input.getPath() + "待压缩文件不存在");
        }
        try (SevenZOutputFile out = new SevenZOutputFile(new File(outputFile))) {
            compress7z(out, input, null);
        } catch (Exception e) {
            log.error("7z文件压缩异常:" + e.getMessage());
        }

    }


    /**
     * 递归压缩7z
     *
     * @param out   输出
     * @param input 输入
     * @param name  名字 默认
     */
    public static void compress7z(SevenZOutputFile out, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        SevenZArchiveEntry entry;
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] fileList = input.listFiles();
            //如果文件夹为空，则只需在目的地.7z文件中写入一个目录进入
            if (IscTools.isEmptyArray(fileList)) {
                entry = out.createArchiveEntry(input, name + File.separator);
                out.putArchiveEntry(entry);
            } else {
                //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (File file : fileList) {
                    compress7z(out, file, name + File.separator + file.getName());
                }
            }
        } else {
            //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入7z文件中
            FileInputStream fos = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fos);
            try {
                entry = out.createArchiveEntry(input, name);
                out.putArchiveEntry(entry);
                int len;
                //将源文件写入到7z文件中
                byte[] buf = new byte[1024];
                while ((len = bis.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            } catch (Exception e) {
                log.error("7z文件压缩异常:" + e.getMessage());
            } finally {
                bis.close();
                fos.close();
                out.closeArchiveEntry();
            }

        }
    }


    /**
     * 解压7z文件
     *
     * @param inputFile   带压缩7z文件 C:/home/7ztest.7z
     * @param destDirPath 压缩后文件路径  C:/home/
     * @throws Exception 异常
     */
    public static void unCompress7z(String inputFile, String destDirPath) throws Exception {
        if (!CommonConstant.DATA_STR_7Z.equals(inputFile.substring(inputFile.length() - CommonConstant.DATA_INT_3))) {
            throw new Exception(inputFile + " :压缩文件格式不正确");
        }
        try {
            //获取当前压缩文件
            File srcFile = new File(inputFile);
            // 判断源文件是否存在
            if (!srcFile.exists()) {
                throw new Exception(srcFile.getPath() + "所指文件不存在");
            }
            //开始解压
            SevenZFile zIn = new SevenZFile(srcFile);
            SevenZArchiveEntry entry;
            File file;
            while ((entry = zIn.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    file = new File(destDirPath, entry.getName());
                    if (!file.exists()) {
                        //创建此文件的上级目录
                        log.info(new File(file.getParent()).mkdirs() ? "成功" + "创建文件" + file.getParent() : "失败" + "创建文件" + file.getParent());
                    }
                    OutputStream out = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(out);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = zIn.read(buf)) != -1) {
                        bos.write(buf, 0, len);
                    }
                    bos.close();
                    out.close();
                }

            }
            zIn.close();
        } catch (IOException e) {
            log.error("7z文件解压异常:" + e.getMessage());
        }

    }

    /**
     * 解压zip到指定文件夹
     *
     * @param zipPath C:/home/testZip.zip
     * @param descDir C:/home/testZip/
     * @throws Exception 异常
     */
    public static void unCompressZipFiles(String zipPath, String descDir) throws Exception {
        if (!CommonConstant.DATA_STR_ZIP.equals(zipPath.substring(zipPath.length() - CommonConstant.DATA_INT_4))) {
            throw new Exception(zipPath + " :压缩文件格式不正确");
        }
        unCompressZip(new File(zipPath), descDir);
    }

    /**
     * 解压文件到指定目录
     */
    @SuppressWarnings("rawtypes")
    public static void unCompressZip(File zipFile, String descDir) {
        File pathFile = new File(descDir);
        try {
            if (!pathFile.exists()) {
                boolean mkdirs = pathFile.mkdirs();
                if (!mkdirs) {
                    throw new Exception("解压文件创建失败");
                }
            }
            //解决zip文件中有中文目录或者中文文件
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = descDir.replaceAll("\\*", File.separator) + zipEntryName;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf("/")));
                if (!file.exists()) {
                    boolean mkdirs = file.mkdirs();
                    log.info(mkdirs + file.getName());
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //输出文件路径信息
                System.out.println(outPath);
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            log.error("zip文件解压异常:" + e.getMessage());
        }
    }


    /**
     * @param path   要压缩的文件路径 C:/home/7ztest
     * @param format 生成的格式（zip、rar）
     */
    public static void compressZipOrRar(String path, String format) throws Exception {
        if (!CommonConstant.DATA_FORMAT_ZIP.equals(format) && !CommonConstant.DATA_FORMAT_RAR.equals(format)) {
            throw new Exception(format + " :压缩文件格式不正确");
        }

        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists() || !file.isDirectory()) {
            throw new Exception("路径 " + path + " 不存在文件，无法进行压缩...");
        }
        // 用于存放压缩文件的文件夹
        String generateFile = file.getParent() + File.separator;
        File compress = new File(generateFile);
        // 如果文件夹不存在，进行创建
        if (!compress.exists()) {
            boolean mkdirs = compress.mkdirs();
            if (!mkdirs) {
                throw new Exception("文件创建失败");
            }
        }

        // 目的压缩文件
        String generateFileName = compress.getAbsolutePath() + File.separator + file.getName() + "." + format;
        // 输入流 表示从一个源读取数据
        // 输出流 表示向一个目标写入数据
        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);
        // 压缩输出流
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
            compressZipOrRar(zipOutputStream, file, "");
            System.out.println("源文件位置：" + file.getAbsolutePath() + "，目的压缩文件生成位置：" + generateFileName);
        } catch (Exception e) {
            log.error("zip文件压缩异常:" + e.getMessage());
        }

    }

    /**
     * @param out  输出流
     * @param file 目标文件
     * @param dir  文件夹
     * @throws Exception 异常
     */
    private static void compressZipOrRar(ZipOutputStream out, File file, String dir) throws Exception {
        // 当前的是文件夹，则进行一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();

            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + File.separator));

            dir = dir.length() == 0 ? "" : dir + File.separator;

            //循环将文件夹中的文件打包
            if (!IscTools.isEmptyArray(files)) {
                for (File value : files) {
                    compressZipOrRar(out, value, dir + value.getName());
                }
            }
        } else {
            // 当前是文件
            // 输入流
            try (FileInputStream inputStream = new FileInputStream(file)) {
                // 标记要打包的条目
                out.putNextEntry(new org.apache.tools.zip.ZipEntry(dir));
                // 进行写操作
                int len;
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
            } catch (Exception e) {
                log.error("zip文件压缩异常:" + e.getMessage());
            }
        }

    }


    /**
     * 压缩zip  rar 7z
     *
     * @param inputFile C:/home/7ztest
     * @param format    zip rar 7z
     * @throws Exception 异常
     */
    public static void compress(String inputFile, String format) throws Exception {
        if (!CommonConstant.DATA_FORMAT_ZIP.equals(format) && !CommonConstant.DATA_FORMAT_RAR.equals(format) && !CommonConstant.DATA_FORMAT_7Z.equals(format)) {
            throw new Exception(format + " :压缩文件格式不正确");
        }
        if (CommonConstant.DATA_FORMAT_ZIP.equals(format) || CommonConstant.DATA_FORMAT_RAR.equals(format)) {
            compressZipOrRar(inputFile, format);
        } else {
            String outputFile = inputFile + CommonConstant.DATA_STR_7Z;
            compress7z(inputFile, outputFile);
        }

    }


    /**
     * 解压文件支持zip 7z
     *
     * @param compressPath C:/home/7ztest.7z
     * @param format       zip 7z
     * @throws Exception 异常
     */
    public static void unCompress(String compressPath, String format) throws Exception {
        if (!CommonConstant.DATA_FORMAT_ZIP.equals(format) && !CommonConstant.DATA_FORMAT_7Z.equals(format)) {
            throw new Exception(format + " :压缩文件格式不正确");
        }
        if (CommonConstant.DATA_FORMAT_7Z.equals(format)) {
            // C:/home/
            String destDirPath = getFilePath(compressPath);
            if (StringUtils.isNotBlank(destDirPath)) {
                unCompress7z(compressPath, destDirPath);
            } else {
                throw new Exception(compressPath + " :路径输入错误");
            }
        } else {
            //  C:/home/testZip/
            String destDirPath = compressPath.replace(CommonConstant.DATA_STR_ZIP, "/");
            unCompressZipFiles(compressPath, destDirPath);
        }

    }


    /**
     * @param path C:/home/7ztest.7z
     * @return C:/home/
     */
    public static String getFilePath(String path) {
        int dot = path.lastIndexOf('\\');
        if ((dot > -1) && (dot < (path.length()))) {
            return path.substring(0, dot + 1);
        }
        return null;
    }

    public static void createFile(File file) throws IOException {
        if (!file.exists() && !file.getName().contains(".")) {
            // 能创建多级目录
            log.info(file.mkdirs() ? "成功" + "创建文件夹" + file.getName() : "失败" + "创建文件" + file.getName());
        }
        //返回的是文件夹
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            // 能创建多级目录
            log.info(fileParent.mkdirs() ? "成功" + "创建文件夹" + fileParent.getName() : "失败" + "创建文件" + fileParent.getName());
        }
        if (!file.exists()) {
            //有路径才能创建文件
            log.info(file.createNewFile() ? "成功" + "创建文件" + file.getName() : "失败" + "创建文件" + file.getName());
        }
    }

    /**
     * @param path C:/home/7ztest.7z
     * @return C:/home/7ztest
     */
    public static String noSuffixPath(String path) {
        int dot = path.lastIndexOf('.');
        if ((dot > -1) && (dot < (path.length()))) {
            return path.substring(0, dot);
        }
        return null;
    }

    //下载服务器上的压缩包
    public static void downloadZip(HttpServletResponse response, String filePath) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // path是指欲下载的文件的路径。
            if (IpfsService.isWindowsOS()) {
                if (!filePath.contains("C:\\") && !filePath.contains("C:/")) {
                    filePath = "C:" + filePath;
                }
            } else {
                if (!filePath.contains("\\home\\") && !filePath.contains("/home/")) {
                    filePath = "/home/" + filePath;
                    log.info("filepath ======" + filePath);
                }
            }
            File file = new File(filePath + ".zip");
            // 取得文件名。
            String filename = file.getName();
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException e) {
            log.info("FileUtils-->文件下载失败" + e.getMessage());
        }
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

    //下载服务器上的压缩包
    public static void scriptDown(HttpServletResponse response, String filePath) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // path是指欲下载的文件的路径。
            if (IpfsService.isWindowsOS()) {
                if (!filePath.contains("C:\\") && !filePath.contains("C:/")) {
                    filePath = "C:" + filePath;
                }
            } else {
                if (!filePath.contains("\\home\\") && !filePath.contains("/home/")) {
                    filePath = "/home/" + filePath;
                    log.info("filepath ======" + filePath);
                }
            }
            File file = new File(filePath + ".zip");
            // 取得文件名。
            String filename = file.getName();
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            // 清空response
            //response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException e) {
            log.info("FileUtils-->文件下载失败" + e.getMessage());
        }
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
    /**
     * 删除下载的文件
     *
     * @param filePathName 文件路径
     */
    public static void deleteLocalFile(String filePathName) {
        //判断操作系统是否是Windows
        if (isWindowsOS()) {
            if (!filePathName.contains("C:\\") && !filePathName.contains("C:/")) {
                filePathName = "C:" + filePathName;
            }
        } else {
            if (!filePathName.contains("\\home\\") && !filePathName.contains("/home/")) {
                filePathName = "/home/" + filePathName;
            }
        }
        File localFile = new File(filePathName);
        if (localFile != null) {
            log.info(deleteFile(localFile) ? "成功" + "删除" + localFile.getName() + "文件夹或者文件" : "失败" + "删除" + localFile.getName() + "文件夹或者文件");
        }
    }

    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
}
