package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserTableBean;

@Repository
public interface UserRepository extends JpaRepository<UserTableBean, Long> {

    // 使用電子郵件查詢用戶，這樣就可以檢查註冊的電子郵件是否已經存在 // 修改這裡，讓 findByEmail 返回 Optional<User>
    UserTableBean findByEmail(String email);
    
    // 如果你有其他查詢需求，可以在這裡加入更多的方法
}