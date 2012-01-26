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
	
	public RemoteElement getParent() {
		return mParent;
	}
	
	public String getUrl() {
		return prepareAbsolutePath(new StringBuilder()).toString();
	}
	
	public String getName() {
		return mName;
	}
	
	public ConnectionHandler getConnectionHandler() {
		return mConnectionHandler;
	}
	
	public void setConnectionHandler(ConnectionHandler connectionHandler) {
		mConnectionHandler = connectionHandler;
	}
	
	public boolean isFullyLoaded() {
		return mFullyLoaded;
	}

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
