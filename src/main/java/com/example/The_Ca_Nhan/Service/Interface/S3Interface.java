package com.example.The_Ca_Nhan.Service.Interface;

import org.springframework.web.multipart.MultipartFile;

public interface S3Interface {
    public String uploadFile(MultipartFile file) ;
    public void deleteImage(String fileUrl) ;
}
