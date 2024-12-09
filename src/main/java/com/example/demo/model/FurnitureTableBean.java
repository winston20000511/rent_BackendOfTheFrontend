package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "furniture_table")
public class FurnitureTableBean {

	@Id
	private Long houseId;
	@Column(name = "washingMachine")
	private Boolean washingMachine;
	@Column(name = "airConditioner")
	private Boolean airConditioner;
	@Column(name = "network")
	private Boolean network;
	@Column(name = "bedstead")
	private Boolean bedstead;
	@Column(name = "mattress")
	private Boolean mattress;
	@Column(name = "refrigerator")
	private Boolean refrigerator;
	@Column(name = "ewaterHeater")
	private Boolean ewaterHeater;
	@Column(name = "gwaterHeater")
	private Boolean gwaterHeater;
	@Column(name = "television")
	private Boolean television;
	@Column(name = "channel4")
	private Boolean channel4;
	@Column(name = "sofa")
	private Boolean sofa;
	@Column(name = "tables")
	private Boolean tables;

    @OneToOne
    @MapsId
    @JoinColumn(name = "house_id",nullable = false)
    private HouseTableBean house;

}