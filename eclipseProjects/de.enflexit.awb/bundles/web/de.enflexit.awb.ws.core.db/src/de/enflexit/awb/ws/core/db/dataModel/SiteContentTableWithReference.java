package de.enflexit.awb.ws.core.db.dataModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * The Class SiteContentTableWithReference.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Entity
public class SiteContentTableWithReference extends SiteContentTable {


	private static final long serialVersionUID = 1L;
	
	@Column(name = "table_data_reference")
	private String reference;

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
}
