package mas;

/*****************************************************************
 JADE - Java Agent DEvelopment Framework is a framework to develop 
 multi-agent systems in compliance with the FIPA specifications.
 Copyright (C) 2002 donated to TILAB

 GNU Lesser General Public License

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/
import jade.imtp.rmi.ServiceManagerRMI;
import java.net.MalformedURLException;
import java.rmi.*;

/**
 * A simple class to test for the existence of a JADE platform. This class can
 * be used as an application (on the command line) or also used by an external
 * application.
 * 
 * @author Stelios Gerogiannakis, (c) Archetypon S.A. 2002, Project Onto-Logging
 */
public class PlatformTest implements Remote {

	/** name of the JADE platform in the RMI registry */
	private static String smName = jade.core.IMTPManager.SERVICE_MANAGER_NAME;

	/**
	 * Checks if the JADE platform exists on the remote registry.
	 * 
	 * @param host
	 *            the fully qualified machine name or IP; leave null for
	 *            localhost
	 * @param port
	 *            where RMI is listening; leave null for default
	 * @return String null if all went well, an error message if not
	 */
	public static String isJADERunning(String host, String port) {
		int rmiPort = 1099;
		String rmiHost = "localhost";
		String url = null;

		// parse the port
		try {
			rmiPort = Integer.parseInt(port);
		} catch (Exception e) {
		}

		// get the host
		if (host != null)
			rmiHost = host;

		// create the URL
		url = "rmi://" + rmiHost + ":" + rmiPort + "/" + smName;

		// try to bind and unbind the object to the RMI
		try {
			ServiceManagerRMI obj = (ServiceManagerRMI) Naming.lookup(url);
			String test = obj.getPlatformName();

		} catch (NotBoundException e) {
			// this means that the registry is running
			return "JADE is not yet bound to the remote registry";
		} catch (MalformedURLException e) {
			return "Not a properly formatted RMI URL (" + url
					+ "). Check for invalid characters";
		} catch (MarshalException e) {
			// ignored, shows that RMI was found
		} catch (RemoteException e) {
			return "RMI does not exist on host " + rmiHost
					+ " or is not listening on port " + rmiPort;
		}

		return null;
	}
}