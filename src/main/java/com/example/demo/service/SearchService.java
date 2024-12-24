package com.example.demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.example.demo.dto.KeyWordDTO;
import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.DrawLatLngDTO;
import com.example.demo.helper.GoogleApiConfig;
import com.example.demo.helper.SearchHelper;
import com.example.demo.model.HouseTableBean;
import com.example.demo.pojo.ResponseMapPOJO;
import com.example.demo.repository.SearchRepository;


@Slf4j
@Service
public class SearchService {
	

	@Autowired
	private SearchRepository searchRepo;
	
	@Autowired
	private SearchHelper searchHelp;
	
	public List<HouseTableBean> findAll() {
		return searchRepo.findAll();
	}

	public List<AddressDTO> caseFilter(List<AddressDTO> listAddressDTO , AddressDTO userKey) {

		if (userKey.getMaxPrice() == 0){
			userKey.setMaxPrice(9999999);
		}

		List<AddressDTO> newListAddressDTO = new ArrayList<>();
		for(AddressDTO item : listAddressDTO ){

			item.setMinPrice(item.getPrice());
			item.setMaxPrice(item.getPrice());
			item.setPriority(userKey.getPriority());
			log.info(item.getHouseType());
			log.info(item.getHouseid().toString());
			if (userKey.equals(item)){
				newListAddressDTO.add(item);
			}
		};
		return newListAddressDTO;
	}

	public ResponseMapPOJO findByCityAndTownship(AddressDTO origin , AddressDTO userKey){
		String[] Parts = searchHelp.splitCityTown(origin.getAddress());
		HashSet<AddressDTO> setAddressDTO = searchRepo.findByCityAndTownship(Parts[0]);
		Integer placeAvgPrice = searchHelp.getPlaceAvgPrice(setAddressDTO);
//		setAddressDTO.add(origin);
		List<AddressDTO> listAddressDTO = new ArrayList<>(setAddressDTO);
		listAddressDTO = caseFilter(listAddressDTO,userKey);
		return new ResponseMapPOJO(listAddressDTO,origin,placeAvgPrice);
	}
	
	public List<AddressDTO> findByKeyWord(String name){
		List<AddressDTO> listAddressDTO = searchRepo.findByKeyWord(name);
		listAddressDTO.sort(Comparator.comparing(AddressDTO::getPaidDate).reversed());
		return listAddressDTO;
	}


	public List<HouseTableBean> houseUpdateAll(List<HouseTableBean> houseList) {
		return searchRepo.saveAll(houseList);
	}
	
	public List<HouseTableBean> updateFakeAddress(String filePath,List<HouseTableBean> houseList){
		
		List<String> lists = searchHelp.openfileRead(filePath);
		
		for (int i = 0; i < houseList.size(); i++) {
			
			houseList.get(i).setAddress(lists.get(i));
		}
		
		return houseList;
	}
	
	public double[] placeToLagLngForRegister(String registerAddress) {
		double[] location = new double[2];
		
		try {
			JSONObject json = searchHelp.fetchGeocodeFromAPI(registerAddress);
			searchHelp.parseLatLng(json).ifPresent(latlng ->{
				location[0] = latlng[0];
				location[1] = latlng[1];
			});
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return location;
		
	}
	
	public AddressDTO placeConvertToAdress(String origin) {
		
		String address="";
		AddressDTO addressDTO = new AddressDTO();
		try {

			JSONObject json = searchHelp.fetchGeocodeFromAPI(origin);
			Optional<String> optionAddress = searchHelp.parseAddress(json);
			if (optionAddress.isPresent()) {
				address = optionAddress.get();
			}

			if (address.length()<=2 ) {
				address = "";
			}
			else if (address.indexOf("台灣") > -1 && address.indexOf("台灣大道") == -1) {
				address = address.split("台灣")[1];
			}
			addressDTO.setAddress(address);
			
			searchHelp.parseLatLng(json).ifPresent(latlng ->{
				addressDTO.setLat(latlng[0]);
				addressDTO.setLng(latlng[1]);
			});
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return addressDTO;
		
	}
	
	public List<HouseTableBean> getLatAndLngGoogleAPI(List<HouseTableBean> houseList) {
		
		houseList.forEach(p->{
			if (p.getLat() == null) {
				
				try {
					JSONObject json = searchHelp.fetchGeocodeFromAPI(p.getAddress());
                    searchHelp.parseLatLng(json).ifPresent(latlng -> {
                        p.setLat(latlng[0]);
                        p.setLng(latlng[1]);
                    });
					Thread.sleep(100);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
			}

			
		});
		
		return houseList;
	}
	
	public List<AddressDTO> getDurationAndDistance(List<AddressDTO> addressDtoList , AddressDTO origin , int spec) {

		List<AddressDTO> newAddressDtoList = new ArrayList<>();
		
		for(int i = 0 ; i < addressDtoList.size() ; i++) {
			
			Double distance = searchHelp.getDistance(origin, addressDtoList.get(i));
			BigDecimal roundedValue = new BigDecimal(distance).setScale(5, RoundingMode.HALF_UP);
			if (roundedValue.compareTo(BigDecimal.valueOf(spec))< 0 && origin.getAddress() != addressDtoList.get(i).getAddress()) {
				newAddressDtoList.add(addressDtoList.get(i));
			}
		}
		
		return newAddressDtoList;

	}

	public List<AddressDTO> getDrawDistance(List<AddressDTO> addressDtoList , AddressDTO origin , List<DrawLatLngDTO> drawDtoList) {

		List<AddressDTO> newAddressDtoList = new ArrayList<>();

		for(int i = 0 ; i < addressDtoList.size() ; i++) {

			if (searchHelp.isPointInPolygon(addressDtoList.get(i).getLat()
					,addressDtoList.get(i).getLng(), drawDtoList) &&
					!origin.getAddress().equals(addressDtoList.get(i).getAddress())){
				newAddressDtoList.add(addressDtoList.get(i));

			}
		}

		return newAddressDtoList;

	}

	public ResponseMapPOJO getPlaceGoogleAPI(List<DrawLatLngDTO> drawDtoList) throws JSONException, IOException {
		
		String address="";
		AddressDTO origin = new AddressDTO();

		DrawLatLngDTO drawDTO = searchHelp.getAvgLatLng(drawDtoList);
		JSONObject json =  searchHelp.fetchReverseGeocodingFromAPI(drawDTO);
		Optional<String> optionAddress = searchHelp.parseAddress(json);
		if (optionAddress.isPresent()) {
			address = optionAddress.get();

		}
		origin.setAddress(address);
		origin.setLat(drawDTO.getLat());
		origin.setLng(drawDTO.getLng());

		String[] Parts = searchHelp.splitCityTown(address);
		HashSet<AddressDTO> setAddressDTO = searchRepo.findByCityAndTownship(Parts[0]);
		Integer placeAvgPrice = searchHelp.getPlaceAvgPrice(setAddressDTO);

		origin.setPrice(placeAvgPrice);
		setAddressDTO.add(origin);
		List<AddressDTO> listAddressDTO = new ArrayList<>(setAddressDTO);
		return new ResponseMapPOJO(listAddressDTO,origin,placeAvgPrice);

	}
	
	
}
