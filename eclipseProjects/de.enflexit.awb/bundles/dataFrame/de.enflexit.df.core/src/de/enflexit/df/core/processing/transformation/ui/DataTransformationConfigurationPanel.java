package de.enflexit.df.core.processing.transformation.ui;

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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import de.enflexit.common.ServiceFinder;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.swing.CheckBoxList;
import de.enflexit.common.swing.CheckBoxListSelectionEvent;
import de.enflexit.common.swing.CheckBoxListSelectionListener;
import de.enflexit.common.swing.RadioButtonList;
import de.enflexit.common.swing.RadioButtonListSelectionEvent;
import de.enflexit.common.swing.RadioButtonListSelectionListener;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformation.DataTransformationServiceSelectColumns;
import de.enflexit.df.core.processing.transformation.filter.DataTransformationServiceFilterRows;
import de.enflexit.df.core.processing.transformation.filter.ui.TableSawRowFilterConfigurationPanel;
import de.enflexit.df.core.processing.transformation.join.DataTransformationServiceJoinTables;
import de.enflexit.df.core.processing.transformation.join.ui.JoinTablesConfigurationPanel;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import de.enflexit.df.core.processing.ui.TransformationGraphEditorPanel;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ListCellRenderer;
import javax.swing.JButton;

/**
 * This panel provides a UI to configure data transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationConfigurationPanel extends JPanel implements ActionListener, CheckBoxListSelectionListener<DataTableNode>, RadioButtonListSelectionListener<DataTableNode> {
	
	private static final long serialVersionUID = -2310318384700465180L;
	
	// --- Events for notifying the parameters panel about selection changes ------------
	public static final String EVENT_ID_TABLE_SELECTED = "tableSelected";
	public static final String EVENT_ID_TABLE_ADDED = "tableAdded";
	public static final String EVENT_ID_TABLE_REMOVED = "tableRemoved";
	
	// --- Events for notifying about new or modified transformations -------------------
	public static final String EVENT_ID_TRANSFORMATION_CREATED = "transformationCreated";
	public static final String EVENT_ID_TRANSFORMATION_MODIFIED = "transformationModified";
	public static final String EVENT_ID_TRANSFORMATION_CANCELED = "transformaitonCanceled";
	
	private JLabel jLabelSelectTransformation;
	private JComboBox<DataTransformationService> jComboBoxSelectTransformation;
	private DefaultComboBoxModel<DataTransformationService> dataTransformationComboBoxModel;
	private JLabel jLabelInputTables;
	private JLabel jLabelParameters;
	private JScrollPane jScrollPaneTablesList;
	private JList<DataTableNode> tableNodesList;
	
	private CheckBoxList<DataTableNode> tableNodesCheckBoxList;
	private RadioButtonList<DataTableNode> tableNodesRadioButtonList;
	private DefaultListModel<DataTableNode> tableNodesListModel;
	
	private JPanel parametersConfigurationPanel;
	
	private TransformationGraphEditorPanel parentEditorPanel;
	
	private PropertyChangeSupport propertyChangeSupport;
	private JPanel jPanelButtons;
	private JButton jButtonApply;
	private JButton jButtonCancel;
	
	private AbstractDataTransformation dataTransformation;
	
	/**
	 * Instantiates a new j panel configure data transformation.
	 */
	public DataTransformationConfigurationPanel() {
		super();
		this.initialize();
	}

	/**
	 * Instantiates a new j panel configure data transformation.
	 * @param parentEditorPanel the editor panel
	 */
	public DataTransformationConfigurationPanel(TransformationGraphEditorPanel parentEditorPanel) {
		this.parentEditorPanel = parentEditorPanel;
		this.initialize();
		this.setToCurrentSelectionFromGraph();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.3, 0.7, 0.0, Double.MIN_VALUE};
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
		gbc_jScrollPaneParametersTable.insets = new Insets(5, 5, 5, 10);
		gbc_jScrollPaneParametersTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneParametersTable.gridx = 1;
		gbc_jScrollPaneParametersTable.gridy = 2;
		add(this.getParametersConfigurationPanel(), gbc_jScrollPaneParametersTable);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.gridwidth = 2;
		gbc_jPanelButtons.insets = new Insets(0, 0, 0, 5);
		gbc_jPanelButtons.fill = GridBagConstraints.BOTH;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 3;
		add(getJPanelButtons(), gbc_jPanelButtons);
	}

	private JLabel getJLabelSelectTransformation() {
		if (jLabelSelectTransformation == null) {
			jLabelSelectTransformation = new JLabel("Select Transformation");
			jLabelSelectTransformation.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSelectTransformation;
	}
	protected JComboBox<DataTransformationService> getJComboBoxSelectTransformation() {
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
		return tableNodesList;
	}
	
	private void setToCurrentSelectionFromGraph() {
		// --- If an element was selected in the parent graph, pre-select it in the table selection list
		Object currentSelectionFromGraph = this.parentEditorPanel.getSelectedGraphElement();
		if (currentSelectionFromGraph!=null) {
			// --- Find the corresponding table node ------------
			DataTableNode currentSelectionNode = this.getTableNodeForGraphElement(currentSelectionFromGraph);
			int selectedElementIndex = this.getListIndexForTableNode(currentSelectionNode);
			
			if (selectedElementIndex>=0) {
				// --- Select it in the list --------------------
				tableNodesList.getSelectionModel().setSelectionInterval(selectedElementIndex, selectedElementIndex);
				// --- Trigger seleciton-related events ---------
				if (this.getTableNodesList() instanceof RadioButtonList<DataTableNode>) {
					this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TABLE_SELECTED, null, currentSelectionNode);
				} else if (this.getTableNodesList() instanceof CheckBoxList<DataTableNode>) {
					this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TABLE_ADDED, null, currentSelectionNode);
				}
			}
		} else {
			// --- If nothing is set pre-select the first transformation and table
//			this.getJComboBoxSelectTransformation().setSelectedIndex(0);
			if (this.getTableNodesListModel().size()>0) {
				this.getTableNodesList().setSelectedIndex(0);
				DataTableNode selectedNode = this.getTableNodesListModel().get(0);
				if (this.getTableNodesList() instanceof RadioButtonList<DataTableNode>) {
					this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TABLE_SELECTED, null, selectedNode);
				} else if (this.getTableNodesList() instanceof CheckBoxList<DataTableNode>) {
					this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TABLE_ADDED, null, selectedNode);
				}
			}
		}
	}
	
	/**
	 * Gets the table node for the provided graph element.
	 * @param graphElement the graph element
	 * @return the corresponding table node
	 */
	private DataTableNode getTableNodeForGraphElement(Object graphElement) {
		if (graphElement instanceof DataTableNode dtn) {
			// --- Table node, use directly ---------------
			return dtn;
		} else if (graphElement instanceof DataTransformationEdge dte) {
			// Transformation edge, use output node -------
			return dte.getDataTransformation().getOutputNode();
		} else {
			// --- Invalid graph element type -------------
			throw new IllegalArgumentException("Unexpected graph element type: " + graphElement.getClass().getName());
		}
	}
	
	/**
	 * Gets the list index for the provided graph element.
	 * @param graphElement the graph element
	 * @return the list index, -1 if not found
	 */
	private int getListIndexForTableNode(DataTableNode tableNode) {
		if (tableNode!=null) {
			
			// --- Find the corresponding index in the list model  ------------
			for (int i=0; i<this.getTableNodesListModel().size(); i++) {
				if (this.getTableNodesListModel().getElementAt(i)==tableNode) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private CheckBoxList<DataTableNode> getTableNodesCheckBoxList() {
		if (tableNodesCheckBoxList==null) {
			tableNodesCheckBoxList = new CheckBoxList<DataTableNode>(this.getTableNodesListModel());
			tableNodesCheckBoxList.setEnabled(false);
			tableNodesCheckBoxList.addCheckBoxListSelectionListener(this);
		}
		return tableNodesCheckBoxList;
	}
	private RadioButtonList<DataTableNode> getTableNodesRadioButtonList() {
		if (tableNodesRadioButtonList==null) {
			tableNodesRadioButtonList = new RadioButtonList<DataTableNode>(this.getTableNodesListModel());
			tableNodesRadioButtonList.setEnabled(false);
			tableNodesRadioButtonList.addSelectionListener(this);
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
			
			ArrayList<DataTableNode> nodesList =  new ArrayList<DataTableNode>(this.parentEditorPanel.getGraphController().getTransformationGraph().getVertices());
			nodesList.sort(Comparator.nullsFirst(Comparator.comparing(DataTableNode::getLongText, String.CASE_INSENSITIVE_ORDER)));
			tableNodesListModel.addAll(nodesList);
		}
		return tableNodesListModel;
	}
	
	private JPanel getParametersConfigurationPanel() {
		if (parametersConfigurationPanel==null) {
			parametersConfigurationPanel = new JPanel();
			parametersConfigurationPanel.add(new JLabel("No transformation selected"));
		}
		return parametersConfigurationPanel;
	}
	
	private void setParametersConfigurationPanel(AbstractTransformationParameterConfigurationPanel paramConfigPanel) {
		
		// --- Remove the previous panel --------
		if (this.parametersConfigurationPanel!=null) {
			this.remove(parametersConfigurationPanel);
			this.getPropertyChangeSupport().removePropertyChangeListener(paramConfigPanel);
		}
		
		// --- Set the new panel ----------------
		paramConfigPanel.setDataController(this.parentEditorPanel.getDataController());
		this.parametersConfigurationPanel = paramConfigPanel;
		
		// --- Put into the right position in the layout
		GridBagConstraints gbc_paramsConfigPanel = new GridBagConstraints();
		gbc_paramsConfigPanel.insets = new Insets(5, 5, 0, 10);
		gbc_paramsConfigPanel.fill = GridBagConstraints.BOTH;
		gbc_paramsConfigPanel.gridx = 1;
		gbc_paramsConfigPanel.gridy = 2;
		this.add(this.parametersConfigurationPanel, gbc_paramsConfigPanel);
		this.getPropertyChangeSupport().addPropertyChangeListener(paramConfigPanel);
		this.revalidate();
		this.repaint();
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonApply = new GridBagConstraints();
			gbc_jButtonApply.anchor = GridBagConstraints.EAST;
			gbc_jButtonApply.insets = new Insets(5, 0, 5, 10);
			gbc_jButtonApply.gridx = 0;
			gbc_jButtonApply.gridy = 0;
			jPanelButtons.add(getJButtonApply(), gbc_jButtonApply);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(5, 10, 5, 0);
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	protected JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton("Apply");
			jButtonApply.addActionListener(this);
			jButtonApply.setEnabled(false);
		}
		return jButtonApply;
	}
	protected JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJComboBoxSelectTransformation()) {
			DataTransformationService selectedTransformation = (DataTransformationService) this.getJComboBoxSelectTransformation().getSelectedItem();
			if (selectedTransformation!=null) {
				// --- Re-initialize the table nodes list if it is the wrong type for the current transformation
				if (	selectedTransformation.getInputType()==InputType.MULTI_TABLE && this.getTableNodesList() instanceof RadioButtonList<DataTableNode> 
					|| selectedTransformation.getInputType()==InputType.SINGLE_TABLE && this.getTableNodesList() instanceof CheckBoxList<DataTableNode>	) {
					this.tableNodesList = null;
					this.getJScrollPaneTablesList().setViewportView(this.getTableNodesList());
				}
				
				AbstractTransformationParameterConfigurationPanel configPanel = this.getParameterConfigurationPanel(selectedTransformation);
				this.setParametersConfigurationPanel(configPanel);
				this.setToCurrentSelectionFromGraph();
			}
			
			this.getTableNodesList().setEnabled(this.isTransformationSelected());
			this.getJButtonApply().setEnabled(this.isTransformationSelected());
		} else if (ae.getSource()==this.getJButtonApply()) {
			
			String eventID = null;
			if (this.dataTransformation==null) {
				eventID = EVENT_ID_TRANSFORMATION_CREATED;
				this.dataTransformation = this.getSelectedDataTransformation().getNewInstance();
				this.dataTransformation.getInputNodes().addAll(this.getSelectedInputNodes());
			} else {
				eventID = EVENT_ID_TRANSFORMATION_MODIFIED;
			}
			
			// --- Set the configured parameters to the transformation --------
			this.dataTransformation.getTransformationParameters().clear();
			this.dataTransformation.setTransformationParameters(this.getConfiguredTransformationParameters());
			
			// --- Reset the results table, if already existing ---------------
			if (this.dataTransformation.getOutputNode()!=null) {
				this.dataTransformation.getOutputNode().resetResultTable();
			}
			
			this.getPropertyChangeSupport().firePropertyChange(eventID, null, this.dataTransformation);
		} else if (ae.getSource()==this.getJButtonCancel()) {
			if (this.dataTransformation==null) {
				// --- Creating a new transformation was canceled, notify the parent UI to close the panel
				this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TRANSFORMATION_CANCELED, null, null);
			} else {
				// --- Editing an existing transformation was canceled, replace UI contents with the original values. 
				this.setModelToForm(this.dataTransformation);
			}
		}
	}
	
	/**
	 * Gets the corresponding parameter configuration panel for the provided transformation service.
	 * @param transformaitonService the transformaiton service
	 * @return the parameter configuration panel
	 */
	private AbstractTransformationParameterConfigurationPanel getParameterConfigurationPanel(DataTransformationService transformaitonService) {
		if (transformaitonService instanceof DataTransformationServiceSelectColumns) {
			return new TableColumnSelectionPanel(transformaitonService);
		} else if (transformaitonService instanceof  DataTransformationServiceJoinTables){
			return new JoinTablesConfigurationPanel(transformaitonService, this.parentEditorPanel);
		} else if (transformaitonService instanceof DataTransformationServiceFilterRows) {
			return new TableSawRowFilterConfigurationPanel(transformaitonService);
		} else {
			return new DefaultTransformationParameterConfigurationPanel(transformaitonService);
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
	
	/**
	 * Gets the configured transformation parameters.
	 * @return the transformation parameters
	 */
	public Properties getConfiguredTransformationParameters(){
		return ((AbstractTransformationParameterConfigurationPanel)this.getParametersConfigurationPanel()).getConfiguredParameters();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.ui.RadioButtonListSelectionListener#selectionChanged(de.enflexit.df.core.processing.ui.RadioButtonListSelectionEvent)
	 */
	@Override
	public void selectionChanged(RadioButtonListSelectionEvent<DataTableNode> selectionEvent) {
		if (selectionEvent.getSource()==this.getTableNodesList()) {
			this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TABLE_SELECTED, null, selectionEvent.getItem());
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.ui.CheckBoxListSelectionListener#selectionChanged(de.enflexit.df.core.processing.ui.CheckBoxListSelectionEvent)
	 */
	@Override
	public void selectionChanged(CheckBoxListSelectionEvent<DataTableNode> selectionEvent) {
		
		if (selectionEvent.getSource()==this.getTableNodesList()) {
			if (selectionEvent.isSelected()==true) {
				this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TABLE_ADDED, null, selectionEvent.getItem());
			} else {
				this.getPropertyChangeSupport().firePropertyChange(EVENT_ID_TABLE_REMOVED, selectionEvent.getItem(), null);
			}
		}
	}
	
	/**
	 * Gets the property change support.
	 * @return the property change support
	 */
	private PropertyChangeSupport getPropertyChangeSupport() {
		if (propertyChangeSupport==null) {
			propertyChangeSupport = new PropertyChangeSupport(this);
		}
		return propertyChangeSupport;
	}
	
	/**
	 * Adds a change listener.
	 * @param changeListener the change listener
	 */
	public void addChangeListener(PropertyChangeListener changeListener) {
		this.getPropertyChangeSupport().addPropertyChangeListener(changeListener);
	}
	
	/**
	 * Removes a change listener.
	 * @param changeListener the change listener
	 */
	public void removeChangeListener(PropertyChangeListener changeListener) {
		this.getPropertyChangeSupport().removePropertyChangeListener(changeListener);
	}

	/**
	 * Adds external action listeners to the buttons.
	 * @param actionListener the action listener
	 */
	public void addActionListenerToButtons(ActionListener actionListener) {
		this.getJButtonApply().addActionListener(actionListener);
		this.getJButtonCancel().addActionListener(actionListener);
	}
	
	/**
	 * Sets the data transformation.
	 * @param transformation the new data transformation
	 */
	public void setDataTransformation(AbstractDataTransformation transformation) {
		this.dataTransformation = transformation;
		this.setModelToForm(transformation);
	}
	
	/**
	 * Sets the UI elements contents according to the provided transformation instance.
	 * @param transformation the transformation
	 */
	private void setModelToForm(AbstractDataTransformation transformation) {
		
		if (transformation!=null) {
			// --- Set the corresponding transformation -------
			for (int i=0; i<this.getDataTransformationComboBoxModel().getSize(); i++) {
				DataTransformationService transformaitonService = this.getDataTransformationComboBoxModel().getElementAt(i);
				if (transformaitonService!=null && transformaitonService.getName().equals(transformation.getTransformationName())) {
					this.getJComboBoxSelectTransformation().setSelectedIndex(i);
					
					// --- Cannot change the type of an existing transformation
					this.getJComboBoxSelectTransformation().setEnabled(false);
				}
			}
			if (this.getTableNodesList() instanceof CheckBoxList<DataTableNode>) {
				this.getTableNodesCheckBoxList().setSelectedItems(transformation.getInputNodes());
			} else if (this.getTableNodesList() instanceof RadioButtonList<DataTableNode>) {
				this.getTableNodesRadioButtonList().setSelectedItem(transformation.getInputNodes().get(0));
			}
			// --- Cannot change the input tables for an existing transformation --
			this.getTableNodesList().setEnabled(false);
			
			((AbstractTransformationParameterConfigurationPanel)this.getParametersConfigurationPanel()).setConfiguredParameters(transformation.getTransformationParameters());
		}
		
		
	}
	
}
