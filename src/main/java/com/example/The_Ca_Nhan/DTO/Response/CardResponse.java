package com.example.The_Ca_Nhan.DTO.Response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponse {
    private int cardId ;
    private String name ;
    private String description ;
    private int price  ;
    private String url ;
}
