package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.Entity.Card;
import com.example.The_Ca_Nhan.Entity.Orders;
import com.example.The_Ca_Nhan.Entity.Users;

public interface MailInterface {
    public void SendMessage(String from, String to  , byte[] qrBytes , Users passenger , Card card , Orders orders) ;
    public void verifyEmail (String from, String to  , String numberVerify, String name) ;
    public void ContactFromCustomer (String from, String to , String name , String title , String content  ) ;
    public void ConnectWithUser (String from,  String name , String title , String content  ) ;

}
