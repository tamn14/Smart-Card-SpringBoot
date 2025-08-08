package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.PaymentRequest;
import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.DTO.Response.PaymentResponse;
import com.example.The_Ca_Nhan.Repository.PaymentRepository;
import com.example.The_Ca_Nhan.Service.Interface.PaymentInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    final PaymentInterface paymentInterface ;

    @GetMapping()
    public ApiResponse<List<PaymentResponse>> insertPayment() {
        List<PaymentResponse> paymentResponses = paymentInterface.findAll() ;
        return ApiResponse.<List<PaymentResponse>>builder()
                .mess("Success")
                .result(paymentResponses)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PaymentResponse> insertPayment(@PathVariable("id") int id) {
        PaymentResponse paymentResponses = paymentInterface.findById(id) ;
        return ApiResponse.<PaymentResponse>builder()
                .mess("Success")
                .result(paymentResponses)
                .build();
    }
}
