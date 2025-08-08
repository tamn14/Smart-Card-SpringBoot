package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Request.ExperiencesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;
import com.example.The_Ca_Nhan.Service.Interface.ExperiencesInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exp")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ExperiencesController {
    final ExperiencesInterface experiencesInterface ;

    @PostMapping()
    public ApiResponse<ExperiencesResponse> insertExp (@Valid @RequestBody ExperiencesRequest request) {
        ExperiencesResponse response = experiencesInterface.insertExp(request) ;
        return ApiResponse.<ExperiencesResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }

    @PutMapping("/{id}")
    public ApiResponse<ExperiencesResponse> updateExp(@Valid @PathVariable("id") int id ,
                                                    @RequestBody ExperiencesRequest request) {
        ExperiencesResponse response = experiencesInterface.updateExp(request , id) ;
        return ApiResponse.<ExperiencesResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteExp(@PathVariable("id") int id) {
        experiencesInterface.deleteExp(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }

    @GetMapping
    public ApiResponse<List<ExperiencesResponse>> findAll() {
        List<ExperiencesResponse> experiencesResponses =  experiencesInterface.findAll() ;
        return ApiResponse.<List<ExperiencesResponse>>builder()
                .mess("Success")
                .result(experiencesResponses)
                .build() ;
    }

    @GetMapping("/{id}")
    public ApiResponse<ExperiencesResponse> findById(@PathVariable("id") int id) {
        ExperiencesResponse educationResponse =  experiencesInterface.findById(id) ;
        return ApiResponse.<ExperiencesResponse>builder()
                .mess("Success")
                .result(educationResponse)
                .build() ;
    }

    @GetMapping("/public/{id}")
    public ApiResponse<List<ExperiencesResponse>> findAllByUser(@Valid @PathVariable("id") String id ) {
        List<ExperiencesResponse> experiencesResponses =  experiencesInterface.findAllByUser(id) ;
        return ApiResponse.<List<ExperiencesResponse>>builder()
                .mess("Success")
                .result(experiencesResponses)
                .build() ;
    }

}
