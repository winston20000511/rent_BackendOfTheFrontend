package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.model.AdBean;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class AdSpecification {

	/**
	 * 動態加入篩選條件
	 * @param userId
	 * @param pageNumber
	 * @param dateRange
	 * @param paymentStatus
	 * @param houseTitle
	 * @return
	 */
	public static Specification<AdBean> filter(
			Long userId, Integer pageNumber, String dateRange, String paymentStatus, String houseTitle) {
	    return (root, query, builder) -> {
	    	
//	    	query.distinct(true);  // 避免重複的 AdBean 物件

	        List<Predicate> predicates = new ArrayList<>();
	        
	        predicates.add(builder.equal(root.get("userId"), userId));
	        
	        if (dateRange != null && !dateRange.equals("all")) {
	            LocalDateTime endDate = LocalDateTime.now();
	            LocalDateTime startDate;

	            // 判斷篩選的日期範圍
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

	            predicates.add(builder.between(root.get("paidDate"), startDate, endDate));
	        }

	        // 處理付款狀態
	        if (paymentStatus != null && !paymentStatus.equals("all")) {
	            Boolean isPaid = true;
	            switch(paymentStatus) {
	                case "paid": isPaid = true; break;
	                case "unpaid": isPaid = false; break;
	            }
	            predicates.add(builder.equal(root.get("isPaid"), isPaid));
	        }

	        if(houseTitle != null) {
	            // 使用 join 查詢 house 內的 title
	            Join<Object, Object> join = root.join("house", JoinType.INNER);
	            predicates.add(builder.like(join.get("title"), "%" + houseTitle + "%"));
	        }

	        // 返回所有條件的合併結果
	        return builder.and(predicates.toArray(new Predicate[0]));
	    };
	}


}

