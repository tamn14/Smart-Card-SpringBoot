package com.example.The_Ca_Nhan.Repository;


import com.example.The_Ca_Nhan.Entity.Experiences;
import com.example.The_Ca_Nhan.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperiencesRepository extends JpaRepository<Experiences, Integer> {
    public List<Experiences> findByUsers(Users users) ;
}
