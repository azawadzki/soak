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

import org.json.simple.JSONValue;

/** RemoteElement is the base class for all elements fetched from SpiderOak server.
 * When working with RemoteElement instances you must strictly follow one rule: in order
 * to access object data other than name and URL, you must call loadData() method first.
 * This paradigm ensures that data is loaded only when needed. If that wouldn't be the
 * case, when iterating over directories you would need to implicitly load all file
 * data killing your application's performance.
 */
abstract class RemoteElement {

	RemoteElement(RemoteElement parent, String name, String urlComponent) {
		mConnectionHandler = (parent != null) ? parent.mConnectionHandler : null;
		mParent = parent;
		mName = name;
		mUrlComponent = urlComponent;
	}
	
	public void loadData() throws BadLoginException, IOException {
		if (containsLoadableContent() && !isFullyLoaded()) {
			String data = performGet(getUrl());
			initWithData(JSONValue.parse(data));
			mFullyLoaded = true;
		}
	}
	
	/** Get parent of current element
	 * @return Reference to parent element or null in case of top-level elements.
	 */
	public RemoteElement getParent() {
		return mParent;
	}
	
	/** Get URL of current element. The URL can be used to download the element with ConnectionHandler.
	 * @return URL of the element.
	 */
	public String getUrl() {
		return prepareAbsolutePath(new StringBuilder()).toString();
	}
	
	/** Get name of the element (e.g. filename, dirname, device name).
	 * @return Name of the element.
	 */
	public String getName() {
		return mName;
	}
	
	/** Get ConnectionHandler which is used for fetching the element data.
	 * @return ConnectionHandler which is used for fetching the element data.
	 */
	public ConnectionHandler getConnectionHandler() {
		return mConnectionHandler;
	}
	
	/** Assign ConnectionHandler which is used for fetching the element data. This method can be used
	 * to change connection handler in case e.g. a file should be downloaded by specialized class.
	 * @param connectionHandler ConnectionHandler which is used for fetching the element data. 
	 */
	public void setConnectionHandler(ConnectionHandler connectionHandler) {
		mConnectionHandler = connectionHandler;
	}
	
	/** Check if the element has been fully initialized with loadData() method so that all
	 * of its getters can be called. In case the element was not fully loaded, usually only
	 * name and URL can be accessed.
	 * @return Flag which stated whether all of the element data is fully available or not.
	 */
	public boolean isFullyLoaded() {
		return mFullyLoaded;
	}

	/** Method used to initialize the object with data fetched from server. The data is provided
	 * in the form of JSON ojectTree.
	 * @param objectTree JSON data with which the object should be initialized.
	 */
	abstract protected void initWithData(Object objectTree);
	
	protected boolean containsLoadableContent() {
		return true;
	}
	
	protected StringBuilder prepareAbsolutePath(StringBuilder builder) {
		if (mParent != null) {
			mParent.prepareAbsolutePath(builder);
		}
		builder.append(mUrlComponent);
		return builder;
	}
	
	protected String performGet(String address) throws BadLoginException, IOException {
		return performRequest(address, "GET");
	}
	
	protected String performPost(String address) throws BadLoginException, IOException {
		return performRequest(address, "POST");
	}

	private String performRequest(String address, String method) throws BadLoginException, IOException {
		return mConnectionHandler.performRequest(address, method);
	}	

	private RemoteElement mParent;
	private String mUrlComponent;
	private String mName;
	private ConnectionHandler mConnectionHandler;
	private boolean mFullyLoaded;
}
