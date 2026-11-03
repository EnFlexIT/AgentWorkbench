package de.enflexit.df.core.processing.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.JWindow;

import de.enflexit.common.swing.CheckBoxList;
import de.enflexit.common.swing.CheckBoxListSelectionEvent;
import de.enflexit.common.swing.CheckBoxListSelectionListener;
import de.enflexit.df.core.charts.TableSawChartHelper;
import de.enflexit.df.core.charts.TableSawChartViewPanel;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

/**
 * The Class DataColumnSelectionWindow.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataColumnSelectionWindow extends JWindow implements ActionListener, CheckBoxListSelectionListener<String> {
	
	private static final long serialVersionUID = -6480818362743194067L;
	private JLabel jLabelHeader;
	private JLabel jLabelSearch;
	private JTextField jTextField;
	private JButton jButtonSearch;
	private JScrollPane jScrollPaneColumnsList;
	private CheckBoxList<String> jListColumnSelection;
	private DefaultListModel<String> columnNamesListModel;
	private JPanel jPanelButtons;
	private JButton jButtonSelectAll;
	private JButton jButtonClearAll;
	
	private TableSawChartViewPanel parentPanel;
	
	private boolean pauseListSelectionListener;
	
	/**
	 * Instantiates a new data column selection window.
	 * @param parentPanel the parent panel
	 */
	public DataColumnSelectionWindow(Window owner, TableSawChartViewPanel parentPanel) {
		super(owner);
		this.parentPanel = parentPanel;
		this.initialize();
		this.setColumnNamesToListModel(this.parentPanel.getTable());
	}
	
	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.gridwidth = 2;
		gbc_jLabelHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jLabelSearch = new GridBagConstraints();
		gbc_jLabelSearch.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelSearch.anchor = GridBagConstraints.EAST;
		gbc_jLabelSearch.gridx = 0;
		gbc_jLabelSearch.gridy = 1;
		getContentPane().add(getJLabelSearch(), gbc_jLabelSearch);
		GridBagConstraints gbc_jTextField = new GridBagConstraints();
		gbc_jTextField.insets = new Insets(0, 0, 5, 5);
		gbc_jTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextField.gridx = 1;
		gbc_jTextField.gridy = 1;
		getContentPane().add(getJTextField(), gbc_jTextField);
		GridBagConstraints gbc_jButtonSearch = new GridBagConstraints();
		gbc_jButtonSearch.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonSearch.gridx = 2;
		gbc_jButtonSearch.gridy = 1;
		getContentPane().add(getJButtonSearch(), gbc_jButtonSearch);
		GridBagConstraints gbc_jScrollPaneColumnsList = new GridBagConstraints();
		gbc_jScrollPaneColumnsList.insets = new Insets(0, 0, 5, 0);
		gbc_jScrollPaneColumnsList.gridwidth = 3;
		gbc_jScrollPaneColumnsList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneColumnsList.gridx = 0;
		gbc_jScrollPaneColumnsList.gridy = 2;
		getContentPane().add(getJScrollPaneColumnsList(), gbc_jScrollPaneColumnsList);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.gridwidth = 3;
		gbc_jPanelButtons.fill = GridBagConstraints.BOTH;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 3;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
		
		this.setSize(300, 200);
		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				setVisible(false);
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// --- Nothing to do here ---
			}
		});
		this.setVisible(true);
		this.requestFocus();
	}


	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Data Column Selection");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JLabel getJLabelSearch() {
		if (jLabelSearch == null) {
			jLabelSearch = new JLabel("Search");
			jLabelSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSearch;
	}
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setText("Under Construction...");
			jTextField.setColumns(10);
		}
		return jTextField;
	}
	private JButton getJButtonSearch() {
		if (jButtonSearch == null) {
			jButtonSearch = new JButton("Search");
		}
		return jButtonSearch;
	}
	private JScrollPane getJScrollPaneColumnsList() {
		if (jScrollPaneColumnsList == null) {
			jScrollPaneColumnsList = new JScrollPane();
			jScrollPaneColumnsList.setViewportView(getJListColumnSelection());
		}
		return jScrollPaneColumnsList;
	}
	private CheckBoxList<String> getJListColumnSelection() {
		if (jListColumnSelection == null) {
			jListColumnSelection = new CheckBoxList<String>();
			jListColumnSelection.setModel(this.getColumnNamesListModel());
			jListColumnSelection.addCheckBoxListSelectionListener(this);
		}
		return jListColumnSelection;
	}
	private DefaultListModel<String> getColumnNamesListModel() {
		if (columnNamesListModel==null) {
			columnNamesListModel = new DefaultListModel<String>();
		}
		return columnNamesListModel;
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
			GridBagConstraints gbc_jButtonSelectAll = new GridBagConstraints();
			gbc_jButtonSelectAll.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonSelectAll.gridx = 0;
			gbc_jButtonSelectAll.gridy = 0;
			jPanelButtons.add(getJButtonSelectAll(), gbc_jButtonSelectAll);
			GridBagConstraints gbc_jButtonClearAll = new GridBagConstraints();
			gbc_jButtonClearAll.gridx = 1;
			gbc_jButtonClearAll.gridy = 0;
			jPanelButtons.add(getJButtonClearAll(), gbc_jButtonClearAll);
		}
		return jPanelButtons;
	}
	private JButton getJButtonSelectAll() {
		if (jButtonSelectAll == null) {
			jButtonSelectAll = new JButton("Select All");
			jButtonSelectAll.addActionListener(this);
		}
		return jButtonSelectAll;
	}
	private JButton getJButtonClearAll() {
		if (jButtonClearAll == null) {
			jButtonClearAll = new JButton("Clear All");
			jButtonClearAll.addActionListener(this);
		}
		return jButtonClearAll;
	}
	
	
	/**
	 * Sets the column names to list model.
	 * @param table the new column names to list model
	 */
	private void setColumnNamesToListModel(Table table) {
		this.pauseListSelectionListener = true;
		this.getColumnNamesListModel().clear();
		this.getJListColumnSelection().clearSelection();
		ArrayList<String> columnNames = new ArrayList<String>();
		for (Column<?> column : table.columns()) {
			if (TableSawChartHelper.isTemporalColumn(column)==false) {
				columnNames.add(column.name());
			}
		}
		this.getColumnNamesListModel().addAll(columnNames);
		this.selectAllListEntries();
		this.pauseListSelectionListener = false;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonSelectAll()) {
			this.selectAllListEntries();
			this.updateVisibilityState();
		} else if (ae.getSource()==this.getJButtonClearAll()) {
			this.getJListColumnSelection().clearSelection();
			this.updateVisibilityState();
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.ui.CheckBoxListSelectionListener#selectionChanged(de.enflexit.df.core.processing.ui.CheckBoxListSelectionEvent)
	 */
	@Override
	public void selectionChanged(CheckBoxListSelectionEvent<String> checkboxListSelectionEvent) {
		if (this.pauseListSelectionListener==false) {
			String seriesName = checkboxListSelectionEvent.getItem();
			boolean visible = checkboxListSelectionEvent.isSelected();
			this.parentPanel.setSeriesVisible(seriesName, visible);
		}
	}
	
	/**
	 * Select all list entries.
	 */
	private void selectAllListEntries() {
		this.getJListColumnSelection().getSelectionModel().setSelectionInterval(0, this.getJListColumnSelection().getModel().getSize()-1);
	}
	
	private void updateVisibilityState() {
		for (int i=0; i<this.getColumnNamesListModel().size(); i++) {
			String name = this.getColumnNamesListModel().getElementAt(i);
			boolean selected = this.getJListColumnSelection().isSelectedIndex(i);
			this.parentPanel.setSeriesVisible(name, selected);
		}
	}
}
