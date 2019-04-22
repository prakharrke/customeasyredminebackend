package easyredminecommunication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.google.gson.Gson;

import customeasyredmineexception.CustomEasyRedmineException;
import net.sf.json.JSONObject;

public class EasyRedmineCommunication {
	private String MIListnerString = "http://dt01070403.technologic.com:9090/MI442QA5/eQHTTPListener";
	
	public JSONObject basicAuthLogin(String username, String password) throws CustomEasyRedmineException {
		
		String url = MIListnerString + "?easyredmine=easyredmine";
		try {
		URL urlObject = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
		String userCredentials = username + ":" + password;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

		con.setRequestProperty ("Authorization", basicAuth);
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		if(responseCode!=500) {
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONObject responseBody = new Gson().fromJson(response.toString(), JSONObject.class);
		//int redmineResponseCode = responseBody.getInt("status");
		return responseBody;
		}else {
			throw new CustomEasyRedmineException("eQubeMI Trigger error. Please contact administrator");
		}
		}catch(Exception e) {
			throw new CustomEasyRedmineException(e.getMessage());
		}
	}
}
