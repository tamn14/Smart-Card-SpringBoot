package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @Positive(message = "mediaId phải > 0")
    private int mediaId;

    @NotEmpty(message = "fileType không được để trống")
    private String fileType;

    @NotNull(message = "entityType không được null")
    @Enumerated(EnumType.STRING)
    private MediaEntityType entityType;

    @NotEmpty(message = "fileName không được để trống")
    private String fileName;

    @Positive(message = "entityId phải > 0")
    private int entityId;

    @NotNull(message = "imageUrl không được null")
    private MultipartFile imageUrl;
}
