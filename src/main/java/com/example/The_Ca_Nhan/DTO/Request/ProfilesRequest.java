package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilesRequest {

    private String summary  ;
    private String hobby ;
    private String github ;
    private String facebook;
    private String career ;
    private String degree ;



}
