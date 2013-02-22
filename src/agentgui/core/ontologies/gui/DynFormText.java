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
package agentgui.core.ontologies.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

/**
 * The Class DynFormText displays the XML documents of the current ontology instances.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynFormText extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4693640456081655952L;
	private static final String pathImage = Application.getGlobalInfo().PathImageIntern();
	
	private DynForm dynForm = null;
	
	private JScrollPane jScrollPaneTextVersion=null;
	private JTextArea jTextArea = null;
	
	private JPanel jPanelBottom = null;
	private JTextField jTextFieldSearch = null;
	private JButton jButtonSearchForward = null;
	private JButton jButtonSearchBackwards = null;
	private JLabel jLabelDummy = null;
	private JButton jButtonSearchDelete = null;

	/**
	 * Instantiates a new JTextArea with search functionalities for the XML representation of an ontology instance.
	 * @param dynForm the current DynForm
	 */
	public DynFormText(DynForm dynForm) {
		this.dynForm = dynForm;
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.gridy = 1;
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridwidth = 1;
		
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getJScrollPaneTextVersion(), gridBagConstraints);
		this.add(getJPanelBottom(), gridBagConstraints11);
	}

	/**
	 * Sets the XML text.
	 * @param newXMLText the new text
	 */
	public void setText(String newXMLText) {
		this.getJTextArea().setText(newXMLText);
		this.getJTextArea().setCaretPosition(0);
	}
	/**
	 * Gets the XML text.
	 * @return the text
	 */
	public String getText() {
		return this.getJTextArea().getText();
	}

	/**
	 * This method initialises jScrollPaneTextVersion.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneTextVersion() {
		if (jScrollPaneTextVersion == null) {
			jScrollPaneTextVersion = new JScrollPane();
			jScrollPaneTextVersion.setViewportView(getJTextArea());
		}
		return jScrollPaneTextVersion;
	}
	/**
	 * This method initialises jTextArea.
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setFont(new Font("Courier New", Font.PLAIN, 12));
			if (this.dynForm.isEmptyForm()==true){
				jTextArea.setEditable(false);
			}
		}
		return jTextArea;
	}

	/**
	 * This method initializes jPanelBottom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 4;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.weightx = 0.0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText("");
			
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.setBorder(BorderFactory.createEmptyBorder()); 
			jPanelBottom.add(getJTextFieldSearch(), gridBagConstraints1);
			jPanelBottom.add(getJButtonSearchBackwards(), gridBagConstraints3);
			jPanelBottom.add(getJButtonSearchForward(), gridBagConstraints2);
			jPanelBottom.add(jLabelDummy, gridBagConstraints4);
			jPanelBottom.add(getJButtonSearchDelete(), gridBagConstraints5);
		}
		return jPanelBottom;
	}
	/**
	 * This method initializes jTextFieldSearch	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setPreferredSize(new Dimension(180, 26));
			jTextFieldSearch.setToolTipText(Language.translate("Suche"));
			jTextFieldSearch.addActionListener(this);
		}
		return jTextFieldSearch;
	}
	/**
	 * This method initializes jButtonSearchForward	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSearchForward() {
		if (jButtonSearchForward == null) {
			jButtonSearchForward = new JButton();
			jButtonSearchForward.setPreferredSize(new Dimension(26, 26));
			jButtonSearchForward.setIcon(new ImageIcon(getClass().getResource(pathImage + "ArrowRight.png")));
			jButtonSearchForward.setToolTipText(Language.translate("Suche vorwärts"));
			jButtonSearchForward.addActionListener(this);
		}
		return jButtonSearchForward;
	}
	/**
	 * This method initializes jButtonSearchBackwards	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSearchBackwards() {
		if (jButtonSearchBackwards == null) {
			jButtonSearchBackwards = new JButton();
			jButtonSearchBackwards.setPreferredSize(new Dimension(26, 26));
			jButtonSearchBackwards.setIcon(new ImageIcon(getClass().getResource(pathImage + "ArrowLeft.png")));
			jButtonSearchBackwards.setToolTipText(Language.translate("Suche rückwärts"));
			jButtonSearchBackwards.addActionListener(this);
		}
		return jButtonSearchBackwards;
	}
	/**
	 * This method initializes jButtonSearchDelete	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSearchDelete() {
		if (jButtonSearchDelete == null) {
			jButtonSearchDelete = new JButton();
			jButtonSearchDelete.setPreferredSize(new Dimension(26, 26));
			jButtonSearchDelete.setIcon(new ImageIcon(getClass().getResource(pathImage + "Delete.png")));
			jButtonSearchDelete.setToolTipText(Language.translate("Suche löschen"));
			jButtonSearchDelete.addActionListener(this);
		}
		return jButtonSearchDelete;
	}
	private void searchText(String searchText, boolean backwardSearch) {
		
		boolean exit = false;
		if (searchText==null) exit=true;
		if (searchText.equals("")) exit=true;
		if (exit==true) {
			this.getJTextArea().getHighlighter().removeAllHighlights();
			return;
		}

		// --- Prepare text -----------------------------------------
		String xmlText = this.getJTextArea().getText().toLowerCase();
		searchText = searchText.toLowerCase();

		int currentCaretPosition = this.getJTextArea().getCaretPosition();
		int newCaretPosition = currentCaretPosition;
		
		// --- search complete text ---------------------------------
		Vector<Integer> searchPositionsFound = new Vector<Integer>();
		int searchFrom = 0;
		int foundPosition = 0;
		while(foundPosition>=0) {
			foundPosition = xmlText.indexOf(searchText, searchFrom);
			if (foundPosition>=0) {
				searchPositionsFound.add(foundPosition);
				searchFrom = foundPosition+1;
			}
		}
		// --- Something found ? ------------------------------------
		if (searchPositionsFound.size()==0) {
			this.getJTextArea().getHighlighter().removeAllHighlights();
			return;
		}
		
		// --- Now find the right index in the Vector ---------------
		int indexInSearchPositionsFound = -1;
		if (backwardSearch==false) {
			// --- forward search -------------------------
			for (int i = 0; i < searchPositionsFound.size(); i++) {
				if (currentCaretPosition < searchPositionsFound.get(i)) {
					indexInSearchPositionsFound = i;
					break;
				}
			}
			if (indexInSearchPositionsFound==-1) {
				newCaretPosition = searchPositionsFound.get(0);
			} else {
				newCaretPosition = searchPositionsFound.get(indexInSearchPositionsFound);	
			}
			
		} else {
			// --- backward search ------------------------
			for (int i = searchPositionsFound.size()-1; i>=0; i--) {
				if (currentCaretPosition > searchPositionsFound.get(i)) {
					indexInSearchPositionsFound = i;
					break;
				}
			}
			if (indexInSearchPositionsFound==-1) {
				newCaretPosition = searchPositionsFound.get(searchPositionsFound.size()-1);
			} else {
				newCaretPosition = searchPositionsFound.get(indexInSearchPositionsFound);	
			}			
		}
		
		// --- Set the new caret position and highlight ---
		try {
			this.getJTextArea().setCaretPosition(newCaretPosition);
			
			Highlighter hl = this.getJTextArea().getHighlighter();
			hl.removeAllHighlights();
			hl.addHighlight(newCaretPosition, newCaretPosition + searchText.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));
			
		} catch (BadLocationException blex) {
			blex.printStackTrace();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == this.getJTextFieldSearch()) {
			this.searchText(this.getJTextFieldSearch().getText(), false);
		} else if (ae.getSource() == this.getJButtonSearchForward()) {
			this.searchText(this.getJTextFieldSearch().getText(), false);
		} else if (ae.getSource() == this.getJButtonSearchBackwards()) {
			this.searchText(this.getJTextFieldSearch().getText(), true);
		} else if (ae.getSource() == this.getJButtonSearchDelete()) {
			this.getJTextFieldSearch().setText(null);
			this.getJTextArea().getHighlighter().removeAllHighlights();	
		}
		
	}

}
