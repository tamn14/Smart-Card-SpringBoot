package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFileCreateRequest {
    @Enumerated(EnumType.STRING)
    private MediaEntityType entityType;
    private String fileType;
    private int entityId ;
    private String fileName;
    private MultipartFile imageUrl ;
}
