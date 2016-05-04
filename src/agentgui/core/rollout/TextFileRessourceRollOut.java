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
package agentgui.core.rollout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * This class allows to roll out text file resources out of the jar-file of <b>Agent.GUI</b>.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TextFileRessourceRollOut {

	private String srcRefer = null; 
	private String destPath = null;
	private String fileContent = null;
	
	/**
	 * Constructor of this class.
	 *
	 * @param srcReference    Reference to a inner file object (e. g. 'mas.onto.AgentGUIProjectOntology.txt')
	 * @param destinationFile Destination Path and File
	 * @param doDirectRollout You can do direct read/write or work on the file content before writing
	 */
	public TextFileRessourceRollOut(String srcReference, String destinationFile, boolean doDirectRollout) {
		
		srcRefer = srcReference; 
		destPath = destinationFile;
		
		this.readFile2String();
		if (doDirectRollout==true) {
			this.writeFile();
		}
	}
	
	/**
	 * Reads the Text-File content.
	 */
	public void readFile2String() {
		
		InputStream inputStream  = null;
		StringBuilder strBuilder = new StringBuilder();
		String inputLine		 = "";	
		
		// --- Datei einlesen ---------------------------------------
		try {
			inputStream  = this.getClass().getClassLoader().getResourceAsStream(srcRefer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((inputLine = reader.readLine()) != null) {
				strBuilder.append(inputLine).append("\n");
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream!=null) inputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		// --- Textinhalt merken ------------------------------------
		fileContent = strBuilder.toString();
	}
	
	/**
	 * Returns the Text-File-Content.
	 *
	 * @return the string
	 */
	public String toString() {
		return fileContent;
	}
	
	/**
	 * Returns the Text-File-Content.
	 *
	 * @return the file string
	 */
	public String getFileString() {
		return fileContent;
	}
	
	/**
	 * Writes the file to the specified location with a new file content.
	 *
	 * @param newFileContent the new file content
	 */
	public void writeFile(String newFileContent) {
		this.fileContent = newFileContent;
		this.writeFile();
	}
	
	/**
	 * Writes the file to the specified location.
	 */
	public void writeFile() {
		
		BufferedWriter buffWrite = null;
		try {
			buffWrite = new BufferedWriter(new FileWriter(destPath));
			buffWrite.write(fileContent);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				buffWrite.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
}
