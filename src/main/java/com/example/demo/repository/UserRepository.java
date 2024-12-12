package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserTableBean;

public interface UserRepository extends JpaRepository<UserTableBean, Long> {
    UserTableBean findByEmail(String email);
}