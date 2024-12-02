package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long houseId;
	private Long userId;

}
