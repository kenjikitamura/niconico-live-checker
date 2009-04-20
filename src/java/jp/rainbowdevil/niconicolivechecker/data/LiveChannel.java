package jp.rainbowdevil.niconicolivechecker.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 放送1チャンネルを表すクラス
 * @author kkitamu
 *
 */
public class LiveChannel {
	private String startDateString;
	private String title;
	private String comment;
	private String communityTitle;
	private String category;
	private String url;
	private String imageUrl;
	private int visitorSize;
	private int commentSize;
	private boolean isNotified;
	private boolean isFavorite;

	/** 日付フォーマット 
	 *  マルチスレッドには対応していないので、マルチスレッドで番組リストの取得をするよう変更する場合は注意 */
	static private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(LiveChannel.class);
	
	/**
	 * 放送開始日のパース
	 */
	private void parseStartDateString(){
		log.debug("開始時間="+startDateString);
		try {
			Date date = dateFormat.parse(startDateString);
			//log.debug(date.toString());
			long minutes = date.getTime() / 1000 / 60;
			long nowMinutes = System.currentTimeMillis() / 1000 / 60;
			log.debug("放送開始から "+(nowMinutes - minutes )+"分経過");

		} catch (ParseException e) {
			log.error("開始時間のパースに失敗 "+startDateString);
		}
	}
	
	public String getStartDateString() {
		return startDateString;
	}
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
		//parseStartDateString();
	}
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getVisitorSize() {
		return visitorSize;
	}
	public void setVisitorSize(int visitorSize) {
		this.visitorSize = visitorSize;
	}
	public int getCommentSize() {
		return commentSize;
	}
	public void setCommentSize(int commentSize) {
		this.commentSize = commentSize;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isNotified() {
		return isNotified;
	}
	public void setNotified(boolean isNotified) {
		this.isNotified = isNotified;
	}
	public boolean isFavorite() {
		return isFavorite;
	}
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}
