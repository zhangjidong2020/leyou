package com.leyou.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {


    public String uploadImage(MultipartFile file) {

        String url=null;

        try {
            //保存图片
            //1.生成保存目录
            File dir = new File("d:\\upload");
            //如果没有upload文件夹 创建
            if(!dir.exists()){

                dir.mkdirs();
            }
            //保存图片
            file.transferTo(new File(dir,file.getOriginalFilename()));

            //拼接图片地址
            url="http://image.leyou.com/"+file.getOriginalFilename();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
