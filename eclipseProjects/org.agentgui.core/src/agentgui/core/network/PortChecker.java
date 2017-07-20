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
package agentgui.core.network;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class can be used in order to evaluate the next free or unused port
 * on the local machine, starting from a specified starting port. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PortChecker {

	private int freePort;

	
	/**
	 * Instantiates a new port checker.
	 * @param portSearchStart the port search start
	 */
	public PortChecker(int portSearchStart){
		this.checkPort(portSearchStart, null);
	}
	/**
	 * Instantiates a new port checker.
	 * @param portSearchStart the port search start
	 * @param checkURLorIP the URL or the IP to check
	 */
	public PortChecker(int portSearchStart, String checkURLorIP){
		this.checkPort(portSearchStart, checkURLorIP);
	}
	
	/**
	 * Checks for the free port starting from specified one.
	 * @param portSearchStart the port search start
	 */
	private void checkPort(int portSearchStart, String checkURLorIP) {
		
		int currPort = portSearchStart;
		while (currPort < portSearchStart+30) {
			if (checkURLorIP==null) {
				if (this.isFreePort(currPort)==true){
					this.freePort = currPort;
					break;
				}
			} else {
				if (this.isFreePort(currPort)==true && this.isFreePortHTTP(currPort, checkURLorIP)==true) {
					this.freePort = currPort;
					break;
				}
			}
			currPort++;
		}
	}
	
	/**
	 * Provides the next free port number.
	 * @return next free port number
	 */
	public int getFreePort() {
		return freePort;
	}
	
	/**
	 * Checks if a port is available (free).
	 *
	 * @param port the port
	 * @return True, if the port is unused or free
	 */
	public boolean isFreePort(int port) {
		
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
			
		} catch (IOException e) {
		} finally {
			if (ds!=null) {
				ds.close();
			}
			if (ss!=null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Checks if a free http was specified.
	 *
	 * @param port the port
	 * @param checkURLorIP the HTTP or HTTPS address
	 * @return true, if is free port http
	 */
	public boolean isFreePortHTTP(int port, String checkURLorIP) {

		boolean isFreeHTTPport = false;
		try {
			Socket s = new Socket(checkURLorIP, port);
			s.close();
			isFreeHTTPport = false;
		    
		} catch (IOException ioex) {
//			ioex.printStackTrace();
			isFreeHTTPport = true;
		}
		return isFreeHTTPport;
	}
	
}
