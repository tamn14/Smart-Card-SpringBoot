package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.Entity.Orders;
import com.example.The_Ca_Nhan.Properties.QrProperties;
import com.example.The_Ca_Nhan.Service.Interface.QrInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class QrServiceImpl implements QrInterface {

    private final QrProperties qrProperties;

    /**
     * Sinh QR từ URL thanh toán
     */
    @Override
    public byte[] generateQRCodeToFile(String url, int width, int height) throws IOException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);
            hintMap.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hintMap);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate QR code", e);
        }
    }

    /**
     * Tạo URL thanh toán VNPAY cho đơn hàng
     */
    @Override
    public String createVnpayPaymentUrl(Orders order) {
        String vnp_TmnCode = qrProperties.getVnp_TmnCode();
        String vnp_HashSecret = qrProperties.getVnp_HashSecret();
        String vnp_Url = qrProperties.getVnp_Url();
        String returnUrl = qrProperties.getReturnUrl();

        int amount = order.getTotalAmount() * 100; // VNPAY dùng đơn vị *100

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", String.valueOf(order.getOrderId()));
        params.put("vnp_OrderInfo", "Thanh toan don hang #" + order.getOrderId());
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Sắp xếp theo Alphabet A-Z để ký
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder signData = new StringBuilder();
        StringBuilder queryUrl = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // build data để ký
                signData.append(fieldName).append("=").append(fieldValue);

                // build query string encode cho URL
                queryUrl.append(fieldName).append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (i < fieldNames.size() - 1) {
                    signData.append("&");
                    queryUrl.append("&");
                }
            }
        }

        // Tạo chữ ký HMAC SHA512
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, signData.toString());
        queryUrl.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return vnp_Url + "?" + queryUrl.toString();
    }

    private String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                return null;
            }
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Cannot sign data with HMAC SHA512", ex);
        }
    }
}
