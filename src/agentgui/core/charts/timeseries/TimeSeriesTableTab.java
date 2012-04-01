package agentgui.core.charts.timeseries;

import jade.util.leap.List;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.JButton;

import agentgui.core.application.Language;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * This class implements a panel showing the time series data as a table.
 * Editing functionality comming soon
 * @author Nils
 *
 */
public class TimeSeriesTableTab extends JPanel implements ActionListener, Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5533838905866978397L;
	
	private JTable table = null;
	
	private TimeSeriesDataModel model = null;
	private JButton btnOk;
	private JButton btnAdd;
	private JButton btnRemove;
	
	public TimeSeriesTableTab(TimeSeriesDataModel model){
		this.model = model;
		initialize();
	}
	
	/**
	 * This method initializes the panel
	 */
	private void initialize(){
		table = new TimeSeriesJTable(model.getTableModel());	// Private class specifying CellRenderers and CellEditors, Definition below
		
		table.setAutoCreateRowSorter(true);
		
		model.addObserver(this);

		setLayout(new GridBagLayout());
		GridBagConstraints gbcTable = new GridBagConstraints();
		gbcTable.weighty = 1.0;
		gbcTable.weightx = 1.0;
		gbcTable.gridheight = 3;
		gbcTable.insets = new Insets(0, 0, 0, 5);
		gbcTable.gridx = 0;
		gbcTable.gridy = 0;
		gbcTable.fill = GridBagConstraints.BOTH;
		add(new JScrollPane(table), gbcTable);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 0;
		add(getBtnAdd(), gbc_btnAdd);
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove.gridx = 1;
		gbc_btnRemove.gridy = 1;
		add(getBtnRemove(), gbc_btnRemove);
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.SOUTH;
		gbc_btnOk.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOk.gridx = 1;
		gbc_btnOk.gridy = 2;
		add(getBtnOk(), gbc_btnOk);
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnOk){
			
			// Trigger rebuild of ontology and chart model 
			model.rebuildFromTable();
			
		}else if(e.getSource() == btnAdd){
			Float[] emptyRow = new Float[model.getTableModel().getColumnCount()];
			emptyRow[0] = new Float(new Date().getTime());
			model.getTableModel().addRow(new Float[] {new Float(0), new Float(0)});
		}else if(e.getSource() == btnRemove){
			int rowIndex = table.getSelectedRow();
			if(rowIndex >= 0){
				model.getTableModel().removeRow(rowIndex);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == this.model && (Integer) arg == TimeSeriesDataModel.TIME_SERIES_ADDED){
			table.setModel(model.getTableModel());
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model.getTableModel());
			sorter.setComparator(0, new Comparator<Float>() {

				@Override
				public int compare(Float o1, Float o2) {
					return o1.compareTo(o2);
				}
			});
			repaint();
		}else if(o == this.model && (Integer) arg == TimeSeriesDataModel.SETTINGS_CHANGED){
			table.getColumnModel().getColumn(0).setHeaderValue(model.getxAxisLabel());
			List labels = model.getOntologyModel().getValueAxisDescriptions();
			for(int i=1; i<table.getColumnCount(); i++){
				table.getColumnModel().getColumn(i).setHeaderValue(labels.get(i-1));
			}
		}
	}
	
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton(Language.translate("Übernehmen"));
			btnOk.addActionListener(this);
		}
		return btnOk;
	}

	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton(Language.translate("Hinzufügen"));
			btnAdd.addActionListener(this);
		}
		return btnAdd;
	}

	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton(Language.translate("Entfernen"));
			btnRemove.addActionListener(this);
		}
		return btnRemove;
	}

	private class TimeSeriesJTable extends JTable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6416669054997825823L;

		public TimeSeriesJTable(DefaultTableModel tableModel) {
			super(tableModel);
		}

		/* (non-Javadoc)
		 * Special cell renderer for first column, default for the others
		 */
		@Override
		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column == 0){
				return new TableCellRenderer4Time();
			}else{
				return super.getCellRenderer(row, column);
			}
		}

		/* (non-Javadoc)
		 * Calendar based cell editor for the first column, default but Float returning for the others
		 */
		@Override
		public TableCellEditor getCellEditor(int row, int column) {
			if(column == 0){
				return new TableCellEditor4Time();
			}else{
				return new TableCellEditor4FloatObject();
			}
		}
	}
}
