/*******************************************************************************
 * Copyright (c) 2012, Andrzej Zawadzki (azawadzki@gmail.com)
 *  
 * soak is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *  
 * soak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with soak; if not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package az.soak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bitpedia.util.Base32;

public class ShareRoom extends RemoteElement {

	private final static String ROOT = "https://spideroak.com/share/";
	
	@SuppressWarnings("rawtypes")
	public static ShareRoom create(ConnectionHandler connectionHandler, String shareId, Object objectTree) {
        Map m = (Map) objectTree;
		String roomKey = (String) m.get("room_key");
		String url = generateUrl(shareId, roomKey);
		String roomName = (String) m.get("room_name");
		String roomDescription = (String) m.get("room_description");

		return new ShareRoom(connectionHandler, roomName, url, roomKey, roomDescription);
	}
	
	public static ShareRoom create(String path, AccountInfo accountInfo, ConnectionHandler connectionHandler) {
		return null;
	}
	
	private ShareRoom(ConnectionHandler connectionHandler, String roomName, String url, String roomKey, String roomDescription) {
		super(null, roomName, url);
		setConnectionHandler(connectionHandler);
		mRoomKey = roomKey;
		mRoomDescription = roomDescription;
	}
	
	public List<Dir> getDirs() {
		return Collections.unmodifiableList(mDirs);
	}
	
	public String firstName() {
		return mFirstName;
	}
	
	public String lastName() {
		return mLastName;
	}
	
	public long getNumberOfFolders() {
		return mNumberOfFolders;
	}
	
	public long getNumberOfFiles() {
		return mNumberOfFiles;
	}
	
	public String getRoomKey() {
		return mRoomKey;
	}
	
	public String getRoomDescription() {
		return mRoomDescription;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void initWithData(Object objectTree) {
		Map stats = (Map) ((Map)(objectTree)).get("stats");
		mFirstName = (String) stats.get("firstname");
		mLastName = (String) stats.get("lastname");
		mNumberOfFiles = ((Number) stats.get("number_of_files")).longValue();
		mNumberOfFolders = ((Number) stats.get("number_of_folders")).longValue();

		mDirs = new ArrayList<Dir>();
		List dirElems = (List)((Map)(objectTree)).get("dirs");
		for (Object o: dirElems) {
			mDirs.add(Dir.create(this, o));
		}
	}
	
	public static String getRoot() {
		return ROOT;
	}
	
	
	private static String generateUrl(String shareId, String roomKey) {
		return String.format("%s%s/%s/", getRoot(), Base32.encode(shareId.getBytes()), roomKey);
	}

	// available immediately
	private String mRoomKey;
	private String mRoomDescription;
	
	// deferred loading
	private List<Dir> mDirs;
	private String mFirstName;
	private String mLastName;
	private long mNumberOfFolders;
	private long mNumberOfFiles;
}
