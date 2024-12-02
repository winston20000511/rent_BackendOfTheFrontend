package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_table")
public class UserTableBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    private String phone;
    @Column(name = "picture")
    private byte[] picture;
    @Column(name = "createtime")
    private LocalDateTime createTime;
    @Column(name = "gender")
    private Byte gender;
    @Column(name = "coupon")
    private Byte coupon;
    @Column(name = "status")
    private Byte status;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<HouseTableBean> houses;
    
    @OneToMany(mappedBy = "rentUser", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BookingBean> bookings;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<HouseImageTableBean> images;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CollectTableBean> collect;
    

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
     private List<AdBean> ads;
     
	 @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	 private List<OrderBean> orders;
     
    
    public UserTableBean() {
    	
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public Byte getCoupon() {
		return coupon;
	}

	public void setCoupon(Byte coupon) {
		this.coupon = coupon;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public List<HouseTableBean> getHouses() {
		return houses;
	}

	public void setHouses(List<HouseTableBean> houses) {
		this.houses = houses;
	}

	public List<BookingBean> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingBean> bookings) {
		this.bookings = bookings;
	}

	public List<HouseImageTableBean> getImages() {
		return images;
	}

	public void setImages(List<HouseImageTableBean> images) {
		this.images = images;
	}

	public List<CollectTableBean> getCollect() {
		return collect;
	}

	public void setCollect(List<CollectTableBean> collect) {
		this.collect = collect;
	}

	public List<AdBean> getAds() {
		return ads;
	}

	public void setAds(List<AdBean> ads) {
		this.ads = ads;
	}


}
