package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.dto.OrderSearchRequestDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.OrderBean;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class OrderSpecification {
	
	public static Specification<OrderBean> filter(OrderSearchRequestDTO requestDTO){
		return (root, query, builder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if (requestDTO.getUserId() != null) {
                predicates.add(builder.equal(root.get("userId"), requestDTO.getUserId()));
            }
			
			if(requestDTO.getStartDate() != null && requestDTO.getEndDate() != null) {
				predicates.add(builder.between(root.get("orderDate"), requestDTO.getStartDate(), requestDTO.getEndDate()));
			}
			
			if (requestDTO.getOrderStatus() != null) {
                predicates.add(builder.equal(root.get("orderStatus"), requestDTO.getOrderStatus()));
            }
			
			if(requestDTO.getHouseTitle() != null) {
				Join<OrderBean, AdBean> adJoin = root.join("ads", JoinType.INNER);
				Join<AdBean, HouseTableBean> houseJoin = adJoin.join("house", JoinType.INNER);
				predicates.add(builder.like(houseJoin.get("title"), "%" + requestDTO.getHouseTitle() + "%"));
			}
			
			if(requestDTO.getMerchantTradNo() != null) {
				Join<OrderBean, AdBean> adJoin = root.join("ads", JoinType.INNER);
				predicates.add(builder.like(adJoin.get("orderId"), "%" + requestDTO.getMerchantTradNo() + "%"));
			}
			
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
