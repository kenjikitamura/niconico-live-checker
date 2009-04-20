package jp.rainbowdevil.niconicolivechecker.config;

import java.io.FileInputStream;
import java.io.IOException;

import jp.rainbowdevil.niconicolivechecker.ui.NiconicoWindow;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

public class Config {
	
	public static final String OUTPUT_SOUND_NOTIFY = "jp.rainbowdevil.niconicolivechecker.sound_notify";
	
	/** 初回のみの最小化時の説明がすんでいるかどうか */
	public static final String FIRST_TIME_MINIMUM_NOTIFY = "jp.rainbowdevil.niconicolivechecker.minimum_notify";
	
	/** リスト更新間隔(分) */
	public static final String UPDATE_INTERVAL = "jp.rainbowdevil.niconicolivechecker.update_interval";
	
	private PropertiesConfiguration config;
	
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
		return config.getBoolean(key,false);
	}
	
	public void setBoolean( String key , boolean flg ){
		config.setProperty(key, flg);
	}
	
	/**
	 * Int値を取得。
	 * デフォルト値は0
	 * @param key
	 */
	public int getInt( String key ){
		return getInt(key,0);
	}
	
	public int getInt( String key , int defaultValue ){
		try{
			return config.getInt(key,defaultValue);
		}catch( ConversionException e){
			return defaultValue;
		}
	}
	
	public void load() {
		String filename = "niconicoLiveChecker.properties";
		try {
			config = new PropertiesConfiguration(filename);
		} catch (ConfigurationException e) {
			config = new PropertiesConfiguration();
			config.setFileName(filename);
		}
	}
	
	public void save() throws ConfigurationException{
		config.save();
	}
}
