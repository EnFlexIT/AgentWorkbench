package de.enflexit.awb.ws.ui.server;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.enflexit.awb.ws.core.JettyAttribute;
import de.enflexit.awb.ws.core.JettyConfiguration;

/**
 * The Class JTableSettingsServer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JTableSettingsServer extends JTable {

	private static final long serialVersionUID = 2703332201281221367L;

	private JettyConfiguration jettyConfiguration;
	private DefaultTableModel tableModel;
	
	
	/**
	 * Instantiates a new JTableSettingsServer.
	 */
	public JTableSettingsServer() {
		this.initialize();
	}
	private void initialize() {
		
		this.setModel(this.getTableModel());
		
		this.setFillsViewportHeight(true);
		this.setShowGrid(false);
		this.setRowHeight(20);
		this.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		
		// --- Set the renderer / editor to column no. 2 ------------
		JTableSettingsServerCellRenderEditor renderEditor = new JTableSettingsServerCellRenderEditor();
		TableColumn tc = this.getColumnModel().getColumn(1);
		tc.setCellRenderer(renderEditor);
		tc.setCellEditor(renderEditor);
	}

	/**
	 * Gets the table model.
	 * @return the table model
	 */
	private DefaultTableModel getTableModel() {
		if (tableModel==null) {
			
			Vector<String> tableHeader = new Vector<>();
			tableHeader.add("Parameter");
			tableHeader.add("Value");
			
			tableModel = new DefaultTableModel(null, tableHeader) {
				private static final long serialVersionUID = -2690121123169598183L;
				@Override
				public boolean isCellEditable(int row, int column) {
					switch (column) {
					case 0:
						return false;
					}
					return true;
				}
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					switch (columnIndex) {
					case 0:
						return String.class;
					case 1:
						return JettyAttribute.class;
					}
					return null;
				}
			};
		}
		return tableModel;
	}
	/**
	 * Fills the table model.
	 */
	private void fillTableModel() {
		// --- Clear table model --------------------------
		this.getTableModel().setRowCount(0);
		if (this.getJettyConfiguration()==null) return;
		// --- Fill each list element into table model ----
		this.getJettyConfiguration().getJettySettingsSorted().forEach((JettyAttribute<?> ja) -> this.addToTableModel(ja));
	}
	/**
	 * Adds the specified JettyAttribute to the table model.
	 * @param jettyAttribute the jetty attribute
	 */
	private void addToTableModel(JettyAttribute<?> jettyAttribute) {
		Vector<Object> row = new Vector<>();
		row.add(jettyAttribute.getKey());
		row.add(jettyAttribute);
		this.getTableModel().addRow(row);
	}
	
	/**
	 * Sets the jetty configuration.
	 * @param jettyConfiguration the new jetty configuration
	 */
	public void setJettyConfiguration(JettyConfiguration jettyConfiguration) {
		this.jettyConfiguration = jettyConfiguration;
		this.fillTableModel();
	}
	/**
	 * Returns the jetty configuration.
	 * @return the jetty configuration
	 */
	public JettyConfiguration getJettyConfiguration() {
		return jettyConfiguration;
	}
	
}
