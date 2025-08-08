package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Response.PaymentResponse;

import java.util.List;

public interface PaymentInterface {

    public List<PaymentResponse> findAll() ;
    public PaymentResponse findById(int id) ;

}
