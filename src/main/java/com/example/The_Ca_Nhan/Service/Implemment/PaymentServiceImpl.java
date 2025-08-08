package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Response.PaymentResponse;
import com.example.The_Ca_Nhan.Entity.Payment;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.PaymentMapper;
import com.example.The_Ca_Nhan.Repository.PaymentRepository;
import com.example.The_Ca_Nhan.Service.Interface.PaymentInterface;
import jakarta.transaction.Transactional;
import jdk.jfr.StackTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentInterface {
    private final PaymentRepository paymentRepository ;
    private final PaymentMapper paymentMapper;


    @Override
    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll()
                .stream().map(paymentMapper::toDTO).toList() ;
    }

    @Override
    public PaymentResponse findById(int id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PAYMENT_NOT_FOUND)) ;
        return paymentMapper.toDTO(payment) ;
    }
}
