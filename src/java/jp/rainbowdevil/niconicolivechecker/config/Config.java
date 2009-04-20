package jp.rainbowdevil.niconicolivechecker.config;

import java.io.FileInputStream;
import java.io.IOException;

import jp.rainbowdevil.niconicolivechecker.ui.NiconicoWindow;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.eclipse.jface.preference.PreferenceStore;

/**
 * 各種設定を管理するクラス
 * Singletonとなっている
 * @author kkitamu
 *
 */
public class Config {
	
	/** お気に入り放送が始まった場合に音声による通知を実行するかどうか */
	public static final String OUTPUT_SOUND_NOTIFY = "jp.rainbowdevil.niconicolivechecker.sound_notify";
	
	/** 初回のみの最小化時の説明がすんでいるかどうか */
	public static final String FIRST_TIME_MINIMUM_NOTIFY = "jp.rainbowdevil.niconicolivechecker.minimum_notify";
	
	/** リスト更新間隔(分) */
	public static final String UPDATE_INTERVAL = "jp.rainbowdevil.niconicolivechecker.update_interval";
	
	/** 使用するブラウザ */
	public static final String BROWSER_PATH = "jp.rainbowdevil.niconicolivechecker.browserpath";
	
	/** 通知サウンドファイル */
	public static final String NOTIFY_SOUND_FILEPATH = "jp.rainbowdevil.niconicolivechecker.notifysoundfile";
	
	/** タスクトレイに入った状態で開始する */
	public static final String TASKTRAY_START = "jp.rainbowdevil.niconicolivechecker.tasktray_start";
	
	//private PropertiesConfiguration config;
	
	/** 設定オブジェクト */
	private PreferenceStore prefStore;
	
	private static Config instance = new Config();
	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(NiconicoWindow.class);
	
	private Config(){
	}
	
	public static Config get(){
		return instance ;
	}
	
	public boolean getBoolean( String key ){
		return prefStore.getBoolean(key);
	}
	
	public String getString( String key ){
		return prefStore.getString(key);
	}
	
//	public void setBoolean( String key , boolean flg ){
//		prefStore.setValue(key, flg);
//	}
	
	/**
	 * Int値を取得。
	 * デフォルト値は0
	 * @param key
	 */
	public int getInt( String key ){
		return prefStore.getInt(key);
		//return getInt(key,0);
	}
	
	public void setBoolean( String key , boolean value ){
		prefStore.setValue(key, value);
	}
	
	public void setInt( String key , int value ){
		prefStore.setValue(key, value);
	}
	
	public int getInt( String key , int defaultValue ){
		prefStore.setDefault(key, defaultValue);
		return prefStore.getInt(key);
	}
	
	public PreferenceStore getPreferenceStore(){
		return prefStore;
	}
	
	
	public void load() {
		String filename = "niconicoLiveChecker.properties";
		
		prefStore = new PreferenceStore(filename);
	
		try {
			prefStore.load();
		} catch (IOException e) {
			log.error("設定ファイルの読み込みに失敗しました。file="+filename,e);
		}
		
//		try {
//			config = new PropertiesConfiguration(filename);
//		} catch (ConfigurationException e) {
//			config = new PropertiesConfiguration();
//			config.setFileName(filename);
//		}
	}
	
	public void save() throws ConfigurationException{
		//config.save();
		try {
			prefStore.save();
		} catch (IOException e) {
			log.error("設定ファイルの保存に失敗しました。",e);
		}
	}
}
