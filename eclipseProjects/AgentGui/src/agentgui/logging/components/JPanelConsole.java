/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.logging.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * This JPanel is used for the console output of the local and remote nodes 
 * of the JADE platform.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelConsole extends JPanel {

	private static final long serialVersionUID = 8836132571457271033L;
	
	private final String lineSeparator = System.getProperty("line.separator");
	private AttributeSet attribute;
	private MutableAttributeSet black;
	private MutableAttributeSet red;
	
	private JEditorPane jEditorPaneOutput;
	private boolean localConsole = false;

	private JScrollPane jScrollPane;

	private Integer printLinesCounter = 0;
	private Integer printLinesMax = 400;
	private Integer printLinesCut = 100; 
	
	/**
	 * Default Constructor of this class
	 */
	public JPanelConsole() {
		super();
		this.initialize();
	}
	/**
	 * Constructor in case, that this Panel is used for the local output
	 * @param isLocalConsole
	 */
	public JPanelConsole(boolean isLocalConsole) {
		super();
		this.localConsole = isLocalConsole;
		this.initialize();
	}
	
	/**
	 * Initialises this
	 */
	private void initialize() {
		
		black = new SimpleAttributeSet();
	    StyleConstants.setForeground(black, Color.black);
	    StyleConstants.setFontFamily(black, "Courier");
	    StyleConstants.setFontSize(black, 11);
	    red = new SimpleAttributeSet();
	    StyleConstants.setForeground(red, Color.red);
	    StyleConstants.setFontFamily(red, "Courier");
	    StyleConstants.setFontSize(red, 11);
	    
	    this.setLayout(new BorderLayout());
		this.setSize(400,100);
		this.setPreferredSize(new Dimension(400, 100));
	    this.add(getJScrollPane(),BorderLayout.CENTER);
		
		if (localConsole==true) {
			// --- listen to local Out/Err-Output ---------
			SysOutBoard.setSysOutScanner(new SysOutScanner(this));
		}
		
	}
	
	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(400, 100));
			jScrollPane.setViewportView(this.getJEditorPaneOutput());
		}
		return jScrollPane;
	}
	/**
	 * @return the jeditorPane for the text output
	 */
	private JEditorPane getJEditorPaneOutput() {
		if (jEditorPaneOutput==null) {
			jEditorPaneOutput = new JEditorPane();
			jEditorPaneOutput.setContentType("text/html");
			jEditorPaneOutput.setFont(new Font("Dialog", Font.PLAIN, 14));
			jEditorPaneOutput.setBackground(new Color(255, 255, 255));
			jEditorPaneOutput.setPreferredSize(new Dimension(400, 100));
			jEditorPaneOutput.setEditable(false);
		}
		return jEditorPaneOutput;
	}
	
	/**
	 * This method can be used in order to append text to the current output
	 * @param text the text to print
	 * @param isError specifies if the text is an error
	 */
	public void appendText(String text, boolean isError) {
		
		// --- Work on the incoming text --------
		text = text.trim();
		if (text==null || text.equals("")) {
			return;
		} else if (text.endsWith(lineSeparator)==false) {
			text = text + lineSeparator;
		}
		// --- Set the right color attribute ----
		if (isError == false ) {
	    	attribute = black;
		} else {
			attribute = red;
		}		
		
		// --- Insert the text in the document --
		Document doc = this.getJEditorPaneOutput().getDocument();
		try {
			doc.insertString(doc.getLength(), text, attribute);
			printLinesCounter++;
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}

		// --- Set the maximum length of the text shown --- 
		if ( printLinesCounter > (printLinesMax+printLinesCut) ) {

			// --- Get document as String -------
			String docText = null;
			try {
				docText = doc.getText(0, doc.getLength());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			
			// --- Find the lines to remove -----
			int lineSeparatorFound = 0;
			int offset = 0;
			while(lineSeparatorFound < printLinesCut ) {
				
				int found = docText.indexOf(lineSeparator, offset);
				if (found == -1) {
					// --- nothing found ---
					break;
				} else {
					lineSeparatorFound++;
					offset = found+1;
				}
				
			}
			// --- Remove the lines -------------
			if (offset>0) {
				offset--;
				offset = offset + lineSeparator.length();
				try {
					doc.remove(0, offset);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				printLinesCounter = printLinesCounter-lineSeparatorFound;
			}
			
		}
		
        // --- Focus to the end ---------------------------
        this.getJEditorPaneOutput().setCaretPosition(doc.getLength());
		
	}
	/**
	 * This method can be used in order to append the console output from a remote container
	 * @param text the text to print out
	 */
	public void appendText(String text) {
		if (text.startsWith(PrintStreamListener.SystemOutput)) {
			String newText = text.substring(PrintStreamListener.SystemOutput.length()).trim();
			this.appendText(newText, false);	
			
		} else if (text.startsWith(PrintStreamListener.SystemError)) {
			String newText = text.substring(PrintStreamListener.SystemError.length()).trim();
			this.appendText(newText, true);
			
		} else {
			this.appendText(text, false);	
		}
	}

	/**
	 * Appends the complete Vector<String> to this console  
	 * @param lines2transfer
	 */
	public void appendText(Vector<String> lines2transfer) {
		for (int i = 0; i < lines2transfer.size(); i++) {
			this.appendText(lines2transfer.get(i));
		}
	}

	/**
	 * @param localConsole the localConsole to set
	 */
	public void setLocalConsole(boolean localConsole) {
		this.localConsole = localConsole;
	}
	/**
	 * @return the localConsole
	 */
	public boolean isLocalConsole() {
		return localConsole;
	}
	
	
}