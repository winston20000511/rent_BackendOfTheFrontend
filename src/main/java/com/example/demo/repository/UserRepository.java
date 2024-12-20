package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.UserTableBean;

public interface UserRepository extends JpaRepository<UserTableBean, Long> {

    // 根據 email 查詢使用者，登入功能需要用到
    UserTableBean findByEmail(String email);

    // 根據房屋ID查詢房東資訊（名稱、電話、照片）
    @Query(value = "SELECT u.name, u.phone, u.picture " +
                   "FROM user_table u " +
                   "JOIN house_table h ON u.user_id = h.user_id " +
                   "WHERE h.house_id = :houseId", nativeQuery = true)
    Object[] getOwnerByHouseId(@Param("houseId") Long houseId);
    
    @Query("SELECT u.coupon FROM UserTableBean u WHERE u.userId =:userId")
	public Byte getCouponNumber(Long userId);
}
