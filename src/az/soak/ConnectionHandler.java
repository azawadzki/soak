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

/** ConnectionHandler objects are used to access SpiderOak server and retrieve data in uniform manner.
 * The handler uses HTTP-based authentication, cookie-based access is not supported.
 */
public class ConnectionHandler {

	// number of times this class will retry connecting SpiderOak servers in case errors.
	final static int MAX_RETRY_NUMBER = 5;
	
	public ConnectionHandler(AccountInfo accountInfo) {
		mAccountInfo = accountInfo;
		System.setProperty("http.maxRedirects", Integer.toString(MAX_RETRY_NUMBER));
		Authenticator.setDefault(new Auth());
	}
	
	private class Auth extends Authenticator {
		
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(mAccountInfo.getUserName(), mAccountInfo.getPassword().toCharArray());
		}
	}
	
	/** Perform HTTP request of given type with submitted url.
	 * @param url URL which the request will access.
	 * @param method Type of request: POST, GET.
	 * @return Upon completion the method returns data retrieved from the server.
	 * @throws IOException Thrown if network error occurred or bad method type was used.
	 * @throws BadLoginException Thrown in case of login/user name mismatch.
	 */
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

	/** Get InputStream object which gives access to data designated by URL.
	 * @param url Address of object to retrieve
	 * @return Stream which gives access to data designated by URL
	 * @throws IOException Thrown if network error occurred.
	 * @throws BadLoginException Thrown in case of login/user name mismatch.
	 */
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
