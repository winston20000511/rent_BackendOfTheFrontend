package com.example.demo.service;

import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
