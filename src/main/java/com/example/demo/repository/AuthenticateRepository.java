package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserTableBean;

	public interface AuthenticateRepository extends JpaRepository<UserTableBean, Long> {
		Optional<UserTableBean> findByEmailAndPassword(String email, String password);
		
		Optional<UserTableBean> findByEmail(String email);

		
	}

	


