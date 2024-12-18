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

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.helper.GoogleApiConfig;
import com.example.demo.helper.SearchHelper;
import com.example.demo.model.HouseTableBean;
import com.example.demo.pojo.ResponseMapPOJO;
import com.example.demo.repository.SearchRepository;


@Service
public class SearchService {
	

	@Autowired
	private SearchRepository searchRepo;
	
	@Autowired
	private GoogleApiConfig googleApiConfig;
	
	public List<HouseTableBean> findAll() {
		return searchRepo.findAll();
	}

	public ResponseMapPOJO findByCityAndTownship(AddressDTO origin){
		String[] Parts = SearchHelper.splitCityTown(origin.getAddress());
		HashSet<AddressDTO> setAddressDTO = searchRepo.findByCityAndTownship(Parts[0]);
		Integer placeAvgPrice = SearchHelper.getPlaceAvgPrice(setAddressDTO);
		setAddressDTO.add(origin);
		List<AddressDTO> listAddressDTO = new ArrayList<>(setAddressDTO);
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
		
		List<String> lists = SearchHelper.openfileRead(filePath);
		
		for (int i = 0; i < houseList.size(); i++) {
			
			houseList.get(i).setAddress(lists.get(i));
		}
		
		return houseList;
	}
	
	public double[] placeToLagLngForRegister(String registerAddress) {
		String encodedAddress;
		double loactionLat=0;
		double locationLng=0;
		try {
			encodedAddress = java.net.URLEncoder.encode(registerAddress,"UTF-8");
			String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
	                + encodedAddress + "&components=country:TW&bounds=21.5,119.5|25.5,122.5&language=zh-TW&key=" + googleApiConfig.getGoogleMapKey();
			StringBuilder content = SearchHelper.urlConnection(urlString);
			
			JSONObject json = new JSONObject(content.toString());
			if("OK".equals(json.getString("status"))) {
				JSONObject location = json.getJSONArray("results")
										.getJSONObject(0);
										
				String address = location.getString("formatted_address");
				if (address.length()<=2 ) {
					address = "";
				}
				else if (address.indexOf("台灣") > -1 && address.indexOf("台灣大道") == -1) {
					address = address.split("台灣")[1];
				}
				
				
				location = json.getJSONArray("results")
						.getJSONObject(0)
						.getJSONObject("geometry")
						.getJSONObject("location");
				
				loactionLat = (location.getDouble("lat"));
				locationLng = (location.getDouble("lng"));
				
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new double[] {loactionLat,locationLng};
		
	}
	
	public AddressDTO placeConvertToAdress(String origin) {
		
		String encodedAddress;
		AddressDTO addressDTO = new AddressDTO();
		try {
			encodedAddress = java.net.URLEncoder.encode(origin,"UTF-8");
			String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
	                + encodedAddress + "&components=country:TW&bounds=21.5,119.5|25.5,122.5&language=zh-TW&key=" + googleApiConfig.getGoogleMapKey();
			StringBuilder content = SearchHelper.urlConnection(urlString);
			
			JSONObject json = new JSONObject(content.toString());
			if("OK".equals(json.getString("status"))) {
				JSONObject location = json.getJSONArray("results")
										.getJSONObject(0);
										
				String address = location.getString("formatted_address");
				if (address.length()<=2 ) {
					address = "";
				}
				else if (address.indexOf("台灣") > -1 && address.indexOf("台灣大道") == -1) {
					address = address.split("台灣")[1];
				}
				
				addressDTO.setAddress(address);
				
				location = json.getJSONArray("results")
						.getJSONObject(0)
						.getJSONObject("geometry")
						.getJSONObject("location");
				
				addressDTO.setLat(location.getDouble("lat"));
				addressDTO.setLng(location.getDouble("lng"));
				
			}
			
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
					String encodedAddress = java.net.URLEncoder.encode(p.getAddress(),"UTF-8");
					String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
	                        + encodedAddress + "&key=" + googleApiConfig.getGoogleMapKey();

					StringBuilder content = SearchHelper.urlConnection(urlString);
					
					JSONObject json = new JSONObject(content.toString());
					if("OK".equals(json.getString("status"))) {
						JSONObject location = json.getJSONArray("results")
												.getJSONObject(0)
												.getJSONObject("geometry")
												.getJSONObject("location");
						
						p.setLat(location.getDouble("lat"));
						p.setLng(location.getDouble("lng"));
					}else {
						System.out.println("無法獲取經緯度資料，狀態：" + json.getString("status"));
					}
					
					
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
				}	
				
			}

			
		});
		
		return houseList;
	}
	
	public List<AddressDTO> GetDurationAndDistance(List<AddressDTO> addressDtoList , AddressDTO origin) {
		
		//String encodedOrigin;
		List<AddressDTO> newAddressDtoList = new ArrayList<>();
		
		for(int i = 1 ; i < addressDtoList.size() ; i++) {
			
			Double distance = SearchHelper.getDistance(origin, addressDtoList.get(i));
			BigDecimal roundedValue = new BigDecimal(distance).setScale(5, RoundingMode.HALF_UP);
			if (roundedValue.compareTo(BigDecimal.valueOf(2.0))< 0 && origin.getAddress() != addressDtoList.get(i).getAddress()) {
				newAddressDtoList.add(addressDtoList.get(i));
			}
		}
		
		return newAddressDtoList;

	}
	
	
}
