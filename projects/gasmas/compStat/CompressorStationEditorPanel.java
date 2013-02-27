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
package gasmas.compStat;

import gasmas.compStat.display.TurboCompressorDisplay;
import gasmas.ontology.CompStat;
import gasmas.ontology.TurboCompressor;
import jade.util.leap.List;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import agentgui.core.application.Language;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;

public class CompressorStationEditorPanel extends OntologyClassEditorJPanel implements ActionListener {

	private static final long serialVersionUID = -1085905726626008322L;

	private CompStat compressorStationModel = null;  //  @jve:decl-index=0:

	private JTabbedPane jTabbedPaneComponents = null;
	private HashMap<String, JComponent> displayComponents = new HashMap<String, JComponent>();  //  @jve:decl-index=0:

	private JPanel jPanelGeneralInformation = null;  //  @jve:decl-index=0:visual-constraint="122,10"

	private JLabel jLabelID = null;
	private JLabel jLabelAlias = null;
	private JLabel jLabelBuildingCosts = null;
	private JLabel jLabelUpgradeCosts = null;
	
	private JTextField jTextFieldID = null;
	private JTextField jTextFieldAlias = null;
	private JTextField jTextFieldBuildingCosts = null;
	private JTextField jTextFieldUpgradeCosts = null;

	/**
	 * Instantiates a new compressor station editor panel.
	 *
	 * @param dynForm the dyn form
	 * @param startArgIndex the start arg index
	 */
	public CompressorStationEditorPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		this.initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		// --- Add the basic visualisation component ------
		this.setLayout(new BorderLayout());
        this.setSize(new Dimension(400, 240));
        this.add(getJTabbedPaneComponents(), BorderLayout.CENTER);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#setOntologyClassInstance(java.lang.Object)
	 */
	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.compressorStationModel = (CompStat) objectInstance;
		this.setVisualisation();
	}
	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#getOntologyClassInstance()
	 */
	@Override
	public Object getOntologyClassInstance() {
		return this.compressorStationModel;
	}
	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#getJToolBarUserFunctions()
	 */
	@Override
	public JToolBar getJToolBarUserFunctions() {
		return null;
	}
	
	/**
	 * Sets the general information.
	 */
	private void setGeneralInformation() {
		
	}
	
	/**
	 * This method initializes this
	 */
	private void setVisualisation() {
        
        // --- General Information ------------------------
		JComponent generalInformation = this.displayComponents.get(Language.translate("General", Language.EN));
		if (generalInformation==null) {
			generalInformation = new TurboCompressorDisplay();
			this.registerDisplay(Language.translate("General", Language.EN), getJPanelGeneralInformation());
		}
        this.setGeneralInformation();
        
    	// --- Compressor ---------------------------------
        // --- Turbo compressor -----------------
        List turboCompressor = this.compressorStationModel.getTurboCompressor();
        if (turboCompressor!=null) {
        	for (int i = 0; i < turboCompressor.size(); i++) {
        		TurboCompressor tc = (TurboCompressor) turboCompressor.get(i);
        		TurboCompressorDisplay tcDisplay = (TurboCompressorDisplay) this.displayComponents.get(tc.getID());
        		if (tcDisplay==null) {
        			tcDisplay = new TurboCompressorDisplay();
        			this.registerDisplay(tc.getID(), tcDisplay);
        		}
    			tcDisplay.setTurboCompressor(tc);
    		}
        }
        
        // --- Piston compressor ----------------
        List pistonCompressor = this.compressorStationModel.getPistonCompressor();
        
        // --- Drives -------------------------------------        
        List gasTurbines = this.compressorStationModel.getGasTurbines();

        
        List gasDrivenMotors = this.compressorStationModel.getGasDrivenMotors();
        
        
        List electricMotors = this.compressorStationModel.getElectricMotors();
        
        
        List steamTurbines = this.compressorStationModel.getSteamTurbines();
        
	}
	
	/**
	 * Adds and registers a display element for a part of the compressor station.
	 *
	 * @param displayName the display name
	 * @param displayComponent the display component
	 */
	private void registerDisplay(String displayName, JComponent displayComponent) {
		this.displayComponents.put(displayName, displayComponent);
		this.getJTabbedPaneComponents().addTab(displayName, displayComponent);
	}
	
	/**
	 * This method initializes jTabbedPaneComponents	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneComponents() {
		if (jTabbedPaneComponents == null) {
			jTabbedPaneComponents = new JTabbedPane();

		}
		return jTabbedPaneComponents;
	}

	/**
	 * This method initializes jPanelGeneralInformation	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGeneralInformation() {
		if (jPanelGeneralInformation == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints21.gridx = 3;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints5.gridx = 3;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			
			jLabelID = new JLabel();
			jLabelID.setText("ID");
			jLabelAlias = new JLabel();
			jLabelAlias.setText("Alias");
			jLabelBuildingCosts = new JLabel();
			jLabelBuildingCosts.setText(Language.translate("Building costs", Language.EN));
			jLabelUpgradeCosts = new JLabel();
			jLabelUpgradeCosts.setText(Language.translate("Upgrade costs", Language.EN));
			
			jPanelGeneralInformation = new JPanel();
			jPanelGeneralInformation.setLayout(new GridBagLayout());
			jPanelGeneralInformation.setSize(new Dimension(200, 200));
			jPanelGeneralInformation.add(jLabelID, gridBagConstraints);
			jPanelGeneralInformation.add(getJTextFieldID(), gridBagConstraints1);
			jPanelGeneralInformation.add(jLabelAlias, gridBagConstraints2);
			jPanelGeneralInformation.add(getJTextFieldAlias(), gridBagConstraints3);
			jPanelGeneralInformation.add(jLabelBuildingCosts, gridBagConstraints4);
			jPanelGeneralInformation.add(getJTextFieldBuildingCosts(), gridBagConstraints5);
			jPanelGeneralInformation.add(jLabelUpgradeCosts, gridBagConstraints11);
			jPanelGeneralInformation.add(getJTextFieldUpgradeCosts(), gridBagConstraints21);
		}
		return jPanelGeneralInformation;
	}

	/**
	 * This method initializes jTextFieldID	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldID() {
		if (jTextFieldID == null) {
			jTextFieldID = new JTextField();
		}
		return jTextFieldID;
	}
	/**
	 * This method initializes jTextFieldAlias	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldAlias() {
		if (jTextFieldAlias == null) {
			jTextFieldAlias = new JTextField();
		}
		return jTextFieldAlias;
	}
	/**
	 * This method initializes jTextFieldBuildingCosts	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldBuildingCosts() {
		if (jTextFieldBuildingCosts == null) {
			jTextFieldBuildingCosts = new JTextField();
		}
		return jTextFieldBuildingCosts;
	}
	/**
	 * This method initializes jTextFieldUpgradeCosts	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldUpgradeCosts() {
		if (jTextFieldUpgradeCosts == null) {
			jTextFieldUpgradeCosts = new JTextField();
		}
		return jTextFieldUpgradeCosts;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		super.update(observable, object);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
