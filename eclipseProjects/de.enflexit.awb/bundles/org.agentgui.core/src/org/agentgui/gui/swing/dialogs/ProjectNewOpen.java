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
package org.agentgui.gui.swing.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.agentgui.gui.AwbProjectNewOpenDialog;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

/**
 * This GUI is used in order to create, open or delete a project.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectNewOpen extends JDialog implements AwbProjectNewOpenDialog, ActionListener {
	
	private static final long serialVersionUID = 4979849463130057295L;

	private static String newLine = Application.getGlobalInfo().getNewLineSeparator();  
	
	private JPanel jContentPane;
	private JPanel jPanelButtons;
	private JLabel jLabel;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabelDummy;
	private JLabel jLabelLineHorizontal;
	private JTextField jTextFieldProjectName;
	private JTextField jTextFieldProjectSubDirectory;
	
	private JScrollPane jScrollTree;
	private JTree projectTree;
	private DefaultTreeModel projectTreeModel;
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode currentNode;  
	
	private JCheckBox jCheckBoxExportBefore;
	
	private JButton jButtonOK;
	private JButton jButtonCancel;
	
	private ProjectAction currDialogAction;
	private boolean Canceled = false;
	private boolean exportBeforeDelete = true;


	/**
	 * Constructor of this class.
	 *
	 * @param owner the owner
	 * @param title the title
	 * @param currentAction the current action
	 */
	public ProjectNewOpen(Frame owner, String title, ProjectAction currentAction ) {
		super(owner, title);
		this.currDialogAction = currentAction;
		
		//--- Get TreeModel -----------------------------------------
		File projectPath = new File(Application.getGlobalInfo().getPathProjects());
		this.rootNode = new DefaultMutableTreeNode("... " + projectPath.getName());
		this.projectTreeModel = new DefaultTreeModel(rootNode);	
		this.initialize();

		// --- Center Dialog ----------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	

	    switch (currentAction) {
	    case NewProject:
	    	jButtonOK.setText("OK");
	    	jTextFieldProjectName.setEnabled(true);
	    	jTextFieldProjectSubDirectory.setEnabled(true);
	    	break;
	    case OpenProject:
	    	jButtonOK.setText("Öffnen");
	    	jTextFieldProjectName.setEnabled(false);
	    	jTextFieldProjectSubDirectory.setEnabled(false);	    
	    	break;
	    case ExportProject:
	    	jButtonOK.setText("Exportieren");
	    	jTextFieldProjectName.setEnabled(false);
	    	jTextFieldProjectSubDirectory.setEnabled(false);	    
	    	break;
	    case DeleteProject:
	    	jButtonOK.setText("Löschen");
	    	jTextFieldProjectName.setEnabled(false);
	    	jTextFieldProjectSubDirectory.setEnabled(false);	    
	    	break;
	    }
	    jButtonOK.setText(Language.translate(jButtonOK.getText()));
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		this.setSize(543, 412);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(true);
		this.setModal(true);
		this.setContentPane(getJContentPane());
		this.registerEscapeKeyStroke();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Canceled = true;
				setVisible(false);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#close()
	 */
	@Override
	public boolean close() {
		this.dispose();
		return true;
	}
	 /**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	Canceled = true;
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
			
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new Insets(0, 15, 20, 0);
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.gridwidth = 3;
			gridBagConstraints21.gridy = 6;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 3;
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.insets = new Insets(5, 15, 15, 30);
			gridBagConstraints11.weightx = 0.0;
			gridBagConstraints11.gridheight = 2;
			gridBagConstraints11.ipadx = 50;
			gridBagConstraints11.gridy = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridheight = 1;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 4;
			gridBagConstraints7.ipadx = 0;
			gridBagConstraints7.ipady = 0;
			gridBagConstraints7.weightx = 0.0;
			gridBagConstraints7.weighty = 1.0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(5, 15, 15, 10);
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.ipadx = 0;
			gridBagConstraints6.ipady = 0;
			gridBagConstraints6.weightx = 0.0;
			gridBagConstraints6.insets = new Insets(5, 5, 0, 15);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridwidth = 3;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.ipadx = 0;
			gridBagConstraints5.ipady = 0;
			gridBagConstraints5.weightx = 0.0;
			gridBagConstraints5.insets = new Insets(15, 5, 0, 15);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(20, 15, 20, 15);
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.ipady = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 0.0;
			gridBagConstraints3.gridwidth = 4;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 15, 0, 12);
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 3;
			gridBagConstraints2.ipadx = 0;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 0.9;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridwidth = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(5, 15, 0, 2);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.ipadx = 33;
			gridBagConstraints1.ipady = 3;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(15, 15, 0, 2);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 38;
			gridBagConstraints.ipady = 3;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = 0;
			jLabel = new JLabel();
			jLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabel.setText("Projekttitel");
			jLabel.setText( Language.translate(jLabel.getText()) );
			
			jLabel1 = new JLabel();
			jLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabel1.setText("Verzeichnis" );
			jLabel1.setText(Language.translate(jLabel1.getText()));
			
			jLabel2 = new JLabel();
			jLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabel2.setText("Vorhandene Projektverzeichnisse" );
			jLabel2.setText(Language.translate(jLabel2.getText()));
			
			jLabelLineHorizontal = new JLabel();
			jLabelLineHorizontal.setText("");
			jLabelLineHorizontal.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
			jLabelLineHorizontal.setPreferredSize(new Dimension(20, 1));
			
			
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabel, gridBagConstraints);
			jContentPane.add(jLabel1, gridBagConstraints1);
			jContentPane.add(jLabel2, gridBagConstraints2);
			jContentPane.add(jLabelLineHorizontal, gridBagConstraints3);
			jContentPane.add(getJTextFieldProjectName(), gridBagConstraints5);
			jContentPane.add(getJTextFieldProjectSubDirectory(), gridBagConstraints6);
			jContentPane.add(getJScrollTree(), gridBagConstraints7);
			jContentPane.add(getJPanelButtons(), gridBagConstraints11);
			if (this.currDialogAction==ProjectAction.DeleteProject) {
				jContentPane.add(getJCheckBoxExportBefore(), gridBagConstraints21);	
			}
			
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jCheckBoxExportBefore	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxExportBefore() {
		if (jCheckBoxExportBefore == null) {
			jCheckBoxExportBefore = new JCheckBox();
			jCheckBoxExportBefore.setText("Vor dem Löschen exportieren!");
			jCheckBoxExportBefore.setText(Language.translate(jCheckBoxExportBefore.getText()));
			jCheckBoxExportBefore.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxExportBefore.setForeground(new Color(153, 0, 0));
			jCheckBoxExportBefore.setSelected(true);
			jCheckBoxExportBefore.setActionCommand("EXPORT");
			jCheckBoxExportBefore.addActionListener(this);
		}
		return jCheckBoxExportBefore;
	}
	
	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 */
	private JTextField getJTextFieldProjectName() {
		if (jTextFieldProjectName == null) {
			jTextFieldProjectName = new JTextField();
			jTextFieldProjectName.setName("ProjectTitel");
			jTextFieldProjectName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldProjectName.setPreferredSize(new Dimension(50, 26));
			jTextFieldProjectName.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					this.setSuggestedProjectSubDirectory();
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					this.setSuggestedProjectSubDirectory();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					this.setSuggestedProjectSubDirectory();
				}
				private void setSuggestedProjectSubDirectory() {
					
					String projectName = ProjectNewOpen.this.getJTextFieldProjectName().getText();
					if (projectName==null) {
						projectName = "";
					} else {
						// --- Remove unwanted characters -----------
						projectName = projectName.toLowerCase();
						projectName = projectName.replaceAll("  ", " ");
						projectName = projectName.replace(" ", "-");
						projectName = projectName.replace("ä", "ae");
						projectName = projectName.replace("ö", "oe");
						projectName = projectName.replace("ü", "ue");
					}
					
					// --- Check all characters ---------------------
					String regExp = "[a-z;_;-]";
					String suggestion = "";
					for (int i = 0; i < projectName.length(); i++) {
						String singleChar = "" + projectName.charAt(i);
						if (singleChar.matches(regExp) == true) {
							suggestion = suggestion + singleChar;	
						}						
				    }
					suggestion = suggestion.replaceAll("--", "-");
					
					// --- Set to maximal length --------------------
					if (suggestion.length()>20) {
						suggestion = suggestion.substring(0, 20);
					}
					ProjectNewOpen.this.getJTextFieldProjectSubDirectory().setText(suggestion);
				}
				
			});
		}
		return jTextFieldProjectName;
	}
	
	/**
	 * This method initializes jTextField.
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldProjectSubDirectory() {
		if (jTextFieldProjectSubDirectory == null) {
			jTextFieldProjectSubDirectory = new JTextField();
			jTextFieldProjectSubDirectory.setName("ProjectFolder");
			jTextFieldProjectSubDirectory.setFont(new Font("Dialog", Font.PLAIN, 12));		
			jTextFieldProjectSubDirectory.setPreferredSize(new Dimension(50, 26));
			jTextFieldProjectSubDirectory.addActionListener(this);
		}
		return jTextFieldProjectSubDirectory;
	}

	/**
	 * This method initializes jScrollTree.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollTree() {
		if (jScrollTree == null) {
			jScrollTree = new JScrollPane();
			jScrollTree.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollTree.setViewportView(getProTree());
		}
		return jScrollTree;
	}
	
	/**
	 * This method initializes ProTree.
	 * @return javax.swing.JTextArea
	 */
	private JTree getProTree() {
		if (projectTree == null) {
			projectTree = new JTree( projectTreeModel );
			projectTree.setName("ProTree");
			projectTree.setBounds(new Rectangle(16, 135, 258, 179));			
			projectTree.setShowsRootHandles(false);
			projectTree.setRootVisible(true);
			projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			// --- Add Node-Object ----------------------------------
			String[] ideProjects = Application.getGlobalInfo().getProjectSubDirectories();
			if (ideProjects!=null) {
				for (String projectFolder : ideProjects) {
					currentNode = new DefaultMutableTreeNode( projectFolder );
					rootNode.add(currentNode);
				}
			}
			TreePath TreePathRoot = new TreePath(rootNode);
			projectTree.expandPath( TreePathRoot );	
			
			// --- In case that a project has to be opened - START --
			if (currDialogAction!=ProjectAction.NewProject) {
				projectTree.addTreeSelectionListener(new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent ts) {
						
						TreePath PathSelected = ts.getPath();
						Integer PathLevel = PathSelected.getPathCount();
						String EndPath = ts.getPath().getLastPathComponent().toString();
						
						if (PathLevel == 2) {
							setProjectDirectory( EndPath );
						}
						else {
							setProjectDirectory(null);
						}		
						
					}
				});
				projectTree.addMouseListener( new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (me.getClickCount() == 2 ) {
							jButtonOK.doClick();	
						}
					}
				});
			};
			// --- In case that a project has to be opened - END ----
			
		}
		return projectTree;
	}

	/**
	 * This method initializes jPanelButtons	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints10.weighty = 0.1;
			gridBagConstraints10.gridy = 3;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.gridy = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints8.gridy = 0;

			jLabelDummy = new JLabel();
			jLabelDummy.setText("");

			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonOK(), gridBagConstraints8);
			jPanelButtons.add(getJButtonCancel(), gridBagConstraints9);
			jPanelButtons.add(jLabelDummy, gridBagConstraints10);
		}
		return jPanelButtons;
	}
	
	/**
	 * This method initializes jButtonOK.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText("OK");
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setActionCommand("OK");
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Abbruch");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText()));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setActionCommand("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled(){
		return Canceled;
	}
	
	/**
	 * @param exportBeforeDelete the exportBeforeDelete to set
	 */
	public void setExportBeforeDelete(boolean exportBeforeDelete) {
		this.exportBeforeDelete = exportBeforeDelete;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#isExportBeforeDelete()
	 */
	@Override
	public boolean isExportBeforeDelete() {
		return exportBeforeDelete;
	}

	/**
	 * Sets the project name.
	 * @param text the new project name
	 */
	public void setProjectName(String text) {
		jTextFieldProjectName.setText(text);
	}
	/**
	 * Returns the project name.
	 * @return the project name
	 */
	public String getProjectName() {
		return jTextFieldProjectName.getText();
	}
	
	/**
	 * Sets the var project folder.
	 * @param text the new var project folder
	 */
	public void setProjectDirectory(String text) {
		jTextFieldProjectSubDirectory.setText(text);
	}
	/**
	 * Gets the var project folder.
	 * @return the var project folder
	 */
	public String getProjectDirectory() {
		return jTextFieldProjectSubDirectory.getText();
	}
	
	/**
	 * Sets the ok button text.
	 * @param newText the new ok button text
	 */
	public void setOkButtonText(String newText) {
		jButtonOK.setText(newText);
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent AC) {

		Object action = AC.getSource();
		if ( action == jButtonOK ) {
			if (isProjectError() == true) return;
			Canceled = false;
			setVisible(false);
		} else if ( action == jButtonCancel ) {
			Canceled = true;
			setVisible(false);
		} else if ( action == jTextFieldProjectName ) {
			// --- Do Nothing yet
		} else if ( action == jTextFieldProjectSubDirectory ) {
			jButtonOK.doClick();
		} else if ( action == jCheckBoxExportBefore) {
			this.setExportBeforeDelete(jCheckBoxExportBefore.isSelected());
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + action);
		}		
	}

	/**
	 * Check for project errors.
	 * @return true or false
	 */
	public boolean isProjectError () {

		String projectName = getProjectName();
		String projectFolder = getProjectDirectory();
		
		boolean projectError = false;
		String projectErrorSource = null;
		String msgTitle = null;
		String msgText = null;
		
		if (currDialogAction==ProjectAction.NewProject) {
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// +++ Create new Project +++++++++++++++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// ----------------------------------------------------
			// --- Get project name -------------------------------		
			if ( projectError==false && projectName == null ) {
				projectErrorSource = "ProName";
				projectError = true;
			} else if ( projectError==false && projectName.length() == 0 ) {
				projectErrorSource = "ProName";
				projectError = true;
			}
			// ----------------------------------------------------
			// --- Check project directory ------------------------		
			if ( projectError==false && projectFolder == null ) {
				projectErrorSource = "ProFolder";
				projectError = true;
			} else if ( projectError==false && projectFolder.length() == 0 ) {
				projectErrorSource = "ProFolder";
				projectError = true;
			}
			// ----------------------------------------------------
			// --- Configure / correct directory name -------------
			projectFolder = projectFolder.trim();
			projectFolder = projectFolder.toLowerCase();
			projectFolder = projectFolder.replaceAll(" ", "_");
			while ( projectFolder.contains( "__" ) ) {
				projectFolder = projectFolder.replaceAll("__", "_");	
			}
			setProjectDirectory(projectFolder);		
			// ----------------------------------------------------
			// --- Check for regular expression -------------------
			String regExpression = "[a-z_-]{3,}";
			if (projectError==false && projectFolder.matches(regExpression)==false) {
				projectErrorSource = "ProFolderRegEx";
				projectError = true;
			}
			// ----------------------------------------------------
			// --- Does the project directory already exists? -----
			if (projectError==false) {
				String[] IDEProjects = Application.getGlobalInfo().getProjectSubDirectories();
				if (IDEProjects!=null) {
					for ( String Pro : IDEProjects ) {
						if ( Pro.equalsIgnoreCase(projectFolder) ) {
							projectErrorSource = "ProFolderDouble";
							projectError = true;	
							break;
						}			
					}
				}
			}
			// ----------------------------------------------------
			// --- Test: Basis-Verzeichnis anlegen ----------------
			if (projectError==false) {
				String NewDirName = Application.getGlobalInfo().getPathProjects() + projectFolder;
				File f = new File(NewDirName);
				if (f.isDirectory()) {
					projectErrorSource = "ProFolderDouble";
					projectError = true;
					
				} else {
					if ( f.mkdir() == false ) {
						projectErrorSource = "ProFolderCreate";
						projectError = true;	
					}
				}
			}		
			
			// ----------------------------------------------------
			// --- Show Error-Msg, if an error occurs -------------
			if (projectError==true) {
				
				if (projectErrorSource.equals("ProName")) {
					msgTitle = Language.translate("Fehler - Projektname !");
					msgText = Language.translate(
								 "Bitte geben Sie einen Projektnamen an!" + newLine + newLine + 
								 "Zulässig sind beliebige Zeichen " + newLine +
								 "sowie Leerzeichen.");
					
				} else if (projectErrorSource.equals("ProFolder")) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !");
					msgText = Language.translate(
								 "Bitte geben Sie ein korrektes Projektverzeichnis an!" + newLine + newLine + 
								 "Zulässig sind beliebige Zeichen (Kleinbuchstaben), die den " + newLine +
								 "Konventionen für Verzeichnisse in Ihrem Betriebssystems" + newLine +
								 "entsprechen. " + newLine + newLine +
								 "Leerzeichen sind nicht zulässig!");
					
				} else if (projectErrorSource.equals("ProFolderRegEx")) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !");
					msgText = Language.translate(
							 "Der gewählte Bezeichner für das Projektverzeichnis enthält" + newLine +  
							 "unzulässige oder zu wenige Zeichen! " + newLine + newLine +
							 "Es werden min. 3 bis max. 20 Zeichen benötigt:" + newLine +
							 "Erlaubt sind nur Kleinbuchstaben. Umlaute und" + newLine +
							 "Leerzeichen sind nicht zulässig (verwenden Sie " + newLine +
							 "stattdessen _ 'Unterstrich').");
					
				} else if (projectErrorSource.equals("ProFolderDouble")) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !");
					msgText = Language.translate(
							 "Das von Ihnen gewählte Projektverzeichnis wird bereits verwendet!" + newLine + newLine + 
							 "Bitte wählen Sie einen anderen Namen für Ihr Verzeichnis");
					
				} else if (projectErrorSource.equals("ProFolderCreate")) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !");
					msgText = Language.translate("Das Verzeichnis konnte nicht aangelegt werden !");
					
				}
				JOptionPane.showMessageDialog( this.getContentPane(), msgText, msgTitle, JOptionPane.ERROR_MESSAGE);
			}
			
		} else {
			
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// +++ Open an existing Project +++++++++++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// --- Check project directory --------------------------
			if ( projectError==false && (projectFolder == null || projectFolder == "" || projectFolder.length() == 0) ) {
				projectErrorSource = "ProFolder";
				projectError = true;
			}
			// --- Check project file -------------------------------
			if ( projectError==false ) {		
				String xmlLFileName = Application.getGlobalInfo().getPathProjects() + projectFolder + File.separator + Application.getGlobalInfo().getFileNameProject();
				File f = new File(xmlLFileName);
				if (f.isFile()==false) {
					projectErrorSource = "ProFolderAgentGUIxml";
					projectError = true;
				}
			}
			
			// ----------------------------------------------------
			// --- Show Error-Msg, if an error occurs -------------
			if (projectError==true) {
				if (projectErrorSource.equals("ProFolder")) {
					msgTitle = Language.translate("Fehler - Projektauswahl !");
					msgText = Language.translate("Bitte wählen Sie das gewünschte Projekt aus!");			
				
				} else if (projectErrorSource.equals("ProFolderAgentGUIxml")) {
					msgTitle = Language.translate("Fehler - '@'");
					msgText = Language.translate("Die Datei '@' wurde nicht gefunden!");	
					msgTitle = msgTitle.replace("@", Application.getGlobalInfo().getFileNameProject() );
					msgText = msgText.replace("@", Application.getGlobalInfo().getFileNameProject() );					
				}				
				JOptionPane.showMessageDialog(this, msgText, msgTitle, JOptionPane.ERROR_MESSAGE);
			}
		}

		return projectError;
	}
	
}  
