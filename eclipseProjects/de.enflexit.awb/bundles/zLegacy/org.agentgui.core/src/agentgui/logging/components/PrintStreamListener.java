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
package agentgui.logging.components;

import java.io.OutputStream;
import java.io.PrintStream;

public class PrintStreamListener extends PrintStream {

	public final static String SystemOutput = "[SysOut]";
	public final static String SystemError = "[SysErr]";
	
	private SysOutScanner sos = null;
	private String prefix2use = null;
	
	
	/**
	 * Constructor for remote outputs.
	 *
	 * @param outputStream the output stream
	 * @param scanner the scanner
	 * @param prefix the prefix
	 */
	public PrintStreamListener(OutputStream outputStream, SysOutScanner scanner, String prefix) {
		super(outputStream);
		this.sos = scanner;
		this.prefix2use = prefix;
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
		// ------------------------------------------------
		// --- Create the String for the console output ---
		String lineOutput = new String(byteArray);
		lineOutput = this.prefix2use + " " + lineOutput;
		final String lineOutputFinal = lineOutput;

		// --- send to Main-Container ---------------------
		if (sos!=null) {
			synchronized (sos) {
				this.sos.append2Stack(lineOutputFinal);
			}
		}

		// ------------------------------------------------
		// --- Do the normal task -------------------------
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
