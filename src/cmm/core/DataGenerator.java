package cmm.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import cmm.comm.JsonReader;
import cmm.data.ProfileCard;




public class DataGenerator {


	public static void main(String[] args){
		DataGenerator data = new DataGenerator();
		data.createFirstDegree(1454377014,"");
	}
	
	public void createFirstDegree(int pUserId, String pToken){
		JSONObject jsonObject;
		//String url = "http://www.facebook.com/ajax/typeahead/search/first_degree.php?__a=1&filter=user&viewer="+pUserId+"&token="+pToken+"&stale_ok=0" ;
		String url = "http://www.facebook.com/ajax/typeahead/search/first_degree.php?__a=1454377014&filter=user&viewer=1454377014&token=&stale_ok=0";
		try {
			JsonReader.cutMalformedJSON(url);
			
			//jsonObject = JsonReader.readJsonFromUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public ProfileCard createProfileCard(String pUserName){
		ProfileCard ret = new ProfileCard();
		JSONObject jsonObject;
		
		String url = "https://graph.facebook.com/" + pUserName + "/";
		
		try {
			jsonObject = JsonReader.readJsonFromUrl(url);
			String id = jsonObject.getString("id");
			int idNumber = Integer.parseInt(id);
			String name = jsonObject.getString("name");		
			String firstName = jsonObject.getString("first_name");	
			String lastName = jsonObject.getString("last_name");	
			String link = jsonObject.getString("link");	
			String userName = jsonObject.getString("username");	
			String gender = jsonObject.getString("gender");	
			String locale = jsonObject.getString("locale");
			System.out.println(name);
			
			ret.setId(idNumber);
			ret.setName(name);
			ret.setFirstName(firstName);
			ret.setLastName(lastName);
			ret.setLink(link);
			ret.setUserName(userName);
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
