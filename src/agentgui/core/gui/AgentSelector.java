package agentgui.core.gui;

import jade.core.Agent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;


import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.jade.ClassSearcherSingle;
import agentgui.core.jade.ClassSearcherSingle.ClassSearcherUpdate;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class AgentSelector extends JDialog implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;
	
	private final String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	private ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private Image image = imageIcon.getImage();
	
	private Project currProject = null;
	
	private DefaultListModel jAgentListModel = new DefaultListModel();
	private Object[] selectedAgentClasses = null; 
	private boolean canceled = false;
	
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JList jListAgents = null;
	private JButton jButtonOk = null;
	private JButton jButtonCancel = null;
	private JPanel jPanelBottom = null;

	private JLabel jLabelSearchCaption = null;
	private JTextField jTextFieldSearch = null;

	
	public AgentSelector(Frame owner, Project project) {
		super(owner);
		this.currProject = project;
		this.currProject.addObserver(this);
		initialize();
	}
	public AgentSelector(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(721, 500);
		this.setTitle("Auswahl - Agenten");
		this.setIconImage( image );
		this.setModal(true);
		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
	 
	    // --- Übersetzungen einstellen -----------------------------
		this.setTitle(Language.translate("Auswahl - Agenten"));
		jLabelSearchCaption.setText(Language.translate("Suche"));
		jButtonOk.setText(Language.translate("Übernehmen"));
		jButtonCancel.setText(Language.translate("Abbrechen"));
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new Insets(20, 20, 0, 0);
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 0;
			jLabelSearchCaption = new JLabel();
			jLabelSearchCaption.setText("Suche");
			jLabelSearchCaption.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new Insets(5, 20, 0, 20);
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new Insets(10, 20, 25, 20);
			gridBagConstraints3.gridy = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(20, 20, 10, 20);
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPane(), gridBagConstraints);
			jContentPane.add(getJPanelBottom(), gridBagConstraints3);
			jContentPane.add(getJTextFieldSearch(), gridBagConstraints11);
			jContentPane.add(jLabelSearchCaption, gridBagConstraints21);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextFieldSearch	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setPreferredSize(new Dimension(250, 26));
			jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					filterAgentList( jTextFieldSearch.getText());
				}
			});
		}
		return jTextFieldSearch;
	}
	
	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJListAgents());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jListAgents	
	 * @return javax.swing.JList	
	 */
	private JList getJListAgents() {
		if (jListAgents == null) {
			jListAgents = new JList(jAgentListModel);
			this.refreshAgentList();
		}
		return jListAgents;
	}
	
	/**
	 * This methods will refresh the list of available agents
	 */
	@SuppressWarnings("unchecked")
	private void refreshAgentList() {
		
		Vector<Class<?>> AgentList = Application.classDetector.getAgentClasse();
		for (int i =0; i<AgentList.size();i++) {
			
			Class<? extends Agent> curAgentClass=(Class<? extends Agent>) AgentList.get(i);
			
			boolean curAgentClassFound = false;
			// --- Ist diese Klasse schon im Model? ---
			for (int j = 0; j < jAgentListModel.size(); j++) {
				AgentClassElement ace = (AgentClassElement) jAgentListModel.getElementAt(j);
				Class<?> clazz = ace.getElementClass();
				if (clazz.equals(curAgentClass)==true) {
					curAgentClassFound = true;
					break;
				}
			}
			if (curAgentClassFound==false) {
				jAgentListModel.addElement(new AgentClassElement(curAgentClass));
			}
		}
	}
	
	/**
	 * This will filter the list of Agents depending on 
	 * the content of the Input-Parameter 
	 * @param filter4
	 */
	@SuppressWarnings("unchecked")
	private void filterAgentList(String filter4) {
		
		jAgentListModel.removeAllElements();
		
		Vector<Class<?>> AgentList = Application.classDetector.getAgentClasse();
		for (int i =0; i<AgentList.size();i++) {
			Class<? extends Agent> curAgentClass=(Class<? extends Agent>) AgentList.get(i);
			if ( curAgentClass.getName().toLowerCase().contains(filter4.toLowerCase()) ) {
				jAgentListModel.addElement( new AgentClassElement(curAgentClass) );
			}		
		}
	}
	
	/**
	 * This method initializes jButtonOk	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setPreferredSize(new Dimension(120, 26));
			jButtonOk.setForeground(new Color(0, 102, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setText("Übernehmen");
			jButtonOk.setActionCommand("Ok");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}

	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setPreferredSize(new Dimension(120, 26));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setText("Abbrechen");
			jButtonCancel.setActionCommand("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jPanelBottom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.insets = new Insets(0, 40, 0, 0);
			gridBagConstraints2.weightx = 0.0;
			gridBagConstraints2.gridy = -1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.anchor = GridBagConstraints.CENTER;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 40);
			gridBagConstraints1.gridy = -1;
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.add(getJButtonOk(), gridBagConstraints1);
			jPanelBottom.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelBottom;
	}

	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	/**
	 * @return the selectedAgentClasses
	 */
	public Object[] getSelectedAgentClasses() {
		return selectedAgentClasses;
	}
	
	/**
	 * Saves the selected Agents in the ResultArray
	 * @return
	 */
	private boolean okAction() {

		if (jListAgents.getSelectedValues().length==0) {
			selectedAgentClasses = null;
			return false;
		} else {
			selectedAgentClasses = jListAgents.getSelectedValues();
			return true;
		}
	}
	
	
	/**
	 * This manages the incomming Action-Calls
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object Trigger = ae.getSource();
		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == jButtonOk ) {
			canceled = false;
			if (okAction()==false) return;
			this.setVisible(false);
		} else if ( Trigger == jButtonCancel ) {
			canceled = true;
			this.setVisible(false);
		} else {
			
		};
	}

	@Override
	public void update(Observable o, Object notifyObject) {

		String ObjectName = notifyObject.toString();
		if ( ObjectName.equals(ClassSearcherSingle.classSearcherNotify) ) {
			ClassSearcherUpdate csu = (ClassSearcherUpdate) notifyObject;
			if (csu.getClass2SearchFor().equals(Agent.class)) {
				this.refreshAgentList();
			}
		} else {
			//System.out.println("Unbekannte Meldung vom Observer: " + ObjectName);
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
