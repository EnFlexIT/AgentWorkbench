package de.enflexit.db.derby.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.derby.impl.drda.NetworkServerControlImpl;

/**
 * The Class DerbyNetworkServer controls start and shutdown of the Derby NetworkServer.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DerbyNetworkServer extends Thread {

	// ------------------------------------------------------------------------
	// --- From here, static access methods -----------------------------------
	// ------------------------------------------------------------------------
	private static DerbyNetworkServer derbyNetworkServerInstance = null;
	
	/**
	 * Will start the Derby network server.
	 */
	public static void execute() {
		
		if (derbyNetworkServerInstance!=null) return;
		
		try {
			
			DerbyNetworkServerProperties sProperties = new DerbyNetworkServerProperties();
			if (sProperties.isStartDerbyNetworkServer()==false) return;
			
			InetAddress iNet = sProperties.getHostInetAddress();
			int serverPort   = sProperties.getPort();
			String userName  = sProperties.getUserName();
			String password  = sProperties.getPassword();
			derbyNetworkServerInstance = new DerbyNetworkServer(iNet, serverPort,userName, password);
			
		} catch (UnknownHostException uhEx) {
			uhEx.printStackTrace();
		}
	}
	
	/**
	 * Terminates the Derby network server.
	 */
	public static void terminate() {
		if (derbyNetworkServerInstance==null) return;
		derbyNetworkServerInstance.doStop();
		derbyNetworkServerInstance = null;
	}


	// ------------------------------------------------------------------------
	// --- From here, instance handling ---------------------------------------
	// ------------------------------------------------------------------------
	private NetworkServerControlImpl derbyServer = null;
	
	private InetAddress address;
	private int portNumber;
	private String userName;
	private String password;
    
	/**
	 * Instantiates a new derby network server.
	 */
	public DerbyNetworkServer() {
		this(null, 1527, null, null);
	}
	/**
	 * Instantiates a new derby network server.
	 *
	 * @param address the address
	 * @param portNumber the port number
	 * @param userName the user name
	 * @param password the password
	 */
	public DerbyNetworkServer(InetAddress address, int portNumber, String userName, String password) {
		this.address = address;
		this.portNumber = portNumber;
		this.userName = userName;
		this.password = password;
		this.setName(DerbyNetworkServer.class.getSimpleName() + "-ControllerThread");
		this.start();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		try {
			if (this.userName!=null) {
				derbyServer = new NetworkServerControlImpl(this.address, this.portNumber, this.userName, this.password);
			} else {
				derbyServer = new NetworkServerControlImpl();
			}
			derbyServer.executeWork(NetworkServerControlImpl.COMMAND_START);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Sets to stop the Derby network server.
	 */
	private void doStop() {
		try {
			this.derbyServer.shutdown();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns the NetworkServerControlImpl that manages the current Derby instance.
	 * @return the NetworkServerControlImpl
	 */
	public NetworkServerControlImpl getNetworkServerControlImpl() {
		return this.derbyServer;
	}
	
}
