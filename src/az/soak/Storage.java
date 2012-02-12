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

public class Storage extends RemoteElement {
	
	private final static String ROOT = "https://spideroak.com/storage/";

	public static void main(String[] args) throws IOException, ParseException, BadLoginException {
		AccountInfo accountInfo = new AccountInfo("", "");
		ConnectionHandler c = new ConnectionHandler(accountInfo);
		Storage s = new Storage(accountInfo, c);
		s.loadData();
		for (Dir d1: s.getDevices()) {
			System.out.println(d1.getName());
			d1.loadData();
			for (Dir d2: d1.getDirs()) {
				System.out.println("\t" + d2.getName());
				d2.loadData();
				for (File f3: d2.getFiles()) {
					System.out.println("\t\t" + f3.getUrl());
				}
			}

		}
	}
	
	public static Storage create(AccountInfo accountInfo, ConnectionHandler connectionHandler) {
		return new Storage(accountInfo, connectionHandler);
	}
	
	public static String getRoot() {
		return ROOT;
	}
	
	public static String getStorageUrl(AccountInfo accountInfo) {
		return String.format("%s%s/", ROOT, accountInfo.getEncodedUserName());
	}
	
	public Storage(AccountInfo accountInfo, ConnectionHandler connectionHandler) {
		super(null, accountInfo.getUserName(), getStorageUrl(accountInfo));
		setConnectionHandler(connectionHandler);
	}
	
	private long parseTotalSize(Number totalSize) {
		return totalSize.longValue() * (long) Math.pow(2, 30);
	}
	
	public List<Dir> getDevices() {
		return Collections.unmodifiableList(mDevices);
	}

	public String getFirstName() {
		return mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public long getTotalSize() {
		return mTotalSize;
	}

	public long getBackupSize() {
		return mBackupSize;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void initWithData(Object objectTree) {
		Map stats = (Map) ((Map)(objectTree)).get("stats");
		mFirstName = (String) stats.get("firstname");
		mLastName = (String) stats.get("lastname");
		mTotalSize = parseTotalSize((Number) stats.get("size"));
		mBackupSize = parseBackupSize((String) stats.get("backupsize"));

		mDevices = new ArrayList<Dir>();
		List devElems = (List)((Map)(objectTree)).get("devices");
		for (Object o: devElems) {
			mDevices.add(Dir.create(this, o));
		}
	}
	
	private long parseBackupSize(String val) {
		String[] fields = val.split(" ");
		if (fields.length == 0) {
			return -1;
		}
		double mantissa = Double.parseDouble(fields[0]);
		long powOf2 = 1;
		String unit = fields[1];
		if (unit.contains("KB")) {
			powOf2 = 10;
		} else if (unit.contains("MB")) {
			powOf2 = 20;
		} else if (unit.contains("GB")) {
			powOf2 = 30;
		} else if (unit.contains("TB")) {
			powOf2 = 40;
		}
		return (long) mantissa * (long) Math.pow(2, powOf2);
	}
	
	
	// deferred loading
	List<Dir> mDevices;
	private String mFirstName;
	private String mLastName;
	private long mTotalSize;
	private long mBackupSize;
}
