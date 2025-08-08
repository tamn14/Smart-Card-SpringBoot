package com.example.The_Ca_Nhan.Service.Interface;

public interface TokenBlackListInterface {
    public  void addToBlacklist(String token);
    public boolean isBlacklisted(String token);
}
