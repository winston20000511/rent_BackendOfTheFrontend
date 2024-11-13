package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.model.ConditionTableBean;

public interface ConditionRepository extends JpaRepository<ConditionTableBean, Long> {
	
}