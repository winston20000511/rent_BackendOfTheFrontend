package com.example.demo.model.linepay;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutPaymentRequestForm {

	private BigDecimal amount;
	private String currency;
	private String orderId;
	private List<ProductPackageForm> packages;
	private RedirectUrls redirectUrls;
	
}
