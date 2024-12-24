package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.AdtypeBean;

import jakarta.persistence.Tuple;

public interface AdtypeRepository extends JpaRepository<AdtypeBean, Integer>{

	@Query("select a.adtypeId, a.adPrice from AdtypeBean a where a.adName = :adName")
	List<Tuple> findAdtypeIdAndPriceByAdtype(String adName);
}
