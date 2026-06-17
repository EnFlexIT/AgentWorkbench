package de.enflexit.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages log transport services. It receives log
 * messages from the @see TransportAppender and sends them to every
 * registered LogTransportService.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class LogTransportServiceRegistry {

	private static List<LogTransportService> logTransportServices;

	/**
	 * Returns the log transport services list.
	 *
	 * @return logTransportServices
	 */
	private static List<LogTransportService> getLogTransportServices(){
		
		if (logTransportServices == null) {
			logTransportServices = new ArrayList<LogTransportService>();
		}
		return logTransportServices;
	}
	
	/**
	 * Registers the specified LogTransportService.
	 *
	 * @param transportService2Register the transport service to register
	 */
	public static void registerTransportService(LogTransportService transportService2Register) {
		getLogTransportServices().add(transportService2Register);
	}

	/**
	 * Removes the specified LogTransportService from the registry.
	 *
	 * @param transportService2Remove the transport service to remove from registry
	 */
	public static void unregisterTransportService(LogTransportService transportService2Remove) {
		getLogTransportServices().remove(transportService2Remove);
	}

	/**
	 * Sends the message to all registered log transport services.
	 *
	 * @param message the message to send
	 */
	public static void send(String message) {
		for (LogTransportService lts : logTransportServices) {
			try {
				lts.sendLog(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}