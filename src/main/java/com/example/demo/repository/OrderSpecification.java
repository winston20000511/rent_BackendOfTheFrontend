package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.dto.OrderSearchRequestDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.OrderBean;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class OrderSpecification {
	
	public static Specification<OrderBean> filter(Map<String, String> conditions){
		return (root, query, builder) ->{
			query.distinct(true);
			
			List<Predicate> predicates = new ArrayList<>();
			
			String dateRange = conditions.get("daterange");
	        String status = conditions.get("status");
	        String inputCondition = conditions.get("inputcondition");
	        String userInput = conditions.get("input");
			
	        if (dateRange != null && !dateRange.equals("all")) {
	            LocalDateTime endDate = LocalDateTime.now();
	            LocalDateTime startDate;

	            switch (dateRange) {
	                case "week":
	                    startDate = endDate.minusWeeks(1);
	                    break;
	                case "month":
	                    startDate = endDate.minusMonths(1);
	                    break;
	                case "year":
	                    startDate = endDate.minusYears(1);
	                    break;
	                default:
	                    // 若是自訂範圍，分割日期並轉換成 LocalDateTime
	                    String[] dates = dateRange.split("~");
	                    startDate = LocalDate.parse(dates[0]).atStartOfDay();
	                    endDate = LocalDate.parse(dates[1]).atTime(23, 59, 59); // 設定時間為一天結束
	                    break;
	            }

	            predicates.add(builder.between(root.get("merchantTradDate"), startDate, endDate));
	        }
			
	        if (status != null && !status.equals("all")) {
	        	Short orderStatus = (short) 0;
	            switch(status) {
	            	case "cancelling": orderStatus = 2; break;
	                case "active": orderStatus = 1; break;
	                case "cancelled": orderStatus = 0; break;
	            }
	            predicates.add(builder.equal(root.get("orderStatus"), orderStatus));
	        }
			
	        
	        if(inputCondition != null && !inputCondition.equals("none")) {
	        	
	        	if(userInput != null) {
	        		Join<OrderBean, AdBean> adJoin = root.join("ads", JoinType.INNER);
	        		switch(inputCondition) {
		        	case "orderid": 
						predicates.add(builder.like(adJoin.get("orderId"), "%" + userInput + "%"));
						break;
		        	case "housetitle": 
						Join<AdBean, HouseTableBean> houseJoin = adJoin.join("house", JoinType.INNER);
						predicates.add(builder.like(houseJoin.get("title"), "%" + userInput + "%"));
						break;
		        	}
	        	}
	        }
			
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
