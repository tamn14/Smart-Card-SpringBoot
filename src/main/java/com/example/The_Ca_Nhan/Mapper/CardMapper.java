package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.Entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    // mapping from Entity to  DTO
    @Mapping(source = "cardId", target = "cardId")
    CardResponse toDTO (Card card) ;

    // mapping from DTO to Entity
    // khong dung entity tu tao de tranh loi detech
    default Card toEntity (CardRequest request) {
        return Card.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build() ;
    }


}
