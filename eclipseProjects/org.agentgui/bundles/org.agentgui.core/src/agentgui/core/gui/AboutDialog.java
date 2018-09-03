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
package agentgui.core.gui;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.gui.components.JHyperLink;

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
	
	private JHyperLink jLabelLinkAWB;
	private JHyperLink jLabelLinkAgentGui;
	private JHyperLink jLabelLinkDAWIS;
	private JLabel jLabelDummy;
	
	private JLabel jLabelMembership;
	private String teamMember = "";
	
	private JButton jButtonOk;
	
	
	/**
	 * Instantiates a new about dialog.
	 */
	public AboutDialog() {
		this.initialize();
	}
	
	/**
	 * Instantiates a new about dialog.
	 * @param owner the owner
	 */
	public AboutDialog(Frame owner) {
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

		this.setSize(550, 480);
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
		
		// --- Set Dialog position ----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);			
		
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
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(20, 40, 5, 5);
			gridBagConstraints31.gridy = 0;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.BOTH;
			gridBagConstraints22.gridy = 2;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.weighty = 1.0;
			gridBagConstraints22.insets = new Insets(20, 40, 5, 40);
			gridBagConstraints22.gridwidth = 2;
			gridBagConstraints22.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.CENTER;
			gridBagConstraints4.insets = new Insets(20, 0, 30, 0);
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridy = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(20, 0, 5, 0);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.insets = new Insets(20, 20, 0, 10);
			gridBagConstraints.gridy = 0;
			
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
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelIcon, gridBagConstraints31);
			jContentPane.add(jLabelTitle, gridBagConstraints2);
			jContentPane.add(jLabelVersion, gridBagConstraints1);
			jContentPane.add(getJButtonOk(), gridBagConstraints4);
			jContentPane.add(getJTabbedPane(), gridBagConstraints22);
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
			jTabbedPane.setName("");
			jTabbedPane.setTabPlacement(JTabbedPane.TOP);
			
			jTabbedPane.addTab("Copyright", null, getJPanelGeneral(), null);
			
			jTabbedPane.addTab("Mitwirkende", null, getJPanelMembers(), null);
			jTabbedPane.setTitleAt(1, Language.translate("Mitwirkende"));
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
			jLabelCopyright1.setText("Copyright \u00A9 2009-" + year + " by Christian Derksen");
			jLabelCopyright1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelCopyright2 = new JLabel();
			jLabelCopyright2.setText("& DAWIS @ ICB - University of Duisburg-Essen");
			jLabelCopyright2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelCopyright3 = new JLabel();
			jLabelCopyright3.setText("Alle Rechte vorbehalten.");
			jLabelCopyright3.setToolTipText("");
			jLabelCopyright3.setFont(new Font("Dialog", Font.PLAIN, 12));
			
			jLabelLinkAWB = new JHyperLink();
			jLabelLinkAWB.setText("https://github.com/EnFlexIT/AgentWorkbench");
			jLabelLinkAWB.addActionListener(this);
			jLabelLinkAgentGui = new JHyperLink();
			jLabelLinkAgentGui.setText("http://www.agentgui.org");
			jLabelLinkAgentGui.addActionListener(this);
			jLabelLinkDAWIS = new JHyperLink();
			jLabelLinkDAWIS.setText("http://dawis.wiwi.uni-due.de");
			jLabelLinkDAWIS.addActionListener(this);
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText(" ");
			
			jPanelGeneral = new JPanel();
			jPanelGeneral.setLayout(new GridBagLayout());
			jPanelGeneral.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelGeneral.add(jLabelCopyright1, gridBagConstraintsCopyRight1);
			jPanelGeneral.add(jLabelCopyright2, gridBagConstraintsCopsRight2);
			jPanelGeneral.add(jLabelCopyright3, gridBagConstraintsCopyRight3);
			jPanelGeneral.add(jLabelLinkAWB, gridBagConstraintsLinkAWB);
			jPanelGeneral.add(jLabelLinkAgentGui, gridBagConstraintsLinkAgentGui);
			jPanelGeneral.add(jLabelLinkDAWIS, gridBagConstraintsLinkDAWIS);
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
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		if (ae.getSource()==jLabelLinkAWB || ae.getSource()==jLabelLinkAgentGui || ae.getSource()==jLabelLinkDAWIS) {
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
		try {
			this.followHyperlink(new URI(uriString));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Follows the specified hyperlink.
	 * @param uri the URI
	 */
	private void followHyperlink(URI uri) {
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
