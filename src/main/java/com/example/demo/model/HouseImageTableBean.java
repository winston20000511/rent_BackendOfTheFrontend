package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "image_table")
public class HouseImageTableBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private byte[] imageUrl;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false) // 房屋ID作為外鍵
    private HouseTableBean house;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 用戶ID作為外鍵
    private UserTableBean user;

	public HouseImageTableBean() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(byte[] imageUrl) {
		this.imageUrl = imageUrl;
	}

	public HouseTableBean getHouse() {
		return house;
	}

	public void setHouse(HouseTableBean house) {
		this.house = house;
	}

	public UserTableBean getUser() {
		return user;
	}

	public void setUser(UserTableBean user) {
		this.user = user;
	}

}
