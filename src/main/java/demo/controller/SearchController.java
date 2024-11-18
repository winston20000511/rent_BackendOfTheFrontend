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
		addressList = searchService.updateFakeAddress("C:\\Users\\User\\Desktop\\Rent_index\\address.csv",addressList);
		addressList = searchService.getLatAndLngGoogleAPI(addressList);
		addressList = searchService.addressUpdateAll(addressList);

		return addressList;
	}
	
	// TODO
	@CrossOrigin(origins = "*")
	@PostMapping("/api/map")
	public List<Address> searchShowMap(@RequestBody OriginRequest request) {
		Address origin = searchService.placeConvertToAdress(request.getOrigin());
		List<Address> addressList = searchService.findByCityAndTownship(origin.getCity()+origin.getTownship());
		addressList.add(0,origin);
		long startTime = System.currentTimeMillis();
		addressList = searchService.GetDurationAndDistanceGoogleAPI(addressList);
		long endTime = System.currentTimeMillis();
		System.out.println("執行時間：" + (endTime - startTime) + " 毫秒");
		return addressList;
	}
	
	@CrossOrigin(origins="*")
	@PostMapping("/api/keyword")
	public List<Address> searchShowkeyword(@RequestBody String srhReq){
		List<Address> addressList = searchService.findByKeyWord(srhReq);
		if (addressList.size() == 0 ) {
			Address address = searchService.placeConvertToAdress(srhReq);
			addressList.add(address);
			return addressList;
		}else if(addressList.size() < 10) {
			return addressList.subList(0, addressList.size());
		}else {
			return addressList.subList(0, 10);
		}
	}
	
	
}
