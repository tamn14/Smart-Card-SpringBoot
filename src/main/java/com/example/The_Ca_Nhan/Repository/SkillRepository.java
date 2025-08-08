package com.example.The_Ca_Nhan.Repository;

import com.example.The_Ca_Nhan.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Integer> {
    public List<Skills> findByUsers(Users users) ;
}
