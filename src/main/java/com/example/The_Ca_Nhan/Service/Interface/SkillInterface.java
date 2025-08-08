package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Request.SkillRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;

import java.util.List;

public interface SkillInterface {
    public SkillResponse insertSkill(SkillRequest request) ;
    public SkillResponse updateSkill(SkillRequest request , int Id) ;
    public void deleteSkill(int Id);
    public List<SkillResponse> findAll() ;
    public SkillResponse findById(int id) ;

    public List<SkillResponse> findAllByUser(String id) ;

}
