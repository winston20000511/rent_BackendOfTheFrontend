package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.dto.OriginRequest;
import demo.model.Address;
import demo.service.SearchService;

@RestController
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("/api/test")
	public List<Address> testupdate() {
		List<Address> addressList = searchService.findAll();
		addressList = searchService.updateFakeAddress("C:\\Users\\User\\Desktop\\Rent_index\\address7.csv",addressList);
		addressList = searchService.getLatAndLngGoogleAPI(addressList);
		addressList = searchService.addressUpdateAll(addressList);

		return addressList;
	}
	
	// TODO
	@CrossOrigin(origins = "*")
	@PostMapping("/api/map")
	public List<Address> searchShowMap(@RequestBody OriginRequest request) {
		Address origin = searchService.placeConvertToAdress(request.getOrigin());
		List<Address> addressList = searchService.findByCity(origin.getCity());
		addressList.add(0,origin);
		
		return addressList;
	}
	
	
}
