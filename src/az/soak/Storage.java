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

/** Storage class objects provide access to devices registered on SpiderOak.
 */
public class Storage extends RemoteElement {
	
	private final static String ROOT = "https://spideroak.com/storage/";
	
	/** Get SpiderOak API base URL of given user storage.
	 * @param SpiderOak API storage URL of given user.
	 */
	public static String getStorageUrl(AccountInfo accountInfo) {
		return String.format("%s%s/", ROOT, accountInfo.getEncodedUserName());
	}
	
	/** Create Storage object, which provides access to devices registered on SpiderOak.
	 * @param accountInfo Authorization data of current user.
	 * @param connectionHandler Handler which will be used to retrieve underlying data.
	 */
	public Storage(AccountInfo accountInfo, ConnectionHandler connectionHandler) {
		super(null, accountInfo.getUserName(), getStorageUrl(accountInfo));
		setConnectionHandler(connectionHandler);
	}
	
	/** Get a list of devices that user registered on SpiderOak.
	 * @return List of Dir objects that designate registered devices.
	 */
	public List<Dir> getDevices() {
		return Collections.unmodifiableList(mDevices);
	}

	/** Get user first name as registered on SpiderOak.
	 * @return User first name
	 */
	public String getFirstName() {
		return mFirstName;
	}
	
	/** Get user last name as registered on SpiderOak.
	 * @return User last name
	 */
	public String getLastName() {
		return mLastName;
	}

	/** Get size of available storage space (in bytes).
	 * @return Total storage space.
	 */
	public long getTotalSize() {
		return mTotalSize;
	}

	/** Get size of storage space already in use (in bytes).
	 * @return Storage space in use.
	 */
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
	
	private long parseTotalSize(Number totalSize) {
		return totalSize.longValue() * (long) Math.pow(2, 30);
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
