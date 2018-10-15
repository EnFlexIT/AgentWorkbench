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
package de.enflexit.common.ontology.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.codec.binary.Base64;

import de.enflexit.common.Language;
import de.enflexit.common.images.ImageProvider;
import de.enflexit.common.images.ImageProvider.ImageFile;
import de.enflexit.common.ontology.AgentStartConfiguration;
import de.enflexit.common.ontology.OntologyVisualisationConfiguration;
import de.enflexit.common.ontology.OntologyVisualizationHelper;

/**
 * This class can be used to display a user interface thats allows to configure
 * an instance of an ontology in a different component.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyInstanceViewer extends JTabbedPane {

	private static final long serialVersionUID = 6748263753769300242L;
	
	private OntologyVisualizationHelper ontologyVisualisationHelper = null;
	private AgentStartConfiguration agentStartConfiguration = null;
	private String agentReference = null;
	private String[] ontologyClassReference = null;
	private boolean use4Agents =  false;

	private JScrollPane jScrollPaneDynForm = null;
	
	private DynTableJPanel dynTablePanel = null;
	private DynForm dynForm = null;
	private DynFormText dynFormText = null;
	
	private final String newLine = System.getProperty("line.separator");
	private final String separatorLine = "------------------------------------------";  
	
	private JPanel jPanelEnlarge = null;
	private JLabel jLabelTitleEnlarge = null;  //  @jve:decl-index=0:visual-constraint="12,220"

	private JPanel jContentPane = null;
	private JPanel jPanel4TouchDown = null;

	
	/**
	 * This is the constructor in case that nothing should be displayed (no form, no slots).
	 * @param ontologyVisualisationHelper the ontology visualisation helper
	 */
	public OntologyInstanceViewer(OntologyVisualizationHelper ontologyVisualisationHelper) {
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.agentStartConfiguration = null; 
		this.agentReference = null;
		this.ontologyClassReference = null;
		this.use4Agents = true;
		initialize();
	}
	/**
	 * Instantiates a new ontology instance viewer.
	 * @param ontologyVisualisationHelper the ontology visualisation helper
	 * @param agentStartConfiguration the agent configuration
	 * @param currentAgentReference the current agent reference
	 */
	public OntologyInstanceViewer(OntologyVisualizationHelper ontologyVisualisationHelper, AgentStartConfiguration agentStartConfiguration, String currentAgentReference) {
		super();
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.agentStartConfiguration = agentStartConfiguration;
		this.agentReference = currentAgentReference;
		this.ontologyClassReference = null;
		this.use4Agents = true;
		initialize();
	}
	/**
	 * Instantiates a new ontology instance viewer.
	 * @param ontologyVisualisationHelper the ontology visualisation helper
	 * @param currOntologyClassReference the current ontology class reference
	 */
	public OntologyInstanceViewer(OntologyVisualizationHelper ontologyVisualisationHelper, String[] currOntologyClassReference) {
		super();
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.agentStartConfiguration = null;
		this.agentReference = null;
		this.ontologyClassReference = currOntologyClassReference;
		this.use4Agents = false;
		initialize();
	}
	
	/**
	 * Returns the current OntologyVisualisationHelper.
	 * @return the OntologyVisualisationHelper
	 */
	public OntologyVisualizationHelper getOntologyVisualisationHelper() {
		return this.ontologyVisualisationHelper;
	}
	
	/**
	 * Can be used to specify a JToolBar where user functions of 
	 * a {@link OntologyClassEditorJPanel} can be placed.
	 *
	 * @param jToolBar4UserFunction the new JToolBar for user function
	 */
	public void setJToolBar4UserFunctions(JToolBar jToolBar4UserFunction) {
		this.getDynTableJPanel().setJToolBar4UserFunctions(jToolBar4UserFunction);
	}
	
	/**
	 * This method initialises this.
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(400, 200);
		this.setPreferredSize(new Dimension(150, 57));
		this.setTabPlacement(JTabbedPane.BOTTOM);
		
		// --- Add the Table Tab --------------------------
		this.addTab("Tabelle", getDynTableJPanel());
		
		// --- Add the Form Tab ---------------------------		
		this.addTab("Formular", getJScrollPaneDynForm());
		
		// --- Add the XML-Tab to the ---------------------
		this.addTab("XML", getDynFormText());

		// --- Add Enlarge-Tab ----------------------------
		if (this.getDynForm().isEmptyForm()==false) {
			this.addEnlargeTab();	
		}

		// --- Configure Translations ---------------------
		this.setTitleAt(0, "  " + Language.translate("Tabelle") + "   ");
		this.setTitleAt(1, "  " + Language.translate("Formular") + "  ");
		this.setTitleAt(2, "    " + Language.translate("XML") + "     ");
		
	}
	
	/**
	 * This method overrides the 'setSelectedIndex' method
	 * in order to catch the selection of the 'Enlarge view - Tab'.
	 *
	 * @param index the new selected index
	 */
	@Override
	public void setSelectedIndex(int index) {
	
		if (index==3) {
			this.setEnlargedView();
			
		} else {
			// --- Save current settings ------------------
			this.save();
			this.setXMLText();
			
			// --- Now do the focus change ----------------
			super.setSelectedIndex(index);
		}		
	}
	
	/**
	 * This method shows the enlarged dialog for the current ontology class instances
	 * in order to provide an easier access for the end user.
	 */
	private void setEnlargedView() {
		
		JDialog dialog = new JDialog(OntologyVisualisationConfiguration.getOwnerWindow());
		dialog.setPreferredSize(new Dimension(100, 200));
		dialog.setName("Ontology-Instance-Viewer");
		dialog.setTitle(OntologyVisualisationConfiguration.getApplicationTitle() +  ": Ontology-Instance-Viewer");
		dialog.setModal(true);
		dialog.setResizable(true);
		dialog.setContentPane(getJContentPane());
		
		// --- Size and center the dialog -----------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int diaWidth = (int) (screenSize.width*0.8);
		int diaHeight = (int) (screenSize.height * 0.9);

		int left = (screenSize.width - diaWidth) / 2;
		int top = (screenSize.height - diaHeight) / 2; 

		dialog.setSize(new Dimension(diaWidth, diaHeight));
	    dialog.setLocation(left, top);	
		
	    // --- Remind and remove THIS from the parent -----
	    this.getDynTableJPanel().setOntologyClassVisualsationVisible(null);
	    Container parentContainer = this.getParent();
	    parentContainer.remove(this);
	    parentContainer.validate();
	    parentContainer.repaint();
	    
	    // --- Add THIS to the dialog ---------------------
	    this.removeEnlargeTab();
	    jPanel4TouchDown.add(this, BorderLayout.CENTER);
	    dialog.setVisible(true);
		// - - - - - - - - - - - - - - - - - - - - - - - -  
	    // - - User-Interaction  - - - - - - - - - - - - - 
		// - - - - - - - - - - - - - - - - - - - - - - - -
	    this.addEnlargeTab();
		
	    // --- Add THIS again to the parent ---------------
	    this.getDynTableJPanel().setOntologyClassVisualsationVisible(null);
	    parentContainer.add(this);
	    parentContainer.validate();
	    parentContainer.repaint();
	    
	}
	
	/**
	 * This method can be called to either allow or disallow the enlargement
	 * of View to the OntologyInstanceViewer.
	 *
	 * @param allowEnlargement the new allow view enlargement
	 */
	public void setAllowViewEnlargement(boolean allowEnlargement) {
		if (this.getTabCount()==4 && allowEnlargement==false) {
			this.removeEnlargeTab();
		}
		if (this.getTabCount()<4 && allowEnlargement==true) {
			this.addEnlargeTab();
		}
	}
	
	/**
	 * This method create the full XML-Text, which can be displayed
	 * in the local 'jTextArea' for the XML-Representation.
	 */
	private void setXMLText() {
		
		String [] xmlConfig = this.dynForm.getOntoArgsXML(); 
		String newText = "";
		String argumentLine = "";
		int seperatorLength = separatorLine.length();
		
		if (xmlConfig!=null) {
			// --- Write down the XML-Array as String -----
			for (int i = 0; i < xmlConfig.length; i++) {
				
				argumentLine = "--- Argument " + (i+1) + " ";
				argumentLine+= separatorLine.substring(0, seperatorLength-argumentLine.length());
				
				String config = ""; 
				config += separatorLine + newLine;
				config += argumentLine;
				config += newLine + separatorLine + newLine;
				config += xmlConfig[i] + newLine;					
				newText += config;
			}
		}
		this.getDynFormText().setText(newText);
		
	}
	
	/**
	 * This method will separate the XML part from the text .
	 *
	 * @param currText the current text
	 * @return the XML parts
	 */	
	private String [] getXMLParts(String currText) {
		
		String workText = currText.trim();
		String xmlPart = null;
		String [] xmlParts = new String [this.dynForm.getOntoArgsXML().length];
		int xmlPartsCounter = 0;
		
		while (workText.equals("")==false) {

			int cut1 = workText.indexOf("<");
			int cut2 = workText.indexOf(">")+1;
			String tag1 = workText.substring(cut1, cut2);
			
			if (tag1.endsWith("/>")) {
				// --------------------------------------------------------------------------
				// --- e. g. <Phy_Size phy_height="1.3F" phy_width="123.4567F"/> ------------
				// --------------------------------------------------------------------------
				xmlPart = workText.substring(cut1, cut2);				
				
			} else {
				// --------------------------------------------------------------------------
				// --- e. g. separate start and end tags like: ------------------------------ 
				// --- <agent-identifier name="Hallo"> ...</agent-identifier> ---------------
				// --------------------------------------------------------------------------
				int space = tag1.indexOf(" ");
				String tagIdentifier = null;
				if (space==-1) {
					tagIdentifier = tag1.substring(1, tag1.length()-1);
					
				} else {
					tagIdentifier = tag1.substring(1,space);	
					
				}
				tagIdentifier = "</" + tagIdentifier + ">";
				cut2 = workText.indexOf(tagIdentifier, cut2);
				cut2 = workText.indexOf(">", cut2)+1;
				
				xmlPart = workText.substring(cut1, cut2);
				
			}
			//System.out.println(xmlPart);
			xmlParts[xmlPartsCounter] = xmlPart;
			xmlPartsCounter++;
			workText = workText.substring(cut2).trim();
			
		}
		return xmlParts;
	}
	
	/**
	 * This method initialises jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(20, 20, 5, 20);
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJPanel4TouchDown(), gridBagConstraints);
		}
		return jContentPane;
	}
	/**
	 * This method initialises jPanel4TouchDown.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4TouchDown() {
		if (jPanel4TouchDown == null) {
			jPanel4TouchDown = new JPanel();
			jPanel4TouchDown.setPreferredSize(new Dimension(20, 20));
			jPanel4TouchDown.setLayout(new BorderLayout());
		}
		return jPanel4TouchDown;
	}
	/**
	 * This method adds the Enlarge-View-Tab to THIS TabbedPane.
	 */
	private void addEnlargeTab() {
		this.addTab("Vergrößern", this.getJPanelEnlarge());
		this.setTabComponentAt(3, this.getJLabelTitleEnlarge());
		this.jLabelTitleEnlarge.setText("  " + Language.translate("Vergrößern ...") + "  ");
	}
	/**
	 * This method removes the Enlarge-View-Tab to THIS TabbedPane.
	 */
	private void removeEnlargeTab() {
		this.remove(jPanelEnlarge);
	}
	/**
	 * This method initialises dynTableJPanel.
	 * @return the DynTableJPanel
	 */
	private DynTableJPanel getDynTableJPanel() {
		if (dynTablePanel==null) {
			dynTablePanel = new DynTableJPanel(this.getDynForm());
		}
		return dynTablePanel;
	}
	/**
	 * Stop the cell editing in the DynTable.
	 */
	private void stopDynTableCellEditingAndSaveOntologyClassInstanceOfOntologyClassWidgetOrOntologyClassEditorJPanel() {
		this.getDynTableJPanel().stopDynTableCellEditing();
		this.getDynTableJPanel().setOntologyClassInstanceToDynForm();
	}
	
	/**
	 * This method initialises dynForm.
	 *
	 * @return the DynForm
	 * @see DynForm
	 */
	private DynForm getDynForm() {
		if (dynForm==null) {
			if (use4Agents==true) {
				dynForm = new DynForm(this.ontologyVisualisationHelper, this.agentStartConfiguration, this.agentReference);
			} else {
				dynForm = new DynForm(this.ontologyVisualisationHelper, ontologyClassReference);
			}
		}
		return dynForm;
	}
	/**
	 * This method initialises jScrollPaneDynForm.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneDynForm() {
		if (jScrollPaneDynForm == null) {
			jScrollPaneDynForm = new JScrollPane();
			jScrollPaneDynForm.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollPaneDynForm.setViewportView(this.getDynForm().getJPanelOntologyVisualization());
		}
		return jScrollPaneDynForm;
	}
	/**
	 * Returns the DynFormText.
	 * @return the DynFormText
	 */
	private DynFormText getDynFormText() {
		if (dynFormText==null) {
			dynFormText = new DynFormText(this.getDynForm());
		}
		return dynFormText;
	}
	/**
	 * This method initialises jPanelEnlarege.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelEnlarge() {
		if (jPanelEnlarge == null) {
			jPanelEnlarge = new JPanel();
			jPanelEnlarge.setLayout(new GridBagLayout());
		}
		return jPanelEnlarge;
	}
	/**
	 * This method initialises jLabelTitleEnlarge.
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLabelTitleEnlarge() {
		if (jLabelTitleEnlarge == null) {
			jLabelTitleEnlarge = new JLabel();
			jLabelTitleEnlarge.setText(" Vergrößern");
			jLabelTitleEnlarge.setIcon(ImageProvider.getImageIcon(ImageFile.MB_FullScreen_PNG));
		}
		return jLabelTitleEnlarge;
	}

	/**
	 * Returns the OntologyClassEditorJPanel for the specified index of the current data model.
	 *
	 * @param targetDataModelIndex the target data model index
	 * @return the ontology class editor j panel
	 */
	public Vector<OntologyClassEditorJPanel> getOntologyClassEditorJPanel(int targetDataModelIndex) {

		Vector<OntologyClassEditorJPanel> ontoVisPanel = new Vector<OntologyClassEditorJPanel>();

		if (this.getSelectedIndex()==0) {
			// --- An open visualization of the DynTable ------------
			OntologyClassEditorJPanel ocep = this.getDynTableJPanel().getOntologyClassEditorJPanel(targetDataModelIndex);
			if (ocep!=null) {
				ontoVisPanel.add(ocep);	
			}
		} else if (this.getSelectedIndex()==1) {
			// --- Visualization on the DynForm himself -------------
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.getDynForm().getObjectTree().getRoot();
			DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) rootNode.getChildAt(targetDataModelIndex);
			OntologyClassWidget widget = this.getDynForm().getOntologyClassWidget(targetNode);
			ontoVisPanel.add(widget);
		}
		
		if (ontoVisPanel.size()==0) ontoVisPanel=null;
		return ontoVisPanel;
	}
	
	/**
	 * This method saves the current configuration.
	 */
	public void save() {
		
		// --- Save configuration depending on the current view -----
		if (this.getSelectedIndex()==0) {
			// --- Table view -----------------------------
			this.stopDynTableCellEditingAndSaveOntologyClassInstanceOfOntologyClassWidgetOrOntologyClassEditorJPanel();
			this.getDynForm().save(true);
			
		} else if (this.getSelectedIndex()==1) {
			// --- Form view ------------------------------
			this.getDynForm().save(true);
			this.getDynTableJPanel().refreshTableModel();
			
		} else if (this.getSelectedIndex()==2) {
			// --- XML view -------------------------------
			String currConfigText = this.getDynFormText().getText();
			String [] currConfig = this.getXMLParts(currConfigText);
			this.setConfigurationXML(currConfig);

			this.getDynForm().save(false);
			this.getDynTableJPanel().refreshTableModel();
		}
		
	}
	
	/**
	 * Sets the configuration in XML.
	 * @param configurationXML the configurationXML to set
	 */
	public void setConfigurationXML(String[] configurationXML) {
		this.dynForm.setOntoArgsXML(configurationXML);
		if (this.getSelectedIndex()==1) {
			this.setXMLText();
		}
	}
	/**
	 * Returns the configuration in XML.
	 * @return the configurationXML
	 */
	public String[] getConfigurationXML() {
		return this.getDynForm().getOntoArgsXML();
	}
	
	/**
	 * Sets the configuration in XML in a Base64 decode form.
	 * @param configurationXML64 the new configuration xm l64
	 */
	public void setConfigurationXML64(String[] configurationXML64) {
		
		String[] configXML = new String[configurationXML64.length];
		for (int i = 0; i < configurationXML64.length; i++) {
			try {
				if (configurationXML64[i]==null || configurationXML64[i].equals("")) {
					configXML[i] = null;
				} else {
					configXML[i] = new String(Base64.decodeBase64(configurationXML64[i].getBytes()), "UTF8");	
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		this.getDynForm().setOntoArgsXML(configXML);
		this.getDynTableJPanel().refreshTableModel();
		if (this.getSelectedIndex()==1) {
			this.setXMLText();
		}
		
	}
	
	/**
	 * Returns the configuration as XML in a Base64 encoded form.
	 * @return the configurationXML as Base64-String
	 */
	public String[] getConfigurationXML64() {
		
		String[] configXML = this.getDynForm().getOntoArgsXML();
		String[] configXML64 = new String[configXML.length];
		
		for (int i = 0; i < configXML.length; i++) {
			configXML64[i] = configXML[i];
			try {
				if (configXML[i]==null) {
					configXML64[i] = null;
				} else  if (configXML[i].equals("")) {
					configXML64[i] = null;
				} else {
					configXML64[i] = new String(Base64.encodeBase64(configXML[i].getBytes("UTF8")));	
				}
				
			} catch (UnsupportedEncodingException uex) {
				uex.printStackTrace();
			}
		}
		return configXML64;
	}
	
	/**
	 * Sets the configuration instances of the ontology objects.
	 * @param configurationInstances the configurationInstances to set
	 */
	public void setConfigurationInstances(final Object[] configurationInstances) {
		this.getDynForm().setOntoArgsInstance(configurationInstances);
		this.getDynTableJPanel().refreshTableModel();
		if (this.getSelectedIndex()==1) {
			this.setXMLText();
		}	
	}
	
	/**
	 * Returns the configuration instances.
	 * @return the configurationInstances
	 */
	public Object[] getConfigurationInstances() {
		return this.getDynForm().getOntoArgsInstance();
	}

	/**
	 * Returns the configuration instances.
	 * @return the configurationInstances
	 */
	public Object[] getConfigurationInstancesCopy() {
		return this.getDynForm().getOntoArgsInstanceCopy();
	}

}
