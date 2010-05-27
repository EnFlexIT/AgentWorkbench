package gui.projectwindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class SetupSimulationAgents extends JPanel implements Observer, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3929823093128900880L;
	private Project currProject;
	private JScrollPane jScrollPaneStartList = null;
	private JList jListStartList = null;
	private JPanel jPanelStarter = null;
	private JButton jButtonStarterAdd = null;
	private JButton jButtonStarterRemove = null;
	private JPanel jPanelRight = null;
	private JLabel jLabelStarter = null;
	private JButton jButtonMoveUp = null;
	private JButton jButtonMoveDown = null;
	/**
	 * This is the default constructor
	 */
	public SetupSimulationAgents( Project project ) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);		
		initialize();	
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 5;
		gridBagConstraints6.gridy = 0;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 5;
		gridBagConstraints5.anchor = GridBagConstraints.NORTH;
		gridBagConstraints5.gridy = 5;
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridwidth = 1;
		gridBagConstraints41.gridy = 4;
		gridBagConstraints41.anchor = GridBagConstraints.NORTH;
		gridBagConstraints41.insets = new Insets(10, 0, 3, 0);
		gridBagConstraints41.fill = GridBagConstraints.NONE;
		gridBagConstraints41.gridheight = 1;
		gridBagConstraints41.gridx = 5;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 0;
		gridBagConstraints31.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints31.anchor = GridBagConstraints.SOUTHWEST;
		gridBagConstraints31.gridwidth = 1;
		gridBagConstraints31.gridheight = 1;
		gridBagConstraints31.gridy = 0;
		jLabelStarter = new JLabel();
		jLabelStarter.setText("Start-Konfiguration");
		jLabelStarter.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints.gridheight = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(700, 350);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.add(getJScrollPaneStartList(), gridBagConstraints);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getJPanelStarter(), gridBagConstraints1);
		this.add(jLabelStarter, gridBagConstraints31);
		this.add(getJButtonMoveUp(), gridBagConstraints41);
		this.add(getJButtonMoveDown(), gridBagConstraints5);
		this.add(getJPanelRight(), gridBagConstraints6);
		
		
	}

	/**
	 * This method initializes ProjectTitel	
	 * 	
	 * @return javax.swing.JTextField	
	 */


	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		System.out.println( "ActCMD/Wert => " + ActCMD );
		System.out.println( "Auslöser => " + Trigger );

		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == "ProjectName" ) {

		}
		else {
			
		};
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method initializes jScrollPaneStartList	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneStartList() {
		if (jScrollPaneStartList == null) {
			jScrollPaneStartList = new JScrollPane();
			jScrollPaneStartList.setViewportView(getJListStartList());
		}
		return jScrollPaneStartList;
	}

	/**
	 * This method initializes jListStartList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJListStartList() {
		if (jListStartList == null) {
			jListStartList = new JList();
			
			
		}
		return jListStartList;
	}

	/**
	 * This method initializes jPanelStarter	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStarter() {
		if (jPanelStarter == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new Insets(10, 10, 0, 10);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.gridx = 0;
			jPanelStarter = new JPanel();
			jPanelStarter.setLayout(new GridBagLayout());
			jPanelStarter.setPreferredSize(new Dimension(20, 46));
			jPanelStarter.add(getJButtonStarterAdd(), gridBagConstraints2);
			jPanelStarter.add(getJButtonStarterRemove(), gridBagConstraints3);
		}
		return jPanelStarter;
	}

	/**
	 * This method initializes jButtonStarterAdd	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStarterAdd() {
		if (jButtonStarterAdd == null) {
			jButtonStarterAdd = new JButton();
			jButtonStarterAdd.setPreferredSize(new Dimension(45, 26));
			jButtonStarterAdd.setActionCommand("StartElementAdd");
			jButtonStarterAdd.setIcon(new ImageIcon(getClass().getResource("/img/ListPlus.png")));
			jButtonStarterAdd.setToolTipText("Add Start-Element");
		}
		return jButtonStarterAdd;
	}

	/**
	 * This method initializes jButtonStarterRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStarterRemove() {
		if (jButtonStarterRemove == null) {
			jButtonStarterRemove = new JButton();
			jButtonStarterRemove.setPreferredSize(new Dimension(45, 26));
			jButtonStarterRemove.setActionCommand("StartElementRemove");
			jButtonStarterRemove.setIcon(new ImageIcon(getClass().getResource("/img/ListMinus.png")));
			jButtonStarterRemove.setToolTipText("Remove Start-Element");
		}
		return jButtonStarterRemove;
	}

	/**
	 * This method initializes jPanelRight	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			jPanelRight = new JPanel();
			jPanelRight.setLayout(new GridBagLayout());
		}
		return jPanelRight;
	}

	/**
	 * This method initializes jButtonMoveUp	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonMoveUp() {
		if (jButtonMoveUp == null) {
			jButtonMoveUp = new JButton();
			jButtonMoveUp.setIcon(new ImageIcon(getClass().getResource("/img/ArrowUp.png")));
			jButtonMoveUp.setPreferredSize(new Dimension(45, 26));
			jButtonMoveUp.setActionCommand("OntoObjectUp");
			
			jButtonMoveUp.setToolTipText("Objekt nach oben");
		}
		return jButtonMoveUp;
	}

	/**
	 * This method initializes jButtonMoveDown	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonMoveDown() {
		if (jButtonMoveDown == null) {
			jButtonMoveDown = new JButton();
			jButtonMoveDown.setPreferredSize(new Dimension(45, 26));
			jButtonMoveDown.setActionCommand("OntoObjectDown");
			jButtonMoveDown.setIcon(new ImageIcon(getClass().getResource("/img/ArrowDown.png")));
			jButtonMoveDown.setToolTipText("Objekt nach unten");
		}
		return jButtonMoveDown;
	}

	
}  //  @jve:decl-index=0:visual-constraint="17,4"
