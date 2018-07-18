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

import java.awt.Font;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Language;
import agentgui.core.project.PlatformJadeConfig;


/**
 * The Class NetworkInterfaces will evaluate all available IP addresses 
 * for the current local machine. Additionally, it provides several methods
 * that allow selecting, sorting and displaying these information. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkAddresses {

	public enum InetAddressType {
		Inet4Address,
		Inet6Address
	}

	private boolean debug = false;
	private Vector<NetworkAddress> networkAddressVector;
	private Vector<NetworkAddress> networkAddressVectorLoopBack;
	private JPopupMenu jPopupMenuNetworkAddresses;
	
	
	/**
	 * Instantiates a new local network interfaces.
	 */
	public NetworkAddresses() {
		this.getNetworkAddressVector();
	}
	/**
	 * Gets the network address vector.
	 * @return the network address vector
	 */
	public Vector<NetworkAddress> getNetworkAddressVector() {
		if (networkAddressVector==null) {
			networkAddressVector = new Vector<NetworkAddresses.NetworkAddress>();
			this.fillNetworkAddressVector();
			this.sortNetworkAddressVector();
		}
		return networkAddressVector;
	}
	/**
	 * Gets the network address vector.
	 * @return the network address vector
	 */
	public Vector<NetworkAddress> getNetworkAddressVectorLoopBack() {
		if (networkAddressVectorLoopBack==null) {
			networkAddressVectorLoopBack = new Vector<NetworkAddresses.NetworkAddress>();
		}
		return networkAddressVectorLoopBack;
	}

	/**
	 * Starts the evaluation of the IP addresses locally available.
	 */
	private void fillNetworkAddressVector() {

	    try {
	    	Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {

	        	NetworkInterface iface = interfaces.nextElement();
	            // --- filter inactive interfaces -------------------
	            if (!iface.isUp()) continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                if (iface.isLoopback()==true) {
	                	this.getNetworkAddressVectorLoopBack().addElement(new NetworkAddress(iface, addr));
	                } else {
	                	this.getNetworkAddressVector().addElement(new NetworkAddress(iface, addr));
	                }
	            }
	        }
	        
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
	}
	/**
	 * Sorts the local network address vector.
	 */
	private void sortNetworkAddressVector() {
		
		// --- Sort the Vector ------------------------
		Collections.sort(this.getNetworkAddressVector(), new Comparator<NetworkAddress>() {
			@Override
			public int compare(NetworkAddress nwa1, NetworkAddress nwa2) {
				if (nwa1.getInetAddressType()!=nwa2.getInetAddressType()) {
					if (nwa1.getInetAddressType()==InetAddressType.Inet4Address) {
						return -1;
					} else {
						return 1;
					}
					
				} else {
					if (nwa1.getNetworkInterface()!=nwa2.getNetworkInterface()) {
						return nwa1.getNetworkInterface().getDisplayName().compareTo(nwa2.getNetworkInterface().getDisplayName());
					} else {
						return nwa1.getInetAddress().getHostAddress().compareTo(nwa2.getInetAddress().getHostAddress());
					}
				}
			}
		});
	}
	
	/**
	 * Sorts the specified network address vector by selection priority.
	 */
	private void sortNetworkAddressVectorBySelectionPriority(Vector<NetworkAddress> addresses) {
		Collections.sort(addresses, new Comparator<NetworkAddress>() {
			@Override
			public int compare(NetworkAddress nwa1, NetworkAddress nwa2) {
				return nwa1.getSelectionPriority().compareTo(nwa2.getSelectionPriority());
			}
		});
	}
	
	/**
	 * Checks if the specified IP address is locally available.
	 *
	 * @param ipAddress the IP address to check
	 * @return true, if is available IP
	 */
	public boolean isAvailableIP(String ipAddress) {
		
		if (ipAddress==null || ipAddress.isEmpty()) return false;
		
		// --- Get all available network addresses --------  
		Vector<NetworkAddress> netAddressVector = new Vector<>(this.getNetworkAddressVector());
		netAddressVector.addAll(this.getInet4AddressesLoopBack());

		// --- Check all available network addresses ------  
		for (int i = 0; i < netAddressVector.size(); i++) {
			NetworkAddress netAddess = netAddressVector.get(i);
			if (netAddess.getInetAddress().getHostAddress().equals(ipAddress)) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Gets the JPopupMenu for the available network addresses.
	 *
	 * @param actListener the act listener
	 * @return the JPopupMenu for {@link NetworkAddresses}
	 * @see NetworkAddresses
	 */
	public JPopupMenu getJPopupMenu4NetworkAddresses(ActionListener actListener) {
		
		jPopupMenuNetworkAddresses = new JPopupMenu("Network Addresses");
		jPopupMenuNetworkAddresses.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		jPopupMenuNetworkAddresses.add(this.getJLabelHeader("Default:"));
		jPopupMenuNetworkAddresses.add(this.getJMenueItemDefault(PlatformJadeConfig.MTP_IP_AUTO_Config, actListener));
		
		jPopupMenuNetworkAddresses.addSeparator();
		jPopupMenuNetworkAddresses.add(this.getJLabelHeader("IP4 - " + Language.translate("Adressen") + ":"));
		for (NetworkAddress netAddress : this.getInet4Addresses()) {
			jPopupMenuNetworkAddresses.add(this.getJMenueItemNetworkAddress(netAddress, actListener));
		}
		
		jPopupMenuNetworkAddresses.addSeparator();
		jPopupMenuNetworkAddresses.add(this.getJLabelHeader("IP6 - " + Language.translate("Adressen") + ":"));
		for (NetworkAddress netAddress : this.getInet6Addresses()) {
			jPopupMenuNetworkAddresses.add(this.getJMenueItemNetworkAddress(netAddress, actListener));	
		}
		return jPopupMenuNetworkAddresses;
	}
	
	/**
	 * Gets the JLabel for a header.
	 * @param headerText the header text
	 * @return the JLabel header
	 */
	private JLabel getJLabelHeader(String headerText) {
		JLabel jLabelHeader = new JLabel("  " + headerText);
		jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		return jLabelHeader;
	}
	/**
	 * Gets the JMenuItem -for the specified {@link NetworkAddress}.
	 *
	 * @param networkAddress the network address
	 * @param actListener the {@link ActionListener}
	 * @return the JMenueItem network address
	 */
	private JMenuItem getJMenueItemDefault(String header, ActionListener actListener) {
		JMenuItem jMenuItem = new JMenuItem(header);
		jMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		jMenuItem.setActionCommand(header);
		jMenuItem.addActionListener(actListener);
		return jMenuItem;
	}
	/**
	 * Gets the JMenuItem for the specified {@link NetworkAddress}.
	 *
	 * @param networkAddress the network address
	 * @param actListener the act listener
	 * @return the JMenueItem network address
	 */
	private JMenuItem getJMenueItemNetworkAddress(NetworkAddress networkAddress, ActionListener actListener) {
		JMenuItem jMenuItem = new JMenuItem(networkAddress.toString());
		jMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		jMenuItem.addActionListener(actListener);
		jMenuItem.setActionCommand(networkAddress.getInetAddress().getHostAddress());
		return jMenuItem;
	}
	
	
	/**
	 * Gets the network address vector.
	 *
	 * @param inetAddressType the filter for the {@link InetAddressType}e
	 * @param getLoopBackAddressesOnly the parameter to get loop back addresses only
	 * @return the network address vector
	 */
	private Vector<NetworkAddress> getNetworkAddressVector(InetAddressType inetAddressType, boolean getLoopBackAddressesOnly) {
		
		Vector<NetworkAddress> searchVector = null;
		if (getLoopBackAddressesOnly==false) {
			searchVector = this.getNetworkAddressVector();
		} else {
			searchVector = this.getNetworkAddressVectorLoopBack();
		}
		// --- Search the vector ------------------------------------
		Vector<NetworkAddress> resultVector = new Vector<NetworkAddresses.NetworkAddress>();
		for (NetworkAddress nwa : searchVector) {
			if (nwa.getInetAddressType()==inetAddressType) {
				resultVector.add(nwa);
			}
		}
		this.sortNetworkAddressVectorBySelectionPriority(resultVector);
		return resultVector; 
		
	}
	/**
	 * Returns the {@link Inet4Address} addresses.
	 * @return the inet4 addresses
	 */
	public Vector<NetworkAddress> getInet4Addresses() {
		return this.getNetworkAddressVector(InetAddressType.Inet4Address, false); 
	}
	/**
	 * Returns the loopback {@link Inet4Address} addresses.
	 * @return the inet4 addresses
	 */
	public Vector<NetworkAddress> getInet4AddressesLoopBack() {
		return this.getNetworkAddressVector(InetAddressType.Inet4Address, true); 
	}
	/**
	 * Returns the {@link Inet6Address} addresses.
	 * @return the inet6 addresses
	 */
	public Vector<NetworkAddress> getInet6Addresses() {
		return this.getNetworkAddressVector(InetAddressType.Inet6Address, false);
	}
	/**
	 * Returns the loopback {@link Inet6Address} addresses.
	 * @return the inet6 addresses
	 */
	public Vector<NetworkAddress> getInet6AddressesLoopBack() {
		return this.getNetworkAddressVector(InetAddressType.Inet6Address, true);
	}

	
	
	/**
	 * Returns the 'preferred' {@link InetAddress}. That means that out of the currently
	 * available IP-addresses, the IP-address will be selected and returned that can be used  
	 * for network communication (e.g. for the MTP of JADE). 
	 * Here the selection algorithm first tries to select out of the IP4 addresses and tries 
	 * to identify the network card connection. If not available, a WiFi connection will be used. 
	 * If no IP4 address is available, the same will be tried with the IP6 network connections.
	 *
	 * @return the preferred InetAddress
	 */
	public InetAddress getPreferredInetAddress() {

		Vector<NetworkAddress> inetVector = this.getInet4Addresses();
		this.printNetworkAddressVector(inetVector);
		if (inetVector.size()==0) {
			inetVector = this.getInet6Addresses();
			if (inetVector.size()==0) return null;
		}

		InetAddress inetAddressFound = null;
		if (inetVector.size()==1) { 
			// --- Just one address was found -------------
			inetAddressFound = inetVector.get(0).getInetAddress();
			
		} else {
			// --- Several addresses were found -----------
			this.sortNetworkAddressVectorBySelectionPriority(inetVector);
			inetAddressFound = inetVector.get(0).getInetAddress();
		}
		return inetAddressFound;
	}
	
	
	
	
	
	/**
	 * Prints the elements of the specified networkAddressVector, if the 
	 * local variable debug is set to true.
	 * @param networkAddressVector the network address vector
	 */
	private void printNetworkAddressVector(Vector<NetworkAddress> networkAddressVector) {
		if (debug) {
			for (NetworkAddress nwa : networkAddressVector) {
				System.out.println(nwa);
			}
		}
	}
	
	
	
	/**
	 * The sub class NetworkAddress describes the list elements that are used within
	 * the super class and their listing elements as listed below.
	 * 
	 * @see NetworkAddresses#getNetworkAddressVector()
	 * @see NetworkAddresses#getJPopupMenu4NetworkAddresses(ActionListener)
	 * @see NetworkAddresses#getInet4Addresses()
	 * @see NetworkAddresses#getInet6Addresses()
	 *
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class NetworkAddress {
		
		private NetworkInterface networkInterface;
		private InetAddress inetAddress;
		private InetAddressType inetAddressType;
		private Integer selectionPriority = 0;
	
		
		/**
		 * Instantiates a new network address.
		 *
		 * @param nInterface the n interface
		 * @param inetAddress the inet address
		 */
		public NetworkAddress(NetworkInterface nInterface, InetAddress inetAddress) {
			this.setNetworkInterface(nInterface);
			this.setInetAddress(inetAddress);
			this.setSelectionPriority();
		}
		
		/**
		 * Gets the NetworkInterface.
		 * @return the network interface
		 */
		public NetworkInterface getNetworkInterface() {
			return networkInterface;
		}
		/**
		 * Sets the NetworkInterface.
		 * @param networkInterface the new network interface
		 */
		public void setNetworkInterface(NetworkInterface networkInterface) {
			this.networkInterface = networkInterface;
		}
		
		/**
		 * Gets the {@link InetAddress}.
		 * @return the InetAddress
		 */
		public InetAddress getInetAddress() {
			return inetAddress;
		}
		/**
		 * Sets the {@link InetAddress}.
		 * @param inetAddress the new inet address
		 */
		public void setInetAddress(InetAddress inetAddress) {
			this.inetAddress = inetAddress;
			if (inetAddress instanceof Inet4Address) {
				this.setInetAddressType(InetAddressType.Inet4Address);
			} else if (inetAddress instanceof Inet6Address) {
				this.setInetAddressType(InetAddressType.Inet6Address);
			} else {
				if (debug) {
					System.out.println("Found: " + this.inetAddress.getClass().getName() );
				}
			}
		}
		
		/**
		 * Gets the {@link InetAddressType}.
		 * @return the {@link InetAddressType}
		 */
		public InetAddressType getInetAddressType() {
			return inetAddressType;
		}
		/**
		 * Sets the {@link InetAddressType}.
		 * @param inetAddressType the new {@link InetAddressType}
		 */
		public void setInetAddressType(InetAddressType inetAddressType) {
			this.inetAddressType = inetAddressType;
		}
		
		/**
		 * Sets the selection priority according to the local 
		 * {@link NetworkInterface} (name and display name).
		 */
		public void setSelectionPriority() {
			
			 String interfaceName = this.networkInterface.getName();
			 if (interfaceName!=null) {
				 interfaceName = interfaceName.toLowerCase();
				 if (interfaceName.contains("eth")) this.selectionPriority--;
				 if (interfaceName.contains("wlan")) this.selectionPriority++;	 
			 }
			 
			 String displayName = this.networkInterface.getDisplayName();
			 if (displayName!=null) {
				 displayName = displayName.toLowerCase();
				 if (displayName.contains("wifi")) this.selectionPriority++;
				 if (displayName.contains("wi-fi")) this.selectionPriority++;
				 if (displayName.contains("wireless")) this.selectionPriority++;	 
			 }
		}
		/**
		 * Sets the selection priority.
		 * @param selectionPriority the new selection priority
		 */
		public void setSelectionPriority(Integer selectionPriority) {
			this.selectionPriority = selectionPriority;
		}
		/**
		 * Gets the selection priority.
		 * @return the selection priority
		 */
		public Integer getSelectionPriority() {
			return selectionPriority;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			
			String description = null; 
			switch (this.getInetAddressType()) {
			case Inet4Address:
				description = "[IP4] " + this.inetAddress.getHostAddress() + " (" + this.networkInterface.getName() + " - " + this.networkInterface.getDisplayName() + ")";
				break;

			case Inet6Address:
				description = "[IP6] " + this.inetAddress.getHostAddress() + " (" + this.networkInterface.getName() + " - " + this.networkInterface.getDisplayName() + ")";
				break;
			}
			return description;
		}
	}

}
