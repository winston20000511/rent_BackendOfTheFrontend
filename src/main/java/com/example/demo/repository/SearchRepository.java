package com.example.demo.repository;

import java.util.HashSet;
import java.util.List;

import com.example.demo.dto.KeyWordDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.HouseTableBean;


@Repository
public interface SearchRepository extends JpaRepository<HouseTableBean, Long>{
	
	@Query("select new com.example.demo.dto.AddressDTO(h.houseId ,h.address , h.lat , h.lng , h.price , " +
			"COALESCE(a.adtype.adName,'0天') , COALESCE(a.paidDate,'1999-01-01 00:00:00'), " +
			"c.pet , c.parkingSpace , c.elevator , c.balcony, c.shortTerm , c.cooking , c.waterDispenser, " +
			"c.managementFee , c.genderRestrictions , h.houseType) " +
			"from HouseTableBean h " + "left join h.ads a " + "left join a.adtype t " + "Left join h.condition c " +
			"where h.address like CONCAT('%',:n,'%')")
	List<AddressDTO> findByKeyWord(@Param("n") String name);

//	@Query("select new com.example.demo.dto.KeyWordDTO(c.pet , c.parkingSpace , c.elevator , c.balcony, c.shortTerm , c.cooking , c.waterDispenser, c.managementFee , c.genderRestrictions , h.houseType)" + "from HouseTableBean h " + "Left join h.condition c " + "where h.houseId = :id")
//	KeyWordDTO findConditionById(@Param("id") long houseid);

	@Query("select new com.example.demo.dto.AddressDTO(h.houseId ,h.address , h.lat , h.lng , h.price , " +
			"COALESCE(a.adtype.adName,'0天') , COALESCE(a.paidDate,'1999-01-01 00:00:00'), " +
			"c.pet , c.parkingSpace , c.elevator , c.balcony, c.shortTerm , c.cooking , c.waterDispenser, " +
			"c.managementFee , c.genderRestrictions , h.houseType) " +
			"from HouseTableBean h " + "left join h.ads a " + "left join a.adtype t " + "Left join h.condition c " +
			"where h.address like CONCAT(:n,'%')")
	HashSet<AddressDTO> findByCityAndTownship(@Param("n") String name);


}
