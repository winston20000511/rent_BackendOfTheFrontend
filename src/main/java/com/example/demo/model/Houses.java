package com.example.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "house_table")
public class Houses {
	@Id
	@Column(name = "house_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer houseId;
	
	@Column(name = "user_id" , insertable = false, updatable = false)
	private Integer	userId;
	private String	title;
	private Integer	price;
	private String	description;
	private Integer	size;
	private String	city;
	private String	township;
	private String	street;
	private Byte	room;
	private Byte	bathroom;
	private Byte	livingroom;
	private Byte	kitchen;
	private Byte	housetype;
	private Byte	floor;
	private Boolean	atticAddition;
	private Byte status;
	
}
