package com.example.demo.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.LinepayService;

@RestController
@RequestMapping("/api/linepay")
public class LinepayRestController {
	
	private Logger logger = Logger.getLogger(LinepayRestController.class.getName());
	private LinepayService linepayService;

	public LinepayRestController(LinepayService linePayService) {
		this.linepayService = linePayService;
	}

	@PostMapping("/request")
	public ResponseEntity<?> process(@RequestBody String orderId) {
		
		ResponseEntity<?> response = linepayService.processPaymentRequest(orderId);
		logger.info("process的response: " + response);
		return response;
			
	}
	
}

