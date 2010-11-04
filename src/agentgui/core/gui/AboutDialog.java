package agentgui.core.gui;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JHyperLink;

public class AboutDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -5882844235988801425L;
	
	private final String PathImage = Application.RunInfo.PathImageIntern();
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();
	
	private JPanel jContentPane = null;
	private JLabel jLabelIcon = null;
	private JLabel jLabelVersion = null;
	private JLabel jLabelTitle = null;
	private JLabel jLabelCopyright1 = null;
	private JLabel jLabelCopyright2 = null;
	private JLabel jLabelCopyright3 = null;
	private JHyperLink jLabelLink = null;

	private JButton jButtonOk = null;

	private JLabel jLabelMember = null;
	private JLabel jLabelMembership = null;
	private String teamMember = "";  //  @jve:decl-index=0:
	/**
	 * @param owner
	 */
	public AboutDialog(Frame owner) {
		super(owner);
		
		teamMember = "<HTML><BODY>Christian Derksen<br>Hanno-Felix Wagner<br>Nils Loose<br>Christopher Nde<br>Marvin Steinberg</HTML></BODY>";
		
		// --- Set the Look and Feel of the Dialog ------------------
		if (Application.isServer==true) {
			if (Application.RunInfo.AppLnF()!=null) {
				setLookAndFeel( Application.RunInfo.AppLnF() );
			}
		}
		
		// --- Create/Config der Dialog-Elemnete --------------------
		initialize();
		
		// --- Übersetzungen einstellen -----------------------------
		this.setTitle( Application.RunInfo.AppTitel() );
		jLabelTitle.setText( Application.RunInfo.AppTitel());
		jLabelVersion.setText("Version: " +  Application.RunInfo.AppVersion());
		jLabelCopyright3.setText(Language.translate("Alle Rechte vorbehalten."));
		
		// --- Dialog zentrieren ------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);			
		
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {

		this.setSize(549, 445);
		this.setIconImage(imageAgentGUI);
		
		this.setModal(true);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		
		// --- Set the IconImage ----------------------------------


	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 7;
			jLabelMembership = new JLabel();
			jLabelMembership.setPreferredSize(new Dimension(260, 85));
			jLabelMembership.setText(teamMember);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 6;
			jLabelMember = new JLabel();
			jLabelMember.setText("Mitwirkende:");
			jLabelMember.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(20, 0, 20, 0);
			gridBagConstraints4.gridy = 8;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 2;
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints31.gridy = 5;
			jLabelLink = new JHyperLink();
			jLabelLink.setText("http://www.dawis.wiwi.uni-due.de/");
			jLabelLink.addActionListener(this);
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 2;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints21.gridy = 4;
			jLabelCopyright3 = new JLabel();
			jLabelCopyright3.setText("Alle Rechte vorbehalten.");
			jLabelCopyright3.setToolTipText("");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints11.gridy = 3;
			jLabelCopyright2 = new JLabel();
			jLabelCopyright2.setText("& DAWIS @ ICB - University of Duisburg-Essen");
			jLabelCopyright2.setToolTipText("");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints3.gridy = 2;
			jLabelCopyright1 = new JLabel();
			jLabelCopyright1.setText("Copyright \u00A9 2009-2010 by Christian Derksen");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints2.gridy = 0;
			jLabelTitle = new JLabel();
			jLabelTitle.setText(" Agent.GUI");
			jLabelTitle.setFont(new Font("Arial", Font.BOLD, 24));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			jLabelVersion = new JLabel();
			jLabelVersion.setText("Version:");
			jLabelVersion.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelVersion.setToolTipText("");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.insets = new Insets(20, 20, 0, 10);
			gridBagConstraints.gridy = 0;
			jLabelIcon = new JLabel();
			jLabelIcon.setText("");
			jLabelIcon.setFont(new Font("Arial", Font.BOLD, 18));
			jLabelIcon.setIcon(new ImageIcon(getClass().getResource(PathImage + "AgentGUI.png")));
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelIcon, gridBagConstraints);
			jContentPane.add(jLabelTitle, gridBagConstraints2);
			jContentPane.add(jLabelVersion, gridBagConstraints1);
			jContentPane.add(jLabelCopyright1, gridBagConstraints3);
			jContentPane.add(jLabelCopyright2, gridBagConstraints11);
			jContentPane.add(jLabelCopyright3, gridBagConstraints21);
			jContentPane.add(jLabelLink, gridBagConstraints31);
			jContentPane.add(getJButtonOk(), gridBagConstraints4);
			jContentPane.add(jLabelMember, gridBagConstraints5);
			jContentPane.add(jLabelMembership, gridBagConstraints6);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButtonOk	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setText("OK");
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setActionCommand("OK");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}		

	/**
	 * This method set the Look and Feel of this Dialog
	 * @param NewLnF
	 */
	private void setLookAndFeel( String NewLnF ) {
		// --- Look and fell einstellen --------------- 
		if ( NewLnF == null ) return;		
		Application.RunInfo.setAppLnf( NewLnF );
		try {
			String lnfClassname = Application.RunInfo.AppLnF();
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lnfClassname);
				SwingUtilities.updateComponentTreeUI(this);				
		} 
		catch (Exception e) {
				System.err.println("Cannot install " + Application.RunInfo.AppLnF()
					+ " on this platform:" + e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		
		// --- Hyperlink abfangen -----------------------------------
		if (ae.getSource() == jLabelLink ) {
			try {
				Desktop.getDesktop().browse(new URI(actCMD));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return;
		}
		
		// --- Andere Aktionen abfangen -----------------------------
		if (actCMD.equalsIgnoreCase("ok")==true) {
			this.setVisible(false);
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="36,11"
