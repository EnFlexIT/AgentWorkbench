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
package org.awb.env.networkModel.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;

import de.enflexit.common.classLoadService.BaseClassLoadServiceUtility;
import de.enflexit.common.classLoadService.ObjectInputStreamForClassLoadService;

/**
 * The Class DataModelEnDecoder64 provides static methods to encode or decode a data model 
 * of either a NetworkComponent or a GraphNode to or from a Base64 encoded string vector.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class DataModelEnDecoder64 {
	
	private static final String NO_ARRAY_INDICATOR = "[NoArray]";

	/**
	 * Return the specified data model as Base64 encoded string vector.
	 *
	 * @param dataModel the data model
	 * @return the data model Base64 encoded or <code>null</code>, if no model instance was provided 
	 */
	public static Vector<String> getDataModelBase64Encoded(Object dataModel) {
		
		if (dataModel==null) return null;
		
		Vector<String> modelVector64 = new Vector<>();
		if (dataModel!=null && dataModel.getClass().isArray()) {
			// --- Data model is an array ---------------------------
			Object[] dmArray = (Object[]) dataModel;
			for (int i = 0; i < dmArray.length; i++) {
				Object singleInstance = dmArray[i];
				String singleInstance64 = getObjectInstanceBase64Encoded(singleInstance);
				modelVector64.add(singleInstance64);
			}
			  
		} else {
			// --- Data model is NOT an array -----------------------
			String singleInstance64 = getObjectInstanceBase64Encoded(dataModel);
			modelVector64.add(NO_ARRAY_INDICATOR + singleInstance64);
		}
		return modelVector64;
	}

	/**
	 * Returns the specified object instance as Base64 encoded String.
	 *
	 * @param singleInstance the single instance
	 * @return the object instance base 64 encoded
	 */
	private static String getObjectInstanceBase64Encoded(Object singleInstance) {
		
		String singleInstance64 = null;
		
		// --- Try to get single instance as base64 encoded String ----
		try {
			if (singleInstance!=null) {
				synchronized (singleInstance) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(singleInstance);
					oos.close();
					singleInstance64 = new String(Base64.encodeBase64(baos.toByteArray()));
				}
			}
	        
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return singleInstance64;
	}
	
	
	
	/**
	 * Returns the data model array decoded from the Base64 encoded string vector.
	 *
	 * @param dataModel the data model
	 * @return the data model base 64 decoded
	 */
	public static Object getDataModelBase64Decoded(Vector<String> dataModel) {
		
		// --- Exit if vector is null or empty ----------------------
		if (dataModel==null || dataModel.size()==0) return null;
		
		// --- Define output array ----------------------------------
		Object[] objectArray = new Object[dataModel.size()];
		for (int i = 0; i < dataModel.size(); i++) {
			String singleInstance64 = dataModel.get(i);
			if (singleInstance64.startsWith(NO_ARRAY_INDICATOR)==true) {
				// --- Return single instance -----------------------
				singleInstance64 = singleInstance64.replace(NO_ARRAY_INDICATOR, "");
				Object singleInstance = getObjectInstanceBase64Decoded(singleInstance64);
				return singleInstance;
			}
			// --- Place in object array ----------------------------
			Object singleInstance = getObjectInstanceBase64Decoded(singleInstance64);
			objectArray[i] = singleInstance;
			
		}
		return objectArray;
	}
	/**
	 * Returns the object instance from the specified Base64 encoded String.
	 *
	 * @param singleInstance64 the single instance as Base64 encoded string 
	 * @return the decoded object instance
	 */
	private static Object getObjectInstanceBase64Decoded(String singleInstance64) {
		
		Object singleInstance = null;
		if (singleInstance64!=null && singleInstance64.isEmpty()==false) {
			try {
				byte[] data = Base64.decodeBase64(singleInstance64.getBytes());
				ObjectInputStreamForClassLoadService ois = new ObjectInputStreamForClassLoadService(new ByteArrayInputStream(data), BaseClassLoadServiceUtility.class);
				singleInstance = ois.readObject();
				ois.close();
				
			} catch (IOException | ClassNotFoundException | InstantiationError ex) {
				ex.printStackTrace();
			}
		}
		return singleInstance;
	}
	
}
