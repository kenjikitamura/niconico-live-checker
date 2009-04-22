package jp.rainbowdevil.niconicolivechecker.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class DataManager {
	
	public static final String DEFAULT_FILENAME = "favorite.dat";
	
	public List<FavoriteChannel> getFavoriteChannelList( String filename ) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream in = new ObjectInputStream( new FileInputStream( new File(filename)));
		List<FavoriteChannel> list = (List<FavoriteChannel>) in.readObject();
		in.close();
		return list;
	}
	
	public void writeFavoriteChannelList( List<FavoriteChannel> list , String filename ) throws IOException{
		ObjectOutputStream os = new ObjectOutputStream( new FileOutputStream( new File( filename )));
		os.writeObject(list);
		os.close();
		return ;
	}

}
