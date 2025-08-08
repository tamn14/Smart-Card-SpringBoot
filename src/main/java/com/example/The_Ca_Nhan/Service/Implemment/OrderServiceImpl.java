package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Request.OrdersUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Entity.*;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.OrdersMapper;
import com.example.The_Ca_Nhan.Mapper.PaymentMapper;
import com.example.The_Ca_Nhan.Properties.OrdersStatus;
import com.example.The_Ca_Nhan.Properties.PaymentProperties;
import com.example.The_Ca_Nhan.Repository.CardRepository;
import com.example.The_Ca_Nhan.Repository.OrdersRepository;
import com.example.The_Ca_Nhan.Repository.PaymentRepository;
import com.example.The_Ca_Nhan.Service.Interface.MailInterface;
import com.example.The_Ca_Nhan.Service.Interface.OrdersInterface;
import com.example.The_Ca_Nhan.Service.Interface.PaymentInterface;
import com.example.The_Ca_Nhan.Service.Interface.QrInterface;
import com.example.The_Ca_Nhan.Util.Extract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersInterface {

    private final OrdersMapper ordersMapper ;
    private final OrdersRepository ordersRepository ;
    private final CardRepository cardRepository  ;
    private final Extract extract ;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final QrInterface qrInterface ;
    private final PaymentProperties paymentProperties ;


    @Value("${Qr.width}")
    private int widthQr ;
    @Value("${Qr.height}")
    private int heightQr ;

    private Card getCardById(int id ) {
        return cardRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CARD_NOT_FOUND)) ;
    }

    private Orders getOrdersById(int id ) {
        return ordersRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.ORDERS_NOT_FOUND)) ;
    }

    private void checkAuthenticated (Users users , Orders orders) {
        if(!(orders.getUsers().getUserName().equals(users.getUserName()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED) ;
        }
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public OrdersResponse insertOrders(OrdersRequest request) {
        // kiem tra card ton tai
        Card card = getCardById(request.getCardId()) ;
        // chuyen request thanh entity
        Orders orders = ordersMapper.toEntity(request) ;
        // lay user
        Users users = extract.getUserInFlowLogin();
        // chuyen payment ve entity
        Payment payment = paymentMapper.toEntity(request.getPaymentRequest()) ;
        // set quan he hai chieu
        orders.setPayment(payment);
        orders.setUsers(users);
        orders.setCard(card);
        orders.setStatus(OrdersStatus.PENDING);
        paymentRepository.save(payment) ;
        Orders ordOrdersInsert = ordersRepository.save(orders) ;

        return ordersMapper.toDTO(ordOrdersInsert) ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public void deleteOrders(int Id) {
        // lay user dang trong phien dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        Orders orders = getOrdersById(Id) ;

        // kiem tra Orders ton tai
        checkAuthenticated(users , orders);
        // xoa quan he hai chieu
        orders.setPayment(null);
        orders.setUsers(null);
        ordersRepository.deleteById(Id);
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public List<OrdersResponse> findAllByUser() {
        // lay user dang trong phien dang nhap
        Users users = extract.getUserInFlowLogin() ;
        return ordersRepository.findByUsers(users).stream().map(ordersMapper::toDTO).toList();
    }



    @Override
    @PreAuthorize( "hasRole('USER')")
    public OrdersResponse findById(int id) {
        // lay user dang trong phien dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra Orders ton tai
        Orders orders = getOrdersById(id) ;
        // kiem tra Orders ton tai
        checkAuthenticated(users , orders);
        return ordersMapper.toDTO(orders) ;
    }

    @Override
    public byte[] QrForPayment(  int orderId) {
        Users users = extract.getUserInFlowLogin();
        Orders orders = getOrdersById(orderId);
        checkAuthenticated(users, orders);

        String paymentUrl = qrInterface.createVnpayPaymentUrl(orders);
        try {
            return qrInterface.generateQRCodeToFile(paymentUrl, 300, 300);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_CREATE_QR);
        }

    }

    @Override
    public OrdersResponse updateOrders(OrdersUpdateRequest ordersUpdateRequest, int orderId) {
        Orders orders = getOrdersById(orderId) ;
        if(ordersUpdateRequest.getStatus() != null) {
            orders.setStatus(ordersUpdateRequest.getStatus());
        }
        if(ordersUpdateRequest.getPaymentUpdateRequest().getStatus() != null) {
            orders.getPayment().setStatus(ordersUpdateRequest.getPaymentUpdateRequest().getStatus());
        }

        Orders ordersUpdate =  ordersRepository.save(orders) ;
        return ordersMapper.toDTO(ordersUpdate) ;

    }

    @Override
    public Page<OrdersResponse> findAllToAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> orderPage = ordersRepository.findAll(pageable);

        List<OrdersResponse> ordersResponses = orderPage.getContent()
                .stream()
                .map(ordersMapper::toDTO)
                .toList();
        return new PageImpl<>(ordersResponses, pageable, orderPage.getTotalElements());
    }
}
