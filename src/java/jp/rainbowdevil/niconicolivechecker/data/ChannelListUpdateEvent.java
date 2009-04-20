package jp.rainbowdevil.niconicolivechecker.data;

/**
 * チャンネルリストの更新時のイベント
 * @author kkitamu
 *
 */
public class ChannelListUpdateEvent {
	
	private String msg;
	//private boolean isFinished;

	public String getMessage() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
