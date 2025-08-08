package com.example.The_Ca_Nhan.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersRequest {
    @NotBlank
    @Size(min = 4, max = 50)
    private String userName ;
    @NotBlank
    private String password ;
    @NotBlank
    private String lastName ;
    @NotBlank
    private String firstName ;
    @Email
    @NotBlank
    private String email ;
    @NotBlank
    private String address ;





}
