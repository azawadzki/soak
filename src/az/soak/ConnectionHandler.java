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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ConnectionHandler {

	final static int MAX_RETRY_NUMBER = 5;
	ConnectionHandler(AccountInfo accountInfo) {
		mAccountInfo = accountInfo;
		System.setProperty("http.maxRedirects", Integer.toString(MAX_RETRY_NUMBER));
		Authenticator.setDefault(new Auth());
	}
	
	class Auth extends Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(mAccountInfo.getUserName(), mAccountInfo.getPassword().toCharArray());
		}
	}
	
	public String performRequest(String url, String method) throws BadLoginException, IOException {
		URL u = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		try {
			conn.setRequestMethod(method);
			if (conn.getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
				throw new BadLoginException();
			}
			return new Scanner(conn.getInputStream()).useDelimiter("\\A").next();
		} finally {
			conn.disconnect();
		}
	}

	public InputStream getDownloadStream(String url) throws IOException, BadLoginException {
		URL u = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		conn.connect();
		if (conn.getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
			throw new BadLoginException();
		}
		return new BufferedInputStream(conn.getInputStream());
	}

	AccountInfo mAccountInfo;
}
