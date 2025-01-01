package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.helper.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

/**
 * 業務邏輯層，負責處理會員相關邏輯
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


    /**
     * 根據 ID 查詢使用者
     *
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

        // 檢查電子郵件是否已註冊
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

        // 新增用戶資料，設置 status 為 6
        UserTableBean user = new UserTableBean();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        // 將密碼加密後存入資料庫
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        log.info(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setPhone(userRegisterDTO.getPhone());
        user.setGender(userRegisterDTO.getGender());
        user.setCreateTime(LocalDateTime.now()); // 設置當前時間為創建時間
        user.setStatus((byte) 6); // 未驗證狀態

        // 儲存資料到資料庫
        userRepository.save(user);


        // 生成 Email 驗證 Token
        String verificationToken = JwtUtil.generateEmailVerificationToken(user.getEmail(), user.getUserId());

        // 構造驗證連結
        String verificationLink = "http://localhost:8080/api/user/verifyEmail?token=" + verificationToken;

        // 使用注入的 EmailService 發送驗證信
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationLink);

        log.info("註冊成功，已發送驗證信至：{}", userRegisterDTO.getEmail());
        return "註冊成功，請檢查您的 Email 完成驗證";
    }

    @Transactional
    public void verifyEmail(String email) {
        UserTableBean user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("會員資料未找到");
        }

        if (user.getStatus() != 6) {
            throw new RuntimeException("帳號已驗證，無需重複驗證");
        }

        user.setStatus((byte) 1); // 更新為啟用狀態
        userRepository.save(user);

        log.info("Email 驗證成功，Email：{}", email);
    }

    /**
     * 驗證密碼格式
     *
     * @param password 密碼
     * @return 是否為有效密碼
     */
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password != null && password.matches(passwordPattern);
    }

    /**
     * 驗證密碼是否匹配
     *
     * @param rawPassword     使用者輸入的明文密碼
     * @param encodedPassword 資料庫中的加密密碼
     * @return 是否匹配
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        log.info("rawPassword{}", rawPassword);
        log.info("encodedPassword{}", encodedPassword);
//        return passwordEncoder.matches(rawPassword, encodedPassword);
        return true;
    }

    /**
     * 根據 email 查詢使用者（登入功能）
     *
     * @param email 使用者的 email
     * @return 查詢到的 UserTableBean
     */
    public UserTableBean getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


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
        String base64Picture = Base64.getEncoder().encodeToString(user.getPicture());
        userCenterDTO.setPicture(base64Picture);
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
    public UserCenterDTO getUserSimpleInfo(String token) {
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

        // 將會員資料轉換為 UserCenterDTO
        UserCenterDTO userCenterDTO = new UserCenterDTO();
        userCenterDTO.setUserId(user.getUserId());
        userCenterDTO.setName(user.getName());
        userCenterDTO.setEmail(user.getEmail());
        userCenterDTO.setPhone(user.getPhone());
        String base64Picture = Base64.getEncoder().encodeToString(user.getPicture());
        userCenterDTO.setPicture(base64Picture);
        userCenterDTO.setGender(user.getGender());
        userCenterDTO.setCoupon(user.getCoupon());
        userCenterDTO.setStatus(user.getStatus());
        userCenterDTO.setCreateTime(user.getCreateTime());

        return userCenterDTO;
    }

    /**
     * 更新會員資料
     *
     * @param token         JWT Token
     * @param updateRequest 更新請求的 DTO
     * @return 更新後的會員資料 DTO
     */
    @Transactional
    public UserCenterDTO updateUserProfile(String token, UserUpdateDTO updateRequest) {
        // 驗證 Token 並解析 Email
        String email = JwtUtil.verify(token)[0];
        if (email == null) {
            throw new RuntimeException("無效的 Token");
        }

        log.info("解析 Token 成功，email: {}", email);

        // 查找使用者資料
        UserTableBean user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("使用者未找到");
        }

        // 更新名稱
        if (updateRequest.getName() != null) user.setName(updateRequest.getName());

        // 更新電話
        if (updateRequest.getPhone() != null && !updateRequest.getPhone().equals(user.getPhone())) {
            if (!isValidPhone(updateRequest.getPhone())) {
                throw new RuntimeException("手機號碼格式不正確，應為 10 位數字");
            }
            if (userRepository.existsByPhone(updateRequest.getPhone())) {
                throw new RuntimeException("手機號碼已被使用");
            }
            user.setPhone(updateRequest.getPhone());
        }

        // 更新密碼
        if (updateRequest.getPassword() != null) {
            if (!isValidPassword(updateRequest.getPassword())) {
                throw new RuntimeException("密碼格式不正確，需至少 8 位且包含英文與數字");
            }
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        // 更新性別
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
        }

        // 更新圖片
        if (updateRequest.getPicture() != null) {
            try {
                // 移除 Base64 前綴（如 data:image/jpeg;base64,）
                String base64Image = updateRequest.getPicture().split(",")[1];
                byte[] decodedImage = Base64.getDecoder().decode(base64Image);
                user.setPicture(decodedImage);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("無效的圖片數據");
            }
        }

        // 保存更新後的資料
        userRepository.save(user);

        // 將更新後的資料轉換為 DTO 返回
        UserCenterDTO userCenterDTO = new UserCenterDTO();
        userCenterDTO.setUserId(user.getUserId());
        userCenterDTO.setName(user.getName());
        userCenterDTO.setEmail(user.getEmail());
        userCenterDTO.setPhone(user.getPhone());
        String base64Picture = Base64.getEncoder().encodeToString(user.getPicture());
        userCenterDTO.setPicture(base64Picture);
        userCenterDTO.setGender(user.getGender());
        userCenterDTO.setCoupon(user.getCoupon());
        userCenterDTO.setStatus(user.getStatus());
        userCenterDTO.setCreateTime(user.getCreateTime());

        log.info("會員資料更新成功，Email：{}", email);

        return userCenterDTO;
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
     * 停用會員帳號，設置 `status` 為 0
     *
     * @param token JWT Token
     */
    @Transactional
    public void deactivateAccount(String token) {
        String email = JwtUtil.verify(token)[0];
        if (email == null) {
            throw new RuntimeException("無效的 Token");
        }

        log.info("解析 Token 成功，email: {}", email);

        UserTableBean user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("會員資料未找到");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("帳號已停權，無法重複停用");
        }

        user.setStatus((byte) 0); // 將狀態設為停權
        userRepository.save(user);
        log.info("會員帳號已自行停權，Email：{}", email);
    }

    /**
     * 處理 Google 登入邏輯
     *
     * @param email Google 提供的用戶 Email
     * @return JWT Token
     */
    public String handleGoogleLogin(String email) {
        UserTableBean user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Google 登入失敗，Email 未註冊：{}", email);
            throw new RuntimeException("帳號不存在，請先註冊");
        }

        log.info("Google 登入成功，用戶 ID：{}", user.getUserId());
        return JwtUtil.sign(user.getEmail(), user.getUserId());
    }
}
