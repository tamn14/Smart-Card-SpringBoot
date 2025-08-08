package com.example.The_Ca_Nhan.DTO.Response;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilesResponse {
    private int profileId ;
    private String summary  ;
    private String hobby ;
    private String github ;
    private String facebook;
    private String career ;
    private String degree ;
    private UsersResponse usersResponse ;


    private List<MediaFileResponse> mediaFiles;
}
