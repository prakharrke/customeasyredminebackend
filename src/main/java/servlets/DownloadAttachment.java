package servlets;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class DownloadAttachment
 */
public class DownloadAttachment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadAttachment() {
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
		userSession = (UserSession)session.getAttribute("userSession");
//		if(userSession == null) {
//			response.setStatus(401);
//			PrintWriter out = response.getWriter();
//			out.println("");
//			return;
//		}
		
		if(userSession == null) {
			userSession = new UserSession();
		}
		String url = request.getParameter("url");
		try {
			//String username = userSession.getJWTToken().validateJWTToken(requestJWS);
			JSONObject issuesObject = userSession.getEasyRedmineCommunication().downloadAttachment(url);
			String meta = issuesObject.getString("meta");
			JSONObject metaObject = new Gson().fromJson(meta, JSONObject.class);
			//userSession.getHelper().extractCookie(meta);
			String body = issuesObject.getString("body");
			response.addHeader("Content-Disposition", metaObject.getString("Content-Disposition").substring(2, metaObject.getString("Content-Disposition").length()-2));
			response.addHeader("Content-Type", metaObject.getString("Content-Type").substring(2, metaObject.getString("Content-Type").length()-2));
			//response.addHeader("Access-Control-Expose-Headers", "Access-Token, Uid");
			response.setStatus(200);
			PrintWriter out = response.getWriter();
			JSONObject resp = new JSONObject();
			resp.put("file", body);
			resp.put("fileName", metaObject.getString("Content-Disposition").substring(2, metaObject.getString("Content-Disposition").length()-2).split(";")[1].split("=")[1]);
			out.println(new Gson().toJson(resp));
		} catch (CustomEasyRedmineException e) {
			
			response.setStatus(401);
			PrintWriter out = response.getWriter();
			out.println("");
			
		}
	}
	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		PrintWriter out = response.getWriter();
		response.setStatus(200);
		out.print("");
	}

}
