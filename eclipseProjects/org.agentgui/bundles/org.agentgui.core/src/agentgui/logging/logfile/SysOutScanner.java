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

import java.io.PrintStream;
import java.util.Vector;

import agentgui.logging.logfile.PrintStreamListener.PrintStreamListenerType;

/**
 * This Class can be used in order to listen to the output that will be generated 
 * through the console by using System.out and System.err - commands.<br>
 * <br>
 * The received output will be stored in the local Vector<String> outputStack 
 * that can be accessed by the synchronised method {@link #getStack()}.   
 * 
 * @see PrintStreamListener
 * @see System#out
 * @see System#err
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SysOutScanner {

	private Vector<PrintStreamListenerOutput> outputStack = new Vector<PrintStreamListenerOutput>(); 
	private LogFileWriter logFileWriter;

	/**
	 * Constructor of this class, if running local in an application.
	 * @param localConsole the LogFileWriter
	 */
	public SysOutScanner(LogFileWriter localConsole) {
		this.logFileWriter = localConsole;
		this.setScanner();
	}
	
	/**
	 * This method will set PrintStreamListener to the System.out and the System.err PrintStream's
	 */
	private void setScanner() {

		PrintStream orgStreamOut = System.out;
		PrintStream orgStreamErr = System.err;
		try {
			// --- Build new PrintStream's ------ 
			PrintStream listenStreamOut = new PrintStreamListener(orgStreamOut, this, PrintStreamListenerType.SystemOutput);
			PrintStream listenStreamErr = new PrintStreamListener(orgStreamErr, this, PrintStreamListenerType.SystemError);

			System.setOut(listenStreamOut);
			System.setErr(listenStreamErr);
			
		} catch (Exception ex) {
			// --- Restoring back to console ----
			System.setOut(orgStreamOut);
			System.setErr(orgStreamErr);
			// --- Notify about it --------------
			System.out.println("Unsuccessful tried to redirect output & exceptions to PrintStreamListener for distribute debugging.");
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * This method will be used in order to append an output line (System.out or System.err) 
	 * to the local outputStack
	 * @param listenerOutput the PrintStreamListenerOutput 
	 */
	public void append2Stack(PrintStreamListenerOutput listenerOutput) {
		if (this.outputStack.size()>=20) {
			this.outputStack.remove(0);
		}
		this.outputStack.add(listenerOutput);
		
		// --- If a local AwbConsole window is used --------------------------------------------------
		if (this.logFileWriter!=null) {
			this.logFileWriter.appendText(this.getStack());		
		}
	}
	/**
	 * Can be used in order to get the current outputStack of the local console.
	 * @return the stack
	 */
	public synchronized Vector<PrintStreamListenerOutput> getStack() {
		Vector<PrintStreamListenerOutput> stack = this.outputStack;
		this.outputStack = new Vector<>();
		return stack;
	}
	
	
	
}
