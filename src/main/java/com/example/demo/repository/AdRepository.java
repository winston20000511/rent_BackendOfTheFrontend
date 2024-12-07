package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.AdDetailsResponseDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.OrderBean;

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
	
}
