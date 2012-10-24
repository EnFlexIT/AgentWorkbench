package agentgui.core.charts.xyChart.gui;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Language;
import agentgui.core.charts.gui.FloatInputDialog;
import agentgui.core.charts.gui.KeyInputDialog;
import agentgui.core.charts.gui.TableCellEditor4FloatObject;
import agentgui.core.charts.gui.TableTab;
import agentgui.core.charts.xyChart.XyDataModel;

public class XyTableTab extends TableTab {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -5737806366707646814L;
	
	public XyTableTab(XyDataModel model){
		this.model = model;
		initialize();
	}

	@Override
	protected JTable getTable() {
		if(table == null){
			table = new JTable(){

				/**
				 * Generated serialVersionUID
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					// Same cell editor for all columns
					return new TableCellEditor4FloatObject();
				}
			};
		}
		
		table.setModel(model.getTableModel());
		table.getSelectionModel().addListSelectionListener(this);
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		table.setRowSorter(sorter);
		
		return table;
	}

	@Override
	protected KeyInputDialog getKeyInputDialog(String title) {
		return new FloatInputDialog(SwingUtilities.getWindowAncestor(this), title, Language.translate("X Wert"));
	}

}
