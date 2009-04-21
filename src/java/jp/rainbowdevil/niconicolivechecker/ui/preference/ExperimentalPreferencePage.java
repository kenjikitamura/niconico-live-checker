package jp.rainbowdevil.niconicolivechecker.ui.preference;

import jp.rainbowdevil.niconicolivechecker.config.Config;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;

/**
 * 実験的な設定ページ
 * @author kkitamu
 *
 */
public class ExperimentalPreferencePage extends FieldEditorPreferencePage {

	public ExperimentalPreferencePage() {
		setTitle("実験的な設定");
        setMessage("実験的な設定");
        
        setDescription("これらの設定項目は将来的に削除されるか、通常の設定に移動されます。\n");
	}
	
	 @Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(Config.TASKTRAY_START,"タスクトレイに入った状態で起動する", getFieldEditorParent()));
		
	}

}
