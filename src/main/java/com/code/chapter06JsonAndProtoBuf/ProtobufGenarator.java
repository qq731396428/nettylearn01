package com.code.chapter06JsonAndProtoBuf;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * protoc生成java文件
 */
public class ProtobufGenarator {
    public static void main(String[] args) throws IOException {
        String protoc = System.getProperty("user.dir") + "\\src\\test\\java\\Netty\\protobuf\\protoTool\\bin\\protoc.exe";//protoc.exe目录
        String protoPath = System.getProperty("user.dir") + "\\src\\test\\java\\Netty\\protobuf\\protofile";//proto源文件目录
        String javaPath = System.getProperty("user.dir") + "\\src\\test\\java";//生成的java文件存放目录
        List<String> protoFileList = new ArrayList<String>();
        File f = new File(protoPath);
        File fa[] = f.listFiles();
        for (File fs : fa) {
            if (fs.isFile()) {
                protoFileList.add(fs.getName());
            }
        }
        for (String protoFile : protoFileList) {
            String strCmd = protoc + " -I=" + protoPath + " --java_out=" + javaPath + " " + protoPath + "\\" + protoFile;
            Runtime.getRuntime().exec(strCmd);
            System.out.println("proc生成proto文件：" + protoFile);
        }
    }
}

