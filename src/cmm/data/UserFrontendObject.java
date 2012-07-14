package cmm.data;

public class UserFrontendObject {
	private String firstName;
	private String lastName;
	private int matchLevel;
	private String linkToPic;
	private String[] commomLikes;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getMatchLevel() {
		return matchLevel;
	}
	public void setMatchLevel(int matchLevel) {
		this.matchLevel = matchLevel;
	}
	public String getLinkToPic() {
		return linkToPic;
	}
	public void setLinkToPic(String linkToPic) {
		this.linkToPic = linkToPic;
	}
	public String[] getCommomLikes() {
		return commomLikes;
	}
	public void setCommomLikes(String[] commomLikes) {
		this.commomLikes = commomLikes;
	}
	
	
}
