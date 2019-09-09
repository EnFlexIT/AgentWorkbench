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
package de.enflexit.common;

import java.io.File;

/**
 * The Class PathHandling.
 */
public class PathHandling {

	/**
	 * Will translate a given path name conform to the local system (Win, Mac, Linux).
	 *
	 * @param fileName the file name
	 * @return the path name for local system
	 */
	public static String getPathName4LocalOS(String fileName) {
		
		if (fileName==null) return null;
		
		// --- Put slash into right direction -------------
		String corrected = "";
		for (int i = 0; i < fileName.length(); i++) {

			boolean skipChar = false;
			String	chara = Character.toString(fileName.charAt(i));
			if (chara.equals("\\") || chara.equals("/")) {
				chara = File.separator;
				if (corrected.isEmpty()==false && corrected.substring(corrected.length()-1).equals(File.separator)) {
					skipChar=true;
				}
			}
			if (skipChar==false) corrected = corrected + chara;
		}
		return corrected;
	}
	
	
	/**
	 * Returns a file name suggestion for the specified initial file name proposal.
	 * E.g German umlauts will be replaced as well as doubled spaces and so on.
	 *
	 * @param initialFileNameProposal the initial file name proposal
	 * @return the file name suggestion
	 */
	public static String getFileNameSuggestion(String initialFileNameProposal) {
		
		String regExp = "[A-Z;a-z;\\-;_;0-9]";
		String suggest = initialFileNameProposal.trim();
		
		// --- Preparations ---------------------
		suggest = suggest.replaceAll("( )+", " ");
		suggest = suggest.replace(" ", "_");
		suggest = suggest.replace("ä", "ae");
		suggest = suggest.replace("ö", "oe");
		suggest = suggest.replace("ü", "ue");
		
		// --- Examine all characters -----------
		String suggestNew = "";
		for (int i = 0; i < suggest.length(); i++) {
			String sinlgeChar = "" + suggest.charAt(i);
			if (sinlgeChar.matches(regExp)==true) {
				suggestNew = suggestNew + sinlgeChar;	
			}						
	    }
		suggest = suggestNew;
		suggest = suggest.replaceAll("(_)+", "_");
		suggest = suggest.replaceAll("(-)+", "-");
		return suggest;
	}
	
	
}
