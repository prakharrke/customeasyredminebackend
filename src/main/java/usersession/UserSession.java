package usersession;

import easyredminecommunication.EasyRedmineCommunication;
import helper.Helper;
import jwt.JWTTokenCreation;

public class UserSession {
	String username = "";
	EasyRedmineCommunication easyRedmineCommunication = null;
	String redmineSessionID = "";
	JWTTokenCreation jwtToken = null;
	String basicAuth = "";
	Helper helper = null;
	public UserSession(String username) {
		
		easyRedmineCommunication = new EasyRedmineCommunication(this);
		jwtToken = new JWTTokenCreation();
		this.username = username;
		this.helper = new Helper(this);
	}
	
	public UserSession() {
		
		easyRedmineCommunication = new EasyRedmineCommunication(this);
		jwtToken = new JWTTokenCreation();
		this.helper = new Helper(this);
		
	}
	
	public EasyRedmineCommunication getEasyRedmineCommunication() {
		return this.easyRedmineCommunication;
	}
	
	public void setRedmineSessionID(String sessionID) {
		this.redmineSessionID = sessionID;
	}
	public String getRedmineSessionID() {
		return redmineSessionID;
	}
	public JWTTokenCreation getJWTToken() {
		return jwtToken;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return this.username;
	}
	public void setBasicAuth(String basicAuth) {
		this.basicAuth = basicAuth;
	}
	public String getBasicAuth() {
		return this.basicAuth;
	}
	
	public Helper getHelper() {
		return this.helper;
	}
}
