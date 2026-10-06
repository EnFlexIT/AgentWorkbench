package de.enflexit.awb.ws.core.db.dataModel;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

/**
 * The Class SiteContentProperties.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
public class SiteContentProperties extends SiteContent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "siteContentProperties")
	private List<SiteContentPropertyEntry> propertyEntries;
	
	
	/**
	 * Returns the property entries.
	 * @return the property entries
	 */
	public List<SiteContentPropertyEntry> getPropertyEntries() {
		return propertyEntries;
	}
	/**
	 * Sets the property entries.
	 * @param propertyEntries the new property entries
	 */
	public void setPropertyEntries(List<SiteContentPropertyEntry> propertyEntries) {
		this.propertyEntries = propertyEntries;
	}
	
}
