package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class HouseDetailsDTO {

    private Long houseId;
    private String useremail;
    private Long userId;
    private String title;
    private Integer price;
    private String description;
    private Integer size;
    private String address;
    private Byte status;
    private Byte room;
    private Byte bathroom;
    private Byte livingroom;
    private Byte kitchen;
    private Byte floor;
    private String houseType;
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
    private List<byte[]> images;

    

	public HouseDetailsDTO() {
		
	}



	public HouseDetailsDTO(Long houseId, Long userId, String useremail, String title, Integer price, String description, Integer size,
			String address,Byte status, Byte room, Byte bathroom, Byte livingroom, Byte kitchen, Byte floor, String houseType,
			Boolean atticAddition, Boolean pet, Boolean parkingSpace, Boolean elevator, Boolean balcony,
			Boolean shortTerm, Boolean cooking, Boolean waterDispenser, Boolean managementFee, Byte genderRestrictions,
			Boolean washingMachine, Boolean airConditioner, Boolean network, Boolean bedstead, Boolean mattress,
			Boolean refrigerator, Boolean ewaterHeater, Boolean gwaterHeater, Boolean television, Boolean channel4,
			Boolean sofa, Boolean tables, List<byte[]> images) {
		super();
		this.houseId = houseId;
		this.useremail = useremail;
		this.userId = userId;
		this.title = title;
		this.price = price;
		this.description = description;
		this.size = size;
		this.address = address;
		this.status =status;
		this.room = room;
		this.bathroom = bathroom;
		this.livingroom = livingroom;
		this.kitchen = kitchen;
		this.floor = floor;
		this.houseType = houseType;
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
		this.images = images;
	}
	
}
