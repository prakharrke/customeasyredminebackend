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
		
		
		
		HttpSession session = request.getSession();
		JSONObject objectDetails = new JSONObject();
		String userDetailsString = request.getParameter("userDetails");
		JSONObject userDetails = new JSONObject();
		userDetails = new Gson().fromJson(userDetailsString, JSONObject.class);
		String username = userDetails.getString("username");
		String password = userDetails.getString("password");
		
		String authenticityToken = "";
		if(userDetails.containsKey("authenticity_token"))
			authenticityToken = userDetails.getString("authenticity_token");
		
		UserSession userSession = null;


		try {
				if(session.getAttribute("userSession") == null) {
					userSession = new UserSession(username);
					session.setAttribute("userSession", userSession);
				}
				else {
					userSession = (UserSession) session.getAttribute("userSession");
				}
			

				JSONObject responseObject = userSession.getEasyRedmineCommunication().basicAuthLogin(username, password, authenticityToken);
				//String body = responseObject.getString("body");
				String meta = responseObject.getString("meta");
				JSONObject metaObject = new Gson().fromJson(meta, JSONObject.class);
				Iterator i = metaObject.keys();
				
				// * CHECK FOR STATUS HERE. IF STATUS IS 200, ONLY THEN, LET IT PASS
				String status = metaObject.getString("Status");
				if(status.contains("302")) {
				while(i.hasNext()) {
					
					String header = i.next().toString();
					//response.setHeader(header, metaObject.getString(header));
					if(header.equalsIgnoreCase("Set-Cookie")) {
						String domain = request.getParameter("domain");
						String cookieValue = metaObject.getString(header);
//						String redmineSessionID = cookieValue.split(";")[0];
//						redmineSessionID = redmineSessionID.substring(2, redmineSessionID.length());
						cookieValue = cookieValue.substring(2, cookieValue.length()-2);
						userSession.setRedmineSessionID(cookieValue.trim());
						
						// * CREATE JWT TOKEN 
						String jws = userSession.getJWTToken().createJWTToken(username);
						response.setStatus(200);
						PrintWriter out = response.getWriter();
						JSONObject bodyObject = new JSONObject();
						//bodyObject = new Gson().fromJson(body, JSONObject.class);
						bodyObject.put("jwt", jws);
						out.println(new Gson().toJson(bodyObject));
						
					}
				}
			}else {
				session.removeAttribute("userSession");
				response.setStatus(401);
				PrintWriter out = response.getWriter();
				out.println("");
				
			}
				
		} catch (CustomEasyRedmineException e) {
			// TODO Auto-generated catch block
			throw new ServletException(e.getMessage());
		}
		
		
	}

}
