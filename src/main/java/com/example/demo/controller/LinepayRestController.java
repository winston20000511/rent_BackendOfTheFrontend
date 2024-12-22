package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.LinepayService;

@RestController
@RequestMapping("/api/linepay")
public class LinepayRestController {

	private LinepayService linepayService;

	public LinepayRestController(LinepayService linePayService) {
		this.linepayService = linePayService;
	}

	@PostMapping("/request")
	public ResponseEntity<?> process(@RequestBody String orderId) {
		
		try {
			if (orderId == null || orderId.isEmpty()) {
				return ResponseEntity.badRequest().body("需有訂單號碼");
			}
			String paymentURL = linepayService.processPaymentRequest(orderId);

			return ResponseEntity.ok(Map.of("paymentUrl", paymentURL));
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing payment request: " + exception.getMessage());
		}
	}

	
}

