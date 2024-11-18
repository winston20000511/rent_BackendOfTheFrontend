package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserTableBean, Integer> {
	
	UserTableBean findByEmail(String email);
}
