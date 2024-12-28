package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.dto.HouseOwnerDetailDTO;
import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;

import jakarta.transaction.Transactional;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {

	void save(HouseDetailsDTO house);

	List<HouseImageTableBean> findByHouseId(Long houseId);

	@Query(value = "select h.house_id as houseId, h.title as houseTitle from house_table h "
			+ "left join ads_table a on a.house_id = h.house_id "
			+ "where h.user_id = :userId and a.ad_id is null", nativeQuery = true)
	public List<Map<String, Object>> findNoAdHouses(Long userId);
	
	@Query("SELECT h.user FROM HouseTableBean h WHERE h.houseId = :houseId")
    UserTableBean findOwnerByHouseId(Long houseId);


	@Query("SELECT new com.example.demo.dto.HouseListByUserIdDTO(h.houseId, h.user.userId) "
		       + "FROM HouseTableBean h WHERE h.user.userId = :userId AND h.status <= 1")
		List<HouseListByUserIdDTO> findHousesByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT h.house_id, u.email FROM  house_table h JOIN user_table u ON h.user_id = u.user_id WHERE h.user_id = :houseId", nativeQuery = true)
	HouseOwnerDetailDTO getOwnerDetailByHouseId(Long houseId);

    @Modifying
    @Transactional
    @Query("UPDATE HouseTableBean h SET h.clickCount = h.clickCount + 1 WHERE h.houseId = :houseId")
    int incrementClickCount(@Param("houseId") Long houseId);
	
    @Query("SELECT h.clickCount FROM HouseTableBean h WHERE h.houseId = :houseId")
    Integer findClickCountByHouseId(@Param("houseId") Long houseId);
	/**
	 * 篩選使用者當前擁有的物件中，無申請或無使用推播服務者
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT h.house_id AS houseId, h.title AS houseTitle " +
            "FROM house_table h " +
            "LEFT JOIN ads_table a ON a.house_id = h.house_id " +
            "WHERE h.status = 1 AND " +
            "(a.ad_id IS NULL OR " +
            "(a.is_paid = 1 AND a.paid_date IS NOT NULL AND " +
            "DATEADD(DAY, CASE WHEN a.adtype_id = 1 THEN 30 " +
            "WHEN a.adtype_id = 2 THEN 60 END, CAST(a.paid_date AS DATE)) < CAST(GETDATE() AS DATE))) " +
            "AND (h.user_id = :userId)",
    countQuery = "SELECT COUNT(*) " +
                 "FROM house_table h " +
                 "LEFT JOIN ads_table a ON a.house_id = h.house_id " +
                 "WHERE h.status = 1 AND " +
                 "(a.ad_id IS NULL OR " +
                 "(a.is_paid = 1 AND a.paid_date IS NOT NULL AND " +
                 "DATEADD(DAY, CASE WHEN a.adtype_id = 1 THEN 30 " +
                 "WHEN a.adtype_id = 2 THEN 60 END, CAST(a.paid_date AS DATE)) < CAST(GETDATE() AS DATE))) " +
                 "AND (h.user_id = :userId)",
    nativeQuery = true)
	public Page<Map<String, Object>> findHousesWithoutAds(Long userId, Pageable pageable);


	
}