package jp.rainbowdevil.niconicolivechecker.data;

import java.io.Serializable;

public class FavoriteChannel implements Serializable{
	
	private String title = "";
	private String communityTitle = "";
	private String communityUrl = "";
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCommunityTitle() {
		return communityTitle;
	}
	public void setCommunityTitle(String communityTitle) {
		this.communityTitle = communityTitle;
	}
	public String getCommunityUrl() {
		return communityUrl;
	}
	public void setCommunityUrl(String communityUrl) {
		this.communityUrl = communityUrl;
	}

}
