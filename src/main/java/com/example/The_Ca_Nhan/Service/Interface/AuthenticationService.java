package com.example.The_Ca_Nhan.Service.Interface;


import com.example.The_Ca_Nhan.DTO.Request.LoginRequest;
import com.example.The_Ca_Nhan.DTO.Request.LogoutRequest;
import com.example.The_Ca_Nhan.DTO.Request.RefreshRequest;
import com.example.The_Ca_Nhan.DTO.Response.AuthenticationResponse;

public interface AuthenticationService {
    public String getAccessToken (LoginRequest loginRequest) ;
    public AuthenticationResponse login (LoginRequest loginRequest) ;
    public AuthenticationResponse refreshToken (RefreshRequest request) ;
//    public String getKeycloakUserName(String email) ;
    public void logout(LogoutRequest logoutRequest) ;
    public AuthenticationResponse googleLogin (String  code) ;
}
