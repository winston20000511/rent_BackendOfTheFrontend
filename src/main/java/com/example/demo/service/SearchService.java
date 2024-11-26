package com.example.demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.helper.GoogleApiConfig;
import com.example.demo.helper.SearchHelper;
import com.example.demo.model.HouseTableBean;
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
//	
//	public List<Address> findByCity(String city){
//		return searchRepo.findByCity(city);
//	}
//	
	public List<AddressDTO> findByCityAndTownship(String address){
		
		String[] Parts = SearchHelper.splitCityTown(address);
		return searchRepo.findByCityAndTownship(Parts[0]+Parts[1]);
	}
	
	public List<AddressDTO> findByKeyWord(String name){
		return searchRepo.findByKeyWord(name);
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
	
	public AddressDTO placeConvertToAdress(String origin) {
		
		String encodedAddress;
		AddressDTO adress = new AddressDTO();
		try {
			encodedAddress = java.net.URLEncoder.encode(origin,"UTF-8");
			String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
	                + encodedAddress + "&components=country:TW&language=zh-TW&key=" + googleApiConfig.getGoogleMapKey();
			StringBuilder content = SearchHelper.urlConnection(urlString);
			
			JSONObject json = new JSONObject(content.toString());
			if("OK".equals(json.getString("status"))) {
				JSONObject location = json.getJSONArray("results")
										.getJSONObject(0);
										
				String address = location.getString("formatted_address");
				if (address.indexOf("台灣") > -1 && address.indexOf("台灣大道") == -1) {
					address = address.split("台灣")[1];
				}
				
				adress.setAddress(address);
				
				location = json.getJSONArray("results")
						.getJSONObject(0)
						.getJSONObject("geometry")
						.getJSONObject("location");
				
				adress.setLat(location.getDouble("lat"));
				adress.setLng(location.getDouble("lng"));
				
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
		
		return adress;
		
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
	
	public List<AddressDTO> GetDurationAndDistanceGoogleAPI(List<AddressDTO> addressDtoList) {
		
		//String encodedOrigin;
		List<AddressDTO> newAddressDtoList = new ArrayList<>();
		
		for(int i = 1 ; i < addressDtoList.size() ; i++) {
			
			Double distance = SearchHelper.getDistance(addressDtoList.get(0), addressDtoList.get(i));
			BigDecimal roundedValue = new BigDecimal(distance).setScale(5, RoundingMode.HALF_UP);
			if (roundedValue.compareTo(BigDecimal.valueOf(2.0))< 0) {
				newAddressDtoList.add(addressDtoList.get(i));
			}
		}
		
		return newAddressDtoList;

	}
	
	public List<HouseTableBean> GetDurationAndDistanceGoogleAPI2(List<HouseTableBean> houseList) {
		
		String encodedOrigin;
		List<HouseTableBean> newHouseList = new ArrayList<>();
		
		try {
			String address = houseList.get(0).getAddress();
			encodedOrigin = java.net.URLEncoder.encode( address ,"UTF-8");
			
			for(int i = 1 ; i < houseList.size() ; i++) {
				
				address = houseList.get(i).getAddress();
				String encodeDestination= java.net.URLEncoder.encode(address,"UTF-8");
				String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" 
		                + encodedOrigin + "&destinations=" + encodeDestination + "&mode=driving&language=zh-TW&key=" + googleApiConfig.getGoogleMapKey();
				StringBuilder content = SearchHelper.urlConnection(urlString);
				
				JSONObject json = new JSONObject(content.toString());
				if("OK".equals(json.getString("status"))) {
					JSONObject distance = json.getJSONArray("rows")
											.getJSONObject(0)
											.getJSONArray("elements")
											.getJSONObject(0)
											.getJSONObject("distance");
					
					JSONObject duration = json.getJSONArray("rows")
											.getJSONObject(0)
											.getJSONArray("elements")
											.getJSONObject(0)
											.getJSONObject("duration");		
					
					if (distance.getInt("value") <2000 && duration.getInt("value") < 3600) {
						newHouseList.add(houseList.get(i));
					}
//					System.out.println(distance.getInt("value"));
//					System.out.println(duration.getInt("value"));						
					
				}
			}
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newHouseList;

	}
	
}
