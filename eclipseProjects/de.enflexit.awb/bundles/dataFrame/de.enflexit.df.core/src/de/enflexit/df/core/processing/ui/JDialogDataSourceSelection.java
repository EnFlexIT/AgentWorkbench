package de.enflexit.df.core.processing.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.model.DataController;
import java.awt.BorderLayout;

/**
 * Simple dialog for selecting data source tables.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDialogDataSourceSelection extends JDialog implements ActionListener, CheckBoxListSelectionListener<AbstractDataSourceDTNO<?>>{

	private static final long serialVersionUID = -653322247106918023L;
	
	private JPanelDataSourceSelection jPanelSelectionList;
	
	private JPanelTransformationGraphEditor parentEditorPanel;
	
	private DataController dataControler;
	
	/**
	 * Instantiates a new j dialog select data sources.
	 * @param owner the owner
	 * @param graphController the graph controller
	 */
	public JDialogDataSourceSelection(Window owner, JPanelTransformationGraphEditor parentEditor, DataController dataControler) {
		super(owner);
		this.parentEditorPanel = parentEditor;
		this.dataControler = dataControler;
		initialize();
	}
	
	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		getContentPane().add(getJPanelSelectionList(), BorderLayout.CENTER);
		
		this.setTitle("Select data sources");
		this.setSize(200, 250);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private JPanelDataSourceSelection getJPanelSelectionList() {
		if (jPanelSelectionList == null) {
			jPanelSelectionList = new JPanelDataSourceSelection(dataControler);
			jPanelSelectionList.addCheckBoxListSelectionListener(this);
		}
		return jPanelSelectionList;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	}
	
	/**
	 * Gets the parent editor panel.
	 * @return the parent editor panel
	 */
	private JPanelTransformationGraphEditor getParentEditorPanel() {
		return parentEditorPanel;
	}
	

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.ui.CheckBoxListSelectionListener#selectionChanged(de.enflexit.df.core.processing.ui.CheckBoxListSelectionEvent)
	 */
	@Override
	public void selectionChanged(CheckBoxListSelectionEvent<AbstractDataSourceDTNO<?>> cblse) {
		AbstractDataSourceDTNO<?> affectedDataSourceDTNO = cblse.getItem();
		
		if (cblse.isSelected()==true) {
			this.getParentEditorPanel().addDataSourceGraphNode(affectedDataSourceDTNO);
		} else {
			this.getParentEditorPanel().removeDataSourceGraphNode(affectedDataSourceDTNO);
		}
	}
}
