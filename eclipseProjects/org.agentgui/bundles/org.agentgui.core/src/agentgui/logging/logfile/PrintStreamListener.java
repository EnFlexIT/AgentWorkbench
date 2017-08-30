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
package agentgui.logging.logfile;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * The listener interface for receiving printStream events.
 * The class that is interested in processing a printStream
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPrintStreamListener<code> method. When
 * the printStream event occurs, that object's appropriate
 * method is invoked.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PrintStreamListener extends PrintStream {

	/**
	 * The Enum PrintStreamListenerType.
	 */
	public enum PrintStreamListenerType {
		SystemOutput,
		SystemError,
	}
	
	private SysOutScanner sos = null;
	private PrintStreamListenerType listenerType;
	
	
	/**
	 * Constructor for remote outputs.
	 *
	 * @param outputStream the output stream
	 * @param scanner the scanner
	 * @param listenerType the listener type
	 */
	public PrintStreamListener(OutputStream outputStream, SysOutScanner scanner, PrintStreamListenerType listenerType) {
		super(outputStream);
		this.sos = scanner;
		this.listenerType = listenerType;
	}
	
	/* (non-Javadoc)
	 * @see java.io.PrintStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] buf, int off, int len) {
		// --- Some Error handling ------------------------
		if (buf == null) {
		    throw new NullPointerException();
		} else if ((off < 0) || (off > buf.length) || (len < 0) ||
			   ((off + len) > buf.length) || ((off + len) < 0)) {
		    throw new IndexOutOfBoundsException();
		} else if (len == 0) {
		    return;
		}
		
		byte[] byteArray = new byte[len]; 
		for (int i = 0 ; i < len ; i++) {
			byteArray[i] = buf[off + i];
		}

		// --- Create the log file output -----------------
		if (sos!=null) {
			synchronized(sos) {
				this.sos.append2Stack(new PrintStreamListenerOutput(this.listenerType,  new String(byteArray)));
			}
		}

		// --- Do regular task ----------------------------
		super.write(buf, off, len);
		
	}
	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) {
		this.write(b, 0, b.length);
	}
	/* (non-Javadoc)
	 * @see java.io.PrintStream#write(int)
	 */
	@Override
	public void write(int b) {
		this.write(new byte[] {(byte) b});
	}
	
}
