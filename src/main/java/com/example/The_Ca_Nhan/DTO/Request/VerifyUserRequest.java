package com.example.The_Ca_Nhan.DTO.Request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyUserRequest {
    @Pattern(regexp = "^[A-Za-z0-9]{5}$")
    private String accountNumber ;
}
