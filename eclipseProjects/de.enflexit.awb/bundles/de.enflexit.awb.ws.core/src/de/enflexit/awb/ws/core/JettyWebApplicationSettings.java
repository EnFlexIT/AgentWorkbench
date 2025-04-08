package de.enflexit.awb.ws.core;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class JettyWebApplicationSettings.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JettyWebApplicationSettings", propOrder = {
    "downloadURL",
    "updateStrategy"
})
public class JettyWebApplicationSettings implements Serializable {

	private static final long serialVersionUID = 1900972227134760200L;

	public enum UpdateStrategy {
		Automatic,
		AskUser,
		Manual
	}
	
	private String downloadURL; 
	private UpdateStrategy updateStrategy;
	
	
	/**
	 * Gets the download URL.
	 * @return the download URL
	 */
	public String getDownloadURL() {
		return downloadURL;
	}
	/**
	 * Sets the download URL.
	 * @param downloadURL the new download URL
	 */
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}
	
	/**
	 * Gets the update strategy.
	 * @return the update strategy
	 */
	public UpdateStrategy getUpdateStrategy() {
		if (updateStrategy==null) {
			updateStrategy = UpdateStrategy.Automatic;
		}
		return updateStrategy;
	}
	/**
	 * Sets the update strategy.
	 * @param updateStrategy the new update strategy
	 */
	public void setUpdateStrategy(UpdateStrategy updateStrategy) {
		this.updateStrategy = updateStrategy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj==null) return false;
		if (obj==this) return true;
		
		if (obj instanceof JettyWebApplicationSettings == false) return false;
	
		JettyWebApplicationSettings compObj = (JettyWebApplicationSettings) obj;
		
		if (compObj.getDownloadURL()==null && this.getDownloadURL()==null) return true;
		if ((compObj.getDownloadURL()!=null && this.getDownloadURL()==null) || (compObj.getDownloadURL()==null && this.getDownloadURL()!=null)) return false;	
		if (this.getDownloadURL().equals(compObj.getDownloadURL())==false) return false;

		if (this.getUpdateStrategy()!=compObj.getUpdateStrategy()) return false;
		
		return true;
	}
	
}
