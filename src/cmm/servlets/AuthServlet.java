package cmm.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmm.comm.UserService;
import cmm.core.DataGenerator;
import cmm.core.Facebook;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/fbauth")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String code = request.getParameter("code");
		String authURL = Facebook.getAuthURL(code);
		URL url = new URL(authURL);
        try {
            String result = readURL(url);
            String accessToken = null;
            Integer expires = null;
            String[] pairs = result.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length != 2) {
                    throw new RuntimeException("Unexpected auth response");
                } else {
                    if (kv[0].equals("access_token")) {
                        accessToken = kv[1];
                    }
                    if (kv[0].equals("expires")) {
                        expires = Integer.valueOf(kv[1]);
                    }
                }
            }
            if (accessToken != null && expires != null) {
                // do the shit right here
            	System.out.println(accessToken);
            	System.out.println(expires);
//            	DataGenerator generator = new DataGenerator();
//            	generator.createFirstDegree(1454377014, "");
            	
            	UserService us = new UserService();
            	us.authFacebookLogin(accessToken,expires);
            	
            } else {
                throw new RuntimeException("Access token and expires not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		
		
		PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<body>");
	    out.print( code );
	    out.print("<a href=>");
	    out.print( "test" );
	    out.println("</body>");
	    out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private String readURL(URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        int r;
        while ((r = is.read()) != -1) {
            baos.write(r);
        }
        return new String(baos.toByteArray());
    }
	
}
