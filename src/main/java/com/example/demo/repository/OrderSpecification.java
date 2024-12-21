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
	
	public static Specification<OrderBean> filter(
			Long userId, Integer page, String orderStatus, String dateRange, String inputCondition, String userInput){
		return (root, query, builder) ->{
			
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(builder.equal(root.get("userId"), userId));
			
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
			
	        if (orderStatus != null && !orderStatus.equals("all")) {
	        	Short status = (short) 0;
	            switch(orderStatus) {
	            	case "cancelling": status = 2; break;
	                case "active": status = 1; break;
	                case "cancelled": status = 0; break;
	            }
	            predicates.add(builder.equal(root.get("orderStatus"), status));
	        }
			
	        
	        if(inputCondition != null && !inputCondition.equals("none") && userInput != null) {
	        	
	        	System.out.println("user input: " + userInput);
	        	Join<OrderBean, AdBean> adJoin = root.join("ads", JoinType.INNER);
	            switch(inputCondition) {
	                case "orderid": 
	                    predicates.add(builder.like(adJoin.get("orderId"), "%" + userInput + "%"));
	                    System.out.println("order id predicates: " + predicates);
	                    break;
	                case "housetitle": 
	                    Join<AdBean, HouseTableBean> houseJoin = adJoin.join("house", JoinType.INNER);
	                    predicates.add(builder.like(houseJoin.get("title"), "%" + userInput + "%"));
	                    System.out.println("house title predicates: " + userInput);
	                    break;
	            }
	        }
			
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}