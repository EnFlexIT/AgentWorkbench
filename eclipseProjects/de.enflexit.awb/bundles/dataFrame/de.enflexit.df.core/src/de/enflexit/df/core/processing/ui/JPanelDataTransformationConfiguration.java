package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import de.enflexit.common.ServiceFinder;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

/**
 * This panel provides a UI to configure data transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelDataTransformationConfiguration extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -2310318384700465180L;
	
	private JLabel jLabelSelectTransformation;
	private JComboBox<DataTransformationService> jComboBoxSelectTransformation;
	private DefaultComboBoxModel<DataTransformationService> dataTransformationComboBoxModel;
	private JLabel jLabelInputTables;
	private JLabel jLabelParameters;
	private JScrollPane jScrollPaneTablesList;
	private JScrollPane jScrollPaneParametersTable;
	private JTable jTableParameters;
	
	private JList<DataTableNode> tableNodesList;
	
	private CheckBoxList<DataTableNode> tableNodesCheckBoxList;
	private RadioButtonList<DataTableNode> tableNodesRadioButtonList;
	private DefaultListModel<DataTableNode> tableNodesListModel;
	
	private JPanelTransformationGraphEditor parentEditorPanel;
	
	/**
	 * Instantiates a new j panel configure data transformation.
	 */
	public JPanelDataTransformationConfiguration() {
		super();
	}

	/**
	 * Instantiates a new j panel configure data transformation.
	 * @param parentEditorPanel the editor panel
	 */
	public JPanelDataTransformationConfiguration(JPanelTransformationGraphEditor parentEditorPanel) {
		this.parentEditorPanel = parentEditorPanel;
		initialize();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelSelectTransformation = new GridBagConstraints();
		gbc_jLabelSelectTransformation.anchor = GridBagConstraints.WEST;
		gbc_jLabelSelectTransformation.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelSelectTransformation.gridx = 0;
		gbc_jLabelSelectTransformation.gridy = 0;
		add(getJLabelSelectTransformation(), gbc_jLabelSelectTransformation);
		GridBagConstraints gbc_jComboBoxSelectTransformation = new GridBagConstraints();
		gbc_jComboBoxSelectTransformation.insets = new Insets(5, 5, 5, 10);
		gbc_jComboBoxSelectTransformation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSelectTransformation.gridx = 1;
		gbc_jComboBoxSelectTransformation.gridy = 0;
		add(getJComboBoxSelectTransformation(), gbc_jComboBoxSelectTransformation);
		GridBagConstraints gbc_jLabelInputTables = new GridBagConstraints();
		gbc_jLabelInputTables.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelInputTables.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelInputTables.gridx = 0;
		gbc_jLabelInputTables.gridy = 1;
		add(getJLabelInputTables(), gbc_jLabelInputTables);
		GridBagConstraints gbc_jScrollPaneTablesList = new GridBagConstraints();
		gbc_jScrollPaneTablesList.insets = new Insets(5, 5, 5, 10);
		gbc_jScrollPaneTablesList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneTablesList.gridx = 1;
		gbc_jScrollPaneTablesList.gridy = 1;
		add(getJScrollPaneTablesList(), gbc_jScrollPaneTablesList);
		GridBagConstraints gbc_jLabelParameters = new GridBagConstraints();
		gbc_jLabelParameters.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelParameters.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelParameters.gridx = 0;
		gbc_jLabelParameters.gridy = 2;
		add(getJLabelParameters(), gbc_jLabelParameters);
		GridBagConstraints gbc_jScrollPaneParametersTable = new GridBagConstraints();
		gbc_jScrollPaneParametersTable.insets = new Insets(5, 5, 0, 10);
		gbc_jScrollPaneParametersTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneParametersTable.gridx = 1;
		gbc_jScrollPaneParametersTable.gridy = 2;
		add(getJScrollPaneParametersTable(), gbc_jScrollPaneParametersTable);
	}

	private JLabel getJLabelSelectTransformation() {
		if (jLabelSelectTransformation == null) {
			jLabelSelectTransformation = new JLabel("Select Transformation");
			jLabelSelectTransformation.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSelectTransformation;
	}
	private JComboBox<DataTransformationService> getJComboBoxSelectTransformation() {
		if (jComboBoxSelectTransformation == null) {
			jComboBoxSelectTransformation = new JComboBox<DataTransformationService>();
			jComboBoxSelectTransformation.setModel(this.getDataTransformationComboBoxModel());
			jComboBoxSelectTransformation.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxSelectTransformation.setRenderer(new DataTransformationListCellRenderer());
			jComboBoxSelectTransformation.addActionListener(this);
		}
		return jComboBoxSelectTransformation;
	}
	private DefaultComboBoxModel<DataTransformationService> getDataTransformationComboBoxModel() {
		if (dataTransformationComboBoxModel==null) {
			dataTransformationComboBoxModel = new DefaultComboBoxModel<DataTransformationService>();
			dataTransformationComboBoxModel.addElement(null);
			List<DataTransformationService> transformationServices = ServiceFinder.findServices(DataTransformationService.class);
			dataTransformationComboBoxModel.addAll(transformationServices);
		}
		return dataTransformationComboBoxModel;
	}
	private JLabel getJLabelInputTables() {
		if (jLabelInputTables == null) {
			jLabelInputTables = new JLabel("Select Input Table(s)");
			jLabelInputTables.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelInputTables;
	}
	private JLabel getJLabelParameters() {
		if (jLabelParameters == null) {
			jLabelParameters = new JLabel("Specify Parameters");
			jLabelParameters.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelParameters;
	}
	private JScrollPane getJScrollPaneTablesList() {
		if (jScrollPaneTablesList == null) {
			jScrollPaneTablesList = new JScrollPane();
			jScrollPaneTablesList.setViewportView(this.getTableNodesList());
		}
		return jScrollPaneTablesList;
	}
	
	/**
	 * Gets the table nodes list, which is either a CheckBoxList or a RadioButtonList, depending on the current transformation.
	 * @return the table nodes list
	 */
	private JList<DataTableNode> getTableNodesList() {
		if (tableNodesList==null) {
			DataTransformationService currentTransformation = (DataTransformationService) this.getJComboBoxSelectTransformation().getSelectedItem();
			if (currentTransformation==null || currentTransformation.getInputType()==InputType.MULTI_TABLE) {
				// --- Checkbox list for multiple selection ---------
				tableNodesList = this.getTableNodesCheckBoxList();
			} else {
				// --- Radio buttons list for single selection ------
				tableNodesList = this.getTableNodesRadioButtonList();
			}
		}
		tableNodesList.setEnabled(this.isTransformationSelected());
		return tableNodesList;
	}
	
	private CheckBoxList<DataTableNode> getTableNodesCheckBoxList() {
		if (tableNodesCheckBoxList==null) {
			tableNodesCheckBoxList = new CheckBoxList<DataTableNode>(this.getTableNodesListModel());
		}
		return tableNodesCheckBoxList;
	}
	private RadioButtonList<DataTableNode> getTableNodesRadioButtonList() {
		if (tableNodesRadioButtonList==null) {
			tableNodesRadioButtonList = new RadioButtonList<DataTableNode>(this.getTableNodesListModel());
		}
		return tableNodesRadioButtonList;
	}
	
	/**
	 * Gets the tables list model.
	 * @return the tables list model
	 */
	private DefaultListModel<DataTableNode> getTableNodesListModel(){
		
		if (tableNodesListModel==null) {
			tableNodesListModel = new DefaultListModel<DataTableNode>();
			
			for (DataTableNode node : this.parentEditorPanel.getGraphController().getTransformationGraph().getVertices()) {
				tableNodesListModel.addElement(node);
			}
		}
		return tableNodesListModel;
	}
	
	private JScrollPane getJScrollPaneParametersTable() {
		if (jScrollPaneParametersTable == null) {
			jScrollPaneParametersTable = new JScrollPane();
			jScrollPaneParametersTable.setViewportView(getJTableParameters());
		}
		return jScrollPaneParametersTable;
	}
	private JTable getJTableParameters() {
		if (jTableParameters == null) {
			jTableParameters = new JTable();
		}
		return jTableParameters;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJComboBoxSelectTransformation()) {
			DataTransformationService selectedTransformation = (DataTransformationService) this.getJComboBoxSelectTransformation().getSelectedItem();
			if (selectedTransformation!=null) {
				if (	selectedTransformation.getInputType()==InputType.MULTI_TABLE && this.getTableNodesList() instanceof RadioButtonList<DataTableNode> 
				|| selectedTransformation.getInputType()==InputType.SINGLE_TABLE && this.getTableNodesList() instanceof CheckBoxList<DataTableNode>	) {
				this.tableNodesList = null;
				this.getJScrollPaneTablesList().setViewportView(this.getTableNodesList());
				}
			// --- Re-initialize the table nodes list if it is the wrong type for the current transformation
			}
		}
	}
	
	/**
	 * Checks if a transformation is selected.
	 * @return true, if is transformation selected
	 */
	private boolean isTransformationSelected() {
		return this.getJComboBoxSelectTransformation().getSelectedItem()!=null;
	}
	
	/**
	 * Custom {@link ListCellRenderer} for displaying data transformations and a null option
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class DataTransformationListCellRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = -1262316751154322416L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object selectedItem, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, selectedItem, index, isSelected, cellHasFocus);
			
			if (selectedItem==null) {
				this.setText("--- Please Select ---");
			} else {
				DataTransformationService transformationService = (DataTransformationService) selectedItem;
				this.setText(transformationService.getName());
			}
			
			return this;
		}
	}
	
	/**
	 * Gets the selected data transformation.
	 * @return the selected data transformation
	 */
	public DataTransformationService getSelectedDataTransformation() {
		return (DataTransformationService) this.getJComboBoxSelectTransformation().getSelectedItem();
	}
	
	/**
	 * Gets the selected input nodes.
	 * @return the selected input nodes
	 */
	public ArrayList<DataTableNode> getSelectedInputNodes(){
		ArrayList<DataTableNode> selectedNodes = new ArrayList<DataTableNode>();
		
		int[] selectedIndices = this.getTableNodesList().getSelectedIndices();
		
		for(int i=0; i<selectedIndices.length; i++) {
			selectedNodes.add(this.getTableNodesList().getModel().getElementAt(selectedIndices[i]));
		}
		
		return selectedNodes;
	}
}
