package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AdBean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class AdBeanDaoImpl implements AdBeanDao{
	
	private EntityManager entityManager;
	
	@Autowired
	public AdBeanDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// c

	// r
	@Override
	public List<AdBean>findAdsByUserId(Integer userId){
		StringBuilder sql = new StringBuilder("select*from users_table where user_id = :userId");
		Query query = entityManager.createNamedQuery(sql.toString());
		query.setParameter("userId", userId);
		List<AdBean> ads = query.getResultList();
		return ads;
	}
	
	// u
	
	// d
	
}
