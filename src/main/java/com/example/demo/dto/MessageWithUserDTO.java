package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;

//use repository

public interface MessageWithUserDTO {
	
    Long getMessageId();
    String getMessage();
    
    LocalDateTime getTimestamp();
    
    Long getSenderId();
    String getSenderName();
    
    Long getReceiverId();
    String getReceiverName();
    
    String getPicture(); 

}
