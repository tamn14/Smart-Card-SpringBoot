package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Request.ProjectRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;
import com.example.The_Ca_Nhan.Entity.Projects;

import java.util.List;

public interface ProjectInterface {
    public ProjectResponse insertProject(ProjectRequest request) ;
    public ProjectResponse updateProject(ProjectRequest request , int Id) ;
    public void deleteProject(int Id);
    public List<ProjectResponse> findAll() ;
    public ProjectResponse findById(int id) ;
    public List<ProjectResponse> findAllByUser( String userId) ;
}
