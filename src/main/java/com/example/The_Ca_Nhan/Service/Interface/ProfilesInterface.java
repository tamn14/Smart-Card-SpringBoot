package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;

import java.util.List;

public interface ProfilesInterface {
    public ProfilesResponse insertProfiles(ProfilesRequest request) ;
    public ProfilesResponse updateProfiles(ProfilesRequest request , int Id) ;
    public void deleteProfiles(int Id);
    public List<ProfilesResponse> findAll() ;
    public ProfilesResponse findById(int id) ;

    public List<ProfilesResponse> findAllByUser ( String id) ;

}
