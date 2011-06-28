package agentgui.core.gui.options;

import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;

public class OptionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private GlobalInfo global = Application.RunInfo;  //  @jve:decl-index=0:
	private final String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	
	private ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private Image image = imageIcon.getImage();  //  @jve:decl-index=0:
	
	private JSplitPane jSplitPaneMain = null;
	private JTabbedPane jTabbedPaneRight = null;
	private JScrollPane jScrollPaneLeft = null;
	private JTree jTreeOptions = null;
	private JPanel jPanelBase = null;
	private JPanel jPanelSouth = null;
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	
	private DefaultTreeModel OptionTreeModel;
	private DefaultMutableTreeNode RootNode;
	private TreeMap<Integer, String[]> additionalNodes = new TreeMap<Integer, String[]>();  //  @jve:decl-index=0:
	
	private StartOptions optionsStart = null;
	
	private boolean canceled = false;
	private boolean forceRestart = false;  //  @jve:decl-index=0:
		
	/**
	 * @param owner
	 */
	public OptionDialog(Frame owner) {
		super(owner);
		
		// --- OptionTree vorbereiten -------------------------------
		RootNode = new DefaultMutableTreeNode( Language.translate("Optionen") );
		OptionTreeModel = new DefaultTreeModel( RootNode );	
		
		// --- Set the Look and Feel of the Dialog ------------------
		if (Application.isServer==true) {
			if (Application.RunInfo.getAppLnF()!=null) {
				setLookAndFeel( Application.RunInfo.getAppLnF() );
			}
		}
		
		// --- Create/Config der Dialog-Elemnete --------------------
		this.initialize();

		// --- Übersetzungen konfigurieren --------------------------
	    this.setTitle( Application.RunInfo.getApplicationTitle() + ": " + Language.translate("Optionen") );
	    this.jButtonCancel.setText(Language.translate("Abbrechen"));
	    
	    // ----------------------------------------------------------
	    // --- Optionen (Sub-Panel) einbauen ------------------------
	    // ----------------------------------------------------------
	    String tabTitle = null;
	    // ----------------------------------------------------------
	    optionsStart = new StartOptions();
	    tabTitle = Language.translate("Programmstart");
	    this.addOptionTab(tabTitle, null, optionsStart, tabTitle);
	    
	    if (Application.RunInfo.isAppUseInternalConsole()==true && Application.isServer==true) {
	    	tabTitle = Language.translate("Konsole");
	    	this.addOptionTab(tabTitle, null, new OptionConsole(), tabTitle);	
	    }
	    // ----------------------------------------------------------
	    
	    
	    // --- Baumsturktur entfalten -------------------------------
	    this.OptionTreeExpand2Level(3, true);
	    
	    // --- Daten in die Formulare übernehmen --------------------
	    this.setGlobalData2Form();

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
		
		this.setModal(true);
		this.setSize(900, 563);
		this.setContentPane(getJPanelBase());
		this.setTitle("Agent.GUI: Optionen");
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		// --- Set the IconImage ----------------------------------
		this.setIconImage( image );

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
			String lnfClassname = Application.RunInfo.getAppLnF();
			if (lnfClassname == null) {
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lnfClassname);
			SwingUtilities.updateComponentTreeUI(this);				
		} 
		catch (Exception e) {
				System.err.println("Cannot install " + Application.RunInfo.getAppLnF()
					+ " on this platform:" + e.getMessage());
		}
	}		
	
	/**
	 * This method initializes jSplitPaneMain	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPaneMain() {
		if (jSplitPaneMain == null) {
			jSplitPaneMain = new JSplitPane();
			jSplitPaneMain.setDividerSize(5);
			jSplitPaneMain.setResizeWeight(0.2D);
			jSplitPaneMain.setRightComponent(getJTabbedPaneRight());
			jSplitPaneMain.setLeftComponent(getJScrollPaneLeft());
			jSplitPaneMain.setDividerLocation(200);
		}
		return jSplitPaneMain;
	}

	/**
	 * This method initializes jTabbedPaneRight	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneRight() {
		if (jTabbedPaneRight == null) {
			jTabbedPaneRight = new JTabbedPane();
			jTabbedPaneRight.setTabPlacement(JTabbedPane.TOP);
			jTabbedPaneRight.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jTabbedPaneRight;
	}

	/**
	 * This method initializes jScrollPaneLeft	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneLeft() {
		if (jScrollPaneLeft == null) {
			jScrollPaneLeft = new JScrollPane();
			jScrollPaneLeft.setViewportView(getJTreeOptions());
		}
		return jScrollPaneLeft;
	}

	/**
	 * This method initializes jTreeOptionGroup	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTreeOptions() {
		if (jTreeOptions == null) {
			jTreeOptions = new JTree(OptionTreeModel);
			jTreeOptions.setName("OptionTree");
			jTreeOptions.setShowsRootHandles(false);
			jTreeOptions.setRootVisible(true);
			jTreeOptions.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			jTreeOptions.addTreeSelectionListener( new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T A R T ----------------
					// ----------------------------------------------------------
					TreePath PathSelected = ts.getPath();
					Integer PathLevel = PathSelected.getPathCount();

					// ----------------------------------------------------------
					if ( PathLevel >= 2 ) {
						// ------------------------------------------------------
						// --- Fokus auf die entsprechende Karteikarte setzen ---
						// ------------------------------------------------------
						Component currComp = null;
						JPanel subJPanel = null;
						JTabbedPane subJTabs = null;
						String FocusNodeName = PathSelected.getPathComponent(1).toString();
						
						// --- Nach entsprechender Karteikarte suchen -----------
						for (int i=0; i<jTabbedPaneRight.getComponentCount();  i++ ) {
							currComp = jTabbedPaneRight.getComponent(i);
							if ( currComp.getName() == FocusNodeName ) {
								jTabbedPaneRight.setSelectedIndex(i);
								if ( currComp instanceof JPanel ) {
									subJPanel = (JPanel) jTabbedPaneRight.getComponent(i);	
								}
							}							
						}	
						// ------------------------------------------------------
						// --- Falls ein Aufruf aus einer tieferen Ebene kam ----
						// ------------------------------------------------------
						if (PathLevel>2 && subJPanel!=null) {
							// --- Suche nach einer JTabbedPane -----------------
							for (int i=0; i<subJPanel.getComponentCount();  i++ ) {
								currComp = subJPanel.getComponent(i);
								if ( currComp instanceof JTabbedPane ) {
									subJTabs = (JTabbedPane) currComp;
									break;									
								}							
							}	
							FocusNodeName = PathSelected.getPathComponent(2).toString();
							if (subJTabs!=null) {
								// --- Fokus auf Karteikarte setzen -------------
								for (int i=0; i<subJTabs.getComponentCount();  i++ ) {
									if ( subJTabs.getComponent(i).getName() == FocusNodeName ) {
										subJTabs.setSelectedIndex(i);
									}							
								}	
							}
						}
						// ------------------------------------------------------
					} 
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T O P ------------------
					// ----------------------------------------------------------
				}// End - valueChanged
			});
		}
		return jTreeOptions;
	}

	/**
	 * This method initializes jPanelBase	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBase() {
		if (jPanelBase == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.insets = new Insets(0, 0, 20, 0);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(20, 20, 20, 20);
			gridBagConstraints.weightx = 1.0;
			jPanelBase = new JPanel();
			jPanelBase.setLayout(new GridBagLayout());
			jPanelBase.add(getJSplitPaneMain(), gridBagConstraints);
			jPanelBase.add(getJPanelSouth(), gridBagConstraints1);
		}
		return jPanelBase;
	}

	/**
	 * This method initializes jPanelSouth	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSouth() {
		if (jPanelSouth == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 40);
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new Insets(0, 40, 0, 0);
			gridBagConstraints2.gridy = 1;
			jPanelSouth = new JPanel();
			jPanelSouth.setLayout(new GridBagLayout());
			jPanelSouth.add(getJButtonOK(), gridBagConstraints3);
			jPanelSouth.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelSouth;
	}

	/**
	 * This method initializes jButtonOK	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText("OK");
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setPreferredSize(new Dimension(100, 26));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setActionCommand("OK");
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Abbrechen");
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setPreferredSize(new Dimension(100, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setActionCommand("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	

	/**
	 * Adds a Project-Tab and a new Base Folder 
	 * (child of root!) to the ProjectWindow
	 * 
	 * @param title
	 * @param icon
	 * @param component
	 * @param tip
	 */	
	public void addOptionTab( String title, Icon icon, Component component, String tip ) {
		// --- GUI-Komponente in das TabbedPane-Objekt einfügen -------------
		component.setName( title ); 							// --- Component benennen ----
		jTabbedPaneRight.addTab( title, icon, component, tip);	// --- Component anhängen ----
		// --- Neuen Basisknoten einfügen ------------------
		addOptionTabNode(title);
	}

	/**
	 * Adds a new node to the left Project-Tree
	 * @param newNode
	 */
	public void addOptionTabNode( String newNode ) {
		RootNode.add( new DefaultMutableTreeNode( newNode ) );
	}
	
	/**
	 * Adds a child-node to a given parent node of the left Project-Tree.
	 * If the node can not be found, the methode adds the textual node-definition
	 * to the local TreeMap 'additionalNodes', for a later addition to the Tree
	 * @param parentNode
	 * @param newNode
	 */
	public void addOptionTabNode( String parentNodeName, String newNodeName ) {
		DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode( newNodeName );
		DefaultMutableTreeNode parentNode  = getTreeNode(parentNodeName); 
		if (parentNode!=null) {
			parentNode.add( currentNode );			
		} else {
			String[] newNodeDef = new String[2];
			newNodeDef[0] = parentNodeName;
			newNodeDef[1] = newNodeName;
			additionalNodes.put(additionalNodes.size()+1, newNodeDef);
		}
	}
	
	/**
	 * Returns the Tree-Node requested by the Reference 
	 * @param Reference
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getTreeNode(String Reference) {
		
		DefaultMutableTreeNode nodeFound = null;
		DefaultMutableTreeNode currNode = null;
		String currNodeText;
		
		for (Enumeration<DefaultMutableTreeNode> e = RootNode.breadthFirstEnumeration(); e.hasMoreElements();) {
			currNode = e.nextElement();
			currNodeText = currNode.getUserObject().toString(); 
			if ( currNodeText.equals(Reference) ) {				
				nodeFound = currNode;
				break;
			} 
		}
		return nodeFound;
	}
	
	/**
	 * Setzt den Fokus auf eine bestimmte Karteikarte
	 * @param title
	 */
	public void setFocusOnTab (String title) {
		for (int i=0; i<jTabbedPaneRight.getComponentCount();  i++ ) {
			Component Comp = jTabbedPaneRight.getComponent(i);
			if ( Comp.getName().equalsIgnoreCase( Language.translate(title) ) ) {
				jTabbedPaneRight.setSelectedIndex(i);		
			}
		}	
	}

	
	// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void OptionTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;

    	OptionTreeExpand( new TreePath(RootNode), expand, CurrNodeLevel, Up2TreeLevel);
    }
	private void OptionTreeExpand( TreePath parent, boolean expand, Integer CurrNodeLevel, Integer Up2TreeLevel) {
    
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (CurrNodeLevel >= Up2TreeLevel) {
        	return;
        }
        if (node.getChildCount() >= 0) {
            for ( @SuppressWarnings("rawtypes") Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                OptionTreeExpand(path, expand, CurrNodeLevel+1, Up2TreeLevel);
            }
        }    
        // Expansion or collapse must be done bottom-up
        if (expand) {
        	jTreeOptions.expandPath(parent);
        } else {
        	jTreeOptions.collapsePath(parent);
        }
    }
	
	/**
	 * Handles the ActionEvents of this dialog
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		if (actCMD.equalsIgnoreCase("OK")) {
			this.doOkAction();
		} else if (actCMD.equalsIgnoreCase("Cancel")) {
			this.canceled = true;
			this.setVisible(false);
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
		
	}
	
	/**
	 * Doe's the Actions when using the OK-Button -------------------
	 */
	private void doOkAction() {
		
		boolean isServerOld = Application.RunInfo.isRunAsServer();
		boolean isServerNew = optionsStart.jRadioButtonRunAsServer.isSelected();
		
		String newLine = Application.RunInfo.AppNewLineString();
		String forceRestartTo = null;
		
		// --- Fehlerbehnaldung -------------------------------------
		if ( errorFound() == true) {
			return;
		}
		// --- If a change from 'Application' to 'Server' occures --- 
		if ( isServerNew != isServerOld ) {
			if (isServerNew==true) {
				forceRestartTo = Language.translate("Server");
			}
			else {
				forceRestartTo = Language.translate("Anwendung");
			}
			
			// --------------------------------------------------------------
			// --- Neustart der Anwendung einleiten, weil von Server --------
			// --- auf Application umgestellt wurde oder umgekehrt ----------
			// --- Wenn der User das möchte !! ------------------------------
			// --------------------------------------------------------------
			String MsgHead = "";
			String MsgText = "";
			
			MsgHead += Language.translate("Agent.GUI umschalten ?");
			MsgText += Language.translate("Progamm umschalten auf") + " '" + forceRestartTo + "':" + newLine; 	
			MsgText += Language.translate("Möchten Sie Agent.GUI nun umschalten und neu starten ?");

			Integer MsgAnswer = JOptionPane.showInternalConfirmDialog( this.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == JOptionPane.YES_OPTION ) {
				forceRestart = true;			
			} else {
				forceRestart = false;
				if (optionsStart.jRadioButtonRunAsServer.isSelected() ) {
					optionsStart.jRadioButtonRunAsApplication.setSelected(true);
				} else {
					optionsStart.jRadioButtonRunAsServer.setSelected(true);
				}
				MsgHead = Language.translate("Umschaltung rückgängig gemacht!");
				MsgText =  Language.translate("Ihre Umschaltung zwischen 'Anwendung' und 'Server' wurde rückgängig gemacht.") + newLine;
				MsgText += Language.translate("Bitte wiederholen Sie den Vorgang bei Bedarf und bestätigen Sie dann mit 'Ja'.");
				JOptionPane.showInternalMessageDialog(this.getContentPane(), MsgText, MsgHead, JOptionPane.OK_OPTION);
				this.canceled = true;
				this.setVisible(false);
				return;
			}
			// --------------------------------------------------------------
		}
		this.setFromData2Global();
		this.canceled = false;
		this.setVisible(false);
		Application.Properties.save();
		
	}
	
	/**
	 * Returns, if this Dialog was canceled
	 * @return boolean 
	 */
	public boolean isCanceled(){
		return canceled;
	}
	/**
	 * Returns, if the Application should be restarted
	 * @return
	 */
	public boolean isForceRestart() {
		return forceRestart;
	}
	
	/**
	 * This method sets the Data from the global Area to the Form
	 */
	private void setGlobalData2Form(){
		
		// --- Panel "Programstart" (optionsStart) ------------------
		if (global.isRunAsServer()== true) {
			optionsStart.jRadioButtonRunAsServer.setSelected(true);
			optionsStart.jRadioButtonRunAsApplication.setSelected(false);
		} else {
			optionsStart.jRadioButtonRunAsServer.setSelected(false);
			optionsStart.jRadioButtonRunAsApplication.setSelected(true);
		}
		if (global.isServerAutoRun()==  true) {
			optionsStart.jCheckBoxAutoStart.setSelected(true);	
		} else {
			optionsStart.jCheckBoxAutoStart.setSelected(false);
		}
		optionsStart.jTextFieldMasterURL.setText(global.getServerMasterURL());
		optionsStart.jTextFieldMasterPort.setText(global.getServerMasterPort().toString());
		optionsStart.jTextFieldMasterPort4MTP.setText(global.getServerMasterPort4MTP().toString());
		
		optionsStart.jTextFieldDBHost.setText(global.getServerMasterDBHost());
		optionsStart.jTextFieldDB.setText(global.getServerMasterDBName());
		optionsStart.jTextFieldDBUser.setText(global.getServerMasterDBUser());
		optionsStart.jTextFieldDBPswd.setText(global.getServerMasterDBPswd());
		
		optionsStart.refreshView();
	}
	
	/**
	 * This method writes the data back from the form to the global area 
	 */
	private void setFromData2Global() {
		
		// --- Panel "Programstart" (optionsStart) ------------------
		global.setRunAsServer( optionsStart.jRadioButtonRunAsServer.isSelected() );
		global.setServerAutoRun( optionsStart.jCheckBoxAutoStart.isSelected() );
		global.setServerMasterURL( optionsStart.jTextFieldMasterURL.getText().trim() );
		
		Integer usePort = Integer.parseInt( optionsStart.jTextFieldMasterPort.getText().trim() );
		global.setServerMasterPort( usePort );
		Integer usePort4MTP = Integer.parseInt( optionsStart.jTextFieldMasterPort4MTP.getText().trim() );
		global.setServerMasterPort4MTP(usePort4MTP);
		
		global.setServerMasterDBHost( optionsStart.jTextFieldDBHost.getText().trim() );
		global.setServerMasterDBName( optionsStart.jTextFieldDB.getText().trim() );
		global.setServerMasterDBUser( optionsStart.jTextFieldDBUser.getText().trim() );
		global.setServerMasterDBPswd( optionsStart.jTextFieldDBPswd.getText().trim() );
		
	}
	
	/**
	 * This method doe's the Error-Handling for this Dialog 
	 * @return true or false
	 */
	private boolean errorFound() {
		
		String MsgHead = null;
		String MsgText = null;
		boolean err = false;
		
		String  testURL = optionsStart.jTextFieldMasterURL.getText().trim();
		String  testPortAsString  = optionsStart.jTextFieldMasterPort.getText().trim();
		Integer testPortAsInteger = Integer.parseInt( testPortAsString );
				
		String  testPort4MTPAsString  = optionsStart.jTextFieldMasterPort4MTP.getText().trim();
		Integer testPort4MTPAsInteger = Integer.parseInt( testPort4MTPAsString );

		// --- Testing URL and Port ---------------------------------
		if ( testURL != null ) {
			if ( testURL.equalsIgnoreCase("")==false  ) {
				// --- Testing the URL ----------------------------------
				if ( testURL.contains(" ") ) {
					MsgHead = Language.translate("Fehler: URL oder IP !");
					MsgText = Language.translate("Die URL oder IP enthält unzulässige Zeichen!");	
					JOptionPane.showInternalMessageDialog( this.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
					return true;
				}
				// --- Testing the Port ---------------------------------
				if ( testPortAsInteger.equals(0) ) {
					MsgHead = Language.translate("Fehler: Port");
					MsgText = Language.translate("Der Port muss einem Wert ungleich 0 entsprechen!");	
					JOptionPane.showInternalMessageDialog( this.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
					return true;
				}
				// --- Testing the Port 4 MTP ---------------------------
				if ( testPort4MTPAsInteger.equals(0) ) {
					MsgHead = Language.translate("Fehler: Port4MTP ");
					MsgText = Language.translate("Der Port für die MTP-Adresse muss einem Wert ungleich 0 entsprechen!");	
					JOptionPane.showInternalMessageDialog( this.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
					return true;
				}
			}
		}
		return err;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="33,9"
