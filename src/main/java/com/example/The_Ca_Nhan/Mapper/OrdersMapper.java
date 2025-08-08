package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.Entity.Experiences;
import com.example.The_Ca_Nhan.Entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {
        UsersMapper.class,
        PaymentMapper.class,
        CardMapper.class

})
public interface OrdersMapper {
    // mapping from Entity to DTO
    @Mapping(source = "orders.users", target = "usersResponse")
    @Mapping(source = "orders.card", target = "cardResponse")
    @Mapping(source = "orders.payment", target = "paymentResponse")
    OrdersResponse toDTO (Orders orders)  ;

    default Orders toEntity (OrdersRequest request) {
        return Orders.builder()
                .orderType(request.getOrderType())
                .totalAmount(request.getTotalAmount())
                .status(request.getStatus())
                .address(request.getAddress())
                .ordersDate(request.getOrdersDate())
                .build();
    }
}
