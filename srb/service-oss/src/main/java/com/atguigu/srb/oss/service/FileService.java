package com.atguigu.srb.oss.service;

import java.io.InputStream;

public interface FileService {
    //文件下载
    String upload(InputStream inputStream, String module, String fileName);
    //文件删除
    void removeFile(String url);
}
