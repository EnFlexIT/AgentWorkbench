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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import agentgui.simulationService.load.threading.storage.ThreadInfoStorage;

/**
 * The Class ThreadMonitorDetailTreeTab.
 * 
 * Displays detailed information about thread/agent load.
 * Builds a Tree view Cluster->Machines->JCM->Container
 * Filter for agents applicable.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMonitorDetailTreeTab extends JPanel implements ActionListener {

	private static final long serialVersionUID = -7315494195421538651L;

	/** The thread info storage. */
	private ThreadInfoStorage threadInfoStorage;

	/** The thread info storage tree. */
	private ThreadInfoStorageTree threadTree;

	/** The left scroll pane. */
	private JScrollPane leftScrollPane;

	/** The right scroll pane. */
	private ThreadInfoStorageScrollPane rightScrollPane;

	/** The J panel filter. */
	private JPanel JPanelFilter;

	/** The j radio button no filter. */
	private JRadioButton jRadioButtonNoFilter;

	/** The j radio button filter agents. */
	private JRadioButton jRadioButtonFilterAgents;

	/** The split pane. */
	private JSplitPane splitPane;
	
	/** The text field. */
	private JTextField textField;
	
	/** The left panel. */
	private JPanel leftPanel;

	/**
	 * Instantiates a new thread measure detail tab.
	 * 
	 * @param threadInfoStorage
	 *            the thread info storage
	 */
	public ThreadMonitorDetailTreeTab(ThreadInfoStorage threadInfoStorage) {
		this.threadInfoStorage = threadInfoStorage;
		initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(getSplitPane(), BorderLayout.CENTER);
		this.add(getJPanelFilter(), BorderLayout.SOUTH);
	}

	/**
	 * Gets the j panel filter.
	 * 
	 * @return the j panel filter
	 */
	private JPanel getJPanelFilter() {
		if (JPanelFilter == null) {
			JPanelFilter = new JPanel();
			JPanelFilter.setBorder(null);

			// --- Configure Button Group -----------------
			ButtonGroup bg = new ButtonGroup();
			bg.add(getJRadioButtonNoFilter());
			bg.add(getJRadioButtonFilterAgents());

			// --- Set default values -----------------------
			getJRadioButtonNoFilter().setSelected(true);
			getJRadioButtonFilterAgents().setSelected(false);
			GridBagLayout gbl_JPanelFilter = new GridBagLayout();
			gbl_JPanelFilter.columnWidths = new int[] { 50, 50, 400 };
			gbl_JPanelFilter.rowHeights = new int[] { 23, 0 };
			gbl_JPanelFilter.columnWeights = new double[] { 0.0, 0.0,
					Double.MIN_VALUE };
			gbl_JPanelFilter.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			JPanelFilter.setLayout(gbl_JPanelFilter);
			GridBagConstraints gbc_jRadioButtonNoFilter = new GridBagConstraints();
			gbc_jRadioButtonNoFilter.fill = GridBagConstraints.BOTH;
			gbc_jRadioButtonNoFilter.insets = new Insets(0, 0, 0, 5);
			gbc_jRadioButtonNoFilter.gridx = 0;
			gbc_jRadioButtonNoFilter.gridy = 0;
			JPanelFilter.add(getJRadioButtonNoFilter(),
					gbc_jRadioButtonNoFilter);
			GridBagConstraints gbc_jRadioButtonFilterAgents = new GridBagConstraints();
			gbc_jRadioButtonFilterAgents.fill = GridBagConstraints.BOTH;
			gbc_jRadioButtonFilterAgents.gridx = 1;
			gbc_jRadioButtonFilterAgents.gridy = 0;
			JPanelFilter.add(getJRadioButtonFilterAgents(),
					gbc_jRadioButtonFilterAgents);
		}
		return JPanelFilter;
	}

	/**
	 * Gets the j radio button no filter.
	 * 
	 * @return the j radio button no filter
	 */
	private JRadioButton getJRadioButtonNoFilter() {
		if (jRadioButtonNoFilter == null) {
			jRadioButtonNoFilter = new JRadioButton("No Filter");
			jRadioButtonNoFilter.setHorizontalAlignment(SwingConstants.LEFT);
			jRadioButtonNoFilter.setToolTipText("Show all threads");
			jRadioButtonNoFilter.addActionListener(this);
		}
		return jRadioButtonNoFilter;
	}

	/**
	 * Gets the j radio button for filtering agents.
	 * 
	 * @return the j radio button filter agents
	 */
	private JRadioButton getJRadioButtonFilterAgents() {
		if (jRadioButtonFilterAgents == null) {
			jRadioButtonFilterAgents = new JRadioButton("Filter for Agents");
			jRadioButtonFilterAgents.setHorizontalAlignment(SwingConstants.LEFT);
			jRadioButtonFilterAgents.setToolTipText("Show only agent threads");
			jRadioButtonFilterAgents.addActionListener(this);
		}
		return jRadioButtonFilterAgents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == this.getJRadioButtonNoFilter()) {
			threadTree.removeAgentFilter();
		} else if (ae.getSource() == this.getJRadioButtonFilterAgents()) {
			threadTree.applyAgentFilter();
		}
	}

	/**
	 * Gets the split pane.
	 * 
	 * @return the split pane
	 */
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					getLeftPanel(), getRightScrollPane());
		}
		return splitPane;
	}

	/**
	 * Gets the left panel.
	 *
	 * @return the left panel
	 */
	private JPanel getLeftPanel(){
		if(leftPanel == null){
			leftPanel = new JPanel();
			GridBagLayout gbl_leftPanel = new GridBagLayout();
			gbl_leftPanel.columnWidths = new int[] {222};
			gbl_leftPanel.rowHeights = new int[] {100, 10};
			gbl_leftPanel.columnWeights = new double[]{1.0};
			gbl_leftPanel.rowWeights = new double[]{1.0, 0.0};
			leftPanel.setLayout(gbl_leftPanel);
			
			GridBagConstraints gbc_leftScrollPane = new GridBagConstraints();
			gbc_leftScrollPane.fill = GridBagConstraints.BOTH;
			gbc_leftScrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_leftScrollPane.gridx = 0;
			gbc_leftScrollPane.gridy = 0;
			leftPanel.add(getLeftScrollPane(), gbc_leftScrollPane);
			
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.anchor = GridBagConstraints.SOUTH;
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 0;
			gbc_textField.gridy = 1;
			leftPanel.add(getTextField(), gbc_textField);
		}
		return leftPanel;
	}
	/**
	 * Gets the left scroll pane.
	 * 
	 * @return the left scroll pane
	 */
	private JScrollPane getLeftScrollPane() {
		if (leftScrollPane == null) {
			leftScrollPane = new JScrollPane(getThreadTree());
			leftScrollPane.setMinimumSize(new Dimension(222, 333));
			leftScrollPane.validate();
		}
		return leftScrollPane;
	}

	/**
	 * Gets the right scroll pane.
	 * 
	 * @return the right scroll pane
	 */
	private ThreadInfoStorageScrollPane getRightScrollPane() {
		if (rightScrollPane == null) {
			rightScrollPane = new ThreadInfoStorageScrollPane(threadTree.getSeriesChartsDelta(), threadTree.getSeriesChartsTotal(), null, false, "", null);
		}
		return rightScrollPane;
	}

	/**
	 * Gets the thread tree.
	 * 
	 * @return the thread tree
	 */
	private ThreadInfoStorageTree getThreadTree() {
		if (threadTree == null) {
			threadTree = new ThreadInfoStorageTree(threadInfoStorage);
			threadTree.setRootVisible(false);
			threadTree.getModel().addTreeModelListener(new MyTreeModelListener());
		}
		return threadTree;
	}

	/**
	 * Gets the text field.
	 * 
	 * @return the text field
	 */
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setToolTipText("Enter filter string");
			textField.addKeyListener(new KeyListener() {
				private boolean once = false;

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					threadTree.applyTextFilter(textField.getText());
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (textField.getText().length() < 1 && once == false) {
						threadTree.prepareTextFilter();
						once = true;
					} else if (textField.getText().length() < 1 && once == true) {
						once = false;
					}
				}
			});
		}
		return textField;
	}
	
	/**
	 * The listener interface for receiving myTreeModel events.
	 * The class that is interested in processing a myTreeModel
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyTreeModelListener<code> method. When
	 * the myTreeModel event occurs, that object's appropriate
	 * method is invoked.
	 *
	 */
	class MyTreeModelListener implements TreeModelListener{
		@Override
		public void treeNodesChanged(TreeModelEvent e) {	
			threadTree.sortThreads();
		}
	
		@Override
		public void treeNodesInserted(TreeModelEvent e) {
		}
	
		@Override
		public void treeNodesRemoved(TreeModelEvent e) {
		}
		
		@Override
		public void treeStructureChanged(TreeModelEvent e) {	
			threadTree.expandRow(0);
			threadTree.expandRow(1);
			threadTree.expandRow(2);
			threadTree.expandRow(3);
		}
	}
}