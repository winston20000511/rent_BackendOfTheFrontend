package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.helper.JwtUtil;
import com.example.demo.model.ComplaintBean;
import com.example.demo.service.ComplaintService;
import com.example.demo.service.MessageService;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitComplaint(
            @RequestBody ComplaintBean complaint,
            @RequestHeader("Authorization") String authorization) {
        try {

            String token = authorization != null ? authorization.replace("Bearer ", "") : null;
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未提供 JWT，無法驗證用戶身份。");
            }

            String[] decodedToken = JwtUtil.verify(token);
            if (decodedToken == null || decodedToken.length < 2) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 無效或格式不正確。");
            }

            Long userId = parseUserId(decodedToken[1]);
            String username = decodedToken[2];

            if (userId == null || username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用戶未登入，無法提交申訴單。");
            }
            
            //need login user detail
            complaint.setUser_id(userId);
            complaint.setUsername(username);

            ComplaintBean savedComplaint = complaintService.saveComplaint(complaint);

            String message = "我們已經收到你的申訴單, " + username + "!";
            messageService.sendSystemMSG(userId, message);

            return ResponseEntity.ok(savedComplaint);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("儲存申訴單時發生錯誤: " + e.getMessage());
        }
    }

    private Long parseUserId(String userIdStr) {
        try {
            return Long.valueOf(userIdStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
