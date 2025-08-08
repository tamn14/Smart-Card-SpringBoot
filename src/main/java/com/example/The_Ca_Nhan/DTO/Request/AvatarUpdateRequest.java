package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvatarUpdateRequest {
    private MultipartFile imageUrl ;
}
