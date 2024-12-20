package com.example.demo.helper;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtil {

    private static UserRepository userRepository;

    // 靜態注入 UserRepository
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        UserUtil.userRepository = userRepository;
    }

    /**
     * 根據 email 獲取 userId 的工具方法
     * @param email 用戶的郵箱
     * @return userId (Long)
     * @throws RuntimeException 若未找到用戶
     */
    public static Long getUserIdByEmail(String email) {
        Optional<Long> userId = userRepository.findUserIdByEmail(email);
        return userId.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}