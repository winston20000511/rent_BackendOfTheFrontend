package com.example.demo.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.example.demo.dto.KeyWordDTO;
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
	private static final double earthRadiusKm = 6378.137;

	private final String[][] rentPriceRanges = {
			{"台北市", "15000", "30000"}, // 最低價格, 最高價格
			{"新北市", "12000", "25000"},
			{"桃園市", "10000", "20000"},
			{"台中市", "9000", "18000"},
			{"台南市", "8000", "15000"},
			{"高雄市", "8000", "16000"},
			{"基隆市", "8500", "14000"},
			{"新竹市", "14000", "22000"},
			{"新竹縣", "13000", "21000"},
			{"苗栗縣", "7000", "12000"},
			{"彰化縣", "7500", "13000"},
			{"南投縣", "7000", "11000"},
			{"雲林縣", "6500", "10000"},
			{"嘉義市", "8500", "14000"},
			{"嘉義縣", "7500", "13000"},
			{"屏東縣", "8000", "14000"},
			{"宜蘭縣", "8500", "16000"},
			{"花蓮縣", "9000", "17000"},
			{"台東縣", "8500", "14000"},
			{"澎湖縣", "7500", "12000"},
			{"金門縣", "8000", "13000"},
			{"連江縣", "7000", "11000"}
	};
	
	
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
	public boolean isPointInPolygon(double lat, double lng, List<DrawLatLngDTO> polygon) {
		int n = polygon.size();
		boolean inside = false;

		for (int i = 0, j = n - 1; i < n; j = i++) {
			double lat1 = polygon.get(i).getLat(), lng1 = polygon.get(i).getLng();
			double lat2 = polygon.get(j).getLat(), lng2 = polygon.get(j).getLng();

			boolean intersect = ((lng1 > lng) != (lng2 > lng)) &&
					(lat < (lat2 - lat1) * (lng - lng1) / (lng2 - lng1) + lat1);
			if (intersect) inside = !inside;
		}

		return inside;
	}

	//計算地區平均價格
	public Integer getPlaceAvgPrice(HashSet<AddressDTO> setAddressDTO) {
		
		
		int sum = 0;
		for (AddressDTO a : setAddressDTO) {
			sum += a.getPrice();
		}
		BigDecimal bd = BigDecimal.ZERO;
		if (setAddressDTO.size() != 0){
			bd = new BigDecimal(sum / setAddressDTO.size()).setScale(0, RoundingMode.HALF_UP);
		}

		return bd.intValue();

	}
	
	public DrawLatLngDTO getAvgLatLng(List<DrawLatLngDTO> drawDtoList) {
		
		double x = 0;
		double y = 0;
		double z = 0;

		for(DrawLatLngDTO drawDto : drawDtoList){
			double latRed = Math.toRadians(drawDto.getLat());
			double lngRed = Math.toRadians(drawDto.getLng());
			x += Math.cos(latRed) * Math.cos(lngRed);
			y += Math.cos(latRed) * Math.sin(lngRed);
			z += Math.sin(latRed);
		}

		int totalPoints = drawDtoList.size();
		x /= totalPoints;
		y /= totalPoints;
		z /= totalPoints;

		double centerLng = Math.toDegrees(Math.atan2(y, x));
		double centerLat = Math.toDegrees(Math.atan2(z,Math.sqrt(x*x+y*y)));
		return new DrawLatLngDTO(centerLat, centerLng);

	}
	public int getRandomHousePrice(String address){
		Random random = new Random();
		int minPrice=0;
		int gapPrice=0;
		for(int i = 0 ; i < rentPriceRanges.length ; i++){
			if (rentPriceRanges[i][0].equals(address)){
				minPrice = Integer.valueOf(rentPriceRanges[i][1]);
				gapPrice = Integer.valueOf(rentPriceRanges[i][2]) - minPrice;
				break;
			}
		}
		return minPrice + random.nextInt(gapPrice);
	}

	public boolean containsByAddress(HashSet<AddressDTO> setAddressDTO , AddressDTO addressDTO){
		return setAddressDTO.stream().anyMatch(a -> a.getAddress().equals(addressDTO.getAddress()));
	}

}
