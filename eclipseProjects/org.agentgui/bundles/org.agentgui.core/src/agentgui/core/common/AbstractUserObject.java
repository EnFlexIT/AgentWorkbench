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
package agentgui.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
		
		if (destinationFile!=null && userObject!=null && userObject instanceof AbstractUserObject) {
			
			try {
				// --- Define the JAXB context ------------
				JAXBContext pc = JAXBContext.newInstance(userObject.getClass());
				Marshaller pm = pc.createMarshaller();
				pm.setProperty(Marshaller.JAXB_ENCODING, FILE_ENCODING);
				pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

				// --- Write instance to xml-File ---------
				Writer pw = new FileWriter(destinationFile);
				pm.marshal(userObject, pw);
				pw.close();
				
				successfulSaved = true;
				
			} catch (Exception ex) {
				System.out.println("[" + AbstractUserObject.class.getSimpleName() + "] Error while saving the user object as XML file:");
				ex.printStackTrace();
			} 
		}
		return successfulSaved;
	}

	
	/**
	 * Load a user object from a XML file.
	 *
	 * @param sourceFile the source XML file
	 * @param rootClassToBeBound the root class to be bound
	 * @return the user object
	 */
	public static AbstractUserObject loadUserObjectFromXmlFile(File sourceFile, Class<?> rootClassToBeBound) {
	
		if (sourceFile.exists()==false) return null;
		if (rootClassToBeBound==null) return null;
		
		AbstractUserObject userObject = null;
		try {
			
			JAXBContext context = JAXBContext.newInstance(rootClassToBeBound);
			Unmarshaller unMarsh = context.createUnmarshaller();
			
			InputStream inputStream= new FileInputStream(sourceFile);
			InputStreamReader isReader  = new InputStreamReader(inputStream, FILE_ENCODING);
			
			Object jaxbObject = unMarsh.unmarshal(isReader);
			isReader.close();
			inputStream.close();
			
			if (jaxbObject!=null && jaxbObject instanceof AbstractUserObject) {
				userObject = (AbstractUserObject)jaxbObject;
			}
			
		} catch (Exception ex) {
			System.out.println("[" + AbstractUserObject.class.getSimpleName() + "] Error while loading the user object from XML file:");
			ex.printStackTrace();
		}
		return userObject;
	}
	
}
