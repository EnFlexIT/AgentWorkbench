/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.load.threading.gui;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import agentgui.simulationService.load.threading.ThreadProtocolVector;
import agentgui.simulationService.load.threading.ThreadDetail;

/**
 * The Class ThreadMonitorProtocolTableTab.
 * 
 * Displays an auto-sortable table with thread details e.g. system-time and user-time
 * Filter for agents applicable.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMonitorProtocolTableTab extends JPanel implements ActionListener {

	private static final long serialVersionUID = -7315494195421538651L;

	private ThreadProtocolVector threadProtocolVector;
	
	private JScrollPane scrollPaneTable;
	private JTable jTableThreadProtocolVector;
	private JPanel JPanelFilter;
	
	private JRadioButton jRadioButtonNoFilter;
	private JRadioButton jRadioButtonFilterAgents;
	
	/**
	 * Instantiates a new thread measure protocol tab.
	 *
	 * @param threadProtocolVector the thread protocol vector
	 */
	public ThreadMonitorProtocolTableTab(ThreadProtocolVector threadProtocolVector) {
		this.threadProtocolVector = threadProtocolVector;
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(getScrollPaneTable(), BorderLayout.CENTER);
		this.add(getJPanelFilter(), BorderLayout.SOUTH);
	}
	
	/**
	 * Gets the scroll pane table.
	 * @return the scroll pane table
	 */
	private JScrollPane getScrollPaneTable() {
		if (scrollPaneTable == null) {
			scrollPaneTable = new JScrollPane();
			scrollPaneTable.setViewportView(this.getJTableThreadProtocolVector());
		}
		return scrollPaneTable;
	}
	/**
	 * Gets the j table thread protocol vector.
	 * @return the j table thread protocol vector
	 */
	private JTable getJTableThreadProtocolVector() {
		if (jTableThreadProtocolVector == null) {

			if (threadProtocolVector==null) {
				jTableThreadProtocolVector = new JTable();
			} else {
				jTableThreadProtocolVector = new JTable(threadProtocolVector.getTableModel());
			}
			jTableThreadProtocolVector.setFillsViewportHeight(true);
			jTableThreadProtocolVector.getTableHeader().setReorderingAllowed(false);

			TableColumnModel tableModel = jTableThreadProtocolVector.getColumnModel(); 
			tableModel.getColumn(0).setMinWidth(50);
			tableModel.getColumn(0).setMaxWidth(150);
			
			tableModel.getColumn(1).setMinWidth(150);
			
			tableModel.getColumn(2).setMinWidth(150);
			
			int numberColumnWidth = 120;
			tableModel.getColumn(3).setMinWidth(numberColumnWidth);
			tableModel.getColumn(3).setMaxWidth(numberColumnWidth);
			tableModel.getColumn(4).setMinWidth(numberColumnWidth);
			tableModel.getColumn(4).setMaxWidth(numberColumnWidth);
			
			
			if (threadProtocolVector!=null) {
				// --- Add a sorter if the model is available -------
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTableThreadProtocolVector.getModel());

				List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
				//--- sort system cpu time, descending ---
				sortKeys.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));
				sorter.setSortKeys(sortKeys);
				sorter.setSortsOnUpdates(true);
				
				jTableThreadProtocolVector.setRowSorter(sorter);	
			}
		}
		return jTableThreadProtocolVector;
	}
	
	/**
	 * Gets the j panel filter.
	 * @return the j panel filter
	 */
	private JPanel getJPanelFilter() {
		if (JPanelFilter == null) {
			JPanelFilter = new JPanel();
			GridBagLayout gbl_JPanelFilter = new GridBagLayout();
			gbl_JPanelFilter.columnWidths = new int[]{0, 0, 0, 0};
			gbl_JPanelFilter.rowHeights = new int[]{23, 0};
			gbl_JPanelFilter.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_JPanelFilter.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			JPanelFilter.setLayout(gbl_JPanelFilter);
			GridBagConstraints gbc_jRadioButtonNoFilter = new GridBagConstraints();
			gbc_jRadioButtonNoFilter.insets = new Insets(5, 5, 5, 5);
			gbc_jRadioButtonNoFilter.anchor = GridBagConstraints.NORTH;
			gbc_jRadioButtonNoFilter.gridx = 0;
			gbc_jRadioButtonNoFilter.gridy = 0;
			JPanelFilter.add(getJRadioButtonNoFilter(), gbc_jRadioButtonNoFilter);
			GridBagConstraints gbc_jRadioButtonFilterAgents = new GridBagConstraints();
			gbc_jRadioButtonFilterAgents.insets = new Insets(0, 0, 0, 5);
			gbc_jRadioButtonFilterAgents.gridx = 1;
			gbc_jRadioButtonFilterAgents.gridy = 0;
			JPanelFilter.add(getJRadioButtonFilterAgents(), gbc_jRadioButtonFilterAgents);
			
			// --- Configure Button Group -----------------
			ButtonGroup bg = new ButtonGroup();
			bg.add(getJRadioButtonNoFilter());
			bg.add(getJRadioButtonFilterAgents());
			
			// --- Set default value -----------------------
			this.getJRadioButtonNoFilter().setSelected(true);
			this.getJRadioButtonFilterAgents().setSelected(false);
			
		}
		return JPanelFilter;
	}
	
	/**
	 * Gets the j radio button no filter.
	 * @return the j radio button no filter
	 */
	private JRadioButton getJRadioButtonNoFilter() {
		if (jRadioButtonNoFilter == null) {
			jRadioButtonNoFilter = new JRadioButton("No Filter");
			jRadioButtonNoFilter.setToolTipText("Display all threads.");
			jRadioButtonNoFilter.addActionListener(this);
		}
		return jRadioButtonNoFilter;
	}
	
	/**
	 * Gets the j radio button filter agents.
	 * @return the j radio button filter agents
	 */
	private JRadioButton getJRadioButtonFilterAgents() {
		if (jRadioButtonFilterAgents == null) {
			jRadioButtonFilterAgents = new JRadioButton("Filter for Agents");
			jRadioButtonFilterAgents.setToolTipText("Display agent threads only.");
			jRadioButtonFilterAgents.addActionListener(this);
		}
		return jRadioButtonFilterAgents;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		@SuppressWarnings("unchecked")
		TableRowSorter<TableModel>sorter = (TableRowSorter<TableModel>) getJTableThreadProtocolVector().getRowSorter();
		
		if (ae.getSource()==this.getJRadioButtonNoFilter()) {
			// --- Remove Filter ----------------
			sorter.setRowFilter(null);
			
		} else if (ae.getSource()==this.getJRadioButtonFilterAgents()) {
			// --- Set Filter -------------------		
			RowFilter<Object,Object> agentFilter = new RowFilter<Object, Object>() {
				
				  public boolean include(Entry<? extends Object, ? extends Object> entry) {

					  // --- get column with ThreadDetail-Instance (ThreadName) ---
					  if(entry.getValue(1) instanceof ThreadDetail) {
						  ThreadDetail tt = (ThreadDetail)entry.getValue(1);	
						  if(tt.isAgent() == true) {
							  return true;
						  }
					  }
					  return false;
				 }
			};
			sorter.setRowFilter(agentFilter);
		}
	}
}
