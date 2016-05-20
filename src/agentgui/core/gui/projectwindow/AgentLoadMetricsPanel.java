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
package agentgui.core.gui.projectwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;

import agentgui.core.application.Language;
import agentgui.core.project.AgentClassLoadMetrics;
import agentgui.core.project.AgentClassMetricDescription;
import agentgui.core.project.Project;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * The Class AgentClassLoadMetrics displays the configuration of the 
 * {@link AgentLoadMetricsPanel}.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentLoadMetricsPanel extends JPanel  implements ActionListener, Observer {

	private static final long serialVersionUID = -5111277438539494520L;
	
	private Project currProject;

	private boolean pauseObserver = false;
	private JLabel jLabelPredictive;
	private JRadioButton jRadioButtonPredictive;
	private JRadioButton jRadioButtonReal;
	private JScrollPane jScrollPaneMetric;
	private JLabel jLabelHeaderMetrics;
	private JTable jTableMetrics;
	
	/**
	 * Instantiates a new agent load metrics.
	 * @param currProject the curr project
	 */
	public AgentLoadMetricsPanel(Project currProject) {
		this.currProject = currProject;
		this.currProject.addObserver(this);
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		ButtonGroup bgMetrics = new ButtonGroup();
		bgMetrics.add(this.getJRadioButtonPredictive());
		bgMetrics.add(this.getJRadioButtonReal());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelPrdictive = new GridBagConstraints();
		gbc_jLabelPrdictive.anchor = GridBagConstraints.WEST;
		gbc_jLabelPrdictive.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelPrdictive.gridx = 0;
		gbc_jLabelPrdictive.gridy = 0;
		add(getJLabelPredictive(), gbc_jLabelPrdictive);
		GridBagConstraints gbc_jRadioButtonPredictive = new GridBagConstraints();
		gbc_jRadioButtonPredictive.insets = new Insets(5, 10, 5, 0);
		gbc_jRadioButtonPredictive.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonPredictive.gridx = 0;
		gbc_jRadioButtonPredictive.gridy = 1;
		add(getJRadioButtonPredictive(), gbc_jRadioButtonPredictive);
		GridBagConstraints gbc_jRadioButtonReal = new GridBagConstraints();
		gbc_jRadioButtonReal.insets = new Insets(0, 10, 5, 0);
		gbc_jRadioButtonReal.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonReal.gridx = 0;
		gbc_jRadioButtonReal.gridy = 2;
		add(getJRadioButtonReal(), gbc_jRadioButtonReal);
		GridBagConstraints gbc_jLabelHeaderMetrics = new GridBagConstraints();
		gbc_jLabelHeaderMetrics.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderMetrics.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelHeaderMetrics.gridx = 0;
		gbc_jLabelHeaderMetrics.gridy = 3;
		add(getJLabelHeaderMetrics(), gbc_jLabelHeaderMetrics);
		GridBagConstraints gbc_jScrollPaneMetric = new GridBagConstraints();
		gbc_jScrollPaneMetric.insets = new Insets(5, 10, 10, 10);
		gbc_jScrollPaneMetric.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneMetric.gridx = 0;
		gbc_jScrollPaneMetric.gridy = 4;
		add(getJScrollPaneMetric(), gbc_jScrollPaneMetric);
		
		jRadioButtonPredictive.setText(Language.translate("Manuelle, vorhergesagte Metrik"));
		jRadioButtonReal.setText(Language.translate("Empirische, reale Metrik"));
		jLabelPredictive.setText(Language.translate("Verteilung basierend auf:"));
		jLabelHeaderMetrics.setText(Language.translate("Metrik-Konfiguration"));
		
		loadMetricsFromProject();
	}
	
	private JLabel getJLabelPredictive() {
		if (jLabelPredictive == null) {
			jLabelPredictive = new JLabel("Verteilung basierend auf:");
			jLabelPredictive.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPredictive;
	}
	private JRadioButton getJRadioButtonPredictive() {
		if (jRadioButtonPredictive == null) {
			jRadioButtonPredictive = new JRadioButton("Manuelle, vorhergesagte Metrik");
			jRadioButtonPredictive.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonPredictive.addActionListener(this);
		}
		return jRadioButtonPredictive;
	}
	private JRadioButton getJRadioButtonReal() {
		if (jRadioButtonReal == null) {
			jRadioButtonReal = new JRadioButton("Empirische, reale Metrik");
			jRadioButtonReal.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonReal.addActionListener(this);
		}
		return jRadioButtonReal;
	}
	private JScrollPane getJScrollPaneMetric() {
		if (jScrollPaneMetric == null) {
			jScrollPaneMetric = new JScrollPane();
			jScrollPaneMetric.setViewportView(getJTableMetrics());
		}
		return jScrollPaneMetric;
	}
	private JLabel getJLabelHeaderMetrics() {
		if (jLabelHeaderMetrics == null) {
			jLabelHeaderMetrics = new JLabel("Metrik-Konfiguration");
			jLabelHeaderMetrics.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderMetrics;
	}
	private JTable getJTableMetrics() {
		if (jTableMetrics == null) {
			jTableMetrics = new JTable(this.currProject.getAgentClassLoadMetrics().getTableModel());
			jTableMetrics.getColumnModel().getColumn(0).setMinWidth(300);
			jTableMetrics.setFillsViewportHeight(true);
			currProject.getAgentClassLoadMetrics().getTableModel().addTableModelListener(new TableModelListener() {
				public void tableChanged (TableModelEvent e) {
					int row = e.getFirstRow();
					int column = e.getColumn();
					if(column != -1){
						Object data = jTableMetrics.getModel().getValueAt(row, column);
						Object className = jTableMetrics.getModel().getValueAt(row, 0);
//						String columnName = jTableMetrics.getModel().getColumnName(column);
//						System.out.println("columnName: " + columnName + " column: " + column+ " row: " + row + " value: "+ (long)data + " className: " + className);
						Vector<AgentClassMetricDescription> agentClassMetricDescriptionVector = currProject.getAgentClassLoadMetrics().getAgentClassMetricDescriptionVector();
						
						int index = currProject.getAgentClassLoadMetrics().getIndexOfAgentClassMetricDescription(className.toString());
						if(index != -1){
							if(column == 1){
								agentClassMetricDescriptionVector.get(index).setUserPredictedMetric((Long)data);
							}else if(column == 2){
								agentClassMetricDescriptionVector.get(index).setRealMetricMin((Long)data);
							}else if(column == 3){
								agentClassMetricDescriptionVector.get(index).setRealMetricMax((Long)data);
							}else if(column == 4){
								agentClassMetricDescriptionVector.get(index).setRealMetricAverage((Long)data);
							}	
							
							currProject.save();
						}
						
//						for(int i = 0; i < agentClassMetricDescriptionVector.size(); i++){
//							if(agentClassMetricDescriptionVector.get(i).getClassName().equals(className.toString())){
//								if(column == 1){
//									agentClassMetricDescriptionVector.get(i).setUserPredictedMetric((long)data);
//								}else if(column == 2){
//									agentClassMetricDescriptionVector.get(i).setRealMetricMin((long)data);
//								}else if(column == 3){
//									agentClassMetricDescriptionVector.get(i).setRealMetricMax((long)data);
//								}else if(column == 4){
//									agentClassMetricDescriptionVector.get(i).setRealMetricAverage((long)data);
//								}	
//								
//								currProject.save();
//							}
//						}
					}
					}
					});
			
			// --- Add a sorter if the model is available -------
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.currProject.getAgentClassLoadMetrics().getTableModel());

			List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
			sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING));
			sorter.setSortKeys(sortKeys);
			sorter.setSortsOnUpdates(true);
			
			jTableMetrics.setRowSorter(sorter);	
		}
		return jTableMetrics;
	}
	
	/**
	 * Load metrics settings from the current project.
	 */
	private void loadMetricsFromProject() {
				
		jRadioButtonReal.setSelected(currProject.getAgentClassLoadMetrics().isUseRealLoadMetric());
		jRadioButtonPredictive.setSelected(!currProject.getAgentClassLoadMetrics().isUseRealLoadMetric());
		
		AgentClassLoadMetrics aclm = currProject.getAgentClassLoadMetrics();
		aclm.clearTableModel();
		Vector<AgentClassMetricDescription> agentClassMetricDescriptionVector = currProject.getAgentClassLoadMetrics().getAgentClassMetricDescriptionVector();
		
		for(int i = 0; i < agentClassMetricDescriptionVector.size(); i++){
			aclm.addTableModelRow(agentClassMetricDescriptionVector.get(i));
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJRadioButtonPredictive()) {
			this.pauseObserver = true;
			this.currProject.getAgentClassLoadMetrics().setUseRealLoadMetric(false);
			this.pauseObserver = false;
			
		} else if (ae.getSource()==this.getJRadioButtonReal()) {
			this.pauseObserver = true;
			this.currProject.getAgentClassLoadMetrics().setUseRealLoadMetric(true);
			this.pauseObserver = false;
			
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		if (this.pauseObserver==false) {
		
			if (updateObject==Project.AGENT_METRIC_Reset) {
				
			} else if (updateObject==Project.AGENT_METRIC_ChangedDataSource) {
				
			} else if (updateObject==Project.AGENT_METRIC_AgentDescriptionAdded) {
				
			} else if (updateObject==Project.AGENT_METRIC_AgentDescriptionEdited) {
				
			} else if (updateObject==Project.AGENT_METRIC_AgentDescriptionRemoved) {

			}	
		}
	}	
}
