package com.example.demo.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.DrawLatLngDTO;

@Component
public class SearchHelper {
	
	@Autowired
	private GoogleApiConfig googleApiConfig;
	
	//地球半徑 單位公里
	private static final double earthRadiusKm = 6371.0; 
	
	
	//將地址切割成縣市 , 鄉鎮區 , 街路號
	public String[] splitCityTown(String address) {
		
		String[] Parts = new String[3];

		if (address.indexOf("台灣") > -1 && address.indexOf("台灣大道") == -1) {
			address = address.split("台灣")[1];
		}
		
		//City
		Parts[0] = address.substring(0, 3);
		address = address.substring(3);
		
		//town
		if(address.indexOf("市區")>-1) {
			Parts[1] = address.split("市區")[0] + "市區";
		}else if(address.indexOf("鎮區")>-1){
			Parts[1] = address.split("鎮區")[0] + "鎮區";
		}else if(address.indexOf("市")>-1) {
			Parts[1] = address.split("市")[0] + "市";
		}else if (address.indexOf("區")>-1) {
			Parts[1] = address.split("區")[0] + "區";
		}else if(address.indexOf("鄉")>-1) {
			Parts[1] = address.split("鄉")[0] + "鄉";
		}else if(address.indexOf("鎮")>-1) {
			Parts[1] = address.split("鎮")[0] + "鎮";
		}
		
		return Parts;
		
	}

	//呼叫API 回傳JOSN字串格式
	public StringBuilder urlConnection(String urlString) throws IOException {
		
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuilder content = new StringBuilder();
		
		while((inputLine = in.readLine())!= null) {
			content.append(inputLine);
		}
		
		in.close();
		conn.disconnect();
		
		return content;
	}
	
	public JSONObject fetchGeocodeFromAPI(String address) throws IOException, JSONException {
		String encodedAddress;
		
		encodedAddress = java.net.URLEncoder.encode(address,"UTF-8");
		String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
                + encodedAddress + "&components=country:TW&bounds=21.5,119.5|25.5,122.5&language=zh-TW&key=" + googleApiConfig.getGoogleMapKey();
		StringBuilder content = urlConnection(urlString);
		return new JSONObject(content.toString());
	}
	public JSONObject fetchReverseGeocodingFromAPI(DrawLatLngDTO drawDto) throws IOException, JSONException{
		
		String encodeLatLng = drawDto.getLat() + "," + drawDto.getLng();
		String urlString="https://maps.googleapis.com/maps/api/geocode/json?latlng=" 
		+ encodeLatLng + "&language=zh-TW&key=" + googleApiConfig.getGoogleMapKey();
		StringBuilder content = urlConnection(urlString);
		return new JSONObject(content.toString());
	}
	
	public Optional<double[]> parseLatLng(JSONObject json) throws JSONException{
		
        if ("OK".equals(json.getString("status"))) {
            JSONObject location = json.getJSONArray("results")
                                      .getJSONObject(0)
                                      .getJSONObject("geometry")
                                      .getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            return Optional.of(new double[]{lat, lng});
        }
        return Optional.empty();
		
	}
	
	public Optional<String> parseAddress(JSONObject json) throws JSONException{
		
		if("OK".equals(json.getString("status"))) {
			JSONObject location = json.getJSONArray("results")
									.getJSONObject(0);
									
			String address = location.getString("formatted_address");
			return Optional.of(address);
		}
		return Optional.empty();
	}
	
	//開啟檔案
	public List<String> openfileRead(String filePath){
		
		List<String> lists = new ArrayList<String>();
		
		try (FileInputStream fis = new FileInputStream(filePath);
			InputStreamReader isr = new InputStreamReader(fis,"MS950");
			BufferedReader br = new BufferedReader(isr);)
		{
			String line;
			//line = br.readLine();
			while((line =br.readLine()) != null) {
				lists.add(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return lists;
	}
	
	//哈弗賽因公式計算
	public Double getDistance(AddressDTO Origin , AddressDTO Target) {
		
		//緯度&經度 角度轉成弧度
		double lat1Rad = Math.toRadians(Origin.getLat());
		double lng1Rad = Math.toRadians(Origin.getLng());
		double lat2Rad = Math.toRadians(Target.getLat());
		double lng2Rad = Math.toRadians(Target.getLng());
		
        //Haversine 公式
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLng = lng2Rad - lng1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        //計算距離
        return earthRadiusKm * c;
		
	}
	
	//計算地區平均價格
	public Integer getPlaceAvgPrice(HashSet<AddressDTO> setAddressDTO) {
		
		
		int sum = 0;
		for (AddressDTO a : setAddressDTO) {
			sum += a.getPrice();
		}
		
		BigDecimal bd = new BigDecimal(sum / setAddressDTO.size()).setScale(0, RoundingMode.HALF_UP);
		return bd.intValue();
		
	}
	
	public DrawLatLngDTO getAvgLatLng(List<DrawLatLngDTO> drawDtoList) {
		
		BigDecimal latBd = BigDecimal.ZERO;
		BigDecimal lngBd = BigDecimal.ZERO;
		
		for (DrawLatLngDTO draw : drawDtoList) {
			latBd = latBd.add(BigDecimal.valueOf(draw.getLat()));
			lngBd = lngBd.add(BigDecimal.valueOf(draw.getLng()));
			
		}
		
		latBd = latBd.divide(BigDecimal.valueOf(drawDtoList.size()), 4,RoundingMode.HALF_UP);
		lngBd = lngBd.divide(BigDecimal.valueOf(drawDtoList.size()),4,RoundingMode.HALF_UP);
		
		return new DrawLatLngDTO(latBd.doubleValue(),lngBd.doubleValue());

	}
	
}