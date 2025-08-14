package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.SkillRequest;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Service.Interface.SkillInterface;
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

@WebMvcTest(SkillController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillInterface skillInterface;

    private SkillRequest skillRequest;
    private SkillResponse skillResponse;
    private List<SkillResponse> skillResponseList;
    private int skillId = 1;

    @BeforeEach
    void init() {
        skillRequest = SkillRequest.builder()
                .name("Java")
                .level(5)
                .build();

        skillResponse = SkillResponse.builder()
                .skillId(skillId)
                .name("Java")
                .level(5)
                .build();

        skillResponseList = List.of(
                skillResponse,
                SkillResponse.builder()
                        .skillId(2)
                        .name("Python")
                        .level(4)
                        .build()
        );
    }

    @Test
    void createSkill_success() throws Exception {
        Mockito.when(skillInterface.insertSkill(any(SkillRequest.class))).thenReturn(skillResponse);

        String content = new ObjectMapper().writeValueAsString(skillRequest);

        mockMvc.perform(post("/skill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.skillId").value(skillId));
    }

    @Test
    void updateSkill_success() throws Exception {
        Mockito.when(skillInterface.updateSkill(any(SkillRequest.class), eq(skillId)))
                .thenReturn(skillResponse);

        String content = new ObjectMapper().writeValueAsString(skillRequest);

        mockMvc.perform(put("/skill/{id}", skillId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.skillId").value(skillId));
    }

    @Test
    void deleteSkill_success() throws Exception {
        Mockito.doNothing().when(skillInterface).deleteSkill(skillId);

        mockMvc.perform(delete("/skill/{id}", skillId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"));
    }

    @Test
    void findAll_success() throws Exception {
        Mockito.when(skillInterface.findAll()).thenReturn(skillResponseList);

        mockMvc.perform(get("/skill")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result[0].skillId").value(1))
                .andExpect(jsonPath("$.result[1].skillId").value(2));
    }

    @Test
    void findById_success() throws Exception {
        Mockito.when(skillInterface.findById(skillId)).thenReturn(skillResponse);

        mockMvc.perform(get("/skill/{id}", skillId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result.skillId").value(skillId));
    }

    @Test
    void findAllByUser_success() throws Exception {
        String userId = "1";
        Mockito.when(skillInterface.findAllByUser(userId)).thenReturn(skillResponseList);

        mockMvc.perform(get("/skill/public/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mess").value("Success"))
                .andExpect(jsonPath("$.result[0].skillId").value(1))
                .andExpect(jsonPath("$.result[1].skillId").value(2));
    }
}
