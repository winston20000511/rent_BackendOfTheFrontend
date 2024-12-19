package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {

	void save(HouseDetailsDTO house);

	List<HouseImageTableBean> findByHouseId(Long houseId);

	@Query(value = "select h.house_id as houseId, h.title as houseTitle from house_table h "
			+ "left join ads_table a on a.house_id = h.house_id "
			+ "where h.user_id = :userId and a.ad_id is null", nativeQuery = true)
	public List<Map<String, Object>> findNoAdHouses(Long userId);

	@Query("SELECT new com.example.demo.dto.HouseOwnerInfoDTO(u.name, u.picture, u.phone) " + "FROM HouseTableBean h "
			+ "JOIN h.user u " + "WHERE h.houseId = :houseId")
	HouseOwnerInfoDTO findHouseOwnerInfoByHouseId(@Param("houseId") Long houseId);

	@Query("SELECT new com.example.demo.dto.HouseListByUserIdDTO(h.houseId, h.user.userId) "
			+ "FROM HouseTableBean h WHERE h.user.userId = :userId")
	List<HouseListByUserIdDTO> findHousesByUserId(@Param("userId") Long userId);

	@Query(value = "select h.house_id as houseId, h.title as houseTitle " + "from house_table h "
			+ "left join ads_table a on a.house_id = h.house_id " + "where h.status = 1 and a.ad_id is null "
			+ "or (a.is_paid = 1 and a.paid_date is not null and " + "DATEADD(DAY, CASE WHEN a.adtype_id = 1 THEN 30 "
			+ "WHEN a.adtype_id = 2 THEN 60 "
			+ "END, CAST(a.paid_date AS DATE)) < CAST(GETDATE() AS DATE))", countQuery = "select count(*) from house_table h "
					+ "left join ads_table a on a.house_id = h.house_id " + "where h.status = 1 and a.ad_id is null "
					+ "or (a.is_paid = 1 and a.paid_date is not null and "
					+ "DATEADD(DAY, CASE WHEN a.adtype_id = 1 THEN 30 " + "WHEN a.adtype_id = 2 THEN 60 "
					+ "END, CAST(a.paid_date AS DATE)) < CAST(GETDATE() AS DATE))", nativeQuery = true)
	public Page<Map<String, Object>> findHousesWithoutAds(Pageable pageable);

}