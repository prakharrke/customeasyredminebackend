package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import customeasyredmineexception.CustomEasyRedmineException;
import net.sf.json.JSONObject;
import usersession.UserSession;

/**
 * Servlet implementation class Authentication
 */
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authentication() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		
		JSONObject objectDetails = new JSONObject();
		String userDetailsString = request.getParameter("userDetails");
		JSONObject userDetails = new JSONObject();
		userDetails = new Gson().fromJson(userDetailsString, JSONObject.class);
		String username = userDetails.getString("username");
		String password = userDetails.getString("password");
		UserSession userSession = new UserSession();
		if(userDetails.containsKey("sessionID")) {
			
		}else {
		try {
			JSONObject responseObject = userSession.getEasyRedmineCommunication().basicAuthLogin(username, password);
			//int redmineResponseCode = responseObject.getInt("status");
			
//				String headers = responseObject.getString("headers");
//				System.out.println(headers);
//				
//				String setCookie = new Gson().fromJson(headers, JSONObject.class).getString("Set-Cookie");
//				System.out.println(setCookie);
//				
//				response.setHeader("Set-Cookie", setCookie.split(";", 1)[0]);
//				String projectsString = responseObject.getString("projects");
//				out.println(projectsString);
				
				String body = responseObject.getString("body");
				String meta = responseObject.getString("meta");
				JSONObject metaObject = new Gson().fromJson(meta, JSONObject.class);
				Iterator i = metaObject.keys();
				while(i.hasNext()) {
					
					String header = i.next().toString();
					//response.setHeader(header, metaObject.getString(header));
					if(header.equalsIgnoreCase("Set-Cookie")) {
						String domain = request.getParameter("domain");
						String cookieValue = metaObject.getString(header);
						String redmineSessionID = cookieValue.split(";")[0].split("=")[1];
						Cookie cookie = new Cookie("_redmine_session", redmineSessionID.trim());
						cookie.setPath("/customeasyredmine");
						 //cookie.setDomain("localhost:3000");
						cookie.setMaxAge(60*60*2);
						cookie.setHttpOnly(true);
						response.addCookie(cookie);
						//response.setHeader();
						response.addHeader("Set-Cookie", "_redmine_session=" + redmineSessionID);
						PrintWriter out = response.getWriter();
						out.println(body);
					}
				}
				
		} catch (CustomEasyRedmineException e) {
			// TODO Auto-generated catch block
			throw new ServletException(e.getMessage());
		}
		}
		
	}

}
