package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.Entity.Card;
import com.example.The_Ca_Nhan.Entity.Orders;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Service.Interface.MailInterface;
import com.example.The_Ca_Nhan.Util.Extract;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailInterface {
    private final JavaMailSender javaMailSender;
    private  final Extract extract ;
    @Override
    public void SendMessage(String from, String to, byte[] qrBytes, Users passenger,  Card card , Orders orders) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("SmartCard - card của bạn");

            String html = """
    <p>Xin chào, %s</p>
    <p>Bạn đã đặt the thành công với mã: <strong>%s</strong></p>
    <p>Ngày đặt: %s</p>
    <p>Link lien ket : %s</p>
    <p>Dưới đây là mã QR để ket noi trang ca nhan cua ban:</p>
    <img src='cid:qrCode'/>
    """.formatted(passenger.getFirstName(),card.getCardId(),
                    orders.getOrdersDate(),
                    passenger.getUrl()
                    );

            helper.setText(html, true);
            helper.addInline("qrCode", new ByteArrayResource(qrBytes), "image/png");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.EMAIL_SERVICE_FAILED);
        }
    }

    @Override
    public void verifyEmail(String from, String to, String numberVerify , String name) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("SmartCard - Xac thuc tai khoan");

            String html = """
    <p>Xin chào, %s</p>
    <p>Dưới đây là mã xac thuc để xac thuc tai khoan cua ban:</p>
    <p>%s</p>
    """.formatted(name,
                    numberVerify);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.EMAIL_SERVICE_FAILED) ;
        }
    }

    @Override
    public void ContactFromCustomer(String from, String to, String name, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Mail Contact");

            String html = """
    <p>Mail phan hoi tu khach hang %s</p>
    <p>Noi dung </p>
    <p>%s</p>
    """.formatted(name,
                    content);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.EMAIL_SERVICE_FAILED) ;
        }
    }


    @Override
    public void ConnectWithUser(String from,  String name, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        Users user = extract.getUserInFlowLogin() ;

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Mail Lien He Tu Portfolio cua ban");

            String html = """
    <p>Mail lien he tu portfolio cua ban %s</p>
    <p>Noi dung </p>
    <p>%s</p>
    """.formatted(name,
                    content);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.EMAIL_SERVICE_FAILED) ;
        }
    }
}
