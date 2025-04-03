package de.enflexit.awb.ws.dynSiteApi.samples;

import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText;

/**
 * A factory for creating DynamicContentExample objects.
 */
public class DynamicContentExampleFactory {

	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param mimeType the mime type
	 * @param text the text
	 * @return a new instance of the type SiteContentText
	 */
	public static SiteContentText createSiteContentText(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, String mimeType, String text) {
		
		SiteContentText scText = new SiteContentText();
		scText.setUniqueContentID(uniqueContentID);
		scText.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		scText.setEditable(isEditable);

		scText.setMimeType("text/plain");
		if (mimeType!=null && mimeType.isBlank()==false) {
			scText.setMimeType(mimeType);
		}
		scText.setText(text);
		
		return scText;
	}

	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param mimeType the mime type
	 * @param dataInBase64 the data in base 64
	 * @return a new instance of the type SiteContentImage
	 */
	public static SiteContentImage createSiteContentImage(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, String mimeType, String dataInBase64) {
		
		SiteContentImage scImage = new SiteContentImage();
		scImage.setUniqueContentID(uniqueContentID);
		scImage.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		scImage.setEditable(isEditable);
		
		scImage.setMimeType(mimeType);
		scImage.setDataInB64(dataInBase64);
		return scImage;
	}
	
}
