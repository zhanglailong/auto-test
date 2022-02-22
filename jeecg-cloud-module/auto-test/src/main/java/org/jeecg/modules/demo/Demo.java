package org.jeecg.modules.demo;

import org.jeecg.modules.common.FileUtils;

import java.io.File;

public class Demo {
    public static void main(String[] args) throws Exception {
//        FileUtils.unCompress7z("C:/home/脚本1.7z","C:/home/123");
        File file = new File("http://192.168.10.101:9044/ipfs/QmbaWTQ4sm8cfk9UYUZzoJ8ae6tXgawRmNTm86QVXguCtg");
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(123);
    }
}
