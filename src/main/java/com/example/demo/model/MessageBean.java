package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor 
@Setter
@Getter
@Entity
@Table(name="Messages")
public class MessageBean {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;               
	
	@Column(name = "sender_id", nullable = false)
    private Long senderId;    
	
	@Column(name = "receiver_id", nullable = false)
    private Long receiverId;       
	
	@Column(name = "message", columnDefinition = "NVARCHAR(MAX)")
    private String message;        
	
	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp = LocalDateTime.now();
	
	//pic
	@Column(name = "picture", columnDefinition = "NVARCHAR(MAX)")
    private String picture ="";        

	//user
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private UserTableBean sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private UserTableBean receiver;
	
    public String getSafePicture() {
        return picture != null ? picture : "";
    }
}
