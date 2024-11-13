package helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchHelper {
	
	//將地址切割成縣市 , 鄉鎮區 , 街路號
	public static String[] splitCityTownStreet(String address) {
		
		String[] Parts = new String[3];

		if (address.indexOf("台灣") > -1 && address.indexOf("台灣大道") == -1) {
			address = address.split("台灣")[1];
		}
		
		//City
		Parts[0] = address.substring(0, 3);
		address = address.substring(3);
		
		//town
		if(address.indexOf("市")>-1) {
			Parts[1] = address.split("市")[0] + "市";
			Parts[2] = address.split("市")[1];
		}
		else if (address.indexOf("區")>-1) {
			Parts[1] = address.split("區")[0] + "區";
			Parts[2] = address.split("區")[1];
		}else if(address.indexOf("鎮區")>-1){
			Parts[1] = address.split("區")[0] + "區";
			Parts[2] = address.split("區")[1];
		}else if(address.indexOf("鄉")>-1) {
			Parts[1] = address.split("鄉")[0] + "鄉";
			Parts[2] = address.split("鄉")[1];
		}else if(address.indexOf("鎮")>-1) {
			Parts[1] = address.split("鎮")[0] + "鎮";
			Parts[2] = address.split("鎮")[1];
		}
		
		return Parts;
		
	}

	public static StringBuilder urlConnection(String urlString) throws IOException {
		
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

	public static List<String> openfileRead(String filePath){
		
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
}
