package cmm.comm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	  System.out.println("URL ="+url.toString());
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
	public static void cutMalformedJSON(String pUrl) throws MalformedURLException, IOException{
		InputStream is;
		is = new URL(pUrl).openStream();

		 String badString = "";
		 try {
			 is = new URL(pUrl).openStream();
			 BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			 badString = readAll(rd);
			 
			 System.out.println(badString);
			 
			 String loopexpr = "for (;;);{\"__ar\":1,\"payload\":{\"entries\":[";
			 
			 if(badString.startsWith(loopexpr)){
				 badString = badString.substring(loopexpr.length());
			 }
			 
			 while(badString.contains("},{")){
				 
			 }
			 
			 String[] toplist = badString.split("},{");
			
			int size = toplist.length;
			int halt = 0; 
	    } finally {
	    	is.close();
	    }
		
		
	   
		
	}
}
