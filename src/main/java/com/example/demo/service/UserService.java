package com.example.demo.service;

import java.util.Optional;
import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.dto.*;
import com.example.demo.dto.UserCenterDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 業務邏輯層，負責處理會員相關邏輯
 */
@Service
@Slf4j
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
     * 註冊新使用者
     *
     * @param userRegisterDTO 前端傳遞的註冊資料
     * @return 註冊結果訊息
     */
    @Transactional
    public String registerUser(UserRegisterDTO userRegisterDTO) {
        log.info("開始處理註冊請求：{}", userRegisterDTO);

        // 檢查名稱是否已存在
        if (userRepository.existsByName(userRegisterDTO.getName())) {
            log.warn("名稱已被註冊：{}", userRegisterDTO.getName());
            return "使用者名稱已被註冊";
        }

        // 檢查電子郵件是否已存在
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            log.warn("電子郵件已被註冊：{}", userRegisterDTO.getEmail());
            return "電子郵件已被註冊";
        }

        // 檢查手機號碼是否已存在
        if (userRepository.existsByPhone(userRegisterDTO.getPhone())) {
            log.warn("手機號碼已被註冊：{}", userRegisterDTO.getPhone());
            return "手機號碼已被註冊";
        }

        // 驗證手機號碼格式
        if (!isValidPhone(userRegisterDTO.getPhone())) {
            log.error("手機號碼格式不正確：{}", userRegisterDTO.getPhone());
            return "手機號碼格式不正確，應為 10 位數字";
        }

        // 驗證密碼格式
        if (!isValidPassword(userRegisterDTO.getPassword())) {
            log.error("密碼格式不正確：{}", userRegisterDTO.getPassword());
            return "密碼格式不正確，需至少 8 位且包含英文與數字";
        }

        // 建立新使用者
        UserTableBean user = new UserTableBean();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword()); // 暫時直接儲存明文密碼
        user.setPhone(userRegisterDTO.getPhone());
        user.setGender(userRegisterDTO.getGender());
        user.setStatus((byte) 1); // 預設啟用狀態
        userRepository.save(user); // 儲存資料到資料庫

        log.info("註冊成功：{}", userRegisterDTO.getName());
        return "註冊成功";
    }

    /**
     * 驗證手機號碼格式
     *
     * @param phone 手機號碼
     * @return 是否為有效手機號碼
     */
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\d{10}$");
    }

    /**
     * 驗證密碼格式
     *
     * @param password 密碼
     * @return 是否為有效密碼
     */
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password != null && Pattern.matches(passwordPattern, password);
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

    /**
     * 查詢屋主資訊，根據房屋 ID
     * @param houseId 房屋的 ID
     * @return 屋主資訊的 DTO，包括名稱、電話和圖片（Base64 格式）
     */
    

    /**
     * 從 Token 中解析使用者資料並返回會員中心資料
     *
     * @param token JWT Token
     * @return 會員中心資料的 DTO
     */
    @Transactional
    public UserCenterDTO getUserCenterData(String token) {
        // 驗證並解析 JWT Token
        String email = JwtUtil.verify(token)[0];
        if (email == null) {
            throw new RuntimeException("無效的 Token");
        }

        log.info("解析 Token 成功，email: {}", email);

        // 根據 email 查詢使用者資料
        Optional<UserTableBean> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isEmpty()) {
            log.warn("未找到對應的使用者資料，email: {}", email);
            throw new RuntimeException("會員資料未找到");
        }

        UserTableBean user = userOptional.get();

        // 將會員資料轉換為 DTO
        UserCenterDTO userCenterDTO = new UserCenterDTO();
        userCenterDTO.setUserId(user.getUserId());
        userCenterDTO.setName(user.getName());
        userCenterDTO.setEmail(user.getEmail());
        userCenterDTO.setPhone(user.getPhone());
        userCenterDTO.setPicture(user.getPicture());
        userCenterDTO.setGender(user.getGender());
        userCenterDTO.setCoupon(user.getCoupon());
        userCenterDTO.setStatus(user.getStatus());
        userCenterDTO.setCreateTime(user.getCreateTime());

        return userCenterDTO;
    }

    /**
     * 從 Token 中解析使用者資料並返回會員簡歷
     *
     * @param token JWT Token
     * @return 包含會員名稱、郵件、電話與圖片的 DTO
     */
    @Transactional
    public UserSimpleInfoDTO getUserSimpleInfo(String token) {
        // 驗證並解析 JWT Token，取得 email
        String email = JwtUtil.verify(token)[0];
        if (email == null) {
            throw new RuntimeException("無效的 Token"); // Token 無效則拋出例外
        }

        log.info("解析 Token 成功，email: {}", email);

        // 根據 email 查詢使用者資料
        Optional<UserTableBean> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isEmpty()) {
            log.warn("未找到對應的使用者資料，email: {}", email);
            throw new RuntimeException("會員資料未找到");
        }

        UserTableBean user = userOptional.get();

        // 將會員資料轉換為 DTO
        UserSimpleInfoDTO userSimpleInfoDTO = new UserSimpleInfoDTO();
        userSimpleInfoDTO.setName(user.getName());
        userSimpleInfoDTO.setEmail(user.getEmail());
        userSimpleInfoDTO.setPhone(user.getPhone());
        userSimpleInfoDTO.setPicture(user.getPicture());

        return userSimpleInfoDTO;
    }

    @Transactional
    public UserCenterDTO updateUserProfile(String token, UserUpdateDTO updateRequest) {
        // 驗證 JWT 並解析 Email
        String email = JwtUtil.verify(token)[0];
        if (email == null) {
            throw new RuntimeException("無效的 Token");
        }

        log.info("解析 Token 成功，email: {}", email);

        // 根據 Email 查詢使用者
        UserTableBean user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("會員資料未找到");
        }

        // 驗證並更新資料
        if (updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }
        if (updateRequest.getPhone() != null && !updateRequest.getPhone().equals(user.getPhone())) {
            if (!isValidPhone(updateRequest.getPhone())) {
                throw new RuntimeException("手機號碼格式不正確，應為 10 位數字");
            }
            if (userRepository.existsByPhone(updateRequest.getPhone())) {
                throw new RuntimeException("手機號碼已被使用");
            }
            user.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getPassword() != null) {
            user.setPassword(updateRequest.getPassword()); // 密碼可進一步加密處理
        }
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
        }
        if (updateRequest.getPicture() != null) {
            user.setPicture(updateRequest.getPicture());
        }

        // 儲存更新後的使用者
        userRepository.save(user);

        // 將更新後的資料轉換為 DTO 返回
        UserCenterDTO userCenterDTO = new UserCenterDTO();
        userCenterDTO.setUserId(user.getUserId());
        userCenterDTO.setName(user.getName());
        userCenterDTO.setEmail(user.getEmail());
        userCenterDTO.setPhone(user.getPhone());
        userCenterDTO.setPicture(user.getPicture());
        userCenterDTO.setGender(user.getGender());
        userCenterDTO.setCoupon(user.getCoupon());
        userCenterDTO.setStatus(user.getStatus());
        userCenterDTO.setCreateTime(user.getCreateTime());

        log.info("會員資料更新成功，Email：{}", email);

        return userCenterDTO;
    }

}
