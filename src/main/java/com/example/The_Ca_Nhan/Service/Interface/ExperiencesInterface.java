package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.ExperiencesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;

import java.util.List;

public interface ExperiencesInterface {
    public ExperiencesResponse insertExp(ExperiencesRequest request) ;
    public ExperiencesResponse updateExp(ExperiencesRequest request , int Id) ;
    public void deleteExp(int id);
    public List<ExperiencesResponse> findAll() ;
    public ExperiencesResponse findById(int id) ;
    public List<ExperiencesResponse> findAllByUser(  String id ) ;


}
