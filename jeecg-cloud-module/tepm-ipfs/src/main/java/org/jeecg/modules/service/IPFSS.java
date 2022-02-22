package org.jeecg.modules.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.ipfs.api.*;
import io.ipfs.multiaddr.MultiAddress;
import org.jeecg.modules.entity.PinKeys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

public class IPFSS extends IPFS {
    public IPFSS(String host, int port) {
        super(host, port);
    }

    public IPFSS(String multiaddr) {
        super(multiaddr);
    }

    public IPFSS(MultiAddress addr) {
        super(addr);
    }

    public IPFSS(String host, int port, String version, boolean ssl) {
        super(host, port, version, ssl);
    }

    public IPFSS(String host, int port, String version, int connectTimeoutMillis, int readTimeoutMillis, boolean ssl) {
        super(host, port, version, connectTimeoutMillis, readTimeoutMillis, ssl);
    }

    public Object filesLs(String arg) {
        try {
            Map reply = this.retrieveMap("files/ls?arg="+arg);
            if (reply != null && reply.get("Entries") != null){
                return reply.get("Entries");
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public Map<String, PinKeys> pin() throws IOException {
        Map reply = this.retrieveMap("pin/ls");
        Map<String, PinKeys> pins = JSONObject.parseObject(JSON.toJSONString(reply.get("Keys")), new TypeReference<Map<String, PinKeys>>() {});
        return pins;
    }

    private Map retrieveMap(String path) throws IOException {
        return (Map)this.retrieveAndParse(path);
    }

    private Object retrieveAndParse(String path) throws IOException {
        byte[] res = this.retrieve(path);
        return JSONParser.parse(new String(res));
    }

    private byte[] retrieve(String path) throws IOException {
        URL target = new URL(this.protocol, this.host, this.port, "/api/v0/" + path);
        return get(target, 10000, 60000);
    }



    private static byte[] get(URL target, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
        HttpURLConnection conn = configureConnection(target, "POST", connectTimeoutMillis, readTimeoutMillis);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        InputStream in;
        try {
            OutputStream out = conn.getOutputStream();
            out.write(new byte[0]);
            out.flush();
            out.close();
            in = conn.getInputStream();
            ByteArrayOutputStream resp = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];

            int r;
            while((r = in.read(buf)) >= 0) {
                resp.write(buf, 0, r);
            }

            return resp.toByteArray();
        } catch (ConnectException var9) {
            throw new RuntimeException("Couldn't connect to IPFS daemon at " + target + "\n Is IPFS running?");
        } catch (IOException var10) {
            in = conn.getErrorStream();
            String err = in == null ? var10.getMessage() : new String(readFully(in));
            throw new RuntimeException("IOException contacting IPFS daemon.\n" + err + "\nTrailer: " + conn.getHeaderFields().get("Trailer"), var10);
        }
    }

    private static HttpURLConnection configureConnection(URL target, String method, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)target.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(connectTimeoutMillis);
        conn.setReadTimeout(readTimeoutMillis);
        return conn;
    }

    private static final byte[] readFully(InputStream in) {
        try {
            ByteArrayOutputStream resp = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];

            int r;
            while((r = in.read(buf)) >= 0) {
                resp.write(buf, 0, r);
            }

            return resp.toByteArray();
        } catch (IOException var4) {
            throw new RuntimeException("Error reading InputStrean", var4);
        }
    }

    //ipfs files 操作

    /**
     * 写入文件
     * @param file 文件
     * @param arg 上传地址
     * @throws IOException
     */
    public void writeFile(NamedStreamable file, String arg ,long filePathId)throws IOException {
        if (!filesStat("/home/"+filePathId)){
            if (!filesMkdir("/home/"+filePathId)){
                throw new UnsupportedOperationException("初始化目录失败!");
            }
        }
        if (filesStat(arg)){
            writeTruncateFile(file,arg);
        }else {
            writeCreateFile(file,arg);
        }
    }

    public void writeCreateFile(NamedStreamable file, String arg) throws IOException {
        Multipart m = new Multipart(this.protocol + "://" + this.host + ":" + this.port + "/api/v0/" + "files/write?arg="+arg+"&create=true&stream-channels=true&w="+false, "UTF-8");
        if (file.isDirectory()) {
            m.addSubtree(Paths.get(""), file);
        } else {
            m.addFilePart("file", Paths.get(""), file);
        }
        m.finish();
    }
    public void writeTruncateFile(NamedStreamable file, String arg) throws IOException {
        Multipart m = new Multipart(this.protocol + "://" + this.host + ":" + this.port + "/api/v0/" + "files/write?arg="+arg+"&truncate=true&stream-channels=true&w="+false, "UTF-8");
        if (file.isDirectory()) {
            m.addSubtree(Paths.get(""), file);
        } else {
            m.addFilePart("file", Paths.get(""), file);
        }
        m.finish();
    }

    /**
     * 判断文件/文件夹是否存在
     * @param arg
     * @return
     * @throws IOException
     */
    public boolean filesStat(String arg) {
        try {
            Map reply = this.retrieveMap("files/stat?arg="+arg);
            if (reply == null || reply.get("Hash") == null){
                return false;
            }
            System.out.println(JSON.toJSONString(reply));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 根据文件/文件夹路径 查询 文件夹/文件hash
     * @param arg
     * @return
     * @throws IOException
     */
    public String filesStatHash(String arg) {
        try {
            Map reply = this.retrieveMap("files/stat?arg="+arg);
            if (reply != null && reply.get("Hash") != null){
                return reply.get("Hash").toString();
            }
            System.out.println(JSON.toJSONString(reply));
        }catch (Exception e){
            return null;
        }
        return null;
    }

    /**
     * 创建文件夹
     * @param arg 路径mkdir
     * @return
     */
    public boolean filesMkdir(String arg) {
        try {
            this.retrieveMap("files/mkdir?arg="+arg);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 文件拷贝
     * @param source  原文件夹包含路径
     * @param destination 目标文件夹包含路径 不可以存在的文件
     * @return true 或者 false
     */
    public boolean fileCp(String source  ,String destination ) {
        try {
            if (!filesStat(destination)) {
                this.retrieveMap("files/cp?arg=" + source + "&arg=" + destination);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    public boolean addFileSection(java.io.File file, String arg,long filePathId) {
        try {
            if (!filesStat("/home/"+filePathId)){
                if (!filesMkdir("/home/"+filePathId)){
                    throw new UnsupportedOperationException("初始化目录失败!");
                }
            }
            if (!filesStat(arg)){
                if (!filesMkdir(arg)){
                    throw new UnsupportedOperationException("初始化目录失败!");
                }
            }
            func(file,arg);
            return true;
        }catch (Exception e){
            System.out.println("异常:"+e.getMessage());
            return false;
        }
    }
    private void func(java.io.File file,String arg) throws IOException {
        java.io.File[] files = file.listFiles();
        for(java.io.File f:files){
            if(f.isDirectory()){
                //判断文件夹是否存在，不存在创建文件夹
                if (!filesStat(arg+"/"+f.getName())){
                    if (!filesMkdir(arg+"/"+f.getName())){
                        throw new UnsupportedOperationException("创建目录失败");
                    }
                }
                func(f,arg+"/"+f.getName());
            }
            if(f.isFile()){
                //判断文件是否存在
                NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(f);
                if (filesStat(arg+"/"+f.getName())){
                    writeTruncateFile(fileWrapper,arg+"/"+f.getName());
                }else {
                    writeCreateFile(fileWrapper,arg+"/"+f.getName());
                }
            }
        }
    }



}
