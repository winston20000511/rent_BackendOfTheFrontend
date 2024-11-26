package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.OriginDTO;
import com.example.demo.model.HouseTableBean;
import com.example.demo.service.SearchService;

@RestController
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("/api/test")
	public List<HouseTableBean> testupdate() {
		List<HouseTableBean> houseList = searchService.findAll();
		houseList = searchService.updateFakeAddress("C:\\Users\\User\\Desktop\\Rent_index\\address.csv",houseList);
		houseList = searchService.getLatAndLngGoogleAPI(houseList);
		houseList = searchService.houseUpdateAll(houseList);

		return houseList;
	}
	
	// TODO
	@CrossOrigin(origins = "*")
	@PostMapping("/api/map")
	public List<AddressDTO> searchShowMap(@RequestBody OriginDTO request) {
		AddressDTO origin = searchService.placeConvertToAdress(request.getOrigin());
		List<AddressDTO> addressDtoList = searchService.findByCityAndTownship(origin.getAddress());
		addressDtoList.add(0,origin);
		long startTime = System.currentTimeMillis();
		addressDtoList = searchService.GetDurationAndDistanceGoogleAPI(addressDtoList);
		long endTime = System.currentTimeMillis();
		System.out.println("執行時間：" + (endTime - startTime) + " 毫秒");
		return addressDtoList;
	}
	
	@CrossOrigin(origins="*")
	@PostMapping("/api/keyword")
	public List<AddressDTO> searchShowkeyword(@RequestBody String srhReq){
		List<AddressDTO> addressDtoList = searchService.findByKeyWord(srhReq);
		if (addressDtoList.size() == 0 ) {
			AddressDTO house = searchService.placeConvertToAdress(srhReq);
			addressDtoList.add(house);
			return addressDtoList;
		}else if(addressDtoList.size() < 10) {
			return addressDtoList.subList(0, addressDtoList.size());
		}else {
			return addressDtoList.subList(0, 10);
		}
	}
	
	
}
