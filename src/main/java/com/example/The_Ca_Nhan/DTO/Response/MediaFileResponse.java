package com.example.The_Ca_Nhan.DTO.Response;

import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFileResponse {
    private int mediaId ;
    @Enumerated(EnumType.STRING)
    private MediaEntityType entityType;
    private int entityId;
    private String fileType;
    private String fileName;
    private String link;
    private LocalDateTime updateDate;
}
