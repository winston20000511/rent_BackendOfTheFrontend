package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adtype_table")
@Getter
@Setter
public class AdtypeBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer adtypeId;
	
	@Column(name = "adname")
	private String adName;
	
	@Column(name = "adprice")
	private Integer adPrice;
	
	@OneToMany(mappedBy = "adtype", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<AdBean> ads;
	
	@OneToMany(mappedBy = "adtype", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<CartItemBean> cartItems;

	public AdtypeBean() {
	}

	
}
