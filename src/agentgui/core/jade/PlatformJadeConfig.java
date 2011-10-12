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
package agentgui.core.jade;

import jade.core.Profile;
import jade.core.ProfileImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.network.PortChecker;

/**
 * With this class, the Profile of a new JADE-Container can be configured.
 * To use this class, just create a new instance of it and go throw 
 * configurations like in the example below.<br>
 * After configuration you can use the method 'getNewInstanceOfProfilImpl()'
 * which returns a new Instance of 'jade.core.Profile'. This can be used to 
 * create a new JADE-Container.<br>
 * <br>
 * EXAMPLE:<br><br
 * <blockquote><code>
 *  PlatformJadeConfig pjc = new PlatformJadeConfig();<br>
 *	pjc.setLocalPort(1099);<br>
 *	pjc.addService(PlatformJadeConfig.SERVICE_AgentGUI_LoadService);<br>
 *  pjc.addService(PlatformJadeConfig.SERVICE_AgentGUI_SimulationService);<br>
 *  pjc.addService(PlatformJadeConfig.SERVICE_NotificationService);<br>
 *  <br>
 *	Profile profile = pjc.getNewInstanceOfProfilImpl();<br>
 * </code></blockquote>
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class PlatformJadeConfig implements Serializable {
	
	private static final long serialVersionUID = -9062155032902746361L;
	
	// --- Services 'Activated automatically' ---------------------------------
	public static final String SERVICE_MessagingService = jade.core.messaging.MessagingService.class.getName();
	public static final String SERVICE_AgentManagementService = jade.core.management.AgentManagementService.class.getName();
	
	// --- Services 'Active by default' ---------------------------------------
	public static final String SERVICE_AgentMobilityService = jade.core.mobility.AgentMobilityService.class.getName();
	public static final String SERVICE_NotificationService = jade.core.event.NotificationService.class.getName(); 
	
	// --- Services 'Inactive by default' -------------------------------------
	public static final String SERVICE_MainReplicationService = jade.core.replication.MainReplicationService.class.getName();
	public static final String SERVICE_FaultRecoveryService = jade.core.faultRecovery.FaultRecoveryService.class.getName();
	public static final String SERVICE_AddressNotificationService = jade.core.replication.AddressNotificationService.class.getName();
	public static final String SERVICE_TopicManagementService = jade.core.messaging.TopicManagementService.class.getName();
	public static final String SERVICE_PersistentDeliveryService = jade.core.messaging.PersistentDeliveryService.class.getName();
	public static final String SERVICE_UDPNodeMonitoringServ = jade.core.nodeMonitoring.UDPNodeMonitoringService.class.getName();
	public static final String SERVICE_BEManagementService = jade.imtp.leap.nio.BEManagementService.class.getName();
	
	// --- Agent.GUI-Services -------------------------------------------------
	public static final String SERVICE_DebugService = jade.debugging.DebugService.class.getName();
	public static final String SERVICE_AgentGUI_LoadService = agentgui.simulationService.LoadService.class.getName();
	public static final String SERVICE_AgentGUI_SimulationService = agentgui.simulationService.SimulationService.class.getName();
	public static final String SERVICE_AgentGUI_P2DEnvironmentProviderService = agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService.class.getName();
	
	// --- Add-On-Services ----------------------------------------------------
	public static final String SERVICE_InterPlatformMobilityService = jade.core.migration.InterPlatformMobilityService.class.getName();
	
	/**
	 * Array of services, which will be started with JADE in every case
	 */
	private static final String[] autoServices = {SERVICE_MessagingService, SERVICE_AgentManagementService};
	private static final String AUTOSERVICE_TextAddition = "Startet automatisch !";
	 
	
	// --- Runtime variables -------------------------------------------------- 
	@XmlTransient
	private Project currProject = null;
	@XmlElement(name="useLocalPort")	
	private Integer useLocalPort = Application.RunInfo.getJadeLocalPort();
	@XmlElementWrapper(name = "serviceList")
	@XmlElement(name="service")			
	private HashSet<String> useServiceList = new HashSet<String>();
	@XmlTransient
	private DefaultListModel listModelServices = null;
	
	
	/**
	 * Constructor of this class.
	 */
	public PlatformJadeConfig() {
	
	}
	
	/**
	 * This method returns the TextAddition if a Service is an automatically starting service of JADE.
	 *
	 * @return the auto service text addition
	 */
	public static String getAutoServiceTextAddition() {
		return " " + Language.translate(AUTOSERVICE_TextAddition) + " ";
	}
	
	/**
	 * Returns if a service generally starts while JADE is starting.
	 *
	 * @param serviceReference the service reference
	 * @return true, if is auto service
	 */
	public static boolean isAutoService(String serviceReference) {
		for (int i = 0; i < autoServices.length; i++) {
			if (autoServices[i].equalsIgnoreCase(serviceReference)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the project.
	 *
	 * @param project the new project
	 */
	public void setProject(Project project) {
		this.currProject = project;
	}

	/**
	 * This Method returns a new Instance of Profil, which
	 * can be used for starting a new JADE-Container.
	 *
	 * @return jade.core.Profile
	 */
	public Profile getNewInstanceOfProfilImpl(){
		Profile prof = new ProfileImpl();
		prof = this.setProfileLocalPort(prof);
		prof = this.setProfileServices(prof);		
		return prof;
	}
	
	/**
	 * This Method scans for a free Port, which can be used
	 * for the JADE-Container. It's starts searching for a free
	 * Port on 'portSearchStart'. If not available, it checks
	 * the next higher Port and so on.
	 *
	 * @param portSearchStart the port search start
	 */
	private void findFreePort(int portSearchStart){
		PortChecker portCheck = new PortChecker(portSearchStart);
		useLocalPort = portCheck.getFreePort();
	}
	
	/**
	 * Adds the local configured 'LocalPort' to the input instance of Profile.
	 *
	 * @param profile the profile
	 * @return jade.core.Profile
	 */
	private Profile setProfileLocalPort(Profile profile){
		this.findFreePort(useLocalPort);
		profile.setParameter(Profile.LOCAL_PORT, useLocalPort.toString());
		return profile;
	}
	
	/**
	 * Adds the local configured services to the input instance of Profile.
	 *
	 * @param profile the profile
	 * @return jade.core.Profile
	 */
	private Profile setProfileServices(Profile profile){
		String serviceListString = this.getServiceListArgument();
		if (serviceListString.equalsIgnoreCase("")==false || serviceListString!=null) {
			profile.setParameter(Profile.SERVICES, serviceListString);
		}
		return profile;
	}	
	
	/**
	 * This method walks through the HashSet of configured Services and returns them
	 * as a String separated with a semicolon (';').
	 *
	 * @return String
	 */
	public String getServiceListArgument() {
		String serviceListString = "";
		Iterator<String> it = useServiceList.iterator();
		while (it.hasNext()) {
			String singeleService = it.next();
			if (singeleService.endsWith(";")==true) {
				serviceListString += singeleService;
			} else {
				serviceListString += singeleService + ";";	
			}
		}
		return serviceListString;
	}
	
	/**
	 * Can be used in order to add a class reference to an extended JADE-BaseService.
	 *
	 * @param serviceClassReference the service class reference
	 */
	public void addService(String serviceClassReference) {
		
		if (this.isUsingService(serviceClassReference)==false && serviceClassReference.contains(getAutoServiceTextAddition())==false) {
			
			// --- add to the local HashSet -------------------------
			this.useServiceList.add(serviceClassReference);
			// --- add to the DefaultListModel ----------------------
			DefaultListModel delimo = this.getListModelServices();
			delimo.addElement(serviceClassReference);
			// --- sort the ListModel -------------------------------
			this.sortListModelServices();
			// --- if set, set project changed and unsaved ----------
			if (currProject!=null) {
				this.currProject.setChangedAndNotify(Project.CHANGED_JadeConfiguration);
			}
		}
	}

	/**
	 * Can be used in order to remove a class reference to an extended JADE-BaseService.
	 *
	 * @param serviceClassReference the service class reference
	 */
	public void removeService(String serviceClassReference) {
		
		if (this.isUsingService(serviceClassReference)==true) {
			// --- remove from the local HashSet --------------------
			this.useServiceList.remove(serviceClassReference);
			// --- remove from the DefaultListModel -----------------
			DefaultListModel delimo = this.getListModelServices();
			delimo.removeElement(serviceClassReference);
			// --- if set, set project changed and unsaved ----------
			if (currProject!=null) {
				this.currProject.setChangedAndNotify(Project.CHANGED_JadeConfiguration);
			}
		}
	}
	
	/**
	 * This method will remove all Services from the current Profile.
	 */
	public void removeAllServices() {
		this.useServiceList.clear();
		this.listModelServices.removeAllElements();
		// --- if set, set project changed and unsaved ----------
		if (currProject!=null) {
			this.currProject.setChangedAndNotify(Project.CHANGED_JadeConfiguration);
		}
	}
	
	/**
	 * Checks if a Service is configured for this instance.
	 * The requested Service can be given with the actual class of the service
	 *
	 * @param requestedService the requested service
	 * @return boolean
	 */
	public boolean isUsingService(String requestedService) {
		if ( useServiceList.contains(requestedService) == true ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Counts the number of services which are currently configured.
	 *
	 * @return the integer
	 */
	public Integer countUsedServices() {
		return this.useServiceList.size();
	}
	
	/**
	 * With this class the LocalPort, which will be used from a JADE-Container can be set.
	 *
	 * @param port2Use the new local port
	 */
	public void setLocalPort(int port2Use){
		useLocalPort = port2Use;
		// --- if set, set project changed and unsaved ----------
		if (currProject!=null) {
			this.currProject.setChangedAndNotify(Project.CHANGED_JadeConfiguration);
		}
	}
	
	/**
	 * Returns the current Port which is  configured for a JADE-Container.
	 *
	 * @return Integer
	 */
	public Integer getLocalPort() {
		return useLocalPort;
	}
	
	/**
	 * Gets the list model services.
	 *
	 * @return the listModelServices
	 */
	@XmlTransient
	public DefaultListModel getListModelServices() {
		if (listModelServices==null) {
			listModelServices = new DefaultListModel();
			Iterator<String> it = useServiceList.iterator();
			while (it.hasNext()) {
				listModelServices.addElement(it.next());
			}
			this.sortListModelServices();
		}
		return listModelServices;
	}

	/**
	 * This method will sort the current list model for the chosen services.
	 */
	private void sortListModelServices() {
		
		if (useServiceList.size()>1) {
			Vector<String> sorty = new Vector<String>(useServiceList);
			Collections.sort(sorty);
			this.listModelServices.removeAllElements();
			for (int i = 0; i < sorty.size(); i++) {
				this.listModelServices.addElement(sorty.get(i));
			}
		}
	}
	
	/**
	 * This Method compares the current instance with another instances
	 * of this class and returns true, if they are logical identical.
	 *
	 * @param jadeConfig2 the jade config2
	 * @return boolean
	 */
	public boolean isEqual(PlatformJadeConfig jadeConfig2) {
		
		// --- Selbe Anzahl der ausgewählten Services ? -------------
		if ( this.countUsedServices() != jadeConfig2.countUsedServices() ) {
			return false;
		}
		// --- Sind die ausgewählten Services identisch? ------------ 
		Iterator<String> it = this.useServiceList.iterator();
		while( it.hasNext() ) {
			String currService = it.next();
			if ( jadeConfig2.isUsingService(currService) == false ) {
				return false;
			}
		}
		// --- Soll der selbe Jade LocalPort verwendet werden ? ----
		if ( jadeConfig2.getLocalPort().equals(this.getLocalPort()) ) {
			return true;
		} else {
			return false;
		}		
	}

	/**
	 * This Method returns a String which shows the current
	 * configuration of this instance.
	 *
	 * @return String
	 */
	public String toString() {
		String bugOut = ""; 
		bugOut += "LocalPort:" + useLocalPort + ";";
		bugOut += "Services:" + getServiceListArgument();
		return bugOut;
	}
	
	
}
