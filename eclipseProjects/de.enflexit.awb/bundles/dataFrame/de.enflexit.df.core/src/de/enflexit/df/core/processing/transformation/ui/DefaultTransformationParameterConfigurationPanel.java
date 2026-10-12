package de.enflexit.df.core.processing.transformation.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import de.enflexit.df.core.processing.transformation.DataTransformationService;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.beans.PropertyChangeEvent;

import javax.swing.JLabel;

/**
 * Default transformation configuration panel, providing a table with one row for each required
 * parameter. All returned parameter values will be strings, parse as required if necessary.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DefaultTransformationParameterConfigurationPanel extends AbstractTransformationParameterConfigurationPanel {

	private static final long serialVersionUID = 3783681403788015570L;
	
	private JScrollPane jScrollPaneParamsTable;
	private JTable jTableParameters;
	private DefaultTableModel parametersTableModel;
	private JLabel jLabelTest;

	/**
	 * Instantiates a new default transformation parameter configuration panel.
	 * @param transformation the transformation
	 */
	public DefaultTransformationParameterConfigurationPanel(DataTransformationService transformation) {
		super(transformation);
		initialize();
    }
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJLabelTransformaitonName(), BorderLayout.NORTH);
		add(getJScrollPaneParametersTable(), BorderLayout.CENTER);
	}

	/**
	 * Gets the j scroll pane parameters table.
	 * @return the j scroll pane parameters table
	 */
	private JScrollPane getJScrollPaneParametersTable() {
		if (jScrollPaneParamsTable == null) {
			jScrollPaneParamsTable = new JScrollPane();
			jScrollPaneParamsTable.setViewportView(getJTableParameters());
		}
		return jScrollPaneParamsTable;
	}
	
	/**
	 * Gets the j table parameters.
	 * @return the j table parameters
	 */
	private JTable getJTableParameters() {
		if (jTableParameters == null) {
			jTableParameters = new JTable();
			jTableParameters.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableParameters.setModel(this.getParametersTableModel());
		}
		return jTableParameters;
	}
	
	/**
	 * Gets the parameters table model.
	 * @return the parameters table model
	 */
	private DefaultTableModel getParametersTableModel() {
		if (parametersTableModel==null) {
			
			Vector<String> headerVector = new Vector<String>();
			headerVector.add("Parameter");
			headerVector.add("Value");

			// --- Add one row for every required parameter ---------
			Vector<Vector<String>> modelVector = new Vector<Vector<String>>();
			for (String paramName : this.getTransformation().getRequiredParameters()) {
				Vector<String> rowVector = new Vector<String>();
				rowVector.add(paramName);
				rowVector.add("");
				modelVector.add(rowVector);
			}
			
			parametersTableModel = new DefaultTableModel(modelVector, headerVector);
		}
		return parametersTableModel;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.ui.AbstractTransformationParameterConfigurationPanel#getConfiguredParameters()
	 */
	@Override
	public Map<String, Object> getConfiguredParameters() {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		// --- Convert the table entries to a key value map ---------
		for (int i=0; i<this.getParametersTableModel().getRowCount(); i++) {
			String paramName = (String) this.getParametersTableModel().getValueAt(i, 0);
			Object paramValue = this.getParametersTableModel().getValueAt(i, 1);
			parameters.put(paramName, paramValue);
		}
		
		return parameters;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#setConfiguredParameters(java.util.Map)
	 */
	@Override
	public void setConfiguredParameters(Map<String, Object> parameters) {
		this.getParametersTableModel().getDataVector().clear();
		for (String paramName : parameters.keySet()) {
			Vector<String> paramRow = new Vector<String>();
			paramRow.add(paramName);
			paramRow.add(parameters.get(paramName).toString());
			this.getParametersTableModel().addRow(paramRow);
		}
	}

	private JLabel getJLabelTransformaitonName() {
		if (jLabelTest == null) {
			jLabelTest = new JLabel("Configuration Panel for " + this.getTransformation().getName());
			jLabelTest.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelTest;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// --- No action required ---------------
	}

}
