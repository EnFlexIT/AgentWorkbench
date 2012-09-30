package agentgui.core.charts.timeseriesChart.gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JToolBar;
import java.awt.GridBagConstraints;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.core.charts.timeseriesChart.TimeSeriesTableModel;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesValuePair;

import javax.swing.JButton;
import javax.swing.JSeparator;

public class TimeSeriesTableTab extends JPanel implements ActionListener, ListSelectionListener{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 9156505881049717567L;
	
	private final String PathImage = Application.getGlobalInfo().PathImageIntern();
	
	private JScrollPane scrollPane;
	
	private JTable table;
	
	private TimeSeriesDataModel model;
	private JToolBar toolBar;
	private JButton btnAddRow;
	private JButton btnAddColumn;
	private JButton btnRemoveRow;
	private JButton btnRemoveColumn;
	private JSeparator separator;
	
	
	
	/**
	 * Create the panel.
	 */
	public TimeSeriesTableTab(TimeSeriesDataModel model) {
		
		this.model = model;
		
		getTable().setModel(this.model.getTableModel());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(getScrollPane(), gbc_scrollPane);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.NORTH;
		gbc_toolBar.gridx = 1;
		gbc_toolBar.gridy = 0;
		add(getToolBar(), gbc_toolBar);

	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable(){

				/**
				 * 
				 */
				private static final long serialVersionUID = 4259474557660027403L;

				/* (non-Javadoc)
				 * @see javax.swing.JTable#getCellEditor(int, int)
				 */
				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					if(column == 0){
						return new TableCellEditor4Time();
					}else{
						return new TableCellEditor4FloatObject();
					}
				}

				/* (non-Javadoc)
				 * @see javax.swing.JTable#getCellRenderer(int, int)
				 */
				@Override
				public TableCellRenderer getCellRenderer(int row, int column) {
					if(column == 0){
						return new TableCellRenderer4Time();
					}else{
						return super.getCellRenderer(row, column);
					}
				}
				
			};
			table.setModel(model.getTableModel());
			table.getSelectionModel().addListSelectionListener(this);
			final TableRowSorter<TimeSeriesTableModel> sorter = new TableRowSorter<TimeSeriesTableModel>(model.getTableModel());
			table.setRowSorter(sorter);
			
		}
		return table;
	}
	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setOrientation(SwingConstants.VERTICAL);
			toolBar.add(getBtnAddRow());
			toolBar.add(getBtnAddColumn());
			toolBar.add(getSeparator());
			toolBar.add(getBtnRemoveRow());
			toolBar.add(getBtnRemoveColumn());
		}
		return toolBar;
	}
	private JButton getBtnAddRow() {
		if (btnAddRow == null) {
			btnAddRow = new JButton();
			btnAddRow.setIcon(new ImageIcon(this.getClass().getResource( PathImage + "AddRow.png")));
			btnAddRow.setToolTipText(Language.translate("Neuen Zeitpunkt hinzufügen"));
			btnAddRow.addActionListener(this);
		}
		return btnAddRow;
	}
	private JButton getBtnAddColumn() {
		if (btnAddColumn == null) {
			btnAddColumn = new JButton();
			btnAddColumn.setIcon(new ImageIcon(this.getClass().getResource( PathImage + "AddCol.png")));
			btnAddColumn.setToolTipText(Language.translate("Neue Zeitreihe hinzufügen"));
			btnAddColumn.addActionListener(this);
		}
		return btnAddColumn;
	}
	private JButton getBtnRemoveRow() {
		if (btnRemoveRow == null) {
			btnRemoveRow = new JButton();
			btnRemoveRow.setIcon(new ImageIcon(this.getClass().getResource( PathImage + "RemoveRow.png")));
			btnRemoveRow.setToolTipText(Language.translate("Markierten Zeitpunkt entfernen"));
			btnRemoveRow.setEnabled(false);
			btnRemoveRow.addActionListener(this);
		}
		return btnRemoveRow;
	}
	private JButton getBtnRemoveColumn() {
		if (btnRemoveColumn == null) {
			btnRemoveColumn = new JButton();
			btnRemoveColumn.setIcon(new ImageIcon(this.getClass().getResource( PathImage + "RemoveCol.png")));
			btnRemoveColumn.setToolTipText(Language.translate("Markierte Zeitreihe Entfernen"));
			btnRemoveColumn.setEnabled(false);
			btnRemoveColumn.addActionListener(this);
		}
		return btnRemoveColumn;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getBtnAddColumn()){
			String seriesLabel = JOptionPane.showInputDialog(this, Language.translate("Label"), Language.translate("Neue Zeitreihe"), JOptionPane.QUESTION_MESSAGE);
			TimeSeries newSeries = new TimeSeries();
			newSeries.setLabel(seriesLabel);
			
			// As JFreeChart doesn't work with empty time series, one value pair must be added 
			TimeSeriesValuePair initialValuePair = new TimeSeriesValuePair();
			Simple_Long timeStamp = new Simple_Long();
			timeStamp.setLongValue((Long) model.getTableModel().getValueAt(0, 0));
			Simple_Float value = new Simple_Float();
			value.setFloatValue(0.0f);
			initialValuePair.setTimestamp(timeStamp);
			initialValuePair.setValue(value);
			newSeries.addTimeSeriesValuePairs(initialValuePair);
			
			model.addSeries(newSeries);
		}else if(e.getSource() == getBtnAddRow()){
			TimeInputDialog dialog = new TimeInputDialog(SwingUtilities.getWindowAncestor(this), Language.translate("Neuer Eintrag"), Language.translate("Zeit:"));
			if(! dialog.isCanceled()){
				long timestamp = dialog.getTimestamp();
				model.getTableModel().addEmptyRow(timestamp);
			}
		}else if(e.getSource() == getBtnRemoveColumn()){
			if(table.getSelectedColumn() > 0){
				int seriesIndex = table.getSelectedColumn()-1;
				
				try {
					model.removeSeries(seriesIndex);
				} catch (NoSuchSeriesException e1) {
					System.err.println("Error removing series "+seriesIndex);
					e1.printStackTrace();
				}
			}
		}else if(e.getSource() == getBtnRemoveRow()){
			long timestamp = (Long) table.getValueAt(table.getSelectedRow(), 0);
			model.removeValuePairsFromAllSeries(timestamp);
			getBtnRemoveColumn().setEnabled(false);
			getBtnRemoveRow().setEnabled(false);
		}
		
		
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Enable btnRemoveRow if a row is selected
		getBtnRemoveRow().setEnabled(table.getSelectedRow() >= 0);
		
		// Enable btnRemoveColumn if a non-timestamp column is selected
		getBtnRemoveColumn().setEnabled(table.getSelectedColumn() > 0);
	}
}
