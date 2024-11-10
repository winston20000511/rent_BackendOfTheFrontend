package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.model.Address;
import demo.service.SearchService;

@RestController
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("/api/test")
	public List<Address> testupdate() {
		List<Address> addressList = searchService.findAll();
		addressList = searchService.updateFakeAddress("C:\\Users\\User\\Desktop\\Rent_index\\Tainan.csv",addressList);
		addressList = searchService.getLatAndLngGoogleAPI(addressList);
		
		addressList = searchService.addressUpdateAll(addressList);
		

		return addressList;
	}
	
	@PostMapping("/api/Map")
	public void searchShowMap() {
		List<Address> addressList = searchService.findAll();
		addressList = searchService.getLatAndLngGoogleAPI(addressList);
	}
	
	
}
