package de.enflexit.df.core.processing.transformation.ui;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.ui.JPanelTransformationGraphEditor;

/**
 * This class provides a dialog to configure data transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDialogDataTransformationConfiguration extends JDialog implements ActionListener{

	private static final long serialVersionUID = -1462269913159910976L;
	
	private JPanelTransformationGraphEditor parentEditorPanel;
	private JPanelDataTransformationConfiguration jPanelDataTransformationConfiguration;

	/**
	 * Instantiates a new j dialog data transformation configuration.
	 * @param owner the owner
	 * @param parentEditorPanel the parent editor panel
	 */
	public JDialogDataTransformationConfiguration(Window owner, JPanelTransformationGraphEditor parentEditorPanel) {
		super(owner);
		this.parentEditorPanel = parentEditorPanel;
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		getContentPane().add(getJPanelDataTransformationConfiguration(), BorderLayout.CENTER);
		
		this.setTitle("Add data transformation");
		this.setSize(550, 450);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Gets the j panel data transforation configuration.
	 * @return the j panel data transforation configuration
	 */
	private JPanelDataTransformationConfiguration getJPanelDataTransformationConfiguration() {
		if (jPanelDataTransformationConfiguration == null) {
			jPanelDataTransformationConfiguration = new JPanelDataTransformationConfiguration(this.parentEditorPanel);
			jPanelDataTransformationConfiguration.getJComboBoxSelectTransformation().addActionListener(this);
			jPanelDataTransformationConfiguration.addActionListenerToButtons(this);
		}
		return jPanelDataTransformationConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJPanelDataTransformationConfiguration().getJButtonApply()) {
			
			//TODO move to the editor, use PropertyCHangeEvent, distinguish added and changed
			AbstractDataTransformation newTransformation = this.getJPanelDataTransformationConfiguration().getSelectedDataTransformation().getNewInstance();
			newTransformation.getInputNodes().addAll(this.getJPanelDataTransformationConfiguration().getSelectedInputNodes());
			newTransformation.setTransformationParameters(this.getJPanelDataTransformationConfiguration().getConfiguredTransformationParameters());
			
			this.parentEditorPanel.addNewDataTransformation(newTransformation);
			
			this.setVisible(false);
			this.dispose();
			
		} else if (ae.getSource()==this.getJPanelDataTransformationConfiguration().getJButtonCancel()) {

			this.setVisible(false);
			this.dispose();
		}
	}
}
