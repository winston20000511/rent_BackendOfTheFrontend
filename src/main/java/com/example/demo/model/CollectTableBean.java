package com.example.demo.model;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "collect_table")
public class CollectTableBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collect_id")
	private Long collectId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id",nullable = false)
    private HouseTableBean house;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserTableBean user;
    
	@Column(name = "collect_time")
	private Time collectTime;



	public CollectTableBean() {
	}



	public Long getCollectId() {
		return collectId;
	}



	public void setCollectId(Long collectId) {
		this.collectId = collectId;
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



	public Time getCollectTime() {
		return collectTime;
	}



	public void setCollectTime(Time collectTime) {
		this.collectTime = collectTime;
	}

	
}
