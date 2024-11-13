package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BookingId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer userId;
	private Integer houseId;
	
	public BookingId(Integer userId, Integer houseId) {
		this.userId = userId;
		this.houseId = houseId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getHouseId() {
		return houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(houseId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookingId other = (BookingId) obj;
		return Objects.equals(houseId, other.houseId) && Objects.equals(userId, other.userId);
	}

}
