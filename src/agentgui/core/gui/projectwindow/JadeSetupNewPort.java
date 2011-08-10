package agentgui.core.gui.projectwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Project;

public class JadeSetupNewPort extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel jContentPane = null;
	private JLabel jLabelNewPort = null;
	private JTextField jTextFieldDefaultPort = null;
	private JButton jButtonSetPortDefault = null;
	private JButton jButtonCancel = null;

	private final KeyStroke keyStrokeESCAPE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);  //  @jve:decl-index=0:
	private final KeyStroke keyStrokeENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);  //  @jve:decl-index=0:
	
	private Point currPosition = null;
	private Project currProject = null;
	private boolean canceled = false;
	private Integer localJadePort = null;
	
	
	/**
	 * This is the default constructor
	 */
	public JadeSetupNewPort(Frame owner, String titel, boolean modal, Project project, Point position) {
		super(owner, titel, modal);
		currProject = project;
		currPosition = position;
		initialize();
		
		// --- Take the "To-use-Port" from the current Project ------
		localJadePort = currProject.JadeConfiguration.getLocalPort();
		this.jTextFieldDefaultPort.setText(localJadePort.toString());
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(new Dimension(420, 46));
		this.setUndecorated(true);
		this.setTitle("Set Jade-LocalPort ...");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		this.setContentPane(getJContentPane());
		
	    // --- ESCAPE abfangen --------------------------------------
	    this.getRootPane().registerKeyboardAction(this, "KeyESCAPE", keyStrokeESCAPE, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	    this.getRootPane().registerKeyboardAction(this, "KeyENTER", keyStrokeENTER,JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

	    // --- Position des Dialogs einstellen ----------------------
	    this.setLocation(currPosition.x, currPosition.y-11);
	    
	}
	/**
	 * This Method returns true if the Dialog-Action was canceld
	 * @return
	 */
	public boolean isCanceled(){
		return canceled;
	}
	
	/**
	 * This Method returns the actual set port 
	 * @return
	 */
	public Integer getNewLocalPort4Jade(){
		return localJadePort;
	}
	
	/**
	 * This method initializes jTextFieldDefaultPort	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDefaultPort() {
		if (jTextFieldDefaultPort == null) {
			jTextFieldDefaultPort = new JTextField();
			jTextFieldDefaultPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldDefaultPort.setBounds(new Rectangle(107, 10, 71, 26));
			jTextFieldDefaultPort.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldDefaultPort;
	}

	/**
	 * This method initializes jContentPane	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {

			jLabelNewPort = new JLabel();
			jLabelNewPort.setText("New LocalPort:");
			jLabelNewPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelNewPort.setBounds(new Rectangle(13, 15, 93, 16));

			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
			jContentPane.add(getJTextFieldDefaultPort(), null);
			jContentPane.add(jLabelNewPort, null);
			jContentPane.add(getJButtonSetPortDefault(), null);
			jContentPane.add(getJButtonCancel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButtonSetPortDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetPortDefault() {
		if (jButtonSetPortDefault == null) {
			jButtonSetPortDefault = new JButton();
			jButtonSetPortDefault.setText("OK");
			jButtonSetPortDefault.setBounds(new Rectangle(215, 10, 77, 26));
			jButtonSetPortDefault.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSetPortDefault.setForeground(new Color(0, 153, 0));
			jButtonSetPortDefault.setActionCommand("SetPortDefault");
			jButtonSetPortDefault.addActionListener(this);
		}
		return jButtonSetPortDefault;
	}
	
	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setBounds(new Rectangle(305, 10, 77, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setText("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * Here all Dialog-Actions will be captured
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object Trigger = ae.getSource();
		String ActCMD = ae.getActionCommand();

		if (Trigger==jButtonSetPortDefault || ActCMD=="KeyENTER") {
			// === OK-Action ========================================

			// --- Fehlerprüfung ------------------------------------
			Long newIntLocalPort = Long.valueOf(this.jTextFieldDefaultPort.getText());
			
			// --- Zahl überprüfen ----------------------------------
			if ( newIntLocalPort == null || newIntLocalPort==0) {
				return;
			} 
			this.jTextFieldDefaultPort.setText(newIntLocalPort.toString());
			
			// --- Länge überprüfen ---------------------------------
			String newStrLocalPort = this.jTextFieldDefaultPort.getText();
			if ( newStrLocalPort.length()>4 ) {
				newStrLocalPort = newStrLocalPort.substring(0, 4);
				this.jTextFieldDefaultPort.setText(newStrLocalPort);
				return;
			}
			// --- Neuen Wert merken --------------------------------
			this.localJadePort = Integer.valueOf(this.jTextFieldDefaultPort.getText());
			this.canceled = false;
			this.setVisible(false);
		
		} else if (Trigger==jButtonCancel || ActCMD=="KeyESCAPE" ) {
			// === Cancel-Action ====================================
			this.canceled = true;
			this.setVisible(false);
			
		} else {
			System.out.println( "Unknown Action " + ActCMD );
			System.out.println( "Source.  " + Trigger );
		}
		
	}

}  //  @jve:decl-index=0:visual-constraint="18,16"
