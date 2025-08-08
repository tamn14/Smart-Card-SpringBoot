package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFileUpdateRequest {
    private int mediaId ;
    private String fileType;
    @Enumerated(EnumType.STRING)
    private MediaEntityType entityType;
    private String fileName;
    private int entityId ;
    private MultipartFile imageUrl ;
}
