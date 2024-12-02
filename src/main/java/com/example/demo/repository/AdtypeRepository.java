package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AdtypeBean;

public interface AdtypeRepository extends JpaRepository<AdtypeBean, Integer>{

<<<<<<< HEAD
=======
	@Query("select a.adtypeId, a.adPrice from AdtypeBean a where a.adName = :adName")
	public List<Tuple> findAdtypeIdAndPriceByadtype(String adName);
>>>>>>> parent of 5ca4084 (2024-11-30 update)
}
