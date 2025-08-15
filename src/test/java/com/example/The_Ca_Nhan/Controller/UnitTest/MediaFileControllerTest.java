package com.example.The_Ca_Nhan.Controller.UnitTest;

import com.example.The_Ca_Nhan.Controller.MediafileController;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Service.Interface.MediaFileInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MediafileController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class MediaFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MediaFileInterface mediaFileInterface;

    private MediaFileCreateRequest createRequest;
    private MediaFileUpdateRequest updateRequest;
    private MediaFileResponse mediaResponse;
    private MockMultipartFile mockFile;
    private final int mediaId = 1;

    @BeforeEach
    void init() {
        mockFile = new MockMultipartFile("imageUrl", "test.png",
                MediaType.IMAGE_PNG_VALUE, "test data".getBytes());

        createRequest = MediaFileCreateRequest.builder()
                .entityType(MediaEntityType.USER)
                .entityId(1)
                .fileType("PNG")
                .fileName("test.png")
                .imageUrl(mockFile)
                .build();

        updateRequest = MediaFileUpdateRequest.builder()
                .mediaId(mediaId)
                .entityType(MediaEntityType.USER)
                .entityId(1)
                .fileType("JPG")
                .fileName("update.png")
                .imageUrl(mockFile)
                .build();

        mediaResponse = MediaFileResponse.builder()
                .mediaId(mediaId)
                .entityType(MediaEntityType.USER)
                .entityId(1)
                .fileType("PNG")
                .fileName("test.png")
                .link("http://example.com/test.png")
                .build();
    }

    @Test
    void createMedia_success() throws Exception {
        Mockito.when(mediaFileInterface.insertMedia(any(MediaFileCreateRequest.class)))
                .thenReturn(mediaResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/media")
                        .file(mockFile)
                        .param("entityType", createRequest.getEntityType().name())
                        .param("entityId", String.valueOf(createRequest.getEntityId()))
                        .param("fileType", createRequest.getFileType())
                        .param("fileName", createRequest.getFileName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.fileName").value("test.png"))
                .andExpect(jsonPath("$.result.entityType").value("USER"));
    }



    @Test
    void updateMedia_success() throws Exception {
        Mockito.when(mediaFileInterface.updateMedia(any(MediaFileUpdateRequest.class), eq(mediaId)))
                .thenReturn(mediaResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/media/{id}", mediaId)
                        .file(mockFile)
                        .param("mediaId", String.valueOf(updateRequest.getMediaId()))
                        .param("entityType", updateRequest.getEntityType().name())
                        .param("entityId", String.valueOf(updateRequest.getEntityId()))
                        .param("fileType", updateRequest.getFileType())
                        .param("fileName", updateRequest.getFileName())
                        .with(request -> { request.setMethod("PUT"); return request; })) // override method
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.fileName").value("test.png"));
    }



    @Test
    void deleteMedia_success() throws Exception {
        Mockito.doNothing().when(mediaFileInterface).deleteMedia(mediaId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/media/{id}", mediaId))
                .andExpect(status().isOk());
    }



    @Test
    void findAllMedia_success() throws Exception {
        List<MediaFileResponse> list = new ArrayList<>();
        list.add(mediaResponse);

        Mockito.when(mediaFileInterface.findAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/media"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].fileName").value("test.png"));
    }

    @Test
    void findById_success() throws Exception {
        Mockito.when(mediaFileInterface.findById(mediaId)).thenReturn(mediaResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/media/{id}", mediaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.mediaId").value(mediaId));
    }



    @Test
    void findByEntity_success() throws Exception {
        List<MediaFileResponse> list = new ArrayList<>();
        list.add(mediaResponse);

        Mockito.when(mediaFileInterface.findByEntity(eq(MediaEntityType.USER), eq(1))).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/media/public/{entityType}/{id}", "USER", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].fileName").value("test.png"));
    }
}
