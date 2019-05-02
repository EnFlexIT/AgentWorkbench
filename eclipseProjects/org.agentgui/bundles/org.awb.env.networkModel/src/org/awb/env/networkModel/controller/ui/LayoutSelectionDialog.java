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
package org.awb.env.networkModel.controller.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGui.ToolBarType;
import org.awb.env.networkModel.settings.LayoutSettings;

import agentgui.core.application.Language;
import de.enflexit.common.swing.JComboBoxWide;


/**
 * Dialog for selecting the layout of the current {@link NetworkModel}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LayoutSelectionDialog extends BasicGraphGuiJInternalFrame implements Observer, ActionListener {

    private static final long serialVersionUID = -7481141098749690137L;
    
	private ComponentAdapter desktopAdapter;
	
	private DefaultComboBoxModel<String> comboBoxModel;
	private JComboBoxWide<String> jComboBoxLayout;
	private boolean isPauseComboBoxActionListener;
	
	
	/**
     * Instantiates a new AddComponentDialog and displays it for the user.
     * @param graphController the GraphEnvironmentController
     */
    public LayoutSelectionDialog(GraphEnvironmentController graphController) {
    	super(graphController);
		this.initialize();
    }
    /**
     * This method initializes this
     */
    private void initialize() {
		
		this.setClosable(false);
		this.setMaximizable(false);
		this.setIconifiable(false);
		
		this.setAutoscrolls(false);
		this.setResizable(false);

		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
		ui.setNorthPane(null);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
    	gridBagLayout.columnWidths = new int[]{0, 0};
    	gridBagLayout.rowHeights = new int[]{0, 0};
    	gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    	gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    	this.getContentPane().setLayout(gridBagLayout);
    	
    	GridBagConstraints gbc_jComboBoxLayout = new GridBagConstraints();
    	gbc_jComboBoxLayout.fill = GridBagConstraints.HORIZONTAL;
    	gbc_jComboBoxLayout.insets = new Insets(3, 3, 3, 3);
    	gbc_jComboBoxLayout.gridx = 0;
    	gbc_jComboBoxLayout.gridy = 0;
    	this.getContentPane().add(this.getJComboBoxLayout(), gbc_jComboBoxLayout);
		
    	this.registerAtDesktopAndSetVisible();	
    }
    
    /* (non-Javadoc)
     * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame#isRemindAsLastOpenedEditor()
     */
    @Override
    protected boolean isRemindAsLastOpenedEditor() {
    	return false;
    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visble) {
    	super.setVisible(visble);
    	if (visble==true) {
    		this.setDialogPosition();
    		this.graphController.addObserver(this);
    		this.graphDesktop.addComponentListener(this.getComponentAdapter4Desktop());
    		this.getJComboBoxLayout().grabFocus();
    	} else {
    		if (graphController!=null) this.graphController.deleteObserver(this);
    		if (this.graphDesktop!=null) {
    			this.graphDesktop.removeComponentListener(this.getComponentAdapter4Desktop());
    			this.graphDesktop.unregisterEditor(this);
    		}
    	}
    }
    /**
     * Returns a ComponentAdapter for the current desktop object.
     * @return the ComponentAdapter 
     */
    private ComponentAdapter getComponentAdapter4Desktop() {
    	if (this.desktopAdapter==null) {
    		desktopAdapter = new ComponentAdapter() {
    			@Override
    			public void componentResized(ComponentEvent ce) {
    				boolean isComboBoxFocus = LayoutSelectionDialog.this.getJComboBoxLayout().hasFocus(); 
    				LayoutSelectionDialog.this.setDialogPosition();
    				if (isComboBoxFocus==true) {
    					LayoutSelectionDialog.this.basicGraphGui.getJToolBar(ToolBarType.LayoutControl).grabFocus();
    					LayoutSelectionDialog.this.getJComboBoxLayout().grabFocus();
    				}
    			}
			};
    	}
    	return desktopAdapter;
    }
    /**
     * Sets the dialog position.
     */
    private void setDialogPosition() {
    	if (this.graphDesktop!=null) {
    		
    		int dialogWidth = 220;
    		int dialogHeight = 32;
    		int toolBarWidth =  this.basicGraphGui.getJToolBar(ToolBarType.LayoutControl).getSize().width;
    		int xPos = this.graphDesktop.getSize().width - dialogWidth - toolBarWidth - 15;
    		this.setBounds(xPos, 0, dialogWidth, dialogHeight);
    	
    	} else {
    		this.setSize(220, 32);
    	}
    }
    
    
    /**
     * Returns the combo box model.
     * @return the combo box model
     */
    private DefaultComboBoxModel<String> getComboBoxModel() {
    	if (comboBoxModel==null) {
    		comboBoxModel = new DefaultComboBoxModel<>();
    	}
    	return comboBoxModel;
    }
    /**
     * Fills the combo box model.
     */
    private void fillComboBoxModel() {
    	
    	// --- Clear the combo box model first ------------ 
    	this.getComboBoxModel().removeAllElements();
    	
    	// --- Get the names of the available layouts -----
    	List<String> lsNameList = new ArrayList<>(); 
    	List<LayoutSettings> lsList = new ArrayList<>(this.graphController.getNetworkModel().getGeneralGraphSettings4MAS().getLayoutSettings().values());
    	for (int i = 0; i < lsList.size(); i++) {
    		lsNameList.add(lsList.get(i).getLayoutName());
		}
    	Collections.sort(lsNameList);
    	
    	// --- Fill the combo box model -------------------
    	this.isPauseComboBoxActionListener = true;
    	for (int i = 0; i < lsNameList.size(); i++) {
			this.getComboBoxModel().addElement(lsNameList.get(i));
		}
    	
    	// --- Set the currently selected item ------------
    	this.getComboBoxModel().setSelectedItem(this.graphController.getNetworkModel().getLayoutSettings().getLayoutName());
    	this.isPauseComboBoxActionListener = false;
    }
    /**
     * Returns the layout selection combo box.
     * @return the combo box for the layout selection
     */
    private JComboBoxWide<String> getJComboBoxLayout() {
		if (jComboBoxLayout == null) {
			jComboBoxLayout = new JComboBoxWide<>(this.getComboBoxModel());
			jComboBoxLayout.setToolTipText(Language.translate("Swicht netowrk model layout ...", Language.EN));
			jComboBoxLayout.setPreferredSize(new Dimension(250, 26));
			jComboBoxLayout.addActionListener(this);
			this.fillComboBoxModel();
		}
		return jComboBoxLayout;
	}
   
 
    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
    	
    	if (ae.getSource()==this.getJComboBoxLayout() && this.isPauseComboBoxActionListener==false) {
			// --- Set the new selected layout ? ----------
    		String layoutCurrent = this.graphController.getNetworkModel().getLayoutSettings().getLayoutName();
    		String layoutNew = (String) this.getComboBoxModel().getSelectedItem();
    		if (layoutNew!=null && layoutNew.equals(layoutCurrent)==false) {
    			// --- Set the new layout ID --------------
    			String newLayoutID = this.graphController.getNetworkModel().getGeneralGraphSettings4MAS().getLayoutIdByLayoutName(layoutNew);
    			this.graphController.getNetworkModelUndoManager().setLayoutIdAndExchangeLayoutSettings(newLayoutID);
    		}
    	}
    }
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		if (updateObject instanceof NetworkModelNotification) {
    		
    		NetworkModelNotification nmNotification = (NetworkModelNotification) updateObject;
    		switch (nmNotification.getReason()) {
    		case NetworkModelNotification.NETWORK_MODEL_ComponentTypeSettingsChanged:
    		case NetworkModelNotification.NETWORK_MODEL_Reload:
    			this.fillComboBoxModel();
    			break;
				
    		case NetworkModelNotification.NETWORK_MODEL_LayoutChanged:
    			String layoutName = this.graphController.getNetworkModel().getLayoutSettings().getLayoutName();
    			this.getJComboBoxLayout().setSelectedItem(layoutName);
    			break;
    			
    		case NetworkModelNotification.NETWORK_MODEL_GraphMouse_EdgeEditing:
    			this.getJComboBoxLayout().setEnabled(false);
    			break;

    		case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
    			this.getJComboBoxLayout().setEnabled(true);
    			break;
    			
			default:
				break;
			}
    	}
		
	}
	
}