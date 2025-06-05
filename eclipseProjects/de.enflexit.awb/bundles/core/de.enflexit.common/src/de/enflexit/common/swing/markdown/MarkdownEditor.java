package de.enflexit.common.swing.markdown;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 * The Class MarkdownEditor.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MarkdownEditor extends JPanel {

	private static final long serialVersionUID = -9112277424657199162L;

	private JScrollPane jScrollPane;
	private JTextArea jTextAreaEditor;
	
	private int fontSize = 12;
	private String fontFamily;
	
	/**
	 * Instantiates a new markdown editor.
	 */
	public MarkdownEditor() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(getJScrollPane());
	}
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(this.getJTextAreaEditor());
		}
		return jScrollPane;
	}
	private JTextArea getJTextAreaEditor() {
		if (jTextAreaEditor==null) {
			jTextAreaEditor = new JTextArea();
			jTextAreaEditor.setFont(new Font(this.getFontFamily(), Font.PLAIN, this.fontSize));
			jTextAreaEditor.setColumns(0);
			jTextAreaEditor.setLineWrap(false);
			jTextAreaEditor.setWrapStyleWord(true);
		}
		return jTextAreaEditor;
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
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isFocusOwner()
	 */
	@Override
	public boolean isFocusOwner() {
		return this.getJTextAreaEditor().isFocusOwner();
	}
	
	/**
	 * Returns the current markdown text.
	 * @return the text
	 */
	public String getText() {
		return this.getJTextAreaEditor().getText();
	}
	/**
	 * Sets the text.
	 * @param markDownText the new text
	 */
	public void setText(String markDownText) {
		this.getJTextAreaEditor().setText(markDownText);
		this.getJTextAreaEditor().setCaretPosition(0);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.getJTextAreaEditor().setFont(font);
	}
	
	/**
	 * Returns the document instance of the markdown editor.
	 * @return the document
	 */
	public Document getDocument() {
		return this.getJTextAreaEditor().getDocument();
	}
	
}
