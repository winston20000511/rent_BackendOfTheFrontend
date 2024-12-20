package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.UserTableBean;

public interface UserRepository extends JpaRepository<UserTableBean, Long> {

    // 根據 email 查詢使用者，登入功能需要用到
    UserTableBean findByEmail(String email);

    @Query("SELECT u.userId FROM UserTableBean u WHERE u.email = :email")
    Optional<Long> findUserIdByEmail(String email);
}
