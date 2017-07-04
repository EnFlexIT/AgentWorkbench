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
package gasmas.compStat;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;

public class CompressorStationToolBar extends JToolBar implements Observer, ActionListener {

	private static final long serialVersionUID = 3028106117258589532L;

	private final String pathImage = GraphGlobals.getPathImages();
	private final ImageIcon iconPlus  =	new ImageIcon(getClass().getResource(pathImage + "ListPlus.png"));  //  @jve:decl-index=0:
	private final ImageIcon iconMinus =	new ImageIcon(getClass().getResource(pathImage + "ListMinus.png"));
	
	private CompressorStationModel compressorStationModel = null; // @jve:decl-index=0:
	private CompressorStationEditorPanel compressorStationEditorPanel = null; 
	
	private JButton jButtonPopUpCompressor = null;
	private JButton jButtonPopUpDrives = null;
	private JButton jButtonRemoveComponent = null;
	
	private JPopupMenu jPopupMenuToolsCompressor = null;
	private JMenuItem jMenuItemTurboCompressorAdd = null;
	private JMenuItem jMenuItemPistonCompressorAdd = null;
	
	private JPopupMenu jPopupMenuToolsDrives = null;
	private JMenuItem jMenuItemGasTurbineAdd = null;
	private JMenuItem jMenuItemGasDrivenMotorAdd = null;
	private JMenuItem jMenuItemElectricMotorAdd = null;
	private JMenuItem jMenuItemSteamTurbineAdd = null;

	
	public CompressorStationToolBar(CompressorStationEditorPanel compressorStationEditorPanel) {
		this.compressorStationEditorPanel = compressorStationEditorPanel;
		this.compressorStationModel = this.compressorStationEditorPanel.getCompressorStationModel();
		this.compressorStationModel.addObserver(this);
		this.initialize();
	}
	private void initialize() {
        this.setFloatable(false);
        this.add(getJButtonPopUpCompressors());
        this.addSeparator();
        this.add(getJButtonPopUpDrives());
        this.addSeparator();
        this.add(getJButtonRemoveComponent());
	}
	
	private JButton getJButtonPopUpCompressors() {
		if (jButtonPopUpCompressor == null) {
			jButtonPopUpCompressor = new JButton(" " + Language.translate("Compressor", Language.EN) + " ");
			jButtonPopUpCompressor.setToolTipText(Language.translate("Add Compressor ...", Language.EN));
			jButtonPopUpCompressor.addActionListener(this);
			jButtonPopUpCompressor.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPopUpCompressors();
				}
				@Override
				public void mousePressed(MouseEvent e) {
					showPopUpCompressors();
				}
			});
		}
		return jButtonPopUpCompressor;
	}
	private void showPopUpCompressors() {
		this.getJPopupMenuCompressorTools().show(this.getJButtonPopUpCompressors(), 0, this.getJButtonPopUpCompressors().getHeight());
	}
	
	private JButton getJButtonPopUpDrives() {
		if (jButtonPopUpDrives == null) {
			jButtonPopUpDrives = new JButton(" " + Language.translate("Drives", Language.EN) + " ");
			jButtonPopUpDrives.setToolTipText(Language.translate("Add Drive ...", Language.EN));
			jButtonPopUpDrives.addActionListener(this);
			jButtonPopUpDrives.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showPopUpDrives();
				}
				@Override
				public void mousePressed(MouseEvent e) {
					showPopUpDrives();
				}
			});
		}
		return jButtonPopUpDrives;
	}
	private void showPopUpDrives() {
		this.getJPopupMenuDriveTools().show(this.getJButtonPopUpDrives(), 0, this.getJButtonPopUpDrives().getHeight());
	}
	
	private JButton getJButtonRemoveComponent() {
		if (jButtonRemoveComponent == null) {
			jButtonRemoveComponent = new JButton();
			jButtonRemoveComponent.setSize(new Dimension(26, 26));
			jButtonRemoveComponent.setPreferredSize(new Dimension(26, 26));
			jButtonRemoveComponent.setToolTipText(Language.translate("Remove Component", Language.EN));
			jButtonRemoveComponent.setIcon(iconMinus);
			jButtonRemoveComponent.addActionListener(this);
		}
		return jButtonRemoveComponent;
	}

	// ----------------------------------------------------
	private JPopupMenu getJPopupMenuCompressorTools() {
		if (jPopupMenuToolsCompressor == null) {
			jPopupMenuToolsCompressor = new JPopupMenu("Compressor Tools");
			jPopupMenuToolsCompressor.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) );
			jPopupMenuToolsCompressor.add(getJMenuItemTurboCompressorAdd());
			jPopupMenuToolsCompressor.add(getJMenuItemPistonCompressorAdd());
			
		}
		return jPopupMenuToolsCompressor;
	}
	private JMenuItem getJMenuItemTurboCompressorAdd() {
		if (jMenuItemTurboCompressorAdd == null) {
			jMenuItemTurboCompressorAdd = new JMenuItem(Language.translate("Add Turbo Compressor", Language.EN));
			jMenuItemTurboCompressorAdd.setIcon(iconPlus); 
			jMenuItemTurboCompressorAdd.addActionListener(this);
		}
		return jMenuItemTurboCompressorAdd;
	}
	private JMenuItem getJMenuItemPistonCompressorAdd() {
		if (jMenuItemPistonCompressorAdd == null) {
			jMenuItemPistonCompressorAdd = new JMenuItem(Language.translate("Add Piston Compressor", Language.EN));
			jMenuItemPistonCompressorAdd.setIcon(iconPlus); 
			jMenuItemPistonCompressorAdd.addActionListener(this);
		}
		return jMenuItemPistonCompressorAdd;
	}
	
	// ----------------------------------------------------
	private JPopupMenu getJPopupMenuDriveTools() {
		if (jPopupMenuToolsDrives == null) {
			jPopupMenuToolsDrives = new JPopupMenu("Drive Tools");
			jPopupMenuToolsDrives.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) );
			jPopupMenuToolsDrives.add(getJMenuItemGasTurbineAdd());
			jPopupMenuToolsDrives.add(getJMenuItemGasDrivenMotorAdd());
			jPopupMenuToolsDrives.add(getJMenuItemElectricMotorAdd());
			jPopupMenuToolsDrives.add(getJMenuItemSteamTurbineAdd());
		}
		return jPopupMenuToolsDrives;
	}
	private JMenuItem getJMenuItemGasTurbineAdd() {
		if (jMenuItemGasTurbineAdd == null) {
			jMenuItemGasTurbineAdd = new JMenuItem(Language.translate("Add Gas Turbine", Language.EN));
			jMenuItemGasTurbineAdd.setIcon(iconPlus); 
			jMenuItemGasTurbineAdd.addActionListener(this);
		}
		return jMenuItemGasTurbineAdd;
	}
	private JMenuItem getJMenuItemGasDrivenMotorAdd() {
		if (jMenuItemGasDrivenMotorAdd == null) {
			jMenuItemGasDrivenMotorAdd = new JMenuItem(Language.translate("Add Gas Driven Motor", Language.EN));
			jMenuItemGasDrivenMotorAdd.setIcon(iconPlus); 
			jMenuItemGasDrivenMotorAdd.addActionListener(this);
		}
		return jMenuItemGasDrivenMotorAdd;
	}
	private JMenuItem getJMenuItemElectricMotorAdd() {
		if (jMenuItemElectricMotorAdd== null) {
			jMenuItemElectricMotorAdd = new JMenuItem(Language.translate("Add Electric Motor", Language.EN));
			jMenuItemElectricMotorAdd.setIcon(iconPlus); 
			jMenuItemElectricMotorAdd.addActionListener(this);
		}
		return jMenuItemElectricMotorAdd;
	}
	private JMenuItem getJMenuItemSteamTurbineAdd() {
		if (jMenuItemSteamTurbineAdd == null) {
			jMenuItemSteamTurbineAdd = new JMenuItem(Language.translate("Add Steam Turbine", Language.EN));
			jMenuItemSteamTurbineAdd.setIcon(iconPlus); 
			jMenuItemSteamTurbineAdd.addActionListener(this);
		}
		return jMenuItemSteamTurbineAdd;
	}
	
	// ----------------------------------------------------
	private String getNewComponentID(String componentType) {
		
		String newID = null;
		String message = Language.translate("Please, specifiy the ID for the new " + componentType + "!", Language.EN);
		String title = Language.translate("Add component", Language.EN);
		String userInput = JOptionPane.showInternalInputDialog(this.compressorStationEditorPanel, message, title, JOptionPane.INFORMATION_MESSAGE);
		if (userInput==null) {
			// --- Cancel was pressed ---------------------
			newID = null;
		} else if (userInput.equals("")) {
			// --- An empty String was specified ----------
			message = Language.translate("The ID for the new " + componentType + " has to be specified!", Language.EN);
			title = Language.translate("Missing ID!", Language.EN);
			JOptionPane.showInternalMessageDialog(this.compressorStationEditorPanel, message, title, JOptionPane.WARNING_MESSAGE);
			newID = null;
			
		} else {
			// --- Error handling -------------------------
			if (this.compressorStationModel.isNewID(userInput)==true) {
				newID = userInput;
			} else {
				message = userInput + ": " + Language.translate("The ID is already in use. Please specify another identifier!", Language.EN);
				title = "'" + userInput + "' " + Language.translate("is already in use!", Language.EN);
				JOptionPane.showInternalMessageDialog(this.compressorStationEditorPanel, message, title, JOptionPane.WARNING_MESSAGE);
				newID = null;	
			}
		}
		return newID;
	}
	
	@Override
	public void update(Observable observable, Object object) {
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String newID = null;
		Object actionSource = ae.getSource();
		if (actionSource==this.getJButtonPopUpCompressors()) {
			// ------------------------------------------------------
			// --- Show tool PopUp for compressor -------------------
			// ------------------------------------------------------
			this.showPopUpCompressors();
			
		} else if (actionSource==this.getJMenuItemTurboCompressorAdd()) {
			newID = this.getNewComponentID(Language.translate("Turbo Compressor", Language.EN));
			if (newID!=null) {
				this.compressorStationModel.addTurboCompressor(newID);
			}
			
		} else if (actionSource==this.getJMenuItemPistonCompressorAdd()) {
			newID = this.getNewComponentID(Language.translate("Piston Compressor", Language.EN));
			if (newID!=null) {
				this.compressorStationModel.addPistonCompressor(newID);
			}

		} else if (actionSource==this.getJButtonPopUpDrives()) {
			// ------------------------------------------------------
			// --- Show tool PopUp for drives -----------------------
			// ------------------------------------------------------
			this.showPopUpDrives();
			
		} else if (actionSource==this.getJMenuItemGasTurbineAdd()) {
			newID = this.getNewComponentID(Language.translate("Gas Turbine", Language.EN));
			if (newID!=null) {
				this.compressorStationModel.addGasTurbine(newID);
			}

		} else if (actionSource==this.getJMenuItemGasDrivenMotorAdd()) {
			newID = this.getNewComponentID(Language.translate("Gas Driven Motor", Language.EN));
			if (newID!=null) {
				this.compressorStationModel.addGasDrivenMotor(newID);
			}

		} else if (actionSource==this.getJMenuItemElectricMotorAdd()) {
			newID = this.getNewComponentID(Language.translate("Electric Motor", Language.EN));
			if (newID!=null) {
				this.compressorStationModel.addElectricMotor(newID);
			}

		} else if (actionSource==this.getJMenuItemSteamTurbineAdd()) {
			newID = this.getNewComponentID(Language.translate("Steam Turbine", Language.EN));
			if (newID!=null) {
				this.compressorStationModel.addSteamTurbine(newID);
			}

		} else if (actionSource==this.getJButtonRemoveComponent()) {
			// ------------------------------------------------------
			// --- Remove component ---------------------------------
			// ------------------------------------------------------
			String title = null;
			String message = null;
			String idComponent = this.compressorStationEditorPanel.getIDofFocusedDisplayedComponent();
			if (idComponent!=null) {
				title = idComponent + " - " + Language.translate("Delete component?", Language.EN);
				message =  Language.translate("Delete component: ", Language.EN) + "'" + idComponent + "'?\n\n";
				message += Language.translate("Are you sure that you want to delete this component?", Language.EN);
				int answer = JOptionPane.showInternalConfirmDialog(this.compressorStationEditorPanel, message, title, JOptionPane.YES_NO_OPTION);
				if (answer==JOptionPane.YES_OPTION) {
					this.compressorStationModel.deleteComponent(idComponent);
				}
				
			} else {
				title = Language.translate("Delete component:", Language.EN);
				message = Language.translate("Please, select the tab of the component that you want to delete!", Language.EN);
				JOptionPane.showInternalMessageDialog(this.compressorStationEditorPanel, message, title, JOptionPane.INFORMATION_MESSAGE);				
			}
			
		}
		
	}
	
	
}
