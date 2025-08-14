package com.example.The_Ca_Nhan.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillRequest {
    @NotNull
    private String name ;
    private Integer level  ;
}
