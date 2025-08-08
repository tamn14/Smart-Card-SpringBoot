package com.example.The_Ca_Nhan.Service.Interface;



import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Request.OrdersUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdersInterface {
    public OrdersResponse insertOrders(OrdersRequest request) ;
    public void deleteOrders(int Id);
    public List<OrdersResponse> findAllByUser() ;
    public OrdersResponse findById(int id) ;
    public byte[] QrForPayment (int orderId );
    public OrdersResponse updateOrders (OrdersUpdateRequest ordersUpdateRequest, int orderId) ;
    public Page<OrdersResponse> findAllToAdmin(int page, int size) ;
}
