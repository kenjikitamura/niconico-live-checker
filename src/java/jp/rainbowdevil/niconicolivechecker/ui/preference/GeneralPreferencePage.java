package jp.rainbowdevil.niconicolivechecker.ui.preference;

import jp.rainbowdevil.niconicolivechecker.config.Config;

import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 一般の設定ページ
 * @author kkitamu
 *
 */
public class GeneralPreferencePage extends FieldEditorPreferencePage {

    public GeneralPreferencePage() {
            setTitle("一般");
            setMessage("一般的な設定");
            
            setDescription("テスト");

    }

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(Config.OUTPUT_SOUND_NOTIFY, "お気に入り放送開始時の通知音声を行う",
		          getFieldEditorParent()));
		addField(new FileFieldEditor(Config.NOTIFY_SOUND_FILEPATH, "通知時の音声ファイル",
		          getFieldEditorParent()));
		addField(new FileFieldEditor(Config.BROWSER_PATH,"使用するブラウザ",getFieldEditorParent()));
		addField(new StringFieldEditor(Config.UPDATE_INTERVAL, "放送リスト更新間隔(分)",
		          getFieldEditorParent()));
		
	}
	

}
