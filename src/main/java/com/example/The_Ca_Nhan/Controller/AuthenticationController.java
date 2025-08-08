package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.LoginRequest;
import com.example.The_Ca_Nhan.DTO.Request.LogoutRequest;
import com.example.The_Ca_Nhan.DTO.Request.RefreshRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.AuthenticationResponse;
import com.example.The_Ca_Nhan.Service.Interface.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticationResponse loginResponse = authenticationService.login(loginRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .mess("Success")
                .result(loginResponse)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh (@Valid @RequestBody RefreshRequest request) {
        AuthenticationResponse response = authenticationService.refreshToken(request) ;
        return ApiResponse.<AuthenticationResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout (@Valid @RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }

    @PostMapping("/login/google")
    public ApiResponse<AuthenticationResponse> loginWithGoogle(String code) {
        AuthenticationResponse loginResponse = authenticationService.googleLogin(code);
        return ApiResponse.<AuthenticationResponse>builder()
                .mess("Success")
                .result(loginResponse)
                .build();
    }




}
