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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.components.TimeSelection;
import agentgui.core.project.DistributionSetup;
import agentgui.core.project.Project;
import agentgui.core.project.RemoteContainerConfiguration;
import agentgui.simulationService.balancing.DynamicLoadBalancing;
import agentgui.simulationService.balancing.DynamicLoadBalancingBase;
import agentgui.simulationService.balancing.StaticLoadBalancing;
import agentgui.simulationService.balancing.StaticLoadBalancingBase;
import agentgui.simulationService.distribution.JadeRemoteStart;
import agentgui.simulationService.load.LoadThresholdLevels;
import de.enflexit.common.classSelection.ClassSelectionDialog;
import de.enflexit.common.swing.KeyAdapter4Numbers;

/**
 * Represents the JPanel/Tab to configure the Distribution mechanisms for
 * agents within the project configuration 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Distribution extends JScrollPane implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;
	
	private final static int STATIC_BALANCING_CLASS = 1;
	private final static int DYNAMIC_BALANCING_CLASS = 2;
	private final static int THRESHOLD_LEVEL = 3;

	private Project currProject;
	private DistributionSetup currDistributionSetup; 
	private LoadThresholdLevels currUserThresholds;
	private RemoteContainerConfiguration currRemoteContainerConfiguration;
	
	private JPanel jPanelOnScrollPane;
	private JPanel jPanelRemoteConfig;
	private JPanel jPanelStatic;
	private JPanel jPanelStaticClass;
	private JPanel jPanelDynamic;
	private JPanel jPanelThreshold;
	private JPanel jPanelRecording;
	private JPanel jPanelDummy;
	
	private JCheckBox jCheckBoxPreventUsageOfUsedComputers;
	private JCheckBox jCheckBoxIsRemoteOnly;
	private JCheckBox jCheckBoxIsEvenDistribution;
	private JCheckBox jCheckBoxShowRMA;
	private JCheckBox jCheckBoxDoLoadStatic;
	private JCheckBox jCheckBoxDoLoadDynamic;
	private JCheckBox jCheckBoxThresholdDefinition;
	private JCheckBox jCheckBoxShowLoadMonitor;
	private JCheckBox jCheckBoxShowThreadMonitor;
	private JCheckBox jCheckboxAutosaveRealMetricsOnSimStop;
	private JCheckBox jCheckBoxImmediatelyStartLoadRecording;
	
	private JComboBox<String> jComboBoxJVMMemoryInitial;
	private JComboBox<String> jComboBoxJVMMemoryMaximum;
	private JComboBox<TimeSelection> jComboBoxRecordingInterval;
	
	private String[] comboData = {JadeRemoteStart.jvmMemo16MB, JadeRemoteStart.jvmMemo32MB,
								  JadeRemoteStart.jvmMemo64MB, JadeRemoteStart.jvmMemo128MB,
								  JadeRemoteStart.jvmMemo256MB, JadeRemoteStart.jvmMemo512MB,
								  JadeRemoteStart.jvmMemo1GB, JadeRemoteStart.jvmMemo2GB,
								  JadeRemoteStart.jvmMemo4GB, JadeRemoteStart.jvmMemo8GB,
								  JadeRemoteStart.jvmMemo16GB,JadeRemoteStart.jvmMemo32GB};
	private DefaultComboBoxModel<String> comboModelInitial = new DefaultComboBoxModel<String>(comboData);
	private DefaultComboBoxModel<String> comboModelMaximal = new DefaultComboBoxModel<String>(comboData);
	private DefaultComboBoxModel<TimeSelection> comboModelRecordingInterval;
	
	private KeyAdapter4Numbers keyAdapter4Numbers;
	private DocumentListener docListener;
	private boolean pauseDocumentListener;
	private boolean pauseObserver;
	
	private JTextField jTextFieldAgentsExpected;
	private JTextField jTextFieldContainerExpected;
	private JTextField jTextFieldStaticLoadClass;
	private JTextField jTextFieldDynamicLoadClass;
	private JTextField jTextFieldCpuLow;
	private JTextField jTextFieldCpuHigh;
	private JTextField jTextFieldMemLow;
	private JTextField jTextFieldMemHigh;
	private JTextField jTextFieldThreadsHigh;
	private JTextField jTextFieldThreadsLow;
	
	private JLabel jLabelMemoryAlloc;
	private JLabel jLabelMemoryMax;
	private JLabel jLabelMemoryMin;
	
	private JLabel jLabelStaticLoadClass;
	private JLabel jLabelDynamicLoadClass;
	private JLabel jLabelCalculation;
	private JLabel jLabelAgentsExpected;
	private JLabel jLabelContainerExpected;
	
	private JLabel jLabelCPU;
	private JLabel jLabelMemory;
	private JLabel jLabelThreads;
	private JLabel jLabelCPUPercent;
	private JLabel jLabelMemPercent;
	private JLabel jLabelThreadsNo;
	private JLabel jLabelHigh;
	private JLabel jLabelLow;
	
	private JButton jButtonRemoteDefault;
	private JButton jButtonCalcContainer;
	
	private JButton jButtonDefaultClassStatic;
	private JButton jButtonDefaultClassDynamic;
	private JButton jButtonDefaultThreshold;
	
	private JButton jButtonSelectStaticClass;
	private JButton jButtonSelectDynamicClass;

	private JLabel jLabelRecording;
	private JLabel jLabelDummy;
	private JSeparator jSeparator01;
	private JSeparator jSeparator02;
	private JSeparator jSeparator03;
	private JSeparator jSeparator04;
	private JSeparator jSeparator05;
	
	private String autosaveRealMetricsOnSimStopString = "Reale Metriken der Agenten bei Simulationsstop aktualisieren";

	
	/**
	 * This is the default constructor
	 */
	public Distribution(Project project) {
		super();
		currProject = project;
		currProject.addObserver(this);
		initialize();
		
		this.setupLoad();
		
		jCheckBoxPreventUsageOfUsedComputers.setText(Language.translate("Keine Computer nutzen, die bereits einen Remote-Container beherbergen"));
		jCheckBoxIsRemoteOnly.setText(Language.translate("Verteilung der Agenten nur auf Remote-Maschinen"));
		jCheckBoxIsEvenDistribution.setText(Language.translate("Gleichmäßige Verteilung der Agenten auf alle verfügbaren Maschinen"));
		jCheckBoxShowRMA.setText(Language.translate("Remote JADE-RMA beim Start des entfernten Containers anzeigen"));
		jCheckBoxDoLoadStatic.setText(Language.translate("Statische Lastverteilung aktivieren"));
		jCheckBoxDoLoadDynamic.setText(Language.translate("Dynamische Lastverteilung aktivieren"));
		jCheckBoxThresholdDefinition.setText(Language.translate("Eigene Auslastungsgrenzwerte verwenden"));
		jCheckBoxShowLoadMonitor.setText(Language.translate("Auslastungs-Monitor bei JADE-Start anzeigen"));
		jCheckBoxShowThreadMonitor.setText(Language.translate("Thread-Monitor bei JADE-Start anzeigen"));
		jCheckboxAutosaveRealMetricsOnSimStop.setText(Language.translate(autosaveRealMetricsOnSimStopString));
		jCheckBoxImmediatelyStartLoadRecording.setText(Language.translate("Lastaufzeichnung mit dem Start von JADE beginnen"));
		
		jLabelMemoryAlloc.setText(Language.translate("Arbeitsspeicher für Remote-JVM"));
		jLabelStaticLoadClass.setText(Language.translate("Java-Klasse für den Start der Simulations-Agenten und die statische Lastverteilung"));
		jLabelDynamicLoadClass.setText(Language.translate("Java-Klasse für die dynamische Lastverteilung"));
		jLabelAgentsExpected.setText(Language.translate("Anzahl Agenten (erwartet)"));
		jLabelContainerExpected.setText(Language.translate("Anzahl Container"));

		jLabelCPU.setText(Language.translate("CPU-Auslastung"));
		jLabelMemory.setText(Language.translate("JVM Heap-Memory"));
		jLabelThreads.setText(Language.translate("N - Threads"));

		jLabelRecording.setText(Language.translate("Abtastintervall"));
		
		jButtonCalcContainer.setText(Language.translate("Anzahl Container berechnen"));
		
		jButtonRemoteDefault.setToolTipText(Language.translate("Standard verwenden"));
		jButtonDefaultClassStatic.setToolTipText(Language.translate("Agent.GUI - Standard verwenden"));	
		jButtonDefaultClassDynamic.setToolTipText(Language.translate("Agent.GUI - Standard verwenden"));
		jButtonSelectStaticClass.setToolTipText(Language.translate("Klasse auswählen"));
		jButtonSelectDynamicClass.setToolTipText(Language.translate("Klasse auswählen"));
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.setViewportView(this.getJPanelOnScrollPane());
		
	}

	/**
	 * This method initializes jPanelOnScrollPane.
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelOnScrollPane() {
		if (jPanelOnScrollPane==null) {
			
			GridBagConstraints gbcJPanelRemoteConfig = new GridBagConstraints();
			gbcJPanelRemoteConfig.gridx = 0;
			gbcJPanelRemoteConfig.gridy = 0;
			gbcJPanelRemoteConfig.anchor = GridBagConstraints.NORTHWEST;
			gbcJPanelRemoteConfig.insets = new Insets(10, 10, 5, 0);
			
			GridBagConstraints gbc_jSeparator01 = new GridBagConstraints();
			gbc_jSeparator01.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparator01.insets = new Insets(0, 10, 5, 100);
			gbc_jSeparator01.gridx = 0;
			gbc_jSeparator01.gridy = 1;
			
			GridBagConstraints gbcJCheckBoxShowThreadMonitor = new GridBagConstraints();
			gbcJCheckBoxShowThreadMonitor.gridx = 0;
			gbcJCheckBoxShowThreadMonitor.gridy = 2;
			gbcJCheckBoxShowThreadMonitor.insets = new Insets(5, 10, 5, 0);
			gbcJCheckBoxShowThreadMonitor.anchor = GridBagConstraints.WEST;
			GridBagConstraints gbcJCheckBoxAutosaveThreadMetricsOnSimStop = new GridBagConstraints();
			gbcJCheckBoxAutosaveThreadMetricsOnSimStop.gridx = 0;
			gbcJCheckBoxAutosaveThreadMetricsOnSimStop.gridy = 3;
			gbcJCheckBoxAutosaveThreadMetricsOnSimStop.insets = new Insets(5, 10, 5, 0);
			gbcJCheckBoxAutosaveThreadMetricsOnSimStop.anchor = GridBagConstraints.WEST;
			
			GridBagConstraints gbc_jSeparator02 = new GridBagConstraints();
			gbc_jSeparator02.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparator02.insets = new Insets(0, 10, 5, 100);
			gbc_jSeparator02.gridx = 0;
			gbc_jSeparator02.gridy = 4;
			
			GridBagConstraints gbcJCheckBoxShowLoadMonitor = new GridBagConstraints();
			gbcJCheckBoxShowLoadMonitor.gridx = 0;
			gbcJCheckBoxShowLoadMonitor.gridy = 5;
			gbcJCheckBoxShowLoadMonitor.insets = new Insets(10, 10, 5, 0);
			gbcJCheckBoxShowLoadMonitor.anchor = GridBagConstraints.WEST;
			GridBagConstraints gbcJCheckBoxImmStartLoadRecording = new GridBagConstraints();
			gbcJCheckBoxImmStartLoadRecording.gridx = 0;
			gbcJCheckBoxImmStartLoadRecording.gridy = 6;
			gbcJCheckBoxImmStartLoadRecording.insets = new Insets(5, 10, 5, 0);
			gbcJCheckBoxImmStartLoadRecording.anchor = GridBagConstraints.WEST;
			GridBagConstraints gbcJPanelRecording = new GridBagConstraints();
			gbcJPanelRecording.gridx = 0;
			gbcJPanelRecording.gridy = 7;
			gbcJPanelRecording.insets = new Insets(0, 32, 5, 10);
			gbcJPanelRecording.anchor = GridBagConstraints.NORTHWEST;
			
			GridBagConstraints gbc_jSeparator03 = new GridBagConstraints();
			gbc_jSeparator03.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparator03.insets = new Insets(0, 10, 5, 100);
			gbc_jSeparator03.gridx = 0;
			gbc_jSeparator03.gridy = 8;

			GridBagConstraints gbcJCheckBoxStatic = new GridBagConstraints();
			gbcJCheckBoxStatic.gridx = 0;
			gbcJCheckBoxStatic.gridy = 11;
			gbcJCheckBoxStatic.anchor = GridBagConstraints.WEST;
			gbcJCheckBoxStatic.insets = new Insets(10, 10, 5, 0);
			
			GridBagConstraints gbcJPanelStaticClass = new GridBagConstraints();
			gbcJPanelStaticClass.gridx = 0;
			gbcJPanelStaticClass.gridy = 12;
			gbcJPanelStaticClass.anchor = GridBagConstraints.NORTHWEST;
			gbcJPanelStaticClass.insets = new Insets(5, 32, 5, 0);
			
			GridBagConstraints gbcJPanelStatic = new GridBagConstraints();
			gbcJPanelStatic.gridx = 0;
			gbcJPanelStatic.gridy = 13;
			gbcJPanelStatic.fill = GridBagConstraints.NONE;
			gbcJPanelStatic.weightx = 1.0;
			gbcJPanelStatic.insets = new Insets(5, 32, 5, 10);
			gbcJPanelStatic.anchor = GridBagConstraints.NORTHWEST;
			
			
			GridBagConstraints gbc_jSeparator04 = new GridBagConstraints();
			gbc_jSeparator04.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparator04.insets = new Insets(0, 10, 5, 100);
			gbc_jSeparator04.gridx = 0;
			gbc_jSeparator04.gridy = 14;
			
			GridBagConstraints gbcJCheckBoxDynamic = new GridBagConstraints();
			gbcJCheckBoxDynamic.gridx = 0;
			gbcJCheckBoxDynamic.gridy = 15;
			gbcJCheckBoxDynamic.anchor = GridBagConstraints.WEST;
			gbcJCheckBoxDynamic.insets = new Insets(10, 10, 5, 0);
			GridBagConstraints gbcJPanelDynamic = new GridBagConstraints();
			gbcJPanelDynamic.gridx = 0;
			gbcJPanelDynamic.gridy = 16;
			gbcJPanelDynamic.fill = GridBagConstraints.NONE;
			gbcJPanelDynamic.weightx = 0.0;
			gbcJPanelDynamic.insets = new Insets(5, 32, 5, 10);
			gbcJPanelDynamic.anchor = GridBagConstraints.WEST;
			
			GridBagConstraints gbc_jSeparator05 = new GridBagConstraints();
			gbc_jSeparator05.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparator05.insets = new Insets(0, 10, 5, 100);
			gbc_jSeparator05.gridx = 0;
			gbc_jSeparator05.gridy = 17;
			
			GridBagConstraints gbcJCheckBoxThreshold = new GridBagConstraints();
			gbcJCheckBoxThreshold.gridx = 0;
			gbcJCheckBoxThreshold.gridy = 18;
			gbcJCheckBoxThreshold.insets = new Insets(10, 10, 5, 0);
			gbcJCheckBoxThreshold.anchor = GridBagConstraints.WEST;
			GridBagConstraints gbcJPanelThreshold = new GridBagConstraints();
			gbcJPanelThreshold.gridx = 0;
			gbcJPanelThreshold.gridy = 19;
			gbcJPanelThreshold.insets = new Insets(5, 32, 5, 10);
			gbcJPanelThreshold.anchor = GridBagConstraints.WEST;
			gbcJPanelThreshold.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraintsDummy = new GridBagConstraints();
			gridBagConstraintsDummy.gridx = 0;
			gridBagConstraintsDummy.gridy = 20;
			gridBagConstraintsDummy.fill = GridBagConstraints.NONE;
			gridBagConstraintsDummy.weighty = 1.0;
			gridBagConstraintsDummy.insets = new Insets(10, 10, 10, 10);
			gridBagConstraintsDummy.anchor = GridBagConstraints.NORTHWEST;
			
			GridBagLayout gbl_jPanelOnScrollPane = new GridBagLayout();
			gbl_jPanelOnScrollPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			gbl_jPanelOnScrollPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			
			
			jPanelOnScrollPane = new JPanel();
			jPanelOnScrollPane.setLayout(gbl_jPanelOnScrollPane);
			jPanelOnScrollPane.add(getJPanelRemoteConfig(), gbcJPanelRemoteConfig);
			jPanelOnScrollPane.add(getJSeparator01(), gbc_jSeparator01);
			jPanelOnScrollPane.add(getJCheckBoxShowLoadMonitor(), gbcJCheckBoxShowLoadMonitor);
			jPanelOnScrollPane.add(getJCheckBoxShowThreadMonitor(), gbcJCheckBoxShowThreadMonitor);
			jPanelOnScrollPane.add(getJCheckboxAutosaveRealMetricsOnSimStop(), gbcJCheckBoxAutosaveThreadMetricsOnSimStop);
			jPanelOnScrollPane.add(getJSeparator02(), gbc_jSeparator02);
			jPanelOnScrollPane.add(getJCheckBoxImmediatelyStartLoadRecording(), gbcJCheckBoxImmStartLoadRecording);
			jPanelOnScrollPane.add(getJPanelRecording(), gbcJPanelRecording);
			jPanelOnScrollPane.add(getJSeparator03(), gbc_jSeparator03);
			jPanelOnScrollPane.add(getJCheckBoxDoLoadStatic(), gbcJCheckBoxStatic);
			jPanelOnScrollPane.add(getJPanelStatic(), gbcJPanelStatic);
			jPanelOnScrollPane.add(getJPanelStaticClass(), gbcJPanelStaticClass);
			jPanelOnScrollPane.add(getJCheckBoxDoLoadDynamic(), gbcJCheckBoxDynamic);
			jPanelOnScrollPane.add(getJSeparator04(), gbc_jSeparator04);
			jPanelOnScrollPane.add(getJPanelDynamic(), gbcJPanelDynamic);
			jPanelOnScrollPane.add(getJCheckBoxThresholdDefinition(), gbcJCheckBoxThreshold);
			jPanelOnScrollPane.add(getJSeparator05(), gbc_jSeparator05);
			jPanelOnScrollPane.add(getJPanelThreshold(), gbcJPanelThreshold);
			jPanelOnScrollPane.add(getJPanelDummy(), gridBagConstraintsDummy);
		}
		return jPanelOnScrollPane;
	}
	/**
	 * Gets the JSeparator no 1.
	 * @return the jSeparator01
	 */
	private JSeparator getJSeparator01() {
		if (jSeparator01 == null) {
			jSeparator01 = new JSeparator();
		}
		return jSeparator01;
	}
	/**
	 * Gets the JSeparator no 2.
	 * @return the jSeparator02
	 */
	private JSeparator getJSeparator02() {
		if (jSeparator02 == null) {
			jSeparator02 = new JSeparator();
		}
		return jSeparator02;
	}
	
	/**
	 * Gets the j separator03.
	 * @return the j separator03
	 */
	private JSeparator getJSeparator03() {
		if (jSeparator03 == null) {
			jSeparator03 = new JSeparator();
		}
		return jSeparator03;
	}
	
	/**
	 * Gets the j separator04.
	 * @return the j separator04
	 */
	private JSeparator getJSeparator04() {
		if (jSeparator04 == null) {
			jSeparator04 = new JSeparator();
		}
		return jSeparator04;
	}
	
	/**
	 * Gets the j separator05.
	 * @return the j separator05
	 */
	private JSeparator getJSeparator05() {
		if (jSeparator05 == null) {
			jSeparator05 = new JSeparator();
		}
		return jSeparator05;
	}

	/**
	 * This method initializes jPanelRemoteConfig	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRemoteConfig() {
		if (jPanelRemoteConfig == null) {
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.gridx = 1;
			gridBagConstraints39.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints39.weightx = 0.5;
			gridBagConstraints39.gridy = 4;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.anchor = GridBagConstraints.WEST;
			gridBagConstraints32.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints32.gridx = 5;
			gridBagConstraints32.gridy = 4;
			gridBagConstraints32.weightx = 0.0;
			gridBagConstraints32.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.anchor = GridBagConstraints.EAST;
			gridBagConstraints34.gridx = 4;
			gridBagConstraints34.gridy = 4;
			gridBagConstraints34.insets = new Insets(5, 10, 0, 5);
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.anchor = GridBagConstraints.WEST;
			gridBagConstraints30.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints30.gridx = 3;
			gridBagConstraints30.gridy = 4;
			gridBagConstraints30.weightx = 0.0;
			gridBagConstraints30.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.anchor = GridBagConstraints.EAST;
			gridBagConstraints35.gridx = 2;
			gridBagConstraints35.gridy = 4;
			gridBagConstraints35.insets = new Insets(5, 10, 0, 5);
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.anchor = GridBagConstraints.WEST;
			gridBagConstraints33.gridwidth = 1;
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 4;
			gridBagConstraints33.weightx = 0.0;
			gridBagConstraints33.insets = new Insets(5, 22, 0, 0);
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints36.gridheight = 2;
			gridBagConstraints36.gridx = 5;
			gridBagConstraints36.gridy = 0;
			gridBagConstraints36.insets = new Insets(0, 20, 0, 0);
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.anchor = GridBagConstraints.WEST;
			gridBagConstraints38.gridx = -1;
			gridBagConstraints38.gridy = -1;
			gridBagConstraints38.gridwidth = 5;
			gridBagConstraints38.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.anchor = GridBagConstraints.WEST;
			gridBagConstraints37.gridwidth = 5;
			gridBagConstraints37.gridx = 0;
			gridBagConstraints37.gridy = 3;
			gridBagConstraints37.weightx = 0.0;
			gridBagConstraints37.insets = new Insets(8, 0, 0, 0);
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText(" ");
			jLabelMemoryMin = new JLabel();
			jLabelMemoryMin.setText("Init.");
			jLabelMemoryMax = new JLabel();
			jLabelMemoryMax.setText("Max.");
			jLabelMemoryAlloc = new JLabel();
			jLabelMemoryAlloc.setText("Arbeitsspeicher für Remote-JVM");
			jLabelMemoryAlloc.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelRemoteConfig = new JPanel();
			jPanelRemoteConfig.setLayout(new GridBagLayout());
			jPanelRemoteConfig.add(getJCheckBoxPreventUsageOfUsedComputers(), gridBagConstraints38);		
			jPanelRemoteConfig.add(getJCheckBoxShowRMA(), gridBagConstraints37);
			jPanelRemoteConfig.add(getJButtonRemoteDefault(), gridBagConstraints36);
			jPanelRemoteConfig.add(jLabelMemoryAlloc, gridBagConstraints33);
			jPanelRemoteConfig.add(jLabelMemoryMin, gridBagConstraints35);
			jPanelRemoteConfig.add(getJComboBoxJVMMemoryInitial(), gridBagConstraints30);
			jPanelRemoteConfig.add(jLabelMemoryMax, gridBagConstraints34);
			jPanelRemoteConfig.add(getJComboBoxJVMMemoryMaximum(), gridBagConstraints32);
			jPanelRemoteConfig.add(jLabelDummy, gridBagConstraints39);
		}
		return jPanelRemoteConfig;
	}
	
	/**
	 * This method initializes jButtonRemoteDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemoteDefault() {
		if (jButtonRemoteDefault == null) {
			jButtonRemoteDefault = new JButton();
			jButtonRemoteDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonRemoteDefault.setPreferredSize(new Dimension(45, 26));
			jButtonRemoteDefault.setToolTipText("Standard verwenden");
			jButtonRemoteDefault.addActionListener(this);
		}
		return jButtonRemoteDefault;
	}
	
	/**
	 * This method initializes jCheckBoxPreventUsageOfUsedComputers	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxPreventUsageOfUsedComputers() {
		if (jCheckBoxPreventUsageOfUsedComputers == null) {
			jCheckBoxPreventUsageOfUsedComputers = new JCheckBox();
			jCheckBoxPreventUsageOfUsedComputers.setText("Keine Computer nutzen, die bereits einen Remote-Container beherbergen");
			jCheckBoxPreventUsageOfUsedComputers.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxPreventUsageOfUsedComputers.addActionListener(this);
		}
		return jCheckBoxPreventUsageOfUsedComputers;
	}
	
	/**
	 * Gets the j check box is remote only.
	 * @return the j check box is remote only
	 */
	private JCheckBox getJCheckBoxIsRemoteOnly() {
		if (jCheckBoxIsRemoteOnly == null) {
			jCheckBoxIsRemoteOnly = new JCheckBox();
			jCheckBoxIsRemoteOnly.setText("Verteilung der Agenten nur auf Remote-Maschinen");
			jCheckBoxIsRemoteOnly.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxIsRemoteOnly.addActionListener(this);
		}
		return jCheckBoxIsRemoteOnly;
	}
	
	/**
	 * Gets the j check box is even distribution.
	 * @return the j check box is even distribution
	 */
	private JCheckBox getJCheckBoxIsEvenDistribution() {
		if (jCheckBoxIsEvenDistribution == null) {
			jCheckBoxIsEvenDistribution = new JCheckBox();
			jCheckBoxIsEvenDistribution.setText("Gleichmäßige Verteilung der Agenten auf alle verfügbaren Maschinen");
			jCheckBoxIsEvenDistribution.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxIsEvenDistribution.addActionListener(this);
		}
		return jCheckBoxIsEvenDistribution;
	}
	
	/**
	 * This method initializes jCheckBoxShowRMA	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxShowRMA() {
		if (jCheckBoxShowRMA == null) {
			jCheckBoxShowRMA = new JCheckBox();
			jCheckBoxShowRMA.setText("Remote JADE-RMA beim Start des entfernten Containers anzeigen");
			jCheckBoxShowRMA.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxShowRMA.addActionListener(this);
		}
		return jCheckBoxShowRMA;
	}

	/**
	 * This method initializes jComboBoxJVMMemoryInitial	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> getJComboBoxJVMMemoryInitial() {
		if (jComboBoxJVMMemoryInitial == null) {
			jComboBoxJVMMemoryInitial = new JComboBox<String>();
			jComboBoxJVMMemoryInitial.setModel(comboModelInitial);
			jComboBoxJVMMemoryInitial.setPreferredSize(new Dimension(80, 26));
			jComboBoxJVMMemoryInitial.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent ie) {
					if (ie.getStateChange()==ItemEvent.SELECTED) {
						@SuppressWarnings("unchecked")
						JComboBox<String> combo = (JComboBox<String>) ie.getSource();
						if (isMemorySelectionError(combo)==false) {
							// --- Save in project ------------
							currRemoteContainerConfiguration.setJvmMemAllocInitial((String) jComboBoxJVMMemoryInitial.getSelectedItem());
							currProject.setRemoteContainerConfiguration(currRemoteContainerConfiguration);	
						} else {
							// --- Set back to old value ------
							getJComboBoxJVMMemoryInitial().setSelectedItem(currRemoteContainerConfiguration.getJvmMemAllocInitial());
						}
					}
				}
			});
		}
		return jComboBoxJVMMemoryInitial;
	}

	/**
	 * This method initializes jComboBoxJVMMemoryMaximum	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> getJComboBoxJVMMemoryMaximum() {
		if (jComboBoxJVMMemoryMaximum == null) {
			jComboBoxJVMMemoryMaximum = new JComboBox<String>();
			jComboBoxJVMMemoryMaximum.setModel(comboModelMaximal);
			jComboBoxJVMMemoryMaximum.setPreferredSize(new Dimension(80, 26));
			jComboBoxJVMMemoryMaximum.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent ie) {
					if (ie.getStateChange()==ItemEvent.SELECTED) {
						@SuppressWarnings("unchecked")
						JComboBox<String> combo = (JComboBox<String>) ie.getSource();
						if (isMemorySelectionError(combo)==false) {
							// --- Save in project ------------
							currRemoteContainerConfiguration.setJvmMemAllocMaximum((String) jComboBoxJVMMemoryMaximum.getSelectedItem());
							currProject.setRemoteContainerConfiguration(currRemoteContainerConfiguration);
						} else {
							// --- Set back to old value ------
							getJComboBoxJVMMemoryMaximum().setSelectedItem(currRemoteContainerConfiguration.getJvmMemAllocMaximum());
						}
					}
				}
			});
		}
		return jComboBoxJVMMemoryMaximum;
	}

	/**
	 * Checks if is memory selection error. The initial memory should 
	 * always be larger than the maximum memory of the JVM.
	 * @return true, if is memory selection error
	 */
	private boolean isMemorySelectionError(JComboBox<String> combo) {
		
		boolean error = false;
		String initialMemory = (String) getJComboBoxJVMMemoryInitial().getSelectedItem();
		String maximumMemory = (String) getJComboBoxJVMMemoryMaximum().getSelectedItem();
		int initMem = Integer.parseInt(initialMemory.replaceAll("[a-zA-Z]", ""));
		int maxiMem = Integer.parseInt(maximumMemory.replaceAll("[a-zA-Z]", ""));
		if(initialMemory.contains("g")){
			initMem = initMem*1024;
		}
		if(maximumMemory.contains("g")){
			maxiMem = maxiMem*1024;
		}
		
		if (initMem>=maxiMem) {
			combo.hidePopup();
			String head = Language.translate("Initialer Arbeitsspeicher >= Maximaler Arbeitsspeicher !");
			String msg = Language.translate("Der maximale Arbeitsspeicher muss größer als der initiale Arbeitsspeicher sein.");
			JOptionPane.showMessageDialog(Application.getMainWindow(), msg, head, JOptionPane.ERROR_MESSAGE);
			error = true;
		}
		return error;
	}
	
	/**
	 * This method initializes jCheckBoxDoLoadStatic	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxDoLoadStatic() {
		if (jCheckBoxDoLoadStatic == null) {
			jCheckBoxDoLoadStatic = new JCheckBox();
			jCheckBoxDoLoadStatic.setText("Statische Lastverteilung aktivieren");
			jCheckBoxDoLoadStatic.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxDoLoadStatic.addActionListener(this);
		}
		return jCheckBoxDoLoadStatic;
	}

	/**
	 * This method initializes jCheckBoxDoLoadDynamic	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxDoLoadDynamic() {
		if (jCheckBoxDoLoadDynamic == null) {
			jCheckBoxDoLoadDynamic = new JCheckBox();
			jCheckBoxDoLoadDynamic.setText("Dynamische Lastverteilung aktivieren");
			jCheckBoxDoLoadDynamic.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxDoLoadDynamic.addActionListener(this);
		}
		return jCheckBoxDoLoadDynamic;
	}

	/**
	 * This method initializes jPanelStatic	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStatic() {
		if (jPanelStatic == null) {
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 2;
			gridBagConstraints27.anchor = GridBagConstraints.CENTER;
			gridBagConstraints27.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints27.fill = GridBagConstraints.NONE;
			gridBagConstraints27.gridy = 3;
			jLabelCalculation = new JLabel();
			jLabelCalculation.setText("");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 3;
			gridBagConstraints10.insets = new Insets(0, 5, 5, 0);
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(5, 0, 0, 5);
			gridBagConstraints9.gridy = 3;
			jLabelContainerExpected = new JLabel();
			jLabelContainerExpected.setText("Anzahl Container");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints8.gridy = 3;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(5, 10, 0, 5);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			gridBagConstraints7.gridy = 2;
			jLabelAgentsExpected = new JLabel();
			jLabelAgentsExpected.setText("Anzahl Agenten (erwartet)");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.insets = new Insets(0, 10, 5, 5);
			gridBagConstraints6.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.fill = GridBagConstraints.WEST;
			gridBagConstraints40.anchor = GridBagConstraints.WEST;
//			gridBagConstraints40.gridwidth = 5;
			gridBagConstraints40.gridx = 0;
			gridBagConstraints40.gridy = 0;
			gridBagConstraints40.weightx = 0.0;
			gridBagConstraints40.insets = new Insets(8, 0, 5, 5);
			
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = GridBagConstraints.WEST;
			gridBagConstraints41.anchor = GridBagConstraints.WEST;
//			gridBagConstraints41.gridwidth = 5;
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.gridy = 1;
			gridBagConstraints41.weightx = 0.0;
			gridBagConstraints41.insets = new Insets(8, 0, 5, 5);
			
			jPanelStatic = new JPanel();
			jPanelStatic.setLayout(new GridBagLayout());
//			jPanelStatic.setPreferredSize(new Dimension(500, 57));
			jPanelStatic.add(getJTextFieldAgentsExpected(), gridBagConstraints6);
			jPanelStatic.add(jLabelAgentsExpected, gridBagConstraints7);
			jPanelStatic.add(getJTextFieldContainerExpected(), gridBagConstraints8);
			jPanelStatic.add(jLabelContainerExpected, gridBagConstraints9);
			jPanelStatic.add(getJButtonCalcContainer(), gridBagConstraints10);
			jPanelStatic.add(jLabelCalculation, gridBagConstraints27);
			jPanelStatic.add(getJCheckBoxIsRemoteOnly(), gridBagConstraints40);
			jPanelStatic.add(getJCheckBoxIsEvenDistribution(), gridBagConstraints41);	
		}
		return jPanelStatic;
	}

	/**
	 * This method initializes jPanelStaticClass	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStaticClass() {
		if (jPanelStaticClass == null) {
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints28.gridy = 1;
			GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
			gridBagConstraints52.fill = GridBagConstraints.NONE;
			gridBagConstraints52.gridx = 2;
			gridBagConstraints52.gridy = 1;
			gridBagConstraints52.insets = new Insets(5, 5, 0, 0);
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridy = 1;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.fill = GridBagConstraints.VERTICAL;
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.anchor = GridBagConstraints.WEST;
			gridBagConstraints42.gridy = 0;
			gridBagConstraints42.gridwidth = 3;
			gridBagConstraints42.gridx = 0;
			jLabelStaticLoadClass = new JLabel();
			jLabelStaticLoadClass.setText("Java-Klasse für den Start der Simulations-Agenten und die statische Lastverteilung");
			jPanelStaticClass = new JPanel();
			jPanelStaticClass.setLayout(new GridBagLayout());
			jPanelStaticClass.add(jLabelStaticLoadClass, gridBagConstraints42);
			jPanelStaticClass.add(getJTextFieldStaticLoadClass(), gridBagConstraints31);
			jPanelStaticClass.add(getJButtonDefaultClassStatic(), gridBagConstraints52);
			jPanelStaticClass.add(getJButtonDefaultClassStaticCheck(), gridBagConstraints28);
		}
		return jPanelStaticClass;
	}
	/**
	 * This method initializes jTextFieldStaticLoadClass	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldStaticLoadClass() {
		if (jTextFieldStaticLoadClass == null) {
			jTextFieldStaticLoadClass = new JTextField();
			jTextFieldStaticLoadClass.setPreferredSize(new Dimension(400, 26));
			jTextFieldStaticLoadClass.setEditable(false);
			jTextFieldStaticLoadClass.getDocument().addDocumentListener(new DocumentListener(){
				@Override
				public void changedUpdate(DocumentEvent e) {
					checkBalancingMethod();
				}
			  	@Override
			  	public void removeUpdate(DocumentEvent e) {
//			  		checkBalancingMethod();
				}
			  	@Override
			  	public void insertUpdate(DocumentEvent e) {
					 checkBalancingMethod();
				}
	
			  	public void checkBalancingMethod() {
			  		if(jTextFieldStaticLoadClass.getText().equals("agentgui.simulationService.balancing.StaticLoadBalancing")){
			  			getJTextFieldAgentsExpected().setVisible(true);
			  			jLabelAgentsExpected.setVisible(true);
			  			getJTextFieldContainerExpected().setVisible(true);
			  			jLabelContainerExpected.setVisible(true);
			  			getJButtonCalcContainer().setVisible(true);
			  			jLabelCalculation.setVisible(true);
			  			getJCheckBoxIsRemoteOnly().setVisible(true);
			  			getJCheckBoxIsEvenDistribution().setVisible(false);
			  			
			  			
			  		}else if(jTextFieldStaticLoadClass.getText().equals("agentgui.simulationService.balancing.PredictiveStaticLoadBalancing")){
			  			getJTextFieldAgentsExpected().setVisible(false);
			  			jLabelAgentsExpected.setVisible(false);
			  			getJTextFieldContainerExpected().setVisible(false);
			  			jLabelContainerExpected.setVisible(false);
			  			getJButtonCalcContainer().setVisible(false);
			  			jLabelCalculation.setVisible(false);
			  			getJCheckBoxIsRemoteOnly().setVisible(true);
			  			currRemoteContainerConfiguration.setPreventUsageOfAlreadyUsedComputers(true);
			  			getJCheckBoxPreventUsageOfUsedComputers().setSelected(currRemoteContainerConfiguration.isPreventUsageOfAlreadyUsedComputers());
			  			getJCheckBoxIsEvenDistribution().setVisible(true);
			  		}
			  	}
			});
		}
		return jTextFieldStaticLoadClass;
	}
	/**
	 * This method initializes jButtonDefaultClassStatic	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClassStatic() {
		if (jButtonDefaultClassStatic == null) {
			jButtonDefaultClassStatic = new JButton();
			jButtonDefaultClassStatic.setToolTipText("Agent.GUI - Standard verwenden");			
			jButtonDefaultClassStatic.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClassStatic.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClassStatic.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonDefaultClassStatic.setActionCommand("StatLoadBalancingDefault");
			jButtonDefaultClassStatic.addActionListener(this);
		}
		return jButtonDefaultClassStatic;
	}
	
	/**
	 * This method initializes jButtonDefaultClassStaticCheck	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClassStaticCheck() {
		if (jButtonSelectStaticClass == null) {
			jButtonSelectStaticClass = new JButton();
			jButtonSelectStaticClass.setToolTipText("Klasse auswählen");
			jButtonSelectStaticClass.setPreferredSize(new Dimension(45, 26));
			jButtonSelectStaticClass.setIcon(GlobalInfo.getInternalImageIcon("Search.png"));
			jButtonSelectStaticClass.setActionCommand("StatLoadBalancingCheck");
			jButtonSelectStaticClass.addActionListener(this);
		}
		return jButtonSelectStaticClass;
	}
	
	/**
	 * This method initializes jPanelDynamic	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDynamic() {
		if (jPanelDynamic == null) {
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 1;
			gridBagConstraints29.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints29.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			gridBagConstraints5.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 0;
			jLabelDynamicLoadClass = new JLabel();
			jLabelDynamicLoadClass.setText("Java-Klasse für die dynamische Lastverteilung");
			jPanelDynamic = new JPanel();
			jPanelDynamic.setLayout(new GridBagLayout());
			jPanelDynamic.add(jLabelDynamicLoadClass, gridBagConstraints4);
			jPanelDynamic.add(getJTextFieldDynamicLoadClass(), gridBagConstraints3);
			jPanelDynamic.add(getJButtonDefaultClassDynamic(), gridBagConstraints5);
			jPanelDynamic.add(getJButtonDefaultClassDynamicCheck(), gridBagConstraints29);
		}
		return jPanelDynamic;
	}

	/**
	 * This method initializes jTextFieldDynamicLoadClass	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDynamicLoadClass() {
		if (jTextFieldDynamicLoadClass == null) {
			jTextFieldDynamicLoadClass = new JTextField();
			jTextFieldDynamicLoadClass.setPreferredSize(new Dimension(400, 26));
			jTextFieldDynamicLoadClass.setEditable(false);
		}
		return jTextFieldDynamicLoadClass;
	}

	/**
	 * This method initializes jButtonDefaultClassDynamic	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClassDynamic() {
		if (jButtonDefaultClassDynamic == null) {
			jButtonDefaultClassDynamic = new JButton();
			jButtonDefaultClassDynamic.setToolTipText("Agent.GUI - Standard verwenden");
			jButtonDefaultClassDynamic.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClassDynamic.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClassDynamic.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonDefaultClassDynamic.setActionCommand("DynLoadBalancingDefault");
			jButtonDefaultClassDynamic.addActionListener(this);
		}
		return jButtonDefaultClassDynamic;
	}
	/**
	 * This method initializes jButtonDefaultClassDynamicCheck	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClassDynamicCheck() {
		if (jButtonSelectDynamicClass == null) {
			jButtonSelectDynamicClass = new JButton();
			jButtonSelectDynamicClass.setToolTipText("Klasse auswählen");
			jButtonSelectDynamicClass.setPreferredSize(new Dimension(45, 26));
			jButtonSelectDynamicClass.setIcon(GlobalInfo.getInternalImageIcon("Search.png"));
			jButtonSelectDynamicClass.setActionCommand("DynLoadBalancingCheck");
			jButtonSelectDynamicClass.addActionListener(this);
		}
		return jButtonSelectDynamicClass;
	}
	
	/**
	 * This method initializes jTextFieldAgentsExpected	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldAgentsExpected() {
		if (jTextFieldAgentsExpected == null) {
			jTextFieldAgentsExpected = new JTextField();
			jTextFieldAgentsExpected.setPreferredSize(new Dimension(100, 26));
			jTextFieldAgentsExpected.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldAgentsExpected.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldAgentsExpected;
	}

	/**
	 * This method initializes jTextFieldContainerExpected	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldContainerExpected() {
		if (jTextFieldContainerExpected == null) {
			jTextFieldContainerExpected = new JTextField();
			jTextFieldContainerExpected.setPreferredSize(new Dimension(100, 26));
			jTextFieldContainerExpected.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldContainerExpected.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldContainerExpected;
	}

	/**
	 * This method initializes jButtonCalcContainer	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCalcContainer() {
		if (jButtonCalcContainer == null) {
			jButtonCalcContainer = new JButton();
			jButtonCalcContainer.setActionCommand("CalcNumberOfContainer");
			jButtonCalcContainer.setPreferredSize(new Dimension(200, 26));
			jButtonCalcContainer.setText("Anzahl Container berechnen");
			jButtonCalcContainer.addActionListener(this);
		}
		return jButtonCalcContainer;
	}

	/**
	 * This method initializes jCheckBoxThresholdDefinition	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxThresholdDefinition() {
		if (jCheckBoxThresholdDefinition == null) {
			jCheckBoxThresholdDefinition = new JCheckBox();
			jCheckBoxThresholdDefinition.setText("Eigene Auslastungsgrenzwerte verwenden");
			jCheckBoxThresholdDefinition.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxThresholdDefinition.addActionListener(this);
		}
		return jCheckBoxThresholdDefinition;
	}

	/**
	 * This method initializes jPanelThreshold	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelThreshold() {
		if (jPanelThreshold == null) {
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 7;
			gridBagConstraints26.insets = new Insets(5, 20, 0, 0);
			gridBagConstraints26.gridy = 2;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 2;
			gridBagConstraints25.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints25.gridy = 1;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 4;
			gridBagConstraints24.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints24.gridy = 1;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 6;
			gridBagConstraints23.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints23.gridy = 4;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 6;
			gridBagConstraints22.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints22.gridy = 3;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 6;
			gridBagConstraints21.insets = new Insets(0, 4, 0, 0);
			gridBagConstraints21.gridy = 2;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints20.gridy = 4;
			gridBagConstraints20.weightx = 1.0;
			gridBagConstraints20.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints20.gridx = 2;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints19.gridy = 4;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints19.gridx = 4;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints18.gridy = 3;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints18.gridx = 4;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints17.gridy = 3;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints17.gridx = 2;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.NONE;
			gridBagConstraints16.gridy = 2;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints16.gridx = 4;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.NONE;
			gridBagConstraints15.gridy = 2;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints15.gridx = 2;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints14.gridy = 4;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints13.gridy = 3;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = -1;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints12.gridy = 2;
			
			jLabelLow = new JLabel();
			jLabelLow.setText("Low");
			jLabelHigh = new JLabel();
			jLabelHigh.setText("High");
			
			jLabelCPU = new JLabel();
			jLabelCPU.setText("CPU-Auslastung");
			jLabelMemory = new JLabel();
			jLabelMemory.setText("JVM Heap-Memory");
			jLabelThreads = new JLabel();
			jLabelThreads.setText("N - Threads");
			
			jLabelCPUPercent = new JLabel();
			jLabelCPUPercent.setText("%");
			jLabelMemPercent = new JLabel();
			jLabelMemPercent.setText("%");
			jLabelThreadsNo = new JLabel();
			jLabelThreadsNo.setText("-");
			
			jPanelThreshold = new JPanel();
			jPanelThreshold.setLayout(new GridBagLayout());
			jPanelThreshold.add(jLabelCPU, gridBagConstraints12);
			jPanelThreshold.add(jLabelMemory, gridBagConstraints13);
			jPanelThreshold.add(jLabelThreads, gridBagConstraints14);
			jPanelThreshold.add(getJTextFieldCpuLow(), gridBagConstraints15);
			jPanelThreshold.add(getJTextFieldCpuHigh(), gridBagConstraints16);
			jPanelThreshold.add(getJTextFieldMemLow(), gridBagConstraints17);
			jPanelThreshold.add(getJTextFieldMemHigh(), gridBagConstraints18);
			jPanelThreshold.add(getJTextFieldThreadsHigh(), gridBagConstraints19);
			jPanelThreshold.add(getJTextFieldThreadsLow(), gridBagConstraints20);
			jPanelThreshold.add(jLabelCPUPercent, gridBagConstraints21);
			jPanelThreshold.add(jLabelMemPercent, gridBagConstraints22);
			jPanelThreshold.add(jLabelThreadsNo, gridBagConstraints23);
			jPanelThreshold.add(jLabelHigh, gridBagConstraints24);
			jPanelThreshold.add(jLabelLow, gridBagConstraints25);
			jPanelThreshold.add(getJButtonDefaultThreshold(), gridBagConstraints26);
		}
		return jPanelThreshold;
	}

	/**
	 * This method initializes jTextFieldCpuLow	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCpuLow() {
		if (jTextFieldCpuLow == null) {
			jTextFieldCpuLow = new JTextField();
			jTextFieldCpuLow.setPreferredSize(new Dimension(50, 26));
			jTextFieldCpuLow.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldCpuLow.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldCpuLow;
	}
	/**
	 * This method initializes jTextFieldCpuHigh	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCpuHigh() {
		if (jTextFieldCpuHigh == null) {
			jTextFieldCpuHigh = new JTextField();
			jTextFieldCpuHigh.setPreferredSize(new Dimension(50, 26));
			jTextFieldCpuHigh.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldCpuHigh.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldCpuHigh;
	}

	/**
	 * This method initializes jTextFieldMemLow	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMemLow() {
		if (jTextFieldMemLow == null) {
			jTextFieldMemLow = new JTextField();
			jTextFieldMemLow.setPreferredSize(new Dimension(50, 26));
			jTextFieldMemLow.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldMemLow.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldMemLow;
	}
	/**
	 * This method initializes jTextFieldMemHigh	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMemHigh() {
		if (jTextFieldMemHigh == null) {
			jTextFieldMemHigh = new JTextField();
			jTextFieldMemHigh.setPreferredSize(new Dimension(50, 26));
			jTextFieldMemHigh.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldMemHigh.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldMemHigh;
	}

	/**
	 * This method initializes jTextFieldThreadsHigh	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldThreadsHigh() {
		if (jTextFieldThreadsHigh == null) {
			jTextFieldThreadsHigh = new JTextField();
			jTextFieldThreadsHigh.setPreferredSize(new Dimension(50, 26));
			jTextFieldThreadsHigh.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldThreadsHigh.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldThreadsHigh;
	}
	/**
	 * This method initializes jTextFieldThreadsLow	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldThreadsLow() {
		if (jTextFieldThreadsLow == null) {
			jTextFieldThreadsLow = new JTextField();
			jTextFieldThreadsLow.setPreferredSize(new Dimension(50, 26));
			jTextFieldThreadsLow.addKeyListener(this.getKeyAdapter4Numbers());
			jTextFieldThreadsLow.getDocument().addDocumentListener(this.getDocumentListener4IntegerJTextField());
		}
		return jTextFieldThreadsLow;
	}

	/**
	 * Returns the JPanel for the recording interval.
	 * @return the jPanelRecording
	 */
	private JPanel getJPanelRecording() {
		if (jPanelRecording==null) {
			jPanelRecording = new JPanel();
			jPanelRecording.setLayout(new GridBagLayout());
			
			GridBagConstraints gridBagConstraintsLabel = new GridBagConstraints();
			gridBagConstraintsLabel.gridx = 0;
			gridBagConstraintsLabel.gridy = 0;
			gridBagConstraintsLabel.insets = new Insets(0, 0, 0, 0);
			gridBagConstraintsLabel.anchor = GridBagConstraints.WEST;
			
			GridBagConstraints gridBagConstraintsCombo = new GridBagConstraints();
			gridBagConstraintsCombo.fill = GridBagConstraints.NONE;
			gridBagConstraintsCombo.gridx = 1;
			gridBagConstraintsCombo.gridy = 0;
			gridBagConstraintsCombo.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraintsCombo.weightx = 1.0;
			gridBagConstraintsCombo.weighty = 1.0;
			gridBagConstraintsCombo.insets = new Insets(0, 5, 0, 0);
			
			jLabelRecording = new JLabel("Abtastintervall");
			
			jPanelRecording.add(jLabelRecording, gridBagConstraintsLabel);
			jPanelRecording.add(getJComboBoxRecordingInterval(), gridBagConstraintsCombo);
		}
		return jPanelRecording;
	}
	/**
	 * Gets the JCheckBox show load monitor.
	 * @return the JCheckBox show load monitor
	 */
	private JCheckBox getJCheckBoxShowLoadMonitor() {
		if (jCheckBoxShowLoadMonitor == null) {
			jCheckBoxShowLoadMonitor = new JCheckBox();
			jCheckBoxShowLoadMonitor.setText("Auslastungs-Monitor bei JADE-Start anzeigen");
			jCheckBoxShowLoadMonitor.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxShowLoadMonitor.addActionListener(this);
		}
		return jCheckBoxShowLoadMonitor;
	}
	/**
	 * Gets the j check box show thread monitor.
	 * @return the j check box show thread monitor
	 */
	private JCheckBox getJCheckBoxShowThreadMonitor() {
		if (jCheckBoxShowThreadMonitor == null) {
			jCheckBoxShowThreadMonitor = new JCheckBox();
			jCheckBoxShowThreadMonitor.setText("Thread-Monitor bei JADE-Start anzeigen");
			jCheckBoxShowThreadMonitor.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxShowThreadMonitor.addActionListener(this);
		}
		return jCheckBoxShowThreadMonitor;
	}
	
	/**
	 * Gets the j checkbox autosave real metrics on sim stop.
	 * @return the j checkbox autosave real metrics on sim stop
	 */
	private JCheckBox getJCheckboxAutosaveRealMetricsOnSimStop() {
		if (jCheckboxAutosaveRealMetricsOnSimStop == null) {
			jCheckboxAutosaveRealMetricsOnSimStop = new JCheckBox();
			jCheckboxAutosaveRealMetricsOnSimStop.setText(autosaveRealMetricsOnSimStopString);
			jCheckboxAutosaveRealMetricsOnSimStop.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckboxAutosaveRealMetricsOnSimStop.addActionListener(this);
		}
		return jCheckboxAutosaveRealMetricsOnSimStop;
	}
	/**
	 * This method initializes jCheckBoxDoLoadStatic	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxImmediatelyStartLoadRecording() {
		if (jCheckBoxImmediatelyStartLoadRecording == null) {
			jCheckBoxImmediatelyStartLoadRecording = new JCheckBox();
			jCheckBoxImmediatelyStartLoadRecording.setText("Lastaufzeichnung mit dem Start von JADE beginnen");
			jCheckBoxImmediatelyStartLoadRecording.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxImmediatelyStartLoadRecording.addActionListener(this);
		}
		return jCheckBoxImmediatelyStartLoadRecording;
	}
	/**
	 * This method initializes jComboBoxInterval.
	 * @return javax.swing.JComboBox
	 */
	private JComboBox<TimeSelection> getJComboBoxRecordingInterval() {
		if (jComboBoxRecordingInterval == null) {
			jComboBoxRecordingInterval = new JComboBox<TimeSelection>(this.getComboBoxModelRecordingInterval());
			jComboBoxRecordingInterval.setMaximumRowCount(this.getComboBoxModelRecordingInterval().getSize());
			jComboBoxRecordingInterval.setSelectedItem(this.getComboBoxModelRecordingInterval().getElementAt(0));
			jComboBoxRecordingInterval.setPreferredSize(new Dimension(60, 26));
			jComboBoxRecordingInterval.setToolTipText(Language.translate("Abtastintervall"));
			jComboBoxRecordingInterval.addActionListener(this);
		}
		return jComboBoxRecordingInterval;
	}
	/**
	 * Returns DefaultComboBoxModel of sampling interval.
	 * @return the default combo box model
	 */
	private DefaultComboBoxModel<TimeSelection> getComboBoxModelRecordingInterval() {
		if (comboModelRecordingInterval==null) {
			comboModelRecordingInterval = new DefaultComboBoxModel<TimeSelection>();
			comboModelRecordingInterval.addElement(new TimeSelection(500));
			
			comboModelRecordingInterval.addElement(new TimeSelection(1000));
			comboModelRecordingInterval.addElement(new TimeSelection(2000));
			comboModelRecordingInterval.addElement(new TimeSelection(3000));
			comboModelRecordingInterval.addElement(new TimeSelection(4000));
			comboModelRecordingInterval.addElement(new TimeSelection(5000));
			comboModelRecordingInterval.addElement(new TimeSelection(6000));
			comboModelRecordingInterval.addElement(new TimeSelection(7000));
			comboModelRecordingInterval.addElement(new TimeSelection(8000));
			comboModelRecordingInterval.addElement(new TimeSelection(9000));
			comboModelRecordingInterval.addElement(new TimeSelection(10000));
			
			comboModelRecordingInterval.addElement(new TimeSelection(15000));
			comboModelRecordingInterval.addElement(new TimeSelection(20000));
			comboModelRecordingInterval.addElement(new TimeSelection(30000));
			comboModelRecordingInterval.addElement(new TimeSelection(60000));	
		}
		return comboModelRecordingInterval;
	}

	/**
	 * Sets the recording interval.
	 * @param timeInMillis the new recording interval
	 */
	private void setRecordingInterval(long timeInMillis) {
		for (int i = 0; i < this.getComboBoxModelRecordingInterval().getSize(); i++) {
			TimeSelection timeSelection = (TimeSelection) this.getComboBoxModelRecordingInterval().getElementAt(i); 
			if (timeSelection.getTimeInMill()==timeInMillis) {
				this.getComboBoxModelRecordingInterval().setSelectedItem(timeSelection);
				break;
			}
		}
	}
	
	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
			jPanelDummy.setPreferredSize(new Dimension(200, 12));
			jPanelDummy.setVisible(true);
		}
		return jPanelDummy;
	}

	/**
	 * This method initializes jButtonDefaultThreshold	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultThreshold() {
		if (jButtonDefaultThreshold == null) {
			jButtonDefaultThreshold = new JButton();
			jButtonDefaultThreshold.setActionCommand("ThresholdDefault");
			jButtonDefaultThreshold.setText("Default");
			jButtonDefaultThreshold.addActionListener(this);
		}
		return jButtonDefaultThreshold;
	}
	
	/**
	 * Gets the key adapter4 numbers.
	 * @return the key adapter4 numbers
	 */
	private KeyAdapter4Numbers getKeyAdapter4Numbers() {
		if (keyAdapter4Numbers==null) {
			keyAdapter4Numbers = new KeyAdapter4Numbers(false);
		}
		return keyAdapter4Numbers;
	}
	/**
	 * Gets the document listener for integer JTextField.
	 * @return the docListener
	 */
	private DocumentListener getDocumentListener4IntegerJTextField() {
		if (docListener==null) {
			docListener = new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent de) {
					this.setDocChanges(de);
				}
				@Override
				public void insertUpdate(DocumentEvent de) {
					this.setDocChanges(de);
				}
				@Override
				public void changedUpdate(DocumentEvent de) {
					this.setDocChanges(de);
				}
				/**
				 * Sets the doc changes to the distribution setup.
				 * @param de the new doc changes
				 */
				private void setDocChanges(DocumentEvent de) {
					
					if (pauseDocumentListener==false) {
						// --- Save document changes --------------------------
						int number = 0;
						Document srcDoc = de.getDocument();

						String numberText=null;
						try {
							numberText = srcDoc.getText(0, srcDoc.getLength());
						} catch (BadLocationException ble) {
							ble.printStackTrace();
						}
						if (numberText!=null && numberText.equals("")==false) {
							number = Integer.parseInt(numberText);	
						} 
						
						// --- Save new value in the distribution setup -------
						if (srcDoc==getJTextFieldAgentsExpected().getDocument()) {
							currDistributionSetup.setNumberOfAgents(number);
						} else if (srcDoc==getJTextFieldContainerExpected().getDocument()) {
							currDistributionSetup.setNumberOfContainer(number);
							
						} else if (srcDoc==getJTextFieldCpuLow().getDocument()) {
							currUserThresholds.setThCpuL(number);
						} else if (srcDoc==getJTextFieldCpuHigh().getDocument()) {
							currUserThresholds.setThCpuH(number);
							
						} else if (srcDoc==getJTextFieldMemLow().getDocument()) {
							currUserThresholds.setThMemoL(number);
						} else if (srcDoc==getJTextFieldMemHigh().getDocument()) {
							currUserThresholds.setThMemoH(number);
							
						} else if (srcDoc==getJTextFieldThreadsLow().getDocument()) {
							currUserThresholds.setThNoThreadsL(number);
						} else if (srcDoc==getJTextFieldThreadsHigh().getDocument()) {
							currUserThresholds.setThNoThreadsH(number);
						}
						pauseObserver=true;
						currProject.setDistributionSetup(currDistributionSetup);
						pauseObserver=false;
					}
				}
			};
		}
		return docListener;
	}
	
	// --------------------------------------------------------------
	// --------------------------------------------------------------
	/**
	 * Sets the default values for the selected fields. Use constant
	 * named "DEFAULT_VALUES_4_...." from this class
	 */
	private void setDefaults(int values2Set) {
		
		switch (values2Set) {
		case STATIC_BALANCING_CLASS:
			this.jTextFieldStaticLoadClass.setText(DistributionSetup.DEFAULT_StaticLoadBalancingClass);
			this.currDistributionSetup.setStaticLoadBalancingClass(this.jTextFieldStaticLoadClass.getText());
			break;
			
		case DYNAMIC_BALANCING_CLASS:
			this.jTextFieldDynamicLoadClass.setText(DistributionSetup.DEFAULT_DynamicLoadBalancingClass);
			this.currDistributionSetup.setDynamicLoadBalancingClass(this.jTextFieldDynamicLoadClass.getText());
			break;

		case THRESHOLD_LEVEL:
			this.currDistributionSetup.setUseUserThresholds(true);
			this.currDistributionSetup.setUserThresholds(new LoadThresholdLevels());
		}
		this.currProject.setDistributionSetup(this.currDistributionSetup);
	}
	
	/**
	 * Loads the current DistributionSetup from the current project
	 */
	private void setupLoad() {
		
		this.pauseDocumentListener = true;
		
		this.currDistributionSetup = this.currProject.getDistributionSetup();
		this.currUserThresholds = this.currDistributionSetup.getUserThresholds();
		this.currRemoteContainerConfiguration = this.currProject.getRemoteContainerConfiguration();
		
		this.jCheckBoxPreventUsageOfUsedComputers.setSelected(currRemoteContainerConfiguration.isPreventUsageOfAlreadyUsedComputers());
		this.jCheckBoxIsRemoteOnly.setSelected(currDistributionSetup.isRemoteOnly());
		this.jCheckBoxIsEvenDistribution.setSelected(currDistributionSetup.isEvenDistribution());
		this.jCheckBoxShowRMA.setSelected(currRemoteContainerConfiguration.isShowJADErmaGUI());
		this.jComboBoxJVMMemoryMaximum.setSelectedItem(currRemoteContainerConfiguration.getJvmMemAllocMaximum());
		this.jComboBoxJVMMemoryInitial.setSelectedItem(currRemoteContainerConfiguration.getJvmMemAllocInitial());
		
		this.jCheckBoxDoLoadStatic.setSelected(currDistributionSetup.isDoStaticLoadBalancing());
		this.jTextFieldAgentsExpected.setText(((Integer)currDistributionSetup.getNumberOfAgents()).toString());
		this.jTextFieldContainerExpected.setText(((Integer)currDistributionSetup.getNumberOfContainer()).toString());
		this.jTextFieldStaticLoadClass.setText(currDistributionSetup.getStaticLoadBalancingClass());
		
		this.jCheckBoxDoLoadDynamic.setSelected(currDistributionSetup.isDoDynamicLoadBalancing());
		this.jTextFieldDynamicLoadClass.setText(currDistributionSetup.getDynamicLoadBalancingClass());
		
		this.jCheckBoxThresholdDefinition.setSelected(currDistributionSetup.isUseUserThresholds());
		this.jTextFieldCpuLow.setText(((Integer)currUserThresholds.getThCpuL()).toString());
		this.jTextFieldCpuHigh.setText(((Integer)currUserThresholds.getThCpuH()).toString());
		this.jTextFieldMemLow.setText(((Integer)currUserThresholds.getThMemoL()).toString());
		this.jTextFieldMemHigh.setText(((Integer)currUserThresholds.getThMemoH()).toString());
		this.jTextFieldThreadsLow.setText(((Integer)currUserThresholds.getThNoThreadsL()).toString());
		this.jTextFieldThreadsHigh.setText(((Integer)currUserThresholds.getThNoThreadsH()).toString());
		
		this.jCheckBoxShowLoadMonitor.setSelected(currDistributionSetup.isShowLoadMonitorAtPlatformStart());
		this.jCheckBoxShowThreadMonitor.setSelected(currDistributionSetup.isShowThreadMonitorAtPlatformStart());
		this.jCheckboxAutosaveRealMetricsOnSimStop.setSelected(currDistributionSetup.isAutoSaveRealMetricsOnSimStop());
		this.jCheckBoxImmediatelyStartLoadRecording.setSelected(currDistributionSetup.isImmediatelyStartLoadRecording());
		this.setRecordingInterval(currDistributionSetup.getLoadRecordingInterval());
		
		this.jLabelCalculation.setText("");
		
		this.pauseDocumentListener = false;

	}
	
	/**
	 * This method can be used to open the class-selector in order to
	 * select a customized static or dynamic load balancing class
	 * @param values2Set
	 */
	private void setBalancingClass(int balancingType) {
		
		Class<?> search4Class = null;
		String 	 search4CurrentValue = null;
		String 	 search4DefaultValue = null;
		String   search4Description = null;
		
		switch (balancingType) {
		case STATIC_BALANCING_CLASS:
			search4Class = StaticLoadBalancingBase.class;
			search4CurrentValue = currDistributionSetup.getStaticLoadBalancingClass();
			search4DefaultValue = StaticLoadBalancing.class.getName();
			search4Description = jLabelStaticLoadClass.getText();
			break;
			
		case DYNAMIC_BALANCING_CLASS:
			search4Class = DynamicLoadBalancingBase.class;
			search4CurrentValue = currDistributionSetup.getDynamicLoadBalancingClass();
			search4DefaultValue = DynamicLoadBalancing.class.getName();
			search4Description = jLabelDynamicLoadClass.getText();
			break;
		}
		
		ClassSelectionDialog cs = new ClassSelectionDialog(Application.getMainWindow(), search4Class, search4CurrentValue, search4DefaultValue, search4Description, false);
		cs.setVisible(true);
		// --- act in the dialog ... --------------------
		if (cs.isCanceled()==true) return;
		
		// ----------------------------------------------
		// --- Class was selected. Proceed it -----------
		String classSelected = cs.getClassSelected();
		cs.dispose();
		cs = null;
		// ----------------------------------------------
		
		if (classSelected.equals("")==false) {
			switch (balancingType) {
			case STATIC_BALANCING_CLASS:
				currDistributionSetup.setStaticLoadBalancingClass(classSelected);
				jTextFieldStaticLoadClass.setText(classSelected);
				break;
				
			case DYNAMIC_BALANCING_CLASS:
				currDistributionSetup.setDynamicLoadBalancingClass(classSelected);
				jTextFieldDynamicLoadClass.setText(classSelected);
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	
		Object trigger = ae.getSource();
		if (trigger==jCheckBoxIsRemoteOnly) {
			this.currDistributionSetup.setRemoteOnly(this.getJCheckBoxIsRemoteOnly().isSelected());
		}else if (trigger==jCheckBoxIsEvenDistribution) {
			this.currDistributionSetup.setEvenDistribution(this.getJCheckBoxIsEvenDistribution().isSelected());
		} else if (trigger==jCheckBoxPreventUsageOfUsedComputers) {
			this.currRemoteContainerConfiguration.setPreventUsageOfAlreadyUsedComputers(this.getJCheckBoxPreventUsageOfUsedComputers().isSelected());
			this.currProject.setRemoteContainerConfiguration(currRemoteContainerConfiguration);
		} else if (trigger==jCheckBoxShowRMA) {
			this.currRemoteContainerConfiguration.setShowJADErmaGUI(this.getJCheckBoxShowRMA().isSelected());
			this.currProject.setRemoteContainerConfiguration(currRemoteContainerConfiguration);
		} else if (trigger==jButtonRemoteDefault) {
			
			this.currRemoteContainerConfiguration = new RemoteContainerConfiguration();
			this.getJCheckBoxPreventUsageOfUsedComputers().setSelected(currRemoteContainerConfiguration.isPreventUsageOfAlreadyUsedComputers());
			this.getJCheckBoxShowRMA().setSelected(this.currRemoteContainerConfiguration.isShowJADErmaGUI());
			this.getJComboBoxJVMMemoryInitial().setSelectedItem(this.currRemoteContainerConfiguration.getJvmMemAllocInitial());
			this.getJComboBoxJVMMemoryMaximum().setSelectedItem(this.currRemoteContainerConfiguration.getJvmMemAllocMaximum());
			this.currProject.setRemoteContainerConfiguration(this.currRemoteContainerConfiguration);
			
			this.currDistributionSetup = new DistributionSetup();
			this.getJCheckBoxIsRemoteOnly().setSelected(currDistributionSetup.isRemoteOnly());
			this.getJCheckBoxIsEvenDistribution().setSelected(currDistributionSetup.isEvenDistribution());
			this.currProject.setDistributionSetup(currDistributionSetup);

		} else if (trigger==jButtonCalcContainer) {
			// --- calculate the number of container required -------
			int noAgents = Integer.parseInt(jTextFieldAgentsExpected.getText());
			int noAgentsMax = Integer.parseInt(jTextFieldThreadsHigh.getText());
			int noContainer = (int) Math.ceil(((float)noAgents / (float)noAgentsMax)) + 1;
			jTextFieldContainerExpected.setText(((Integer)noContainer).toString());
			String calculation = "Math.ceil(" + noAgents + " / " + noAgentsMax + ") + 1 = " + noContainer;
			jLabelCalculation.setText(calculation);
			
			currDistributionSetup.setNumberOfAgents(noAgents);
			currDistributionSetup.setNumberOfContainer(noContainer);
			
		} else if (trigger==jButtonDefaultClassStatic) {
			this.setDefaults(STATIC_BALANCING_CLASS);
		} else if (trigger==jButtonDefaultClassDynamic) { 
			this.setDefaults(DYNAMIC_BALANCING_CLASS);
		} else if (trigger==jButtonDefaultThreshold) {
			this.setDefaults(THRESHOLD_LEVEL);
			
		} else if (trigger==jButtonSelectStaticClass) {
			this.setBalancingClass(STATIC_BALANCING_CLASS);			
		} else if (trigger==jButtonSelectDynamicClass) {
			this.setBalancingClass(DYNAMIC_BALANCING_CLASS);
			
		} else if (trigger==jCheckBoxDoLoadStatic) {
			currDistributionSetup.setDoStaticLoadBalancing(jCheckBoxDoLoadStatic.isSelected());
		} else if (trigger==jCheckBoxDoLoadDynamic) {
			currDistributionSetup.setDoDynamicLoadBalancing(jCheckBoxDoLoadDynamic.isSelected());
		} else if (trigger==jCheckBoxThresholdDefinition) {
			currDistributionSetup.setUseUserThresholds(jCheckBoxThresholdDefinition.isSelected());
		} else if (trigger==jCheckBoxShowLoadMonitor) {
			currDistributionSetup.setShowLoadMonitorAtPlatformStart(jCheckBoxShowLoadMonitor.isSelected());
		} else if (trigger==jCheckBoxShowThreadMonitor) {
			currDistributionSetup.setShowThreadMonitorAtPlatformStart(jCheckBoxShowThreadMonitor.isSelected());
		} else if (trigger==jCheckboxAutosaveRealMetricsOnSimStop){
			currDistributionSetup.setAutoSaveRealMetricsOnSimStop(jCheckboxAutosaveRealMetricsOnSimStop.isSelected());
		} else if (trigger==jCheckBoxImmediatelyStartLoadRecording) {
			currDistributionSetup.setImmediatelyStartLoadRecording(jCheckBoxImmediatelyStartLoadRecording.isSelected());
		} else if (trigger==jComboBoxRecordingInterval) {
			TimeSelection timeSelection = (TimeSelection) jComboBoxRecordingInterval.getSelectedItem();
			currDistributionSetup.setLoadRecordingInterval(timeSelection.getTimeInMill());
			
		} else {
			//System.err.println("Action nicht implementiert: " + ae.getActionCommand());
		}
		currProject.setDistributionSetup(this.currDistributionSetup);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object notifyObject) {
		
		if (notifyObject==Project.CHANGED_DistributionSetup) {
			if (pauseObserver==false) {
				this.setupLoad();	
			}
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
