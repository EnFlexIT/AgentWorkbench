package de.enflexit.awb.ws.dynSiteApi.content;

import java.util.List;

import de.enflexit.awb.ws.dynSiteApi.gen.model.PropertyEntry;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValueType;

/**
 * A factory for creating DynamicContentExample objects.
 */
public class DynamicContentFactory {

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
	 * @param width the width of the image
	 * @param height the height of the image
	 * @return a new instance of the type SiteContentImage
	 */
	public static SiteContentImage createSiteContentImage(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, String mimeType, String dataInBase64, Integer width, Integer height) {
		
		SiteContentImage scImage = new SiteContentImage();
		scImage.setUniqueContentID(uniqueContentID);
		scImage.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		scImage.setEditable(isEditable);
		
		scImage.setMimeType(mimeType);
		scImage.setDataInB64(dataInBase64);
		return scImage;
	}
	
	
	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param peList the list of PropertyEntry
	 * @return a new instance of the type SiteContentImage
	 */
	public static SiteContentProperties createSiteContentProperties(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<PropertyEntry> peList) {
		
		SiteContentProperties scProps = new SiteContentProperties();
		scProps.setUniqueContentID(uniqueContentID);
		scProps.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		scProps.setEditable(isEditable);
		
		scProps.setPropertyEntries(peList);
		return scProps;
	}

	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param key the key
	 * @param valueType the value type
	 * @param value the value
	 * @return the property entry
	 */
	public static PropertyEntry createPropertyEntry(String key, ValueType valueType, String value) {
		
		PropertyEntry pe = new PropertyEntry();
		pe.setKey(key);
		pe.setValueType(valueType);
		pe.setValue(value);
		return pe;
	}
	
}
