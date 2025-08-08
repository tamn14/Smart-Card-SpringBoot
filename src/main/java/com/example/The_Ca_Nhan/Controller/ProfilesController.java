package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.Service.Interface.ProfilesInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProfilesController {
    final ProfilesInterface profilesInterface ;
    @PostMapping()
    public ApiResponse<ProfilesResponse> insertProfile (@Valid @RequestBody ProfilesRequest request) {
        ProfilesResponse response = profilesInterface.insertProfiles(request) ;
        return ApiResponse.<ProfilesResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }

    @PutMapping("/{id}")
    public ApiResponse<ProfilesResponse> updateProfile(@Valid @PathVariable("id") int id ,
                                                    @RequestBody ProfilesRequest request) {
        ProfilesResponse response = profilesInterface.updateProfiles(request , id) ;
        return ApiResponse.<ProfilesResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProfile(@PathVariable("id") int id) {
        profilesInterface.deleteProfiles(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }

    @GetMapping
    public ApiResponse<List<ProfilesResponse>> findAll() {
        List<ProfilesResponse> profilesResponses =  profilesInterface.findAll() ;
        return ApiResponse.<List<ProfilesResponse>>builder()
                .mess("Success")
                .result(profilesResponses)
                .build() ;
    }

    @GetMapping("/{id}")
    public ApiResponse<ProfilesResponse> findById(@PathVariable("id") int id) {
        ProfilesResponse response =  profilesInterface.findById(id) ;
        return ApiResponse.<ProfilesResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }

    @GetMapping("/public/{id}")
    public ApiResponse<List<ProfilesResponse>> findAllByUser(@Valid @PathVariable("id") String id) {
        List<ProfilesResponse> profilesResponses =  profilesInterface.findAllByUser(id) ;
        return ApiResponse.<List<ProfilesResponse>>builder()
                .mess("Success")
                .result(profilesResponses)
                .build() ;
    }

}
