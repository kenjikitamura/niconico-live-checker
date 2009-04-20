package jp.rainbowdevil.niconicolivechecker.data;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class NiconicoParserTest extends TestCase{
	
	private String html;
	private NiconicoParser parser;
	
	@Before
	public void setUp(){
		try {
			html = FileUtils.readFileToString(new File("../dat/recent3.htm"),"UTF-8");
		} catch (IOException e) {
		}
		parser = new NiconicoParser();
	}
	
	
//  2009年1月のフォーマット	
//	@Test
//	public void testParseText(){
//		List<LiveChannel> list = parser.parseText(html);
//		assertEquals(12, list.size());
//		
//		LiveChannel channel;
//		channel = list.get(0);
//		assertEquals("茨城なまり気味なメダロット２", channel.getTitle());
//		assertEquals("リクエスト停止中?",channel.getComment());
//		assertEquals(0, channel.getCommentSize());
//		assertEquals(5, channel.getVisitorSize());
//		assertEquals("ハルビンのコミュニティコミュニティ", channel.getCommunityTitle());
//		assertEquals("http://live.nicovideo.jp/watch/lv68526", channel.getUrl());
//		//assertEquals("http://icon.nicovideo.jp/community/co6606.jpg?", channel.getImageUrl());
//		
//		channel = list.get(1);
//		assertEquals("どうも、放置プレイに定評のある僕です。透明水彩鬼畜過ぎる", channel.getComment());
//		channel = list.get(2);
//		assertEquals("地獄モード本格的にはいりましたｗ\n今日の成績　4→2→4→4", channel.getComment());
//		channel = list.get(3);
//		assertEquals("まず最初に…主はゲームがやばいくらい苦手＆下手くそですのでご...", channel.getComment());
//		channel = list.get(4);
//		assertEquals("リクエストされた動画を鑑賞しながら\n皆で楽しく談笑する番組で...", channel.getComment());
//		channel = list.get(5);
//		assertEquals("テスト放送（っていうか…練習？汗）その3～…\n口下手の上緊張屋...", channel.getComment());
//	}
	
	@Test
	public void testParseText() throws IOException{
		List<LiveChannel> list = parser.parseText(html);
		assertEquals(12, list.size());
		LiveChannel channel;
		channel = list.get(0);
		assertEquals("ボケたい！方どうぞいらっしゃい！！＆大喜...", channel.getTitle());
		assertEquals("(´３`)/~あの楽器って最近知った☆生主がマターリ放送するお。...",channel.getComment());
		assertEquals(0, channel.getCommentSize());
		assertEquals(0, channel.getVisitorSize());
		assertEquals("Ch. UKIHATE GOKKOコミュニティ", channel.getCommunityTitle());
		assertEquals("http://live.nicovideo.jp/watch/lv558629", channel.getUrl());
		//assertEquals("http://icon.nicovideo.jp/community/co6606.jpg?", channel.getImageUrl());
		
		
		channel = list.get(11);
		assertEquals("脱出しながら雑談！141回目ノ巻！", channel.getTitle());
		assertEquals("脱出実況をするここ最近。【今やってるゲームと内容】ゲ...",channel.getComment());
		assertEquals(25, channel.getCommentSize());
		assertEquals(66, channel.getVisitorSize());
		assertEquals("のんびり好きな事しようか。コミュニティ", channel.getCommunityTitle());
		assertEquals("http://live.nicovideo.jp/watch/lv558612", channel.getUrl());
		
		
		html = FileUtils.readFileToString(new File("../dat/recent4.htm"),"UTF-8");
		list = parser.parseText(html);
		assertEquals(12, list.size());
		channel = list.get(10);
		assertEquals("記念放送（コミュ限）", channel.getTitle());
		assertEquals("基本的にはシャナが主は好きなのでシャナの話とか、アニメの話を...",channel.getComment());
		assertEquals(0, channel.getCommentSize());
		assertEquals(13, channel.getVisitorSize());
		assertEquals("素敵な仮想旅行号コミュニティ", channel.getCommunityTitle());
		assertEquals("http://live.nicovideo.jp/watch/lv558910", channel.getUrl());
		
		html = FileUtils.readFileToString(new File("../dat/recent5.htm"),"UTF-8");
		
		list = parser.parseText(html);
		assertEquals(8, list.size());
		channel = list.get(6);
		
		assertEquals("題名どうりです…でも…怖い…だれか……たすけ…………て……………...",channel.getComment());
		assertEquals(2359, channel.getCommentSize());
		assertEquals(639, channel.getVisitorSize());
		assertEquals("いじっかしく生らじ☆コミュニティ", channel.getCommunityTitle());
		assertEquals("http://live.nicovideo.jp/watch/lv539702", channel.getUrl());
		
		html = FileUtils.readFileToString(new File("../dat/recent6.htm"),"UTF-8");
		list = parser.parseText(html);
		assertEquals(9, list.size());
	}

}
