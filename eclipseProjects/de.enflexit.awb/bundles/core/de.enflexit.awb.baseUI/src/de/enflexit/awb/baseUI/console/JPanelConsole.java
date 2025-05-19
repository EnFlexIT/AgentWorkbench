package de.enflexit.awb.baseUI.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;
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

import de.enflexit.awb.core.ui.AwbConsole;
import de.enflexit.awb.simulation.logging.PrintStreamListener;
import de.enflexit.awb.simulation.logging.SysOutBoard;
import de.enflexit.awb.simulation.logging.SysOutScanner;
import de.enflexit.common.swing.AwbThemeColor;


/**
 * This JPanel is used for the console output of the local and remote nodes 
 * of the JADE platform.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelConsole extends JPanel implements AwbConsole {

	private static final long serialVersionUID = 8836132571457271033L;
	
	private final String lineSeparator = System.getProperty("line.separator");
	
	private String fontFamily;// = "Consolas";//"Courier New";
	private final int fontSize      = 13;
	
	
	
	private AttributeSet attribute;
	private MutableAttributeSet black;
	private MutableAttributeSet red;
	
	private JEditorPane jEditorPaneOutput;
	private boolean localConsole;

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
	 * Constructor in case, that this Panel is used for the local output.
	 * @param isLocalConsole the indicator if this is a local console
	 */
	public JPanelConsole(boolean isLocalConsole) {
		super();
		this.setLocalConsole(isLocalConsole);
		this.initialize();
	}

	/**
	 * Returns the font family to be used for console print outs.
	 * @return the font family
	 */
	private String getFontFamily() {
		if (fontFamily==null) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			HashSet<String> ffNames = new HashSet<>(Arrays.asList(ge.getAvailableFontFamilyNames()));
			if (fontFamily==null && ffNames.contains("Consolas")==true) {
				fontFamily = "Consolas";	
			}
			if (fontFamily==null && ffNames.contains("Courier New")==true) {
				fontFamily = "Courier New";
			}
		}
		return fontFamily;
	}
	
	/**
	 * Initialises this
	 */
	private void initialize() {
		
		black = new SimpleAttributeSet();
	    StyleConstants.setForeground(black, AwbThemeColor.RegularText.getColor());
	    StyleConstants.setFontFamily(black, this.getFontFamily());
	    StyleConstants.setFontSize(black, this.fontSize);
	    red = new SimpleAttributeSet();
	    StyleConstants.setForeground(red, Color.red);
	    StyleConstants.setFontFamily(red, this.getFontFamily());
	    StyleConstants.setFontSize(red, this.fontSize);
	    
	    this.setLayout(new BorderLayout());
		this.setSize(400,100);
		this.setPreferredSize(new Dimension(400, 100));
	    this.add(getJScrollPane(),BorderLayout.CENTER);
		
		if (this.isLocalConsole()==true) {
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
			jEditorPaneOutput.setFont(new Font(this.getFontFamily(), Font.PLAIN, this.fontSize));
			jEditorPaneOutput.setPreferredSize(new Dimension(400, 100));
			jEditorPaneOutput.setEditable(false);
		}
		return jEditorPaneOutput;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsole#appendText(java.util.Vector)
	 */
	@Override
	public void appendText(Vector<String> lines2transfer) {
		for (int i = 0; i < lines2transfer.size(); i++) {
			this.appendText(lines2transfer.get(i));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsole#appendText(java.lang.String)
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
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsole#appendText(java.lang.String, boolean)
	 */
	@Override
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
	

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsole#setLocalConsole(boolean)
	 */
	@Override
	public void setLocalConsole(boolean localConsole) {
		this.localConsole = localConsole;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsole#isLocalConsole()
	 */
	@Override
	public boolean isLocalConsole() {
		return localConsole;
	}
	
}