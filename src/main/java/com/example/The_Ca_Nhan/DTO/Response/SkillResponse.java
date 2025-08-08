package com.example.The_Ca_Nhan.DTO.Response;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillResponse {
    private int skillId ;
    private String name ;
    private int level  ;
    private UsersResponse usersResponse ;
    private List<MediaFileResponse> mediaFiles ;
}
