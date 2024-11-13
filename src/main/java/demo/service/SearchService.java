package demo.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import demo.model.AddressTableBean;
import demo.repository.SearchRepository;
import helper.SearchHelper;

@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepo;
	
	@Value("${google.api.key}")
	private String apiKey;
	
	public List<AddressTableBean> findAll() {
		return searchRepo.findAll();
	}
	
	public List<AddressTableBean> findByCity(String city){
		return searchRepo.findByCity(city);
	}
	
	public List<AddressTableBean> findByKeyWord(String name){
		return searchRepo.findByKeyWord(name);
	}
	
	public List<AddressTableBean> addressUpdateAll(List<AddressTableBean> addressList) {
		return searchRepo.saveAll(addressList);
	}
	
	public List<AddressTableBean> updateFakeAddress(String filePath,List<AddressTableBean> addressList){
		
		List<String> lists = SearchHelper.openfileRead(filePath);
		
		for (int i = 0; i < addressList.size(); i++) {
			
			String[] Parts = SearchHelper.splitCityTownStreet(lists.get(i));
			addressList.get(i).setCity(Parts[0]);
			addressList.get(i).setTownship(Parts[1]);
			addressList.get(i).setStreet(Parts[2]);
		}
		
		return addressList;
	}
	
	public List<AddressTableBean> getLatAndLngGoogleAPI(List<AddressTableBean> addressList) {
		
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
	
	public AddressTableBean placeConvertToAdress(String origin) {
		
		String encodedAddress;
		AddressTableBean address = new AddressTableBean();
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
}
