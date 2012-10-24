package agentgui.core.charts.timeseriesChart.gui;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Language;
import agentgui.core.charts.gui.KeyInputDialog;
import agentgui.core.charts.gui.TableCellEditor4FloatObject;
import agentgui.core.charts.gui.TableCellEditor4Time;
import agentgui.core.charts.gui.TableCellRenderer4Time;
import agentgui.core.charts.gui.TableTab;
import agentgui.core.charts.gui.TimeInputDialog;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;

public class TimeSeriesTableTab extends TableTab{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 9156505881049717567L;

	/**
	 * Create the TimeSeriesTableTab.
	 */
	public TimeSeriesTableTab(TimeSeriesDataModel model) {
		this.model = model;
		initialize();
	}
	@Override
	protected JTable getTable() {
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
			final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model.getTableModel());
			table.setRowSorter(sorter);
			
		}
		return table;
	}
	@Override
	protected KeyInputDialog getKeyInputDialog(String title) {
		return new TimeInputDialog(SwingUtilities.getWindowAncestor(this), title, Language.translate("Zeit"));
	}
}
