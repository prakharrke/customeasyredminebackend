package usersession;

import easyredminecommunication.EasyRedmineCommunication;

public class UserSession {
	String username = "";
	EasyRedmineCommunication easyRedmineCommunication = null;
	String redmineSessionID = "";
	public UserSession() {
		
		easyRedmineCommunication = new EasyRedmineCommunication();
		
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
}
