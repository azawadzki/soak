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
import java.sql.Date;
import java.util.Map;


public class File extends RemoteElement {

	@SuppressWarnings("rawtypes")
	public static File create(RemoteElement parent, Object objectTree) {
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
	
	public String getExternalDownloadUrl() throws BadLoginException, IOException {
		// TODO: cache the url and handle its expiration
		String path = performPost(getUrl());
		assert(path.charAt(0) == '/');
		return AccountInfo.SITE_ROOT + path;
	}
	
	@Override
	protected void initWithData(Object objectTree) {
		throw new Error("Forbidden method call for File class.");
	}
	
	@Override
	protected boolean containsLoadableContent() {
		return false;
	}

	public long getSize() {
		return mSize;
	}

	public Date getCreationTime() {
		return mCreationTime;
	}

	public Date getModificationTime() {
		return mModificationTime;
	}

	public int getVersionsNumber() {
		return mVersionsNumber;
	}

	private long mSize;
	private Date mCreationTime;
	private Date mModificationTime;
	private int mVersionsNumber;
}
