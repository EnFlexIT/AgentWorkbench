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
package agentgui.simulationService.load.threading;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * The Class ThreadMeasureProtocolTab.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMeasureProtocolTab extends JPanel {

	private static final long serialVersionUID = -7315494195421538651L;

	private ThreadProtocolVector threadProtocolVector;
	
	private JLabel JLabelHeader;
	
	/**
	 * Instantiates a new thread measure protocol tab.
	 *
	 * @param threadProtocolVector the thread protocol vector
	 */
	public ThreadMeasureProtocolTab(ThreadProtocolVector threadProtocolVector) {
		this.threadProtocolVector = threadProtocolVector;
		initialize();

	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{33, 335, 25, 15, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_JLabelHeader = new GridBagConstraints();
		gbc_JLabelHeader.anchor = GridBagConstraints.NORTHWEST;
		gbc_JLabelHeader.insets = new Insets(0, 0, 0, 5);
		gbc_JLabelHeader.gridx = 0;
		gbc_JLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_JLabelHeader);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		JTable table = new JTable(threadProtocolVector.getTableModel());
		
		table.setPreferredScrollableViewportSize(new Dimension(600, 500));
		table.setFillsViewportHeight(true);
		//set column min-width
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setMinWidth(200);
		
		// Sort Threads - top most user time first
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 3; //User Time
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.DESCENDING));
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		sorter.setSortsOnUpdates(true);
		sorter.setSortKeys(sortKeys);
		table.setRowSorter(sorter);
		
		scrollPane.setViewportView(table);
	}
	
	/**
	 * Gets the j label header.
	 *
	 * @return the j label header
	 */
	private JLabel getJLabelHeader() {
		if (JLabelHeader == null) {
			JLabelHeader = new JLabel("Thread Times");
		}
		return JLabelHeader;
	}
}
