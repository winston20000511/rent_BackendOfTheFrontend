package com.example.demo.repository;

<<<<<<< HEAD
import java.util.List;
<<<<<<< HEAD
import java.util.Map;
=======
>>>>>>> chen

=======
>>>>>>> parent of cba30fb (Merge branch 'seiya' into devlop)
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {

	void save(HouseDetailsDTO house);
	
<<<<<<< HEAD
<<<<<<< HEAD
	@Query(value="select h.house_id as houseId, h.title as houseTitle from house_table h "
			+ "left join ads_table a on a.house_id = h.house_id "
			+ "where h.user_id = :userId and a.ad_id is null",
			nativeQuery = true)
	public List<Map<String,Object>> findNoAdHouses(Long userId);
=======
	 List<HouseImageTableBean> findByHouseId(Long houseId);
>>>>>>> chen
=======
>>>>>>> parent of cba30fb (Merge branch 'seiya' into devlop)
}