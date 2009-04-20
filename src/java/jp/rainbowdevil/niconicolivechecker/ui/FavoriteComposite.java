package jp.rainbowdevil.niconicolivechecker.ui;

import java.util.List;

import jp.rainbowdevil.niconicolivechecker.NiconicoLiveChecker;
import jp.rainbowdevil.niconicolivechecker.data.FavoriteChannel;
import jp.rainbowdevil.niconicolivechecker.data.LiveChannel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class FavoriteComposite extends Composite {
	
	private TableViewer tableViewer;
	
	private NiconicoLiveChecker checker;
	
	/** Logインスタンスを取得 */
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(FavoriteComposite.class);
	
	public FavoriteComposite( Composite parent , int style ,NiconicoLiveChecker checker ){
		super(parent, style );
		this.checker = checker;
		setLayout( new FillLayout());
		
		tableViewer = new TableViewer(this,SWT.FULL_SELECTION);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		
		//Tableの設定
        Table table = tableViewer.getTable();
        TableColumn col1 = new TableColumn(table, SWT.LEFT);
        col1.setText("キーワード");
        col1.setWidth(300);
        table.setHeaderVisible(true);
        
        table.addKeyListener(new KeyListener(){
        	@Override
        	public void keyPressed(KeyEvent arg0) {
        		// TODO Auto-generated method stub
        		
        	}
        	@Override
        	public void keyReleased(KeyEvent event) {
        		log.debug("key="+event.keyCode);
        		if( event.keyCode == 127 ){
        			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
        			if( selection != null ){
        				FavoriteChannel favorite = (FavoriteChannel)selection.getFirstElement();
        				((List)tableViewer.getInput()).remove(favorite);
        				tableViewer.refresh();
        			}
        		}
        	}
        });
        
        table.addMouseListener( new MouseAdapter(){
        	@Override
        	public void mouseUp(MouseEvent event) {
        		if( event.button == 3 ){
        			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
        			//LiveChannel channel = (LiveChannel)selection.getFirstElement();
        			FavoriteChannel favorite = (FavoriteChannel)selection.getFirstElement(); 
        			if( favorite != null ){
        				getContextMenu(favorite).createContextMenu(tableViewer.getTable()).setVisible(true);
        			}
        		}
        	}
        });
	}
	
	/**
	 * テーブルを右クリックしたときに表示されるコンテキストメニューを取得する
	 * @return
	 */
	private MenuManager getContextMenu( final FavoriteChannel favorite ){
		MenuManager menu = new MenuManager();
		menu.add(new Action("キーワード "+favorite.getKeyword()+" を削除"){
			@Override
			public void run() {
				log.debug("お気に入りの削除 "+favorite.getKeyword());
				checker.getFavoriteChannelList().remove(favorite);
				checker.getWindow().favoriteListRefresh();
			}
		});
		return menu;
	}
	
	public void setInput( List<FavoriteChannel> input ){
		tableViewer.setInput(input);
	}
	
	public void refresh(){
		tableViewer.refresh();
	}
	
	
private class TableLabelProvider implements ITableLabelProvider{
		
		@Override
		public String getColumnText(Object obj, int index) {
			FavoriteChannel channel = (FavoriteChannel)obj;
			switch( index ){
			case 0:
				return channel.getKeyword();
			}
			return null;
		}

		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener arg0) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
		}
	}
}
