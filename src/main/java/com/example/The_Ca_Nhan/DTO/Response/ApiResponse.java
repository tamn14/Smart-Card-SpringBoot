package com.example.The_Ca_Nhan.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T>{
    @Builder.Default
    private int code = 1000;
    private String mess;
    private T result;
}
