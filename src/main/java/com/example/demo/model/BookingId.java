package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class BookingId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long houseId;
	private Long userId;
	
	public BookingId() {
		
	}
	
	
	public BookingId(Long houseId, Long userId) {
		this.houseId = houseId;
		this.userId = userId;
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
