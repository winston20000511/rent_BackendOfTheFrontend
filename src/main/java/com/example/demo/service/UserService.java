package com.example.demo.service;

import java.util.Optional;
import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根據 ID 查詢使用者
     * @param userId 使用者的 ID
     * @return Optional 包裝的 UserTableBean
     */
    public Optional<UserTableBean> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * 創建新用戶（註冊功能）
     * @param user 新的使用者資料
     * @return 創建後的 UserTableBean
     */
    public UserTableBean createUser(UserTableBean user) {
        return userRepository.save(user);
    }

    /**
     * 更新使用者資料（會員中心功能）
     * @param userId 使用者的 ID
     * @param updatedUser 更新後的使用者資料
     * @return 更新後的 UserTableBean
     */
    public UserTableBean updateUser(Long userId, UserTableBean updatedUser) {
        Optional<UserTableBean> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            UserTableBean user = existingUser.get();
            // 更新使用者資料
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPhone(updatedUser.getPhone());
            user.setGender(updatedUser.getGender());
            user.setCoupon(updatedUser.getCoupon());
            user.setStatus(updatedUser.getStatus());
            user.setCreateTime(updatedUser.getCreateTime());

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /**
     * 刪除使用者（完全刪除）
     * @param userId 使用者的 ID
     */
    public void deleteUser(Long userId) {
        Optional<UserTableBean> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /**
     * 根據 email 查詢使用者（登入功能）
     * @param email 使用者的 email
     * @return 查詢到的 UserTableBean
     */
    public UserTableBean getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
