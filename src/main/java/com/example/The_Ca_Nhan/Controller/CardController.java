package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.Service.Interface.CardInterface;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CardController {
    final CardInterface cardInterface;

    @PostMapping()
    public ApiResponse<CardResponse> insertCard(@Valid @ModelAttribute CardRequest cardRequest) {
        CardResponse cardResponse = cardInterface.insertCard(cardRequest);
        return ApiResponse.<CardResponse>builder()
                .mess("Success")
                .result(cardResponse)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CardResponse> updateCard(@Valid @ModelAttribute CardRequest cardRequest,
                                                @PathVariable("id") int id) {
        CardResponse cardResponse = cardInterface.updateCard(cardRequest, id);
        return ApiResponse.<CardResponse>builder()
                .mess("Success")
                .result(cardResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCard(@PathVariable("id") int id) {
        cardInterface.deleteCard(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build();

    }

    @GetMapping()
    public ApiResponse<Page<CardResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Page<CardResponse> cardResponses = cardInterface.findAll(page, size);
        return ApiResponse.<Page<CardResponse>>builder()
                .mess("Success")
                .result(cardResponses)
                .build();
    }

    @GetMapping("id/{id}")
    public ApiResponse<CardResponse> findById(@PathVariable("id") int id) {
        CardResponse cardResponse = cardInterface.findById(id);
        return ApiResponse.<CardResponse>builder()
                .mess("Success")
                .result(cardResponse)
                .build();
    }

    @GetMapping("name/{name}")
    public ApiResponse<Page<CardResponse>> findByName(@PathVariable("name") String name,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size
    ) {
        Page<CardResponse> cardResponse = cardInterface.findByName(name, page, size);
        return ApiResponse.<Page<CardResponse>>builder()
                .mess("Success")
                .result(cardResponse)
                .build();
    }


}
