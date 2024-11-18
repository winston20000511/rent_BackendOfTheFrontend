package demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.helper.SearchHelper;
import com.example.demo.model.HouseTableBean;

import demo.repository.SearchRepository;

@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepo;
	
	@Value("${google.api.key}")
	private String apiKey;
	
	public List<HouseTableBean> findAll() {
		return searchRepo.findAll();
	}
//	
//	public List<Address> findByCity(String city){
//		return searchRepo.findByCity(city);
//	}
//	
	public List<HouseTableBean> findByCityAndTownship(String address){
		
		String[] Parts = SearchHelper.splitCityTownStreet(address);
		return searchRepo.findByCityAndTownship(Parts[0]+Parts[1]);
	}
	
	public List<HouseTableBean> findByKeyWord(String name){
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
	
	public HouseTableBean placeConvertToAdress(String origin) {
		
		String encodedAddress;
		HouseTableBean house = new HouseTableBean();
		try {
			encodedAddress = java.net.URLEncoder.encode(origin,"UTF-8");
			String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
	                + encodedAddress + "&components=country:TW&language=zh-TW&key=" + apiKey;
			StringBuilder content = SearchHelper.urlConnection(urlString);
			
			JSONObject json = new JSONObject(content.toString());
			if("OK".equals(json.getString("status"))) {
				JSONObject location = json.getJSONArray("results")
										.getJSONObject(0);
										
				String address = location.getString("formatted_address");
				if (address.indexOf("台灣") > -1 && address.indexOf("台灣大道") == -1) {
					address = address.split("台灣")[1];
				}
				
				house.setAddress(address);
				
				location = json.getJSONArray("results")
						.getJSONObject(0)
						.getJSONObject("geometry")
						.getJSONObject("location");
				
				house.setLat(location.getDouble("lat"));
				house.setLng(location.getDouble("lng"));
				
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
		
		return house;
		
	}
	
	public List<HouseTableBean> getLatAndLngGoogleAPI(List<HouseTableBean> houseList) {
		
		houseList.forEach(p->{
			if (p.getLat() == null) {
				
				try {
					String encodedAddress = java.net.URLEncoder.encode(p.getAddress(),"UTF-8");
					String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
	                        + encodedAddress + "&key=" + apiKey;

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
	
	public List<HouseTableBean> GetDurationAndDistanceGoogleAPI(List<HouseTableBean> houseList) {
		
		//String encodedOrigin;
		List<HouseTableBean> newHouseList = new ArrayList<>();
		
		for(int i = 1 ; i < houseList.size() ; i++) {
			
			Double distance = SearchHelper.getDistance(houseList.get(0), houseList.get(i));
			BigDecimal roundedValue = new BigDecimal(distance).setScale(5, RoundingMode.HALF_UP);
			if (roundedValue.compareTo(BigDecimal.valueOf(2.0))< 0) {
				newHouseList.add(houseList.get(i));
			}
			
//				String encodeDestination= java.net.URLEncoder.encode(address,"UTF-8");
//				String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" 
//		                + encodedOrigin + "&destinations=" + encodeDestination + "&mode=driving&language=zh-TW&key=" + apiKey;
//				StringBuilder content = SearchHelper.urlConnection(urlString);
//				
//				JSONObject json = new JSONObject(content.toString());
//				if("OK".equals(json.getString("status"))) {
//					JSONObject distance = json.getJSONArray("rows")
//											.getJSONObject(0)
//											.getJSONArray("elements")
//											.getJSONObject(0)
//											.getJSONObject("distance");
//					
//					JSONObject duration = json.getJSONArray("rows")
//											.getJSONObject(0)
//											.getJSONArray("elements")
//											.getJSONObject(0)
//											.getJSONObject("duration");		
//					
//					if (distance.getInt("value") <1500 && duration.getInt("value") < 3600) {
//						newAddressList.add(addressList.get(i));
//					}
////					System.out.println(distance.getInt("value"));
////					System.out.println(duration.getInt("value"));						
//					
//				}
		}
		
		return newHouseList;

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
		                + encodedOrigin + "&destinations=" + encodeDestination + "&mode=driving&language=zh-TW&key=" + apiKey;
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
