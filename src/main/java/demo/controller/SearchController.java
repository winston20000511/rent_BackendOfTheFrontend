package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OriginRequest;
import com.example.demo.model.HouseTableBean;

import demo.service.SearchService;

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
	public List<HouseTableBean> searchShowMap(@RequestBody OriginRequest request) {
		HouseTableBean origin = searchService.placeConvertToAdress(request.getOrigin());
		List<HouseTableBean> houseList = searchService.findByCityAndTownship(origin.getAddress());
		houseList.add(0,origin);
		long startTime = System.currentTimeMillis();
		houseList = searchService.GetDurationAndDistanceGoogleAPI(houseList);
		long endTime = System.currentTimeMillis();
		System.out.println("執行時間：" + (endTime - startTime) + " 毫秒");
		return houseList;
	}
	
	@CrossOrigin(origins="*")
	@PostMapping("/api/keyword")
	public List<HouseTableBean> searchShowkeyword(@RequestBody String srhReq){
		List<HouseTableBean> houseList = searchService.findByKeyWord(srhReq);
		if (houseList.size() == 0 ) {
			HouseTableBean house = searchService.placeConvertToAdress(srhReq);
			houseList.add(house);
			return houseList;
		}else if(houseList.size() < 10) {
			return houseList.subList(0, houseList.size());
		}else {
			return houseList.subList(0, 10);
		}
	}
	
	
}
