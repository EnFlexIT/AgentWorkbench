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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import agentgui.core.application.Language;
import agentgui.core.common.ExceptionHandling;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;

/**
 * The Class CompressorStationEditorPanel handles the visualisation of a CompressorStation.
 */
public class CompressorStationEditorPanel extends OntologyClassEditorJPanel implements ActionListener {

	private static final long serialVersionUID = -1085905726626008322L;

	private CompStat compressorStationModel = null;  //  @jve:decl-index=0:
	private HashMap<String, JComponent> displayComponents = null;  //  @jve:decl-index=0:
	
	private JTabbedPane jTabbedPaneComponents = null;
	private JPanel jPanelGeneralInformation = null;  //  @jve:decl-index=0:visual-constraint="122,10"
	private JPanel jPanelGeneralWest = null;
	private JPanel jPanelGeneralEast = null;

	private JLabel jLabelHeader = null;
	private JLabel jLabelID = null;
	private JLabel jLabelAlias = null;
	private JLabel jLabelBuildingCosts = null;
	private JLabel jLabelUpgradeCosts = null;
	private JLabel jLabelDummy = null;
	
	private KeyAdapter myKeyAdapter = null;
	private JTextField jTextFieldError = null;
	private JTextField jTextFieldID = null;
	private JTextField jTextFieldAlias = null;
	private JTextField jTextFieldBuildingCosts = null;
	private JTextField jTextFieldUpgradeCosts = null;


	

	/**
	 * Instantiates a new compressor station editor panel.
	 *
	 * @param dynForm the current DynForm
	 * @param startArgIndex the start argument index
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
        this.setVisualisation();
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
		
		try {
			this.compressorStationModel.setID(this.getJTextFieldID().getText());
			this.compressorStationModel.setAlias(this.getJTextFieldAlias().getText());
			
			String buildingCosts = this.getJTextFieldBuildingCosts().getText();
			if (buildingCosts==null || buildingCosts.equals("")) {
				this.compressorStationModel.setBuildingCosts(0.0f);
				this.getJTextFieldBuildingCosts().setText("0.0");
			} else {
				this.compressorStationModel.setBuildingCosts(Float.parseFloat(buildingCosts));	
			}
			String upgradeCosts= this.getJTextFieldUpgradeCosts().getText();
			if (upgradeCosts==null || upgradeCosts.equals("")) {
				this.compressorStationModel.setUpgradeCosts(0.0f);
				this.getJTextFieldUpgradeCosts().setText("0.0");
			} else {
				this.compressorStationModel.setUpgradeCosts(Float.parseFloat(upgradeCosts));	
			}
			
			this.setErrorMessage(null);
			
		} catch (Exception ex) {
//			ex.printStackTrace();
			this.setErrorMessage(ExceptionHandling.getFirstTextLineOfException(ex));
		}
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
		
		if (this.compressorStationModel==null) return;
		
		this.getJTextFieldID().setText(this.compressorStationModel.getID());
		this.getJTextFieldAlias().setText(this.compressorStationModel.getAlias());
		this.getJTextFieldBuildingCosts().setText(((Float)this.compressorStationModel.getBuildingCosts()).toString());
		this.getJTextFieldUpgradeCosts().setText(((Float)this.compressorStationModel.getUpgradeCosts()).toString());
		
	}
	
	/**
	 * This method initializes this
	 */
	private void setVisualisation() {
        
        // --- General Information ------------------------
		JComponent generalInformation = this.getDisplayComponents().get(Language.translate("General", Language.EN));
		if (generalInformation==null) {
			generalInformation = getJPanelGeneralInformation();
			this.registerDisplay(Language.translate("General", Language.EN), getJPanelGeneralInformation());
		}
        this.setGeneralInformation();
        
        // --- Exit if there is no model available --------
        if (this.compressorStationModel==null) return;
        
    	// --- Compressor ---------------------------------
        // --- Turbo compressor -----------------
        List turboCompressor = this.compressorStationModel.getTurboCompressor();
        if (turboCompressor!=null) {
        	for (int i = 0; i < turboCompressor.size(); i++) {
        		TurboCompressor tc = (TurboCompressor) turboCompressor.get(i);
        		if (tc.isEmpty()==false) {
        			TurboCompressorDisplay tcDisplay = (TurboCompressorDisplay) this.getDisplayComponents().get(tc.getID());
            		if (tcDisplay==null) {
            			tcDisplay = new TurboCompressorDisplay();
            			this.registerDisplay(tc.getID(), tcDisplay);
            		}
            		tcDisplay.setTurboCompressor(tc);
        		}
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
	 * Returns all current display components.
	 * @return the displayComponents
	 */
	public HashMap<String, JComponent> getDisplayComponents() {
		if (displayComponents==null) {
			displayComponents = new HashMap<String, JComponent>();
		}
		return displayComponents;
	}
	/**
	 * Sets the display components.
	 * @param displayComponents the displayComponents to set
	 */
	public void setDisplayComponents(HashMap<String, JComponent> displayComponents) {
		this.displayComponents = displayComponents;
	}

	/**
	 * Adds and registers a display element for a part of the compressor station.
	 *
	 * @param displayName the display name
	 * @param displayComponent the display component
	 */
	private void registerDisplay(String displayName, JComponent displayComponent) {
		this.getDisplayComponents().put(displayName, displayComponent);
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
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.weightx = 0.5;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.weightx = 0.5;
			gridBagConstraints6.gridy = 1;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.BOTH;
			gridBagConstraints51.gridy = 3;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.gridwidth = 3;
			gridBagConstraints51.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints51.gridx = 0;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.weighty = 1.0;
			gridBagConstraints41.gridy = 2;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints31.gridy = 0;
			
			jLabelHeader = new JLabel();
			jLabelHeader.setText("Compressor Station");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText("");
			
			jPanelGeneralInformation = new JPanel();
			jPanelGeneralInformation.setLayout(new GridBagLayout());
			jPanelGeneralInformation.setSize(new Dimension(380, 200));
			jPanelGeneralInformation.add(jLabelHeader, gridBagConstraints31);
			jPanelGeneralInformation.add(jLabelDummy, gridBagConstraints41);
			jPanelGeneralInformation.add(getJTextFieldError(), gridBagConstraints51);
			jPanelGeneralInformation.add(getJPanelGeneralWest(), gridBagConstraints6);
			jPanelGeneralInformation.add(getJPanelGeneralEast(), gridBagConstraints7);
		}
		return jPanelGeneralInformation;
	}
	/**
	 * This method initializes jPanelGeneralWest	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGeneralWest() {
		if (jPanelGeneralWest == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 0.0;
			gridBagConstraints3.insets = new Insets(5, 5, 0, 0);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.insets = new Insets(5, 5, 0, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(5, 5, 0, 0);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.insets = new Insets(5, 5, 0, 0);
			
			jLabelID = new JLabel();
			jLabelID.setText("ID");
			jLabelAlias = new JLabel();
			jLabelAlias.setText("Alias");
			
			jPanelGeneralWest = new JPanel();
			jPanelGeneralWest.setLayout(new GridBagLayout());
			jPanelGeneralWest.add(jLabelID, gridBagConstraints);
			jPanelGeneralWest.add(getJTextFieldID(), gridBagConstraints1);
			jPanelGeneralWest.add(jLabelAlias, gridBagConstraints2);
			jPanelGeneralWest.add(getJTextFieldAlias(), gridBagConstraints3);
		}
		return jPanelGeneralWest;
	}
	/**
	 * This method initializes jPanelGeneralEast	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGeneralEast() {
		if (jPanelGeneralEast == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 0.0;
			gridBagConstraints9.insets = new Insets(5, 5, 0, 5);
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = -1;
			gridBagConstraints5.gridy = -1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(5, 5, 0, 5);
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.gridy = -1;
			gridBagConstraints4.insets = new Insets(5, 10, 0, 0);
			
			jLabelBuildingCosts = new JLabel();
			jLabelBuildingCosts.setText(Language.translate("Building costs", Language.EN));
			
			jLabelUpgradeCosts = new JLabel();
			jLabelUpgradeCosts.setText(Language.translate("Upgrade costs", Language.EN));
			
			jPanelGeneralEast = new JPanel();
			jPanelGeneralEast.setLayout(new GridBagLayout());
			jPanelGeneralEast.add(jLabelBuildingCosts, gridBagConstraints4);
			jPanelGeneralEast.add(getJTextFieldBuildingCosts(), gridBagConstraints5);
			jPanelGeneralEast.add(jLabelUpgradeCosts, gridBagConstraints8);
			jPanelGeneralEast.add(getJTextFieldUpgradeCosts(), gridBagConstraints9);
		}
		return jPanelGeneralEast;
	}
	
	/**
	 * This method initializes jTextFieldID	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldID() {
		if (jTextFieldID == null) {
			jTextFieldID = new JTextField();
			jTextFieldID.setPreferredSize(new Dimension(80, 26));
			jTextFieldID.addActionListener(this);
			jTextFieldID.addKeyListener(this.getKeyAdapter4TextField());
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
			jTextFieldAlias.setPreferredSize(new Dimension(80, 26));
			jTextFieldAlias.addActionListener(this);
			jTextFieldAlias.addKeyListener(this.getKeyAdapter4TextField());
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
			jTextFieldBuildingCosts.setPreferredSize(new Dimension(60, 26));
			jTextFieldBuildingCosts.addActionListener(this);
			jTextFieldBuildingCosts.addKeyListener(this.getKeyAdapter4TextField());
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
			jTextFieldUpgradeCosts.setPreferredSize(new Dimension(60, 26));
			jTextFieldUpgradeCosts.addActionListener(this);
			jTextFieldUpgradeCosts.addKeyListener(this.getKeyAdapter4TextField());
		}
		return jTextFieldUpgradeCosts;
	}
	/**
	 * This method initializes jTextFieldError	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldError() {
		if (jTextFieldError == null) {
			jTextFieldError = new JTextField();
			jTextFieldError.setEditable(false);
			jTextFieldError.setBorder(BorderFactory.createEmptyBorder());
			jTextFieldError.setOpaque(true);
			jTextFieldError.setBackground(new Color(255, 255, 255, 0));
			jTextFieldError.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldError.setForeground(Color.red);
		}
		return jTextFieldError;
	}
	
	/**
	 * Sets the error message.
	 * @param text the new error message
	 */
	public void setErrorMessage(String text) {
		if (text==null) {
			this.getJTextFieldError().setText(null);
		} else {
			this.getJTextFieldError().setText(Language.translate("Error", Language.EN) + ": " + text);
		}
		this.getJTextFieldError().repaint();
		this.repaint();
	}
	
	/**
	 * Returns a key adapter for text fields.
	 * @return the key adapter
	 */
	private KeyAdapter getKeyAdapter4TextField() {
		if (myKeyAdapter==null) {
			myKeyAdapter = new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
//					setNewOntologyClassInstance();
				};
			};
		}
		return myKeyAdapter;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object actor = ae.getSource();
		if (actor==this.getJTextFieldID() || 
			actor==this.getJTextFieldAlias() || 
			actor==this.getJTextFieldBuildingCosts() || 
			actor==this.getJTextFieldUpgradeCosts() ) {
			// --- Set the current status to the DynForm -------
			
		}
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
