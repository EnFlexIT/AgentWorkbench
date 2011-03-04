package agentgui.core.gui.projectwindow.simsetup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetups;

/**
 * @author: Christian Derksen
 *
 */
public class StartSetupSelector extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = -3929823093128900880L;
	
	private final String PathImage = Application.RunInfo.PathImageIntern(); 
	
	private final Integer SETUP_add = 1;  //  @jve:decl-index=0:
	private final Integer SETUP_rename = 2;  //  @jve:decl-index=0:
	private final Integer SETUP_copy = 3;  //  @jve:decl-index=0:
	
	private Project currProject;
	private SimulationSetup currSimSetup = null;  //  @jve:decl-index=0:
	private DefaultComboBoxModel jComboBoxModel4Setups = new DefaultComboBoxModel();  //  @jve:decl-index=0:visual-constraint="566,21"
	
	private JPanel jPanelTop = null;
	private JComboBox jComboBoxSetupSelector = null;
	private JLabel jLabelSetupSelector = null;
	private JButton jButtonSetupRename = null;
	private JButton jButtonSetupCopy = null;
	private JButton jButtonSetupNew = null;
	private JButton jButtonSetupDelete = null;
	
	/**
	 * This is the default constructor
	 */
	public StartSetupSelector( Project project ) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);		
		initialize();	
		
		// --- Das aktuelle Setup laden -------------------
		this.setupLoad();
		
		// --- Übersetzungen laden ------------------------
		jButtonSetupRename.setText(Language.translate("Umbenennen"));
		jButtonSetupCopy.setToolTipText(Language.translate("Setup kopieren"));
		jButtonSetupRename.setToolTipText(Language.translate("Setup umbenennen"));
		jButtonSetupNew.setToolTipText(Language.translate("Setup hinzufügen"));
		jButtonSetupDelete.setToolTipText(Language.translate("Setup löschen"));

	}

	/**
	 * This method initialises this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints11.gridwidth = 1;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.ipadx = 0;
		gridBagConstraints11.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(520, 53);
		this.setPreferredSize(new Dimension(520, 53));
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getJPanelTop(), gridBagConstraints11);
		
	}
	
	/**
	 * This method initialises jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints12.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 2;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.insets = new Insets(0, 5, 0, 0);
			
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 3;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.insets = new Insets(0, 10, 0, 0);
			
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 4;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.insets = new Insets(0, 10, 0, 0);
			
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 5;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints15.fill = GridBagConstraints.NONE;
			
			jLabelSetupSelector = new JLabel();
			jLabelSetupSelector.setText("Setup:");
			jLabelSetupSelector.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelTop.setPreferredSize(new Dimension(500, 38));
			jPanelTop.add(getJComboBoxSetupSelector(), gridBagConstraints12);
			jPanelTop.add(jLabelSetupSelector, gridBagConstraints13);
			jPanelTop.add(getJButtonSetupNew(), gridBagConstraints14);
			jPanelTop.add(getJButtonSetupDelete(), gridBagConstraints15);
			jPanelTop.add(getJButtonSetupRename(), gridBagConstraints16);
			jPanelTop.add(getJButtonSetupCopy(), gridBagConstraints17);
			
		}
		return jPanelTop;
	}

	/**
	 * This method initialises jComboBoxSetupSelector	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxSetupSelector() {
		if (jComboBoxSetupSelector == null) {
			jComboBoxSetupSelector = new JComboBox(jComboBoxModel4Setups);
			jComboBoxSetupSelector.setPreferredSize(new Dimension(100, 26));
			jComboBoxSetupSelector.setActionCommand("SetupSelected");
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
			jButtonSetupRename.setPreferredSize(new Dimension(110, 26));
			jButtonSetupRename.setText("Umbenennen");
			jButtonSetupRename.setToolTipText("Setup umbenennen");
			jButtonSetupRename.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSetupRename.setActionCommand("SetupRename");
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
			jButtonSetupCopy.setPreferredSize(new Dimension(45, 26));
			jButtonSetupCopy.setIcon(new ImageIcon(getClass().getResource(PathImage + "Copy.png")));
			jButtonSetupCopy.setToolTipText("Setup kopieren");
			jButtonSetupCopy.setActionCommand("SetupCopy");
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
			jButtonSetupNew.setPreferredSize(new Dimension(45, 26));
			jButtonSetupNew.setToolTipText("Setup hinzufügen");
			jButtonSetupNew.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListPlus.png")));
			jButtonSetupNew.setActionCommand("SetupNew");
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
			jButtonSetupDelete.setPreferredSize(new Dimension(45, 26));
			jButtonSetupDelete.setToolTipText("Setup löschen");
			jButtonSetupDelete.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListMinus.png")));
			jButtonSetupDelete.setActionCommand("SetupDelete");
			jButtonSetupDelete.addActionListener(this);
		}
		return jButtonSetupDelete;
	}
	
	/**
	 * This Method handles all ActionEvents from this part of the User-View
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		Object Trigger = ae.getSource();
		
		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == jComboBoxSetupSelector ) {
			if (jComboBoxSetupSelector.getSelectedItem()!= null) {
				currProject.simSetups.setupSave();
				currProject.simSetups.setupLoadAndFocus(SimulationSetups.SIMULATION_SETUP_LOAD, jComboBoxSetupSelector.getSelectedItem().toString(), false);
			}
			
		} else if ( Trigger == jButtonSetupRename ) {
			this.setupRename();
		} else if ( Trigger == jButtonSetupCopy ) {
			this.setupCopy();
		} else if ( Trigger == jButtonSetupNew ) {
			this.setupAdd();
		} else if ( Trigger == jButtonSetupDelete ) {
			this.setupRemove();
		} else {
			System.out.println(ae.toString());
		};
	}

	/**
	 * Listens to the Data-Model of this Project (MVC-Pattern)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {

		if ( arg1.toString().equalsIgnoreCase("SimSetups")) {
			this.setupLoad();
		}
		
	}
	
	private void setupLoad() {
	
		// --- Aktuelles Setup ermitteln ------------------
		String currSetup = currProject.simSetupCurrent;
		String currSetupFile = currProject.simSetups.get(currSetup);
		
		// --- ComboBoxModel neu aufbauen -----------------
		jComboBoxSetupSelector.removeActionListener(this);
		jComboBoxModel4Setups.removeAllElements();
		
		Vector<String> v = new Vector<String>(currProject.simSetups.keySet());
		Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
		Iterator<String> it = v.iterator();
		while (it.hasNext()) {
			String setupName = it.next().trim();
			if (jComboBoxModel4Setups.getIndexOf(setupName)!=-1) {
				jComboBoxModel4Setups.removeElement(setupName);
			}
			jComboBoxModel4Setups.addElement(setupName);
		}
		
		// --- Auf das aktuelle Setup setzen --------------
		jComboBoxSetupSelector.setToolTipText("Setup '" + currSetup + "' " + Language.translate("in Datei") + " '" + currSetupFile + "'");
		jComboBoxSetupSelector.setSelectedItem(currSetup);
		jComboBoxSetupSelector.addActionListener(this);
		
		// --- Das akuelle DefaultListModel laden ---------
		currSimSetup = currProject.simSetups.getCurrSimSetup();
		if ( currSimSetup==null ) {
			currProject.simSetups.setupLoadAndFocus(SimulationSetups.SIMULATION_SETUP_LOAD, currProject.simSetupCurrent, false);
			currSimSetup = currProject.simSetups.getCurrSimSetup();
		}
	}

	/**
	 * Asks the user for a name of a setup
	 * @param isAddNewSetup
	 * @return
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
				input = (String) JOptionPane.showInputDialog(Application.MainWindow, msg, head, JOptionPane.OK_CANCEL_OPTION);	
			} else {
				input = (String) JOptionPane.showInputDialog(Application.MainWindow, msg, head, JOptionPane.OK_CANCEL_OPTION, null, null, suggestion);	
			}
			
			if (input==null) return null;
			input = input.trim();
			if (input.equalsIgnoreCase("")) return null;
			
			// --- Kann der Name in einen gültigen Dateinamen umbenannt werden? ----
			newFileName = currProject.simSetups.getSuggestSetupFile(input);
			if (newFileName.length() < 8) {
				head = Language.translate("Setup-Name zu kurz!");
				msg  = "Name: '" + input + "' " + Language.translate("zu Datei") + ": '" + newFileName + ".xml':";
				msg += Language.translate("<br>Bitte geben Sie einen längeren Namen für das Setup an.");
				JOptionPane.showMessageDialog(Application.MainWindow, msg, head, JOptionPane.NO_OPTION);
			} else {
				inputIsOk = true;
			}

			// --- Wird der Name bereits verwendet ---------------------------------
			if (currProject.simSetups.containsSetupName(input)) {
				head = Language.translate("Setup-Name wird bereits verwendet!");
				msg  = Language.translate("Der Name") + " '" + input + "' " + Language.translate("wird bereits verwendet.");
				msg += Language.translate("<br>Bitte geben Sie einen anderen Namen für das Setup an.");
				JOptionPane.showMessageDialog(Application.MainWindow, msg, head, JOptionPane.NO_OPTION);
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
		String fileNameNew = currProject.simSetups.getSuggestSetupFile(nameNew); 

		currProject.simSetups.setupAddNew(nameNew, fileNameNew);

	}
	
	/**
	 * This method renames the current Simulation-Setup 
	 */
	private void setupRename() {
		
		String nameOld = jComboBoxSetupSelector.getSelectedItem().toString();
		String nameNew = this.setupAskUser4SetupName(this.SETUP_rename, nameOld);
		if (nameNew==null) return;
		if (nameNew.equalsIgnoreCase(nameOld)) return;
		String fileNameNew = currProject.simSetups.getSuggestSetupFile(nameNew);

		currProject.simSetups.setupRename(nameOld, nameNew, fileNameNew);
		
	}
	
	/**
	 * This method copy the current Simulation-Setup 
	 */
	private void setupCopy(){

		String nameOld = jComboBoxSetupSelector.getSelectedItem().toString();
		String nameNew = this.setupAskUser4SetupName(this.SETUP_copy, nameOld + " (Copy)");
		if (nameNew==null) return;
		String fileNameNew = currProject.simSetups.getSuggestSetupFile(nameNew);

		currProject.simSetups.setupCopy(nameOld, nameNew, fileNameNew);
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
		input = JOptionPane.showConfirmDialog(Application.MainWindow, msg, head, JOptionPane.YES_NO_OPTION);
		if (input == JOptionPane.YES_OPTION) {
			currProject.simSetups.setupRemove(jComboBoxSetupSelector.getSelectedItem().toString());
		}		
	}
	
}  //  @jve:decl-index=0:visual-constraint="20,8"
