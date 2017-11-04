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
package org.agentgui.gui.swing.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;

import agentgui.core.application.Language;
import agentgui.core.project.AgentClassMetricDescription;
import agentgui.core.project.Project;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * The Class AgentClassLoadMetricsTable displays the configuration of the 
 * {@link AgentClassLoadMetricsPanel}.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentClassLoadMetricsPanel extends JPanel  implements ActionListener, Observer {

	private static final long serialVersionUID = -5111277438539494520L;
	
	private Project currProject;

	private boolean pauseObserver = false;
	private JLabel jLabelPredictive;
	private JRadioButton jRadioButtonPredictive;
	private JRadioButton jRadioButtonReal;
	private JButton jButtonCopyReal;
	private JScrollPane jScrollPaneMetric;
	private JLabel jLabelHeaderMetrics;
	private JTextArea jTextAreaMetricExplanation;
	private JTable jTableMetrics;
	
	private String metricExplanationString = "Metrik: Äquivalent für die Leistungsfähigkeit einer Maschine, die durch den Thread vollständig ausgelastet würde.[Einheit:MFLOPs, Wert:0-200]";
	private String headerMetricsString = "Metrik-Konfiguration"; 
	private String labelPredictiveString = "Verteilung der Agenten bei \"PredictiveStaticLoadBalancing\" basierend auf:";
	private String buttonRealString = "Empirisch ermittelte, reale Metrik";
	private String buttonPredictiveString = "Manuell eingegebene, vorhergesagte Metrik";
	private String buttonCopyRealString = "Übernehme Durchschnittswerte für reale Metrik";
	
	/**
	 * Instantiates a new agent load metrics.
	 * @param currProject the curr project
	 */
	public AgentClassLoadMetricsPanel(Project currProject) {
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
		
		GridBagConstraints gbc_jLabelMetricExplanation = new GridBagConstraints();
		gbc_jLabelMetricExplanation.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelMetricExplanation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelMetricExplanation.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelMetricExplanation.gridx = 0;
		gbc_jLabelMetricExplanation.gridy = 0;
		this.add(getJTextAreaMetricExplanation(), gbc_jLabelMetricExplanation);
		
		GridBagConstraints gbc_jLabelPredictive = new GridBagConstraints();
		gbc_jLabelPredictive.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelPredictive.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelPredictive.gridx = 0;
		gbc_jLabelPredictive.gridy = 1;
		this.add(getJLabelPredictive(), gbc_jLabelPredictive);
		
		GridBagConstraints gbc_jRadioButtonPredictive = new GridBagConstraints();
		gbc_jRadioButtonPredictive.insets = new Insets(5, 10, 5, 0);
		gbc_jRadioButtonPredictive.anchor = GridBagConstraints.NORTHWEST;
		gbc_jRadioButtonPredictive.gridx = 0;
		gbc_jRadioButtonPredictive.gridy = 2;
		this.add(getJRadioButtonPredictive(), gbc_jRadioButtonPredictive);
		
		GridBagConstraints gbc_jRadioButtonReal = new GridBagConstraints();
		gbc_jRadioButtonReal.insets = new Insets(0, 10, 5, 0);
		gbc_jRadioButtonReal.anchor = GridBagConstraints.NORTHWEST;
		gbc_jRadioButtonReal.gridx = 0;
		gbc_jRadioButtonReal.gridy = 3;
		this.add(getJRadioButtonReal(), gbc_jRadioButtonReal);
		
		GridBagConstraints gbc_jLabelHeaderMetrics = new GridBagConstraints();
		gbc_jLabelHeaderMetrics.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelHeaderMetrics.insets = new Insets(10, 10, 10, 0);
		gbc_jLabelHeaderMetrics.gridx = 0;
		gbc_jLabelHeaderMetrics.gridy = 4;
		this.add(getJLabelHeaderMetrics(), gbc_jLabelHeaderMetrics);
		
		GridBagConstraints gbc_jScrollPaneMetric = new GridBagConstraints();
		gbc_jScrollPaneMetric.insets = new Insets(5, 10, 10, 10);
		gbc_jScrollPaneMetric.anchor = GridBagConstraints.NORTHWEST;
		gbc_jScrollPaneMetric.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneMetric.gridx = 0;
		gbc_jScrollPaneMetric.gridy = 5;
		this.add(getJScrollPaneMetric(), gbc_jScrollPaneMetric);
		
		GridBagConstraints gbc_jButtonCopyReal = new GridBagConstraints();
		gbc_jButtonCopyReal.insets = new Insets(10, 10, 10, 10);
		gbc_jButtonCopyReal.anchor = GridBagConstraints.EAST;
		gbc_jButtonCopyReal.gridx = 0;
		gbc_jButtonCopyReal.gridy = 6;
		this.add(getJButtonCopyReal(), gbc_jButtonCopyReal);
		
		getJRadioButtonPredictive().setText(Language.translate(buttonPredictiveString));
		getJRadioButtonReal().setText(Language.translate(buttonRealString));
		getJButtonCopyReal().setText(Language.translate(buttonCopyRealString));
		getJLabelPredictive().setText(Language.translate(labelPredictiveString));
		getJLabelHeaderMetrics().setText(Language.translate(headerMetricsString));
		getJTextAreaMetricExplanation().setText(Language.translate(metricExplanationString));
		
		this.currProject.getAgentClassLoadMetrics().loadMetricsFromProject();
		jRadioButtonReal.setSelected(currProject.getAgentClassLoadMetrics().isUseRealLoadMetric());
		jRadioButtonPredictive.setSelected(!currProject.getAgentClassLoadMetrics().isUseRealLoadMetric());
	}
	
	private JLabel getJLabelPredictive() {
		if (jLabelPredictive == null) {
			jLabelPredictive = new JLabel(labelPredictiveString);
			jLabelPredictive.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPredictive;
	}
	private JRadioButton getJRadioButtonPredictive() {
		if (jRadioButtonPredictive == null) {
			jRadioButtonPredictive = new JRadioButton(buttonPredictiveString);
			jRadioButtonPredictive.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonPredictive.addActionListener(this);
		}
		return jRadioButtonPredictive;
	}
	private JRadioButton getJRadioButtonReal() {
		if (jRadioButtonReal == null) {
			jRadioButtonReal = new JRadioButton(buttonRealString);
			jRadioButtonReal.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonReal.addActionListener(this);
		}
		return jRadioButtonReal;
	}
	
	private JButton getJButtonCopyReal() {
		if (jButtonCopyReal == null) {
			jButtonCopyReal = new JButton(buttonCopyRealString);
			jButtonCopyReal.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonCopyReal.addActionListener(this);
		}
		return jButtonCopyReal;
	}
	
	private JScrollPane getJScrollPaneMetric() {
		if (jScrollPaneMetric == null) {
			jScrollPaneMetric = new JScrollPane();
			jScrollPaneMetric.setViewportView(getJTableMetrics());
			jScrollPaneMetric.setMinimumSize(new Dimension(300, 300));
		}
		return jScrollPaneMetric;
	}
	private JLabel getJLabelHeaderMetrics() {
		if (jLabelHeaderMetrics == null) {
			jLabelHeaderMetrics = new JLabel(headerMetricsString);
			jLabelHeaderMetrics.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderMetrics;
	}
	
	private JTextArea getJTextAreaMetricExplanation() {
		if (jTextAreaMetricExplanation == null) {
			jTextAreaMetricExplanation = new JTextArea(metricExplanationString);
			jTextAreaMetricExplanation.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextAreaMetricExplanation.setWrapStyleWord(true);
			jTextAreaMetricExplanation.setLineWrap(true);
			jTextAreaMetricExplanation.setOpaque(false);
			jTextAreaMetricExplanation.setEditable(false);
			jTextAreaMetricExplanation.setFocusable(false);
			jTextAreaMetricExplanation.setMinimumSize(new Dimension(600, 30));
			jTextAreaMetricExplanation.setBackground(new Color(214,217,223));
			jTextAreaMetricExplanation.setBorder(null);
		}
		return jTextAreaMetricExplanation;
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
								agentClassMetricDescriptionVector.get(index).setRealMetric((Long)data);
							}else if(column == 3){
								agentClassMetricDescriptionVector.get(index).setRealMetricMin((Long)data);
							}else if(column == 4){
								agentClassMetricDescriptionVector.get(index).setRealMetricMax((Long)data);
							}else if(column == 5){
								agentClassMetricDescriptionVector.get(index).setRealMetricAverage((Long)data);
							}	
							
							currProject.save();
						}
					}
					}
					});
			
			// --- Add a sorter if the model is available -------
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.currProject.getAgentClassLoadMetrics().getTableModel());

			List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
			sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
			sorter.setSortKeys(sortKeys);
			sorter.setSortsOnUpdates(true);
			
			jTableMetrics.setRowSorter(sorter);	
		}
		return jTableMetrics;
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
			
		} else if (ae.getSource()==this.getJButtonCopyReal()) {
			this.pauseObserver = true;
			this.currProject.getAgentClassLoadMetrics().copyRealMetricsAverage2RealMetrics();
			this.currProject.getAgentClassLoadMetrics().loadMetricsFromProject();
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
