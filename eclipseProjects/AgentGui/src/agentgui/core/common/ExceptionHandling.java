/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.common;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * The Class ExceptionHandling provides some methods to deal with
 * exceptions (e. g. in order to get the line of  as String).
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ExceptionHandling {
	
	/**
	 * Returns the first line of an exception.
	 *
	 * @param exception the exception
	 * @return the first line of exception
	 */
	public static String getFirstTextLineOfException(Exception exception) {
		String firstLine = getExceptionAsString(exception);
		if (firstLine!=null) {
			int cutAt = firstLine.indexOf(System.getProperty("line.separator"));
			if (cutAt>0) {
				firstLine = firstLine.substring(0, cutAt);
			}
		}
		return firstLine;
	}
	
	/**
	 * Returns an exception as string.
	 *
	 * @param exception the exception
	 * @return the string from exception
	 */
	public static String getExceptionAsString(Exception exception) {

		if (exception == null) return null;

		StringWriter stringWriter = null;
		PrintWriter printWriter = null;
		try {
			stringWriter = new StringWriter();

			printWriter = new PrintWriter(stringWriter);
			exception.printStackTrace(printWriter);
			return stringWriter.toString();
			
		} finally {
			printWriter.flush();
			stringWriter.flush();
		}
		
	}
	
}
