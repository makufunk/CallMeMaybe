package cmm.core;

import com.visural.common.StringUtil;
//main facebook connection object
public class Facebook {
    // get these from your FB Dev App
    private static final String api_key = "253437074766683";
    private static final String secret = "8694c9e2585284036324b2a27116a3fd";
    private static final String client_id = "253437074766683";  

    // set this to your servlet URL for the authentication servlet/filter
    private static final String redirect_uri = "http://localhost:8080/CallMeMaybe/fbauth";
    /// set this to the list of extended permissions you want
    private static final String[] perms = new String[] {"publish_stream", "email",
    	"friends_likes","user_likes","friends_relationships","user_relationships"};

    public static String getAPIKey() {
        return api_key;
    }

    public static String getSecret() {
        return secret;
    }

    public static String getLoginRedirectURL() {
        return "https://graph.facebook.com/oauth/authorize?client_id=" +
            client_id + "&display=page&redirect_uri=" +
            redirect_uri+"&scope="+StringUtil.delimitObjectsToString(",", perms);
    }

    public static String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id=" +
            client_id+"&redirect_uri=" +
            redirect_uri+"&client_secret="+secret+"&code="+authCode;
    }
}