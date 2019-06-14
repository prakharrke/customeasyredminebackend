package easyredminecommunication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.google.gson.Gson;

import customeasyredmineexception.CustomEasyRedmineException;
import net.sf.json.JSONObject;
import usersession.UserSession;

public class EasyRedmineCommunication {
	private String MIListnerString = "http://host.docker.internal:9090/MI45QA14/eQHTTPListener";
	
	UserSession userSession = null;
	
	public EasyRedmineCommunication(UserSession userSession) {
		this.userSession = userSession; 
	}
	
	public JSONObject basicAuthLogin(String username, String password, String authenticityToken) throws CustomEasyRedmineException {
		
		String url = MIListnerString + "?easyredmine=easyredmine&loc=login&method=POST";
		
		try {
		URL urlObject = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
		String userCredentials = username + ":" + password;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

		con.setRequestProperty ("username", username);
		con.setRequestProperty ("password", password);
		con.setRequestProperty ("authenticity_token", authenticityToken);
		con.setRequestProperty ("RedminSession", userSession.getRedmineSessionID());
		//con.setRequestProperty ("Cookie", "");
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
	
	public JSONObject tokenAuthLogin(String redmineSessionID, String username) throws CustomEasyRedmineException {
		
		String url = MIListnerString + "?easyredmine=easyredmine&loc=projects.json?limit=100";
		try {
		URL urlObject = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
		String userCredentials = username + ":" + "";
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
		con.setRequestProperty ("Authorization", basicAuth);
		con.setRequestProperty ("RedminSession", redmineSessionID.trim());
		
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
	
	
	public JSONObject autoCompleteProjectSearch(String autoCompleteString) throws CustomEasyRedmineException {
		String url = MIListnerString + "?easyredmine=easyredmine&loc=easy_autocompletes/my_projects?term=" + autoCompleteString;
		
		try {
			URL urlObject = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
			//String userCredentials = username + ":" + "";
			//String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			//con.setRequestProperty ("Authorization", basicAuth);
			con.setRequestProperty ("RedminSession",this.userSession.getRedmineSessionID());
			
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
	
	public JSONObject getProjectIssues(String projectID) throws CustomEasyRedmineException {
		String url = MIListnerString + "?easyredmine=easyredmine&loc=projects/" + projectID + "/issues.json?limit=100";
		try {
			URL urlObject = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
			//String userCredentials = username + ":" + "";
			//String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			//con.setRequestProperty ("Authorization", basicAuth);
			con.setRequestProperty ("RedminSession",this.userSession.getRedmineSessionID());
			
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
	
	
	public JSONObject getIssues(String locationURL) throws CustomEasyRedmineException {
		String url = MIListnerString + "?easyredmine=easyredmine&loc=" + locationURL + ".json";
		try {
			URL urlObject = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
			//String userCredentials = username + ":" + "";
			//String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			//con.setRequestProperty ("Authorization", basicAuth);
			con.setRequestProperty ("RedminSession",this.userSession.getRedmineSessionID());
			
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
	
	public JSONObject getIssueDetails(String locationURL) throws CustomEasyRedmineException {
		String url = MIListnerString + "?easyredmine=easyredmine&loc=" + locationURL;
		try {
			URL urlObject = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
			//String userCredentials = username + ":" + "";
			//String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			//con.setRequestProperty ("Authorization", basicAuth);
			con.setRequestProperty ("RedminSession",this.userSession.getRedmineSessionID());
			
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
	
	public JSONObject downloadAttachment(String locationURL) throws CustomEasyRedmineException {
		String url = MIListnerString + "?easyredmine=easyredmine&loc=" + locationURL;
		try {
			URL urlObject = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
			//String userCredentials = username + ":" + "";
			//String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			//con.setRequestProperty ("Authorization", basicAuth);
			con.setRequestProperty ("RedminSession",this.userSession.getRedmineSessionID());
			
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
