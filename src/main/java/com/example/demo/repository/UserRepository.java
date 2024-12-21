package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.UserTableBean;

import java.util.Optional;

/**
 * 資料訪問層，用於操作 user_table 表
 */
public interface UserRepository extends JpaRepository<UserTableBean, Long> {
    
    @Query("SELECT u.userId FROM UserTableBean u WHERE u.email = :email")
    Optional<Long> findUserIdByEmail(String email);
    
    // 根據 email 查詢使用者，登入功能需要用到
    UserTableBean findByEmail(String email);

    // 根據房屋ID查詢房東資訊（名稱、電話、照片）
    @Query(value = "SELECT u.name, u.phone, u.picture " +
                   "FROM user_table u " +
                   "JOIN house_table h ON u.user_id = h.user_id " +
                   "WHERE h.house_id = :houseId", nativeQuery = true)
    Object[] getOwnerByHouseId(@Param("houseId") Long houseId);

    boolean existsByName(String name);   // 檢查名稱是否已存在
    boolean existsByEmail(String email); // 檢查電子郵件是否已存在
    boolean existsByPhone(String phone); // 檢查手機號碼是否已存在

    /**
     * 根據 userId 查找會員資料
     *
     * @param userId 使用者 ID
     * @return 會員資料
     */
    Optional<UserTableBean> findById(Long userId);
    
    @Query("SELECT u.coupon FROM UserTableBean u WHERE u.userId =:userId")
	public Byte getCouponNumber(Long userId);
    
    @Modifying
    @Query("UPDATE UserTableBean u SET u.coupon = u.coupon - 1 WHERE u.userId = :userId AND u.coupon > 0")
    public int removeOneCoupon(Long userId);
    
}
