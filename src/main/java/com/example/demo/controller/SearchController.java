package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import com.example.demo.dto.KeyWordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.DrawLatLngDTO;
import com.example.demo.dto.OriginDTO;
import com.example.demo.model.HouseTableBean;
import com.example.demo.pojo.ResponseMapPOJO;
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
	
	@CrossOrigin(origins="*")
	@PostMapping("/api/map")
	public ResponseMapPOJO searchShowMap(@RequestBody AddressDTO key) {
		AddressDTO origin = searchService.placeConvertToAdress(key.getAddress());
		ResponseMapPOJO mapPOJO = searchService.findByCityAndTownship(origin, key);
		long startTime = System.currentTimeMillis();
		mapPOJO.setSearchList(searchService.getDurationAndDistance(mapPOJO.getSearchList(), origin, 2));
		long endTime = System.currentTimeMillis();
		System.out.println("執行時間：" + (endTime - startTime) + " 毫秒");
		return mapPOJO;
	}

	@CrossOrigin(origins="*")
	@PostMapping("/api/keyword")
	public List<AddressDTO> searchShowkeyword(@RequestBody KeyWordDTO key){
		List<AddressDTO> addressDtoList = searchService.findByKeyWord(key.getOrigin());
		addressDtoList = searchService.caseSort(addressDtoList,key.getPriority(),key.getSort());
//		addressDtoList = searchService.caseFilter(addressDtoList,key);
		if (addressDtoList.size() == 0 ) {
			AddressDTO house = searchService.placeConvertToAdress(key.getOrigin());
			addressDtoList.add(house);
			return addressDtoList;
		}else if(addressDtoList.size() < 10) {
			return addressDtoList.subList(0, addressDtoList.size());
		}else {
			return addressDtoList.subList(0, 10);
		}
	}
	
	@CrossOrigin(origins="*")
	@PostMapping("/api/draw")
	public ResponseMapPOJO drawShowMap(@RequestBody List<DrawLatLngDTO> drawDtoList) {
        ResponseMapPOJO mapPOJO = null;
        try {
            mapPOJO = searchService.getPlaceGoogleAPI(drawDtoList);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long startTime = System.currentTimeMillis();
		mapPOJO.setSearchList(searchService.getDrawDistance(mapPOJO.getSearchList(), mapPOJO.getSearchOrigin(),drawDtoList));
		return mapPOJO;
	}
	
}
