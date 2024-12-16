package com.example.demo.model.linepay;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductForm {
	private String id;
	private String name;
//	private String imageUrl;
	private BigDecimal quantity;
	private BigDecimal price;
	
}
