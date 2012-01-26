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

public class BadLoginException extends Exception {

	private static final long serialVersionUID = 7195189976794835669L;

	public BadLoginException() {
		super();
	}
	
    public BadLoginException(String message) {
    	super(message);
    }
    
    public BadLoginException(String message, Throwable cause) {
    	super(message, cause);
    }
    
    public BadLoginException(Throwable cause) {
    	super(cause);
    }

}
