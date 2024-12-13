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
import com.example.demo.dto.HouseOwnerDetailDTO;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {

	void save(HouseDetailsDTO house);

	List<HouseImageTableBean> findByHouseId(Long houseId);

	@Query(value = "select h.house_id as houseId, h.title as houseTitle from house_table h "
			+ "left join ads_table a on a.house_id = h.house_id "
			+ "where h.user_id = :userId and a.ad_id is null", nativeQuery = true)
	public List<Map<String, Object>> findNoAdHouses(Long userId);

	@Query(value = "SELECT h.house_id, u.email FROM  house_table h JOIN user_table u ON h.user_id = u.user_id WHERE h.user_id = :houseId", nativeQuery = true)
	HouseOwnerDetailDTO getOwnerDetailByHouseId(Long houseId);
	
	@Query("SELECT new com.example.demo.dto.HouseListByUserIdDTO(h.houseId, h.user.userId) " +
		       "FROM HouseTableBean h WHERE h.user.userId = :userId")
		List<HouseListByUserIdDTO> findHousesByUserId(@Param("userId") Long userId);
	@Query(value = "select h.house_id as houseId, h.title as houseTitle from house_table h "
			+ "left join ads_table a on a.house_id = h.house_id "
			+ "where a.ad_id is null", countQuery = "select count(*) " + "from house_table h "
					+ "left join ads_table a on a.house_id = h.house_id where a.ad_id is null", nativeQuery = true)
	public Page<Map<String, Object>> findHousesWithoutAds(Pageable pageable);


	
}