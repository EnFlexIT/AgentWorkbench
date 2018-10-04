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
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;

/**
 * The MainWindowStatusBar.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MainWindowStatusBar extends JPanel {

	private static final long serialVersionUID = -575684753041100082L;
	
	private final ImageIcon iconGreen = GlobalInfo.getInternalImageIcon("StatGreen.png");
	private final ImageIcon iconRed = GlobalInfo.getInternalImageIcon("StatRed.png");

	private JLabel jLabelStatusText;
	private JLabel jLabelJadeState;
	private JToolBar jPanelCenter;
	
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
		
		this.setPreferredSize(new Dimension(881, 22));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{300, 194, 200, 0};
		gridBagLayout.rowHeights = new int[]{22, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelStatusText = new GridBagConstraints();
		gbc_jLabelStatusText.anchor = GridBagConstraints.WEST;
		gbc_jLabelStatusText.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelStatusText.gridx = 0;
		gbc_jLabelStatusText.gridy = 0;
		this.add(getJLabelStatusText(), gbc_jLabelStatusText);
		
		GridBagConstraints gbc_jPanelCenter = new GridBagConstraints();
		gbc_jPanelCenter.fill = GridBagConstraints.BOTH;
		gbc_jPanelCenter.gridx = 1;
		gbc_jPanelCenter.gridy = 0;
		this.add(getJToolBarCenter(), gbc_jPanelCenter);
		
		GridBagConstraints gbc_jLabelJadeState = new GridBagConstraints();
		gbc_jLabelJadeState.anchor = GridBagConstraints.WEST;
		gbc_jLabelJadeState.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelJadeState.gridx = 2;
		gbc_jLabelJadeState.gridy = 0;
		this.add(getJLabelJadeState(), gbc_jLabelJadeState);
		
		this.setJadeIsRunning(false);
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
	 * Sets the indicator if JADE is running or not (red or green button in the right corner of the status bar + text).
	 * @param isRunning the new status jade running
	 */
	public void setJadeIsRunning(boolean isRunning) {
		if (isRunning == false) {
			this.getJLabelJadeState().setText(Language.translate("JADE wurde noch nicht gestartet.") + "   ");
			this.getJLabelJadeState().setIcon(this.iconRed);
		} else {
			this.getJLabelJadeState().setText(Language.translate("JADE wurde lokal gestartet.") + "   ");
			this.getJLabelJadeState().setIcon(this.iconGreen);
		}
	}
	
	
	/**
	 * Returns the CENTER JPanel.
	 * @return the jPanelCenter
	 */
	private JToolBar getJToolBarCenter() {
		if (jPanelCenter == null) {
			jPanelCenter = new JToolBar();
			jPanelCenter.setFloatable(false);
			jPanelCenter.setBorder(BorderFactory.createEmptyBorder());
			jPanelCenter.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jPanelCenter.addSeparator();
			jPanelCenter.setPreferredSize(new Dimension(300, 16));
			jPanelCenter.setMinimumSize(new Dimension(300, 16));
		}
		return jPanelCenter;
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
	
}
