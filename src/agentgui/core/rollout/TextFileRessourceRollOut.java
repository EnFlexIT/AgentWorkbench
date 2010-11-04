package agentgui.core.rollout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TextFileRessourceRollOut {

	private String srcRefer = null; 
	private String destPath = null;
	private String fileContent = null;
	
	/**
	 * Constructor of this class
	 * @param srcReference    = Reference to a inner file object (e. g. 'mas.onto.AgentGUIProjectOntology.txt')
	 * @param destFile        = Destination Path and File 
	 * @param doDirectRollout = You can do direct read/write or work on the filecontent before writing
	 */
	public TextFileRessourceRollOut(String srcReference, String destFile, boolean doDirectRollout) {
		
		srcRefer = srcReference; 
		destPath = destFile;
		
		this.readFile2String();
		if (doDirectRollout==true) {
			this.writeFile();
		}
	}
	
	/**
	 * Reads the Text-File content
	 */
	public void readFile2String() {
		
		InputStream inputStream  = this.getClass().getClassLoader().getResourceAsStream( srcRefer ); 
		StringBuilder strBuilder = new StringBuilder();
		String inputLine		 = "";	
		
		// --- Datei einlesen ---------------------------------------
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader( inputStream, "UTF-8"));
			while ((inputLine = reader.readLine()) != null) {
				strBuilder.append(inputLine).append("\n");
			}
			inputStream.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		// --- Textinhalt merken ------------------------------------
		fileContent = strBuilder.toString();
	}
	
	/**
	 * Returns the Text-File-Content
	 * @return 
	 */
	public String toString() {
		return fileContent;
	}
	
	/**
	 * Returns the Text-File-Content
	 * @return 
	 */
	public String getFileString() {
		return fileContent;
	}
	
	/**
	 * Writes the file to the specified location with a new file content
	 */
	public void writeFile(String newFileContent) {
		this.fileContent = newFileContent;
		this.writeFile();
	}
	
	/**
	 * Writes the file to the specified location
	 */
	public void writeFile() {
		
		BufferedWriter buffWrite = null;
		// --- Als Datei im richtigen Verzeichnis ablegen -----
		try {
			buffWrite = new BufferedWriter(new FileWriter(destPath));
			//fileContent.replaceAll("\n", System.getProperty("line.separator"));
			buffWrite.write(fileContent);
			buffWrite.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
