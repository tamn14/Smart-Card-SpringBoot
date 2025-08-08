package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Service.Interface.MediaFileInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/media")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MediafileController {
    final MediaFileInterface mediaFileInterface ;

    @PostMapping()
    public ApiResponse<MediaFileResponse> insertMedia (@Valid @ModelAttribute MediaFileCreateRequest request) {
        MediaFileResponse response = mediaFileInterface.insertMedia(request) ;
        return ApiResponse.<MediaFileResponse>builder()
                .mess("Success")
                .result(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<MediaFileResponse> updateMedia (@Valid @PathVariable("id") int id ,
                                                       @ModelAttribute MediaFileUpdateRequest request) {
        MediaFileResponse mediaFileResponse = mediaFileInterface.updateMedia(request , id) ;
        return ApiResponse.<MediaFileResponse>builder()
                .mess("Success")
                .result(mediaFileResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMedia (@PathVariable("id") int id ) {
        try {
            mediaFileInterface.deleteMedia(id);
            return ApiResponse.<Void>builder()
                    .mess("Success")
                    .build() ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping
    public ApiResponse<List<MediaFileResponse>> findAll () {
        List<MediaFileResponse> mediaFileResponses = mediaFileInterface.findAll() ;
        return ApiResponse.<List<MediaFileResponse>>builder()
                .mess("Success")
                .result(mediaFileResponses)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<MediaFileResponse> findById (@PathVariable("id") int id) {
        MediaFileResponse mediaFileResponses = mediaFileInterface.findById(id) ;
        return ApiResponse.<MediaFileResponse>builder()
                .mess("Success")
                .result(mediaFileResponses)
                .build();
    }

    @GetMapping("/public/{entityType}/{id}")
    public ApiResponse<List<MediaFileResponse>> findMediaByUser (@PathVariable("id") int id,
                                                                 @PathVariable("entityType") String entityType) {
        List<MediaFileResponse> mediaFileResponses = mediaFileInterface.findByEntity(MediaEntityType.valueOf(entityType), id) ;
        return ApiResponse.<List<MediaFileResponse>>builder()
                .mess("Success")
                .result(mediaFileResponses)
                .build();
    }





}
