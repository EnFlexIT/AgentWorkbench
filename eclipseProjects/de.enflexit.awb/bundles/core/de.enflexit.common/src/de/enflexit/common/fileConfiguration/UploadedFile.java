package de.enflexit.common.fileConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Class UploadedFile is a DTO for uploaded files, carrying the content
 * as an InputStream as well as file name and media type as Strings.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UploadedFile {

	private String fileName;
	private String mediaType;
	private InputStream inputStream;
	
	/**
	 * Instantiates a new uploaded file.
	 * @param inputStream the body input stream
	 */
	public UploadedFile(InputStream inputStream) {
		this("", "", inputStream);
	}
	/**
	 * Instantiates a new uploaded file.
	 *
	 * @param fileName the file name
	 * @param mediaType the media type
	 * @param inputStream the input stream
	 */
	public UploadedFile(String fileName, String mediaType, InputStream inputStream) {
		this.setFileName(fileName);
		this.setMediaType(mediaType);
		this.setInputStream(inputStream);
	}

	/**
	 * Returns the file name.
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * Sets the file name.
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns the media type.
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}
	/**
	 * Sets the media type.
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * Returns the input stream.
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	/**
	 * Sets the input stream. Will be set to null
	 * if the inputStream has no data.
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {

		if (inputStream == null) {
			this.inputStream = null;
			return;
		}

		try {
			// --- Find out whether the inputStream contains data -------------
			byte[] data = inputStream.readAllBytes();
			// --- Set inputStream to null if it doesn't ------------------
			if (data.length == 0) {
				this.inputStream = null;
				
			} else {
				this.inputStream = new ByteArrayInputStream(data);
			}
		} catch (IOException ioExceptionRead) {
			ioExceptionRead.printStackTrace();

		} finally {
			try {
				inputStream.close();

			} catch (IOException ioExceptionClose) {
				ioExceptionClose.printStackTrace();
			}
		}
	}
	
}