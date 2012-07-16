package cmm.data;

import java.util.List;

public class UserFrontendObject {
	private String firstName;
	private String lastName;
	private int matchLevel;
	private String linkToPic;
	private List<String> commomLikes;
	private boolean isMHB;
	
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
	public List<String> getCommomLikes() {
		return commomLikes;
	}
	public void setCommomLikes(List<String> commomLikes) {
		this.commomLikes = commomLikes;
	}
	public boolean getIsMHB(){
		return this.isMHB;
	}
	public void setIsMHB(boolean isMarriedHasBaby){
		this.isMHB = isMarriedHasBaby;
	}
	
	
}
