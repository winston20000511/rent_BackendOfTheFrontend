package demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="condition_table")
public class ConditionTableBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long conditionId;
	@Column(name="houseId")
	private Integer houseId;
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
	private Boolean genderRestrictions;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "houseid", nullable = false)
    private HouseTableBean HouseTableBean;
	
	public ConditionTableBean() {
		
	}
	public long getConditionId() {
		return conditionId;
	}
	public void setConditionId(long conditionId) {
		this.conditionId = conditionId;
	}
	public Integer getHouseId() {
		return houseId;
	}
	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}
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
	public Boolean getGenderRestrictions() {
		return genderRestrictions;
	}
	public void setGenderRestrictions(Boolean genderRestrictions) {
		this.genderRestrictions = genderRestrictions;
	}
	
}
