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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class Shares extends RemoteElement {
	
	public static void main(String[] args) throws IOException, ParseException, BadLoginException {
		AccountInfo accountInfo = new AccountInfo("", "");
		ConnectionHandler c = new ConnectionHandler(accountInfo);
		Shares s = new Shares(accountInfo, c);
		s.loadData();
		for (ShareRoom r: s.getShareRooms()) {
			System.out.println(r.getName());
			r.loadData();
			for (Dir d: r.getDirs()) {
				System.out.println("\t" + d.getName());
				d.loadData();
				for (File f: d.getFiles()) {
					System.out.println("\t\t" + f.getName());
				}
				/*
				InputStream out = c.getDownloadStream(d.getZipUrl());
				java.io.File fileOutput = new java.io.File("out.zip");
				System.out.println(d.getZipUrl());

				fileOutput.createNewFile();
				FileOutputStream fos = new FileOutputStream(fileOutput);
				int b;
				while((b = out.read()) != -1)
				    fos.write(b);
				fos.close();
				break;
*/
				
			}
		}
	}
	
	public static Shares create(AccountInfo accountInfo, ConnectionHandler connectionHandler) {
		return new Shares(accountInfo, connectionHandler);
	}
	
	private static String getSharesListUrl(AccountInfo accountInfo) {
		return String.format("%s%s", Storage.getStorageUrl(accountInfo), "shares");
	}

	public Shares(AccountInfo accountInfo, ConnectionHandler connectionHandler) {
		super(null, accountInfo.getUserName(), getSharesListUrl(accountInfo));
		setConnectionHandler(connectionHandler);
		mConnectionHandler = connectionHandler;
	}
	
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
