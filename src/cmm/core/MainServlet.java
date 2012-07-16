package cmm.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import cmm.data.DataCenter;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<body>");
	    out.print("<a href=");
	    out.print( Facebook.getLoginRedirectURL());
	    out.print(">LINK </a>");
	    out.println("</body>");
	    out.println("</html>");
	    
	    response.sendRedirect(Facebook.getLoginRedirectURL());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//	    out.println("<html>");
//	    out.println("<body>");
//	    out.print("<a href=>");
//	    out.print( Facebook.getLoginRedirectURL() );
//	    out.println("</body>");
//	    out.println("</html>");
	    
		String name = null;
		String pw = null;
		String gender = null;
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	    System.out.println("request: "+request);
	    
	    if (!isMultipart) {
	    	name = request.getParameter("userName");
			System.out.println(name + "SLAYERUSERNAME");
			pw = request.getParameter("userPassword");
			gender = request.getParameter("gender");
	    } else {
	    	FileItemFactory factory = new DiskFileItemFactory();
	    	ServletFileUpload upload = new ServletFileUpload(factory);
	    	List items = null;

	    	try {
	    		items = upload.parseRequest(request);
	    		System.out.println("items: "+items);
	    	} catch (FileUploadException e) {
	    		e.printStackTrace();
	    	}
	    	Iterator itr = items.iterator();
	    	while (itr.hasNext()) {
	    		FileItem item = (FileItem) itr.next();
	    		if (item.isFormField()){
	    			String fieldname = item.getFieldName();
	    			if(fieldname.equals("userName")){
	    				String value = item.getString();
	    				fieldname = value;
	    			}else if(fieldname.equals("userPassword")){
	    				String value = item.getString();
	    				pw = value;
	    			}
	    		}
	    	}
	    }
	    
	    if(name != null && pw != null && gender != null){
	    	DataCenter.getInstance().setPw(pw);
	    	DataCenter.getInstance().setUsername(name);
	    	DataCenter.getInstance().setGender(gender);
	    	response.sendRedirect(Facebook.getLoginRedirectURL());
	    }else{
			PrintWriter out = response.getWriter();
		    out.println("<html>");
		    out.println("<body>");
		    out.print("<p>");
		    out.print( "ENTER SOME USERDATA" );
		    out.print("</p>");
		    out.println("</body>");
		    out.println("</html>");
	    }
		
	   
	}

}
