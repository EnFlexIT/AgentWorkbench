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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
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
 * The Class ChangesDialog will display the changes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ChangeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final String pathImage = Application.getGlobalInfo().getPathImageIntern();
	private final Image imageAgentGUI = new ImageIcon( this.getClass().getResource( pathImage + "AgentGUI.png") ).getImage();  //  @jve:decl-index=0:
	
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JEditorPane jEditorPaneChanges = null;
	
	private JPanel jPanelBottom = null;
	private JLabel jLabelVersion = null;
	private JComboBox jComboBoxVersions = null;
	private JLabel jLabelDummy = null;
	private JButton jButtonClose = null;
	
	/** The HTML file with the version changes. */
	private String changeFilesPackage = "agentgui/";  //  @jve:decl-index=0:
	

	/**
	 * Instantiates a new changes dialog.
	 * @param owner the owner
	 */
	public ChangeDialog(Frame owner) {
		super(owner);
		this.initialize();
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
	private void loadBuildChanges(String versionNumber) {
		
		try {
			String versionFile = changeFilesPackage + "build." + versionNumber + ".changes.html";
			URL changesFileURL = getClass().getClassLoader().getResource(versionFile);
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
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.insets = new Insets(0, 17, 20, 15);
			gridBagConstraints2.gridy = 1;
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
			jContentPane.add(getJPanelBottom(), gridBagConstraints2);
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
						} catch (URISyntaxException urie) {
							System.err.println("Could not find: '" + linkURI +"'");
						}
					}
				}
			});
		}
		return jEditorPaneChanges;
	}

	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;

			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 0.0;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints3.gridx = 1;
			
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridy = 0;
			
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.CENTER;
			gridBagConstraints1.gridx = 3;
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);

			jLabelVersion = new JLabel();
			jLabelVersion.setText("Version:");
			jLabelVersion.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText("");
			
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.add(jLabelVersion, gridBagConstraints4);
			jPanelBottom.add(getJComboBoxVersions(), gridBagConstraints3);
			jPanelBottom.add(jLabelDummy, gridBagConstraints5);			
			jPanelBottom.add(getJButtonClose(), gridBagConstraints1);

		}
		return jPanelBottom;
	}
	
	/**
	 * Gets the combo box model for the versions.
	 * @return the combo box model
	 */
	private DefaultComboBoxModel getComboBoxModel() {

		DefaultComboBoxModel cbm = new DefaultComboBoxModel();
		Vector<String> versionNumbers = new Vector<String>(); 

		// --- Find all files in the package 'agentgui/ -------------
		Vector<String> fileNameFound = new Vector<String>();
		URL changeFilesURL = getClass().getClassLoader().getResource(this.changeFilesPackage);
		if (changeFilesURL!=null) {
			if (changeFilesURL.getProtocol().equals("file")) {
				// --- Happens in the IDE ---------------------------
				try {
					String[] tmpFileNameFound = new File(changeFilesURL.toURI()).list();
					for (int i = 0; i < tmpFileNameFound.length; i++) {
						fileNameFound.add(tmpFileNameFound[i]);
					}
					
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}	
			
			} else if(changeFilesURL.getProtocol().equals("jar")) {
				// --- Happens in the AgentGui.jar ------------------
				JarInputStream jarFile=null;
				try {
					jarFile = new JarInputStream(new FileInputStream(Application.getGlobalInfo().getFileRunnableJar(true)));
					int pathSepPosition = this.changeFilesPackage.indexOf("/");
					while(true) {
						JarEntry jarEntry=jarFile.getNextJarEntry ();
						if(jarEntry==null) break;

						String jarEntryName = jarEntry.getName();
						if((jarEntryName.startsWith(this.changeFilesPackage)) && jarEntryName.lastIndexOf("/")<=pathSepPosition) {
							fileNameFound.add(jarEntryName);
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					// --- Ensure that the file will be closed again ---
					try {
						jarFile.close();
					} catch (IOException e) {
					}	
				}
			}
			
		} 
		
		if (fileNameFound!=null) {
			// --- Files found - get the version files -------------- 
			versionNumbers = new Vector<String>();
			for (int i = 0; i < fileNameFound.size(); i++) {
				String fileName = fileNameFound.get(i);
				if (fileName.endsWith(".html")) {
					int cut1 = fileName.indexOf("build") + 6;
					int cut2 = fileName.indexOf("changes") - 1;
					String versionNumber = fileName.substring(cut1, cut2);
					versionNumbers.addElement(versionNumber);
				}
			}
			// --- Sort result descending ---------------------------
			Collections.sort(versionNumbers, new Comparator<String>() {
				public int compare(String version1, String version2) {
					int compared = 0;
					Integer buildNo1 = Integer.parseInt(version1.substring(version1.indexOf("-")+1));
					Integer buildNo2 = Integer.parseInt(version2.substring(version2.indexOf("-")+1));
					compared = buildNo1.compareTo(buildNo2);
					return compared*-1;
				};
			});
			// --- Fill the combo box model -------------------------
			cbm = new DefaultComboBoxModel(versionNumbers);
			
			// --- set the current file -----------------------------
			this.loadBuildChanges(versionNumbers.get(0));
		}
		return cbm;
	}
	
	/**
	 * This method initializes jComboBoxVersions	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxVersions() {
		if (jComboBoxVersions == null) {
			jComboBoxVersions = new JComboBox(this.getComboBoxModel());
			jComboBoxVersions.setPreferredSize(new Dimension(120, 26));
			jComboBoxVersions.addActionListener(this);
		}
		return jComboBoxVersions;
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
		} else if (ae.getSource()==this.getJComboBoxVersions()) {
			String versionNumber = (String) this.getJComboBoxVersions().getSelectedItem();
			this.loadBuildChanges(versionNumber);
		}
		
	}

	

}  //  @jve:decl-index=0:visual-constraint="10,10"
