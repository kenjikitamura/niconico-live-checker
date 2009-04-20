package jp.rainbowdevil.niconicolivechecker.ui.action;

import jp.rainbowdevil.niconicolivechecker.ui.NiconicoWindow;

import org.eclipse.jface.action.Action;

/**
 * 終了Action
 * @author kkitamu
 *
 */
public class ExitAction extends Action {
	
	private NiconicoWindow window;
	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ExitAction.class);
	
	public ExitAction( NiconicoWindow window ){
		super("Exit");
		this.window = window;
	}
	
	@Override
	public void run() {
		log.debug("exit");
		window.close();
	}
}
