package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Request.ProjectRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;
import com.example.The_Ca_Nhan.Service.Interface.ProjectInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    final ProjectInterface projectInterface ;
    @PostMapping()
    public ApiResponse<ProjectResponse> insertProject (@Valid @RequestBody ProjectRequest request) {
        ProjectResponse response = projectInterface.insertProject(request) ;
        return ApiResponse.<ProjectResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }

    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> updateProject(@Valid @PathVariable("id") int id ,
                                                       @RequestBody ProjectRequest request) {
        ProjectResponse response = projectInterface.updateProject(request , id) ;
        return ApiResponse.<ProjectResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProject(@PathVariable("id") int id) {
        projectInterface.deleteProject(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> findAll() {
        List<ProjectResponse> responses =  projectInterface.findAll() ;
        return ApiResponse.<List<ProjectResponse>>builder()
                .mess("Success")
                .result(responses)
                .build() ;
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> findById(@PathVariable("id") int id) {
        ProjectResponse response =  projectInterface.findById(id) ;
        return ApiResponse.<ProjectResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }
    @GetMapping("/public/{id}")
    public ApiResponse<List<ProjectResponse>> findAllByUser(@PathVariable("id") String id) {
        List<ProjectResponse> responses =  projectInterface.findAllByUser(id) ;
        return ApiResponse.<List<ProjectResponse>>builder()
                .mess("Success")
                .result(responses)
                .build() ;
    }

}
