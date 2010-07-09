package gui.projectwindow.simsetup;

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
import javax.swing.JTextField;

/**
 * @author: Christian Derksen
 *
 */
public class StartSetup extends JPanel implements Observer, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3929823093128900880L;
	private Project currProject;
	private JScrollPane jScrollPaneStartList = null;
	private JList jListStartList = null;
	private JButton jButtonStarterAdd = null;
	private JButton jButtonStarterRemove = null;
	private JButton jButtonMoveUp = null;
	private JButton jButtonMoveDown = null;
	private JPanel jPanelRight = null;
	private JPanel jPanelButtons = null;
	private JLabel jLabelHeader = null;
	private JTextField jTextFieldStartAs = null;
	private JButton jButtonStartOK = null;
	/**
	 * This is the default constructor
	 */
	public StartSetup( Project project ) {
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
		gridBagConstraints6.gridx = 2;
		gridBagConstraints6.anchor = GridBagConstraints.NORTH;
		gridBagConstraints6.fill = GridBagConstraints.NONE;
		gridBagConstraints6.gridy = 0;
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 3;
		gridBagConstraints51.weighty = 1.0;
		gridBagConstraints51.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints51.weightx = 1.0;
		gridBagConstraints51.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints51.anchor = GridBagConstraints.NORTH;
		gridBagConstraints51.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(766, 350);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getJScrollPaneStartList(), gridBagConstraints);
		this.add(getJPanelRight(), gridBagConstraints51);
		this.add(getJPanelButtons(), gridBagConstraints6);
		
		
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
			jScrollPaneStartList.setPreferredSize(new Dimension(300, 131));
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

	/**
	 * This method initializes jPanelRight	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints5.gridx = 1;
			jLabelHeader = new JLabel();
			jLabelHeader.setText("Starten als:");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			jPanelRight = new JPanel();
			jPanelRight.setLayout(new GridBagLayout());
			jPanelRight.add(jLabelHeader, new GridBagConstraints());
			jPanelRight.add(getJTextFieldStartAs(), gridBagConstraints5);
			jPanelRight.add(getJButtonStartOK(), gridBagConstraints7);
		}
		return jPanelRight;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints4.gridy = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.gridx = -1;
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonStarterAdd(), gridBagConstraints1);
			jPanelButtons.add(getJButtonStarterRemove(), gridBagConstraints2);
			jPanelButtons.add(getJButtonMoveUp(), gridBagConstraints3);
			jPanelButtons.add(getJButtonMoveDown(), gridBagConstraints4);
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jTextFieldStartAs	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldStartAs() {
		if (jTextFieldStartAs == null) {
			jTextFieldStartAs = new JTextField();
			jTextFieldStartAs.setPreferredSize(new Dimension(4, 26));
		}
		return jTextFieldStartAs;
	}

	/**
	 * This method initializes jButtonStartOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStartOK() {
		if (jButtonStartOK == null) {
			jButtonStartOK = new JButton();
			jButtonStartOK.setPreferredSize(new Dimension(50, 20));
			jButtonStartOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonStartOK.setText("OK");
		}
		return jButtonStartOK;
	}

	
}  //  @jve:decl-index=0:visual-constraint="20,8"
