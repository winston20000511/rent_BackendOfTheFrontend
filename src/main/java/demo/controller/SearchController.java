package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.model.House;
import demo.service.SearchService;

@RestController
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("/api/test")
	public List<House> testupdate() {
		List<House> houseList = searchService.findAll();
		List<String> addressList = searchService.fakeAddress("C:\\Users\\User\\Desktop\\Rent_index\\Tainan.csv");
		
		houseList = searchService.houseUpdateFakeAdress(houseList, addressList);
		List<double[]> posList = searchService.getLatAndLonGoogleAPI(houseList);
		
		return houseList;
	}
	
	@PostMapping("/api/Map")
	public void searchShowMap() {
		
		
	}
	
	
}
