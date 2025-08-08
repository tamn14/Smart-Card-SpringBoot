package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;

import java.util.List;

public interface EducationInterface {
    public EducationResponse insertEdu(EducationRequest request) ;
    public EducationResponse updateEdu(EducationRequest request,  int Id) ;
    public void deleteEdu(int eduId);
    public List<EducationResponse> findAll() ;
    public EducationResponse findById(int id) ;
    public List<EducationResponse> findAllByUsers(String userId ) ;


}
