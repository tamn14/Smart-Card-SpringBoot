package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.ProjectRequest;
import com.example.The_Ca_Nhan.DTO.Request.SkillRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.Service.Interface.SkillInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skill")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SkillController {
    final SkillInterface skillInterface ;
    @PostMapping()
    public ApiResponse<SkillResponse> insertSkill (@Valid @RequestBody SkillRequest request) {
        SkillResponse response = skillInterface.insertSkill(request) ;
        return ApiResponse.<SkillResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }

    @PutMapping("/{id}")
    public ApiResponse<SkillResponse> updateSkill(@Valid @PathVariable("id") int id ,
                                                      @RequestBody SkillRequest request) {
        SkillResponse response = skillInterface.updateSkill(request , id) ;
        return ApiResponse.<SkillResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSkill(@PathVariable("id") int id) {
        skillInterface.deleteSkill(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }

    @GetMapping
    public ApiResponse<List<SkillResponse>> findAll() {
        List<SkillResponse> responses =  skillInterface.findAll() ;
        return ApiResponse.<List<SkillResponse>>builder()
                .mess("Success")
                .result(responses)
                .build() ;
    }

    @GetMapping ("/public/{id}")
    public ApiResponse<List<SkillResponse>> findAllByUser(@PathVariable("id") String id) {
        List<SkillResponse> responses =  skillInterface.findAllByUser(id) ;
        return ApiResponse.<List<SkillResponse>>builder()
                .mess("Success")
                .result(responses)
                .build() ;
    }

    @GetMapping("/{id}")
    public ApiResponse<SkillResponse> findById(@PathVariable("id") int id) {
        SkillResponse response =  skillInterface.findById(id) ;
        return ApiResponse.<SkillResponse>builder()
                .mess("Success")
                .result(response)
                .build() ;
    }
}
