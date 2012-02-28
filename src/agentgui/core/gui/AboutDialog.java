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
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JHyperLink;
import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;

/**
 * The GUI of the AboutDialog.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AboutDialog extends JDialog implements ActionListener{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5882844235988801425L;
	
	private final String PathImage = Application.RunInfo.PathImageIntern();
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();
	
	private JPanel jContentPane = null;

	private JTabbedPane jTabbedPane = null;
	private JPanel jPanelGeneral = null;
	private JPanel jPanelMembers = null;

	private JLabel jLabelTitle = null;
	private JLabel jLabelIcon = null;
	private JLabel jLabelVersion = null;
	private JLabel jLabelCopyright1 = null;
	private JLabel jLabelCopyright2 = null;
	private JLabel jLabelCopyright3 = null;
	private JHyperLink jLabelLink = null;
	private JHyperLink jLabelLinkDAWIS = null;
	private JLabel jLabelDummy = null;
	
	private JLabel jLabelMembership = null;
	private String teamMember = "";  //  @jve:decl-index=0:
	
	private JButton jButtonOk = null;
	
	
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

		teamMember = "<HTML><BODY><CENTER><br>Christian Derksen<br>" +
					"Hanno-Felix Wagner<br>" +
					"Nils Loose<br>" +
					"Christopher Nde<br>" +
					"Marvin Steinberg<br>" +
					"Tim Lewen<br>" +
					"Satyadeep Karnati<br>" +
					"David Pachula" +
					"</CENTER></HTML></BODY>";

		this.setSize(550, 445);
		this.setIconImage(imageAgentGUI);
		
		this.setModal(true);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.registerEscapeKeyStroke();
		
		// --- Set the Look and Feel of the Dialog ------------------
		if (Application.isServer==true) {
			if (Application.RunInfo.getAppLnF()!=null) {
				setLookAndFeel(Application.RunInfo.getAppLnF());
			}
		}

		// --- ‹bersetzungen einstellen -----------------------------
		this.setTitle( Application.RunInfo.getApplicationTitle() );
		jLabelTitle.setText( Application.RunInfo.getApplicationTitle());
		jLabelVersion.setText("Version: " +  Application.Version.getFullVersionInfo(false, " "));
		jLabelCopyright3.setText(Language.translate("Alle Rechte vorbehalten."));
		
		// --- Dialog zentrieren ------------------------------------
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
			gridBagConstraints31.insets = new Insets(20, 40, 0, 0);
			gridBagConstraints31.gridy = 0;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.BOTH;
			gridBagConstraints22.gridy = 2;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.weighty = 1.0;
			gridBagConstraints22.insets = new Insets(20, 40, 0, 40);
			gridBagConstraints22.gridwidth = 2;
			gridBagConstraints22.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.CENTER;
			gridBagConstraints4.insets = new Insets(20, 0, 30, 0);
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridy = 11;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.insets = new Insets(20, 20, 0, 10);
			gridBagConstraints.gridy = 0;
			
			jLabelIcon = new JLabel();
			jLabelIcon.setText(" ");
			jLabelIcon.setIcon(new ImageIcon(getClass().getResource(PathImage + "AgentGUI.png")));
			
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
			jButtonOk.setText("Schlieﬂen");
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
	 *
	 * @param NewLnF the new look and feel
	 */
	private void setLookAndFeel( String NewLnF ) {
		// --- Look and fell einstellen --------------- 
		if ( NewLnF == null ) return;		
		Application.RunInfo.setAppLnf( NewLnF );
		try {
			String lnfClassname = Application.RunInfo.getAppLnF();
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lnfClassname);
				SwingUtilities.updateComponentTreeUI(this);				
		} 
		catch (Exception e) {
				System.err.println("Cannot install " + Application.RunInfo.getAppLnF()
					+ " on this platform:" + e.getMessage());
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
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints5.gridy = 5;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.anchor = GridBagConstraints.CENTER;
			gridBagConstraints10.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints10.gridy = 4;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.CENTER;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 3;
			gridBagConstraints9.insets = new Insets(15, 10, 0, 0);
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.CENTER;
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.insets = new Insets(5, 10, 0, 0);
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.CENTER;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new Insets(5, 10, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.CENTER;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.weightx = 0.0;
			gridBagConstraints3.insets = new Insets(30, 10, 0, 0);
			
			jLabelCopyright1 = new JLabel();
			jLabelCopyright1.setText("Copyright \u00A9 2009-2012 by Christian Derksen");
			jLabelCopyright1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelCopyright2 = new JLabel();
			jLabelCopyright2.setText("& DAWIS @ ICB - University of Duisburg-Essen");
			jLabelCopyright2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelCopyright3 = new JLabel();
			jLabelCopyright3.setText("Alle Rechte vorbehalten.");
			jLabelCopyright3.setToolTipText("");
			jLabelCopyright3.setFont(new Font("Dialog", Font.PLAIN, 12));
			
			jLabelLink = new JHyperLink();
			jLabelLink.setText("http://www.agentgui.org");
			jLabelLink.addActionListener(this);
			jLabelLinkDAWIS = new JHyperLink();
			jLabelLinkDAWIS.setText("http://dawis.wiwi.uni-due.de");
			jLabelLinkDAWIS.addActionListener(this);
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText(" ");
			
			jPanelGeneral = new JPanel();
			jPanelGeneral.setLayout(new GridBagLayout());
			jPanelGeneral.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelGeneral.add(jLabelCopyright1, gridBagConstraints3);
			jPanelGeneral.add(jLabelCopyright2, gridBagConstraints7);
			jPanelGeneral.add(jLabelCopyright3, gridBagConstraints8);
			jPanelGeneral.add(jLabelLink, gridBagConstraints9);
			jPanelGeneral.add(jLabelLinkDAWIS, gridBagConstraints10);
			jPanelGeneral.add(jLabelDummy, gridBagConstraints5);
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
			jLabelMembership.setVerticalAlignment(SwingConstants.TOP);
			jLabelMembership.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelMembership.setFont(new Font("Dialog", Font.PLAIN, 14));
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
		
		} else if (ae.getSource() == jLabelLinkDAWIS) {
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
