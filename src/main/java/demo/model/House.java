package demo.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="house_table")
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long house_id;
	
	@Column(name="user_id")
	private long user_id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="price")
	private long price;
	
	@Column(name="description")
	private String description;
	
	@Column(name="size")
	private int size;
	
	@Column(name="address")
	private String address;
	
	@Column(name="lat")
	private Double lat;
	
	@Column(name="lng")
	private Double lng;
	
	@Column(name="room")
	private short room;
	
	@Column(name="bathroom")
	private short bathroom;
	
	@Column(name="livingroom")
	private short livingroom;
	
	@Column(name="kitchen")
	private short kitchen;

	@Column(name="floor")
	private short floor;
	
	@Column(name="atticAddition")
	private boolean atticAddition;
	
	@Column(name="status")
	private short status;

	public long getHouse_id() {
		return house_id;
	}

	public void setHouse_id(long house_id) {
		this.house_id = house_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public short getRoom() {
		return room;
	}

	public void setRoom(short room) {
		this.room = room;
	}

	public short getBathroom() {
		return bathroom;
	}

	public void setBathroom(short bathroom) {
		this.bathroom = bathroom;
	}

	public short getLivingroom() {
		return livingroom;
	}

	public void setLivingroom(short livingroom) {
		this.livingroom = livingroom;
	}

	public short getKitchen() {
		return kitchen;
	}

	public void setKitchen(short kitchen) {
		this.kitchen = kitchen;
	}

	public short getFloor() {
		return floor;
	}

	public void setFloor(short floor) {
		this.floor = floor;
	}

	public boolean isAtticAddition() {
		return atticAddition;
	}

	public void setAtticAddition(boolean atticAddition) {
		this.atticAddition = atticAddition;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}
	
	
	
	
	
	
}
