package jp.rainbowdevil.niconicolivechecker.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


/**
 * ニコニコ動画ユーザ生放送のサイトへ接続し、LiveChannelインスタンスのリストを取得するクラス
 * @author kkitamu
 *
 */
public class Connector {
	
	/** 接続先URL */
	private String URL = "http://live.nicovideo.jp/recent?tab=<TAG>&p=<PAGE>&without_notice=true&sort=start";
	
	/** 取得タグ */
	private String[] tags = {"common","try","live","req","face","r18"};
	
	/** 取得タグラベル */
	private String[] tagName = {"一般","やってみた","実況","リクエスト","顔出し","R18"};
	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Connector.class);
	
	/**
	 * 指定のURLに接続し、結果を取得する
	 * 文字コードはUTF-8固定
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public String getString( String url ) throws HttpException, IOException{
	    HttpClient client = new HttpClient();
	    GetMethod method = new GetMethod(url);
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3, false));
	    try {
	      int statusCode = client.executeMethod(method);

	      if (statusCode != HttpStatus.SC_OK) {
	        System.err.println("Method failed: " + method.getStatusLine());
	      }
	      byte[] responseBody = method.getResponseBody();
	      return new String(responseBody,"UTF-8");
	    } finally {
	      method.releaseConnection();
	    }  	
	}
	
	/**
	 * すべての放送中チャンネルリストを取得する
	 * 
	 * @param listener 取得中の状態を通知するためのリスナ nullでも可
	 * @return 放送中のLiveChannelのリスト
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<LiveChannel> getAllChannelList( StateChangeListener listener ) throws HttpException, IOException{
		List<LiveChannel> allChannelList = new ArrayList<LiveChannel>();
		NiconicoParser parser = new NiconicoParser();
		
		
		
		for( int i = 0 ; i < tags.length ; i++ ){
			int page = 1;
			String tag = tags[i];
			List<LiveChannel> list = new ArrayList<LiveChannel>();
			LOOP:while(true){
				String url = URL.replace("<PAGE>", String.valueOf(page));
				url = url.replace("<TAG>", tag);
				//log.debug("tag="+tag+" url="+url);
				String html = getString(url);
				List<LiveChannel> pageChannels = parser.parseText(html);
				//log.debug("size="+pageChannels.size());
				
				if( pageChannels.size() == 0 && html.indexOf("エラー") != -1){
					log.debug("エラーが帰ってきたので1度だけ取得し直す");
					html = getString(url);
					pageChannels = parser.parseText(html);
					log.debug("エラー時再取得 size="+pageChannels.size());
				}
				
				if( pageChannels.size() == 0 ){
					break LOOP;
				}
				
				for (LiveChannel liveChannel : list) {
					LiveChannel delChannel = null;
					for( LiveChannel newChannel : pageChannels ){
						if( liveChannel.getUrl().equals(newChannel.getUrl())){
							delChannel = newChannel;
						}
					}
					if( delChannel != null ){
						pageChannels.remove(delChannel);
					}
				}
				list.addAll(pageChannels);
				
				//----------------------------------------------------------------
				// 通知
				String msg = "更新中 (カテゴリ"+tagName[i]+" "+page+"ページ目読み込み完了)";
				ChannelListUpdateEvent event = new ChannelListUpdateEvent();
				event.setMsg(msg);
				if( listener != null ){
					listener.handleStateChange(event);
				}
				//----------------------------------------------------------------
				
				page++;
			}
			
			// カテゴリ名付与
			for( LiveChannel ch : list ){
				ch.setCategory(tagName[i]);
			}
			
			allChannelList.addAll(list);
		}
		return allChannelList;
	}
	
}
