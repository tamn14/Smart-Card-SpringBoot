package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Request.OrdersUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.Service.Interface.OrdersInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrdersController {
    final  OrdersInterface ordersInterface ;

    @PostMapping()
    public ApiResponse<OrdersResponse> insertOrders (@Valid @RequestBody OrdersRequest request) {
        OrdersResponse ordersResponse = ordersInterface.insertOrders(request) ;
        return ApiResponse.<OrdersResponse>builder()
                .mess("Success")
                .result(ordersResponse)
                .build() ;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrders (@PathVariable("id") int id) {
        ordersInterface.deleteOrders(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }
    @GetMapping("/user")
    public ApiResponse<List<OrdersResponse>> findAllByUser() {
        List<OrdersResponse> ordersResponses = ordersInterface.findAllByUser() ;
        return ApiResponse.<List<OrdersResponse>>builder()
                .mess("Success")
                .result(ordersResponses)
                .build() ;
    }

    @GetMapping("/{id}")
    public ApiResponse<OrdersResponse> findById(@PathVariable("id") int id) {
        OrdersResponse ordersResponses = ordersInterface.findById(id) ;
        return ApiResponse.<OrdersResponse>builder()
                .mess("Success")
                .result(ordersResponses)
                .build() ;
    }

    @GetMapping(value = "/qr/image/{order}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] qrImage(
                          @PathVariable("order") int order

    ) {
        return ordersInterface.QrForPayment( order);
    }

    @GetMapping()
    public ApiResponse<Page<OrdersResponse>> findAllToAdmin(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<OrdersResponse> ordersResponses = ordersInterface.findAllToAdmin(page , size) ;
        return ApiResponse.<Page<OrdersResponse>>builder()
                .mess("Success")
                .result(ordersResponses)
                .build() ;
    }

    @PutMapping("/{id}")
    public ApiResponse<OrdersResponse> updateOrders (@Valid @RequestBody OrdersUpdateRequest request ,
                                                     @PathVariable("id") int id) {
        OrdersResponse ordersResponse = ordersInterface.updateOrders(request ,id) ;
        return ApiResponse.<OrdersResponse>builder()
                .mess("Success")
                .result(ordersResponse)
                .build() ;
    }

}
