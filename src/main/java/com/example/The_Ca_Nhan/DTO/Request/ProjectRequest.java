package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequest {
    private String title  ;
    private String description ;
    private String tech ;
    private String link ;
}
