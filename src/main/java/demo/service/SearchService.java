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

import demo.model.House;
import demo.repository.SearchRepository;

@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepo;
	
	@Value("${google.api.key}")
	private String apiKey;
	
	public List<House> findAll() {
		return searchRepo.findAll();
	}
	
	public List<House> houseUpdateFakeAdress(List<House> houseList , List<String> fakeAddress) {
		for (int i = 0; i < houseList.size(); i++) {
			
			String[] Parts = fakeAddress.get(i).split("市|區");
			if (fakeAddress.get(i).indexOf("新市區") > -1) {
				Parts[1] += "市";
				Parts[2] = Parts[3];
			}
			
			houseList.get(i).setCity(Parts[0] + "市");
			houseList.get(i).setTownship(Parts[1] + "區");
			houseList.get(i).setStreet(Parts[2]);
		}
		
		return searchRepo.saveAll(houseList);
	}
	
	public List<String> fakeAddress(String filePath){
		
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
	
	
	public List<double[]> getLatAndLonGoogleAPI(List<House> houseList) {
		
		List<double[]> posList = new ArrayList<>();
		houseList.forEach(house->{
			
			String address = house.getCity() + house.getTownship() + house.getStreet();
			try {
				String encodedAddress = java.net.URLEncoder.encode(address,"UTF-8");
				String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
                        + encodedAddress + "&key=" + apiKey;
				
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
				
				JSONObject json = new JSONObject(content.toString());
				if("OK".equals(json.getString("status"))) {
					JSONObject location = json.getJSONArray("results")
											.getJSONObject(0)
											.getJSONObject("geometry")
											.getJSONObject("location");
					double[] pos =  new double[] {location.getDouble("lat"),location.getDouble("lng")};
					posList.add(pos);
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
			
			
		});
		
		return posList;
	}
	
}
