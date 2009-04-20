package jp.rainbowdevil.niconicolivechecker.ui;

import java.io.IOException;
import java.util.List;

import jp.rainbowdevil.niconicolivechecker.NiconicoLiveChecker;
import jp.rainbowdevil.niconicolivechecker.config.Config;
import jp.rainbowdevil.niconicolivechecker.data.FavoriteChannel;
import jp.rainbowdevil.niconicolivechecker.data.LiveChannel;
import jp.rainbowdevil.niconicolivechecker.ui.provider.ChannelListContentProvider;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * チャンネルリストGUI部分
 * @author kkitamu
 *
 */
public class ChannelListComposite extends Composite {
	
	private NiconicoLiveChecker checker;
	
	private TableViewer tableViewer;
	private ChannelListContentProvider provider;
	
	private Color redColor;
	private Color normalColor;
	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ChannelListComposite.class);
	
	public ChannelListComposite( Composite parent , int style , NiconicoLiveChecker checker) {
		super(parent,style);
		this.checker = checker;
		
		setLayout( new FillLayout());
		tableViewer = new TableViewer(this,SWT.FULL_SELECTION);
		provider = new ChannelListContentProvider();
		tableViewer.setContentProvider(provider);
		tableViewer.setLabelProvider(new TableLabelProvider());
		
		//Tableの設定
        Table table = tableViewer.getTable();

        TableColumn col5 = new TableColumn(table, SWT.LEFT);
        col5.setText("コミュニティ名");
        col5.setWidth(200);

        TableColumn col1 = new TableColumn(table, SWT.LEFT);
        col1.setText("タイトル");
        col1.setWidth(300);
        //TableColumn col2 = new TableColumn(table, SWT.LEFT);
        TableViewerColumn col2 = new TableViewerColumn(tableViewer, SWT.NONE);
        col2.getColumn().setText("作成日時");
        col2.getColumn().setWidth(80);
        col2.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return StringUtils.substringAfter(((LiveChannel)element).getStartDateString(), "日").trim();
			}
		});
        
        //TableColumn col3 = new TableColumn(table, SWT.LEFT);
        TableViewerColumn col3 = new TableViewerColumn(tableViewer, SWT.NONE);
        col3.getColumn().setText("来場者");
        col3.getColumn().setWidth(40);
        col3.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ""+((LiveChannel) element).getVisitorSize();
			}
		});
        
        TableViewerColumn col4 = new TableViewerColumn(tableViewer, SWT.NONE);
        col4.getColumn().setText("コメ");
        col4.getColumn().setWidth(40);
        col4.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ""+((LiveChannel) element).getCommentSize();
			}
		});
        
        TableViewerColumn col7 = new TableViewerColumn(tableViewer, SWT.NONE);
        col7.getColumn().setText("カテゴリ");
        col7.getColumn().setWidth(70);
        col7.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((LiveChannel) element).getCategory();
			}
		});

        
        
        TableColumn col6 = new TableColumn(table, SWT.LEFT);
        col6.setText("うｐ主コメント");
        col6.setWidth(400);
        
        ColumnViewerSorter cSorter2 = new ColumnViewerSorter(tableViewer,col2) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				LiveChannel p1 = (LiveChannel) e1;
				LiveChannel p2 = (LiveChannel) e2;
				return p1.getStartDateString().compareToIgnoreCase(p2.getStartDateString());
			}
		};
		
		ColumnViewerSorter cSorter3 = new ColumnViewerSorter(tableViewer,col3) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				LiveChannel p1 = (LiveChannel) e1;
				LiveChannel p2 = (LiveChannel) e2;
				if( p1.getVisitorSize() > p2.getVisitorSize() ){
					return -1;
				}else if(p1.getVisitorSize() < p2.getVisitorSize() ){
					return 1;
				}else{
					return 0;
				}
			}
		};
        
        ColumnViewerSorter cSorter = new ColumnViewerSorter(tableViewer,col4) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				LiveChannel p1 = (LiveChannel) e1;
				LiveChannel p2 = (LiveChannel) e2;
				if( p1.getCommentSize() > p2.getCommentSize() ){
					return -1;
				}else if(p1.getCommentSize() < p2.getCommentSize() ){
					return 1;
				}else{
					return 0;
				}
			}
		};
		
		ColumnViewerSorter cSorter7 = new ColumnViewerSorter(tableViewer,col7) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				LiveChannel p1 = (LiveChannel) e1;
				LiveChannel p2 = (LiveChannel) e2;
				return p1.getCategory().compareTo(p2.getCategory());
			}
		};

        
        table.setHeaderVisible(true);
        
        tableViewer.addDoubleClickListener(new IDoubleClickListener(){
        	@Override
        	public void doubleClick(DoubleClickEvent event) {
        		StructuredSelection selection = (StructuredSelection) event.getSelection();
        		if( selection != null ){
        			LiveChannel channel = (LiveChannel)selection.getFirstElement();
            		executeBrowser(channel.getUrl());
        		}
        	}
        });
        
        table.addMouseListener( new MouseAdapter(){
        	@Override
        	public void mouseUp(MouseEvent event) {
        		if( event.button == 3 ){
        			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
        			LiveChannel channel = (LiveChannel)selection.getFirstElement();
        			if( channel != null ){
        				getContextMenu(channel).createContextMenu(tableViewer.getTable()).setVisible(true);
        			}
        		}
        	}
        });
        redColor = getShell().getDisplay().getSystemColor(SWT.COLOR_RED);
        normalColor = getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK);
        
        //cSorter.setSorter(cSorter, ColumnViewerSorter.ASC);

	}
	
	/**
	 * テーブルを右クリックしたときに表示されるコンテキストメニューを取得する
	 * @return
	 */
	private MenuManager getContextMenu( final LiveChannel channel ){
		MenuManager menu = new MenuManager();
		menu.add(new Action("URLをコピー"){
			@Override
			public void run() {
				Clipboard clipboard = new Clipboard(getShell().getDisplay());
				clipboard.setContents(new Object[]{channel.getUrl()},
				                      new Transfer[]{TextTransfer.getInstance()});

			}
		});
		menu.add(new Action(channel.getCommunityTitle()+"をお気に入りに追加"){
			@Override
			public void run() {
				FavoriteChannel favoriteChannel = new FavoriteChannel();
				favoriteChannel.setKeyword(channel.getCommunityTitle());
				
				boolean exist = false;
				for( FavoriteChannel favorite :checker.getFavoriteChannelList()){
					if( channel.getTitle().equals(favorite.getKeyword()) || channel.getCommunityTitle().equals(favorite.getKeyword())){
						exist = true;
					}
				}
				if( !exist ){
					checker.getFavoriteChannelList().add(favoriteChannel);
					checker.getWindow().favoriteListRefresh();
				}
			}
		});

		return menu;
	}
	
	public static void executeBrowser(String url) {
		log.debug("URLを開く "+url);
		
		String browserPath = Config.get().getString(Config.BROWSER_PATH);
		if( browserPath != null ){
			try {
				Runtime.getRuntime().exec( browserPath +" " + url);
				return;
			} catch (IOException e) {
				log.error("設定されたブラウザ("+browserPath+")での表示に失敗しました。");
				log.debug("デフォルトのブラウザでの表示を行います。");
			}
		}
		
	    // 拡張子が"html"のアプリケーションを開く。
	    Program program = Program.findProgram("html");
	    if( program == null ){
	    	program = Program.findProgram("htm");
	    }
	    
	    if (program != null) {
	        program.execute(url);
	    } else {
	        try {
	            Runtime.getRuntime().exec(
	                    new String[] { "rundll32.exe",
	                            "url.dll,FileProtocolHandler", url });
	        } catch (IOException e) {
	        	log.error("ブラウザ起動の失敗",e);
	        }
	    }
	}
	
	public void setInput( final List<LiveChannel> input ){
		tableViewer.setInput(input);
	}
	
	private class TableLabelProvider implements ITableLabelProvider , ITableColorProvider{
		
		@Override
		public String getColumnText(Object obj, int index) {
			LiveChannel channel = (LiveChannel)obj;
			switch( index ){
			case 0:
				return channel.getCommunityTitle();
			case 1:
				return channel.getTitle();
			case 2:
				String text = StringUtils.substringAfter(channel.getStartDateString(), "日").trim();
				return text;
			case 3:
				return String.valueOf(channel.getVisitorSize());
			case 4:
				return String.valueOf(channel.getCommentSize());
			case 5:
				return channel.getCategory();
			case 6:
				return channel.getComment();
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

		@Override
		public Color getBackground(Object arg0, int arg1) {
			return null;
		}

		@Override
		public Color getForeground(Object obj, int arg1) {
			LiveChannel channel = (LiveChannel)obj;
//			if( arg1 == 0 ){
//				log.debug("channel favorite="+channel.isFavorite() +" title="+channel.getTitle());
//			}
			if( channel.isFavorite() ){
				return redColor;
			}else{
				return normalColor;
			}
		}
	}
	
	private abstract class ColumnViewerSorter extends ViewerComparator {
		public static final int ASC = 1;
		
		public static final int NONE = 0;
		
		public static final int DESC = -1;
		
		private int direction = 0;
		
		private TableViewerColumn column;
		
		private ColumnViewer viewer;
		private boolean isRemoveDuplicate = false;
		
		public ColumnViewerSorter(ColumnViewer viewer, TableViewerColumn column) {
			this.column = column;
			this.viewer = viewer;
			isRemoveDuplicate = !"カテゴリ".equals(ColumnViewerSorter.this.column.getColumn().getText());
			
			this.column.getColumn().addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if( ColumnViewerSorter.this.viewer.getComparator() != null ) {
						if( ColumnViewerSorter.this.viewer.getComparator() == ColumnViewerSorter.this ) {
							int tdirection = ColumnViewerSorter.this.direction;
							if( tdirection == ASC ) {
								if( isRemoveDuplicate ){
									provider.setRemoveDuplicate(true);
								}
								setSorter(ColumnViewerSorter.this, DESC);
							} else if( tdirection == DESC ) {
								if( isRemoveDuplicate ){
									provider.setRemoveDuplicate(false);
								}
								setSorter(ColumnViewerSorter.this, NONE);
							}
						} else {
							if( isRemoveDuplicate ){
								provider.setRemoveDuplicate(true);
							}
							setSorter(ColumnViewerSorter.this, ASC);
						}
					} else {
						if( isRemoveDuplicate ){
							provider.setRemoveDuplicate(true);
						}
						setSorter(ColumnViewerSorter.this, ASC);
					}
				}
			});
		}
		
		public void setSorter(ColumnViewerSorter sorter, int direction) {
			if( direction == NONE ) {
				column.getColumn().getParent().setSortColumn(null);
				column.getColumn().getParent().setSortDirection(SWT.NONE);
				viewer.setComparator(null);
			} else {
				column.getColumn().getParent().setSortColumn(column.getColumn());
				sorter.direction = direction;
				
				if( direction == ASC ) {
					column.getColumn().getParent().setSortDirection(SWT.DOWN);
				} else {
					column.getColumn().getParent().setSortDirection(SWT.UP);
				}
				
				if( viewer.getComparator() == sorter ) {
					viewer.refresh();
				} else {
					viewer.setComparator(sorter);
				}
				
			}
		}

		public int compare(Viewer viewer, Object e1, Object e2) {
			return direction * doCompare(viewer, e1, e2);
		}
		
		protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
	}

}
