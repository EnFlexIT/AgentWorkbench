package de.enflexit.df.impl.db;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.common.NumberHelper;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.BundleHelper;

/**
 * The Class JPanelQueryConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelQueryConfiguration extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1582587866051687200L;
	
	private DatabaseDataSourceIntegration databaseDataSourceIntegration;
	
	private enum EditState {
		NoEditing,
		EditNewEntryUnsaved,
		EditKnownEntry,
		EditKnownEntryUnsaved,
	}
	
	private EditState editState;
	private DatabaseQuery databaseQueryEdit;
	private boolean isPauserEditStateDocumentListener;
	
	private JLabel jLabelHeader;
	private JTextArea jTextAreaSqlStatement;
	private JLabel jLabelSQLHeader;
	private JLabel jLabelNo;
	private JTextField jTextFieldNo;
	private JLabel jLabelName;
	private JTextField jTextFieldName;
	
	private JScrollPane jScrollPaneSQLList;
	private JTable jTableDatabaseQueries;
	private DefaultTableModel tablModelDatabaseQuery;
	
	private JToolBar jToolBarDatabaseQuery;
	private JButton jButtonQueryAdd;
	private JButton jButtonQuerySave;
	private JButton jButtonQueryRemove;
	private JButton jButtonQueryUp;
	private JButton jButtonQueryDown;
	private JButton jButtonQueryCheck;
	private JButton jButtonShowTable;
	
	/**
	 * Instantiates a new JPanel that enables to configure queries.
	 * @param databaseDataSourceIntegration the database data source integration
	 */
	public JPanelQueryConfiguration(DatabaseDataSourceIntegration databaseDataSourceIntegration) {
		this.databaseDataSourceIntegration = databaseDataSourceIntegration;
		this.initialize();
		this.fillTableModel();
		this.setEditState(EditState.NoEditing);
	}
	
	/**
	 * Returns the current list of DatabaseQuery that belong to the {@link DatabaseDataSource}.
	 * @return the database query list
	 */
	private List<DatabaseQuery> getDatabaseQueryList() {
		return this.databaseDataSourceIntegration.getDataSource().getDatabaseQueryList();
	}
	/**
	 * Add the specified DatabaseQuery to the {@link DatabaseDataSource}.
	 *
	 * @param dbQueryToSave the db query to save
	 * @return the database query to data source
	 */
	private void addDatabaseQueryToDataSource(DatabaseQuery dbQueryToSave) {
		this.getDatabaseQueryList().add(dbQueryToSave);
		this.saveDatabaseQueryList();
	}
	/**
	 * Saves the current database query list.
	 */
	private void saveDatabaseQueryList() {
		this.databaseDataSourceIntegration.getDataSource().updateSubConfigurations();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.gridwidth = 4;
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(this.getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jScrollPaneSQLList = new GridBagConstraints();
		gbc_jScrollPaneSQLList.insets = new Insets(10, 10, 0, 10);
		gbc_jScrollPaneSQLList.gridwidth = 4;
		gbc_jScrollPaneSQLList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneSQLList.gridx = 0;
		gbc_jScrollPaneSQLList.gridy = 1;
		this.add(this.getJScrollPaneSQLList(), gbc_jScrollPaneSQLList);
		GridBagConstraints gbc_jToolBarDatabaseQuery = new GridBagConstraints();
		gbc_jToolBarDatabaseQuery.anchor = GridBagConstraints.WEST;
		gbc_jToolBarDatabaseQuery.insets = new Insets(10, 10, 0, 10);
		gbc_jToolBarDatabaseQuery.gridwidth = 4;
		gbc_jToolBarDatabaseQuery.gridx = 0;
		gbc_jToolBarDatabaseQuery.gridy = 2;
		this.add(this.getJToolBarDatabaseQuery(), gbc_jToolBarDatabaseQuery);
		GridBagConstraints gbc_jLabelNo = new GridBagConstraints();
		gbc_jLabelNo.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelNo.anchor = GridBagConstraints.WEST;
		gbc_jLabelNo.gridx = 0;
		gbc_jLabelNo.gridy = 3;
		this.add(this.getJLabelNo(), gbc_jLabelNo);
		GridBagConstraints gbc_jTextFieldNo = new GridBagConstraints();
		gbc_jTextFieldNo.anchor = GridBagConstraints.WEST;
		gbc_jTextFieldNo.insets = new Insets(10, 5, 0, 0);
		gbc_jTextFieldNo.gridx = 1;
		gbc_jTextFieldNo.gridy = 3;
		this.add(this.getJTextFieldNo(), gbc_jTextFieldNo);
		GridBagConstraints gbc_jLabelName = new GridBagConstraints();
		gbc_jLabelName.anchor = GridBagConstraints.WEST;
		gbc_jLabelName.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelName.gridx = 0;
		gbc_jLabelName.gridy = 4;
		this.add(this.getJLabelName(), gbc_jLabelName);
		GridBagConstraints gbc_jTextFieldName = new GridBagConstraints();
		gbc_jTextFieldName.gridwidth = 2;
		gbc_jTextFieldName.anchor = GridBagConstraints.WEST;
		gbc_jTextFieldName.insets = new Insets(10, 5, 0, 0);
		gbc_jTextFieldName.gridx = 1;
		gbc_jTextFieldName.gridy = 4;
		this.add(this.getJTextFieldName(), gbc_jTextFieldName);
		GridBagConstraints gbc_jLabelSQLHeader = new GridBagConstraints();
		gbc_jLabelSQLHeader.insets = new Insets(12, 10, 0, 0);
		gbc_jLabelSQLHeader.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelSQLHeader.gridx = 0;
		gbc_jLabelSQLHeader.gridy = 5;
		this.add(this.getJLabelSQLHeader(), gbc_jLabelSQLHeader);
		GridBagConstraints gbc_textAreaSqlStatement = new GridBagConstraints();
		gbc_textAreaSqlStatement.gridwidth = 3;
		gbc_textAreaSqlStatement.insets = new Insets(10, 5, 0, 10);
		gbc_textAreaSqlStatement.fill = GridBagConstraints.BOTH;
		gbc_textAreaSqlStatement.gridx = 1;
		gbc_textAreaSqlStatement.gridy = 5;
		this.add(this.getJTextAreaSqlStatement(), gbc_textAreaSqlStatement);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Tables, Views or Queries used with the Database-Connection");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelHeader;
	}
	
	private JScrollPane getJScrollPaneSQLList() {
		if (jScrollPaneSQLList == null) {
			jScrollPaneSQLList = new JScrollPane(this.getJTableDatabaseQueries());
			jScrollPaneSQLList.setPreferredSize(new Dimension(300, 180));
		}
		return jScrollPaneSQLList;
	}
	private JTable getJTableDatabaseQueries() {
		if (jTableDatabaseQueries==null) {
			jTableDatabaseQueries = new JTable(this.getTableModelDatabaseQuery());
			jTableDatabaseQueries.setFillsViewportHeight(true);
			//jTableDatabaseQueries.setShowGrid(false);
			jTableDatabaseQueries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableDatabaseQueries.getTableHeader().setReorderingAllowed(false);
			jTableDatabaseQueries.setFont(new Font("Dialog", Font.PLAIN, 12));
			
			// --- Define the column widths -------------------------
			TableColumnModel colModel = jTableDatabaseQueries.getColumnModel();
			colModel.getColumn(0).setPreferredWidth(0);
			colModel.getColumn(0).setMinWidth(0);
			colModel.getColumn(0).setMaxWidth(0);
			
			colModel.getColumn(1).setPreferredWidth(60);
			colModel.getColumn(1).setMinWidth(40);
			colModel.getColumn(1).setMaxWidth(80);
					
			colModel.getColumn(2).setPreferredWidth(240);
			colModel.getColumn(2).setMinWidth(100);
			colModel.getColumn(2).setMaxWidth(300);
			
			
			jTableDatabaseQueries.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lsEv) {
					
					if (lsEv.getValueIsAdjusting()==true) return;
					
					int rowSelected = JPanelQueryConfiguration.this.getJTableDatabaseQueries().getSelectedRow();
					if (rowSelected==-1) return;
					
					if (JPanelQueryConfiguration.this.isContinueFocusChangeOfDatabaseQuery()==true) {
						DatabaseQuery dbQuery = (DatabaseQuery) JPanelQueryConfiguration.this.getJTableDatabaseQueries().getValueAt(rowSelected, 0);
						JPanelQueryConfiguration.this.setDatabaseQuery(dbQuery);
						JPanelQueryConfiguration.this.setEditState(EditState.EditKnownEntry);
					} else {
						JPanelQueryConfiguration.this.selectDataBaseQueryInTable(JPanelQueryConfiguration.this.getDatabaseQuery());
					}
				}
			});
			
		}
		return jTableDatabaseQueries;
	}
	private DefaultTableModel getTableModelDatabaseQuery() {
		if (tablModelDatabaseQuery==null) {
			
			Vector<String> headerVector = new Vector<>();
			headerVector.add("DatabaseQuery");
			headerVector.add("No.");
			headerVector.add("Name");
			headerVector.add("SQL-Statement");
			
			tablModelDatabaseQuery = new DefaultTableModel(null, headerVector) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					
					Class<?> clazz = super.getColumnClass(columnIndex); 
					switch (columnIndex) {
					case 0:
						break;
					case 1:
						clazz = Integer.class;
						break;
					case 2:
						clazz = String.class;
						break;
					case 3:
						clazz = String.class;
						break;
					}
					return clazz;
				}
			};
		}
		return tablModelDatabaseQuery;
	}
	/**
	 * Returns the row index of specified {@link DatabaseQuery}.
	 *
	 * @param dbQuery the DatabaseQuery to search for
	 * @return the table model row
	 */
	private int getTableModelRowIndex(DatabaseQuery dbQuery) {
		
		int modelRowToRemove = -1;
		for (int row = 0; row < this.getTableModelDatabaseQuery().getRowCount(); row++) {
			DatabaseQuery dbQueryCheck = (DatabaseQuery) this.getTableModelDatabaseQuery().getValueAt(row, 0);
			if (dbQueryCheck.equals(dbQuery)==true) {
				modelRowToRemove = row;
			}
		}
		return modelRowToRemove;
	}
	
	/**
	 * Fills the table model with the already defined DatabaseQueries.
	 */
	private void fillTableModel() {
		this.getDatabaseQueryList().forEach(dbQuery -> this.addDatabaseQueryToTable(dbQuery) );
	}
	
	/**
	 * Adds a DatabaseQuery to the table model.
	 *
	 * @param dbQuery the DatabaseQuery
	 */
	private void addDatabaseQueryToTable(DatabaseQuery dbQuery) {
		this.addDatabaseQueryToTable(dbQuery, null);
	}
	/**
	 * Adds a DatabaseQuery to the table model.
	 *
	 * @param dbQuery the DatabaseQuery
	 * @param atRowIndex the at row index
	 */
	private void addDatabaseQueryToTable(DatabaseQuery dbQuery, Integer atRowIndex) {
		Vector<Object> row = new Vector<>();
		row.add(dbQuery);
		row.add(dbQuery.getNumber());
		row.add(dbQuery.getName());
		row.add(dbQuery.getSqlStatement());
		if (atRowIndex==null) {
			this.getTableModelDatabaseQuery().addRow(row);
		} else {
			this.getTableModelDatabaseQuery().insertRow(atRowIndex, row);
		}
	}
	/**
	 * Updates the table row of the specified DatabaseQuery.
	 * @param dbQuery the db query
	 */
	private void updateDatabaseQueryInTable(DatabaseQuery dbQuery) {
		
		int modelRowToUpdate = this.getTableModelRowIndex(dbQuery);
		if (modelRowToUpdate==-1) return;
		
		this.getTableModelDatabaseQuery().setValueAt(dbQuery.getNumber(), modelRowToUpdate, 1);
		this.getTableModelDatabaseQuery().setValueAt(dbQuery.getName(), modelRowToUpdate, 2);
		this.getTableModelDatabaseQuery().setValueAt(dbQuery.getSqlStatement(), modelRowToUpdate, 3);
	}
	
	/**
	 * Removes the DatabaseQuery from the table.
	 *
	 * @param dbQuery the DatabaseQuery to delete 
	 * @return the index position at which the query was located 
	 */
	private int removeDatabaseQueryFromTable(DatabaseQuery dbQuery) {
		
		if (dbQuery==null) return -1;
		
		int modelRowToRemove = this.getTableModelRowIndex(dbQuery);
		if (modelRowToRemove==-1) return -1;
		
		// --- Remove the element -----------------------------------
		this.getTableModelDatabaseQuery().removeRow(modelRowToRemove);
		
		// --- Re-number the remaining DatabaseQuery's --------------
		for (int row = 0; row < this.getTableModelDatabaseQuery().getRowCount(); row++) {
			DatabaseQuery dbQueryCheck = (DatabaseQuery) this.getTableModelDatabaseQuery().getValueAt(row, 0);
			dbQueryCheck.setNumber(row + 1);
			this.getTableModelDatabaseQuery().setValueAt(row + 1, row, 1);
		}
		return modelRowToRemove;
	}
	/**
	 * Selects the specified Database query in the table.
	 * @param dbQuery the DatabaseQuery to focus
	 */
	private void selectDataBaseQueryInTable(DatabaseQuery dbQuery) {
		
		if (dbQuery==null) return;
		
		int prevSelModel = this.getTableModelRowIndex(dbQuery);
		int prevSelTable = this.getJTableDatabaseQueries().convertRowIndexToView(prevSelModel);
		if (prevSelModel==-1) {
			this.getJTableDatabaseQueries().clearSelection();	
		} else {
			this.getJTableDatabaseQueries().getSelectionModel().setSelectionInterval(prevSelTable, prevSelTable);
		}
	}

	
	private JToolBar getJToolBarDatabaseQuery() {
		if (jToolBarDatabaseQuery == null) {
			jToolBarDatabaseQuery = new JToolBar();
			jToolBarDatabaseQuery.setFont(new Font("Dialog", Font.PLAIN, 12));
			jToolBarDatabaseQuery.setFloatable(false);
			
			jToolBarDatabaseQuery.add(this.getJButtonQueryAdd());
			jToolBarDatabaseQuery.add(this.getJButtonQuerySave());
			jToolBarDatabaseQuery.add(this.getJButtonQueryRemove());
			jToolBarDatabaseQuery.addSeparator();
			jToolBarDatabaseQuery.add(this.getJButtonQueryUp());
			jToolBarDatabaseQuery.add(this.getJButtonQueryDown());
			jToolBarDatabaseQuery.addSeparator();
			jToolBarDatabaseQuery.add(getJButtonQueryCheck());
			jToolBarDatabaseQuery.add(getJButtonShowTable());
			jToolBarDatabaseQuery.addSeparator();
		}
		return jToolBarDatabaseQuery;
	}
	
	private JButton getJButtonQueryAdd() {
		if (jButtonQueryAdd == null) {
			jButtonQueryAdd = new JButton();
			jButtonQueryAdd.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonQueryAdd.setToolTipText("Add new query");
			jButtonQueryAdd.setPreferredSize(new Dimension(26, 26));
			jButtonQueryAdd.addActionListener(this);
		}
		return jButtonQueryAdd;
	}
	private JButton getJButtonQuerySave() {
		if (jButtonQuerySave == null) {
			jButtonQuerySave = new JButton();
			jButtonQuerySave.setIcon(BundleHelper.getImageIcon("MBsave.png"));
			jButtonQuerySave.setToolTipText("Save selected query");
			jButtonQuerySave.setPreferredSize(new Dimension(26, 26));
			jButtonQuerySave.addActionListener(this);
		}
		return jButtonQuerySave;
	}
	private JButton getJButtonQueryRemove() {
		if (jButtonQueryRemove == null) {
			jButtonQueryRemove = new JButton();
			jButtonQueryRemove.setIcon(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonQueryRemove.setToolTipText("Remove selected query");
			jButtonQueryRemove.setPreferredSize(new Dimension(26, 26));
			jButtonQueryRemove.addActionListener(this);
		}
		return jButtonQueryRemove;
	}
	
	
	private JButton getJButtonQueryUp() {
		if (jButtonQueryUp == null) {
			jButtonQueryUp = new JButton();
			jButtonQueryUp.setIcon(BundleHelper.getImageIcon("ArrowUp.png"));
			jButtonQueryUp.setToolTipText("Move selected query up");
			jButtonQueryUp.setPreferredSize(new Dimension(26, 26));
			jButtonQueryUp.addActionListener(this);
		}
		return jButtonQueryUp;
	}
	private JButton getJButtonQueryDown() {
		if (jButtonQueryDown == null) {
			jButtonQueryDown = new JButton();
			jButtonQueryDown.setIcon(BundleHelper.getImageIcon("ArrowDown.png"));
			jButtonQueryDown.setToolTipText("Move selected query down");
			jButtonQueryDown.setPreferredSize(new Dimension(26, 26));
			jButtonQueryDown.addActionListener(this);
		}
		return jButtonQueryDown;
	}
	
	
	private JButton getJButtonQueryCheck() {
		if (jButtonQueryCheck == null) {
			jButtonQueryCheck = new JButton();
			jButtonQueryCheck.setIcon(BundleHelper.getImageIcon("MBcheckRed.png"));
			jButtonQueryCheck.setToolTipText("Check current SQL statement");
			jButtonQueryCheck.setPreferredSize(new Dimension(26, 26));
			jButtonQueryCheck.addActionListener(this);
		}
		return jButtonQueryCheck;
	}
	private JButton getJButtonShowTable() {
		if (jButtonShowTable == null) {
			jButtonShowTable = new JButton();
			jButtonShowTable.setIcon(BundleHelper.getImageIcon("MBtable.png"));
			jButtonShowTable.setToolTipText("Show data table");
			jButtonShowTable.setPreferredSize(new Dimension(26, 26));
			jButtonShowTable.addActionListener(this);
		}
		return jButtonShowTable;
	}
	
	private JLabel getJLabelNo() {
		if (jLabelNo == null) {
			jLabelNo = new JLabel("Number:");
			jLabelNo.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelNo;
	}
	private JTextField getJTextFieldNo() {
		if (jTextFieldNo == null) {
			jTextFieldNo = new JTextField();
			jTextFieldNo.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldNo.setPreferredSize(new Dimension(60, 26));
			jTextFieldNo.setEditable(false);
		}
		return jTextFieldNo;
	}
	private JLabel getJLabelName() {
		if (jLabelName == null) {
			jLabelName = new JLabel("Name:");
			jLabelName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelName;
	}
	private JTextField getJTextFieldName() {
		if (jTextFieldName == null) {
			jTextFieldName = new JTextField();
			jTextFieldName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldName.setPreferredSize(new Dimension(240, 26) );
			jTextFieldName.getDocument().addDocumentListener(this.getDocumentListenerForEditState());
		}
		return jTextFieldName;
	}
	private JLabel getJLabelSQLHeader() {
		if (jLabelSQLHeader == null) {
			jLabelSQLHeader = new JLabel("SQL-Statement:");
			jLabelSQLHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSQLHeader;
	}
	private JTextArea getJTextAreaSqlStatement() {
		if (jTextAreaSqlStatement == null) {
			jTextAreaSqlStatement = new JTextArea();
			jTextAreaSqlStatement.setPreferredSize(new Dimension(200, 78));
			jTextAreaSqlStatement.setFont(new Font("Monospaced", Font.PLAIN, 12));
			jTextAreaSqlStatement.getDocument().addDocumentListener(this.getDocumentListenerForEditState());
		}
		return jTextAreaSqlStatement;
	}
	
	/**
	 * Returns a DocumentListener that reacts on changes by setting a new current EditState
	 * @return the document listener for edit state
	 */
	private DocumentListener getDocumentListenerForEditState() {
		
		return new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				this.updateEditState();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				this.updateEditState();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				this.updateEditState();
			}
			private void updateEditState() {
				if (JPanelQueryConfiguration.this.isPauserEditStateDocumentListener==true) return;
				if (JPanelQueryConfiguration.this.getEditState()!=EditState.EditNewEntryUnsaved) {
					JPanelQueryConfiguration.this.setEditState(EditState.EditKnownEntryUnsaved);
				}
			}
		};
	}
	
	/**
	 * Sets the database query, currently edited.
	 * @param dbQuery the new database query
	 */
	private void setDatabaseQuery(DatabaseQuery dbQuery) {
		
		boolean isEnabledEditing = (dbQuery!=null);

		this.isPauserEditStateDocumentListener = true;
		if (isEnabledEditing==false) {
			this.getJTextFieldNo().setText("");
			this.getJTextFieldName().setText("");
			this.getJTextAreaSqlStatement().setText("");
		} else {
			this.getJTextFieldNo().setText(dbQuery.getNumber() + "");
			this.getJTextFieldName().setText(dbQuery.getName());
			this.getJTextAreaSqlStatement().setText(dbQuery.getSqlStatement());	
		}
		this.isPauserEditStateDocumentListener = false;
		
		this.getJTextFieldName().setEditable(isEnabledEditing);
		this.getJTextAreaSqlStatement().setEditable(isEnabledEditing);
		
		this.databaseQueryEdit = dbQuery;
	}
	/**
	 * Returns the DatabaseQuery currently edited.
	 * @return the database query
	 */
	private DatabaseQuery getDatabaseQuery() {
		if (this.databaseQueryEdit!=null) {
			this.databaseQueryEdit.setNumber(NumberHelper.parseInteger(this.getJTextFieldNo().getText()));
			this.databaseQueryEdit.setName(this.getJTextFieldName().getText());
			this.databaseQueryEdit.setSqlStatement(this.getJTextAreaSqlStatement().getText());
		}
		return databaseQueryEdit;
	}
	
	/**
	 * Sets the edits the state.
	 * @param editState the new edits the state
	 */
	private void setEditState(EditState editState) {
		
		if (editState!=null && editState==this.editState) return;
		
		this.editState = editState;
		switch (this.editState) {
		case NoEditing:
			this.getJButtonQueryAdd().setEnabled(true);
			this.getJButtonQuerySave().setEnabled(false);
			this.getJButtonQueryRemove().setEnabled(false);
			this.getJButtonQueryUp().setEnabled(false);
			this.getJButtonQueryDown().setEnabled(false);
			this.getJButtonQueryCheck().setEnabled(false);
			this.getJButtonShowTable().setEnabled(false);
			break;
			
		case EditNewEntryUnsaved:
			this.getJButtonQueryAdd().setEnabled(false);
			this.getJButtonQuerySave().setEnabled(true);
			this.getJButtonQueryRemove().setEnabled(true);
			this.getJButtonQueryUp().setEnabled(false);
			this.getJButtonQueryDown().setEnabled(false);
			this.getJButtonQueryCheck().setEnabled(true);
			this.getJButtonShowTable().setEnabled(true);
			break;
			
		case EditKnownEntry:
			this.getJButtonQueryAdd().setEnabled(true);
			this.getJButtonQuerySave().setEnabled(false);
			this.getJButtonQueryRemove().setEnabled(true);
			this.getJButtonQueryUp().setEnabled(true);
			this.getJButtonQueryDown().setEnabled(true);
			this.getJButtonQueryCheck().setEnabled(true);
			this.getJButtonShowTable().setEnabled(true);
			break;
			
		case EditKnownEntryUnsaved:
			this.getJButtonQueryAdd().setEnabled(true);
			this.getJButtonQuerySave().setEnabled(true);
			this.getJButtonQueryRemove().setEnabled(true);
			this.getJButtonQueryUp().setEnabled(true);
			this.getJButtonQueryDown().setEnabled(true);
			this.getJButtonQueryCheck().setEnabled(true);
			this.getJButtonShowTable().setEnabled(true);
			break;
		}
	}
	/**
	 * Returns the current {@link EditState}.
	 * @return the edits the state
	 */
	private EditState getEditState() {
		return editState;
	}
	/**
	 * Checks if the {@link EditState} is in an unsaved state.
	 * @return true, if is in unsaved state
	 */
	private boolean isInUnsavedState() {
		return this.getEditState()==EditState.EditKnownEntryUnsaved || this.getEditState()==EditState.EditNewEntryUnsaved;
	}
	
	/**
	 * Checks if a focus change to another DatabaseQuery may continue 
	 * by considering unsaved states and asking the user what to do.
	 * @return true, if is continue focus change of database query
	 */
	private boolean isContinueFocusChangeOfDatabaseQuery() {
		
		boolean isContinue = true;
		if (this.isInUnsavedState()==true) {
			Window owner = OwnerDetection.getOwnerWindowForComponent(this);
			String message = "The database query " + this.getDatabaseQuery().getNumber() + ": '" + this.getDatabaseQuery().getName() + "' has unsaved changes.\n";
			message += "Would you like to save this changes?"; 
			int answer = AwbMessageDialog.showConfirmDialog(owner, message, "Save current Query?", AwbMessageDialog.YES_NO_CANCEL_OPTION);
			if (answer==AwbMessageDialog.YES_OPTION) {
				this.saveDatabaseQuery(false);
				isContinue = true;
			} else if (answer==AwbMessageDialog.NO_OPTION) {
				isContinue = true;
			} else if (answer==AwbMessageDialog.CANCEL_OPTION) {
				isContinue = false;
			}
		}
		return isContinue;
	}
	
	/**
	 * Creates a new DatabaseQuery.
	 * @return the DatabaseQuery
	 */
	private DatabaseQuery createNewDatabaseQuery() {
		DatabaseQuery dbq = new DatabaseQuery();
		dbq.setNumber(this.getTableModelDatabaseQuery().getRowCount() + 1);
		dbq.setName("New database query");
		dbq.setSqlStatement("SELECT * FROM 'MyTable' ORDER BY 'dataColum';");
		return dbq;
	}
	
	/**
	 * Saves the current DatabaseQuery.
	 * @param isChangeTableFocus the indicator to change the table focus or not
	 */
	private void saveDatabaseQuery(boolean isChangeTableFocus) {
		
		// --- Save the current query settings ----------------------
		DatabaseQuery dbQueryToSave = this.getDatabaseQuery();
		switch (this.getEditState()) {
		case EditNewEntryUnsaved:
			this.addDatabaseQueryToTable(dbQueryToSave);
			this.addDatabaseQueryToDataSource(dbQueryToSave);
			this.setEditState(EditState.EditKnownEntry);
			if (isChangeTableFocus==true) {
				this.selectDataBaseQueryInTable(dbQueryToSave);
			}
			break;
		default:
			this.updateDatabaseQueryInTable(dbQueryToSave);
			this.saveDatabaseQueryList();
			this.setEditState(EditState.EditKnownEntry);
			break;
		}
		
		// --- As next check or create tree node for the query ------  
		
		
	}
	
	/**
	 * Moves the currently selected DatabaseQuery in the specified direction.
	 * @param direction the direction
	 */
	private void moveDatabaseQuery(int direction) {
		
		DatabaseQuery dbQueryToMove = this.getDatabaseQuery();
		int prevNumber = dbQueryToMove.getNumber();
		int newNumber = prevNumber + direction;
		
		if (newNumber<1 || newNumber>this.getDatabaseQueryList().size()) return;
		
		DatabaseQuery dbQueryToExchange = (DatabaseQuery) this.getTableModelDatabaseQuery().getValueAt(newNumber-1, 0);
		
		// --- Assign new numbers -----------------------------------
		dbQueryToMove.setNumber(newNumber);
		dbQueryToExchange.setNumber(prevNumber);

		this.updateDatabaseQueryInTable(dbQueryToMove);
		this.updateDatabaseQueryInTable(dbQueryToExchange);
		
		// --- Change position in table model -----------------------
		this.getTableModelDatabaseQuery().removeRow(prevNumber-1);
		this.addDatabaseQueryToTable(dbQueryToMove, newNumber-1);
		
		// --- Set new selected row ---------------------------------
		int newSelection = this.getJTableDatabaseQueries().convertRowIndexToView(newNumber-1);
		this.getJTableDatabaseQueries().getSelectionModel().setSelectionInterval(newSelection, newSelection);
		
		// --- Reorder the list of DatabaseQueries in the model -----
		Collections.sort(this.getDatabaseQueryList());
		this.databaseDataSourceIntegration.getDataSource().updateSubConfigurations();
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonQueryAdd()) {
			// --- Create a new, unsaved DatabaseQuery --------------
			this.getJTableDatabaseQueries().clearSelection();
			this.getJTextFieldName().requestFocusInWindow();
			this.setDatabaseQuery(this.createNewDatabaseQuery());
			this.setEditState(EditState.EditNewEntryUnsaved);
			
		} else if (ae.getSource()==this.getJButtonQuerySave()) {
			// --- Save the current query settings ------------------
			this.saveDatabaseQuery(true);
			
		} else if (ae.getSource()==this.getJButtonQueryRemove()) {
			// --- Remove the currently shown Query -----------------
			if (this.getEditState()!=EditState.EditNewEntryUnsaved) {
				Window owner = OwnerDetection.getOwnerWindowForComponent(this);
				String message = "Delete database query '" + this.getDatabaseQuery().getName() + "'?";
				int answer = AwbMessageDialog.showConfirmDialog(owner, message, "Delete Query?", AwbMessageDialog.OK_CANCEL_OPTION);
				if (answer==AwbMessageDialog.CANCEL_OPTION) return;
			}
			int newIdxSelection = this.removeDatabaseQueryFromTable(this.getDatabaseQuery()) - 1;
			if (this.getTableModelDatabaseQuery().getRowCount()==0) {
				this.setDatabaseQuery(null);
				this.setEditState(EditState.NoEditing);
			} else {
				newIdxSelection = newIdxSelection<0 ? newIdxSelection=0 : newIdxSelection;
				int newIdxSelectionTable = this.getJTableDatabaseQueries().convertColumnIndexToView(newIdxSelection);
				this.getJTableDatabaseQueries().getSelectionModel().setSelectionInterval(newIdxSelectionTable, newIdxSelectionTable);
			}
			
		} else if (ae.getSource()==this.getJButtonQueryUp()) {
			// --- Move DatabaseQuery up ----------------------------
			this.moveDatabaseQuery(-1);
		} else if (ae.getSource()==this.getJButtonQueryDown()) {
			// --- Move DatabaseQuery down --------------------------
			this.moveDatabaseQuery(+1);
			
		} else if (ae.getSource()==this.getJButtonQueryCheck()) {
			// --- Check if the SQL statement is valid --------------
			
			
		} else if (ae.getSource()==this.getJButtonShowTable()) {
			// --- Will set the focus to the selected table --------- 
			
			
		}
	}
	
	
	
	
}
