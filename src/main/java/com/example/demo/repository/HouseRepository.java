package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.model.HouseTableBean;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {

	void save(HouseDetailsDTO house);
		
		@Query(value="select h.house_id as houseId, h.title as houseTitle from house_table h "
				+ "left join ads_table a on a.house_id = h.house_id "
				+ "where h.user_id = :userId and a.ad_id is null",
				nativeQuery = true)
		public List<Map<String,Object>> findNoAdHouses(Long userId);
		
		@Query("SELECT new com.example.demo.dto.HouseListByUserIdDTO(h.houseId, h.user.userId) " +
			       "FROM HouseTableBean h WHERE h.user.userId = :userId")
			List<HouseListByUserIdDTO> findHousesByUserId(@Param("userId") Long userId);
}