package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.model.MessageBean;
import com.example.demo.service.MessageService;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageBean message) {
        try {
            MessageBean savedMessage = messageService.saveMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while sending message: " + e.getMessage());
        }
    }

    
	// find currentUserId = login user targetUserId = wanna find userId
    //save
	@GetMapping("/{currentUserId}/{targetUserId}")
	public ResponseEntity<List<MessageDTO>> getMessages(
			@PathVariable Long currentUserId,@PathVariable Long targetUserId,@RequestParam(required = false) String lastChecked) {

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
	@SuppressWarnings("unused")
	@GetMapping("/list")
    public ResponseEntity<List<Object[]>> getChatUsers(HttpSession session) {
        Long currentUserId = 51L;

        // Long currentUserId = (Long) session.getAttribute("loginUserId");

        if (currentUserId == null) {
            return ResponseEntity.status(401).body(null);    }

        List<Object[]> chatUsers = messageService.findChatUsers(currentUserId);
        return ResponseEntity.ok(chatUsers);
    }
	// find all
	@GetMapping("/test")
	public ResponseEntity<List<MessageBean>> getUserData() {
		List<MessageBean> chatHistory = messageService.findMessagesByUserId();

		return ResponseEntity.ok(chatHistory);
	}

}
//
//	  // WebSocket: 處理聊天消息
//    @MessageMapping("/chat")
//    @SendToUser("/queue/messages")
//    public MessageDTO handleChatMessage(MessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
//        // 開發階段：使用硬編碼
//        String username = "測試用戶";
//        Long userId = 51L;
//
//        // 生產環境：從 Session 中提取
//        // String username = (String) headerAccessor.getSessionAttributes().get("loginUsername");
//        // Long userId = (Long) headerAccessor.getSessionAttributes().get("loginUserId");
//
//        if (username == null || userId == null) {
//            throw new IllegalStateException("未授權的用戶");
//        }
//
//        // 打印消息和用戶信息
//        System.out.println("收到來自用戶 " + username + "（ID：" + userId + "）的消息: " + message.getMessage());
//
//        // 回傳消息給當前用戶
//        return new MessageDTO(userId, username, message.getMessage());
//    }
//    
//    





//@GetMapping("/list")
//public ResponseEntity<List<UserDTO>> getChatUsers(HttpSession session) {
//
//    Long currentUserId = (Long) session.getAttribute("loginUserId");
//
//    if (currentUserId == null) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//    }
//
//    List<UserDTO> chatUsers = messageService.findChatUsers(currentUserId);
//    return ResponseEntity.ok(chatUsers);
//}
