package com.example.The_Ca_Nhan.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRequest {
    @NotNull
    private String name ;
    private String description ;
    private Integer price  ;
    private MultipartFile imageUrl ;
}
