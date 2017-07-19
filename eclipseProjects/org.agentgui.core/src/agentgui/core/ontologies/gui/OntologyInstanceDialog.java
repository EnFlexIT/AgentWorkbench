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
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.ontologies.OntologyVisualisationHelper;
import agentgui.core.project.AgentStartConfiguration;

/**
 * This class can be used to display a user interface thats allows to configure
 * an instance of an ontology in a separate JDialog. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyInstanceDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private int ontologyInstanceViewerConstructor = 0;
	
	protected EnvironmentController environmentController = null;
	private OntologyVisualisationHelper ontologyVisualisationHelper = null;
	private AgentStartConfiguration agentStartConfiguration = null;
	private String agentReference = null;
	
	private String[] ontologyClassReference = null;
	private Object[] objectConfiguration = null;

	private OntologyInstanceViewer oiv = null;

	private JPanel jContentPane = null;
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	private JPanel jPanelBottom = null;

	private boolean cancelled = false;
	
	
	// --- Start of constructor section  ------------------
	/**
	 * Instantiates a new ontology instance dialog. In this special case an empty
	 * form will be created, because of missing references to special ontology classes.
	 *
	 * @param owner the owner
	 * @param environmentController the EnvironmentController
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 */
	public OntologyInstanceDialog(Frame owner, EnvironmentController environmentController, OntologyVisualisationHelper ontologyVisualisationHelper) {
		super(owner);
		this.environmentController = environmentController;
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.ontologyInstanceViewerConstructor = 0;
		this.initialize();
	}
	/**
	 * Instantiates a new ontology instance dialog. The dialog will be created depending
	 * on the start arguments configured for an agent.
	 *
	 * @param owner the owner
	 * @param environmentController the EnvironmentController
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 * @param agentStartConfiguration the agent configuration
	 * @param agentReference the agent reference
	 */
	public OntologyInstanceDialog(Frame owner, EnvironmentController environmentController, OntologyVisualisationHelper ontologyVisualisationHelper, AgentStartConfiguration agentStartConfiguration, String agentReference) {
		super(owner);
		this.environmentController = environmentController;
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.agentStartConfiguration = agentStartConfiguration;
		this.agentReference = agentReference;
		this.ontologyInstanceViewerConstructor = 1;
		this.initialize();
	}
	/**
	 * Instantiates a new ontology instance dialog. The dialog will be created depending
	 * on the class references. This classes have to be part of an ontology.
	 *
	 * @param owner the owner
	 * @param environmentController the EnvironmentController
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 * @param ontologyClassReference the ontology class reference
	 */
	public OntologyInstanceDialog(Frame owner, EnvironmentController environmentController, OntologyVisualisationHelper ontologyVisualisationHelper, String[] ontologyClassReference) {
		super(owner);
		this.environmentController = environmentController;
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.ontologyClassReference = ontologyClassReference;
		this.ontologyInstanceViewerConstructor = 2;
		this.initialize();
	}
	
	/**
	 * Instantiates a new ontology instance dialog. In this special case an empty
	 * form will be created, because of missing references to special ontology classes.
	 *
	 * @param owner the owner
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 */
	public OntologyInstanceDialog(Frame owner, OntologyVisualisationHelper ontologyVisualisationHelper) {
		super(owner);
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.ontologyInstanceViewerConstructor = 0;
		this.initialize();
	}
	/**
	 * Instantiates a new ontology instance dialog. The dialog will be created depending
	 * on the start arguments configured for an agent.
	 *
	 * @param owner the owner
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 * @param agentStartConfiguration the agent configuration
	 * @param agentReference the agent reference
	 */
	public OntologyInstanceDialog(Frame owner, OntologyVisualisationHelper ontologyVisualisationHelper, AgentStartConfiguration agentStartConfiguration, String agentReference) {
		super(owner);
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.agentStartConfiguration = agentStartConfiguration;
		this.agentReference = agentReference;
		this.ontologyInstanceViewerConstructor = 1;
		this.initialize();
	}
	/**
	 * Instantiates a new ontology instance dialog. The dialog will be created depending
	 * on the class references. This classes have to be part of an ontology.
	 *
	 * @param owner the owner
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 * @param ontologyClassReference the ontology class reference
	 */
	public OntologyInstanceDialog(Frame owner, OntologyVisualisationHelper ontologyVisualisationHelper, String[] ontologyClassReference) {
		super(owner);
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.ontologyClassReference = ontologyClassReference;
		this.ontologyInstanceViewerConstructor = 2;
		this.initialize();
	}
	// --- End of constructor section  --------------------
	
	/**
	 * This method initialises this dialog.
	 */
	private void initialize() {
		
		this.setSize(600, 500);
		this.setModal(true);
		this.setTitle(Language.translate("Ontologie-Klassen initialisieren"));
		this.setContentPane(getJContentPane());
		
//		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setCancelled(true);
				setVisible(false);
			}
		});
		// --- Set the IconImage ----------------------------------
		if (Application.getMainWindow()!=null) {
			this.setIconImage(Application.getMainWindow().getIconImage());
		}
		
		// --- Dialog zentrieren ------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
	    
	}

	/**
	 * Gets the ontology instance viewer.
	 *
	 * @return the ontology instance viewer
	 */
	public OntologyInstanceViewer getOntologyInstanceViewer() {
		if(oiv==null) {
			switch (this.ontologyInstanceViewerConstructor) {
			case 1:
				oiv = new OntologyInstanceViewer(this.environmentController, this.ontologyVisualisationHelper, this.agentStartConfiguration, this.agentReference);
				break;
			case 2:
				oiv = new OntologyInstanceViewer(this.environmentController, this.ontologyVisualisationHelper, this.ontologyClassReference);
				break;
			default:
				oiv = new OntologyInstanceViewer(this.environmentController, this.ontologyVisualisationHelper);
				break;
			}
		}
		oiv.setAllowViewEnlargement(false);
		return oiv;
	}
	
	/**
	 * Sets the cancelled.
	 * @param canceled the new cancelled
	 */
	public void setCancelled(boolean canceled) {
		this.cancelled = canceled;
	}
	/**
	 * Checks if is cancelled.
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Sets the object configuration.
	 * @param objectConfiguration the objectConfiguration to set
	 */
	public void setObjectConfiguration(Object[] objectConfiguration) {
		this.objectConfiguration = objectConfiguration;
	}
	/**
	 * Gets the object configuration.
	 * @return the objectConfiguration
	 */
	public Object[] getObjectConfiguration() {
		return objectConfiguration;
	}
	
	/**
	 * This method initialises jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(10, 10, 20, 10);
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(10, 10, 0, 10);
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(this.getOntologyInstanceViewer(), gridBagConstraints);
			jContentPane.add(getJPanelBottom(), gridBagConstraints3);
		}
		return jContentPane;
	}
	
	/**
	 * This method initialises jButtonOK.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setPreferredSize(new Dimension(120, 26));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setText("OK");
			jButtonOK.setText(Language.translate(jButtonOK.getText()));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	
	/**
	 * This method initialises jButtonCancel.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setPreferredSize(new Dimension(120, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setText("Abbruch");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText()));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/**
	 * This method initialises jPanelBottom.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.insets = new Insets(10, 50, 10, 0);
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.gridy = -1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.insets = new Insets(10, 0, 10, 50);
			gridBagConstraints1.gridy = -1;
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelBottom.add(getJButtonOK(), gridBagConstraints1);
			jPanelBottom.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelBottom;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object trigger = ae.getSource();
		if (trigger==jButtonOK) {
			this.getOntologyInstanceViewer().save();
			this.setObjectConfiguration(this.getOntologyInstanceViewer().getConfigurationInstances());
			this.setCancelled(false);
			this.setVisible(false);
			
		} else if (trigger==jButtonCancel) {
			this.setCancelled(true);
			this.setVisible(false);
			
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
