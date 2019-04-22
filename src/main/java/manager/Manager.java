package manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import usersession.UserSession;

public class Manager implements Map<String, UserSession>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Map<String, UserSession> userSessionMap = new HashMap<String, UserSession>();
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return userSessionMap.containsKey(key);
	}
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}
	public Set<Entry<String, UserSession>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	public UserSession get(Object key) {
		// TODO Auto-generated method stub
		return userSessionMap.get(key);
	}
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return userSessionMap.isEmpty();
	}
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}
	public UserSession put(String key, UserSession value) {
		userSessionMap.put(key, value);
		return null;
	}
	public void putAll(Map<? extends String, ? extends UserSession> m) {
		// TODO Auto-generated method stub
		
	}
	public UserSession remove(Object key) {
		// TODO Auto-generated method stub
		return userSessionMap.remove(key);
	}
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Collection<UserSession> values() {
		
		return null;
	}

	

}
