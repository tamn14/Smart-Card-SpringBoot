package com.example.The_Ca_Nhan.Util;

import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.Entity.Card;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.CardMapper;
import com.example.The_Ca_Nhan.Repository.CardRepository;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class Extract {

    private final UsersRepository usersRepository ;
    private final CardRepository cardRepository ;
    private final CardMapper cardMapper ;
    public String extractUserId(ResponseEntity<?> responseEntity) {
        // lay ra Location
        // Response ma keycloak tra ve gom HTTP va Location vd nhu sau :
        // HTTP/1.1 201 Created
        //Location: http://localhost:8080/admin/realms/myrealm/users/1a2b3c4d-5678-90ab-cdef-1234567890ab
        String Location = Objects.requireNonNull(responseEntity.getHeaders().get("Location")).get(0);
        // tach theo dau '/'
        String[] strings = Location.split("/") ;
        return strings[strings.length-1] ;
    }

    public Users getUserInFlowLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String keyloakId = auth.getName();
        log.info("Current authenticated username from token: {}", keyloakId);
        Users users = usersRepository.findByKeycloakId(keyloakId) ;
        if(users == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED) ;
        }
        return users ;
    }



}
