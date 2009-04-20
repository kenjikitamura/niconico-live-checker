package jp.rainbowdevil.niconicolivechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.swt.widgets.Display;

import jp.rainbowdevil.niconicolivechecker.config.Config;
import jp.rainbowdevil.niconicolivechecker.data.DataManager;
import jp.rainbowdevil.niconicolivechecker.data.FavoriteChannel;
import jp.rainbowdevil.niconicolivechecker.data.LiveChannel;
import jp.rainbowdevil.niconicolivechecker.ui.NiconicoWindow;

/**
 * NiconicoLiveCheckerのメインクラス
 * @author KENJI
 *
 */
public class NiconicoLiveChecker {
	
	/** アプリ名 */
	public static final String APP_NAME = "Niconico Live Checker";
	
	/** 現在放送中のチャンネルリスト */
	private List<LiveChannel> currentLiveChannelList;
	
	/** お気に入りのリスト */
	private List<FavoriteChannel> favoriteChannelList;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(3,new DaemonThreadFactory());
	
	/** UI Window */
	private NiconicoWindow window;
	
	/** 設定オブジェクト */
	private Config config;
	
	/** デフォルト生放送チャンネルリスト更新間隔(分) */
	private int checkInterval = 3 ;
	
	/** Logインスタンスを取得 */
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(NiconicoLiveChecker.class);
	
	/**
	 * Main
	 * @param args
	 */
	public static void main( String[] args ){
		(new DOMConfigurator()).doConfigure(NiconicoLiveChecker.class.getResourceAsStream( "/log4j.xml" ), LogManager.getLoggerRepository());
		
		NiconicoLiveChecker main = new NiconicoLiveChecker();
		main.start();
	}
	
	public void start(){
		readFavoriteFile();

		config = Config.get();
		config.load();

		window = new NiconicoWindow(this);
		window.setBlockOnOpen(true);
		window.open();
		Display.getDefault().close();
		
		writeFavoriteFile();
		
		try {
			config.save();
		} catch (ConfigurationException e) {
			log.error("設定ファイルの保存に失敗",e);
		}
	}
	
	
	private boolean isTimerStart;
	/**
	 * 一定間隔で放送中リストを取得するためのタイマ
	 * 	 */
	public void startTimer(){
		if( !isTimerStart ){
			log.debug("startTimer");
			
			int interval = Config.get().getInt(Config.UPDATE_INTERVAL,checkInterval);
			if( interval < 1 ){
				interval = checkInterval;
			}
			log.debug("check interval = "+interval+" minute");
			
			executor.scheduleWithFixedDelay(new Timer(), 1, interval * 60, TimeUnit.SECONDS );
			isTimerStart = true;
		}
	}
	
	
	/**
	 * お気に入りリストの保存
	 */
	public void writeFavoriteFile(){
		DataManager dataManager = new DataManager();
		// 最後にお気に入り保存
		try {
			dataManager.writeFavoriteChannelList(favoriteChannelList, DataManager.DEFAULT_FILENAME );
		} catch (IOException e) {
			log.error("favorite file write error.",e);
		}
	}
	
	/**
	 * お気に入りリストの読み込み
	 */
	private void readFavoriteFile(){
		DataManager dataManager = new DataManager();
		currentLiveChannelList = new ArrayList<LiveChannel>();
		try {
			if( new File( DataManager.DEFAULT_FILENAME ).exists() ){
				favoriteChannelList = dataManager.getFavoriteChannelList(DataManager.DEFAULT_FILENAME);
			}else{
				favoriteChannelList = new ArrayList<FavoriteChannel>();
			}
			
		} catch (FileNotFoundException e) {
			log.error("favorite file read error.",e);
			favoriteChannelList = new ArrayList<FavoriteChannel>();
		} catch (IOException e) {
			log.error("favorite file read error.",e);
			favoriteChannelList = new ArrayList<FavoriteChannel>();
		} catch (ClassNotFoundException e) {
			log.error("favorite file read error.",e);
			favoriteChannelList = new ArrayList<FavoriteChannel>();
		}
		
	}
	
	private class Timer implements Runnable{
		@Override
		public void run() {
			log.debug("生放送チャンネルリスト更新");
			try{
			window.updateList();
			}catch( Exception e ){
				log.error("error",e);
			}
			return ;
		}
	}
	
	

	public List<LiveChannel> getCurrentLiveChannelList() {
		return currentLiveChannelList;
	}

	public void setCurrentLiveChannelList(List<LiveChannel> currentLiveChannelList) {
		this.currentLiveChannelList = currentLiveChannelList;
	}

	public List<FavoriteChannel> getFavoriteChannelList() {
		return favoriteChannelList;
	}

	public void setFavoriteChannelList(List<FavoriteChannel> favoriteChannelList) {
		this.favoriteChannelList = favoriteChannelList;
	}


	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	
	public class DaemonThreadFactory implements ThreadFactory {


		  public DaemonThreadFactory() {
		    SecurityManager securitymanager = System.getSecurityManager();
		    group = securitymanager == null ? Thread.currentThread()
		                                              .getThreadGroup() : securitymanager.getThreadGroup();
		    namePrefix = (new StringBuilder()).append("pool-").toString();
		  }

		  public Thread newThread(Runnable runnable) {
		    Thread thread = new Thread(group, runnable, (new StringBuilder())
		                          .append(namePrefix).append(threadNumber.getAndIncrement())
		                          .toString(), 0L);
		    thread.setDaemon(true);
		    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
		      @Override
		      public void uncaughtException(Thread t, Throwable e) {
		        log.error("UncaughtException!! thread name=("+t.getName()+")",e);
		      }
		    });
		    if (thread.getPriority() != 5)
		    thread.setPriority(5);
		    return thread;
		  }

		  final ThreadGroup group;
		  final AtomicInteger threadNumber = new AtomicInteger(1);
		  final String namePrefix;
		}



	public NiconicoWindow getWindow() {
		return window;
	}

	public void setWindow(NiconicoWindow window) {
		this.window = window;
	}

	public ScheduledExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ScheduledExecutorService executor) {
		this.executor = executor;
	}

	
}
