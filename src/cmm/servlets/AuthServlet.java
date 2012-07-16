package cmm.servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cmm.comm.JsonReader;
import cmm.comm.UserService;
import cmm.core.Facebook;
import cmm.core.FirstDegreeObject;
import cmm.data.DataCenter;
import cmm.data.ProfileCard;
import cmm.data.UserFrontendObject;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/fbauth")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	
	private boolean foundKidInLastEntry = false;
       
	private double maxIndex = 10000;
	
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
		String accessToken = null;
        Integer expires = null;
        try {
            String result = readURL(url);
            
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

            	
            	UserService us = new UserService();
            	us.authFacebookLogin(accessToken,expires);
            	
            } else {
                throw new RuntimeException("Access token and expires not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        List<ProfileCard> familyList = searchForFamilyMembers(accessToken);
        
        final String female = "female";
        final String male   = "male";
        String genderToSearchFor = DataCenter.getInstance().getGender();
        boolean searchForGuys = false;
        boolean searchForGirls = false;
        if(genderToSearchFor.equals("Male")){
        	searchForGuys = true;
        }else if(genderToSearchFor.equals("Female")){
        	searchForGirls = true;
        }else if(genderToSearchFor.equals("Both")){
        	searchForGuys = true;
        	searchForGirls = true;
        }
        
        String user = DataCenter.getInstance().getUsername();
        String pw = DataCenter.getInstance().getPw();
        
        long id = getUserIdByUserName(user);
        
		List<UserFrontendObject> feList = new ArrayList<UserFrontendObject>();
        List<FirstDegreeObject> first = systemCallForFirstDegree(id,user,pw);
        for(int i = 0; i < first.size(); i++){
        	FirstDegreeObject current = first.get(i);
        	boolean isFamilyMember = false;
        	long uid = current.getUid();
        	
        	//filter family members
        	for(int j = 0; j < familyList.size(); j++){
        		if(familyList.get(j).getId() == uid){
        			isFamilyMember = true;
        			break;
        		}
        	}
        	if(!isFamilyMember){

        		ProfileCard card = createProfileCard(uid);
        		boolean badBecausKidsAndSuch = checkForKidsAndMarried(uid,accessToken);            	
            	if(card != null){
            	
            		boolean match = false;
            		if(searchForGirls){
            			if(female.equals(card.getGender())){
            				match = true;
            			}
            		}
            		if(searchForGuys){
            			if(male.equals(card.getGender())){
            				match = true;
            			}
            		}
    	        	if(match){
    	        		
    	        		//create matching percentage - first one is the best match
    	        		double currentIndex = current.getIndex();;
    	        		if( maxIndex == 10000 ){
    	        			maxIndex = (-1)*current.getIndex();
    	        		}
    	        		
    	        		UserFrontendObject feObj = new UserFrontendObject();
    	        		
    	        		feObj.setMatchLevel((int)((currentIndex*(-1)/maxIndex)*100));
    	        		System.out.println("MATCHING WITH PERSON " + card.getFirstName() + " "+ feObj.getMatchLevel() );
    	        		List<String> commonLikesList = calcCommonLikes(id, uid, accessToken);
    	        		feObj.setCommomLikes(commonLikesList);
    	            	feObj.setFirstName(card.getFirstName());
    	            	feObj.setLastName(card.getLastName());
    	            	feObj.setLinkToPic(current.getLinkToPic());
    	            	feObj.setIsMHB(badBecausKidsAndSuch);
    	            	feList.add(feObj);
    	        	}
    	        	
    	        	if(feList.size() >= 10){
    	        		break;
    	        	}
            	}
        	}
        }
        
        for(int j=0; j < feList.size(); j++){
        	System.out.println(feList.get(j).getFirstName() + feList.get(j).getLastName());
        }
        
        maxIndex = 10000;
		
        request.setAttribute("resultList",feList); 
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/results.jsp");
		dispatcher.forward(request, response); 
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
	
	public List<String> calcCommonLikes(long my_user_id, long friend_user_id, String access_token){
		
		List<String> ret = new ArrayList<String>();
		try {
			Runtime rt = Runtime.getRuntime();
	        //
			Process proc;
			
			proc = rt.exec("python /Users/makufunk/common_likes.py "+my_user_id+" "+friend_user_id+" "+access_token );
			
	        InputStream stdin = proc.getInputStream();
	        InputStreamReader isr = new InputStreamReader(stdin);
	        BufferedReader br = new BufferedReader(isr);
	        String line = null;
	        
	        
	        System.out.println("<OUTPUT_Likes>");
	        while ( (line = br.readLine()) != null){
	        	String oneLike = line;
	        	
	        	//System.out.println(line);
	
	        	ret.add(oneLike);
	        	
	        }
	        System.out.println("</OUTPUT_Likes>");
	        int exitVal = proc.waitFor();  
	        System.out.println("Process exitValue: " + exitVal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public long getUserIdByUserName(String pUserName){
		
		long ret = -1;
		JSONObject jsonObject;
		
		String url = "https://graph.facebook.com/"+pUserName;		
		try {				
			jsonObject = JsonReader.readJsonFromUrl(url);
			
			String idString = jsonObject.getString("id");
			ret = Long.parseLong(idString);

			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
		return ret;
	}
	
	public boolean  checkForKidsAndMarried(long pId, String access){
		boolean ret = false;
		JSONObject jsonObject;
		
		String url = "https://graph.facebook.com/"+pId+"/family?access_token="+access;		
		try {				
			jsonObject = JsonReader.readJsonFromUrl(url);
			
			
			JSONArray array = jsonObject.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject tmp = array.getJSONObject(i);
				String relString = tmp.getString("relationship");									
				if(relString.equals("daughter") || relString.equals("son") ){
					return true;
				}
			}

			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
		return ret;
	}
	

	private List<FirstDegreeObject> systemCallForFirstDegree(long pUserNumbers, String login, String pw){
		List<FirstDegreeObject> firstList = null;
		try {
			firstList = new ArrayList<FirstDegreeObject>();
			
			Runtime rt = Runtime.getRuntime();
            //
			Process proc = rt.exec("python /Users/makufunk/fbranks.py "+pUserNumbers+" "+login+" "+pw );
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            
            
            System.out.println("<OUTPUT>");
            while ( (line = br.readLine()) != null){
            	FirstDegreeObject obj = new FirstDegreeObject();
            	
            	System.out.println(line);
            	
            	String delimiter = ",";
            	/* given string will be split by the argument delimiter provided. */
            	String[] temp = line.split(delimiter);
            	String useridString = temp[0];
            	String indexString = temp[1];
            	String picPathString = temp[2];
            	
            	long userId = Long.parseLong(useridString);
            	double index = Double.parseDouble(indexString);
            	
            	obj.setIndex(index);
            	obj.setUid(userId);
            	obj.setLinkToPic(picPathString);
            	
            	firstList.add(obj);
            	
            }
            System.out.println("</OUTPUT>");
            int exitVal = proc.waitFor();  
            System.out.println("Process exitValue: " + exitVal);
			
			
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return firstList;
	}
	
	public List<ProfileCard>  searchForFamilyMembers(String pToken){
		List<ProfileCard>  ret = new ArrayList<ProfileCard>();
		JSONObject jsonObject;
		
		String url = "https://graph.facebook.com/me/family?access_token="+pToken;		
		if(!url.equals("false")){
			try {				
				jsonObject = JsonReader.readJsonFromUrl(url);
				
				
				JSONArray array = jsonObject.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					ProfileCard card = new ProfileCard();
					JSONObject tmp = array.getJSONObject(i);
					String idString = tmp.getString("id");									
					long id = Long.parseLong(idString);
					card.setId(id);
					ret.add(card);
				}

				System.out.println(array.toString());
				
				/*
				String id = jsonObject.getString("id");
				long idNumber = Long.parseLong(id);
				String name = jsonObject.getString("name");		
				String firstName = jsonObject.getString("first_name");	
				String lastName = jsonObject.getString("last_name");	
				//String userName = jsonObject.getString("username");	
				String gender = jsonObject.getString("gender");	
				String locale = jsonObject.getString("locale");
				System.out.println(name);
				
				ret.setId(idNumber);
				ret.setName(name);
				ret.setFirstName(firstName);
				ret.setLastName(lastName);
				//ret.setUserName(userName);
				ret.setGender(gender);
				ret.setLocale(locale);
				
				*/
				
			} catch (IOException e) {
				e.printStackTrace();
				ret = null;
			} catch (JSONException e) {
				e.printStackTrace();
				ret = null;
			} 
		}else{
			ret = null;
		}
		
		return ret;
	}
	
	
	public ProfileCard createProfileCard(long pUserId){
		ProfileCard ret = new ProfileCard();
		JSONObject jsonObject;
		
		String url = "https://graph.facebook.com/" + pUserId + "/";		
		System.out.println(pUserId);
		try {
			jsonObject = JsonReader.readJsonFromUrl(url);
			String id = jsonObject.getString("id");
			long idNumber = Long.parseLong(id);
			String name = jsonObject.getString("name");		
			String firstName = jsonObject.getString("first_name");	
			String lastName = jsonObject.getString("last_name");	
			//String userName = jsonObject.getString("username");	
			String gender = jsonObject.getString("gender");	
			String locale = jsonObject.getString("locale");
			System.out.println(name);
			
			ret.setId(idNumber);
			ret.setName(name);
			ret.setFirstName(firstName);
			ret.setLastName(lastName);
			//ret.setUserName(userName);
			ret.setGender(gender);
			ret.setLocale(locale);
			
		} catch (IOException e) {
			e.printStackTrace();
			ret = null;
		} catch (JSONException e) {
			e.printStackTrace();
			ret = null;
		} 

		
		return ret;
	}
}
