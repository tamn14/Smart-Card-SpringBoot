package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CardInterface {
    public CardResponse insertCard(CardRequest request) ;
    public CardResponse updateCard(CardRequest request , int cardId) ;
    public void deleteCard(int cardId);
    public Page<CardResponse> findAll(int page, int size) ;
    public CardResponse findById(int id) ;
    public Page<CardResponse> findByName( String name, int page, int size) ;
}
