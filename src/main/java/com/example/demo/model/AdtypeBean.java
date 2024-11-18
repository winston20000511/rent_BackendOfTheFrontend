package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "adtype_table")
public class AdtypeBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer adTypeId;
	
	@Column(name = "adname")
	private String adName;
	
	@Column(name = "adprice")
	private Integer adPrice;
	
	@OneToMany(mappedBy = "adtype", fetch = FetchType.LAZY)
	private List<AdBean> ads;

	public AdtypeBean() {
	}

	public Integer getAdTypeId() {
		return adTypeId;
	}

	public void setAdTypeId(Integer adTypeId) {
		this.adTypeId = adTypeId;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public Integer getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Integer adPrice) {
		this.adPrice = adPrice;
	}
	
}
