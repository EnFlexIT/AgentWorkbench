package de.enflexit.awb.baseUI.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.common.swing.JHyperLink;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.language.Language;

/**
 * The GUI of the AboutDialog.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AboutDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -5882844235988801425L;
	
	private JPanel jContentPane;

	private JTabbedPane jTabbedPane;
	private JPanel jPanelGeneral;
	private JPanel jPanelMembers;

	private JLabel jLabelTitle;
	private JLabel jLabelIcon;
	private JLabel jLabelVersion;
	private JLabel jLabelCopyright1;
	private JLabel jLabelCopyright2;
	private JLabel jLabelCopyright3;
	
	private JHyperLink jLabelLinkGitHub;
	private JHyperLink jLabelLinkGitBook;
	private JHyperLink jLabelLinkSOFTEC;
	private JLabel jLabelDummy;
	
	private JLabel jLabelMembership;
	private String teamMember = "";
	
	private JButton jButtonOk;
	private JPanel jPanelSystemInformation;
	private JScrollPane jScrollPaneSystemInfo;
	private JTextArea jTextAreaSystemInfo;
	
	
	/**
	 * Instantiates a new about dialog.
	 */
	public AboutDialog() {
		this(null);
	}
	/**
	 * Instantiates a new about dialog.
	 * @param owner the owner
	 */
	public AboutDialog(Window owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {

		teamMember = "<HTML><BODY><CENTER>Christian Derksen<br>" +
					"Hanno-Felix Wagner<br>" +
					"Nils Loose<br>" +
					"Christopher Nde<br>" +
					"Marvin Steinberg<br>" +
					"Tim Lewen<br>" +
					"Satyadeep Karnati<br>" +
					"David Pachula<br>" +
					"Hanno Monschan<br>" +
					"Mohamed Amine Jedidi" +
					"</CENTER></HTML></BODY>";

		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		
		this.setModal(true);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.registerEscapeKeyStroke();
		
		// --- Set the Look and Feel of the Dialog ------------------
		ExecutionMode execMode = Application.getGlobalInfo().getExecutionMode(); 
		if (Application.isRunningAsServer()==true ||  execMode==ExecutionMode.DEVICE_SYSTEM) {
			if (Application.getGlobalInfo().getAppLookAndFeelClassName()!=null) {
				this.setLookAndFeel();
			}
		}

		// --- Translate --------------------------------------------
		this.setTitle( Application.getGlobalInfo().getApplicationTitle() );
		jLabelTitle.setText( Application.getGlobalInfo().getApplicationTitle());
		jLabelVersion.setText("Version: " +  Application.getGlobalInfo().getVersionInfo().getFullVersionInfo(false, " "));
		jLabelCopyright3.setText(Language.translate("Alle Rechte vorbehalten."));
		this.getJTextAreaSystemInfo().setText(Application.getGlobalInfo().getSystemInformation());
		
		// --- Set Dialog size and position -------------------------
		this.setSize(650, 500);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
	/**
	 * This method initializes jContentPane.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			GridBagLayout gbl_Content = new GridBagLayout();
			gbl_Content.columnWidths = new int[]{0, 0, 0};
			gbl_Content.rowHeights = new int[]{0, 0, 0, 0};
			gbl_Content.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_Content.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			
			GridBagConstraints gbc_JLabelIcon = new GridBagConstraints();
			gbc_JLabelIcon.insets = new Insets(20, 40, 5, 5);
			gbc_JLabelIcon.gridx = 0;
			gbc_JLabelIcon.gridy = 0;

			GridBagConstraints gbc_JLableTitle = new GridBagConstraints();
			gbc_JLableTitle.anchor = GridBagConstraints.WEST;
			gbc_JLableTitle.insets = new Insets(20, 0, 5, 0);
			gbc_JLableTitle.gridx = 1;
			gbc_JLableTitle.gridy = 0;

			GridBagConstraints gbc_JLabelVersion = new GridBagConstraints();
			gbc_JLabelVersion.anchor = GridBagConstraints.WEST;
			gbc_JLabelVersion.insets = new Insets(0, 0, 5, 0);
			gbc_JLabelVersion.gridx = 1;
			gbc_JLabelVersion.gridy = 1;
			
			GridBagConstraints gbc_JTabbedPane = new GridBagConstraints();
			gbc_JTabbedPane.fill = GridBagConstraints.BOTH;
			gbc_JTabbedPane.weightx = 1.0;
			gbc_JTabbedPane.weighty = 1.0;
			gbc_JTabbedPane.insets = new Insets(20, 40, 5, 40);
			gbc_JTabbedPane.gridwidth = 2;
			gbc_JTabbedPane.gridx = 0;
			gbc_JTabbedPane.gridy = 2;

			GridBagConstraints gbc_JButtonOk = new GridBagConstraints();
			gbc_JButtonOk.anchor = GridBagConstraints.CENTER;
			gbc_JButtonOk.insets = new Insets(20, 0, 30, 0);
			gbc_JButtonOk.gridwidth = 2;
			gbc_JButtonOk.gridx = 0;
			gbc_JButtonOk.gridy = 3;

			
			jLabelIcon = new JLabel();
			jLabelIcon.setText(" ");
			jLabelIcon.setIcon(GlobalInfo.getInternalImageIconAwbIcon48());
			
			jLabelTitle = new JLabel();
			jLabelTitle.setText("Agent.GUI");
			jLabelTitle.setFont(new Font("Arial", Font.BOLD, 24));
			
			jLabelVersion = new JLabel();
			jLabelVersion.setText("Version:");
			jLabelVersion.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelVersion.setToolTipText("");
			
			jContentPane = new JPanel();
			jContentPane.setLayout(gbl_Content);
			jContentPane.add(jLabelIcon, gbc_JLabelIcon);
			jContentPane.add(jLabelTitle, gbc_JLableTitle);
			jContentPane.add(jLabelVersion, gbc_JLabelVersion);
			jContentPane.add(getJButtonOk(), gbc_JButtonOk);
			jContentPane.add(getJTabbedPane(), gbc_JTabbedPane);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButtonOk.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setText("Schließen");
			jButtonOk.setText(Language.translate(jButtonOk.getText()));
			jButtonOk.setForeground(new Color(0, 0 ,153));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setPreferredSize(new Dimension(120, 28));
			jButtonOk.setActionCommand("OK");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}		

	/**
	 * This method set the Look and Feel of this Dialog.
	 * @param newLnF the new look and feel
	 */
	private void setLookAndFeel() {
 
		String lnfClassName = Application.getGlobalInfo().getAppLookAndFeelClassName();
		if (lnfClassName==null) return;

		String currLookAndFeelClassName = UIManager.getLookAndFeel().getClass().getName();
		if (lnfClassName.equals(currLookAndFeelClassName)==true) return;

		try {
			UIManager.setLookAndFeel(lnfClassName);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Cannot install " + lnfClassName + " on this platform: " + e.getMessage());
		}
		
	}

	/**
	 * This method initializes jTabbedPane	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setFont(new Font("Dialog", Font.BOLD, 13));
			jTabbedPane.setTabPlacement(JTabbedPane.TOP);
			
			jTabbedPane.addTab("Copyright", null, getJPanelGeneral(), null);
			jTabbedPane.addTab("Mitwirkende", null, getJPanelMembers(), null);
			jTabbedPane.setTitleAt(1, Language.translate("Mitwirkende"));
			jTabbedPane.addTab("Systeminformationen", null, this.getJPanelSystemInformation(), null);
			jTabbedPane.setTitleAt(2, Language.translate("Systeminformationen"));
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanelGeneral	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGeneral() {
		if (jPanelGeneral == null) {
			
			GridBagConstraints gridBagConstraintsDummy = new GridBagConstraints();
			gridBagConstraintsDummy.gridx = 0;
			gridBagConstraintsDummy.fill = GridBagConstraints.BOTH;
			gridBagConstraintsDummy.weightx = 1.0;
			gridBagConstraintsDummy.weighty = 1.0;
			gridBagConstraintsDummy.insets = new Insets(10, 10, 10, 10);
			gridBagConstraintsDummy.gridy = 6;
			
			GridBagConstraints gridBagConstraintsCopyRight1 = new GridBagConstraints();
			gridBagConstraintsCopyRight1.anchor = GridBagConstraints.CENTER;
			gridBagConstraintsCopyRight1.gridx = -1;
			gridBagConstraintsCopyRight1.gridy = -1;
			gridBagConstraintsCopyRight1.fill = GridBagConstraints.NONE;
			gridBagConstraintsCopyRight1.weightx = 0.0;
			gridBagConstraintsCopyRight1.insets = new Insets(30, 10, 0, 0);

			GridBagConstraints gridBagConstraintsCopsRight2 = new GridBagConstraints();
			gridBagConstraintsCopsRight2.anchor = GridBagConstraints.CENTER;
			gridBagConstraintsCopsRight2.gridx = 0;
			gridBagConstraintsCopsRight2.gridy = 1;
			gridBagConstraintsCopsRight2.insets = new Insets(5, 10, 0, 0);

			GridBagConstraints gridBagConstraintsCopyRight3 = new GridBagConstraints();
			gridBagConstraintsCopyRight3.anchor = GridBagConstraints.CENTER;
			gridBagConstraintsCopyRight3.gridx = 0;
			gridBagConstraintsCopyRight3.gridy = 2;
			gridBagConstraintsCopyRight3.insets = new Insets(5, 10, 0, 0);

			GridBagConstraints gridBagConstraintsLinkAWB = new GridBagConstraints();
			gridBagConstraintsLinkAWB.anchor = GridBagConstraints.CENTER;
			gridBagConstraintsLinkAWB.gridx = 0;
			gridBagConstraintsLinkAWB.gridy = 3;
			gridBagConstraintsLinkAWB.insets = new Insets(15, 10, 0, 0);
			
			GridBagConstraints gridBagConstraintsLinkAgentGui = new GridBagConstraints();
			gridBagConstraintsLinkAgentGui.anchor = GridBagConstraints.CENTER;
			gridBagConstraintsLinkAgentGui.gridx = 0;
			gridBagConstraintsLinkAgentGui.gridy = 4;
			gridBagConstraintsLinkAgentGui.insets = new Insets(5, 10, 0, 0);

			GridBagConstraints gridBagConstraintsLinkDAWIS = new GridBagConstraints();
			gridBagConstraintsLinkDAWIS.gridx = 0;
			gridBagConstraintsLinkDAWIS.anchor = GridBagConstraints.CENTER;
			gridBagConstraintsLinkDAWIS.insets = new Insets(5, 10, 0, 0);
			gridBagConstraintsLinkDAWIS.gridy = 5;

						
			jLabelCopyright1 = new JLabel();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			jLabelCopyright1.setText("Copyright \u00A9 2009-" + year + " by Christian Derksen,");
			jLabelCopyright1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelCopyright2 = new JLabel();
			jLabelCopyright2.setText("SOFTEC / DAWIS @ ICB - University of Duisburg-Essen");
			jLabelCopyright2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelCopyright3 = new JLabel();
			jLabelCopyright3.setText("Alle Rechte vorbehalten.");
			jLabelCopyright3.setToolTipText("");
			jLabelCopyright3.setFont(new Font("Dialog", Font.PLAIN, 12));
			
			jLabelLinkGitHub = new JHyperLink();
			jLabelLinkGitHub.setText("https://github.com/EnFlexIT/AgentWorkbench");
			jLabelLinkGitHub.addActionListener(this);
			jLabelLinkGitBook = new JHyperLink();
			jLabelLinkGitBook.setText("https://enflexit.gitbook.io/agent-workbench/");
			jLabelLinkGitBook.addActionListener(this);
			jLabelLinkSOFTEC = new JHyperLink();
			jLabelLinkSOFTEC.setText("http://www.softec.wiwi.uni-due.de");
			jLabelLinkSOFTEC.addActionListener(this);
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText(" ");
			
			jPanelGeneral = new JPanel();
			jPanelGeneral.setLayout(new GridBagLayout());
			jPanelGeneral.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelGeneral.add(jLabelCopyright1, gridBagConstraintsCopyRight1);
			jPanelGeneral.add(jLabelCopyright2, gridBagConstraintsCopsRight2);
			jPanelGeneral.add(jLabelCopyright3, gridBagConstraintsCopyRight3);
			jPanelGeneral.add(jLabelLinkGitHub, gridBagConstraintsLinkAWB);
			jPanelGeneral.add(jLabelLinkGitBook, gridBagConstraintsLinkAgentGui);
			jPanelGeneral.add(jLabelLinkSOFTEC, gridBagConstraintsLinkDAWIS);
			jPanelGeneral.add(jLabelDummy, gridBagConstraintsDummy);
		}
		return jPanelGeneral;
	}

	/**
	 * This method initializes jPanelMembers	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelMembers() {
		if (jPanelMembers == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.fill = GridBagConstraints.BOTH;

			jLabelMembership = new JLabel();
			jLabelMembership.setPreferredSize(new Dimension(260, 100));
			jLabelMembership.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelMembership.setFont(new Font("Dialog", Font.PLAIN, 13));
			jLabelMembership.setText(teamMember);
			
			jPanelMembers = new JPanel();
			jPanelMembers.setLayout(new GridBagLayout());
			jPanelMembers.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelMembers.add(jLabelMembership, gridBagConstraints6);
		}
		return jPanelMembers;
	}
	
	private JPanel getJPanelSystemInformation() {
		if (jPanelSystemInformation==null) {
			jPanelSystemInformation = new JPanel();
			GridBagLayout gbl_jPanelSystemInformation = new GridBagLayout();
			gbl_jPanelSystemInformation.columnWidths = new int[]{0, 0};
			gbl_jPanelSystemInformation.rowHeights = new int[]{0, 0};
			gbl_jPanelSystemInformation.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelSystemInformation.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			jPanelSystemInformation.setLayout(gbl_jPanelSystemInformation);
			
			GridBagConstraints gbc_jScrollPaneSystemInfo = new GridBagConstraints();
			gbc_jScrollPaneSystemInfo.insets = new Insets(5, 5, 5, 5);
			gbc_jScrollPaneSystemInfo.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneSystemInfo.gridx = 0;
			gbc_jScrollPaneSystemInfo.gridy = 0;
			jPanelSystemInformation.add(this.getjScrollPaneSystemInfo(), gbc_jScrollPaneSystemInfo);
			
		}
		return jPanelSystemInformation;
	}
	private JScrollPane getjScrollPaneSystemInfo() {
		if (jScrollPaneSystemInfo==null) {
			jScrollPaneSystemInfo = new JScrollPane();
			jScrollPaneSystemInfo.setViewportView(this.getJTextAreaSystemInfo());
		}
		return jScrollPaneSystemInfo;
	}
	private JTextArea getJTextAreaSystemInfo() {
		if (jTextAreaSystemInfo==null) {
			jTextAreaSystemInfo = new JTextArea();
			jTextAreaSystemInfo.setEditable(false);
			jTextAreaSystemInfo.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jTextAreaSystemInfo;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		if (ae.getSource()==jLabelLinkGitHub || ae.getSource()==jLabelLinkGitBook || ae.getSource()==jLabelLinkSOFTEC) {
			this.followHyperlink(actCMD);
		
		} else if (ae.getSource()==this.getJButtonOk()) {
			this.setVisible(false);
		}
	}
	
	/**
	 * Follows the specified hyperlink.
	 * @param uriString the URI string
	 */
	private void followHyperlink(String uriString) {
		Application.browseURI(uriString);
	}
	
}
