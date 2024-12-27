package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MessageDTO;
import com.example.demo.model.MessageBean;
import com.example.demo.repository.MessageRepository;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	// system auto send MSG start
	
	// welcome user register
	public void sendSystemMSG(Long receiverId , String messageContent) {
		MessageBean sysMSGUserRegister = new MessageBean();
		sysMSGUserRegister.setSenderId((long) 3);
		sysMSGUserRegister.setReceiverId(receiverId);
		sysMSGUserRegister.setMessage(messageContent);
		sysMSGUserRegister.setPicture(null);
		
		messageRepository.save(sysMSGUserRegister);
		System.out.println("sys send MSG to user : " + receiverId +" : " +messageContent);
	}
	
	// content
	//**********example***********
	/*
	@Autowired
	private MessageService MSGservice;
	// we receive you complaint form
	String Message = "我們已經收到你的申訴單," +loginUsername + "!";
	messageService.sendSystemMSG(userId, message);
	*/
	
//	String welocomeMessage = "歡迎你來到我們網站," +user.name() + "!";
//	messageService.sendSystemMSG(userId, message);
	

	// office gift coupon 	
//	String giftMessage = "你獲得了一張優惠券, " + user.getName() + "!";
//	messageService.sendSystemMSG(userId, message);
	
	
	// you has a new booking	
//	String bookingMessage = "你有一個新的預約申請, " + user.getName() + "!";
//	messageService.sendSystemMSG(userId, message);
	
	
	// you submission has been approved	
//	String approvalMessage = "你的申請已被核准, " + user.getName() + "!";
//	messageService.sendSystemMSG(userId, message);
	
	
	// we receive you complaint form
//    String message = "我們已經收到你的申訴單, " + username + "!";
//    messageService.sendSystemMSG(userId, message);
	
	
	
	// system auto send MSG end
	
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    //save
    @Transactional
    public MessageBean saveMessage(MessageBean message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        if (message.getMessage() == null && message.getPicture() == null) {
            throw new IllegalArgumentException("Message content and picture cannot both be null");
        }

        return messageRepository.save(message);
    }

	// find between user currentUserId And targeUserId
	//insert 4 param ==> 2param
  //save
	public List<MessageDTO> getMessagesBetweenUsers(Long currentUserId, Long targetUserId) {
	    List<MessageBean> messages = messageRepository.findMessagesBetweenUsers(currentUserId, targetUserId);
	    return messages.stream()
	        .map(msg -> new MessageDTO(
	            msg.getSender().getName(),
	            msg.getReceiver().getName(),
	            msg.getMessage(),
	            msg.getTimestamp()
	        ))
	        .collect(Collectors.toList());
	}

	//save??
    // query MSG from last time check
    public List<MessageDTO> getNewMessagesBetweenUsers(
    		Long currentUserId, Long targetUserId, LocalDateTime lastChecked) {
        List<MessageBean> messages = messageRepository.findMessagesBetweenUsersAfter
        		(currentUserId, targetUserId, lastChecked);
        return messages.stream()
            .map(msg -> new MessageDTO(
                msg.getSender().getName(),
                msg.getReceiver().getName(),
                msg.getMessage(),
                msg.getTimestamp()
            ))
            .collect(Collectors.toList());
    }
    
    
	// only login user find (send And receiver)
	// data no name (!! only one parpm)
	public List<MessageBean> findMessagesByUserId() {
		Long userId = (long) 51;
		return messageRepository.findMessagesByUserId(userId);
	}
	
	
//	//get user name param 
//	public List<MessageWithUserDTO> getMessagesIdAndName(Long currentUserId, Long targetUserId) {
//		List<MessageWithUserDTO> messages = messageRepository.findMessagesWithUserDetails(currentUserId, targetUserId);
//		return messages;
//	}
	
	
	//save check
	//get currentuser List param 
	public List<Object[]> findChatUsers(Long currentUserId) {
//		Long currentUserId = (long) 51;
		System.out.println("currentUserId" + currentUserId);
		List<Object[]> messages = messageRepository.findChatUserDetails(currentUserId);
		return messages;
	}
	

}
