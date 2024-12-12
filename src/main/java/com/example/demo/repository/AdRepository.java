package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.AdBean;

public interface AdRepository extends JpaRepository<AdBean, Long>, JpaSpecificationExecutor<AdBean>{
	
//	@Query(value="select new com.example.demo.dto.AdDetailsResponseDTO"
//			+ "(a.adId, u.userId, u.name, h.title, adt.adName, a.adPrice, a.isPaid, a.orderId, a.paidDate) "
//			+ "from AdBean a "
//			+ "join AdtypeBean adt on a.adtypeId = adt.adtypeId "
//			+ "join UserTableBean u on a.userId = u.userId "
//			+ "join HouseTableBean h on a.houseId = h.houseId "
//			+ "where a.adId = :adId")
//	public AdDetailsResponseDTO findAdDetailsByAdId(Long adId);
//	
//	@Query(value="select new com.example.demo.dto.AdDetailsResponseDTO"
//			+ "(a.adId, u.userId, u.name, h.title, adt.adName, a.adPrice, a.isPaid, a.orderId, a.paidDate) "
//			+ "from AdBean a "
//			+ "join AdtypeBean adt on a.adtypeId = adt.adtypeId "
//			+ "join UserTableBean u on a.userId = u.userId "
//			+ "join HouseTableBean h on a.houseId = h.houseId "
//			+ "where a.orderId = :orderId")
//	public List<AdDetailsResponseDTO> findAdDetailsByOrderId(String orderId);
	
	@Query("From AdBean where adId = :adId and userId = :userId")
	public AdBean findAdByAdIdAndUserId(Long adId, Long userId);
	
}
