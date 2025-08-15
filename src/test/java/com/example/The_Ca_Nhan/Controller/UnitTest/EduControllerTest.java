package com.example.The_Ca_Nhan.Controller.UnitTest;

import com.example.The_Ca_Nhan.Controller.EducationController;
import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.Service.Interface.EducationInterface;
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

@WebMvcTest(EducationController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class EduControllerTest {
    @Autowired
    private MockMvc mockMvc ;
    @MockBean
    private EducationInterface educationInterface ;

    private EducationRequest educationRequest ;
    private EducationResponse createEducationResponse ;
    private EducationResponse updateEducationResponse ;
    private int EduId  = 1 ;


    @BeforeEach
    void init() {
        educationRequest = EducationRequest.builder()
                .schoolName("test")
                .degree("test")
                .endDate("12/2/2021")
                .startDate("12/2/2020")
                .description("test")
                .build();


        createEducationResponse = EducationResponse.builder()
                .eduId(1)
                .schoolName("test")
                .degree("test")
                .endDate("12/2/2021")
                .startDate("12/2/2020")
                .description("test")
                .build();


        updateEducationResponse = EducationResponse.builder()
                .eduId(1)
                .schoolName("update")
                .degree("update")
                .endDate("12/2/2021")
                .startDate("12/2/2020")
                .description("update")
                .build();


    }


    @Test
    void createEdu_success() throws Exception {

        Mockito.when(educationInterface.insertEdu(any(EducationRequest.class)))
                .thenReturn(createEducationResponse) ;

        ObjectMapper mapper = new ObjectMapper() ;
        String content = mapper.writeValueAsString(educationRequest) ;
        mockMvc.perform(MockMvcRequestBuilders.post("/edu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.schoolName").value("test"));
    }

    @Test
    void updateEdu_success() throws Exception {
        Mockito.when(educationInterface.updateEdu(any(EducationRequest.class), eq(EduId)))
                .thenReturn(updateEducationResponse);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(educationRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/edu/{id}", EduId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.schoolName").value("update"));
    }

    @Test
    void deleteEdu_success() throws Exception {
        // Mock void method
        Mockito.doNothing().when(educationInterface).deleteEdu(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/edu/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"));
    }


}
