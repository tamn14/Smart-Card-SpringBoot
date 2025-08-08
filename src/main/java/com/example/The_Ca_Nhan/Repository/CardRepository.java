package com.example.The_Ca_Nhan.Repository;

import com.example.The_Ca_Nhan.Entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card , Integer> {
    public boolean existsByCardId(int id) ;
    public List<Card> findByName(String name) ;
    Page<Card> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
