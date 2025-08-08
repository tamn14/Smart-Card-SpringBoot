package com.example.The_Ca_Nhan.Service.Interface;



import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;

import java.util.List;

public interface MediaFileInterface {
    public MediaFileResponse insertMedia(MediaFileCreateRequest request) ;
    public MediaFileResponse updateMedia(MediaFileUpdateRequest request , int Id) ;
    public void deleteMedia(int id);
    public List<MediaFileResponse> findAll() ;
    public MediaFileResponse findById(int id) ;
    List<MediaFileResponse> findByEntity(MediaEntityType type, int entityId) ;

}
