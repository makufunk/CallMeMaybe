package cmm.data;

public class DataCenter {

	private static DataCenter instance = null;
	
	private String username;
	private String pw;
	private String gender;

    public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	private DataCenter() {}
 

    public static DataCenter getInstance() {
        if (instance == null) {
            instance = new DataCenter();
        }
        return instance;
    }


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPw() {
		return pw;
	}


	public void setPw(String pw) {
		this.pw = pw;
	}
    

}
