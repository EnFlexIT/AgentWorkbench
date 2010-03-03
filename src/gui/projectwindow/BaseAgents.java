package gui.projectwindow;

import jade.wrapper.StaleProxyException;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import contmas.agents.ControlGUIAgent;

import application.Application;
import application.Language;
import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class BaseAgents extends JPanel implements Observer, ActionListener {

	private Project CurrProject;
	private static final long serialVersionUID = 1L;

	private JScrollPane jScrollAgents = null;
	private JList jAgentList = null;
	
	private JPanel jNorthPanel = null;
	private JTextField jAgent2Start = null;
	private JTextField jAgentStartAs = null;
	
	private JLabel jLabel1 = null;
	private JLabel jLabel = null;
	private JLabel jLabelDummy1 = null;
	private JLabel jLabelDummy2 = null;
	
	private JButton jAgentListRefresh = null;
	private JButton jAgentStartButton = null;
	
	private JLabel DummyLeft = null;
	private JLabel DummyRight = null;

	/**
	 * This is the default constructor
	 */
	public BaseAgents( Project CP ) {
		super();
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
		initialize();	
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
				
		DummyRight = new JLabel();
		DummyRight.setText("");
		DummyLeft = new JLabel();
		DummyLeft.setText("");
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(10);
		borderLayout.setVgap(0);
		
		this.setLayout(borderLayout);
		this.setSize(850, 500);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getJScrollAgents(), BorderLayout.CENTER);
		this.add(getJNorthPanel(), BorderLayout.NORTH);
		this.add(DummyLeft, BorderLayout.WEST);
		this.add(DummyRight, BorderLayout.EAST);
		
	}

	/**
	 * This method initializes jScrollAgents	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollAgents() {
		if (jScrollAgents == null) {
			jScrollAgents = new JScrollPane();
			jScrollAgents.setViewportView( getJAgentList() );			
		}
		return jScrollAgents;
	}
	/**
	 * This method initializes jAgentList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJAgentList() {
		if (jAgentList == null) {			
			Vector<Class<?>> AgentList = CurrProject.getProjectAgents();
			DefaultListModel jAgentListModel = new DefaultListModel();
			for (int i =0; i<AgentList.size();i++) {
				jAgentListModel.addElement( AgentList.get(i).getName() );
			}
			jAgentList = new JList(jAgentListModel);
			jAgentList.setToolTipText("Agenten in diesem Projekt");
			jAgentList.setVisibleRowCount(12);
			jAgentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jAgentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent se) {

					if ( jAgentList.getSelectedValue() != null ) {
						// -----------------------------------------------------
						// --- Eintrag für den aktuellen Agenten vornehmen -----
						String ToDisplay = jAgentList.getSelectedValue().toString();
						int maxLenght = 30;
						if ( ToDisplay.length() > maxLenght ) {
							String ToDisplayStart = ToDisplay.substring(0, 4);
							String ToDisplayEnde = ToDisplay.substring( ToDisplay.length() - maxLenght );
							ToDisplay = ToDisplayStart + "..." + ToDisplayEnde;
						}
						jAgent2Start.setText( ToDisplay );
						// -----------------------------------------------------
						// --- Vorschlag für den Ausführungsnamen finden -------
						String StartAs = jAgentList.getSelectedValue().toString();
						StartAs = StartAs.substring(StartAs.lastIndexOf(".")+1);
						// -----------------------------------------------------
						// --- Alle Großbuchstaben filtern ---------------------
						String RegExp = "[A-Z]";	
						String StartAsNew = ""; 
						for (int i = 0; i < StartAs.length(); i++) {
							String SngChar = "" + StartAs.charAt(i);
							if ( SngChar.matches( RegExp ) == true ) {
								StartAsNew = StartAsNew + SngChar;	
								// --- ggf. den zweiten Buchstaben mitnehmen ---
								if ( i < StartAs.length() ) {
									String SngCharN = "" + StartAs.charAt(i+1);
									if ( SngCharN.matches( RegExp ) == false ) {
										StartAsNew = StartAsNew + SngCharN;	
									}
								}	
								// ---------------------------------------------
							}						
					    }
						if ( StartAsNew != "" && StartAsNew.length() >= 4 ) {
							StartAs = StartAsNew;
						}
						// -----------------------------------------------------
						// --- Check, ob es dieser Agent schon läuft -----------
						int i = 1;
						StartAsNew = StartAs;
						while ( Application.JadePlatform.jadeAgentIsRunning( StartAs, CurrProject.getProjectFolder() ) == true ){
							StartAs = StartAsNew + i;
							i++; 
						}
						// -----------------------------------------------------
						// --- Vorschlagsnamen einstellen ----------------------
						jAgentStartAs.setText(StartAs);
					}
					// ----------------------------------------------------
					// --- Fertig -----------------------------------------
					// ----------------------------------------------------
				}
			});
		}
		return jAgentList;
	}
	
	/**
	 * This method initializes jNorthPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJNorthPanel() {
		if (jNorthPanel == null) {
			
			jLabelDummy1 = new JLabel();
			jLabelDummy1.setText("");
			jLabelDummy1.setPreferredSize(new Dimension(10, 16));

			jLabelDummy2 = new JLabel();
			jLabelDummy2.setPreferredSize(new Dimension(10, 16));
			jLabelDummy2.setText("");
			
			jLabel = new JLabel();
			jLabel.setText("Agent:");			
			jLabel.setPreferredSize(new Dimension(36, 19));
			jLabel.setFont(new Font("Dialog", Font.BOLD, 12));

			jLabel1 = new JLabel();
			jLabel1.setText("Starten als: ");
			jLabel1.setFont(new Font("Dialog", Font.BOLD, 12));			

			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(10);
			flowLayout.setHgap(5);

			jNorthPanel = new JPanel();
			jNorthPanel.setLayout(flowLayout);
			jNorthPanel.setComponentOrientation(ComponentOrientation.UNKNOWN);
			jNorthPanel.add(jLabelDummy1, null);
			jNorthPanel.add(jLabel, null);
			jNorthPanel.add(getJAgent2Start(), null);
			jNorthPanel.add(jLabelDummy2, null);
			jNorthPanel.add(jLabel1, null);
			jNorthPanel.add(getJAgentStartAs(), null);
			jNorthPanel.add(getJAgentStartButton(), null);
			jNorthPanel.add(getJAgentListRefresh(), null);
		}
		return jNorthPanel;
	}

	/**
	 * This method initializes jAgent2Start	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJAgent2Start() {
		if (jAgent2Start == null) {
			jAgent2Start = new JTextField();
			jAgent2Start.setBounds( new Rectangle( 100, jAgent2Start.getHeight() ) );			
			jAgent2Start.setPreferredSize(new Dimension(250, 26));
		}
		return jAgent2Start;
	}
	/**
	 * This method initializes jAgentStartAs	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJAgentStartAs() {
		if (jAgentStartAs == null) {
			jAgentStartAs = new JTextField();
			jAgentStartAs.setPreferredSize(new Dimension(150, 26));
		}
		return jAgentStartAs;
	}
	/**
	 * This method initializes jAgentStartButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAgentStartButton() {
		if (jAgentStartButton == null) {
			jAgentStartButton = new JButton();
			jAgentStartButton.setText("Ok");
			jAgentStartButton.setPreferredSize(new Dimension(50, 26));
			jAgentStartButton.setToolTipText("Agent starten ...");
			jAgentStartButton.setFont(new Font("Dialog", Font.BOLD, 12));
			jAgentStartButton.setActionCommand("AgentStart");
			jAgentStartButton.addActionListener(this);
		}
		return jAgentStartButton;
	}

	/**
	 * This method initializes jAgentListRefresh	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJAgentListRefresh() {
		if (jAgentListRefresh == null) {
			jAgentListRefresh = new JButton();
			jAgentListRefresh.setIcon(new ImageIcon(getClass().getResource("/img/Refresh.png")));
			jAgentListRefresh.setPreferredSize(new Dimension(30, 26));
			jAgentListRefresh.setToolTipText("Agentenliste aktualisieren");
			jAgentListRefresh.setActionCommand("AgentsRefresh");
			jAgentListRefresh.addActionListener(this);			
		}
		return jAgentListRefresh;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();

		if ( Trigger == jAgentListRefresh ) {
			// --------------------------------------------
			// --- AgentList aktualisieren ----------------
			// --------------------------------------------
			// --- Aktuelle Auswahl zurücksetzen ----------
			jAgent2Start.setText(null);
			jAgentStartAs.setText(null);
			// --- Agentenliste aktualisieren -------------	
			Application.JadePlatform.jadeFindAgentClasse();
			CurrProject.filterProjectAgents();
		}
		else if ( Trigger == jAgentStartButton ) {
			// --------------------------------------------
			// --- Den ausgewählten Agenten starten -------
			// --------------------------------------------
			if ( jAgentStartAs.getText().length() == 0 ) {
				return;
			}	
			
			//HFW Change 

			/*
			try{
				Application.JadePlatform.jadeContainer(CurrProject.getProjectFolder()).acceptNewAgent(jAgentStartAs.getText(),new ControlGUIAgent(Application.Projects.get(CurrProject.getProjectFolder()).ProjectDesktop ));
			}catch(StaleProxyException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			Object[] args={Application.Projects.get(CurrProject.getProjectFolder()).ProjectDesktop};
			
			Application.JadePlatform.jadeAgentStart( jAgentStartAs.getText(), 
					 								 jAgentList.getSelectedValue().toString(), 
					 								 args,
					 								 CurrProject.getProjectFolder());
			/*
			Application.JadePlatform.jadeAgentStart( jAgentStartAs.getText(), 
													 jAgentList.getSelectedValue().toString(), 
													 CurrProject.getProjectFolder());
			*/
			//End Of HFW Change 

			jAgentStartAs.setText("");
			jAgent2Start.setText("");
			jAgentList.clearSelection();
		}
		else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);	
		};
		this.repaint();
	}

	@Override
	public void update(Observable arg0, Object OName) {
		
		String ObjectName = OName.toString();
		if ( ObjectName.equalsIgnoreCase( "ProjectAgents" ) ) {
			
			jScrollAgents.remove( jAgentList );			
			jAgentList = null;			
			jScrollAgents.setViewportView( getJAgentList() );			
		}
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
