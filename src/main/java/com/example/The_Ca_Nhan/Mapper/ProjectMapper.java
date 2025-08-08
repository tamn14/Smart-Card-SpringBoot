package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.ProjectRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Profiles;
import com.example.The_Ca_Nhan.Entity.Projects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {
        UsersMapper.class,
        MediaFilesMapper.class

})
public interface ProjectMapper {

    @Mapping(source = "projects.users", target = "usersResponse")
    @Mapping(source = "mediaFiles", target = "mediaFiles")
    ProjectResponse toDTO (Projects projects, List<MediaFiles> mediaFiles)  ;

    @Mapping(source = "project.users", target = "usersResponse")
    ProjectResponse toDTO (Projects project)  ;

    default Projects toEntity(ProjectRequest request) {
        return Projects.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .link(request.getLink())
                .tech(request.getTech())
                .build();
    }
}
