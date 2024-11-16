package demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.model.Address;
import demo.model.House;


@Repository
public interface SearchRepository extends JpaRepository<House, Long>{
	
//	List<Address> findByCity(String city);
//	
	@Query("from House where address like %:n%")
	List<House> findByKeyWord(@Param("n") String name);

	@Query("from House where address like :n%")
	List<House> findByCityAndTownship(@Param("n") String name);
	
}
