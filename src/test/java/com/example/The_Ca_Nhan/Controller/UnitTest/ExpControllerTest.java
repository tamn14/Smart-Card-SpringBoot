package com.example.The_Ca_Nhan.Controller.UnitTest;


import com.example.The_Ca_Nhan.Controller.ExperiencesController;
import com.example.The_Ca_Nhan.DTO.Request.ExperiencesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;
import com.example.The_Ca_Nhan.Service.Interface.ExperiencesInterface;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(ExperiencesController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ExpControllerTest {
    @Autowired
    private MockMvc mockMvc ;
    @MockBean
    private ExperiencesInterface experiencesInterface ;

    private ExperiencesRequest experiencesRequest ;
    private ExperiencesResponse createExperiensResposne ;
    private ExperiencesResponse updateExperiensResposne ;
    private int expId  = 1 ;

    @BeforeEach
    void init() {
        experiencesRequest = ExperiencesRequest.builder()
                .name("test")
                .description("test")
                .endDate("12/2/2021")
                .startDate("12/2/2020")
                .position("Senior")
                .build();



        createExperiensResposne = ExperiencesResponse.builder()
                .expId(1)
                .name("test")
                .description("test")
                .endDate("12/2/2021")
                .startDate("12/2/2020")
                .position("Senior")
                .build();


        updateExperiensResposne = ExperiencesResponse.builder()
                .expId(1)
                .name("update")
                .description("update")
                .endDate("12/2/2021")
                .startDate("12/2/2020")
                .position("Junior")
                .build();





    }

    @Test
    void createExp_success() throws Exception {

        Mockito.when(experiencesInterface.insertExp(any(ExperiencesRequest.class)))
                .thenReturn(createExperiensResposne) ;

        ObjectMapper mapper = new ObjectMapper() ;
        String content = mapper.writeValueAsString(experiencesRequest) ;
        mockMvc.perform(MockMvcRequestBuilders.post("/exp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("test"));
    }

    @Test
    void updateExp_success() throws Exception {
        Mockito.when(experiencesInterface.updateExp(any(ExperiencesRequest.class), eq(expId)))
                .thenReturn(updateExperiensResposne);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(experiencesRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/exp/{id}", expId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("update"));
    }

    @Test
    void deleteExp_success() throws Exception {
        // Mock void method
        Mockito.doNothing().when(experiencesInterface).deleteExp(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/exp/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"));
    }




}
