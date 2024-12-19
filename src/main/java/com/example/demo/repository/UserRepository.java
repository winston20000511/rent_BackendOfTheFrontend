package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserTableBean;

public interface UserRepository extends JpaRepository<UserTableBean, Long> {

    // 根據 email 查詢使用者，登入功能需要用到
    UserTableBean findByEmail(String email);


}
