/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.codec.binary.Base64;

import agentgui.core.application.Language;

/**
 * The Class UpdateInformation extends the class {@link VersionInformation} and add some additional
 * information for the download of an update.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement
public class UpdateInformation extends VersionInformation {

	private static final long serialVersionUID = -4484122392563983935L;

	private String downloadLink; 
	private String downloadFile;
	private Integer downloadSize;
	
	@XmlTransient private boolean error = false;
	@XmlTransient private String errorMsg;
	
	
	/**
	 * Creates an example file.
	 * @param destinFile the destination file
	 */
	public void createExampleFile(String destinFile) {
		
		this.setMajorRevision(0);
		this.setMinorRevision(98);
		this.setBuild(123);
		this.setDownloadLink("http://update.agentgui.org/updates/Agent.GUI_0.98_123.zip");
		this.setDownloadFile("Agent.GUI_0.98_123.zip");
		this.setDownloadSize(72456123);
		this.saveUpdateInformation(destinFile);
	}
	
	/**
	 * Save update information.
	 * @param updateFile the update file
	 */
	public void saveUpdateInformation(File updateFile) {
		this.saveUpdateInformation(updateFile.getAbsolutePath());
	}
	/**
	 * Save update information.
	 * @param updateFilePath the update file
	 */
	public void saveUpdateInformation(String updateFilePath) {
		
		String downloadLink = this.getDownloadLink();
		String downloadFile = this.getDownloadFile();
		String downloadLink64 = null;
		String downloadFile64 = null;

		// --- Encode downloadLink and downloadFile -------
		try {
			downloadLink64 = new String(Base64.encodeBase64(downloadLink.getBytes("UTF8")));
			downloadFile64 = new String(Base64.encodeBase64(downloadFile.getBytes("UTF8")));
			
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}

		// --- Temporary change the values ----------------
		this.setDownloadLink(downloadLink64);
		this.setDownloadFile(downloadFile64);
		
		// --- Save File ----------------------------------
	    JAXBContext context;
		try {
			FileWriter componentFileWriter = new FileWriter(updateFilePath);
			
			context = JAXBContext.newInstance(UpdateInformation.class);
		    Marshaller marsh = context.createMarshaller();
		    marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		    marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		    marsh.marshal(this, componentFileWriter);

		    componentFileWriter.close();

		} catch (JAXBException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// --- Temporary change the values ----------------
		this.setDownloadLink(downloadLink);
		this.setDownloadFile(downloadFile);
		
	}

	/**
	 * Load update information.
	 * @param updateFile the update file
	 */
	public void loadUpdateInformation(File updateFile) {
		this.loadUpdateInformation(updateFile.getAbsolutePath());
	}
	/**
	 * Load update information.
	 * @param updateFilePath the update file
	 */
	public void loadUpdateInformation(String updateFilePath) {
	    
		File componentFile = null;
		// --- Load file ----------------------------------
		UpdateInformation updateInfo = null;
		try {
		    componentFile = new File(updateFilePath);
		    FileReader componentReader = new FileReader(componentFile);
	
		    JAXBContext context = JAXBContext.newInstance(UpdateInformation.class);
		    Unmarshaller unmarsh = context.createUnmarshaller();
		    updateInfo = (UpdateInformation) unmarsh.unmarshal(componentReader);
		    
		    componentReader.close();
		    
		} catch (JAXBException ex) {
//			ex.printStackTrace();
			String msg = "Error while parsing the update information from the configured update server!";
		    msg = Language.translate(msg, Language.EN); 
			System.err.println(msg);
		    this.setError(true);
		    this.setErrorMessage(msg);
		    return;
		} catch (FileNotFoundException ex) {
		    ex.printStackTrace();
		    this.setError(true);
		    return;
		} catch (IOException ex) {
		    ex.printStackTrace();
		    this.setError(true);
		    return;
		}

		// --- Decode downloadLink and downloadFile -------
		String downloadLink = null;
		String downloadFile = null;
		try {
			downloadLink = new String(Base64.decodeBase64(updateInfo.getDownloadLink().getBytes("UTF8")));
			downloadFile = new String(Base64.decodeBase64(updateInfo.getDownloadFile().getBytes("UTF8")));
			
			updateInfo.setDownloadLink(downloadLink);
			updateInfo.setDownloadFile(downloadFile);
			
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		
		// --- Finally, set this instance -----------------
		this.setMajorRevision(updateInfo.getMajorRevision());
	    this.setMinorRevision(updateInfo.getMinorRevision());
	    this.setBuild(updateInfo.getBuild());
	    this.setDownloadLink(updateInfo.getDownloadLink());
	    this.setDownloadFile(updateInfo.getDownloadFile());
	    this.setDownloadSize(updateInfo.getDownloadSize());
		
	}
	
	
	/**
	 * Gets the download link.
	 * @return the downloadLink
	 */
	public String getDownloadLink() {
		return downloadLink;
	}
	/**
	 * Sets the download link.
	 * @param downloadLink the downloadLink to set
	 */
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
	
	/**
	 * Gets the download file.
	 * @return the downloadFile
	 */
	public String getDownloadFile() {
		return downloadFile;
	}
	/**
	 * Sets the download file.
	 * @param downloadFile the downloadFile to set
	 */
	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

	/**
	 * Sets the download size.
	 * @param downloadSize the new download size
	 */
	public void setDownloadSize(Integer downloadSize) {
		this.downloadSize = downloadSize;
	}
	/**
	 * Gets the download size.
	 * @return the download size
	 */
	public Integer getDownloadSize() {
		return downloadSize;
	}

	/**
	 * Sets that there is an error.
	 * @param error the new error
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	/**
	 * Checks if there is an error.
	 * @return true, if there is an error
	 */
	@XmlTransient 
	public boolean isError() {
		return error;
	}

	/**
	 * Sets the error message.
	 * @param errorMsg the new error message
	 */
	public void setErrorMessage(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	/**
	 * Gets the error message.
	 * @return the error message
	 */
	@XmlTransient 
	public String getErrorMessage() {
		return errorMsg;
	}

}
