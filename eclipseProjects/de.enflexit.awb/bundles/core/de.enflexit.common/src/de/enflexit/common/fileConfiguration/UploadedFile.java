package de.enflexit.common.fileConfiguration;

import java.io.InputStream;

/**
 * The Class UploadedFile is a DTO for uploaded files, carrying the body
 * as an InputStream as well as file name and media type as Strings.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UploadedFile {

	private String fileName;
	private String mediaType;
	private InputStream body;
	
	public UploadedFile(InputStream body) {
		this("", "", body);
	}
	
	public UploadedFile(String fileName, String mediaType, InputStream body) {
		this.setFileName(fileName);
		this.setMediaType(mediaType);
		this.setBody(body);
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * @return the body
	 */
	public InputStream getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(InputStream body) {
		this.body = body;
	}
}