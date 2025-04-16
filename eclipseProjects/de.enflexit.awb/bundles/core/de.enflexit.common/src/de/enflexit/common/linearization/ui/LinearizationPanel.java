package de.enflexit.common.linearization.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import de.enflexit.common.BundleHelper;
import de.enflexit.common.linearization.LinearCoefficient;
import de.enflexit.common.linearization.LinearFormula;
import de.enflexit.common.linearization.Linearization;
import javax.swing.JSplitPane;

/**
 * The Class LinearizationPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LinearizationPanel extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 7367197262728781067L;
	
	public static String ACTION_COMMAND_ADD_VARIABLE = "Add-Variable";
	public static String ACTION_COMMAND_ADD_FORMULA = "Add-Formula";
	public static String ACTION_COMMAND_REMOVE_FORMULA = "Remove-Formula";
	
	private Linearization linearization;

	private JLabel jLabelHeaderVariables;
	private JPanel jPanelVariablesUsed;
	private JButton jButtonAddVariable;
	
	private JLabel jLabelHeaderFormulas;
	private JScrollPane jScrollPaneFormulas;
	private JTable jTableFormulas;
	private JScrollPane jScrollPaneVariablesUsed;
	private JButton jButtonAddFormula;
	private JButton jButtonRemoveFormula;
	
	private ActionListener externalActionListener;
	private JSplitPane jSplitPane;
	private JPanel jPanelFormulas;
	private JPanel jPanelRanges;
	private LinearizationCheckPanel linearizationCheckPanel;
	
	
	/**
	 * Instantiates a new linearization panel.
	 */
	public LinearizationPanel() {
		this.initialize();
	}
	/**
	 * Initializes the panel.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 48, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeaderVariables = new GridBagConstraints();
		gbc_jLabelHeaderVariables.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderVariables.gridx = 0;
		gbc_jLabelHeaderVariables.gridy = 0;
		add(getJLabelHeaderVariables(), gbc_jLabelHeaderVariables);
		GridBagConstraints gbc_jScrollPaneVariablesUsed = new GridBagConstraints();
		gbc_jScrollPaneVariablesUsed.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneVariablesUsed.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneVariablesUsed.gridx = 0;
		gbc_jScrollPaneVariablesUsed.gridy = 1;
		add(getJScrollPaneVariablesUsed(), gbc_jScrollPaneVariablesUsed);
		GridBagConstraints gbc_jButtonAddVariable = new GridBagConstraints();
		gbc_jButtonAddVariable.insets = new Insets(5, 5, 0, 0);
		gbc_jButtonAddVariable.anchor = GridBagConstraints.NORTH;
		gbc_jButtonAddVariable.gridx = 1;
		gbc_jButtonAddVariable.gridy = 1;
		add(getJButtonAddVariable(), gbc_jButtonAddVariable);
		GridBagConstraints gbc_jSplitPane = new GridBagConstraints();
		gbc_jSplitPane.insets = new Insets(10, 0, 0, 0);
		gbc_jSplitPane.gridwidth = 2;
		gbc_jSplitPane.fill = GridBagConstraints.BOTH;
		gbc_jSplitPane.gridx = 0;
		gbc_jSplitPane.gridy = 2;
		add(getJSplitPane(), gbc_jSplitPane);
	}

	private JLabel getJLabelHeaderVariables() {
		if (jLabelHeaderVariables == null) {
			jLabelHeaderVariables = new JLabel("Used Variables");
			jLabelHeaderVariables.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderVariables;
	}
	private JScrollPane getJScrollPaneVariablesUsed() {
		if (jScrollPaneVariablesUsed == null) {
			jScrollPaneVariablesUsed = new JScrollPane();
			jScrollPaneVariablesUsed.setViewportView(this.getJPanelVariablesUsed());
		}
		return jScrollPaneVariablesUsed;
	}
	private JPanel getJPanelVariablesUsed() {
		if (jPanelVariablesUsed == null) {
			jPanelVariablesUsed = new JPanel();
			jPanelVariablesUsed.setBackground(new Color(255, 255, 255));
			jPanelVariablesUsed.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		}
		return jPanelVariablesUsed;
	}
	
	/**
	 * Update the used variables in the UI.
	 */
	private void updateViewVariablesUsed() {
		
		this.getJPanelVariablesUsed().removeAll();
		for (String variableID : this.getLinearization().getVariableIDs()) {
			// --- Create new JButton -------------------------------
			JButton jButtonVariableID = new JButton(variableID);
			jButtonVariableID.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonVariableID.setIcon(BundleHelper.getImageIcon("Delete.png"));
			jButtonVariableID.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jButtonVariableID.setToolTipText("Remove variable '" + variableID + "'");
			jButtonVariableID.setActionCommand(variableID);
			jButtonVariableID.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JButton jButtonVariableID = (JButton) ae.getSource();
					LinearizationPanel.this.getLinearization().removeLinearCoefficient(jButtonVariableID.getActionCommand());
					LinearizationPanel.this.updateView();
				}
			});
			this.getJPanelVariablesUsed().add(jButtonVariableID);
			
		}
		this.getJPanelVariablesUsed().validate();
		this.getJPanelVariablesUsed().repaint();
	}
	private JButton getJButtonAddVariable() {
		if (jButtonAddVariable == null) {
			jButtonAddVariable = new JButton();
			jButtonAddVariable.setToolTipText("Add variable ...");
			jButtonAddVariable.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAddVariable.setPreferredSize(new Dimension(26, 26));
			jButtonAddVariable.setMinimumSize(new Dimension(26, 26));
			jButtonAddVariable.setMaximumSize(new Dimension(26, 26));
			jButtonAddVariable.setActionCommand(ACTION_COMMAND_ADD_VARIABLE);
			jButtonAddVariable.addActionListener(this);
		}
		return jButtonAddVariable;
	}
	
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setResizeWeight(0.666);
			jSplitPane.setDividerSize(8);
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setLeftComponent(getJPanelFormulas());
			jSplitPane.setRightComponent(getJPanelRanges());
		}
		return jSplitPane;
	}
	private JPanel getJPanelFormulas() {
		if (jPanelFormulas == null) {
			jPanelFormulas = new JPanel();
			GridBagLayout gbl_jPanelFormulas = new GridBagLayout();
			gbl_jPanelFormulas.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelFormulas.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelFormulas.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelFormulas.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			jPanelFormulas.setLayout(gbl_jPanelFormulas);
			GridBagConstraints gbc_jLabelHeaderFormulas = new GridBagConstraints();
			gbc_jLabelHeaderFormulas.anchor = GridBagConstraints.WEST;
			gbc_jLabelHeaderFormulas.gridx = 0;
			gbc_jLabelHeaderFormulas.gridy = 0;
			jPanelFormulas.add(getJLabelHeaderFormulas(), gbc_jLabelHeaderFormulas);
			GridBagConstraints gbc_jButtonAddFormula = new GridBagConstraints();
			gbc_jButtonAddFormula.gridx = 1;
			gbc_jButtonAddFormula.gridy = 0;
			jPanelFormulas.add(getJButtonAddFormula(), gbc_jButtonAddFormula);
			GridBagConstraints gbc_jButtonRemoveFormula = new GridBagConstraints();
			gbc_jButtonRemoveFormula.gridx = 2;
			gbc_jButtonRemoveFormula.gridy = 0;
			jPanelFormulas.add(getJButtonRemoveFormula(), gbc_jButtonRemoveFormula);
			GridBagConstraints gbc_jScrollPaneFormulas = new GridBagConstraints();
			gbc_jScrollPaneFormulas.insets = new Insets(0, 0, 5, 0);
			gbc_jScrollPaneFormulas.gridwidth = 3;
			gbc_jScrollPaneFormulas.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneFormulas.gridx = 0;
			gbc_jScrollPaneFormulas.gridy = 1;
			jPanelFormulas.add(getJScrollPaneFormulas(), gbc_jScrollPaneFormulas);
		}
		return jPanelFormulas;
	}
	private JLabel getJLabelHeaderFormulas() {
		if (jLabelHeaderFormulas == null) {
			jLabelHeaderFormulas = new JLabel("List of linear coefficients and their boundaries");
			jLabelHeaderFormulas.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderFormulas;
	}
	private JButton getJButtonAddFormula() {
		if (jButtonAddFormula == null) {
			jButtonAddFormula = new JButton();
			jButtonAddFormula.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAddFormula.setToolTipText("Add linear equation");
			jButtonAddFormula.setPreferredSize(new Dimension(26, 26));
			jButtonAddFormula.setMaximumSize(new Dimension(26, 26));
			jButtonAddFormula.setMinimumSize(new Dimension(26, 26));
			jButtonAddFormula.setActionCommand(ACTION_COMMAND_ADD_FORMULA);
			jButtonAddFormula.addActionListener(this);
		}
		return jButtonAddFormula;
	}
	private JButton getJButtonRemoveFormula() {
		if (jButtonRemoveFormula == null) {
			jButtonRemoveFormula = new JButton();
			jButtonRemoveFormula.setIcon(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonRemoveFormula.setToolTipText("Remove selected linear equation");
			jButtonRemoveFormula.setPreferredSize(new Dimension(26, 26));
			jButtonRemoveFormula.setMaximumSize(new Dimension(26, 26));
			jButtonRemoveFormula.setMinimumSize(new Dimension(26, 26));
			jButtonRemoveFormula.setActionCommand(ACTION_COMMAND_REMOVE_FORMULA);
			jButtonRemoveFormula.addActionListener(this);
		}
		return jButtonRemoveFormula;
	}
	private JScrollPane getJScrollPaneFormulas() {
		if (jScrollPaneFormulas == null) {
			jScrollPaneFormulas = new JScrollPane();
			jScrollPaneFormulas.setViewportView(getJTableFormulas());
		}
		return jScrollPaneFormulas;
	}
	
	/**
	 * Returns the table model for formulas.
	 * @return the table model formulas
	 */
	private DefaultTableModel getTableModelFormulas() {
		
		Vector<String> columnNames = new Vector<>(); 

		// --- Coefficient columns ------------------------
		List<String> variableIDs = this.getLinearization().getVariableIDs();
		for (String variableID : variableIDs) {
			columnNames.add("Coeff. of " + variableID);
		}
		columnNames.add("Axis Intercept");
		
		// --- Coefficient Ranges -------------------------
		for (String variableID : variableIDs) {
			columnNames.add(variableID + " from (>=)");
			columnNames.add(variableID + " to (<)");
		}

		// --- Initiate the table model -------------------
		DefaultTableModel tableModelFormulas = new DefaultTableModel(null, columnNames) {
			private static final long serialVersionUID = 7986575831602975168L;
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return Double.class;
			}
		};
		this.fillTableModel(tableModelFormulas);
		
		// --- Add table model listener ------------------- 
		tableModelFormulas.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent tmEv) {
				if (tmEv.getType()==TableModelEvent.UPDATE) {
					Double newValue = (Double) LinearizationPanel.this.getJTableFormulas().getModel().getValueAt(tmEv.getFirstRow(), tmEv.getColumn());
					LinearizationPanel.this.updateValue(tmEv.getFirstRow(), tmEv.getColumn(), newValue);
				}
			}
		});
		return tableModelFormulas;
	}
	/**
	 * Fills the specified table model.
	 * @param tableModel the table model to fill
	 */
	private void fillTableModel(DefaultTableModel tableModel) {
		for (LinearFormula linFormula : this.getLinearization().getLinearFormulaList()) {
			tableModel.addRow(this.getModelRow(linFormula));
		}
	}
	/**
	 * Returns a model row for the specified LinearFormula.
	 * @param linFormula the linear formula to a create a model row for
	 * @return the model row
	 */
	private Vector<Double> getModelRow(LinearFormula linFormula) {
		
		Vector<Double> modelRow = new Vector<>();
		Vector<Double> rangeColumns = new Vector<>();
		
		List<String> variableIDs = this.getLinearization().getVariableIDs();
		for (String variableID : variableIDs) {
			
			LinearCoefficient linCoeff = linFormula.getCoefficient(variableID);
			modelRow.add(linCoeff.getValue());

			rangeColumns.add(linCoeff.getValidFrom());
			rangeColumns.add(linCoeff.getValidTo());
		}
		// --- Add the axis intercept value -------------------------
		modelRow.add(linFormula.getAxisIntercept());
		
		// --- Add the range columns to the actual row --------------
		modelRow.addAll(rangeColumns);
		
		return modelRow;
	}
	
	/**
	 * Updates the value in the {@link Linearization}.
	 *
	 * @param rowIndex the table model row
	 * @param colIndex the table model column
	 * @param newValue the new value
	 */
	private void updateValue(int rowIndex, int colIndex, double newValue) {
		
		List<String> variableIDList = this.getLinearization().getVariableIDs();
		int noOfCoeff = variableIDList.size();
		LinearFormula editLinearFormula = this.getLinearization().getLinearFormulaList().get(rowIndex);
		
		if (colIndex <= (noOfCoeff-1)) {
			// --- A coefficient was edited ---------------
			String editVariableID = variableIDList.get(colIndex);
			LinearCoefficient coefficient = editLinearFormula.getCoefficient(editVariableID);
			coefficient.setValue(newValue);
			
		} else if (colIndex==noOfCoeff) {
			// --- The axis intersection was edited -------
			editLinearFormula.setAxisIntercept(newValue);
		
		} else {
			// --- A range value was edited ---------------
			int variableIDIndex = 0;
			for (int i = noOfCoeff+1; i < this.getJTableFormulas().getColumnCount(); i=i+2) {
				if (i==colIndex || (i+1)==colIndex) {
					// --- Update coefficient value ------- 
					String editVariableID = variableIDList.get(variableIDIndex);
					LinearCoefficient coefficient = editLinearFormula.getCoefficient(editVariableID);
					if (i==colIndex) {
						// --- From value -----------------
						coefficient.setValidFrom(newValue);
					} else {
						// --- To value -------------------
						coefficient.setValidTo(newValue);
					}
					break;
				}
				variableIDIndex++;
			}
			
		}
	}
	/**
	 * Returns the JTable for formulas.
	 * @return the JTable formulas
	 */
	private JTable getJTableFormulas() {
		if (jTableFormulas == null) {
			jTableFormulas = new JTable(this.getTableModelFormulas()) {
				private static final long serialVersionUID = -2553594034480496315L;
				private int alignment = SwingConstants.LEFT;
				@Override
				public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
					Component comp = super.prepareRenderer(renderer, row, column);
					if (comp instanceof JLabel) {
						JLabel jLabel = (JLabel) comp;
						jLabel.setHorizontalAlignment(alignment);
					}
					return comp;
				}
				
				@Override
				public Component prepareEditor(TableCellEditor editor, int row, int column) {
					Component comp = super.prepareEditor(editor, row, column);
					if (comp instanceof JTextField) {
						JTextField jTextField = (JTextField) comp;
						jTextField.setHorizontalAlignment(alignment);
						jTextField.selectAll();
					}
					return comp;
				}
			};
			jTableFormulas.setFillsViewportHeight(true);
			jTableFormulas.getTableHeader().setReorderingAllowed(false);
			jTableFormulas.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableFormulas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTableFormulas.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableFormulas.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
			jTableFormulas.setRowHeight(20);
			
		}
		return jTableFormulas;
	}
	/**
	 * Updates the formula table.
	 */
	private void updateViewFormulaTable() {
		this.getJTableFormulas().setModel(this.getTableModelFormulas());
		this.updateTableColumnWidth();
	}
	/**
	 * Updates the table column width.
	 */
	private void updateTableColumnWidth() {
		// --- Set a preferred column width -----------
		int colWidth = 140;
		TableColumnModel tcm = this.getJTableFormulas().getColumnModel();
		for (int col = 0; col < tcm.getColumnCount(); col++) {
			tcm.getColumn(col).setPreferredWidth(colWidth);
		}
	}
	
	private JPanel getJPanelRanges() {
		if (jPanelRanges == null) {
			jPanelRanges = new JPanel();
			GridBagLayout gbl_jPanelRanges = new GridBagLayout();
			gbl_jPanelRanges.columnWidths = new int[]{0, 0};
			gbl_jPanelRanges.rowHeights = new int[]{0, 0};
			gbl_jPanelRanges.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelRanges.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			jPanelRanges.setLayout(gbl_jPanelRanges);
			GridBagConstraints gbc_linearizationCheckPanel = new GridBagConstraints();
			gbc_linearizationCheckPanel.insets = new Insets(5, 0, 0, 0);
			gbc_linearizationCheckPanel.fill = GridBagConstraints.BOTH;
			gbc_linearizationCheckPanel.gridx = 0;
			gbc_linearizationCheckPanel.gridy = 0;
			jPanelRanges.add(this.getLinearizationCheckPanel(), gbc_linearizationCheckPanel);
		}
		return jPanelRanges;
	}
	private LinearizationCheckPanel getLinearizationCheckPanel() {
		if (linearizationCheckPanel == null) {
			linearizationCheckPanel = new LinearizationCheckPanel(this.getLinearization());
		}
		return linearizationCheckPanel;
	}	
	
	
	/**
	 * Updates the overall view onto the current Linearization.
	 */
	private void updateView() {
		this.updateViewVariablesUsed();
		this.updateViewFormulaTable();
	}
	
	/**
	 * Returns the linearization.
	 * @return the linearization
	 */
	public Linearization getLinearization() {
		if (linearization==null) {
			linearization = new Linearization();
			linearization.addPropertyChangeListener(this);
		}
		return linearization;
	}
	/**
	 * Sets the linearization.
	 * @param linearization the new linearization
	 */
	public void setLinearization(Linearization linearization) {
		this.linearization = linearization;
		this.linearization.addPropertyChangeListener(this);
		this.getLinearizationCheckPanel().setLinearization(this.linearization);
		this.updateView();
		this.doLinearizationChecks();
	}
	/**
	 * Does the linearization checks.
	 */
	private void doLinearizationChecks() {
		this.getLinearization().getValidator().doChecksInThread();
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		switch (evt.getPropertyName()) {
		case Linearization.PROPERTY_LINEAR_COEFFICIENT_ADDED:
		case Linearization.PROPERTY_LINEAR_COEFFICIENT_REMOVED:
		case Linearization.PROPERTY_LINEAR_COEFFICIENT_RENAMED:
			this.updateView();
			break;
		case Linearization.PROPERTY_LINEAR_FORMULA_ADDED:
		case Linearization.PROPERTY_LINEAR_FORMULA_REMOVED:
			this.updateViewFormulaTable();
			break;
		case Linearization.PROPERTY_VALIDATION_DONE:
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					LinearizationPanel.this.getLinearizationCheckPanel().updateValidationMessages();
				}
			});
		}
	}
	
	
	/**
	 * Sets the specified ActionListener to the JButton for the add action.
	 * @param newActionListener the new action listener to add action
	 */
	public void setActionListenerToAddVariableAction(ActionListener newActionListener) {
		
		// --- Which ActionListener is currently used ---------------
		ActionListener alUsed = this.externalActionListener!=null ? this.externalActionListener : this;
		
		// --- Remove previously used ActionListener ---------------- 
		this.getJButtonAddVariable().removeActionListener(alUsed);
		
		// --- Apply the new ActionListener -------------------------
		ActionListener alToUse = newActionListener;  
		this.externalActionListener = newActionListener;
		if (alToUse==null) alToUse = this;
		
		// --- Add the selected ActionListener ---------------------- 
		this.getJButtonAddVariable().addActionListener(alToUse);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	
		if (ae.getSource()==this.getJButtonAddVariable()) {
			// --- Add a new linearization variable -----------------
			String title = "Add new variable to linearization";
			String msg = "Please, specify the new variable for the linearization!";
			int msgType = JOptionPane.QUESTION_MESSAGE;
			
			String newVariableID = null;
			while (newVariableID==null || this.getLinearization().getVariableIDs().contains(newVariableID)) {
				
				String errMsg = "";
				if (newVariableID!=null) {
					errMsg = "\n(Note: The variable name '" + newVariableID + "' is already in use)";
					msgType = JOptionPane.WARNING_MESSAGE;
				}
				// --- Ask user ---------------------------
				newVariableID = JOptionPane.showInputDialog(this, msg + errMsg + "\n ", title, msgType);
				if (newVariableID==null || newVariableID.isBlank()) {
					return;
				}
			}
			// --- Add new LinearCoefficient --------------
			this.getLinearization().addLinearCoefficient(LinearCoefficient.createLinearCoefficient(newVariableID, 0.0, this.getLinearization().getLowerBoundary(newVariableID), this.getLinearization().getUpperBoundary(newVariableID)));
			
		} else if (ae.getSource()==this.getJButtonAddFormula()) {
			// --- Add a new Formula ----------------------
			LinearFormula formula = this.getLinearization().createLinearFormula();
			this.getLinearization().addLinearFormula(formula);
			// --- Select that new formula in the table ---
			int newRowCount = this.getJTableFormulas().getRowCount();
			if (newRowCount>0) {
				newRowCount--;
				this.getJTableFormulas().setRowSelectionInterval(newRowCount, newRowCount);
			}
			
		} else if (ae.getSource()==this.getJButtonRemoveFormula()) {
			// --- Remove selected Formula ----------------
			int tableRowSelected = this.getJTableFormulas().getSelectedRow();
			if (tableRowSelected!=-1) {
				// --- Get model row and remove -----------
				int modelRowSelected = this.getJTableFormulas().convertRowIndexToModel(tableRowSelected);
				LinearFormula formulaToRemove = this.getLinearization().getLinearFormulaList().get(modelRowSelected);
				if (this.getLinearization().removeLinearFormula(formulaToRemove)==true) {
					// --- Select next row ----------------
					int nTableRows = this.getJTableFormulas().getRowCount();
					if (nTableRows>0) {
						if (tableRowSelected>(nTableRows-1)) tableRowSelected--;
						this.getJTableFormulas().setRowSelectionInterval(tableRowSelected, tableRowSelected);
					}
				}
			}
			
		}
	}
	
}
