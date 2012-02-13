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
import java.util.Date;
import java.util.Map;

/** File objects encapsulate files stored on remote server.
 */
public class File extends RemoteElement {

	@SuppressWarnings("rawtypes")
	static File create(RemoteElement parent, Object objectTree) {
		Map m = (Map) objectTree;
		String name = (String) m.get("name");
		String urlComponent = (String) m.get("url");
		long size = ((Number) m.get("size")).longValue();
		Date creationTime = new Date(((Number)m.get("ctime")).longValue() * 1000);
		Date modificationTime = new Date(((Number)m.get("mtime")).longValue() * 1000);
		Number v = (Number) m.get("versions");
		int versionsNumber = (v != null) ? v.intValue() : 1; // not available in share room files
		return new File(parent, name, urlComponent, size, creationTime, modificationTime, versionsNumber);
	}

	private File(RemoteElement parent, String name, String urlComponent, long size, Date creationTime, Date modificationTime, int versionsNumber) {
		super(parent, name, urlComponent);
		mSize = size;
		mCreationTime = creationTime;
		mModificationTime = modificationTime;
		mVersionsNumber = versionsNumber;
	}
	
	/** Fetch URL which lets external, not logged in users, download given file.
	 * @return URL which permits for external download.
	 * @throws IOException Thrown if network error occurred when fetching the URL.
	 */
	public String getExternalDownloadUrl() throws BadLoginException, IOException {
		// TODO: cache the url and handle its expiration
		try {
			String path = performPost(getUrl());
			assert(path.charAt(0) == '/');
			return String.format("%s/%s", AccountInfo.getSiteRoot(), path);
		} catch (BadLoginException e) {
			// actually, this shouldn't happen as there is no logging in involved.
			throw new IOException(e);
		}
	}

	/** Get file size in bytes.
	 * @return File size.
	 */
	public long getSize() {
		return mSize;
	}

	/** Get file creation time.
	 * @return Creation time.
	 */
	public Date getCreationTime() {
		return mCreationTime;
	}

	/** Get file modification time.
	 * @return Modification time.
	 */
	public Date getModificationTime() {
		return mModificationTime;
	}

	/** Get number of available backup versions of the file.
	 * Fetching backed up versions is not supported.
	 * @return Number of backed up file versions.
	 */
	public int getVersionsNumber() {
		return mVersionsNumber;
	}

	
	
	@Override
	protected void initWithData(Object objectTree) {
		throw new Error("Forbidden method call for File class.");
	}
	
	@Override
	protected boolean containsLoadableContent() {
		return false;
	}

	private long mSize;
	private Date mCreationTime;
	private Date mModificationTime;
	private int mVersionsNumber;
}
