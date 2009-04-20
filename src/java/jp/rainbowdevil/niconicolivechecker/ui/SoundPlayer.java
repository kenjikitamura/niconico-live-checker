package jp.rainbowdevil.niconicolivechecker.ui;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SoundPlayer {
	
	private static final int EXTERNAL_BUFFER_SIZE = 128000;

	
	public void play( InputStream is) {
	    try {
	      // オーディオ入力ストリームを取得します
	      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);
	      // オーディオ形式を取得します
	      AudioFormat audioFormat = audioInputStream.getFormat();

	      // データラインの情報オブジェクトを生成します
	      DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat);
	      // 指定されたデータライン情報に一致するラインを取得します
	      SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
	      // 指定されたオーディオ形式でラインを開きます
	      line.open(audioFormat);
	      // ラインでのデータ入出力を可能にします
	      line.start();

	      int nBytesRead = 0;
	      byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
	      while (nBytesRead != -1) {
	        // オーディオストリームからデータを読み込みます
	        nBytesRead = audioInputStream.read(abData, 0, abData.length);
	        if (nBytesRead >= 0) {
	          // オーディオデータをミキサーに書き込みます
	          int nBytesWritten = line.write(abData, 0, nBytesRead);
	        }
	      }
	      // ラインからキューに入っているデータを排出します
	      line.drain();
	      // ラインを閉じます
	      line.close();

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }


}
