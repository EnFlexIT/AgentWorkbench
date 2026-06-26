package de.enflexit.common.fileConfiguration;

/**
 * The Class FileDownload consists of the file to be downloaded as a byte array
 * and corresponding meta information, namely file name and content type.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileDownload {

	private byte[] content;
	private String fileName;
	private String contentType;
	
	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	public static final String XML_CONTENT_TYPE = "application/xml";
	
	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}
	/**
	 * @param content the content of the file
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}
	/**
	 * @return the name of the file
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * Returns the content type of the file, e.g. 'application/xml' or the
	 * less specific 'application/octet-stream' as the default option if not set.
	 * 
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType != null ? contentType : DEFAULT_CONTENT_TYPE;
	}
	/**
	 * @param contentType the content type of the file
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
}