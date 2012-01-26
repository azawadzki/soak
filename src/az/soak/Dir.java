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

public class Dir extends RemoteElement {

	@SuppressWarnings("rawtypes")
	public static Dir create(RemoteElement parent, Object objectTree) {
		List l = (List) objectTree;
		assert(l.size() == 2);
		String name = (String) l.get(0);
		String urlComponent = (String) l.get(1);
		return new Dir(parent, name, urlComponent);
	}

	protected Dir(RemoteElement parent, String name, String urlComponent) {
		super(parent, name, urlComponent);
	}
	
	public List<Dir> getDirs() {
		return Collections.unmodifiableList(mDirs);
	}
	
	public List<File> getFiles() {
		return Collections.unmodifiableList(mFiles);
	}
	
	public String getZipUrl() {
		return getUrl() + "?format=zip";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void initWithData(Object objectTree) {
		mDirs = new ArrayList<Dir>();
		List dirElems = (List)((Map)(objectTree)).get("dirs");
		for (Object o: dirElems) {
			mDirs.add(Dir.create(this, o));
		}
		mFiles = new ArrayList<File>();
		List fileElems = (List)((Map)(objectTree)).get("files");
		for (Object o: fileElems) {
			mFiles.add(File.create(this, o));
		}
	}
	
	// deferred loading
	private List<Dir> mDirs;
	private List<File> mFiles;
}
