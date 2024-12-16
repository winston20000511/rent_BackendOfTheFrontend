package com.example.demo.model.linepay;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RedirectUrls {
	private String confirmUrl;
	private String cancelUrl;
}
