package demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.Address;


@Repository
public interface SearchRepository extends JpaRepository<Address, Long>{
	
	List<Address> findByCity(String city);
}
