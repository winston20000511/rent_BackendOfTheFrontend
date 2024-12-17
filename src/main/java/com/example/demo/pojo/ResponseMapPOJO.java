package com.example.demo.pojo;

import java.util.List;

import com.example.demo.dto.AddressDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseMapPOJO {
	private List<AddressDTO> searchList;
	private AddressDTO searchOrigin;
	private Integer avgPrice;
}
