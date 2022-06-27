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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Language;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorage;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageAgent;

import javax.swing.SwingConstants;

/**
 * The Class ThreadMonitorMetricsTableTab.
 * 
 * Displays an auto-sortable table of agents and their metrics.
 * Filter for agents applicable.
 * 
 */
public class ThreadMonitorMetricsTableTab extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 4848470750194315559L;
	
	private ThreadInfoStorage threadInfoStorage;
	
	private JScrollPane scrollPaneTable;
	private JTable jTableThreadInfoMetrics;
	private JPanel JPanelFilter;
	
	private JRadioButton jRadioButtonNoFilter;
	private JRadioButton jRadioButtonFilterAgents;
	
	private JLabel lblCalcBaseHint;
	private JRadioButton rdbtnIntegralDelta;
	private JRadioButton rdbtnLastTotal;
	private JRadioButton rdbtnIndividual;
	private JRadioButton rdbtnClass;
	private JRadioButton rdbtnIntegralTotal;
	
	private JPanel JPanelOptions;
	
	private String lblCalcBaseHintString = Language.translate("Calculate the real metrics based on recorded chart value selection.", Language.EN);
	private String rdBtnIntegralTotalString = Language.translate("Calculate metrics based on total integrals.", Language.EN);
	private String rdBtnIntegralDeltaString = Language.translate("Calculate metrics based on delta integrals.", Language.EN);
	private String rdBtnLastTotalString =  Language.translate("Calculate metrics based on last total system cpu time.", Language.EN);
	private String rdBtnIndividualString = Language.translate("Calculate metrics based on individual agent data.", Language.EN);
	private String rdBtnClassString = Language.translate("Calculate metrics based on agent class data.", Language.EN);
	
	
	/**
	 * Instantiates a new thread measure metrics tab.
	 * @param threadInfoStorage the thread info storage
	 */
	public ThreadMonitorMetricsTableTab(ThreadInfoStorage threadInfoStorage) {
		this.threadInfoStorage = threadInfoStorage;
		this.initialize();
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
		this.setLayout(gridBagLayout);
		
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
		this.add(getJPanelOptions(), gbc_JPanelOptions);
	}

	/**
	 * Gets the scroll pane table.
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
			jTableThreadInfoMetrics.getTableHeader().setReorderingAllowed(false);
			
			TableColumnModel columnModel = jTableThreadInfoMetrics.getColumnModel();
			
			columnModel.getColumn(0).setMinWidth(50);
			columnModel.getColumn(1).setMinWidth(200);
			
			int numberColumnWidth = 120;
			columnModel.getColumn(2).setMinWidth(numberColumnWidth);
			columnModel.getColumn(2).setMaxWidth(numberColumnWidth);
			
			columnModel.getColumn(3).setMinWidth(numberColumnWidth);
			columnModel.getColumn(3).setMaxWidth(numberColumnWidth);
			
			
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
			
			GridBagConstraints gbc_calcBaseHint = new GridBagConstraints();
			gbc_calcBaseHint.anchor = GridBagConstraints.EAST;
			gbc_calcBaseHint.insets = new Insets(1, 5, 5, 1);
			gbc_calcBaseHint.gridx = 4;
			gbc_calcBaseHint.gridy = 0;
			JPanelFilter.add(getLblCalcBaseHint(), gbc_calcBaseHint);
			
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

					  // --- get column with ThreadDetail-Instance (ThreadName) ---
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
			
		} else if (ae.getSource()== this.getRdbtnIndividual()) {
			threadInfoStorage.getThreadMeasureMetrics().setMetricBase(threadInfoStorage.getThreadMeasureMetrics().METRIC_BASE_INDIVIDUAL);
		} else if (ae.getSource()== this.getRdbtnClass()) {
			threadInfoStorage.getThreadMeasureMetrics().setMetricBase(threadInfoStorage.getThreadMeasureMetrics().METRIC_BASE_CLASS);
		} else if (ae.getSource()== this.getRdbtnIntegralDelta()) {
			threadInfoStorage.getThreadMeasureMetrics().setCalcType(threadInfoStorage.getThreadMeasureMetrics().CALC_TYPE_INTEGRAL_DELTA);
		} else if (ae.getSource()== this.getRdbtnIntegralTotal()) {
			threadInfoStorage.getThreadMeasureMetrics().setCalcType(threadInfoStorage.getThreadMeasureMetrics().CALC_TYPE_INTEGRAL_TOTAL);
		} else if (ae.getSource()== this.getRdbtnLastTotal()) {
			threadInfoStorage.getThreadMeasureMetrics().setCalcType(threadInfoStorage.getThreadMeasureMetrics().CALC_TYPE_LAST_TOTAL);
		}
	}
	
	/**
	 * Gets the button calculate metrics hint.
	 * @return the button calculate metrics hint
	 */
	private JLabel getLblCalcBaseHint() {
		if (lblCalcBaseHint == null) {
			lblCalcBaseHint = new JLabel(lblCalcBaseHintString);
			lblCalcBaseHint.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblCalcBaseHint;
	}
	
	/**
	 * Gets the radio button average.
	 *
	 * @return the radio button integral total
	 */
	private JRadioButton getRdbtnIntegralTotal() {
		if (rdbtnIntegralTotal == null) {
			rdbtnIntegralTotal = new JRadioButton("Total");
			rdbtnIntegralTotal.setToolTipText(rdBtnIntegralTotalString );
			rdbtnIntegralTotal.addActionListener(this);
		}
		return rdbtnIntegralTotal;
	}
	
	/**
	 * Gets the radio button integral delta.
	 *
	 * @return the radio button integral delta
	 */
	private JRadioButton getRdbtnIntegralDelta() {
		if (rdbtnIntegralDelta == null) {
			rdbtnIntegralDelta = new JRadioButton("Delta");
			rdbtnIntegralDelta.setToolTipText(rdBtnIntegralDeltaString);
			rdbtnIntegralDelta.addActionListener(this);
		}
		return rdbtnIntegralDelta;
	}
	
	/**
	 * Gets the radio button last total.
	 *
	 * @return the radio button last total
	 */
	private JRadioButton getRdbtnLastTotal() {
		if (rdbtnLastTotal == null) {
			rdbtnLastTotal = new JRadioButton("Last Total");
			rdbtnLastTotal.setToolTipText(rdBtnLastTotalString);
			rdbtnLastTotal.addActionListener(this);
		}
		return rdbtnLastTotal;
	}
	
	/**
	 * Gets the radio button individual.
	 *
	 * @return the radio button individual
	 */
	private JRadioButton getRdbtnIndividual() {
		if (rdbtnIndividual == null) {
			rdbtnIndividual = new JRadioButton("Individual");
			rdbtnIndividual.setToolTipText(rdBtnIndividualString);
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
			rdbtnClass.setToolTipText(rdBtnClassString);
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
			GridBagConstraints gbc_rdbtnIntegralDelta = new GridBagConstraints();
			gbc_rdbtnIntegralDelta.anchor = GridBagConstraints.EAST;
			gbc_rdbtnIntegralDelta.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnIntegralDelta.gridx = 2;
			gbc_rdbtnIntegralDelta.gridy = 1;
			JPanelOptions.add(getRdbtnIntegralDelta(), gbc_rdbtnIntegralDelta);
			GridBagConstraints gbc_rdbtnIntegralTotal = new GridBagConstraints();
			gbc_rdbtnIntegralTotal.anchor = GridBagConstraints.WEST;
			gbc_rdbtnIntegralTotal.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnIntegralTotal.gridx = 3;
			gbc_rdbtnIntegralTotal.gridy = 1;
			JPanelOptions.add(getRdbtnIntegralTotal(), gbc_rdbtnIntegralTotal);
			GridBagConstraints gbc_rdbtnLastTotal = new GridBagConstraints();
			gbc_rdbtnLastTotal.anchor = GridBagConstraints.WEST;
			gbc_rdbtnLastTotal.insets = new Insets(0, 0, 5, 0);
			gbc_rdbtnLastTotal.gridx = 4;
			gbc_rdbtnLastTotal.gridy = 1;
			JPanelOptions.add(getRdbtnLastTotal(), gbc_rdbtnLastTotal);
			
			ButtonGroup bgOptionsCalcType = new ButtonGroup();
			bgOptionsCalcType.add(getRdbtnIndividual());
			bgOptionsCalcType.add(getRdbtnClass());
			getRdbtnIndividual().setSelected(true);
			getRdbtnClass().setSelected(false);
			
			ButtonGroup bgOptionsMetricBase = new ButtonGroup();
			bgOptionsMetricBase.add(getRdbtnIntegralDelta());
			bgOptionsMetricBase.add(getRdbtnLastTotal());
			bgOptionsMetricBase.add(getRdbtnIntegralTotal());
			getRdbtnIntegralDelta().setSelected(true);
			getRdbtnLastTotal().setSelected(false);
			getRdbtnIntegralTotal().setSelected(false);
		}
		return JPanelOptions;
	}
}
