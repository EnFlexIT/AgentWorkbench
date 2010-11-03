package network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import application.Application;

public class JadeUrlChecker {

	private InetAddress currAddress = null;
	private InetAddress addressLocal = null;
	private InetAddress addressLocalAlt = null;
	
	private String currURL = null;
	private Integer currPort = 0;
	private Integer currPort4MTP = 0;
	
	
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
			System.err.println( "[" + Application.RunInfo.AppTitel() + "] Error while try to resolve the address '" + err.getLocalizedMessage() + "'. Please check your Agent.GUI - start options." );
			currURL = null;
		}
		
	}
	
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
	
	public boolean isLocalhost() {
		if (currAddress.equals(addressLocal)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getJADEurl(){
		if ( currAddress!=null && currPort.equals(-1)==false) {
			return currAddress.getHostAddress() + ":" + currPort + "/JADE";	
		} else {
			return null;
		}
	}
	public String getJADEurl4MTP() {
		if ( currAddress!=null && currPort4MTP.equals(-1)==false) {
			return "http://" + currAddress.getHostAddress() + ":" + currPort4MTP + "/acc";	
		} else {
			return null;
		}
	}
	public String getHostIP() {
		if (currAddress==null) {
			return null;
		} else {
			return currAddress.getHostAddress();
		}
	}

	public String getHostName() {
		if (currAddress==null) {
			return null;
		} else {
			return currAddress.getHostName();	
		}		
	}
	
	public Integer getPort() {
		return currPort; 
	}
	public void setPort(Integer newPort) {
		currPort = newPort; 
	}
	public void setPort4MTP(Integer newPort4MTP) {
		this.currPort4MTP = newPort4MTP;
	}
	public Integer getPort4MTP() {
		return currPort4MTP;
	}
	
}
