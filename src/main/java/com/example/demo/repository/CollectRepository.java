package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CollectTableBean;

import jakarta.transaction.Transactional;

@Repository
public interface CollectRepository extends JpaRepository<CollectTableBean, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM CollectTableBean c WHERE c.house.houseId = :houseId")
    void deleteByHouseId(@Param("houseId") Long houseId);
}