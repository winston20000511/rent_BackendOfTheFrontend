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
@Table(name = "furniture_table")
public class FurnitureTableBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long furnitureId;

	@Column(name = "house_id")
	private Integer houseId;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "houseid", nullable = false)
    private HouseTableBean HouseTableBean;
	
	public FurnitureTableBean() {

	}

	public long getFurnitureId() {
		return furnitureId;
	}

	public void setFurnitureId(long furnitureId) {
		this.furnitureId = furnitureId;
	}

	public Integer getHouseId() {
		return houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
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
