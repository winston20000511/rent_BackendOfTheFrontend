package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="condition_table")
public class ConditionTableBean {

	@Id
	private Long houseId;
	@Column(name = "pet")
	private Boolean pet;
	@Column(name = "parkingSpace")
	private Boolean parkingSpace;
	@Column(name = "elevator")
	private Boolean elevator;
	@Column(name = "balcony")
	private Boolean balcony;
	@Column(name = "shortTerm")
	private Boolean shortTerm;
	@Column(name = "cooking")
	private Boolean cooking;
	@Column(name = "waterDispenser")
	private Boolean waterDispenser;
	@Column(name ="fee")
	private Boolean managementFee;
	@Column(name = "genderRestrictions")
	private Byte genderRestrictions;
	
    @OneToOne
    @MapsId
    @JoinColumn(name = "house_id",nullable = false)
    private HouseTableBean house;


}
