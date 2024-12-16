package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ComplaintBean;
import com.example.demo.service.ComplaintService;
import com.example.demo.service.MessageService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/complaints")	
public class ComplaintController {

	@Autowired
	private ComplaintService service;
	@Autowired
	private MessageService MSGservice;
	
	@PostMapping("/submit")
	public ResponseEntity<?> submitComplaint(
			@RequestBody ComplaintBean complaint ,HttpSession httpSession ) {
		try {
			Long logingUserId = (Long) httpSession.getAttribute("loginUserId");
			String loginUsername = (String) httpSession.getAttribute("loginUsername");
			
			if (logingUserId == null || loginUsername == null) {
                return ResponseEntity.status(401).body("使用者未登入，無法提交申訴單。");
            }
			
			complaint.setUser_id(logingUserId);
            complaint.setUsername(loginUsername);
            
			ComplaintBean savedComplaint = service.saveComplaint(complaint);
			
			// we receive you complaint form
			String Message = "我們已經收到你的申訴單," +loginUsername + "!";
			MSGservice.sendSystemMSG(logingUserId, Message );

			return ResponseEntity.ok(savedComplaint);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error saving complaint: " + e.getMessage());
		}
	}
}