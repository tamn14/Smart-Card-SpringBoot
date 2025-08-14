package com.example.The_Ca_Nhan.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExperiencesRequest {
    @NotNull
    private String name ;
    private String position  ;
    private String description ;
    private String startDate ;
    private String endDate ;
}
