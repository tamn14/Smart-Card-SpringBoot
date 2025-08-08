package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.Entity.Orders;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrInterface {
    public byte[] generateQRCodeToFile(String json , int width, int height)  throws WriterException, IOException;
    public String createVnpayPaymentUrl(Orders orders) ;
}
