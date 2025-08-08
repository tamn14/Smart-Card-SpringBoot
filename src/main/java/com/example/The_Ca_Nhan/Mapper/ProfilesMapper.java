package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.Entity.Experiences;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Profiles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {
        UsersMapper.class,
        MediaFilesMapper.class

})
public interface ProfilesMapper {


    @Mapping(source = "profiles.users", target = "usersResponse")
    @Mapping(source = "mediaFiles", target = "mediaFiles")
    ProfilesResponse toDTO (Profiles profiles, List<MediaFiles> mediaFiles)  ;

    @Mapping(source = "profiles.users", target = "usersResponse")
    ProfilesResponse toDTO (Profiles profiles)  ;

    default Profiles toEntity(ProfilesRequest request) {
        return Profiles.builder()
                .summary(request.getSummary())
                .hobby(request.getHobby())
                .facebook(request.getFacebook())
                .github(request.getGithub())
                .career(request.getCareer())
                .degree(request.getDegree())
                .build();
    }

}
