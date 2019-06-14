package helper;

import com.google.gson.Gson;

import customeasyredmineexception.CustomEasyRedmineException;
import net.sf.json.JSONObject;
import usersession.UserSession;

public class Helper {

	UserSession userSession = null;
	
	public Helper(UserSession userSession) {
		this.userSession = userSession;
	}
	public void extractCookie(String meta) throws CustomEasyRedmineException {
		
		JSONObject metaObject = new Gson().fromJson(meta, JSONObject.class);
		 
		 String status = metaObject.getString("Status");
		if(status.contains("200")) {
			
			String cookieValue = metaObject.getString("Set-Cookie");
			String newRedmineSessionID = cookieValue.split(";")[0];
			newRedmineSessionID = newRedmineSessionID.substring(2, newRedmineSessionID.length());
			userSession.setRedmineSessionID(newRedmineSessionID);
		}else {
			throw new CustomEasyRedmineException("401");
			
		}
	}
}
