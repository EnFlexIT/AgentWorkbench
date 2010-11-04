package agentgui.core.network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class PortChecker {

	private int freePort;
	
	public PortChecker(int portSearchStart){
		
		int currPort = portSearchStart;
		while (currPort < portSearchStart+10) {
			if ( available(currPort) ){
				freePort = currPort;
				break;
			}
			currPort++;
		}
	}
	
	public int getFreePort() {
		return freePort;
	}
	
	public boolean available(int port) {
		
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
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}
		return false;
	}
	
	
}
