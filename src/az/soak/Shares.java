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

/** Shares class objects provide access to SpiderOak share rooms.
 */
public class Shares extends RemoteElement {

	private static String getSharesListUrl(AccountInfo accountInfo) {
		return String.format("%s%s", Storage.getStorageUrl(accountInfo), "shares");
	}

	/** Create Shares object, which provides access to SpiderOak share rooms
	 * @param accountInfo Authorization data of current user.
	 * @param connectionHandler Handler which will be used to retrieve underlying data.
	 */
	public Shares(AccountInfo accountInfo, ConnectionHandler connectionHandler) {
		super(null, accountInfo.getUserName(), getSharesListUrl(accountInfo));
		setConnectionHandler(connectionHandler);
		mConnectionHandler = connectionHandler;
	}
	
	/** Get a list of share rooms that user defined on SpiderOak.
	 * @return List of ShareRoom objects.
	 */
	public List<ShareRoom> getShareRooms() {
		return Collections.unmodifiableList(mShareRooms);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initWithData(Object objectTree) {
		String shareId = (String)((Map)(objectTree)).get("share_id");
		mShareRooms = new ArrayList<ShareRoom>();
		List rooms = (List)((Map)(objectTree)).get("share_rooms");
		for (Object o: rooms) {
			mShareRooms.add(ShareRoom.create(mConnectionHandler, shareId, o));
		}
	}
	
	// deferred loading
	private List<ShareRoom> mShareRooms;
	private ConnectionHandler mConnectionHandler;
}
