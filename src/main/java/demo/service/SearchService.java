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

import demo.model.Address;
import demo.repository.SearchRepository;
import helper.SearchHelper;

@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepo;
	
	@Value("${google.api.key}")
	private String apiKey;
	
	public List<Address> findAll() {
		return searchRepo.findAll();
	}
	
	public List<Address> findByCity(String city){
		return searchRepo.findByCity(city);
	}
	
	public List<Address> findByCityAndTownship(String cityTownship){
		return searchRepo.findByCityAndTownship(cityTownship);
	}
	
	public List<Address> findByKeyWord(String name){
		return searchRepo.findByKeyWord(name);
	}
	
	public List<Address> addressUpdateAll(List<Address> addressList) {
		return searchRepo.saveAll(addressList);
	}
	
	public List<Address> updateFakeAddress(String filePath,List<Address> addressList){
		
		List<String> lists = SearchHelper.openfileRead(filePath);
		
		for (int i = 0; i < addressList.size(); i++) {
			
			String[] Parts = SearchHelper.splitCityTownStreet(lists.get(i));
			addressList.get(i).setCity(Parts[0]);
			addressList.get(i).setTownship(Parts[1]);
			addressList.get(i).setStreet(Parts[2]);
		}
		
		return addressList;
	}
	
	public Address placeConvertToAdress(String origin) {
		
		String encodedAddress;
		Address address = new Address();
		try {
			encodedAddress = java.net.URLEncoder.encode(origin,"UTF-8");
			String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
	                + encodedAddress + "&language=zh-TW&key=" + apiKey;
			StringBuilder content = SearchHelper.urlConnection(urlString);
			
			JSONObject json = new JSONObject(content.toString());
			if("OK".equals(json.getString("status"))) {
				JSONObject location = json.getJSONArray("results")
										.getJSONObject(0);
										
				
				String[] Parts = SearchHelper.splitCityTownStreet(location.getString("formatted_address"));
				address.setCity(Parts[0]);
				address.setTownship(Parts[1]);
				address.setStreet(Parts[2]);
				
				location = json.getJSONArray("results")
						.getJSONObject(0)
						.getJSONObject("geometry")
						.getJSONObject("location");
				
				address.setLat(location.getDouble("lat"));
				address.setLng(location.getDouble("lng"));
				
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
		
		return address;
		
	}
	
	public List<Address> getLatAndLngGoogleAPI(List<Address> addressList) {
		
		addressList.forEach(p->{
			if (p.getLat() == null) {
				
				String address = p.getCity() + p.getTownship() + p.getStreet();
				try {
					String encodedAddress = java.net.URLEncoder.encode(address,"UTF-8");
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
		
		return addressList;
	}
	
	public List<Address> GetDurationAndDistanceGoogleAPI(List<Address> addressList) {
		
		String encodedOrigin;
		List<Address> newAddressList = new ArrayList<>();
		
		try {
			String address = addressList.get(0).getCity()+addressList.get(0).getTownship()+addressList.get(0).getStreet();
			encodedOrigin = java.net.URLEncoder.encode( address ,"UTF-8");
			
			for(int i = 1 ; i < addressList.size() ; i++) {
				
				address = addressList.get(i).getCity()+addressList.get(i).getTownship()+addressList.get(i).getStreet();
				Double distance = SearchHelper.getDistance(addressList.get(0), addressList.get(i));
				BigDecimal roundedValue = new BigDecimal(distance).setScale(5, RoundingMode.HALF_UP);
				if (roundedValue.compareTo(BigDecimal.valueOf(2.0))< 0) {
					newAddressList.add(addressList.get(i));
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
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newAddressList;

	}
	
	public List<Address> GetDurationAndDistanceGoogleAPI2(List<Address> addressList) {
		
		String encodedOrigin;
		List<Address> newAddressList = new ArrayList<>();
		
		try {
			String address = addressList.get(0).getCity()+addressList.get(0).getTownship()+addressList.get(0).getStreet();
			encodedOrigin = java.net.URLEncoder.encode( address ,"UTF-8");
			
			for(int i = 1 ; i < addressList.size() ; i++) {
				
				address = addressList.get(i).getCity()+addressList.get(i).getTownship()+addressList.get(i).getStreet();

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
					
					if (distance.getInt("value") <1500 && duration.getInt("value") < 3600) {
						newAddressList.add(addressList.get(i));
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
		
		return newAddressList;

	}
	
}
