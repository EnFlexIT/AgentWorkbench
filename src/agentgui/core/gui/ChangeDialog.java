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
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;


/**
 * The Class ChangesDialog.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ChangeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final String pathImage = Application.getGlobalInfo().PathImageIntern();
	private final Image imageAgentGUI = new ImageIcon( this.getClass().getResource( pathImage + "AgentGUI.png") ).getImage();  //  @jve:decl-index=0:
	
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JEditorPane jEditorPaneChanges = null;
	private JButton jButtonClose = null;
	
	/** The HTML file with the version changes. */
	private String changesFile = "agentgui/build.changes.html";  //  @jve:decl-index=0:
	
	
	/**
	 * Instantiates a new changes dialog.
	 * @param owner the owner
	 */
	public ChangeDialog(Frame owner) {
		super(owner);
		this.initialize();
		this.loadBuildChanges();
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		
		String title = Language.translate("Letzte Änderungen") + " - ";
		title += Application.getGlobalInfo().getVersionInfo().getFullVersionInfo(true, null);
		this.setTitle(title);
		
		this.setIconImage(imageAgentGUI);
		this.setSize(850, 550);
		this.setModal(true);
		this.setResizable(true);
		this.setContentPane(getJContentPane());
		this.registerEscapeKeyStroke();
		
		// --- Set the Look and Feel of the Dialog ------------------
		if (Application.isRunningAsServer()==true) {
			if (Application.getGlobalInfo().getAppLnF()!=null) {
				setLookAndFeel(Application.getGlobalInfo().getAppLnF());
			}
		}

		// --- Center dialog ----------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);
		
	}

	/**
	 * Load the file 'build.changes.html' that is located in
	 * the package agentgui.
	 */
	private void loadBuildChanges() {
		
		try {
			URL changesFileURL = getClass().getClassLoader().getResource(changesFile);
			this.getJEditorPaneChanges().setPage(changesFileURL);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
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
	 * This method set the Look and Feel of this Dialog.
	 * @param newLnF the new look and feel
	 */
	private void setLookAndFeel(String newLnF) {
 
		if (newLnF==null) return;	
		Application.getGlobalInfo().setAppLnf(newLnF);
		try {
			String lnfClassname = Application.getGlobalInfo().getAppLnF();
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lnfClassname);
				SwingUtilities.updateComponentTreeUI(this);
				
		} catch (Exception e) {
				System.err.println("Cannot install " + Application.getGlobalInfo().getAppLnF() + " on this platform:" + e.getMessage());
		}
		
	}
	
	/**
	 * This method initializes jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.CENTER;
			gridBagConstraints1.insets = new Insets(0, 0, 20, 0);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(15, 15, 15, 15);
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPane(), gridBagConstraints);
			jContentPane.add(getJButtonClose(), gridBagConstraints1);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(400, 100));
			jScrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollPane.setViewportView(getJEditorPaneChanges());
		}
		return jScrollPane;
	}
	
	/**
	 * This method initializes jEditorPaneChanges.
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPaneChanges() {
		if (jEditorPaneChanges == null) {
			jEditorPaneChanges = new JEditorPane();
			jEditorPaneChanges.setContentType("text/html");
			jEditorPaneChanges.setFont(new Font("Dialog", Font.PLAIN, 14));
			jEditorPaneChanges.setBackground(new Color(255, 255, 255));
			jEditorPaneChanges.setPreferredSize(new Dimension(400, 100));
			jEditorPaneChanges.setEditable(false);
			jEditorPaneChanges.addHyperlinkListener(new HyperlinkListener() {
				@Override
				public void hyperlinkUpdate(HyperlinkEvent he) {
					if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						URL linkURL = null;
						URI linkURI = null;
						try {
							linkURL = he.getURL();
							linkURI = new URI(linkURL.toString());
							Desktop.getDesktop().browse(linkURI);
							
						} catch (IOException ioe) {
							System.err.println("Could not find: '" + linkURI + "'");
//							ioe.printStackTrace();
						} catch (URISyntaxException urie) {
							System.err.println("Could not find: '" + linkURI +"'");
//							urie.printStackTrace();
						}
					}
				}
			});
		}
		return jEditorPaneChanges;
	}

	/**
	 * This method initializes jButtonClose.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setText("Schließen");
			jButtonClose.setText(Language.translate(jButtonClose.getText()));
			jButtonClose.setForeground(new Color(0, 0 ,153));
			jButtonClose.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonClose.setPreferredSize(new Dimension(120, 28));
			jButtonClose.addActionListener(this);
		}
		return jButtonClose;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonClose()) {
			this.setVisible(false);
		}
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
