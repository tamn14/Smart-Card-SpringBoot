package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillRequest {
    private String name ;
    private Integer level  ;
}
