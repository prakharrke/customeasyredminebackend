package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import customeasyredmineexception.CustomEasyRedmineException;
import net.sf.json.JSONObject;
import usersession.UserSession;

/**
 * Servlet implementation class AutoCompleteProjects
 */
public class AutoCompleteProjects extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoCompleteProjects() {
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
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String authHeader = request.getHeader("Authorization");
		String requestJWS = "";
		if(authHeader!= null && authHeader.contains("Bearer")) {
		requestJWS = authHeader.split("Bearer ")[1];
		}
		
		UserSession userSession = null;
		userSession = (UserSession)session.getAttribute("userSession");
		/*if(userSession == null) {
			response.setStatus(401);
			PrintWriter out = response.getWriter();
			out.println("");
			return;
		}*/
		if(userSession == null)
			userSession = new UserSession();
		String autoCompleteString = request.getParameter("autoCompleteString");
		try {
			//String username = userSession.getJWTToken().validateJWTToken(requestJWS);
			JSONObject autoCompleteProjects = userSession.getEasyRedmineCommunication().autoCompleteProjectSearch(autoCompleteString);
			String meta = autoCompleteProjects.getString("meta");
			//userSession.getHelper().extractCookie(meta);
			String body = autoCompleteProjects.getString("body");
			response.setStatus(200);
			PrintWriter out = response.getWriter();
			out.println(body);
		} catch (CustomEasyRedmineException e) {
			
			response.setStatus(401);
			PrintWriter out = response.getWriter();
			out.println("");
			
		}
		
	}

}
