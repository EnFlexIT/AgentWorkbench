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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * This class controls the input fields for number formats. 
 * For integer values just numbers are allowed. 
 * For floats numbers and just one separator character "," or "." is allowed.
 * The class can be used by adding this as a KeyListener
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class KeyAdapter4Numbers extends KeyAdapter {

	private boolean isFloatValue = false;
	
	/**
	 * Instantiates a new number watcher.
	 * @param isFloatValue indicates, if this watcher is used for Float values. If not Integer values are assumed.
	 */
	public KeyAdapter4Numbers(boolean isFloatValue) {
		this.isFloatValue = isFloatValue;
	}

	/**
	 * Count occurrences of a character in a given search string.
	 * @param searchString the String to search in
	 * @param searchCharacter to search for
	 * @return the number of occurrences of searchCharacter in searchString 
	 */
	public int countCharsInString(String searchString, char searchCharacter) {
		if (searchString==null) return 0; 
		int count = 0;
	    for (int i=0; i < searchString.length(); i++) {
	        if (searchString.charAt(i)==searchCharacter) {
	             count++;
	        }
	    }
	    return count;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent kT) {
		
		char charackter = kT.getKeyChar();
		String singleChar = Character.toString(charackter);
		
		JTextField displayField = (JTextField) kT.getComponent();
		String currValue = displayField.getText();
		int caretPosition = displayField.getCaretPosition();

		// --- Allow negative values ------------------
		if (this.isFloatValue==true) {
			// --- Float values -----------------------
			if (singleChar.equals("-") && countCharsInString(currValue, charackter)<2) {
				return;
			}
		} else {
			// --- Integer values ---------------------
			if (singleChar.equals("-") && caretPosition==0 && currValue.startsWith("-")==false) {
				return;
			}
		}
		
		if (this.isFloatValue==true) {
			// --- Float values -----------------------
			if (singleChar.equals(".") || singleChar.equals(",")) {
				if (currValue!=null) {
					if (currValue.contains(".") || currValue.contains("," )) {
						kT.consume();	
						return;
					}
				}
			} else  if (singleChar.equalsIgnoreCase("e")) {
				if (currValue!=null) {
					if (currValue.contains("e")) {
						kT.consume();	
						return;
					}	
				}
			} else  if (singleChar.matches( "[0-9]" ) == false) {
				kT.consume();	
				return;
			}
			
		} else {
			// --- Integer values ---------------------
			if ( singleChar.matches( "[0-9]" ) == false ) {
				kT.consume();	
				return;
			}
			
		} // --- end if -------------------------------
		
	 }	
	
}
