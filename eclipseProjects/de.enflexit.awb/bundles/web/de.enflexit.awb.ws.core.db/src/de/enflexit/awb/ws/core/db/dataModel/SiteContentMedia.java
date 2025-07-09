package de.enflexit.awb.ws.core.db.dataModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

/**
 * The abstract Class SiteContentMedia.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
public abstract class SiteContentMedia extends SiteContent {

	@Column(name = "mime_type")
	private String mimeType;
	
	@Lob
	@Column(name = "text_data")
	private String textData;

	
	/**
	 * Returns the mime type.
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}
	/**
	 * Sets the mime type.
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Returns the text data.
	 * @return the textData
	 */
	public String getTextData() {
		return textData;
	}
	/**
	 * Sets the text data.
	 * @param textData the textData to set
	 */
	public void setTextData(String textData) {
		this.textData = textData;
	}
	
}
