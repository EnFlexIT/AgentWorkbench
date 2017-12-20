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
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.agentgui.gui.swing.dialogs.FeatureSelectionDialog;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.featureEvaluation.FeatureEvaluator;
import de.enflexit.common.featureEvaluation.FeatureInfo;
import de.enflexit.common.p2.P2OperationsHandler;

/**
 * The Class FeaturePanel.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class FeaturePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 3537858633408322463L;

	private Vector<FeatureInfo> featureVector; 

	private Dimension buttonSize = new Dimension(45, 26);
	
	private JTable jTableFeatures;
	private DefaultTableModel featureTableModel;
	private JScrollPane jScrollPaneProjectFeatures;
	private JPanel jPanelFeatureButtons;
	private JButton jButtonAddFeatures;
	private JButton jButtonRemoveFeatures;
	private JButton jButtonRefreshFeatures;
	private JButton jButtonEditFeature;

	
	/**
	 * Instantiates a new feature panel.
	 * @param featureVector the feature vector
	 */
	public FeaturePanel(Vector<FeatureInfo> featureVector) {
		this.setFeatureVector(featureVector);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_jScrollPaneProjectFeatures = new GridBagConstraints();
		gbc_jScrollPaneProjectFeatures.insets = new Insets(0, 0, 0, 5);
		gbc_jScrollPaneProjectFeatures.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneProjectFeatures.gridx = 0;
		gbc_jScrollPaneProjectFeatures.gridy = 0;
		this.add(this.getJScrollPaneProjectFeatures(), gbc_jScrollPaneProjectFeatures);
		GridBagConstraints gbc_jPanelFeatureButtons = new GridBagConstraints();
		gbc_jPanelFeatureButtons.insets = new Insets(0, 5, 0, 0);
		gbc_jPanelFeatureButtons.fill = GridBagConstraints.BOTH;
		gbc_jPanelFeatureButtons.gridx = 1;
		gbc_jPanelFeatureButtons.gridy = 0;
		this.add(this.getJPanelFeatureButtons(), gbc_jPanelFeatureButtons);
				
	}
	private JScrollPane getJScrollPaneProjectFeatures() {
		if (jScrollPaneProjectFeatures == null) {
			jScrollPaneProjectFeatures = new JScrollPane();
			jScrollPaneProjectFeatures.setViewportView(this.getJTableFeatures());
			
		}
		return jScrollPaneProjectFeatures;
	}
	public JTable getJTableFeatures() {
		if (jTableFeatures==null) {
			jTableFeatures = new JTable(this.getTableModelForFeature());
			jTableFeatures.setFillsViewportHeight(true);
			jTableFeatures.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableFeatures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			jTableFeatures.getTableHeader().setReorderingAllowed(false);
			
			jTableFeatures.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount()==2) {
						if (me.getSource()==getJTableFeatures()) {
							getJButtonEditFeature().doClick();
						}
					}
				}
			});
			
			TableColumnModel tcm = jTableFeatures.getColumnModel();
			tcm.getColumn(0).setWidth(0);
			tcm.getColumn(0).setPreferredWidth(0);
			tcm.getColumn(0).setMinWidth(0);
			tcm.getColumn(0).setMaxWidth(0);
			
		}
		return jTableFeatures;
	}
	/**
	 * Returns the table model for feature.
	 * @return the table model for feature
	 */
	public DefaultTableModel getTableModelForFeature() {
		if (featureTableModel==null) {
			
			Vector<String> header = new Vector<String>();
			header.add("Instance");
			header.add("Name");
			header.add("Feature-ID");
			header.add("Version");
			header.add("Repo. Name");
			header.add("Repo. URI");
			featureTableModel = new DefaultTableModel(null, header) {
				private static final long serialVersionUID = 5020829471298147886L;
				public boolean isCellEditable(int row, int column) {
					return false;
				};
			};
		}
		return featureTableModel;
	}
	/**
	 * Fill table model.
	 */
	private void fillTableModel() {
		this.emptyTableModelForFeature();
		for (FeatureInfo fi : this.featureVector) {
			this.addTableRow(fi);
		}
	}
	/**
	 * Empties the table model.
	 */
	private void emptyTableModelForFeature() {
		this.getJTableFeatures().clearSelection();
		while (this.getTableModelForFeature().getRowCount()>0) {
			this.getTableModelForFeature().removeRow(0);
		}
	}
	/**
	 * Adds the table row.
	 * @param featureInfo the feature info
	 */
	private void addTableRow(FeatureInfo featureInfo) {
		Vector<Object> data = new Vector<Object>();
		data.add(featureInfo);
		data.add(featureInfo.getName());
		data.add(featureInfo.getId());
		data.add(featureInfo.getVersion());
		data.add(featureInfo.getRepositoryName());
		data.add(featureInfo.getRepositoryURI());
		this.getTableModelForFeature().addRow(data);
	}
	
	
	
	private JPanel getJPanelFeatureButtons() {
		if (jPanelFeatureButtons == null) {
			jPanelFeatureButtons = new JPanel();
			jPanelFeatureButtons.setPreferredSize(new Dimension(45, 110));
			GridBagLayout gbl_jPanelFeatureButtons = new GridBagLayout();
			gbl_jPanelFeatureButtons.columnWidths = new int[]{0, 0};
			gbl_jPanelFeatureButtons.rowHeights = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelFeatureButtons.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_jPanelFeatureButtons.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
			jPanelFeatureButtons.setLayout(gbl_jPanelFeatureButtons);
			GridBagConstraints gbc_jButtonSelectFeature = new GridBagConstraints();
			gbc_jButtonSelectFeature.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonSelectFeature.gridx = 0;
			gbc_jButtonSelectFeature.gridy = 0;
			jPanelFeatureButtons.add(getJButtonAddFeatures(), gbc_jButtonSelectFeature);
			GridBagConstraints gbc_jButtonEditFeature = new GridBagConstraints();
			gbc_jButtonEditFeature.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonEditFeature.gridx = 0;
			gbc_jButtonEditFeature.gridy = 1;
			jPanelFeatureButtons.add(getJButtonEditFeature(), gbc_jButtonEditFeature);
			GridBagConstraints gbc_button_1 = new GridBagConstraints();
			gbc_button_1.insets = new Insets(0, 0, 5, 0);
			gbc_button_1.gridx = 0;
			gbc_button_1.gridy = 2;
			jPanelFeatureButtons.add(getJButtonRemoveFeatures(), gbc_button_1);
			GridBagConstraints gbc_button_2 = new GridBagConstraints();
			gbc_button_2.insets = new Insets(10, 0, 0, 0);
			gbc_button_2.anchor = GridBagConstraints.SOUTH;
			gbc_button_2.gridx = 0;
			gbc_button_2.gridy = 3;
			jPanelFeatureButtons.add(getJButtonRefreshFeatures(), gbc_button_2);
		}
		return jPanelFeatureButtons;
	}
	private JButton getJButtonAddFeatures() {
		if (jButtonAddFeatures == null) {
			jButtonAddFeatures = new JButton();
			jButtonAddFeatures.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonAddFeatures.setToolTipText("Select features");
			jButtonAddFeatures.setPreferredSize(this.buttonSize);
			jButtonAddFeatures.setMinimumSize(this.buttonSize);
			jButtonAddFeatures.addActionListener(this);
		}
		return jButtonAddFeatures;
	}
	private JButton getJButtonEditFeature() {
		if (jButtonEditFeature == null) {
			jButtonEditFeature = new JButton();
			jButtonEditFeature.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonEditFeature.setToolTipText("Edit feature repository");
			jButtonEditFeature.setPreferredSize(this.buttonSize);
			jButtonEditFeature.setMinimumSize(this.buttonSize);
			jButtonEditFeature.addActionListener(this);
		}
		return jButtonEditFeature;
	}
	private JButton getJButtonRemoveFeatures() {
		if (jButtonRemoveFeatures == null) {
			jButtonRemoveFeatures = new JButton();
			jButtonRemoveFeatures.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonRemoveFeatures.setToolTipText("Remove feature");
			jButtonRemoveFeatures.setPreferredSize(this.buttonSize);
			jButtonRemoveFeatures.setMinimumSize(this.buttonSize);
			jButtonRemoveFeatures.addActionListener(this);
		}
		return jButtonRemoveFeatures;
	}
	private JButton getJButtonRefreshFeatures() {
		if (jButtonRefreshFeatures == null) {
			jButtonRefreshFeatures = new JButton();
			jButtonRefreshFeatures.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png"));
			jButtonRefreshFeatures.setToolTipText("Determine required features.");
			jButtonRefreshFeatures.setPreferredSize(this.buttonSize);
			jButtonRefreshFeatures.setMinimumSize(this.buttonSize);
			jButtonRefreshFeatures.addActionListener(this);
		}
		return jButtonRefreshFeatures;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	
		if (ae.getSource()==this.getJButtonAddFeatures()) {
			// --- Open the feature selection dialog --------------------------
			List<IInstallableUnit> availableFeatures = null;
			try {
				availableFeatures = P2OperationsHandler.getInstance().getInstalledFeatures();
			} catch (Exception e1) {
				Frame owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
				JOptionPane.showMessageDialog(owner , e1.getMessage(), "Error accessing p2 profile", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// --- Show the feature selection dialog --------------------------
			Frame ownerFrame = Application.getGlobalInfo().getOwnerFrameForComponent(getJButtonAddFeatures());
			FeatureSelectionDialog fsd = new FeatureSelectionDialog(ownerFrame, this.featureVector, availableFeatures);

			// --- Add the selected features to the list ----------------------
			if (fsd.isCanceled() == false) {
				// --- Get the selected IUs from the dialog -------------------
				List<IInstallableUnit> selectedFeatures = fsd.getSelectedFeatures();
				for (IInstallableUnit installableUnit : selectedFeatures) {
					if (FeatureEvaluator.getInstance().isFeatureOfBaseInstallation(installableUnit)==false) {
						// --- Create FeatureInfo -----------------------------
						FeatureInfo featureInfo = FeatureInfo.createFeatureInfoFromIU(installableUnit);
						if (this.featureVector.contains(featureInfo)==false) {
							this.addTableRow(featureInfo);
							this.featureVector.add(featureInfo);
							this.addedFeatureInfo(featureInfo);
						}
					}
				}
			}
			
		} else if (ae.getSource()==this.getJButtonEditFeature()) {
			// --- Edit the selected FeatureInfo ------------------------------
			int tableRowIndex = this.getJTableFeatures().getSelectedRow();
			if (tableRowIndex!=-1) {
				// --- Get the feature info to edit ---------------------------
				FeatureInfo featureInfoToEdit = (FeatureInfo) this.getJTableFeatures().getValueAt(tableRowIndex, 0);
				
				// --- Ask user for the repository ----------------------------
				String title = Language.translate("Edit feature repository", Language.EN);
				String message = Language.translate("Please insert a valid repository URI for the feature", Language.EN) + " '" + featureInfoToEdit.getId() + "'!";
				String newUriString = (String) JOptionPane.showInputDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE, null, null, featureInfoToEdit.getRepositoryURI() );
				if (newUriString!=null && newUriString.equalsIgnoreCase(featureInfoToEdit.getRepositoryURI().toString())==false) {

					try {
						URI newURI = new URI(newUriString);
						featureInfoToEdit.setRepositoryURI(newURI);
						this.getJTableFeatures().setValueAt(newURI, tableRowIndex, 5);
						this.updatedFeatureInfo(featureInfoToEdit);
						
					} catch (URISyntaxException uriEx) {
						JOptionPane.showMessageDialog(this, uriEx.getLocalizedMessage(), title, JOptionPane.ERROR_MESSAGE);
						//uriEx.printStackTrace();
					}
					
				}
			}			
			
		} else if (ae.getSource()==this.getJButtonRemoveFeatures()) {
			// --- Remove the selected FeatureInfo ----------------------------
			int tableRowIndex = this.getJTableFeatures().getSelectedRow();
			if (tableRowIndex!=-1) {
				FeatureInfo featureInfoToRemove = (FeatureInfo) this.getJTableFeatures().getValueAt(tableRowIndex, 0);
				int modelRowIndex = this.getJTableFeatures().convertRowIndexToModel(tableRowIndex);
				this.getTableModelForFeature().removeRow(modelRowIndex);
				this.featureVector.remove(featureInfoToRemove);
				this.removedFeatureInfo(featureInfoToRemove);
			}
			
		} else if (ae.getSource()==this.getJButtonRefreshFeatures()) {
			// --- Automatically set the required features -------------------
			this.updateFeatureInfos();
			
		}
		
	}

	/**
	 * Informs that the specified FeatureInfo was added.
	 * @param addedFeatureInfo the added FeatureInfo
	 */
	public abstract void addedFeatureInfo(FeatureInfo addedFeatureInfo);
	/**
	 * Informs that the specified FeatureInfo was updates.
	 * @param updatedFeatureInfo the updated FeatureInfo
	 */
	public abstract void updatedFeatureInfo(FeatureInfo updatedFeatureInfo);
	/**
	 * Informs that the specified FeatureInfo was removed.
	 * @param removedFeatureInfo the removed FeatureInfo
	 */
	public abstract void removedFeatureInfo(FeatureInfo removedFeatureInfo);
	/**
	 * Informs that the FeatureInfos should be updated and set through {@link #setFeatureVector(Vector)}.
	 */
	public abstract void updateFeatureInfos();
	
	
	/**
	 * Returns the feature vector.
	 * @return the feature vector
	 */
	public Vector<FeatureInfo> getFeatureVector() {
		if (featureVector==null) {
			featureVector = new Vector<>();
		}
		return featureVector;
	}
	/**
	 * Sets the feature vector.
	 * @param featureVector the new feature vector
	 */
	public void setFeatureVector(Vector<FeatureInfo> featureVector) {
		this.featureVector = featureVector;
		this.fillTableModel();
	}
	
}
