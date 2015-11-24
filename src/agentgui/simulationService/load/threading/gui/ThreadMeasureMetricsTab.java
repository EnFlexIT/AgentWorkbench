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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import agentgui.simulationService.load.threading.ThreadInfoStorage;
import agentgui.simulationService.load.threading.ThreadInfoStorageAgent;

import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * The Class ThreadMeasureMetricsTab.
 */
public class ThreadMeasureMetricsTab extends JPanel implements ActionListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4848470750194315559L;

	/** The thread info storage. */
	private ThreadInfoStorage threadInfoStorage;
	
	/** The scroll pane table. */
	private JScrollPane scrollPaneTable;
	
	/** The j table thread protocol vector. */
	private JTable jTableThreadInfoMetrics;
	
	/** The J panel filter. */
	private JPanel JPanelFilter;
	
	/** The j radio button no filter. */
	private JRadioButton jRadioButtonNoFilter;
	
	/** The j radio button filter agents. */
	private JRadioButton jRadioButtonFilterAgents;
	
	/** The button calculate metrics. */
	private JButton btnCalcMetrics;
	
	/** The radio button integral. */
	private JRadioButton rdbtnIntegral;
	
	/** The radio button total. */
	private JRadioButton rdbtnTotal;
	
	/** The radio button individual. */
	private JRadioButton rdbtnIndividual;
	
	/** The radio button class. */
	private JRadioButton rdbtnClass;
	
	/** The radio button average. */
	private JRadioButton rdbtnAverage;
	
	/** The J panel options. */
	private JPanel JPanelOptions;
	
	/**
	 * Instantiates a new thread measure metrics tab.
	 *
	 * @param threadInfoStorage the thread info storage
	 */
	public ThreadMeasureMetricsTab(ThreadInfoStorage threadInfoStorage) {
		this.threadInfoStorage = threadInfoStorage;
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[] {241, 30, 15, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_scrollPaneTable = new GridBagConstraints();
		gbc_scrollPaneTable.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTable.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneTable.gridx = 0;
		gbc_scrollPaneTable.gridy = 0;
		this.add(getScrollPaneTable(), gbc_scrollPaneTable);
		GridBagConstraints gbc_JPanelFilter = new GridBagConstraints();
		gbc_JPanelFilter.insets = new Insets(0, 0, 5, 0);
		gbc_JPanelFilter.anchor = GridBagConstraints.SOUTH;
		gbc_JPanelFilter.fill = GridBagConstraints.HORIZONTAL;
		gbc_JPanelFilter.gridx = 0;
		gbc_JPanelFilter.gridy = 1;
		this.add(getJPanelFilter(), gbc_JPanelFilter);
		GridBagConstraints gbc_JPanelOptions = new GridBagConstraints();
		gbc_JPanelOptions.fill = GridBagConstraints.HORIZONTAL;
		gbc_JPanelOptions.anchor = GridBagConstraints.SOUTH;
		gbc_JPanelOptions.gridx = 0;
		gbc_JPanelOptions.gridy = 2;
		add(getJPanelOptions(), gbc_JPanelOptions);
	}
	


	/**
	 * Gets the scroll pane table.
	 *
	 * @return the scroll pane table
	 */
	private JScrollPane getScrollPaneTable() {
		if (scrollPaneTable == null) {
			scrollPaneTable = new JScrollPane();
			scrollPaneTable.setViewportView(getJTableThreadInfoMetrics());
		}
		return scrollPaneTable;
	}


	
	/**
	 * Gets the j table thread info metrics.
	 *
	 * @return the j table thread info metrics
	 */
	private JTable getJTableThreadInfoMetrics() {
		if (jTableThreadInfoMetrics == null) {
			
			if (threadInfoStorage==null) {
				jTableThreadInfoMetrics = new JTable();
			} else {
				jTableThreadInfoMetrics = new JTable(threadInfoStorage.getTableModel());
			}
			jTableThreadInfoMetrics.setFillsViewportHeight(true);
			jTableThreadInfoMetrics.getColumnModel().getColumn(0).setMinWidth(50);
			jTableThreadInfoMetrics.getColumnModel().getColumn(1).setMinWidth(200);
			
			
			if (threadInfoStorage!=null) {
				// --- Add a sorter if the model is available -------
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTableThreadInfoMetrics.getModel());

				List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
				sortKeys.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));
				sorter.setSortKeys(sortKeys);
				sorter.setSortsOnUpdates(true);
				
				jTableThreadInfoMetrics.setRowSorter(sorter);	
			}
		}
		return jTableThreadInfoMetrics;
	}
	
	/**
	 * Gets the j panel filter.
	 * @return the j panel filter
	 */
	private JPanel getJPanelFilter() {
		if (JPanelFilter == null) {
			JPanelFilter = new JPanel();
			GridBagLayout gbl_JPanelFilter = new GridBagLayout();
			gbl_JPanelFilter.columnWidths = new int[] {30, 0, 0, 0, 0};
			gbl_JPanelFilter.rowHeights = new int[] {15};
			gbl_JPanelFilter.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
			gbl_JPanelFilter.rowWeights = new double[]{0.0};
			JPanelFilter.setLayout(gbl_JPanelFilter);
			GridBagConstraints gbc_jRadioButtonNoFilter = new GridBagConstraints();
			gbc_jRadioButtonNoFilter.insets = new Insets(1, 1, 5, 5);
			gbc_jRadioButtonNoFilter.gridx = 0;
			gbc_jRadioButtonNoFilter.gridy = 0;
			JPanelFilter.add(getJRadioButtonNoFilter(), gbc_jRadioButtonNoFilter);
			GridBagConstraints gbc_jRadioButtonFilterAgents = new GridBagConstraints();
			gbc_jRadioButtonFilterAgents.anchor = GridBagConstraints.WEST;
			gbc_jRadioButtonFilterAgents.insets = new Insets(1, 1, 5, 5);
			gbc_jRadioButtonFilterAgents.gridx = 1;
			gbc_jRadioButtonFilterAgents.gridy = 0;
			JPanelFilter.add(getJRadioButtonFilterAgents(), gbc_jRadioButtonFilterAgents);
			
			// --- Configure Button Group -----------------
			ButtonGroup bgFilter = new ButtonGroup();
			bgFilter.add(getJRadioButtonNoFilter());
			bgFilter.add(getJRadioButtonFilterAgents());
			
			// --- Set default value -----------------------
			getJRadioButtonNoFilter().setSelected(true);
			getJRadioButtonFilterAgents().setSelected(false);
			GridBagConstraints gbc_btnCalcMetrics = new GridBagConstraints();
			gbc_btnCalcMetrics.anchor = GridBagConstraints.EAST;
			gbc_btnCalcMetrics.insets = new Insets(1, 5, 5, 1);
			gbc_btnCalcMetrics.gridx = 4;
			gbc_btnCalcMetrics.gridy = 0;
			JPanelFilter.add(getBtnCalcMetrics(), gbc_btnCalcMetrics);
			
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
			jRadioButtonNoFilter.setVerticalAlignment(SwingConstants.BOTTOM);
			jRadioButtonNoFilter.setHorizontalAlignment(SwingConstants.CENTER);
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
			jRadioButtonFilterAgents.setVerticalAlignment(SwingConstants.BOTTOM);
			jRadioButtonFilterAgents.setHorizontalAlignment(SwingConstants.CENTER);
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
		TableRowSorter<TableModel>sorter = (TableRowSorter<TableModel>) getJTableThreadInfoMetrics().getRowSorter();
		
		if (ae.getSource()==this.getJRadioButtonNoFilter()) {
			// --- Remove Filter ----------------
			sorter.setRowFilter(null);
			
		} else if (ae.getSource()==this.getJRadioButtonFilterAgents()) {
			// --- Set Filter -------------------		
			RowFilter<Object,Object> agentFilter = new RowFilter<Object, Object>() {
				
				  public boolean include(Entry<? extends Object, ? extends Object> entry) {

					  // --- get column with ThreadTime-Instance (ThreadName) ---
					  if(entry.getValue(0) instanceof ThreadInfoStorageAgent) {
						  ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)entry.getValue(0);	
						  if(tia.isAgent() == true) {
							  return true;
						  }
					  }
					  return false;
				 }
			};
			
			sorter.setRowFilter(agentFilter);
			
		} else if (ae.getSource()== this.getBtnCalcMetrics()) {	
			threadInfoStorage.getThreadMeasureMetrics().calculateMetrics();
		} else if (ae.getSource()== this.getRdbtnIndividual()) {
			threadInfoStorage.getThreadMeasureMetrics().setMetricBase(threadInfoStorage.getThreadMeasureMetrics().METRIC_BASE_INDIVIDUAL);
		} else if (ae.getSource()== this.getRdbtnClass()) {
			threadInfoStorage.getThreadMeasureMetrics().setMetricBase(threadInfoStorage.getThreadMeasureMetrics().METRIC_BASE_CLASS);
		} else if (ae.getSource()== this.getRdbtnIntegral()) {
			threadInfoStorage.getThreadMeasureMetrics().setCalcType(threadInfoStorage.getThreadMeasureMetrics().CALC_TYPE_INTEGRAL);
		} else if (ae.getSource()== this.getRdbtnAverage()) {
			threadInfoStorage.getThreadMeasureMetrics().setCalcType(threadInfoStorage.getThreadMeasureMetrics().CALC_TYPE_AVG);
		} else if (ae.getSource()== this.getRdbtnTotal()) {
			threadInfoStorage.getThreadMeasureMetrics().setCalcType(threadInfoStorage.getThreadMeasureMetrics().CALC_TYPE_LAST_TOTAL);
		}
	}

	/**
	 * Enable metrics calculation button.
	 */
	public void enableMetricsCalculationButton(){
		if(threadInfoStorage.getThreadMeasureMetrics().isDataUsable() == true){
			getBtnCalcMetrics().setEnabled(true);
		}
	}
	/**
	 * Gets the button for calculating the metrics.
	 *
	 * @return the button calculation metrics
	 */
	private JButton getBtnCalcMetrics() {
		if (btnCalcMetrics == null) {
			btnCalcMetrics = new JButton("Calculate Metrics");
			btnCalcMetrics.setVerticalAlignment(SwingConstants.BOTTOM);
			btnCalcMetrics.setToolTipText("Calculates the real metrics for each agent based on recorded thread times.");
			btnCalcMetrics.addActionListener(this);
			btnCalcMetrics.setEnabled(false);
		}
		return btnCalcMetrics;
	}
	
	/**
	 * Gets the radio button average.
	 *
	 * @return the radio button average
	 */
	private JRadioButton getRdbtnAverage() {
		if (rdbtnAverage == null) {
			rdbtnAverage = new JRadioButton("Average");
			rdbtnAverage.setToolTipText("Calculate Metrices based on average system cpu time.");
			rdbtnAverage.addActionListener(this);
		}
		return rdbtnAverage;
	}
	
	/**
	 * Gets the radio button integral.
	 *
	 * @return the radio button integral
	 */
	private JRadioButton getRdbtnIntegral() {
		if (rdbtnIntegral == null) {
			rdbtnIntegral = new JRadioButton("Integral");
			rdbtnIntegral.setToolTipText("Calculate Metrices based on integrals.");
			rdbtnIntegral.addActionListener(this);
		}
		return rdbtnIntegral;
	}
	
	/**
	 * Gets the radio button total.
	 *
	 * @return the radio button total
	 */
	private JRadioButton getRdbtnTotal() {
		if (rdbtnTotal == null) {
			rdbtnTotal = new JRadioButton("Total");
			rdbtnTotal.setToolTipText("Calculate Metrices based on last total system cpu time.");
			rdbtnTotal.addActionListener(this);
		}
		return rdbtnTotal;
	}
	
	/**
	 * Gets the radio button individual.
	 *
	 * @return the radio button individual
	 */
	private JRadioButton getRdbtnIndividual() {
		if (rdbtnIndividual == null) {
			rdbtnIndividual = new JRadioButton("Individual");
			rdbtnIndividual.setToolTipText("Calculate Metrices based on individual agent data.");
			rdbtnIndividual.addActionListener(this);
		}
		return rdbtnIndividual;
	}
	
	/**
	 * Gets the radio button class.
	 *
	 * @return the radio button class
	 */
	private JRadioButton getRdbtnClass() {
		if (rdbtnClass == null) {
			rdbtnClass = new JRadioButton("Class");
			rdbtnClass.setToolTipText("Calculate Metrices based on agent class data.");
			rdbtnClass.addActionListener(this);
		}
		return rdbtnClass;
	}
	
	/**
	 * Gets the j panel options.
	 *
	 * @return the j panel options
	 */
	private JPanel getJPanelOptions() {
		if (JPanelOptions == null) {
			JPanelOptions = new JPanel();
			GridBagLayout gbl_JPanelOptions = new GridBagLayout();
			gbl_JPanelOptions.columnWidths = new int[]{0};
			gbl_JPanelOptions.rowHeights = new int[] {15};
			gbl_JPanelOptions.columnWeights = new double[]{Double.MIN_VALUE};
			gbl_JPanelOptions.rowWeights = new double[]{Double.MIN_VALUE};
			JPanelOptions.setLayout(gbl_JPanelOptions);
			GridBagConstraints gbc_rdbtnIndividual = new GridBagConstraints();
			gbc_rdbtnIndividual.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnIndividual.anchor = GridBagConstraints.WEST;
			gbc_rdbtnIndividual.gridx = 3;
			gbc_rdbtnIndividual.gridy = 0;
			JPanelOptions.add(getRdbtnIndividual(), gbc_rdbtnIndividual);
			GridBagConstraints gbc_rdbtnClass = new GridBagConstraints();
			gbc_rdbtnClass.anchor = GridBagConstraints.WEST;
			gbc_rdbtnClass.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnClass.gridx = 4;
			gbc_rdbtnClass.gridy = 0;
			JPanelOptions.add(getRdbtnClass(), gbc_rdbtnClass);
			GridBagConstraints gbc_rdbtnIntegral = new GridBagConstraints();
			gbc_rdbtnIntegral.anchor = GridBagConstraints.EAST;
			gbc_rdbtnIntegral.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnIntegral.gridx = 2;
			gbc_rdbtnIntegral.gridy = 1;
			JPanelOptions.add(getRdbtnIntegral(), gbc_rdbtnIntegral);
			GridBagConstraints gbc_rdbtnAverage = new GridBagConstraints();
			gbc_rdbtnAverage.anchor = GridBagConstraints.WEST;
			gbc_rdbtnAverage.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnAverage.gridx = 3;
			gbc_rdbtnAverage.gridy = 1;
			JPanelOptions.add(getRdbtnAverage(), gbc_rdbtnAverage);
			GridBagConstraints gbc_rdbtnTotal = new GridBagConstraints();
			gbc_rdbtnTotal.anchor = GridBagConstraints.WEST;
			gbc_rdbtnTotal.insets = new Insets(0, 0, 5, 0);
			gbc_rdbtnTotal.gridx = 4;
			gbc_rdbtnTotal.gridy = 1;
			JPanelOptions.add(getRdbtnTotal(), gbc_rdbtnTotal);
			
			ButtonGroup bgOptionsCalcType = new ButtonGroup();
			bgOptionsCalcType.add(getRdbtnIndividual());
			bgOptionsCalcType.add(getRdbtnClass());
			getRdbtnIndividual().setSelected(true);
			getRdbtnClass().setSelected(false);
			
			ButtonGroup bgOptionsMetricBase = new ButtonGroup();
			bgOptionsMetricBase.add(getRdbtnIntegral());
			bgOptionsMetricBase.add(getRdbtnTotal());
			bgOptionsMetricBase.add(getRdbtnAverage());
			getRdbtnIntegral().setSelected(true);
			getRdbtnTotal().setSelected(false);
			getRdbtnAverage().setSelected(false);
		}
		return JPanelOptions;
	}
}
