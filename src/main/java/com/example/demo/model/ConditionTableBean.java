package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="condition_table")
public class ConditionTableBean {

<<<<<<< Updated upstream
	@Id
	private Long houseId;
=======
	@Column(name="houseId")
	private Integer houseId;
>>>>>>> Stashed changes
	@Column(name = "pet")
	private Boolean pet;
	@Column(name = "parkingSpace")
	private Boolean parkingSpace;
	@Column(name = "elevator")
	private Boolean elevator;
	@Column(name = "balcony")
	private Boolean balcony;
	@Column(name = "shortTern")
	private Boolean shortTern;
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

	public ConditionTableBean() {
		
	}

<<<<<<< Updated upstream

=======
	public Integer getHouseId() {
		return houseId;
	}
	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}
>>>>>>> Stashed changes
	public Boolean getPet() {
		return pet;
	}

	public void setPet(Boolean pet) {
		this.pet = pet;
	}

	public Boolean getParkingSpace() {
		return parkingSpace;
	}

	public void setParkingSpace(Boolean parkingSpace) {
		this.parkingSpace = parkingSpace;
	}

	public Boolean getElevator() {
		return elevator;
	}

	public void setElevator(Boolean elevator) {
		this.elevator = elevator;
	}

	public Boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(Boolean balcony) {
		this.balcony = balcony;
	}

	public Boolean getShortTern() {
		return shortTern;
	}

	public void setShortTern(Boolean shortTern) {
		this.shortTern = shortTern;
	}

	public Boolean getCooking() {
		return cooking;
	}

	public void setCooking(Boolean cooking) {
		this.cooking = cooking;
	}

	public Boolean getWaterDispenser() {
		return waterDispenser;
	}

	public void setWaterDispenser(Boolean waterDispenser) {
		this.waterDispenser = waterDispenser;
	}

	public Boolean getManagementFee() {
		return managementFee;
	}

	public void setManagementFee(Boolean managementFee) {
		this.managementFee = managementFee;
	}

	public Byte getGenderRestrictions() {
		return genderRestrictions;
	}

	public void setGenderRestrictions(Byte genderRestrictions) {
		this.genderRestrictions = genderRestrictions;
	}

	public HouseTableBean getHouse() {
		return house;
	}

	public void setHouse(HouseTableBean house) {
		this.house = house;
	}


}
