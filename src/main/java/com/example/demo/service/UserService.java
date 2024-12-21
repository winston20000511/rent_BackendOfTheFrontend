package com.example.demo.service;

import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.dto.UserCenterDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public HouseOwnerInfoDTO getOwnerInfo(Long houseId) {
        // 從 UserRepository 查詢屋主資料
        Object[] ownerData = userRepository.getOwnerByHouseId(houseId);

        if (ownerData == null || ownerData.length == 0) {
            System.out.println("No owner data found for houseId: " + houseId);
            return null;
        }

        // 轉換查詢結果
        String name = (ownerData[0] != null) ? ownerData[0].toString() : "";
        String phone = (ownerData[1] != null) ? ownerData[1].toString() : "";
        byte[] picture = (ownerData[2] != null) ? (byte[]) ownerData[2] : null;

        // 將圖片轉換成 Base64 格式
        String base64Picture = null;
        if (picture != null) {
            base64Picture = Base64.getEncoder().encodeToString(picture);
        }

        return new HouseOwnerInfoDTO(name, phone, base64Picture);
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
        userCenterDTO.setPicture(user.getPicture());
        userCenterDTO.setGender(user.getGender());
        userCenterDTO.setCoupon(user.getCoupon());
        userCenterDTO.setStatus(user.getStatus());
        userCenterDTO.setCreateTime(user.getCreateTime());

        return userCenterDTO;
    }
}