package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OriginRequest;
import com.example.demo.model.AddressTableBean;
import com.example.demo.service.SearchService;

@RestController
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("/api/test")
	public List<AddressTableBean> testupdate() {
		List<AddressTableBean> addressList = searchService.findAll();
		addressList = searchService.updateFakeAddress("C:\\Users\\User\\Desktop\\Rent_index\\address.csv",addressList);
		addressList = searchService.getLatAndLngGoogleAPI(addressList);
		addressList = searchService.addressUpdateAll(addressList);

		return addressList;
	}
	
	// TODO
	@CrossOrigin(origins = "*")
	@PostMapping("/api/map")
	public List<AddressTableBean> searchShowMap(@RequestBody OriginRequest request) {
		AddressTableBean origin = searchService.placeConvertToAdress(request.getOrigin());
		List<AddressTableBean> addressList = searchService.findByCity(origin.getCity());
		addressList.add(0,origin);
		
		
		
		return addressList;
	}
	
	@CrossOrigin(origins="*")
	@PostMapping("/api/keyword")
	public List<AddressTableBean> searchShowText(@RequestBody String srhReq){
//		Address address = searchService.placeConvertToAdress(srhReq);
//		List<Address> addressList = searchService.findByKeyWord(address.getCity()+address.getTownship()+address.getStreet());
//		if (addressList.size() == 0 ) {
//			addressList.add(address);
//			return addressList;
//		}else if(addressList.size() < 8) {
//			return addressList.subList(0, addressList.size());
//		}else {
//			return addressList.subList(0, 10);
//		}
		List<AddressTableBean> addressList = searchService.findByKeyWord(srhReq);
		return addressList.subList(0, 10);
		
	}
	
	
}
