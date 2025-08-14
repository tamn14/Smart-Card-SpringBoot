package com.example.The_Ca_Nhan.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvatarUpdateRequest {
    @NotNull
    private MultipartFile imageUrl ;
}
