package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Response.ApiResponse;
import com.example.The_Ca_Nhan.Service.Interface.MailInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MailController {
    MailInterface mailInterface ;

    @PostMapping("/customer")
    public ApiResponse<Void> ContactFromCus(@RequestParam("name") String name ,
                                            @RequestParam("from") String from ,
                                            @RequestParam("title") String title ,
                                            @RequestParam("content") String content
                                            ) {

        mailInterface.ContactFromCustomer(from , "tamb2110658@student.ctu.edu.vn" , name , title , content);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build();

    }

    @PostMapping("/connect")
    public ApiResponse<Void> ConnectWithUser (@RequestParam("name") String name ,
                                            @RequestParam("from") String from ,
                                            @RequestParam("title") String title ,
                                            @RequestParam("content") String content
    ) {

        mailInterface.ConnectWithUser(from , name , title , content);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build();

    }




}
