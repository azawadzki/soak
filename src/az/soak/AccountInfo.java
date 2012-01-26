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

import org.bitpedia.util.Base32;

public class AccountInfo {
	
	public final static String SITE_ROOT = "https://spideroak.com";
	public final static String STORAGE_ROOT = "https://spideroak.com/storage/";
	public final static String SHARES_ROOT = "https://spideroak.com/share/";

	AccountInfo(String userName, String password) {
		mUserName = userName;
		mPassword = password;
		mEncodedUserName = Base32.encode(mUserName.getBytes());
	}
	
	public String getUserName() {
		return mUserName;
	}
	
	public String getPassword() {
		return mPassword;
	}
	
	public String getStorageUrl() {
		return String.format("%s%s/", STORAGE_ROOT, mEncodedUserName);
	}
	
	public String getSharesListUrl() {
		return getStorageUrl() + "shares";
	}

	String mUserName;
	String mEncodedUserName;
	String mPassword;
}
