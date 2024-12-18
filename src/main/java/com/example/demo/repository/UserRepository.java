package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.UserTableBean;

public interface UserRepository extends JpaRepository<UserTableBean, Long> {

	UserTableBean findByEmail(String email);

	@Query(value = "SELECT u.name, u.phone, u.picture " + "FROM user_table u "
			+ "JOIN house_table h ON u.user_id = h.user_id " + "WHERE h.house_id = :houseId", nativeQuery = true)
	Object[] getOwnerByHouseId(@Param("houseId") Long houseId);

}