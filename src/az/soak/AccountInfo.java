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
	
	private final static String SITE_ROOT = "https://spideroak.com";

	public AccountInfo(String userName, String password) {
		mUserName = userName;
		mPassword = password;
		mEncodedUserName = Base32.encode(mUserName.getBytes());
	}
	
	public String getUserName() {
		return mUserName;
	}
	
	public String getEncodedUserName() {
		return mEncodedUserName;
	}
	
	public String getPassword() {
		return mPassword;
	}

	public static String getSiteRoot() {
		return SITE_ROOT;
	}
	
	String mUserName;
	String mEncodedUserName;
	String mPassword;
}
