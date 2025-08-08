package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.*;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Entity.Users;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UsersInterface {
    public UsersResponse createUser (UsersRequest request) ;
    public Page<UsersResponse> getAllUsers(int page, int size) ;
    public UsersResponse getUserById(int id) ;
    public UsersResponse updateUser (UsersUpdateRequest usersRequest ) ;
    public UsersResponse getMyInfor() ;
    public void changePassword (ChangePasswordRequest request) ;
    public void deleteUser(int id) ;
    public UsersResponse VerifyUsers(VerifyUserRequest accountNumber ,  int id ) ;
    public Page<UsersResponse> findByName(String name , int page, int size) ;
    public void UpdateAvatar (AvatarUpdateRequest request) ;
    public UsersResponse getUserByKeycloakId(String id) ;





}
