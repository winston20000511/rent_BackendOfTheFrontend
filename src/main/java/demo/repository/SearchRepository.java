package demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.model.Address;


@Repository
public interface SearchRepository extends JpaRepository<Address, Long>{
	
	List<Address> findByCity(String city);
	
	@Query("from Address where concat(city ,concat(township,street)) like %:n%")
	List<Address> findByKeyWord(@Param("n") String name);
}
