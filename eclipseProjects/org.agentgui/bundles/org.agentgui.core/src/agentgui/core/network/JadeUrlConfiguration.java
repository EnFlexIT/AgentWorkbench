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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * This class can be used in order to evaluate and compare configured JADE URL's and its ports. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeUrlConfiguration {

	private String currURLorIP;
	private InetAddress currInetAddress;
	private int currPort = -1;
	private int currPort4MTP = -1;
	private String currMtpProtocol;

	private boolean errors=false;
	private String errorMsg;
	
	private List<InetAddress> inetAddresses;
	private List<InetAddress> ip4InetAddresses;
	private List<InetAddress> ip6InetAddresses;
	
	
	/**
	 * Instantiates a new JadeUrlConfiguration.
	 * 
	 * @param urlOrIPtoCheck the URL or IP on which the JADE platform is running or acting (e.g "localhost:1099/JADE")
	 */
	public JadeUrlConfiguration(String urlOrIPtoCheck) {
				
		try {
			if (urlOrIPtoCheck == null || urlOrIPtoCheck.trim().equals("")) {
				this.errors = true;
				this.errorMsg="JADE URL empty.";
				return;
//				this.currURLorIP = InetAddress.getLocalHost().getCanonicalHostName();
//				this.currInetAddress = InetAddress.getLocalHost();
			} else {
				this.currURLorIP = this.filterPort(urlOrIPtoCheck);
				this.currInetAddress = InetAddress.getByName(this.currURLorIP);
			}
			this.errors = false;
					
		} catch (UnknownHostException err) {
//			err.printStackTrace();

			this.errors = true;
			this.errorMsg = "Error while trying to receive network address '" + this.currURLorIP + "' !\n";
			this.errorMsg+= "=> Please, check your settings (e.g. for the server.master) and\n";
			this.errorMsg+= "=> make sure that the local machine has a working network connection.\n";

			this.currURLorIP = null;
			this.currInetAddress = null;

			System.err.println(this.errorMsg);
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
			this.currPort = Integer.parseInt(workPortNew);
		}
		return workURL;
	}
	
	
	/**
	 * Check for errors.
	 * @return true, if errors occurred with the current URL or IP-address
	 */
	public boolean hasErrors() {
		return errors;
	}
	/**
	 * Returns the error message, if any.
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMsg;
	}
	
	/**
	 * Checks if the specified instance equal to the current one.
	 *
	 * @param jucToCompare the JadeUrlChecker to compare
	 * @return true, if the jade platform configuration is equal
	 */
	public boolean isEqualJadePlatform(JadeUrlConfiguration jucToCompare) {

		if (hasErrors()==true) return false;
		if (jucToCompare.getHostIP().equals(this.getHostIP())==true) {
			if (jucToCompare.getPort()==this.getPort()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true, if the current JADE instance is located on a local machine.
	 * @return True, if the JADE URL is pointing to the local machine 
	 */
	public boolean isLocalhost() {
		if(hasErrors()){
			return false;
		}
		for (InetAddress inetAddress : this.getLocalInetAddresses()) {
			if (inetAddress.equals(this.currInetAddress)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Provides the JADE URL out of the analysed subcomponents like IP, Port and "/JADE"-Suffix.
	 *
	 * @return JADE URL
	 */
	public String getJadeURL(){
		if (currInetAddress!=null && currPort!=-1) {
			return this.currURLorIP + ":" + currPort + "/JADE";	
		} else {
			return null;
		}
	}
	
	/**
	 * return the URL for the MTP of JADE.
	 * @return JADE URL for MTP
	 */
	public String getJadeURL4MTP() {
		if (currInetAddress!=null && currPort4MTP!=-1) {
			if (currMtpProtocol.equals("HTTP")){
				return "http://" + this.currURLorIP + ":" + currPort4MTP + "/acc";	
			}else{
				return "https://" + this.currURLorIP + ":" + currPort4MTP + "/acc";	
			}
		} else {
			return null;
		}
	}
	/**
	 * 
	 * Provides the IP-Address of the current JADE-URL.
	 * @return IP address (e.g. 127.0.0.1) 
	 */
	public String getHostIP() {
		if (currInetAddress!=null) {
			return currInetAddress.getHostAddress();
		}
		return null;
	}
	/**
	 * Provides the host name of the current JADE-URL.
	 * @return host name (e.g. 'localhost')
	 */
	public String getHostName() {
		if (currInetAddress!=null) {
			return currInetAddress.getHostName();	
		}		
		return null;
	}
	
	/**
	 * Provides the port number of JADE.
	 * @return The port number
	 */
	public int getPort() {
		return currPort; 
	}
	
	/**
	 * Can be used to set the port number for JADE.
	 * @param newPort The port to be used
	 */
	public void setPort(int newPort) {
		currPort = newPort; 
	}

	/**
	 * Provides the port number of JADE, that is used for the MTP of the MainContainer.
	 * @return the port4 mtp
	 */
	public int getPort4MTP() {
		return currPort4MTP;
	}
	
	/**
	 * Can be used to set the port number for the MTP of the Main-Container.
	 * @param newPort4MTP the new port4 mtp
	 */
	public void setPort4MTP(int newPort4MTP) {
		this.currPort4MTP = newPort4MTP;
	}
	
	/**
	 * Gets the MtpProtocol.
	 * @return the currMtpProtocol
	 */
	public String getMtpProtocol(){
		return currMtpProtocol;
	}
	
	/**
	 * Sets the MtpProtocol.
	 * @param mtpProtocol the new currMtpProtocol
	 */
	public void setMtpProtocol(String mtpProtocol){
		this.currMtpProtocol = mtpProtocol;
	}
	
	
	/**
	 * Returns all local InetAddress's.
	 * @return the local InetAddress's
	 */
	private List<InetAddress> getLocalInetAddresses() {
		if (inetAddresses==null) {
			inetAddresses = new ArrayList<InetAddress>();
			inetAddresses.addAll(this.getLocalIP4InetAddresses());
			inetAddresses.addAll(this.getLocalIP6InetAddresses());
		}
		return inetAddresses;
	}
	/**
	 * Returns all local IP4 InetAddress's.
	 * @return the local IP4 InetAddress's
	 */
	private List<InetAddress> getLocalIP4InetAddresses() {
		if (ip4InetAddresses==null) {
			ip4InetAddresses = new ArrayList<InetAddress>();
			this.setLocalInetAddresses();
		}
		return ip4InetAddresses;
	}
	/**
	 * Returns all local IP6 InetAddress's.
	 * @return the local IP6 InetAddress's
	 */
	private List<InetAddress> getLocalIP6InetAddresses() {
		if (ip6InetAddresses==null) {
			ip6InetAddresses = new ArrayList<InetAddress>();
			this.setLocalInetAddresses();
		}
		return ip6InetAddresses;
	}
	/**
	 * Sets the local InetAddress's.
	 */
	private void setLocalInetAddresses() {
		
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {

				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
				if (inetAddresses.hasMoreElements()) {
					// System.out.printf("Display name: %s\n",
					// netint.getDisplayName());
					// System.out.printf("Name: %s\n", netint.getName());
					for (InetAddress inetAddress : Collections.list(inetAddresses)) {
						if (inetAddress instanceof Inet4Address) {
							if (this.getLocalIP4InetAddresses().contains(inetAddress)==false) {
								this.getLocalIP4InetAddresses().add(inetAddress);	
							}
							
						} else if (inetAddress instanceof Inet6Address) {
							if (this.getLocalIP6InetAddresses().contains(inetAddress)==false) {
								this.getLocalIP6InetAddresses().add(inetAddress);	
							}
							
						}
					}
				}
			}

		} catch (SocketException e) {
			System.err.println("Error retrieving local network addresses list ... ");
		}
	}
	
	
}

