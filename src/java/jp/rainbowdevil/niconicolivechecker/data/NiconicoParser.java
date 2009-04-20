package jp.rainbowdevil.niconicolivechecker.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class NiconicoParser {
	
	private Pattern commentSizePattern = Pattern.compile("コメ数:<strong>(.*)</strong>");
	private Pattern commentPattern = Pattern.compile("</a><br>\n(.*)<br>");
	private Pattern urlPattern = Pattern.compile("http://live.nicovideo.jp/watch/lv(\\d+)");
	private Pattern titlePattern = Pattern.compile(">(.*?)</a><br>");
	private Pattern visitorSizePattern = Pattern.compile("来場者数:<strong>(.*)</strong>人");
	private Pattern communityTitlePattern = Pattern.compile("<p class=\"txt10\">(.*?)</p>");
	private Pattern startDatePattern = Pattern.compile("			<strong>(.*?) 開始</strong>");

	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(NiconicoParser.class);

	/**
	 * HTMLをパースし、チャンネルリストを取得する
	 * パースできなかった場合はサイズが0のリストが返る
	 * @param text
	 * @return
	 */
	public List<LiveChannel> parseText( String text ){
		List<LiveChannel> list = new ArrayList<LiveChannel>();
		if( text == null ){
			return list;
		}
		
		String channelHtmlText = StringUtils.substringBetween(text, "<div id=\"search_result\" class=\"clear\">", "<!-- #search_result -->");
		if( channelHtmlText == null ){
			log.debug("チャンネルリストではない。");
			return list;
		}
		String[] channelData = channelHtmlText.split("<!-- div.uc -->");
		
		//log.debug("channelDataSize="+channelData.length);
		
		// チャンネル区切り文字の最後にもゴミがついてるので最後の1個は見ない
		for( int i = 0 ; i < channelData.length -1; i++ ){
			String channelHtml = channelData[i];
			//log.debug("html="+channelHtml);
			
			LiveChannel channel = new LiveChannel();
			
			Matcher commentSizeMatcher = commentSizePattern.matcher(channelHtml);
			Matcher commentMatcher = commentPattern.matcher(channelHtml);
//			commentMatcher.find();
//			commentMatcher = commentPattern.matcher(channelHtml.substring(commentMatcher.end()));
			Matcher urlMatcher = urlPattern.matcher(channelHtml);
			Matcher titleMatcher = titlePattern.matcher(channelHtml);
			Matcher visitorSizeMatcher = visitorSizePattern.matcher(channelHtml);
			Matcher communityTitleMatcher = communityTitlePattern.matcher(channelHtml);
			Matcher startDateMatcher = startDatePattern.matcher(channelHtml);
			
			
			
			if( commentSizeMatcher.find() ){
				channel.setCommentSize(Integer.parseInt(commentSizeMatcher.group(1)));
			}
			if( urlMatcher.find() ){
				channel.setUrl("http://live.nicovideo.jp/watch/lv"+urlMatcher.group(1));
			}
			if( titleMatcher.find() ){
				String title = titleMatcher.group(1);
				// メンバーオンリーの場合は画像タグが含まれるので除去
				if( title.indexOf("member_only.gif") != -1 ){
					int index = title.lastIndexOf("\">");
					if( index != -1 ){
						title = title.substring(index+2);
					}
				}
				channel.setTitle(convert(title));
			}
			channel.setComment(convert(StringUtils.substringsBetween(channelHtml, "</a><br>", "<br>")[0]).replaceAll("\n", "").replaceAll("\r", "").trim());
//			if( commentMatcher.find() ){
//				channel.setComment(commentMatcher.group(1));
//			}
			if( visitorSizeMatcher.find() ){
				channel.setVisitorSize(Integer.parseInt(visitorSizeMatcher.group(1)));
			}
			if( communityTitleMatcher.find() ){
				channel.setCommunityTitle(convert(communityTitleMatcher.group(1)));
			}
			if( startDateMatcher.find() ){
				channel.setStartDateString(startDateMatcher.group(1));
			}
			
			// コミュニティ名が<a href=から始まっているとユーザ生放送ではない
			if( channel.getCommunityTitle().indexOf("<a href=\"http://ch.nicovideo.jp") == 0){
				log.debug("コミュニティ名が<a href=\"http://ch.nicovideo.jpから始まっているのでユーザ生放送ではないと判断。リストから除外する。");
				continue;
			}
			
			// コミュタイトルがメンバーオンリーに誤反応している
			if( channel.getCommunityTitle().indexOf("<img src=\"img/community/member_only.gif") != -1){
				channel.setCommunityTitle(StringUtils.substringsBetween(channelHtml, "<p class=\"txt10\">", "</p>")[2]);
				
			}

			if( channel.getComment() == null ){
				log.debug("Comment=null. allHtml="+channelHtmlText);
				channel.setComment("");
			}
			
			if( channel.getUrl() == null ){
				log.debug("URL=null. allHtml="+channelHtmlText);
				channel.setUrl("");
			}
			
			if( channel.getTitle() == null ){
				log.debug("Title=null. communityTitle="+channel.getCommunityTitle()+" url="+channel.getUrl()+" allHtml="+channelHtmlText);
				channel.setTitle("");
			}
			if( channel.getCommunityTitle() == null ){
				log.debug("Community=null. allHtml="+channelHtmlText);
				channel.setCommunityTitle("");
			}
			
			// 実態参照の変換
			list.add(channel);
		}
		
		return list;
	}
	
	private String convert( String line ){
		if( line == null ){
			log.debug("line=null.");
			return null;
		}
		
		
		line = line.replaceAll("<wbr>", "");
		line = line.replaceAll("<wbr />", "");
		line = line.replaceAll("&lt;", "<");
		line = line.replaceAll("&gt;", ">");
		line = line.replaceAll("&nbsp;", " ");
		line = line.replaceAll("&#039;", "'");
		line = line.replaceAll("&amp;", "&");
			
		return line;
	}

}
