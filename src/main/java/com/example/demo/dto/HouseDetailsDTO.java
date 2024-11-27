package com.example.demo.dto;

public class HouseDetailsDTO {

    private Long houseId;
    private Long userId;
    private String title;
    private Integer price;
    private String description;
    private Integer size;
    private String address;
    private Byte room;
    private Byte bathroom;
    private Byte livingroom;
    private Byte kitchen;
    private Byte floor;
    private Boolean atticAddition;
    private Boolean pet;
	private Boolean parkingSpace;
	private Boolean elevator;
	private Boolean balcony;
	private Boolean shortTerm;
	private Boolean cooking;
	private Boolean waterDispenser;
	private Boolean managementFee;
	private Byte genderRestrictions;
	private Boolean washingMachine;
	private Boolean airConditioner;
	private Boolean network;
	private Boolean bedstead;
	private Boolean mattress;
	private Boolean refrigerator;
	private Boolean ewaterHeater;
	private Boolean gwaterHeater;
	private Boolean television;
	private Boolean channel4;
	private Boolean sofa;
	private Boolean tables;
	
	public HouseDetailsDTO(Long houseId, Long userId, String title, Integer price, String description, Integer size,
			String address, Byte room, Byte bathroom, Byte livingroom, Byte kitchen, Byte floor, Boolean atticAddition,
			Boolean pet, Boolean parkingSpace, Boolean elevator, Boolean balcony, Boolean shortTerm, Boolean cooking,
			Boolean waterDispenser, Boolean managementFee, Byte genderRestrictions, Boolean washingMachine,
			Boolean airConditioner, Boolean network, Boolean bedstead, Boolean mattress, Boolean refrigerator,
			Boolean ewaterHeater, Boolean gwaterHeater, Boolean television, Boolean channel4, Boolean sofa,
			Boolean tables) {
		super();
		this.houseId = houseId;
		this.userId = userId;
		this.title = title;
		this.price = price;
		this.description = description;
		this.size = size;
		this.address = address;
		this.room = room;
		this.bathroom = bathroom;
		this.livingroom = livingroom;
		this.kitchen = kitchen;
		this.floor = floor;
		this.atticAddition = atticAddition;
		this.pet = pet;
		this.parkingSpace = parkingSpace;
		this.elevator = elevator;
		this.balcony = balcony;
		this.shortTerm = shortTerm;
		this.cooking = cooking;
		this.waterDispenser = waterDispenser;
		this.managementFee = managementFee;
		this.genderRestrictions = genderRestrictions;
		this.washingMachine = washingMachine;
		this.airConditioner = airConditioner;
		this.network = network;
		this.bedstead = bedstead;
		this.mattress = mattress;
		this.refrigerator = refrigerator;
		this.ewaterHeater = ewaterHeater;
		this.gwaterHeater = gwaterHeater;
		this.television = television;
		this.channel4 = channel4;
		this.sofa = sofa;
		this.tables = tables;
	}

	public HouseDetailsDTO() {
		
	}
	
	public Long getHouseId() {
		return houseId;
	}
	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Byte getRoom() {
		return room;
	}
	public void setRoom(Byte room) {
		this.room = room;
	}
	public Byte getBathroom() {
		return bathroom;
	}
	public void setBathroom(Byte bathroom) {
		this.bathroom = bathroom;
	}
	public Byte getLivingroom() {
		return livingroom;
	}
	public void setLivingroom(Byte livingroom) {
		this.livingroom = livingroom;
	}
	public Byte getKitchen() {
		return kitchen;
	}
	public void setKitchen(Byte kitchen) {
		this.kitchen = kitchen;
	}
	public Byte getFloor() {
		return floor;
	}
	public void setFloor(Byte floor) {
		this.floor = floor;
	}
	public Boolean getAtticAddition() {
		return atticAddition;
	}
	public void setAtticAddition(Boolean atticAddition) {
		this.atticAddition = atticAddition;
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
	public Boolean getShortTerm() {
		return shortTerm;
	}
	public void setShortTerm(Boolean shortTerm) {
		this.shortTerm = shortTerm;
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
	public Boolean getWashingMachine() {
		return washingMachine;
	}
	public void setWashingMachine(Boolean washingMachine) {
		this.washingMachine = washingMachine;
	}
	public Boolean getAirConditioner() {
		return airConditioner;
	}
	public void setAirConditioner(Boolean airConditioner) {
		this.airConditioner = airConditioner;
	}
	public Boolean getNetwork() {
		return network;
	}
	public void setNetwork(Boolean network) {
		this.network = network;
	}
	public Boolean getBedstead() {
		return bedstead;
	}
	public void setBedstead(Boolean bedstead) {
		this.bedstead = bedstead;
	}
	public Boolean getMattress() {
		return mattress;
	}
	public void setMattress(Boolean mattress) {
		this.mattress = mattress;
	}
	public Boolean getRefrigerator() {
		return refrigerator;
	}
	public void setRefrigerator(Boolean refrigerator) {
		this.refrigerator = refrigerator;
	}
	public Boolean getEwaterHeater() {
		return ewaterHeater;
	}
	public void setEwaterHeater(Boolean ewaterHeater) {
		this.ewaterHeater = ewaterHeater;
	}
	public Boolean getGwaterHeater() {
		return gwaterHeater;
	}
	public void setGwaterHeater(Boolean gwaterHeater) {
		this.gwaterHeater = gwaterHeater;
	}
	public Boolean getTelevision() {
		return television;
	}
	public void setTelevision(Boolean television) {
		this.television = television;
	}
	public Boolean getChannel4() {
		return channel4;
	}
	public void setChannel4(Boolean channel4) {
		this.channel4 = channel4;
	}
	public Boolean getSofa() {
		return sofa;
	}
	public void setSofa(Boolean sofa) {
		this.sofa = sofa;
	}
	public Boolean getTables() {
		return tables;
	}
	public void setTables(Boolean tables) {
		this.tables = tables;
	}

}
