package com.example.The_Ca_Nhan.Controller.UnitTest;

import com.example.The_Ca_Nhan.Controller.ProjectController;
import com.example.The_Ca_Nhan.DTO.Request.ProjectRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;
import com.example.The_Ca_Nhan.Service.Interface.ProjectInterface;
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

@WebMvcTest(ProjectController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectInterface projectInterface;

    private ProjectRequest projectRequest;
    private ProjectResponse projectResponse;
    private List<ProjectResponse> projectResponseList;
    private int projectId = 1;

    @BeforeEach
    void init() {
        projectRequest = ProjectRequest.builder()
                .title("My Project")
                .description("Project Description")
                .tech("Java, Spring Boot")
                .link("https://github.com/myproject")
                .build();

        projectResponse = ProjectResponse.builder()
                .projectId(projectId)
                .title("My Project")
                .description("Project Description")
                .tech("Java, Spring Boot")
                .link("https://github.com/myproject")
                .build();

        projectResponseList = List.of(
                projectResponse,
                ProjectResponse.builder()
                        .projectId(2)
                        .title("Another Project")
                        .description("Description 2")
                        .tech("React, NodeJS")
                        .link("https://github.com/another")
                        .build()
        );
    }

    @Test
    void createProject_success() throws Exception {
        Mockito.when(projectInterface.insertProject(any(ProjectRequest.class)))
                .thenReturn(projectResponse);

        String content = new ObjectMapper().writeValueAsString(projectRequest);

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.projectId").value(projectId));
    }

    @Test
    void updateProject_success() throws Exception {
        Mockito.when(projectInterface.updateProject(any(ProjectRequest.class), eq(projectId)))
                .thenReturn(projectResponse);

        String content = new ObjectMapper().writeValueAsString(projectRequest);

        mockMvc.perform(put("/project/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.projectId").value(projectId));
    }

    @Test
    void deleteProject_success() throws Exception {
        Mockito.doNothing().when(projectInterface).deleteProject(projectId);

        mockMvc.perform(delete("/project/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"));
    }

    @Test
    void findAll_success() throws Exception {
        Mockito.when(projectInterface.findAll()).thenReturn(projectResponseList);

        mockMvc.perform(get("/project")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result[0].projectId").value(1))
                .andExpect(jsonPath("$.result[1].projectId").value(2));
    }

    @Test
    void findById_success() throws Exception {
        Mockito.when(projectInterface.findById(projectId)).thenReturn(projectResponse);

        mockMvc.perform(get("/project/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.projectId").value(projectId));
    }

    @Test
    void findAllByUser_success() throws Exception {
        String userId = "1";
        Mockito.when(projectInterface.findAllByUser(userId)).thenReturn(projectResponseList);

        mockMvc.perform(get("/project/public/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result[0].projectId").value(1))
                .andExpect(jsonPath("$.result[1].projectId").value(2));
    }
}
