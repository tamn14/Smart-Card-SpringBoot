package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.PaymentRequest;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.DTO.Response.PaymentResponse;
import com.example.The_Ca_Nhan.Entity.Orders;
import com.example.The_Ca_Nhan.Entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" )
public interface PaymentMapper {
    // mapping from Entity to DTO
    PaymentResponse toDTO (Payment payment)  ;


    Payment toEntity (PaymentRequest request)  ;
}
