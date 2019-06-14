package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import net.sf.json.JSONObject;
import usersession.UserSession;

/**
 * Servlet implementation class TokenValidation
 */
public class TokenValidation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TokenValidation() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String authHeader = request.getHeader("Authorization");
		String requestJWS = "";
		if(authHeader!= null && authHeader.contains("Bearer")) {
		requestJWS = authHeader.split("Bearer ")[1];
		}
		
		UserSession userSession = null;
		String username = "";
		
		// * JWS IS PRESENT
					/*
					 * CASE 1 :  HTTPSESSION HAS EXPIRED AND THE SESSION IS NEW SESSION -> REDIRECT TO LOGIN PAGE
					 * CASE 2 : SESSION HAS NOT EXPIRED AND USERSESSION ATTRIBUTE IS PRESENT -> GET USERSESSION -> VALIDATE THE TOKEN
					 */
					if(session.getAttribute("userSession") == null) {
						
						// * REDIRECT TO LOGIN PAGE
						response.setStatus(401);
						PrintWriter out = response.getWriter();
						out.println("");
					}else {
						// * VALIDATE TOKEN
						userSession = (UserSession)session.getAttribute("userSession");
						try {
						 username = userSession.getJWTToken().validateJWTToken(requestJWS);
						 userSession.setUsername(username);
						 String redmineSessionID = userSession.getRedmineSessionID();
						 JSONObject responseObject =  userSession.getEasyRedmineCommunication().tokenAuthLogin(redmineSessionID, username);
						 String meta = responseObject.getString("meta");
						 JSONObject metaObject = new Gson().fromJson(meta, JSONObject.class);
						 
						 String status = metaObject.getString("Status");
						if(status.contains("200")) {
							Iterator i = metaObject.keys();
							String body = responseObject.getString("body");
							while(i.hasNext()) {
								
								String header = i.next().toString();
								//response.setHeader(header, metaObject.getString(header));
								if(header.equalsIgnoreCase("Set-Cookie")) {
									String domain = request.getParameter("domain");
									String cookieValue = metaObject.getString(header);
									String newRedmineSessionID = cookieValue.split(";")[0];
									newRedmineSessionID = newRedmineSessionID.substring(2, newRedmineSessionID.length());
									userSession.setRedmineSessionID(newRedmineSessionID.trim());
									response.setStatus(200);
									PrintWriter out = response.getWriter();
									JSONObject bodyObject = new JSONObject();
									bodyObject = new Gson().fromJson(body, JSONObject.class);
									out.println(new Gson().toJson(bodyObject));
								}
							}
						}else {
							session.removeAttribute("userSession");
							response.setStatus(401);
							PrintWriter out = response.getWriter();
							out.println("");
							
						}
						 System.out.println();
						}catch(Exception e) {
							// * INVALID JWS
							response.setStatus(401);
							PrintWriter out = response.getWriter();
							out.println("");
						}
						
					}
		
	}

}
