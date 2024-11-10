package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.House;


@Repository
public interface SearchRepository extends JpaRepository<House, Long>{

}
