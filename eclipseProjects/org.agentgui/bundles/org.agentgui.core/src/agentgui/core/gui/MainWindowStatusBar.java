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
package agentgui.core.gui;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.BundleProperties;
import agentgui.core.config.GlobalInfo;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadUnits;
import agentgui.simulationService.load.monitoring.AbstractMonitoringTask;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;

/**
 * The MainWindowStatusBar.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MainWindowStatusBar extends JPanel {

	private static final long serialVersionUID = -575684753041100082L;
	
	public enum JadeStatusColor {
		Green("JADE wurde lokal gestartet.",	"StatGreen.png"),
		Yellow("JADE wird gestartet ... ", 		"StatYellow.png"),
		Red("JADE wurde noch nicht gestartet.", "StatRed.png");
		
		private String deDescription;
		private String imageName; 
		
		private ImageIcon icon;
		private JadeStatusColor(String deDescription, String imageName) {
			this.deDescription = deDescription;
			this.imageName   = imageName;
		}
		public String getDescription() {
			return Language.translate(this.deDescription);
		}
		public ImageIcon getImageIcon() {
			if (icon==null) {
				icon = GlobalInfo.getInternalImageIcon(this.imageName);
			}
			return icon;
		}
	}
	
	private JLabel jLabelStatusText;

	private HeapUsageMonitoringTask heapStatusMonitoringTask;
	private JToolBar jToolBarSystemLoad;
	private JProgressBar jProgressBarHeapUsage;
	private JToolBar jToolBarCenter;

	private JLabel jLabelJadeState;
	
	private HashMap<String, JLabel> databaseStateHashMap;
	
	
	/**
	 * Instantiates a new main window status bar.
	 */
	public MainWindowStatusBar() {
		this.initialize();
	}
	/**
	 * Initializes this JPanel.
	 */
	private void initialize() {
		
		this.setPreferredSize(new Dimension(1071, 22));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{300, 0, 300, 200, 0};
		gridBagLayout.rowHeights = new int[]{22, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelStatusText = new GridBagConstraints();
		gbc_jLabelStatusText.anchor = GridBagConstraints.WEST;
		gbc_jLabelStatusText.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelStatusText.gridx = 0;
		gbc_jLabelStatusText.gridy = 0;
		this.add(this.getJLabelStatusText(), gbc_jLabelStatusText);
		
		GridBagConstraints gbc_jPanelSystemLoad = new GridBagConstraints();
		gbc_jPanelSystemLoad.fill = GridBagConstraints.BOTH;
		gbc_jPanelSystemLoad.gridx = 1;
		gbc_jPanelSystemLoad.gridy = 0;
		add(this.getJToolBarSystemLoad(), gbc_jPanelSystemLoad);
		
		GridBagConstraints gbc_jPanelCenter = new GridBagConstraints();
		gbc_jPanelCenter.fill = GridBagConstraints.BOTH;
		gbc_jPanelCenter.gridx = 2;
		gbc_jPanelCenter.gridy = 0;
		this.add(this.getJToolBarCenter(), gbc_jPanelCenter);
		
		GridBagConstraints gbc_jLabelJadeState = new GridBagConstraints();
		gbc_jLabelJadeState.anchor = GridBagConstraints.WEST;
		gbc_jLabelJadeState.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelJadeState.gridx = 3;
		gbc_jLabelJadeState.gridy = 0;
		this.add(this.getJLabelJadeState(), gbc_jLabelJadeState);
		
		this.setJadeStatusColor(JadeStatusColor.Red);
	}
	
	/**
	 * Returns the LEFT JLabel for the status text.
	 * @return the jLabelStatusText
	 */
	private JLabel getJLabelStatusText() {
		if (jLabelStatusText == null) {
			jLabelStatusText = new JLabel("  Starting " + Application.getGlobalInfo().getApplicationTitle() + " ...");
			jLabelStatusText.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelStatusText;
	}
	/**
	 * Sets a text in the applications status bar.
	 * @param message the new status bar
	 */
	public void setStatusBarMessage(String message) {
		if (message == null) {
			this.getJLabelStatusText().setText("  ");
		} else {
			this.getJLabelStatusText().setText("  " + message);
		}
		this.getJLabelStatusText().validate();
		this.getJLabelStatusText().repaint();
	}
	
	private JToolBar getJToolBarSystemLoad() {
		if (jToolBarSystemLoad== null) {
			jToolBarSystemLoad = new JToolBar();
			jToolBarSystemLoad.setFloatable(false);
			jToolBarSystemLoad.setBorder(BorderFactory.createEmptyBorder());
			jToolBarSystemLoad.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jToolBarSystemLoad.addSeparator();
			jToolBarSystemLoad.add(this.getJProgressBarHeapUsage());

			Dimension progressBarSize = this.getJProgressBarHeapUsage().getPreferredSize();
			Dimension toolbarSize = new Dimension(progressBarSize.width + 10, progressBarSize.height);
			jToolBarSystemLoad.setPreferredSize(toolbarSize);
			jToolBarSystemLoad.setMinimumSize(toolbarSize);
		}
		return jToolBarSystemLoad;
	}
	private JProgressBar getJProgressBarHeapUsage() {
		if (jProgressBarHeapUsage == null) {
			jProgressBarHeapUsage = new JProgressBar();
			jProgressBarHeapUsage.setPreferredSize(new Dimension(180, 20));
			jProgressBarHeapUsage.setFont(new Font("Dialog", Font.PLAIN, 12));
			jProgressBarHeapUsage.setStringPainted(true);
			jProgressBarHeapUsage.setToolTipText("JVM Heap-Usage: ...");
			jProgressBarHeapUsage.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (SwingUtilities.isLeftMouseButton(me) && me.getClickCount()==2) {
						Application.startGarbageCollection();
					}
				}
			});
			// --- Set the progress bar visible? ------------------------------
			boolean setProgressBarVisible = Application.getGlobalInfo().getBooleanFromConfiguration(BundleProperties.DEF_SHOW_HEAP_MONITOR, true);
			if (setProgressBarVisible==true) {
				// --- Register heap measurement as monitoring task -----------
				this.createAndRegisterHeapStatusMonitoringTask();
				this.updateHeapUsage();
			}
			jProgressBarHeapUsage.setVisible(setProgressBarVisible);
		}
		return jProgressBarHeapUsage;
	}

	/**
	 * Switches the heap monitor visibility.
	 */
	public void doSwitchHeapMonitorVisibility() {
		this.setHeapMonitorVisible(!this.getJProgressBarHeapUsage().isVisible());
	}
	/**
	 * Can be invoked to set the heap status visible or not.
	 * @param setVisible the new heap status visible
	 */
	public void setHeapMonitorVisible(boolean setVisible) {
		
		this.getJProgressBarHeapUsage().setVisible(setVisible);
		
		// --- Start / Stop the monitoring task -----------
		if (setVisible==true) {
			this.createAndRegisterHeapStatusMonitoringTask();
		} else {
			HeapUsageMonitoringTask task = this.getHeapStatusMonitoringTask();
			if (task!=null) {
				task.unregisterTask();
				this.setHeapStatusMonitoringTask(null);
			}
		}
		// --- Set to core bundle properties --------------
		Application.getGlobalInfo().putBooleanToConfiguration(BundleProperties.DEF_SHOW_HEAP_MONITOR, setVisible);
	}
	/**
	 * Updates the progress of the heap usage indicator.
	 */
	private void updateHeapUsage() {

		double jvmHeapMax = LoadUnits.bytes2(LoadMeasureThread.getCurrentLoadMeasureJVM().getJvmHeapMax(), LoadUnits.CONVERT2_GIGA_BYTE);
		double jvmHeapUsed = LoadUnits.bytes2(LoadMeasureThread.getCurrentLoadMeasureJVM().getJvmHeapUsed(), LoadUnits.CONVERT2_GIGA_BYTE);
		
		double relative = jvmHeapUsed / jvmHeapMax;
		int percentage = (int) (relative * 100.0);
		
		this.getJProgressBarHeapUsage().setValue(percentage);
		this.getJProgressBarHeapUsage().setToolTipText("JVM Heap-Usage: " + percentage + " % of " + jvmHeapMax + " GB");
	}
	/**
	 * Returns the heap status monitoring task.
	 * @return the heap status monitoring task
	 */
	private void createAndRegisterHeapStatusMonitoringTask() {
		if (heapStatusMonitoringTask==null) {
			heapStatusMonitoringTask = new HeapUsageMonitoringTask(this);
			heapStatusMonitoringTask.registerTask();
		}
	}
	/**
	 * Returns the heap status monitoring task.
	 * @return the heap status monitoring task
	 */
	private HeapUsageMonitoringTask getHeapStatusMonitoringTask() {
		return heapStatusMonitoringTask;
	}
	/**
	 * Sets the heap status monitoring task.
	 * @param heapStatusMonitoringTask the new heap status monitoring task
	 */
	private void setHeapStatusMonitoringTask(HeapUsageMonitoringTask heapStatusMonitoringTask) {
		this.heapStatusMonitoringTask = heapStatusMonitoringTask;
	}
	/**
	 * The actual HeapUsageMonitoringTask that will be registered at the {@link LoadMeasureThread}.
	 * This Thread will fire the check in a regular manner, so that the heap usage can be displayed 
	 * with the help of local progress bar.
	 */
	private class HeapUsageMonitoringTask extends AbstractMonitoringTask {

		private MainWindowStatusBar statusBar;
		
		private HeapUsageMonitoringTask(MainWindowStatusBar statusBar) {
			this.statusBar = statusBar;
		}
		@Override
		public String getTaskDescription() {
			return "Measure Heap-Usage of JVM";
		}
		@Override
		public MonitoringType getMonitoringType() {
			return MonitoringType.CUSTOMIZED_MONITORING;
		}
		@Override
		public MonitoringMeasureType getMonitoringMeasureType() {
			return MonitoringMeasureType.CUSTOM_MEASURE;
		}
		@Override
		public Runnable getFaultMeasure() {
			// --- Nothing to do here -----------
			return null;
		}
		@Override
		public boolean removeTaskAfterMeasure() {
			return false;
		}
		@Override
		public boolean isFaultlessProcess() {
			// --- Here the task can be done ----
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					HeapUsageMonitoringTask.this.statusBar.updateHeapUsage();
				}
			});
			return true;
		}
	}
	
	
	/**
	 * Returns the CENTER toolbar.
	 * @return the jToolBarCenter
	 */
	private JToolBar getJToolBarCenter() {
		if (jToolBarCenter == null) {
			jToolBarCenter = new JToolBar();
			jToolBarCenter.setFloatable(false);
			jToolBarCenter.setBorder(BorderFactory.createEmptyBorder());
			jToolBarCenter.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jToolBarCenter.addSeparator();
			jToolBarCenter.setPreferredSize(new Dimension(300, 16));
			jToolBarCenter.setMinimumSize(new Dimension(300, 16));
		}
		return jToolBarCenter;
	}
	/**
	 * Sets the specified session factory state to the status bar.
	 *
	 * @param factoryID the factory ID
	 * @param sessionFactoryState the session factory state
	 */
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {
		JLabel dbStateLabel = this.getJLabelDatabaseState(factoryID);
		dbStateLabel.setToolTipText("SessionFactory '" + factoryID + "': " + sessionFactoryState.getDescription());
		dbStateLabel.setIcon(sessionFactoryState.getIconImage());
	}
	/**
	 * Gets the database state hash map.
	 * @return the database state hash map
	 */
	private HashMap<String, JLabel> getDatabaseStateHashMap() {
		if (databaseStateHashMap==null) {
			databaseStateHashMap = new HashMap<>();
		}
		return databaseStateHashMap;
	}
	/**
	 * Returns the JLabel for the hibernate database state of the specified factory ID.
	 * @param factoryID the factory ID
	 * @return the JLabel to display the database state
	 */
	private JLabel getJLabelDatabaseState(String factoryID) {
		JLabel dbStateLabel = this.getDatabaseStateHashMap().get(factoryID);
		if (dbStateLabel==null) {
			dbStateLabel = new JLabel();
			dbStateLabel.setPreferredSize(new Dimension(16, 16));
			this.getDatabaseStateHashMap().put(factoryID, dbStateLabel);
			this.getJToolBarCenter().add(dbStateLabel);
		}
		return dbStateLabel;
	}
	
	/**
	 * Returns the RIGHT JLabel for the Jade state.
	 * @return the jLabelJadeState 
	 */
	private JLabel getJLabelJadeState() {
		if (jLabelJadeState == null) {
			jLabelJadeState = new JLabel("Jade State");
			jLabelJadeState.setPreferredSize(new Dimension(200, 16));
			jLabelJadeState.setMinimumSize(new Dimension(200, 16));
			jLabelJadeState.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelJadeState.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return jLabelJadeState;
	}
	/**
	 * Sets the jade color status.
	 * @param jadeStatus the new jade color status
	 */
	public void setJadeStatusColor(JadeStatusColor jadeStatus) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindowStatusBar.this.getJLabelJadeState().setText(jadeStatus.getDescription());
				MainWindowStatusBar.this.getJLabelJadeState().setIcon(jadeStatus.getImageIcon());
				MainWindowStatusBar.this.getJLabelJadeState().validate();
				MainWindowStatusBar.this.getJLabelJadeState().repaint();
			}
		});
	}
	
}
