package com.example.The_Ca_Nhan.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationRequest {
    @NotNull
    private String schoolName ;
    private String degree ;
    private String startDate ;
    private String endDate ;
    private String description ;

}
