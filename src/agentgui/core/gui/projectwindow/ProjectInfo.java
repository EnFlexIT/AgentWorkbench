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
package agentgui.core.gui.projectwindow;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Language;
import agentgui.core.project.Project;

/**
 * Represents the JPanel/Tab 'Info' for the general project information
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectInfo extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;

	private Project currProject;

	private JLabel jLableTitleProject = null;
	private JLabel jLableDescription = null;
	private JLabel jLableFolder = null;
	
	private JTextField projectName = null;
	private JScrollPane jScrollPane = null;
	private JTextArea jTextAreaProjectDescription = null;
	private DocumentListener projectNameDocumentListener;  //  @jve:decl-index=0:
	private JTextField jTextFieldProjectFolder = null;
	
	/**
	 * This is the default constructor
	 */
	public ProjectInfo( Project CP ) {
		super();
		this.currProject = CP;
		this.currProject.addObserver(this);		
		initialize();
		
		// --- Sprachspezifische Einstellungen -----------------
		jLableTitleProject.setText( Language.translate("Projekttitel") );
		jLableDescription.setText( Language.translate("Beschreibung") );
		jLableFolder.setText( Language.translate("Verzeichnis") );
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.insets = new Insets(0, 10, 10, 5);
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.gridy = 2;
		jLableFolder = new JLabel();
		jLableFolder.setText("JLabel");
		jLableFolder.setFont(new Font("Dialog", Font.BOLD, 14));
		jLableFolder.setText( Language.translate("Beschreibung") );
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints4.insets = new Insets(2, 10, 0, 5);
		gridBagConstraints4.gridy = 1;
		jLableDescription = new JLabel();
		jLableDescription.setText("JLabel");
		jLableDescription.setFont(new Font("Dialog", Font.BOLD, 14));
		jLableDescription.setText( Language.translate("Beschreibung") );		
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.insets = new Insets(0, 0, 10, 10);
		gridBagConstraints3.gridx = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.insets = new Insets(0, 0, 10, 10);
		gridBagConstraints2.gridx = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(0, 10, 0, 5);
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 0;
		jLableTitleProject = new JLabel();
		jLableTitleProject.setText("JLabel");
		jLableTitleProject.setFont(new Font("Dialog", Font.BOLD, 14));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.insets = new Insets(10, 0, 10, 10);
		gridBagConstraints.weightx = 1.0;
		this.setSize(557, 329);
		this.setLayout(new GridBagLayout());
		this.add(getProjectName(), gridBagConstraints);
		this.add(jLableTitleProject, gridBagConstraints1);
		this.add(getJScrollPane(), gridBagConstraints2);
		this.add(getProjectFolder(), gridBagConstraints3);
		this.add(jLableDescription, gridBagConstraints4);
		this.add(jLableFolder, gridBagConstraints5);
	}

	/**
	 * This method initializes ProjectName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProjectName() {
		if (projectName == null) {
			projectName = new JTextField();
			projectName.setBounds(new Rectangle(140, 15, 520, 26));
			projectName.setName("ProjectTitel");
			projectName.setFont(new Font("Dialog", Font.PLAIN, 12));
			projectName.setText( currProject.getProjectName() );
			projectNameDocumentListener = new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					currProject.setProjectName( projectName.getText() );
				}
				public void insertUpdate(DocumentEvent e) {
					currProject.setProjectName( projectName.getText() );
				}
				public void changedUpdate(DocumentEvent e) {
					currProject.setProjectName( projectName.getText() );
				}
			};
			projectName.getDocument().addDocumentListener(projectNameDocumentListener);
			
		}
		return projectName;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getProjectDescription());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes ProjectDescription	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getProjectDescription() {
		if (jTextAreaProjectDescription == null) {
			jTextAreaProjectDescription = new JTextArea();
			jTextAreaProjectDescription.setName("ProjectDescription");
			jTextAreaProjectDescription.setColumns(0);
			jTextAreaProjectDescription.setLineWrap(true);
			jTextAreaProjectDescription.setWrapStyleWord(true);
			jTextAreaProjectDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaProjectDescription.setText( currProject.getProjectDescription() );
			jTextAreaProjectDescription.setCaretPosition(0);
			jTextAreaProjectDescription.getDocument().addDocumentListener( new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					currProject.setProjectDescription( jTextAreaProjectDescription.getText() );
				}
				public void insertUpdate(DocumentEvent e) {
					currProject.setProjectDescription( jTextAreaProjectDescription.getText() );
				}
				public void changedUpdate(DocumentEvent e) {
					currProject.setProjectDescription( jTextAreaProjectDescription.getText() );
				}
			});
		}
		return jTextAreaProjectDescription;
	}

	/**
	 * This method initializes ProjectFolder	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProjectFolder() {
		if (jTextFieldProjectFolder == null) {
			jTextFieldProjectFolder = new JTextField();
			jTextFieldProjectFolder.setName("ProjectFolder");
			jTextFieldProjectFolder.setBounds(new Rectangle(140, 285, 520, 26));
			jTextFieldProjectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldProjectFolder.setEditable( false );
			jTextFieldProjectFolder.setText( currProject.getProjectFolderFullPath() );			
		}
		return jTextFieldProjectFolder;
	}
	
	@Override
	/**
	 * Get the notyfication of the ObjectModel
	 */
	public void update(Observable arg0, Object OName) {
		
		String ObjectName = OName.toString();
		if ( ObjectName.equalsIgnoreCase( Project.CHANGED_ProjectName ) ) {
			if ( projectName.isFocusOwner() == false ) {
				projectName.setText( currProject.getProjectName() );				
			}								
		}			
		else if ( ObjectName.equalsIgnoreCase( Project.CHANGED_ProjectDescription ) ) {
			if ( jTextAreaProjectDescription.isFocusOwner() == false ) {
				jTextAreaProjectDescription.setText( currProject.getProjectDescription() );
			}
		}			
		else {
			//System.out.println("Unbekannter Updatebefehl vom Observerable ...");
		};
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		//System.out.println( "ActCMD/Wert => " + ActCMD );
		//System.out.println( "Auslöser => " + Trigger );

		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == projectName ) {
			currProject.setProjectName( ae.getActionCommand() );
		}
		else if ( Trigger == jTextFieldProjectFolder ) {
			currProject.setProjectFolder( ae.getActionCommand() );
		}
		else if ( Trigger == jTextAreaProjectDescription ) {
			currProject.setProjectDescription( ae.getActionCommand() );
		}
		else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
		};
	}
	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
