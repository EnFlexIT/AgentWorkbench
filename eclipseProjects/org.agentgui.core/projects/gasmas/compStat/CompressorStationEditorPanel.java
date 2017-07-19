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

import gasmas.compStat.CompressorStationModel.Notification;
import gasmas.compStat.display.TurboCompressorDisplay;
import gasmas.ontology.CompStat;
import gasmas.ontology.GridComponent;
import gasmas.ontology.TurboCompressor;
import jade.util.leap.List;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import agentgui.core.application.Language;
import agentgui.core.common.ExceptionHandling;
import agentgui.core.common.KeyAdapter4Numbers;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;

/**
 * The Class CompressorStationEditorPanel handles the visualisation of a
 * CompressorStation.
 */
public class CompressorStationEditorPanel extends OntologyClassEditorJPanel implements ActionListener, Observer {

	private static final long serialVersionUID = -1085905726626008322L;

	private CompressorStationModel compressorStationModel = null; // @jve:decl-index=0:
	private HashMap<String, JComponent> displayComponents = null; // @jve:decl-index=0:

	private CompressorStationToolBar compressorStationToolBar = null;
	private JTabbedPane jTabbedPaneComponents = null;
	private JPanel jPanelGeneralInformation = null; // @jve:decl-index=0:visual-constraint="122,10"
	private JPanel jPanelGeneralWest = null;
	private JPanel jPanelGeneralEast = null;

	private JLabel jLabelHeader = null;
	private JLabel jLabelID = null;
	private JLabel jLabelAlias = null;
	private JLabel jLabelBuildingCosts = null;
	private JLabel jLabelUpgradeCosts = null;
	private JLabel jLabelDummy = null;

	private KeyAdapter4Numbers keyAdapter4Float = null;
	private KeyAdapter keyAdapter4Actions = null; // @jve:decl-index=0:
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

	/**
	 * Gets the compressor station model.
	 * @return the compressor station model
	 */
	public CompressorStationModel getCompressorStationModel() {
		if (this.compressorStationModel == null) {
			this.compressorStationModel = new CompressorStationModel();
			this.compressorStationModel.addObserver(this);
		}
		return this.compressorStationModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#
	 * setOntologyClassInstance(java.lang.Object)
	 */
	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.getCompressorStationModel().setCompStat((CompStat) objectInstance);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#
	 * getOntologyClassInstance()
	 */
	@Override
	public Object getOntologyClassInstance() {
		return this.getCompressorStationModel().getCompStat();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#
	 * getJToolBarUserFunctions()
	 */
	@Override
	public JToolBar getJToolBarUserFunctions() {
		if (compressorStationToolBar==null) {
			compressorStationToolBar = new CompressorStationToolBar(this);
		}
		return compressorStationToolBar;
	}

	/**
	 * Sets the general information.
	 */
	private void setGeneralInformationToForm() {

		CompStat compStat = this.getCompressorStationModel().getCompStat();

		this.getJTextFieldID().setText(compStat.getID());
		this.getJTextFieldAlias().setText(compStat.getAlias());
		this.getJTextFieldBuildingCosts().setText( ((Float) compStat.getBuildingCosts()).toString());
		this.getJTextFieldUpgradeCosts().setText( ((Float) compStat.getUpgradeCosts()).toString());

	}

	/**
	 * Sets the general information to model.
	 * @param component the new general information to model
	 */
	private void setGeneralInformationToModel(JComponent component) {

		this.setErrorMessage(null);
		
		CompStat compStat = this.getCompressorStationModel().getCompStat();
		if (component == this.getJTextFieldID()) {
			compStat.setID(this.getJTextFieldID().getText());

		} else if (component == this.getJTextFieldAlias()) {
			compStat.setAlias(this.getJTextFieldAlias().getText());

		} else if (component == this.getJTextFieldBuildingCosts()) {
			String costs = this.getJTextFieldBuildingCosts().getText();
			if (costs == null | costs.equals("")) {
				compStat.setBuildingCosts(0.0f);
			} else {
				try {
					compStat.setBuildingCosts(Float.parseFloat(costs));	
				} catch (Exception ex) {
					this.setErrorMessage(ExceptionHandling.getFirstTextLineOfException(ex));
				}
				
			}

		} else if (component == this.getJTextFieldUpgradeCosts()) {
			String costs = this.getJTextFieldUpgradeCosts().getText();
			if (costs == null | costs.equals("")) {
				compStat.setUpgradeCosts(0.0f);
			} else {
				try {
					compStat.setUpgradeCosts(Float.parseFloat(costs));
				} catch (Exception ex) {
					this.setErrorMessage(ExceptionHandling.getFirstTextLineOfException(ex));
				}
			}

		} else {
			System.out.println("Compressor Station: unknown edit");
		}

	}

	/**
	 * This method initializes this
	 */
	private void setVisualisation() {

		// --- Define a positive list of current components ---------
		Vector<String> currentComponentIDs = new Vector<String>();
		
		// --- Get the ontology model of the compressor station -----
		CompStat compStat = this.getCompressorStationModel().getCompStat();

		// --- Compressor -------------------------------------------
		// --- Turbo compressor ---------------------------
		List turboCompressor = compStat.getTurboCompressor();
		if (turboCompressor != null) {
			for (int i = 0; i < turboCompressor.size(); i++) {
				TurboCompressor tc = (TurboCompressor) turboCompressor.get(i);
				if (tc.isEmpty() == false) {
					TurboCompressorDisplay tcDisplay = (TurboCompressorDisplay) this.getDisplayComponents().get(tc.getID());
					if (tcDisplay == null) {
						tcDisplay = new TurboCompressorDisplay(this.getCompressorStationModel(), tc.getID());
						this.displayRegister(tc.getID(), tcDisplay);
					}
					tcDisplay.setTurboCompressor(tc);
					currentComponentIDs.add(tc.getID());
				}
			}
		}

		// --- Piston compressor --------------------------
		List pistonCompressor = compStat.getPistonCompressor();

		// --- Drives -----------------------------------------------
		List gasTurbines = compStat.getGasTurbines();

		List gasDrivenMotors = compStat.getGasDrivenMotors();

		List electricMotors = compStat.getElectricMotors();

		List steamTurbines = compStat.getSteamTurbines();

		// --- Cleanup the remaining tabs ---------------------------
		this.displayCleanup(currentComponentIDs);
		
	}

	/**
	 * Returns all current display components.
	 * @return the displayComponents
	 */
	public HashMap<String, JComponent> getDisplayComponents() {
		if (displayComponents == null) {
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
	 * @param displayName the display name
	 * @param displayComponent the display component
	 */
	private void displayRegister(String displayName, JComponent displayComponent) {
		this.getDisplayComponents().put(displayName, displayComponent);
		this.getJTabbedPaneComponents().addTab(displayName, displayComponent);
	}
	/**
	 * Cleanup the display tabs.
	 * @param currentComponentIDs the current component IDs
	 */
	private void displayCleanup(Vector<String> currentComponentIDs) {
		// --- Define a negative list with all tabs that are currently registered -------
		Vector<String> negativeList = new Vector<String>(this.getDisplayComponents().keySet());
		for (int i = 0; i < currentComponentIDs.size(); i++) {
			String idEntry = currentComponentIDs.get(i);
			// --- remove a current tab from the negative list ------
			negativeList.removeElement(idEntry);
		}
		// --- Delete the negative tabs now ---------------------------------------------
		for (int i = 0; i < negativeList.size(); i++) {
			this.displayUnregister(negativeList.get(i));
		}
	}
	/**
	 * Display unregister.
	 * @param displayNameOrID the display name or id
	 */
	private void displayUnregister(String displayNameOrID) {
		JComponent component = this.getDisplayComponents().get(displayNameOrID);
		this.getDisplayComponents().remove(displayNameOrID);
		this.getJTabbedPaneComponents().remove(component);		
	}
	/**
	 * Focus on a specified component display.
	 * @param idToFocus the id to focus
	 */
	public void displayFocus(String idToFocus) {
		JComponent component = this.getDisplayComponents().get(idToFocus);
		if (component!=null) {
			this.getJTabbedPaneComponents().setSelectedComponent(component);	
		}
	}
	
	/**
	 * Gets the ID of the focused displayed component.
	 * @return the ID of the focused displayed component
	 */
	public String getIDofFocusedDisplayedComponent() {
		
		String focusedDisplayComponent = null;
		Component componentSelected = this.getJTabbedPaneComponents().getSelectedComponent();
		if (componentSelected!=null) {
			// --- Run through the registered component tabs --------
			Set<String> keys = this.getDisplayComponents().keySet();
			for (String key : keys) {
				if (this.getDisplayComponents().get(key)==componentSelected) {
					focusedDisplayComponent = key;
					break;
				}
			}
		}
		return focusedDisplayComponent;
	}
	
	/**
	 * This method initializes jTabbedPaneComponents
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPaneComponents() {
		if (jTabbedPaneComponents == null) {
			jTabbedPaneComponents = new JTabbedPane();
			jTabbedPaneComponents.addTab(Language.translate("General", Language.EN), this.getJPanelGeneralInformation());
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
			jTextFieldBuildingCosts.addKeyListener(this.getKeyAdapter4Float());
			jTextFieldBuildingCosts.addKeyListener(this .getKeyAdapter4TextField());
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
			jTextFieldUpgradeCosts.addKeyListener(this.getKeyAdapter4Float());
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
		if (text == null) {
			this.getJTextFieldError().setText(null);
		} else {
			this.getJTextFieldError().setText(Language.translate("Error", Language.EN) + ": " + text);
		}
		this.getJTextFieldError().repaint();
		this.repaint();
	}

	/**
	 * Gets the key adapter for float values.
	 * @return the key adapter for float values
	 */
	public KeyAdapter4Numbers getKeyAdapter4Float() {
		if (keyAdapter4Float==null) {
			keyAdapter4Float = new KeyAdapter4Numbers(true);
		}
		return keyAdapter4Float;
	}
	
	/**
	 * Returns a key adapter for text fields.
	 * @return the key adapter
	 */
	private KeyAdapter getKeyAdapter4TextField() {
		if (keyAdapter4Actions == null) {
			keyAdapter4Actions = new KeyAdapter() {
				public void keyReleased(KeyEvent ke) {
					setGeneralInformationToModel((JComponent) ke.getSource());
				};
			};
		}
		return keyAdapter4Actions;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		this.setGeneralInformationToModel((JComponent) ae.getSource());
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {

		if (object==CompressorStationModel.UPDATE.CompressorStation) {
			this.setGeneralInformationToForm();
			this.setVisualisation();
			
		} else if (object instanceof Notification) {
			// --- The Object is an Notification of the CompressorStationModel ----------
			Notification compStatNotification = (Notification) object;
			switch (compStatNotification.getReason()) {
			case TurboCompressorAdd:
			case PistonCompressorAdd:
			case GasTurbineAdd:
			case GasDrivenMotorAdd:
			case ElectricMotorAdd:
			case SteamTurbineAdd:
				this.setVisualisation();
				GridComponent component = (GridComponent) compStatNotification.getInfoObject();
				if (component!=null) {
					this.displayFocus(component.getID());
				}
				break;

			case DeleteComponent:
				this.setVisualisation();
				break;
			default:
				break;
			}
			
		}

	}

} // @jve:decl-index=0:visual-constraint="10,10"
