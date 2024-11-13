package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.model.FurnitureTableBean;

public interface FurnitureRepository extends JpaRepository<FurnitureTableBean, Long> {
	
}
