package com.example.demo.model.linepay;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductPackageForm {
	private String id;
	private String name;
	private BigDecimal amount;
	private List<ProductForm> products;
}
