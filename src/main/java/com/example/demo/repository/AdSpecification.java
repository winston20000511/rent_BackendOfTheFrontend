package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.model.AdBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.OrderBean;

import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AdSpecification {

    // 動態加入篩選條件
	public static Specification<AdBean> filter(Map<String, String> conditions) {
	    return (root, query, builder) -> {
	        // 確保結果集不會因為 JOIN 造成重複
	        query.distinct(true);  // 避免重複的 AdBean 物件

	        List<Predicate> predicates = new ArrayList<>();
	        
	        // 取得篩選條件
	        String dateRange = conditions.get("daterange");
	        String paymentStatus = conditions.get("paymentstatus");
	        String houseTitle = conditions.get("input");
	        
	        System.out.println("houseTitle: " + houseTitle);

	        // 如何避免N+1??

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

