package jp.rainbowdevil.niconicolivechecker.ui.action;

import jp.rainbowdevil.niconicolivechecker.NiconicoLiveChecker;
import jp.rainbowdevil.niconicolivechecker.config.Config;
import jp.rainbowdevil.niconicolivechecker.ui.NiconicoWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class OutputSoundAction extends Action {
	
	private NiconicoWindow window;
	
	public OutputSoundAction( NiconicoWindow window ){
		super("お気に入り番組開始時の通知を音声でも行う",SWT.CHECK);
		this.window = window;
		
		boolean flg = Config.get().getBoolean(Config.OUTPUT_SOUND_NOTIFY);
		setChecked(flg);
	}
	
	
	
	@Override
	public void run() {
		boolean flg =  Config.get().getBoolean(Config.OUTPUT_SOUND_NOTIFY);
		flg = !flg;
		Config.get().setBoolean(Config.OUTPUT_SOUND_NOTIFY, flg);
		setChecked(flg);
	}

}
