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
package org.agentgui.gui.swing.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;

import agentgui.core.application.Language;
import de.enflexit.common.featureEvaluation.FeatureEvaluator;
import de.enflexit.common.featureEvaluation.FeatureInfo;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * The Class FeatureSelectionDialog.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class FeatureSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -9120650393031177126L;
	
	private boolean canceled;

	private Vector<FeatureInfo> featureSelectedVector;
	private List<IInstallableUnit> availableFeatures;

	private JLabel jLabelHeader;

	private JLabel jLabelInfo;
	private JPanel jPanelButtons;
	private JButton jButtonOk;
	private JButton jButtonCancel;
	
	private JScrollPane jScrollPaneFeatureTree;
	private JTree jTreeFeatures;
	private DefaultMutableTreeNode rootTreeNode;
	private DefaultTreeModel treeModel;
	
	
	/**
	 * Instantiates a new feature selection dialog.
	 *
	 * @param ownerFrame the owner frame
	 * @param featureSelectedVector the feature vector
	 * @param availableFeatures the available features
	 */
	public FeatureSelectionDialog(Frame ownerFrame, Vector<FeatureInfo> featureSelectedVector, List<IInstallableUnit> availableFeatures) {
		super(ownerFrame);
		this.featureSelectedVector = featureSelectedVector;
		this.availableFeatures = availableFeatures;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.NORTH;
		gbc_jLabelHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 10);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jScrollPaneFeatureTree = new GridBagConstraints();
		gbc_jScrollPaneFeatureTree.insets = new Insets(5, 10, 0, 10);
		gbc_jScrollPaneFeatureTree.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneFeatureTree.gridx = 0;
		gbc_jScrollPaneFeatureTree.gridy = 1;
		getContentPane().add(getJScrollPaneFeatureTree(), gbc_jScrollPaneFeatureTree);
		GridBagConstraints gbc_jLabelInfo = new GridBagConstraints();
		gbc_jLabelInfo.anchor = GridBagConstraints.WEST;
		gbc_jLabelInfo.insets = new Insets(5, 10, 0, 10);
		gbc_jLabelInfo.gridx = 0;
		gbc_jLabelInfo.gridy = 2;
		getContentPane().add(getLabelInfo(), gbc_jLabelInfo);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(10, 10, 15, 10);
		gbc_jPanelButtons.anchor = GridBagConstraints.NORTH;
		gbc_jPanelButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 3;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);

		this.setTitle(Language.translate("Features auswählen"));
		this.setSize(800, 450);
		this.setModal(true);
		this.registerEscapeKeyStroke();
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		this.setVisible(true);
	}

	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
	 * Sets the action in the dialog to be canceled.
	 * @param canceled the new canceled
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setVisible(false);
    			setCanceled(true);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * Gets the j label header.
	 * @return the j label header
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel(Language.translate("Vom Projekt benötigte Features:"));
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	
	/**
	 * Checks if the feature with the given ID is in the list
	 * 
	 * @param featuresList The list
	 * @param featureID The ID
	 * @return
	 */
	private boolean isFeatureInList(List<FeatureInfo> featuresList, String featureID) {
		for (int i = 0; i < featuresList.size(); i++) {
			FeatureInfo feature = featuresList.get(i);
			if (feature.getId().equals(featureID)) {
				return true;
			}
		}
		return false;
	}

	private JLabel getLabelInfo() {
		if (jLabelInfo == null) {
			jLabelInfo = new JLabel(Language.translate("'#' markiert alle Features, die zur Basisinstallation gehören."));
			jLabelInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelInfo;
	}
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[] { 0, 0, 0 };
			gbl_jPanelButtons.rowHeights = new int[] { 0, 0 };
			gbl_jPanelButtons.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_jPanelButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.anchor = GridBagConstraints.EAST;
			gbc_jButtonOk.weightx = 1.0;
			gbc_jButtonOk.insets = new Insets(5, 0, 5, 20);
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			jPanelButtons.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(5, 20, 5, 0);
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.weightx = 1.0;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setPreferredSize(new Dimension(80, 26));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == this.getJButtonOk()) {
			this.setCanceled(false);
			this.dispose();
		} else {
			this.setCanceled(true);
			this.dispose();
		}
	}

	/**
	 * Gets the display text for an {@link IInstallableUnit}, which is either the name or the ID.
	 *
	 * @param installableUnit the installable unit
	 * @return the display text for the installable unit
	 */
	private String getDisplayTextForInstallableUnit(IInstallableUnit installableUnit) {

		// --- Try to get the description -----------------
		String displalyText = FeatureEvaluator.getInstance().getIInstallableUnitDescription(installableUnit);
		if (displalyText==null || displalyText.isEmpty()) {
			// --- Get the IU's name property -------------
			displalyText = installableUnit.getProperty(IInstallableUnit.PROP_NAME);
			
			// --- If not set, use the ID instead ---------
			if (displalyText==null || displalyText.equals("%featureName")) {
				displalyText = installableUnit.getId();
			}
		}
		return displalyText;
	}
	/**
	 * Returns the installable unit specified by the ID.
	 * @param id the id of the IInstallableUnit 
	 * @return the installable unit by ID
	 */
	private IInstallableUnit getInstallableUnitByID(String id) {
		for (int i = 0; i < this.availableFeatures.size(); i++) {
			IInstallableUnit iuCheck = this.availableFeatures.get(i); 
			if (iuCheck.getId().equals(id)) {
				return iuCheck; 
			}
		}
		return null;
	}
	
	
	// ----------------------------------------------------------------------------------
	// --- From here methods for the tree display can be found --------------------------
	// ----------------------------------------------------------------------------------
	private JScrollPane getJScrollPaneFeatureTree() {
		if (jScrollPaneFeatureTree == null) {
			jScrollPaneFeatureTree = new JScrollPane();
			jScrollPaneFeatureTree.setViewportView(getJTreeFeatures());
		}
		return jScrollPaneFeatureTree;
	}
	private JTree getJTreeFeatures() {
		if (jTreeFeatures == null) {
			jTreeFeatures = new JTree(this.getTreeModel());
			jTreeFeatures.setRootVisible(false);
			jTreeFeatures.setEditable(true);
			jTreeFeatures.setCellRenderer(new FeatureTreeRenderEditor(jTreeFeatures));
			jTreeFeatures.setCellEditor(new FeatureTreeRenderEditor(jTreeFeatures));
			
		}
		return jTreeFeatures;
	}
	private DefaultMutableTreeNode getRootTreeNode() {
		if (rootTreeNode==null) {
			rootTreeNode = new DefaultMutableTreeNode("Root");
		}
		return rootTreeNode;
	}
	private DefaultTreeModel getTreeModel() {
		if (treeModel==null) {
			treeModel = new DefaultTreeModel(this.getRootTreeNode());

			// --- Sort the Installable Units found -------
			Collections.sort(this.availableFeatures, new Comparator<IInstallableUnit>() {
				@Override
				public int compare(IInstallableUnit iu1, IInstallableUnit iu2) {
					String displayText1 = FeatureSelectionDialog.this.getDisplayTextForInstallableUnit(iu1);
					String displayText2 = FeatureSelectionDialog.this.getDisplayTextForInstallableUnit(iu2);
					return displayText1.compareTo(displayText2);
				}
			});
			
			// --- Fill the model -------------------------
			for (int i = 0; i < this.availableFeatures.size(); i++) {

				// --- Add first level node --------------- 
				IInstallableUnit installableUnit = this.availableFeatures.get(i);
				boolean isSelected = this.isFeatureInList(this.featureSelectedVector, installableUnit.getId());
				DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(new FeatureTreeNodeObject(installableUnit, isSelected));
				this.getRootTreeNode().add(subNode);

				// --- Check for included features --------
				this.addBaseIUs(subNode);
				
			}
		}
		return treeModel;
	}
	/**
	 * Adds the base or required UIs to the specified node.
	 * @param baseNode the current base UI node
	 */
	private void addBaseIUs(DefaultMutableTreeNode baseNode) {
		
		FeatureTreeNodeObject ftno = (FeatureTreeNodeObject) baseNode.getUserObject();
		IInstallableUnit iu = ftno.getInstallableUnit();
		
		for (Iterator<IRequirement> iterator = iu.getRequirements().iterator(); iterator.hasNext();) {
			IRequirement requiers = (IRequirement) iterator.next();
			Object[] matchParams = requiers.getMatches().getParameters();
			
			for (int i = 0; i < matchParams.length; i++) {
				Object matchParam = matchParams[i];
				if (matchParam instanceof String) {
					String matchParamString = (String) matchParam;  
					IInstallableUnit subIU = this.getInstallableUnitByID(matchParamString);
					if (subIU!=null) {
						DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(new FeatureTreeNodeObject(subIU, false));
						baseNode.add(subNode);
						this.addBaseIUs(subNode);
					}
				}
			}
		}
	}
	/**
	 * Return the selected features.
	 * @return the selected features
	 */
	public List<IInstallableUnit> getSelectedFeatures() {
		List<IInstallableUnit> selectedUIs = new ArrayList<>();
		for (int i = 0; i < this.getRootTreeNode().getChildCount(); i++) {
			DefaultMutableTreeNode firstLevelNode = (DefaultMutableTreeNode) this.getRootTreeNode().getChildAt(i);
			FeatureTreeNodeObject ftno = (FeatureTreeNodeObject) firstLevelNode.getUserObject();
			if (ftno.isSelected()==true) {
				selectedUIs.add(ftno.getInstallableUnit());
			}
		}
		return selectedUIs;
	}
	
	/**
	 * The Class FeatureTreeNodeObject represents the user object used in the local tree nodes.
	 *
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class FeatureTreeNodeObject {
		
		private IInstallableUnit installableUnit;  
		private boolean selected;
		
		/**
		 * Instantiates a new feature tree node object.
		 *
		 * @param installableUnit the installable unit
		 * @param isSelected the is selected
		 */
		public FeatureTreeNodeObject(IInstallableUnit installableUnit, boolean isSelected) {
			this.setInstallableUnit(installableUnit);
			this.setSelected(isSelected);
		}

		/**
		 * Gets the installable unit.
		 * @return the installable unit
		 */
		public IInstallableUnit getInstallableUnit() {
			return installableUnit;
		}
		/**
		 * Sets the installable unit.
		 * @param installableUnit the new installable unit
		 */
		public void setInstallableUnit(IInstallableUnit installableUnit) {
			this.installableUnit = installableUnit;
		}

		/**
		 * Checks if is selected.
		 * @return true, if is selected
		 */
		public boolean isSelected() {
			return selected;
		}
		/**
		 * Sets the selected.
		 * @param selected the new selected
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
	
	/**
	 * The Class FeatureTreeRenderEditor.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class FeatureTreeRenderEditor implements TreeCellRenderer, TreeCellEditor {

		private JTree jTree;
		private DefaultMutableTreeNode treeNode;
		private FeatureTreeNodeObject ftno;
		
		private DefaultTreeCellRenderer defaultRenderer;
		
		/**
		 * Instantiates a new feature tree render editor.
		 * @param jTreeFeatures the j tree features
		 */
		private FeatureTreeRenderEditor(JTree jTreeFeatures) {
			this.jTree = jTreeFeatures;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#getCellEditorValue()
		 */
		@Override
		public Object getCellEditorValue() {
			return this.treeNode;
		}
		
		/**
		 * Returns the default tree cell renderer.
		 * @return the default tree cell renderer
		 */
		private DefaultTreeCellRenderer getDefaultTreeCellRenderer() {
			if (defaultRenderer==null) {
				defaultRenderer = new DefaultTreeCellRenderer();
			}
			return defaultRenderer;
		}
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean isLeaf, int row, boolean hasFocus) {
			
			this.treeNode = (DefaultMutableTreeNode) value;
					
			// --- Get the default renderer -------------------------
			Component displayComponent = this.getDefaultTreeCellRenderer().getTreeCellRendererComponent(tree, value, isSelected, expanded, isLeaf, row, hasFocus);
			JLabel displayLabel = (JLabel) displayComponent;

			// --- Try to get IU and selection indicator ------------
			IInstallableUnit iu = null;
			boolean isSelectedForProject = false;
			boolean isBaseFeature = false;
			if (this.treeNode.getUserObject() instanceof FeatureTreeNodeObject) {
				
				this.ftno = (FeatureTreeNodeObject) this.treeNode.getUserObject();
				
				iu = this.ftno.getInstallableUnit();
				isSelectedForProject = this.ftno.isSelected();
				isBaseFeature = FeatureEvaluator.getInstance().isFeatureOfBaseInstallation(iu);
				
				displayLabel.setText(FeatureSelectionDialog.this.getDisplayTextForInstallableUnit(iu));
			}
			
			// --- Check if is first level --------------------------
			DefaultMutableTreeNode treeNodeParent = (DefaultMutableTreeNode) this.treeNode.getParent();
			if (treeNodeParent.isRoot()==true) {

				// --- Replace label by check box -------------------
				JCheckBox displayCheckBox = new JCheckBox(displayLabel.getText());
				displayCheckBox.setFont(displayLabel.getFont());
				displayCheckBox.setBackground(displayLabel.getBackground());
				displayCheckBox.setForeground(displayLabel.getForeground());
				
				// --- Check if feature is AWB base part ------------
				if (iu!=null) {
					if (isBaseFeature==true) {
						displayCheckBox.setSelected(true);
					} else {
						displayCheckBox.setSelected(isSelectedForProject);	
					}
				}
				displayComponent = displayCheckBox;
			}
			
			if (iu!=null) {
				displayComponent.setEnabled(!isBaseFeature);
			}
			
			return displayComponent;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
		 */
		@Override
		public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean isLeaf, int row) {
			
			Component displayComponent = this.getTreeCellRendererComponent(tree, value, true, expanded, isLeaf, row, true);
			if (displayComponent instanceof JCheckBox) {
				// --- Add an item listener to the check box --------
				JCheckBox checkBox = (JCheckBox) displayComponent;
				checkBox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent ie) {
						JCheckBox checkBox = (JCheckBox) ie.getSource();
						FeatureTreeRenderEditor.this.ftno.setSelected(checkBox.isSelected());
					}
				});
			}
			return displayComponent;
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
		 */
		@Override
		public boolean isCellEditable(EventObject anEvent) {

			boolean isEditable = false;
			if (anEvent instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent) anEvent;
				TreePath path = jTree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
				if (path != null) {
					Object node = path.getLastPathComponent();
					if ((node!=null) && (node instanceof DefaultMutableTreeNode)) {
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
						if (treeNode.getUserObject() instanceof FeatureTreeNodeObject) {
							FeatureTreeNodeObject ftno = (FeatureTreeNodeObject) treeNode.getUserObject();
							isEditable  = ! FeatureEvaluator.getInstance().isFeatureOfBaseInstallation(ftno.getInstallableUnit());
						}
					}
				}
			}
			return isEditable;
		}
		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
		 */
		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}
		
		@Override
		public boolean stopCellEditing() {
			return false;
		}
		@Override
		public void cancelCellEditing() {
		}

		@Override
		public void addCellEditorListener(CellEditorListener cel) {
		}
		@Override
		public void removeCellEditorListener(CellEditorListener cel) {
		}
	} // ---  end sub class ---
	
	
}
