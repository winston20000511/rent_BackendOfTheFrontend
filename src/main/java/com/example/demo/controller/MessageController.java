package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MessageDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.helper.UnTokenException;
import com.example.demo.model.MessageBean;
import com.example.demo.service.MessageService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageBean message, HttpServletRequest request) {
        try {
        	System.out.println("接收到的訊息：" + message);
            MessageBean savedMessage = messageService.saveMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while sending message: " + e.getMessage());
        }
    }

    
	// find currentUserId = login user targetUserId = wanna find userId
    //save
    //font need modify
    
	@GetMapping("/{currentUserId}/{targetUserId}")
	public ResponseEntity<List<MessageDTO>> getMessages(
			@PathVariable Long currentUserId,
			@PathVariable Long targetUserId,
			@RequestParam(required = false) String lastChecked) {

		List<MessageDTO> messages;
        if (lastChecked != null) {
            LocalDateTime lastCheckedTimestamp = LocalDateTime.parse(lastChecked);
            messages = messageService.getNewMessagesBetweenUsers(currentUserId, targetUserId, lastCheckedTimestamp);
        } else {
            messages = messageService.getMessagesBetweenUsers(currentUserId, targetUserId);
        }

        return ResponseEntity.ok(messages);
    }

	// get data have name
	//need modify
	//save
//	@SuppressWarnings("unused")
	@GetMapping("/list")
    public  ResponseEntity<?>  getChatUsers(HttpServletRequest request) {
//        Long currentUserId = 51L;
		// Long currentUserId = (Long) session.getAttribute("loginUserId");
		
		try {
	        String token = request.getHeader("authorization");
	        if (token == null || token.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token 未提供");
	        }

	        String[] jwtResult = JwtUtil.verify(token);
	        
	        System.out.println("JWT 解析結果: ");
	        if (jwtResult != null) {
	            for (int i = 0; i < jwtResult.length; i++) {
	                System.out.println("jwtResult[" + i + "]: " + jwtResult[i]);
	            }
	        } else {
	            System.out.println("JWT 解析失敗，返回值為 null");
	        }
	        
	        if (jwtResult ==null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token 無效");
	        }
	        Long currentUserId;
	        try {
	            currentUserId = Long.parseLong(jwtResult[1]); 
	        } catch (NumberFormatException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token 中的用戶 ID 無效");
	        }

	        List<Object[]> chatUsers = messageService.findChatUsers(currentUserId);
	        return ResponseEntity.ok(chatUsers);

	    } catch (UnTokenException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token 驗證失敗");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("伺服器內部錯誤：" + e.getMessage());
	    }
    }
	// find all
	@GetMapping("/test")
	public ResponseEntity<List<MessageBean>> getUserData() {
		List<MessageBean> chatHistory = messageService.findMessagesByUserId();

		return ResponseEntity.ok(chatHistory);
	}

}