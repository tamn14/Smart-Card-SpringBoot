package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.Service.Interface.EducationInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EducationController {
    final EducationInterface educationInterface ;

    @PostMapping()
    public ApiResponse<EducationResponse> insertEdu (@Valid @RequestBody EducationRequest request) {
        EducationResponse response = educationInterface.insertEdu(request) ;
        return ApiResponse.<EducationResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }

    @PutMapping("/{id}")
    public ApiResponse<EducationResponse> updateEdu(@Valid @PathVariable("id") int id ,
                                                    @RequestBody EducationRequest educationRequest) {
        EducationResponse response = educationInterface.updateEdu(educationRequest , id) ;
        return ApiResponse.<EducationResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEdu(@PathVariable("id") int id) {
        educationInterface.deleteEdu(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }

    @GetMapping
    public ApiResponse<List<EducationResponse>> findAll() {
        List<EducationResponse> educationResponses =  educationInterface.findAll() ;
        return ApiResponse.<List<EducationResponse>>builder()
                .mess("Success")
                .result(educationResponses)
                .build() ;
    }

    @GetMapping("/{id}")
    public ApiResponse<EducationResponse> findById(@PathVariable("id") int id) {
        EducationResponse educationResponse =  educationInterface.findById(id) ;
        return ApiResponse.<EducationResponse>builder()
                .mess("Success")
                .result(educationResponse)
                .build() ;
    }


    @GetMapping("/public/{id}")
    public ApiResponse<List<EducationResponse>> findAllEduPublic(@Valid @PathVariable("id") String id) {
        List<EducationResponse> educationResponses =  educationInterface.findAllByUsers(id) ;
        return ApiResponse.<List<EducationResponse>>builder()
                .mess("Success")
                .result(educationResponses)
                .build() ;
    }

}
