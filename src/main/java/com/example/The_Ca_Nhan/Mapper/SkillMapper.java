package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.SkillRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.Entity.Educations;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Skills;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {
        UsersMapper.class,
        MediaFilesMapper.class

})
public interface SkillMapper {

    @Mapping(source = "skills.users", target = "usersResponse")
    @Mapping(source = "mediaFiles", target = "mediaFiles")
    SkillResponse toDTO (Skills skills ,  List<MediaFiles> mediaFiles)  ;

    @Mapping(source = "skills.users", target = "usersResponse")
    SkillResponse toDTO (Skills skills )  ;

    default Skills toEntity (SkillRequest request) {
        return Skills.builder()
                .name(request.getName())
                .level(request.getLevel())
                .build();
    }
}
