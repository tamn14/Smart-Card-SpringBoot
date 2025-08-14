package com.example.The_Ca_Nhan.Controller;


import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Service.Interface.CardInterface;
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
import reactor.core.publisher.Sinks;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(CardController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc ;
    @MockBean
    private CardInterface cardInterface ;

    private CardRequest cardRequest ;
    private CardResponse createCardResponse  ;
    private CardResponse updateCardResponse   ;
    private List<CardResponse> cardResponseList ;
    private int cardID = 1 ;

    @BeforeEach
    void init() {
        cardRequest = CardRequest.builder()
                .name("CardTest")
                .description("Mo ta test")
                .price(100)
                .build();

        createCardResponse = CardResponse.builder()
                .cardId(1)
                .name("CardTest")
                .description("Mo ta test")
                .price(100)
                .build();

        // Response cho update
        updateCardResponse = CardResponse.builder()
                .cardId(1)
                .name("CardUpdate")
                .description("Mo ta test update")
                .price(1020)
                .build();

        cardResponseList = List.of(
                createCardResponse ,
                CardResponse.builder()
                        .cardId(2)
                        .name("CardTest2")
                        .description("Mo ta test2")
                        .price(1005)
                        .build()

        ) ;

    }


    @Test
    void createCard_success() throws Exception {

        Mockito.when(cardInterface.insertCard(any(CardRequest.class)))
                .thenReturn(createCardResponse) ;

        ObjectMapper mapper = new ObjectMapper() ;
        String content = mapper.writeValueAsString(cardRequest) ;
        mockMvc.perform(MockMvcRequestBuilders.post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("CardTest"));
    }

    @Test
    void updateCard_success() throws Exception {
        Mockito.when(cardInterface.updateCard(any(CardRequest.class), eq(cardID)))
                .thenReturn(updateCardResponse);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(cardRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/cards/{id}", cardID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("CardUpdate"));
    }

    @Test
    void deleteCard_success() throws Exception {
        // Mock void method
        Mockito.doNothing().when(cardInterface).deleteCard(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cards/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"));
    }

    @Test
    void getAllCard_success() throws Exception {
        // Mock Page
        org.springframework.data.domain.Page<CardResponse> page =
                new org.springframework.data.domain.PageImpl<>(cardResponseList);

        Mockito.when(cardInterface.findAll(0, 4)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/cards")
                        .param("page", "0")
                        .param("size", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].name").value("CardTest"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[1].name").value("CardTest2"));
    }



}
