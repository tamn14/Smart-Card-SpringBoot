package com.example.The_Ca_Nhan.DTO.Response;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExperiencesResponse {
    private int expId ;
    private String name ;
    private String position  ;
    private String description ;
    private String startDate ;
    private String endDate ;
    private UsersResponse usersResponse ;
    private List<MediaFileResponse> mediaFiles ;
}
