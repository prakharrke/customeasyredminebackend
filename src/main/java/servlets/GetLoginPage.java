package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import easyredminecommunication.EasyRedmineCommunication;
import net.sf.json.JSONObject;
import usersession.UserSession;

/**
 * Servlet implementation class GetLoginPage
 */
public class GetLoginPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLoginPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "http://host.docker.internal:9090/MI45QA14/eQHTTPListener?easyredmine=easyredmine&loc=login";
		HttpSession session = request.getSession();
		UserSession userSession = new UserSession();
		if(session.getAttribute("userSession")!=null) {
			session.removeAttribute("userSession");
		}
		
		session.setAttribute("userSession", userSession);
		try {
		URL urlObject = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
		
		
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		if(responseCode!=500) {
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response1 = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response1.append(inputLine);
		}
		in.close();
		JSONObject responseBody = new Gson().fromJson(response1.toString(), JSONObject.class);
		//int redmineResponseCode = responseBody.getInt("status");
		String meta = responseBody.getString("meta");
		JSONObject metaObject = new Gson().fromJson(meta, JSONObject.class);
		Iterator i = metaObject.keys();
		String body = responseBody.getString("body");
		String status = metaObject.getString("Status");
			while(i.hasNext()) {
				
				String header = i.next().toString();
				//response.setHeader(header, metaObject.getString(header));
				if(header.equalsIgnoreCase("Set-Cookie")) {
					String domain = request.getParameter("domain");
					String cookieValue = metaObject.getString(header);
//					String redmineSessionID = cookieValue.split(";")[0];
//					redmineSessionID = redmineSessionID.substring(2, redmineSessionID.length());
					cookieValue = cookieValue.substring(2, cookieValue.length()-2);
					userSession.setRedmineSessionID(cookieValue);
				}
			}
			
		PrintWriter out = response.getWriter();
		out.println(body);
		}
		
		}catch(Exception e) {
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
