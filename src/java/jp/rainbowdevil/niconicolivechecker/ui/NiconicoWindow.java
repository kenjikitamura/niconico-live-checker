package jp.rainbowdevil.niconicolivechecker.ui;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jp.rainbowdevil.niconicolivechecker.NiconicoLiveChecker;
import jp.rainbowdevil.niconicolivechecker.config.Config;
import jp.rainbowdevil.niconicolivechecker.data.ChannelListUpdateEvent;
import jp.rainbowdevil.niconicolivechecker.data.Connector;
import jp.rainbowdevil.niconicolivechecker.data.FavoriteChannel;
import jp.rainbowdevil.niconicolivechecker.data.LiveChannel;
import jp.rainbowdevil.niconicolivechecker.data.StateChangeListener;
import jp.rainbowdevil.niconicolivechecker.ui.action.ExitAction;
import jp.rainbowdevil.niconicolivechecker.ui.action.OpenPreferenceAction;
import jp.rainbowdevil.niconicolivechecker.ui.action.OutputSoundAction;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

/**
 * ニコニコユーザ生放送チェッカーメインウインドウ
 * @author KENJI
 *
 */
public class NiconicoWindow extends ApplicationWindow{
	
	/** 生放送チャンネルリスト */
	private ChannelListComposite channelListComposite;
	
	/** お気に入りリスト */
	private FavoriteComposite favoriteComposite;
	
	/** タスクトレイアイテム */
	private TrayItem trayItem;
	
	/** アプリケーションアイコン */
	private Image icon;
	private CTabFolder tabFolder;
	
	/** メイン処理クラス */
	private NiconicoLiveChecker checker;
	
	private java.util.Map<String,String> notifiedUrlSet;
	
	private String liveChennelsTitle = "放送中リスト";
	private CTabItem liveChannelsTabItem ;
	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(NiconicoWindow.class);
	
	public NiconicoWindow( NiconicoLiveChecker checker ) {
		super(null);
		this.checker = checker;
		addMenuBar();
		addStatusLine();
		notifiedUrlSet = new HashMap<String,String>();
	}
	
	
	/**
	 * メニューの作成
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("");
		
		MenuManager fileMenu = new MenuManager("File");
		fileMenu.add(new Action("今すぐリスト更新"){
			@Override
			public void run() {
				updateList();
			}
		});
		
		fileMenu.add(new Separator());
		fileMenu.add( new ExitAction(this) );
		menuManager.add(fileMenu);
		
		MenuManager favoriteMenu = new MenuManager("お気に入り");
		menuManager.add(favoriteMenu);
		favoriteMenu.add(new Action("お気に入りの追加"){
			@Override
			public void run() {
				InputDialog dialog = new InputDialog( getShell() , NiconicoLiveChecker.APP_NAME , "お気に入りを追加します。\nコミュニティ名か、キーワードを設定してください。コミュニティ名や放送タイトルに入力したキーワードが含まれている場合に通知が行われます。\n\n例：「○○のgdgd放送コミュニティ」「ゲーム」など","",null);
				int ret = dialog.open();
				if( ret == InputDialog.OK ){
					String input = dialog.getValue();
					log.debug("お気に入り="+input);
					if( input.trim().length() != 0 ){
						FavoriteChannel favoriteChannel = new FavoriteChannel();
						favoriteChannel.setKeyword(input.trim());
						checker.getFavoriteChannelList().add(favoriteChannel);
						favoriteComposite.refresh();
						checker.writeFavoriteFile();
						
					}
				}
			}
		});
		favoriteMenu.add(new Action("お気に入りの削除"){
			@Override
			public void run() {
				MessageDialog.openInformation(getShell(), NiconicoLiveChecker.APP_NAME, "削除はお気に入りをクリックして選択してからDeleteキーを押してね！");
			}
		});
		
		MenuManager configMenu = new MenuManager("設定");
		configMenu.add( new OutputSoundAction(this));
		configMenu.add( new OpenPreferenceAction(this));
		menuManager.add(configMenu);
		
		return menuManager;
	}
	
	
	
	/**
	 * 生放送リストを取得し治す
	 */
	public void updateList(){
		
		setStatesOtherThread("読み込み開始...");
		
		Runnable task = new Runnable(){
			public void run() {
				Connector connector = new Connector();
				try {
					final List<LiveChannel> list = connector.getAllChannelList( new StateChangeListener(){
						@Override
						public void handleStateChange(
								ChannelListUpdateEvent event) {
							setStatesOtherThread(event.getMessage());
						}
					});
					checker.setCurrentLiveChannelList(list);
					log.debug("list size = "+list.size());
//					for (LiveChannel liveChannel : list) {
//						log.debug("title="+liveChannel.getTitle());
//					}

					// リスト更新
					Display.getDefault().asyncExec(new Runnable(){
						@Override
						public void run() {
							channelListComposite.setInput(list);
							liveChannelsTabItem.setText(liveChennelsTitle+"("+list.size() +")");
						}
					});
					
					// 読み込み中を表すステータスラインのメッセージをクリア
					setStatesOtherThread("");
					
					// 読み込みが完了した生放送チャンネルリストの中にお気に入りが含まれているかチェック
					checkFavorite( );
					
				} catch( UnknownHostException e){
					setStatesOtherThread("更新エラー ニコニコ動画のサイトにアクセスできません。ネットにつながるか確認してください。"+e.getMessage());
					log.error("error",e);
				} catch (HttpException e) {
					setStatesOtherThread("更新エラー ニコニコ動画のサイトにアクセスできません。"+e.getMessage());
					log.error("error",e);
				} catch (IOException e) {
					setStatesOtherThread("更新エラー ニコニコ動画のサイトにアクセスできません。"+e.getMessage());
					log.error("error",e);
				}
			}
		};
		
		checker.getExecutor().submit(task);
	}
	
	/**
	 * 現在のチャンネルリストの中にお気に入りが含まれているかチェックする
	 */
	private void checkFavorite( ){
		
		// お気に入りが含まれていたかどうかのフラグ
		boolean hit = false;
		
		for (LiveChannel channel  : checker.getCurrentLiveChannelList()) {
			for( FavoriteChannel favorite : checker.getFavoriteChannelList() ){
				if( favorite.getKeyword().length() == 0 ){
					continue;
				}
				if( channel.getTitle().indexOf(favorite.getKeyword()) != -1  ||
					 channel.getCommunityTitle().indexOf(favorite.getKeyword()) != -1 ){

					channel.setFavorite(true);
					log.debug("setFavorite true "+channel.getTitle()+" "+channel.getCommunityTitle());
					
					if( !notifiedUrlSet.containsKey(channel.getUrl() )){
						log.debug("hit!! "+channel.getTitle() + " url="+channel.getUrl() + " containsKey?="+ notifiedUrlSet.containsKey(channel.getUrl() ));
						notifiedUrlSet.put(channel.getUrl(),channel.getTitle());
//						for( String str : notifiedUrlSet.keySet()){
//							log.debug("通知済み "+str);
//						}
						hit = true;
						final String msg = channel.getTitle()+"が"+channel.getStartDateString()+"から放送開始しています。";
						showTooltip(msg,channel);
						setStatesOtherThread(msg);
					}else{
						log.debug("hitしたが通知済み "+channel.getTitle()+" "+channel.getCommunityTitle());
					}
				}
			}
		}
		
		if( hit && checker.getConfig().getBoolean(Config.OUTPUT_SOUND_NOTIFY) ){
			SoundPlayer player = new SoundPlayer();
			player.play(NiconicoWindow.class.getResourceAsStream("/start.wav"));
		}
	}
	
	
	/**
	 * 他のスレッドからウインドウのステータスバーのメッセージを更新する 
	 * @param msg
	 */
	private void setStatesOtherThread( final String msg ){
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				setStatus(msg);
			}
		});
	}
	
	/**
	 * タスクトレイにバルーンメッセージを表示する
	 * Vistaでは表示されない？
	 * @param msg
	 */
	private void showTooltip( final String msg , final LiveChannel channel){
		getShell().getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				ToolTip tip = new ToolTip(getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
				tip.addSelectionListener(new SelectionAdapter(){
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						ChannelListComposite.executeBrowser(channel.getUrl());
					}
				});
				tip.setText(NiconicoLiveChecker.APP_NAME);
				tip.setMessage(msg);
				tip.setAutoHide(true);
				trayItem.setToolTip(tip);
				tip.setVisible(true);
				
			}
		});
	}
	
	
	@Override
	protected Control createContents(Composite parent) {
		Composite top = new Composite( parent , SWT.NULL);
		top.setLayout(new FillLayout());
		tabFolder = new CTabFolder(top,SWT.BORDER);
		tabFolder.setSimple(false);
		liveChannelsTabItem = new CTabItem( tabFolder , SWT.NONE );
		liveChannelsTabItem.setText(liveChennelsTitle);
		channelListComposite = new ChannelListComposite(tabFolder,SWT.BORDER,checker);
		liveChannelsTabItem.setControl(channelListComposite);
		
		CTabItem tabItem2 = new CTabItem( tabFolder , SWT.NONE );
		tabItem2.setText("お気に入り");
		favoriteComposite = new FavoriteComposite(tabFolder,SWT.BORDER , checker );
		favoriteComposite.setInput(checker.getFavoriteChannelList());
		tabItem2.setControl(favoriteComposite);
		
		icon = new Image(getShell().getDisplay(),NiconicoLiveChecker.class.getResourceAsStream("icon.png"));
		getShell().setImage(icon);
		Tray tray = getShell().getDisplay().getSystemTray();
		if (tray != null){
			trayItem = new TrayItem(tray, SWT.NONE);
			trayItem.setImage( icon );
			trayItem.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if( !getShell().isVisible() ){
						getShell().setVisible(true);
						getShell().setMinimized(false);
					}
				}
			});
		}

		checker.startTimer();
		return top;
	}
	
	public void favoriteListRefresh(){
		favoriteComposite.refresh();
	}
	

	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(700,500);
		shell.setText(NiconicoLiveChecker.APP_NAME);
		
		shell.addShellListener(new ShellAdapter(){
			@Override
			public void shellIconified(ShellEvent event) {
				getShell().setVisible(false);
				boolean flg = false;
				

				flg = Config.get().getBoolean(Config.FIRST_TIME_MINIMUM_NOTIFY);

				
				if( !flg ){
					String msg = "最小化時はタスクバーに入ります。\n元のサイズに戻す場合はアイコンをクリックしてください。";
					ToolTip tip = new ToolTip(getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
					tip.setText(NiconicoLiveChecker.APP_NAME);
					tip.setMessage(msg);
					tip.setAutoHide(true);
					trayItem.setToolTip(tip);
					tip.setVisible(true);
					Config.get().setBoolean(Config.FIRST_TIME_MINIMUM_NOTIFY, true);
					try {
						Config.get().save();
					} catch (ConfigurationException e) {
						log.error("設定の保存に失敗",e);
					}
				}
			}
			
		});
		
		//------------------------------------------------------
		// 起動時に非表示の設定
		if( Config.get().getBoolean(Config.TASKTRAY_START) ){
			log.debug("非表示で開始する");
			shell.setVisible(false);
			shell.setMinimized(true);
		}
		//------------------------------------------------------
	}
}
