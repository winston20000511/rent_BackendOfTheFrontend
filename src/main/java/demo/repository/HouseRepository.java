package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.model.HouseTableBean;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {
	
}