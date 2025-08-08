package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.Service.Interface.TokenBlackListInterface;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class TokenBlackListServiceImpl implements TokenBlackListInterface {
    private final RedisTemplate<String, String> redisTemplate; //redisTemplate duoc dung de doc ghi du lieu tu Redis
    private static final String BLACKLIST_PREFIX = "blacklisted_token:"; // Prefix duoc dung de phan biet token da duoc dua vao blacklist hay chua
    @Autowired
    public TokenBlackListServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void addToBlacklist(String token) {
        // Lưu token với timeToLive là thời gian sống còn lại của token
        long timeToLive = extractRemainingSeconds(token);
        log.info("⏱ TTL của token: {}", timeToLive);
        var connFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        System.out.println("✅ Redis kết nối host=" + connFactory.getHostName() + ", port=" + connFactory.getPort());

        // dua ttl vao blacklist
        // 'revoked' dung de danh dau token nay da bi thu hoi
        // timeToLive dung de dam bao rang token se bi xoa sau khi het han
        // 'TimeUnit.SECONDS' dung de chi dinh timeToLive la giay
        // cau lenh co nghia SET key value EX 3600 trong redis , EX=Expire in seconds va do redis dinh nghia
        if (timeToLive > 0) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "revoked", timeToLive, TimeUnit.SECONDS);
            log.info("🔒 Token đã được thêm vào Redis blacklist: {}", BLACKLIST_PREFIX + token);

        } else {
            log.warn("⚠️ Token hết hạn. Không thêm vào Redis.");
        }


    }

    @Override
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }

    private long extractRemainingSeconds(String token) {
        try {
            // phan tich token de lay thoi gian song
            SignedJWT signedJWT = SignedJWT.parse(token); // phan tich chuoi jwt
            Date expirationTimeToken = signedJWT.getJWTClaimsSet().getExpirationTime();

            long expirationTime = expirationTimeToken.toInstant().getEpochSecond(); // lay thoi gian het han va chuyen thanh epoch seconds
            long nowTime = Instant.now().getEpochSecond(); // lay thoi gian hien tai
            long remaining = expirationTime - nowTime; // tinh thoi gian con lai
            // neu con song co nghia thoi gian het han lon hon thoi gian hien tai thi tra ve thoi gian con lai
            return remaining > 0 ? remaining : 0;
        } catch (Exception e) {
            //  truong hop khong lay duoc timeToLive, luc nay set mac dinh la 3600 giay de sau 3600 thi token se bi xoa khoi redis
            log.error("Không lấy được TTL từ token. Dùng mặc định 3600 giây.", e);
            return 3600;

        }
    }


}
