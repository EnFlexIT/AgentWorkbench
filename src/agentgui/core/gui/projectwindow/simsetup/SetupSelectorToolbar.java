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
package agentgui.core.gui.projectwindow.simsetup;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.core.sim.setup.SimulationSetupNotification;

/**
 * The Class SetupSelectorToolbar is used in the main toolbar of the application
 * and manages different simulation setups for a current project.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SetupSelectorToolbar implements ActionListener {

	private final String pathImage = Application.getGlobalInfo().getPathImageIntern();
	
	private final Integer SETUP_add = 1;
	private final Integer SETUP_rename = 2;
	private final Integer SETUP_copy = 3;
	
	private Project currProject = null;
	private SimulationSetup currSimSetup = null;
	
	private MyObserver myProjectMyObserver = null;
	private JToolBar jToolBar2Add2 = null;
	
	private JLabel jLabelSetupSelector = null;
	private JComboBox<String> jComboBoxSetupSelector = null;
	private DefaultComboBoxModel<String> jComboBoxModel4Setups = new DefaultComboBoxModel<String>(); 
	
	private JButton jButtonSetupRename = null;
	private JButton jButtonSetupCopy = null;
	private JButton jButtonSetupNew = null;
	private JButton jButtonSetupDelete = null;
	
	
	/**
	 * Instantiates a new setup selector toolbar.
	 * @param jToolBar2Add2 the JToolBar, on which the elements of this class are added
	 */
	public SetupSelectorToolbar(JToolBar jToolBar2Add2) {
		this.jToolBar2Add2 = jToolBar2Add2;
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.jToolBar2Add2.add(this.getJLabel4SetupSelection());
		this.jToolBar2Add2.add(this.getJComboBoxSetupSelector());
		
		this.jToolBar2Add2.add(this.getJButtonSetupRename());
		this.jToolBar2Add2.add(this.getJButtonSetupCopy());
		this.jToolBar2Add2.add(this.getJButtonSetupNew());
		this.jToolBar2Add2.add(this.getJButtonSetupDelete());
		
		
		// --- Translate ----------------------------------
		this.jComboBoxSetupSelector.setToolTipText(Language.translate("Setup auswählen"));
		this.jButtonSetupRename.setToolTipText(Language.translate("Setup umbenennen"));
		this.jButtonSetupCopy.setToolTipText(Language.translate("Setup kopieren"));
		this.jButtonSetupNew.setToolTipText(Language.translate("Setup hinzufügen"));
		this.jButtonSetupDelete.setToolTipText(Language.translate("Setup löschen"));
		
		this.setEnabled(false);
		
	}
	
	/**
	 * Sets the project.
	 * @param project the new project
	 */
	public void setProject(Project project) {
		
		if (project==null) {
			this.setEnabled(false);
			this.myProjectMyObserver = null;
			this.currProject = null;
			this.jComboBoxSetupSelector.removeActionListener(this);
			this.jComboBoxModel4Setups.removeAllElements();
			
		} else {
			this.setEnabled(true);
			if (project!=this.currProject) {
				this.myProjectMyObserver = null;
				this.myProjectMyObserver = new MyObserver();
				this.currProject = project;
				this.currProject.addObserver(this.myProjectMyObserver);
				this.setupLoad();
			}
		}
	}
	
	/**
	 * Sets the enabled.
	 * @param enable the new enabled
	 */
	public void setEnabled(boolean enable) {
		
		this.jLabelSetupSelector.setEnabled(enable);
		this.jComboBoxSetupSelector.setEnabled(enable);
		this.jButtonSetupRename.setEnabled(enable);
		this.jButtonSetupCopy.setEnabled(enable);
		this.jButtonSetupNew.setEnabled(enable);
		this.jButtonSetupDelete.setEnabled(enable);
		
	}
	
	/**
	 * Gets the JLabel for the setup selection.
	 * @return the JLabel for the setup selection
	 */
	private JLabel getJLabel4SetupSelection() {
		if (jLabelSetupSelector==null){
			jLabelSetupSelector = new JLabel();
			jLabelSetupSelector.setText(" Setup:  ");
			jLabelSetupSelector.setFont(new Font("Dialog", Font.BOLD, 12));	
		}
		return jLabelSetupSelector;
	}
	/**
	 * This method initialises jComboBoxSetupSelector	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> getJComboBoxSetupSelector() {
		if (jComboBoxSetupSelector == null) {
			jComboBoxSetupSelector = new JComboBox<String>(jComboBoxModel4Setups);
			jComboBoxSetupSelector.setToolTipText("Setup auswählen");
			jComboBoxSetupSelector.setMaximumRowCount(18);
			jComboBoxSetupSelector.setPreferredSize(new Dimension(250, 26));
			jComboBoxSetupSelector.addActionListener(this);
		}
		return jComboBoxSetupSelector;
	}
	/**
	 * This method initialises jButtonSetupRename	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetupRename() {
		if (jButtonSetupRename == null) {
			jButtonSetupRename = new JButton();
			jButtonSetupRename.setPreferredSize(new Dimension(26, 26));
			jButtonSetupRename.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rename.gif")));
			jButtonSetupRename.setToolTipText("Setup umbenennen");
			jButtonSetupRename.addActionListener(this);

		}
		return jButtonSetupRename;
	}
	
	/**
	 * This method initialises jButtonSetupCopy	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetupCopy() {
		if (jButtonSetupCopy == null) {
			jButtonSetupCopy = new JButton();
			jButtonSetupCopy.setPreferredSize(new Dimension(26, 26));
			jButtonSetupCopy.setIcon(new ImageIcon(getClass().getResource(pathImage + "Copy.png")));
			jButtonSetupCopy.setToolTipText("Setup kopieren");
			jButtonSetupCopy.addActionListener(this);

		}
		return jButtonSetupCopy;
	}
	
	/**
	 * This method initialises jButtonSetupNew	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetupNew() {
		if (jButtonSetupNew == null) {
			jButtonSetupNew = new JButton();
			jButtonSetupNew.setPreferredSize(new Dimension(26, 26));
			jButtonSetupNew.setToolTipText("Setup hinzufügen");
			jButtonSetupNew.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
			jButtonSetupNew.addActionListener(this);
		}
		return jButtonSetupNew;
	}

	/**
	 * This method initialises jButtonSetupDelete	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetupDelete() {
		if (jButtonSetupDelete == null) {
			jButtonSetupDelete = new JButton();
			jButtonSetupDelete.setPreferredSize(new Dimension(26, 26));
			jButtonSetupDelete.setToolTipText("Setup löschen");
			jButtonSetupDelete.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
			jButtonSetupDelete.addActionListener(this);
		}
		return jButtonSetupDelete;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJComboBoxSetupSelector()) {
			if (this.getJComboBoxSetupSelector().getSelectedItem()!=null) {
				String selectedSetup = this.getJComboBoxSetupSelector().getSelectedItem().toString();	
				this.currProject.getSimulationSetups().setupSave();
				this.currProject.getSimulationSetups().setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, selectedSetup, false);
			}
		} else if (ae.getSource()==this.getJButtonSetupRename()) {
			this.setupRename();
		} else if (ae.getSource()==this.getJButtonSetupCopy()) {
			this.setupCopy();
		} else if (ae.getSource()==this.getJButtonSetupNew()) {
			this.setupAdd();
		} else if (ae.getSource()==this.getJButtonSetupDelete()) {
			this.setupRemove();
		} else {
			System.out.println(ae.toString());
		};
		
	}

	/**
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class MyObserver implements Observer {

		/* (non-Javadoc)
		 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
		 */
		@Override
		public void update(Observable observable, Object updateObject) {
			
			if (updateObject instanceof SimulationSetupNotification) {
				// --- Change inside the simulation setup ---------------
				SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
				switch (scn.getUpdateReason()) {
				case SIMULATION_SETUP_SAVED:
					break;
				default:
					setupLoad();	
					break;
				}			
			}
		}
		
	}
	
	/**
	 * Setup load.
	 */
	private void setupLoad() {
		
		if (this.currProject==null) return;
		
		// --- Determine current setup --------------------
		String currSetup = this.currProject.getSimulationSetupCurrent();
		String currSetupFile = this.currProject.getSimulationSetups().get(currSetup);
		
		// --- (Re)Create ComboBoxModel -------------------
		jComboBoxSetupSelector.removeActionListener(this);
		jComboBoxModel4Setups.removeAllElements();
		
		Vector<String> v = new Vector<String>(this.currProject.getSimulationSetups().keySet());
		Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
		Iterator<String> it = v.iterator();
		while (it.hasNext()) {
			String setupName = it.next().trim();
			if (jComboBoxModel4Setups.getIndexOf(setupName)!=-1) {
				jComboBoxModel4Setups.removeElement(setupName);
			}
			jComboBoxModel4Setups.addElement(setupName);
		}
		
		// --- Set to current SimulationSetup -------------
		jComboBoxSetupSelector.setToolTipText("Setup '" + currSetup + "' " + Language.translate("in Datei") + " '" + currSetupFile + "'");
		jComboBoxSetupSelector.setSelectedItem(currSetup);
		jComboBoxSetupSelector.addActionListener(this);
		
		// --- Load DefaultListModel laden ---------
		this.currSimSetup = this.currProject.getSimulationSetups().getCurrSimSetup();
		if (this.currSimSetup==null) {
			this.currProject.getSimulationSetups().setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, this.currProject.getSimulationSetupCurrent(), false);
			this.currSimSetup = this.currProject.getSimulationSetups().getCurrSimSetup();
		}
	}

	/**
	 * Asks the user for a name of a setup.
	 *
	 * @param ReasonConstant the reason constant
	 * @param suggestion the suggestion
	 * @return the string for the requested setup name or null
	 */
	private String setupAskUser4SetupName(Integer ReasonConstant, String suggestion) {
		
		String head = null;
		String msg  = null;
		String input = null;
		String newFileName = null;
		boolean inputIsOk = false;
		
		while (inputIsOk==false) {
			
			switch (ReasonConstant) {
			case 1:
				head = Language.translate("Neues Setup anlegen ...");
				msg  = Language.translate("Bitte geben Sie den Namen für das neue Setup an!");
				break;
			case 2:
				head = Language.translate("Setup umbenennen ...");
				msg  = Language.translate("Bitte geben Sie den neuen Namen für das Setup an!");
				break;
			case 3:
				head = Language.translate("Setup kopieren ...");
				msg  = Language.translate("Bitte geben Sie den neuen Namen für das Setup an!");
				break;

			}
			
			if (suggestion== null) {
				input = (String) JOptionPane.showInputDialog(Application.getMainWindow(), msg, head, JOptionPane.OK_CANCEL_OPTION);	
			} else {
				input = (String) JOptionPane.showInputDialog(Application.getMainWindow(), msg, head, JOptionPane.OK_CANCEL_OPTION, null, null, suggestion);	
			}
			
			if (input==null) return null;
			input = input.trim();
			if (input.equalsIgnoreCase("")) return null;
			
			// --- Can the name be used as file name? -----
			newFileName = currProject.getSimulationSetups().getSuggestSetupFile(input);
			if (newFileName.length() < 8) {
				head = Language.translate("Setup-Name zu kurz!");
				msg  = "Name: '" + input + "' " + Language.translate("zu Datei") + ": '" + newFileName + ".xml':";
				msg += Language.translate("<br>Bitte geben Sie einen längeren Namen für das Setup an.");
				JOptionPane.showMessageDialog(Application.getMainWindow(), msg, head, JOptionPane.NO_OPTION);
			} else {
				inputIsOk = true;
			}

			// --- Wird der Name bereits verwendet ---------------------------------
			if (currProject.getSimulationSetups().containsSetupName(input)) {
				head = Language.translate("Setup-Name wird bereits verwendet!");
				msg  = Language.translate("Der Name") + " '" + input + "' " + Language.translate("wird bereits verwendet.");
				msg += Language.translate("<br>Bitte geben Sie einen anderen Namen für das Setup an.");
				JOptionPane.showMessageDialog(Application.getMainWindow(), msg, head, JOptionPane.NO_OPTION);
				inputIsOk = false;
			}
			
		}
		return input;
	}
	
	
	/**
	 * This method adds a new Simulation-Setup to this project
	 */
	private void setupAdd() {
		
		String nameNew = this.setupAskUser4SetupName(this.SETUP_add, null);
		if (nameNew==null) return;
		String fileNameNew = currProject.getSimulationSetups().getSuggestSetupFile(nameNew); 

		currProject.getSimulationSetups().setupAddNew(nameNew, fileNameNew);

	}
	
	/**
	 * This method renames the current Simulation-Setup 
	 */
	private void setupRename() {
		
		String nameOld = jComboBoxSetupSelector.getSelectedItem().toString();
		String nameNew = this.setupAskUser4SetupName(this.SETUP_rename, nameOld);
		if (nameNew==null) return;
		if (nameNew.equalsIgnoreCase(nameOld)) return;
		String fileNameNew = currProject.getSimulationSetups().getSuggestSetupFile(nameNew);

		currProject.getSimulationSetups().setupRename(nameOld, nameNew, fileNameNew);
		
	}
	
	/**
	 * This method copy the current Simulation-Setup 
	 */
	private void setupCopy(){

		String nameOld = jComboBoxSetupSelector.getSelectedItem().toString();
		String nameNew = this.setupAskUser4SetupName(this.SETUP_copy, nameOld + " (Copy)");
		if (nameNew==null) return;
		String fileNameNew = currProject.getSimulationSetups().getSuggestSetupFile(nameNew);

		currProject.getSimulationSetups().setupCopy(nameOld, nameNew, fileNameNew);
	}
	
	/**
	 * This method removes the current Simulation-Setup from to this project
	 */
	private void setupRemove() {
		
		String head = null;
		String msg  = null;
		Integer input = null;
		
		head = jComboBoxSetupSelector.getSelectedItem().toString() + ": " + Language.translate("Setup löschen?");
		msg  = Language.translate("Wollen Sie das aktuelle Setup wirklich löschen?");
		input = JOptionPane.showConfirmDialog(Application.getMainWindow(), msg, head, JOptionPane.YES_NO_OPTION);
		if (input == JOptionPane.YES_OPTION) {
			currProject.getSimulationSetups().setupRemove(jComboBoxSetupSelector.getSelectedItem().toString());
		}		
	}
	
}
