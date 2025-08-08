package com.example.The_Ca_Nhan.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id  ;
    @Column(nullable = false, unique = true)
    private String userName ;
    private String keycloakId ;
    private String lastName ;
    private String firstName ;
    @Column(nullable = false, unique = true)
    private String email ;
    private String address ;
    private LocalDate createAt ;
    private boolean enable;
    private String accountNumber ;
    private String url ; // duong dan den web ca nhan
    private String image ;
    private LocalDateTime deleteAt ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Orders> orders = new ArrayList<>();
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Experiences> experiences = new ArrayList<>();
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Projects> projects = new ArrayList<>() ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Educations> educations = new ArrayList<>() ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Skills> skills = new ArrayList<>() ;
    @OneToOne(mappedBy = "users")
    private Profiles profiles ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<MediaFiles> mediaFiles = new ArrayList<>() ;



}
