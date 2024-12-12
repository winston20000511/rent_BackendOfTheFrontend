package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    // 注入 UserRepository，用來訪問資料庫
    @Autowired
    private UserRepository userRepository;

    // 創建新的使用者
    public UserTableBean createUser(UserTableBean user) {
        // 保存使用者到資料庫，並返回保存的結果
        return userRepository.save(user);
    }

    // 根據 ID 查詢使用者。使用 Optional 可以讓我們在查詢時顯式地處理空值，避免出現 NullPointerException。
    public Optional<UserTableBean> getUserById(Long userId) {
        // 使用 findById 查詢使用者，這會返回 Optional 包裝的結果
        return userRepository.findById(userId);
    }

    // 根據電子信箱查詢使用者
    public UserTableBean getUserByEmail(String email) {
        // 查詢使用者
        return userRepository.findByEmail(email);
    }

    // 更新使用者資料
    public UserTableBean updateUser(Long userId, UserTableBean updatedUser) {
        // 檢查使用者是否存在
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

            // 保存並返回更新後的使用者
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // 刪除使用者
    public void deleteUser(Long userId) {
        // 查詢是否存在該使用者
        Optional<UserTableBean> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // 刪除使用者
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }
 // 註冊使用者
    public boolean registerUser(UserTableBean user) {
        // 可以在這裡做資料驗證、密碼加密等操作
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



	public UserTableBean checkLogin(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}
}

