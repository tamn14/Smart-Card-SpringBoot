package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRequest {
    private String name ;
    private String description ;
    private Integer price  ;
    private MultipartFile imageUrl ;
}
