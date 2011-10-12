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

import java.net.InetAddress;
import java.net.UnknownHostException;

import agentgui.core.application.Application;

/**
 * This class can be used in order to evaluate the currently configured JADE URL and its ports. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeUrlChecker {

	private InetAddress currAddress = null;
	private InetAddress addressLocal = null;
	private InetAddress addressLocalAlt = null;
	
	private String currURL = null;
	private Integer currPort = 0;
	private Integer currPort4MTP = 0;
	
	/**
	 * Constructor of this class.
	 *
	 * @param url The URL on which JADE is running or acting (e.g "localhost:1099/JADE")
	 */
	public JadeUrlChecker(String url) {
		
		currURL = this.filterPort(url);		
		try {
			currAddress = InetAddress.getByName(currURL);
			addressLocal = InetAddress.getLocalHost();
			addressLocalAlt = InetAddress.getByName("127.0.0.1");
			if (currAddress.equals(addressLocalAlt)) {
				currAddress = addressLocal;
			}
		} catch (UnknownHostException err) {
			//err.printStackTrace();
			System.err.println( "[" + Application.RunInfo.getApplicationTitle() + "] Error while try to resolve the address '" + err.getLocalizedMessage() + "'. Please check your Agent.GUI - start options." );
			currURL = null;
		}
		
	}
	
	/**
	 * Filters the Port on which JADE is running or acting.
	 * @param url The URL on which JADE is running or acting (e.g "localhost:1099/JADE")
	 * @return The URL or IP, which is used by JADE (e.g "localhost")
	 */
	private String filterPort(String url){
		
		String workURL = url;
		String workPort = null;
		String workPortNew = "";
		
		if (workURL==null) return null;
		
		// --- Remove Port-Information, if URL contains one ---------
		if ( url.contains(":")) {
			
			workURL = url.substring(0, url.indexOf(":"));
			workPort = url.substring(url.indexOf(":")+1).trim();
			String workPortArr[] = workPort.split(""); 
			
			for (int i = 0; i < workPortArr.length; i++) {
				if ( workPortArr[i].equalsIgnoreCase("")==false ) {
					String sngChar = workPortArr[i];
					if ( sngChar.matches( "[0-9]" )==true ) {
						workPortNew += sngChar;
					} else {
						break;
					}
				}
			}
			currPort = Integer.parseInt(workPortNew);
		}
		return workURL;
	}
	
	/**
	 * Returns true, if the current JADE instance is located on a local machine.
	 * @return True, if the JADE URL is pointing to the local machine 
	 */
	public boolean isLocalhost() {
		if (currAddress.equals(addressLocal)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Provides the JADE URL out of the analysed subcomponents like IP, Port and "/JADE"-Suffix.
	 *
	 * @return JADE URL
	 */
	public String getJADEurl(){
		if ( currAddress!=null && currPort.equals(-1)==false) {
			return currAddress.getHostAddress() + ":" + currPort + "/JADE";	
		} else {
			return null;
		}
	}
	
	/**
	 * return the URL for the MTP of JADE.
	 *
	 * @return JADE URL for MTP
	 */
	public String getJADEurl4MTP() {
		if ( currAddress!=null && currPort4MTP.equals(-1)==false) {
			return "http://" + currAddress.getHostAddress() + ":" + currPort4MTP + "/acc";	
		} else {
			return null;
		}
	}
	/**
	 * Provides the IP-Address of the current JADE-URL.
	 * @return IP address (e.g. 127.0.0.1) 
	 */
	public String getHostIP() {
		if (currAddress==null) {
			return null;
		} else {
			return currAddress.getHostAddress();
		}
	}
	/**
	 * Provides the host name of the current JADE-URL.
	 * @return host name (e.g. 'localhost')
	 */
	public String getHostName() {
		if (currAddress==null) {
			return null;
		} else {
			return currAddress.getHostName();	
		}		
	}
	
	/**
	 * Provides the port number of JADE.
	 *
	 * @return The port number
	 */
	public Integer getPort() {
		return currPort; 
	}
	
	/**
	 * Can be used to set the port number for JADE.
	 *
	 * @param newPort The port to be used
	 */
	public void setPort(Integer newPort) {
		currPort = newPort; 
	}

	/**
	 * Provides the port number of JADE, that is used for the MTP of the MainContainer.
	 *
	 * @return the port4 mtp
	 */
	public Integer getPort4MTP() {
		return currPort4MTP;
	}
	
	/**
	 * Can be used to set the port number for the MTP of the Main-Container.
	 *
	 * @param newPort4MTP the new port4 mtp
	 */
	public void setPort4MTP(Integer newPort4MTP) {
		this.currPort4MTP = newPort4MTP;
	}
	
}
