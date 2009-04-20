package jp.rainbowdevil.niconicolivechecker.ui.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jp.rainbowdevil.niconicolivechecker.data.LiveChannel;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.internal.ole.win32.LICINFO;

public class ChannelListContentProvider implements IStructuredContentProvider {
	
	private List<LiveChannel> channelList;
	
	private boolean isRemoveDuplicate = false;
	
	/** Logインスタンスを取得 */
	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ChannelListContentProvider.class);

	@Override
	public Object[] getElements(Object arg0) {
		List<LiveChannel> list = new ArrayList<LiveChannel>((List<LiveChannel>)arg0);
		
		if( isRemoveDuplicate ){
			List<LiveChannel> deleteList = new ArrayList<LiveChannel>();
			HashSet<String> existUrl = new HashSet<String>();
			for( LiveChannel channel : list ){
				if( existUrl.contains(channel.getUrl()) ){
					deleteList.add(channel);
				}else{
					existUrl.add(channel.getUrl());
				}
			}
			for( LiveChannel channel : deleteList ) {
				list.remove(channel);
				//log.debug("remove "+channel.getTitle());
			}
		}
		
		return list.toArray(new LiveChannel[list.size()]);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	public List<LiveChannel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<LiveChannel> channelList) {
		this.channelList = channelList;
	}

	public boolean isRemoveDuplicate() {
		return isRemoveDuplicate;
	}

	public void setRemoveDuplicate(boolean isRemoveDuplicate) {
		this.isRemoveDuplicate = isRemoveDuplicate;
	}

}
