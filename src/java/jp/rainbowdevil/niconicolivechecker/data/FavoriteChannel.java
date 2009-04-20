package jp.rainbowdevil.niconicolivechecker.data;

import java.io.Serializable;

/**
 * お気に入りクラス
 * @author kkitamu
 *
 */
public class FavoriteChannel implements Serializable{
	
	/** タイトル */
	private String keyword = "";
	
	
	//private String communityTitle = "";
	//private String communityUrl = "";
	
	/** コミュニティID */
	private String communityId = "";
	
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	/**
	 * お気に入りのタイトルを取得する
	 * 現在はお気に入りのタイトルに含まれるものをキーワードとしてコミュニティ名と放送タイトルにマッチングさせている
	 * @return
	 */
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String title) {
		this.keyword = title;
	}
	
	
//	public String getCommunityTitle() {
//		return communityTitle;
//	}
//	public void setCommunityTitle(String communityTitle) {
//		this.communityTitle = communityTitle;
//	}
//	public String getCommunityUrl() {
//		return communityUrl;
//	}
//	public void setCommunityUrl(String communityUrl) {
//		this.communityUrl = communityUrl;
//	}

}
