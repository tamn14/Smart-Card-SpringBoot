package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Service.Interface.ProfilesInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfilesController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ProfilesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfilesInterface profilesInterface;

    private ProfilesRequest profilesRequest;
    private ProfilesResponse profilesResponse;
    private List<ProfilesResponse> profilesResponseList;
    private int profileId = 1;

    @BeforeEach
    void init() {
        profilesRequest = ProfilesRequest.builder()
                .summary("Experienced Java developer")
                .hobby("Reading, Coding")
                .github("https://github.com/example")
                .facebook("https://facebook.com/example")
                .career("Backend Developer")
                .degree("Bachelor of Computer Science")
                .build();

        profilesResponse = ProfilesResponse.builder()
                .profileId(profileId)
                .summary("Experienced Java developer")
                .hobby("Reading, Coding")
                .github("https://github.com/example")
                .facebook("https://facebook.com/example")
                .career("Backend Developer")
                .degree("Bachelor of Computer Science")
                .build();

        profilesResponseList = List.of(
                profilesResponse,
                ProfilesResponse.builder()
                        .profileId(2)
                        .summary("Frontend developer")
                        .hobby("Drawing, Music")
                        .github("https://github.com/frontend")
                        .facebook("https://facebook.com/frontend")
                        .career("Frontend Developer")
                        .degree("Bachelor of Information Technology")
                        .build()
        );
    }

    @Test
    void createProfile_success() throws Exception {
        Mockito.when(profilesInterface.insertProfiles(any(ProfilesRequest.class)))
                .thenReturn(profilesResponse);

        String content = new ObjectMapper().writeValueAsString(profilesRequest);

        mockMvc.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.profileId").value(profileId));
    }

    @Test
    void updateProfile_success() throws Exception {
        Mockito.when(profilesInterface.updateProfiles(any(ProfilesRequest.class), eq(profileId)))
                .thenReturn(profilesResponse);

        String content = new ObjectMapper().writeValueAsString(profilesRequest);

        mockMvc.perform(put("/profile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.profileId").value(profileId));
    }

    @Test
    void deleteProfile_success() throws Exception {
        Mockito.doNothing().when(profilesInterface).deleteProfiles(profileId);

        mockMvc.perform(delete("/profile/{id}", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"));
    }

    @Test
    void findAll_success() throws Exception {
        Mockito.when(profilesInterface.findAll()).thenReturn(profilesResponseList);

        mockMvc.perform(get("/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result[0].profileId").value(1))
                .andExpect(jsonPath("$.result[1].profileId").value(2));
    }

    @Test
    void findById_success() throws Exception {
        Mockito.when(profilesInterface.findById(profileId)).thenReturn(profilesResponse);

        mockMvc.perform(get("/profile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.profileId").value(profileId));
    }

    @Test
    void findAllByUser_success() throws Exception {
        String userId = "1";
        Mockito.when(profilesInterface.findAllByUser(userId)).thenReturn(profilesResponseList);

        mockMvc.perform(get("/profile/public/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result[0].profileId").value(1))
                .andExpect(jsonPath("$.result[1].profileId").value(2));
    }

}
