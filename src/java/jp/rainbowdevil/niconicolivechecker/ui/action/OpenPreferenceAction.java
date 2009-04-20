package jp.rainbowdevil.niconicolivechecker.ui.action;

import java.io.IOException;

import jp.rainbowdevil.niconicolivechecker.NiconicoLiveChecker;
import jp.rainbowdevil.niconicolivechecker.config.Config;
import jp.rainbowdevil.niconicolivechecker.ui.NiconicoWindow;
//import jp.rainbowdevil.niconicolivechecker.ui.preference.ExperimentalPreferencePage;
//import jp.rainbowdevil.niconicolivechecker.ui.preference.GeneralPreferencePage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;

/**
 * 設定ダイアログを開く
 * @author kkitamu
 *
 */
public class OpenPreferenceAction extends Action {
	
	private NiconicoWindow checker;
	
	/** Logインスタンスを取得 */
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(OpenPreferenceAction.class);
	
	public OpenPreferenceAction( NiconicoWindow checker ){
		super("設定");
		this.checker = checker;
	}

	
	@Override
	public void run() {
		PreferenceManager pm = new PreferenceManager();

//        PreferenceNode pnode1 = new PreferenceNode("NODE1");
//        pnode1.setPage(new GeneralPreferencePage());
//        pm.addToRoot(pnode1);
//        
//        PreferenceNode pnode2 = new PreferenceNode("NODE2");
//        pnode2.setPage(new ExperimentalPreferencePage());
//        pm.addToRoot(pnode2);
        

//        PreferenceNode pnode2 = new PreferenceNode("NODE2");
//        pnode2.setPage(new PreferencePage2());
//        pm.addTo("NODE1", pnode2);

        PreferenceDialog dialog = new PreferenceDialog(checker.getShell(), pm);
        dialog.setPreferenceStore(Config.get().getPreferenceStore());
        dialog.setBlockOnOpen(true);
        dialog.open();
        PreferenceStore store = (PreferenceStore) dialog.getPreferenceStore();
        
        try {
			store.save();
		} catch (IOException e) {
			log.error("設定ファイルの保存に失敗しました。",e);
		}
        

	}
	
	
}