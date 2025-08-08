package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.*;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Service.Interface.UsersInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    final UsersInterface usersInterface;

    @PostMapping()
    public ApiResponse<UsersResponse> createUser(@Valid @RequestBody UsersRequest request) {
        UsersResponse response = usersInterface.createUser(request);
        return ApiResponse.<UsersResponse>builder()
                .mess("Success")
                .result(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UsersResponse> updateUser(@Valid
                                                 @RequestBody UsersUpdateRequest request) {
        UsersResponse response = usersInterface.updateUser(request);
        return ApiResponse.<UsersResponse>builder()
                .mess("Success")
                .result(response)
                .build();

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") int id) {
        usersInterface.deleteUser(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<UsersResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "4") int size) {
        Page<UsersResponse> responses = usersInterface.getAllUsers(page, size);
        return ApiResponse.<Page<UsersResponse>>builder()
                .mess("Success")
                .result(responses)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UsersResponse> getMyInfor() {
        UsersResponse responses = usersInterface.getMyInfor();
        return ApiResponse.<UsersResponse>builder()
                .mess("Success")
                .result(responses)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UsersResponse> getUserById(@PathVariable("id") int id) {
        UsersResponse response = usersInterface.getUserById(id);
        return ApiResponse.<UsersResponse>builder()
                .mess("Success")
                .result(response)
                .build();
    }


    @PutMapping("/change/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        usersInterface.changePassword(request);
        ;
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build();
    }

    @PatchMapping("/verify/{id}")
    public ApiResponse<UsersResponse> verifyUser(@Valid @RequestBody VerifyUserRequest request,
                                                 @PathVariable("id") int id) {
        UsersResponse response = usersInterface.VerifyUsers(request, id);
        ;
        return ApiResponse.<UsersResponse>builder()
                .mess("Success")
                .result(response)
                .build();
    }

    @GetMapping("/name/{name}")
    public ApiResponse<Page<UsersResponse>> getAllUsers(@PathVariable("name") String name,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<UsersResponse> responses = usersInterface.findByName(name, page, size);
        return ApiResponse.<Page<UsersResponse>>builder()
                .mess("Success")
                .result(responses)
                .build();
    }

    @PostMapping("/update/image")
    public ApiResponse<Void> updateImage(@Valid @ModelAttribute AvatarUpdateRequest request) {
        try {
            usersInterface.UpdateAvatar(request);
            return ApiResponse.<Void>builder()
                    .mess("Success")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    @GetMapping("public/{id}")
    public ApiResponse<UsersResponse> getUserByKeycloakId(@PathVariable("id") String id) {
        UsersResponse response = usersInterface.getUserByKeycloakId(id);
        return ApiResponse.<UsersResponse>builder()
                .mess("Success")
                .result(response)
                .build();
    }



}
