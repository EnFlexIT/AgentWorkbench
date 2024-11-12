package de.enflexit.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * The Class AbstracttUserObject serves as base for individual user objects
 * that may be saved in an XML structure.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractUserObject implements Serializable {

	private static final long serialVersionUID = -3244668521291248991L;

	private static final String FILE_ENCODING = "UTF-8";
	
	/**
	 * Saves the specified user object as XML file, if possible.
	 *
	 * @param destinationFile the destination file
	 * @param userObject the user object
	 * @return true, if successful
	 */
	public static boolean saveUserObjectAsXmlFile(File destinationFile, Object userObject) {
		
		boolean successfulSaved = false;
		
		// --- Check if the file was defined --------------
		if (destinationFile==null) {
			System.err.println("[" + AbstractUserObject.class.getSimpleName() + "] The path for saving a user object as XML file is not allowed to be null!");
			return false;
		}
		// --- Check the user object instance -------------
		if (userObject==null) {
			return true;
		} 
		
		// --- Check type of the user object --------------
		if (!(userObject instanceof AbstractUserObject)) {
			System.err.println("[" + AbstractUserObject.class.getSimpleName() + "] The user object to save as XML file needs to be of type '" + AbstractUserObject.class.getName() + "'!");
			
		} else {
			
			FileWriter fileWriter = null;
			try {
				// --- Define the JAXB context ------------
				JAXBContext pc = JAXBContext.newInstance(userObject.getClass());
				Marshaller pm = pc.createMarshaller();
				pm.setProperty(Marshaller.JAXB_ENCODING, FILE_ENCODING);
				pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

				// --- Write instance to xml-File ---------
				fileWriter = new FileWriter(destinationFile);
				pm.marshal(userObject, fileWriter);
				successfulSaved = true;
				
			} catch (Exception ex) {
				System.out.println("[" + AbstractUserObject.class.getSimpleName() + "] Error while saving the user object as XML file:");
				ex.printStackTrace();
			} finally {
				try {
					if (fileWriter!=null) fileWriter.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}				
			}
		}
		return successfulSaved;
	}

	
	/**
	 * Load a user object from a XML file.
	 *
	 * @param sourceFile the source XML file
	 * @param rootClassToBeBound the root class to be bound within the JAXB Unmarshaller.
	 * @return the loaded user object
	 */
	public static AbstractUserObject loadUserObjectFromXmlFile(File sourceFile, Class<?> rootClassToBeBound) {
	
		if (sourceFile==null || sourceFile.exists()==false) return null;
		if (rootClassToBeBound==null) return null;
		
		AbstractUserObject userObject = null;
		InputStream inputStream = null;
		InputStreamReader isReader = null;
		try {
			
			JAXBContext context = JAXBContext.newInstance(rootClassToBeBound);
			Unmarshaller unMarsh = context.createUnmarshaller();
			
			inputStream = new FileInputStream(sourceFile);
			isReader  = new InputStreamReader(inputStream, FILE_ENCODING);
			
			Object jaxbObject = unMarsh.unmarshal(isReader);
			if (jaxbObject!=null && jaxbObject instanceof AbstractUserObject) {
				userObject = (AbstractUserObject)jaxbObject;
			}
			
		} catch (Exception ex) {
			System.out.println("[" + AbstractUserObject.class.getSimpleName() + "] Error while loading the user object from XML file:");
			ex.printStackTrace();
		} finally {
			try {
				if (isReader!=null) isReader.close();
				if (inputStream!=null) inputStream.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}	
		}
		return userObject;
	}
	
}
