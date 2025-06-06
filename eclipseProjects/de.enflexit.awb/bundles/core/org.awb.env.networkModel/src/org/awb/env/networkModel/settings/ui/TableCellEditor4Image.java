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
package org.awb.env.networkModel.settings.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.enflexit.language.Language;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.common.PathHandling;
import de.enflexit.common.swing.imageFileSelection.ImageFileView;
import de.enflexit.common.swing.imageFileSelection.ImageFilterAll;
import de.enflexit.common.swing.imageFileSelection.ImagePreview;

/**
 * Table cell editor for displaying the file chooser dialog for selecting
 * ImageIcon for a network component type.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class TableCellEditor4Image extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = -1601832987481564552L;
	
    protected static final String EDIT = "edit";
    
    private ComponentTypeDialog ctsDialog;

    /** Current ImageIcon of the selected Cell. */
	private ImageIcon currentImageIcon;
	/** Button to be displayed in the cell during editing. */
	private JButton button;
	/** File chooser for selecting the image. */
	private JFileChooser fileChooser;
    private JButton jButtonRemove;

    
    /**
     * Instantiates a new table cell editor 4 image.
     * @param ctsDialog the current ComponentTypeDialog
     */
	public TableCellEditor4Image(ComponentTypeDialog ctsDialog) {
		this.ctsDialog = ctsDialog;
		// --- Creating the button --------------
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);
	}
	
	/**
	 * Value to be returned to the table after editing.
	 * @return the cell editor value
	 */
	@Override
	public Object getCellEditorValue() {
		return currentImageIcon;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	  	
		if (ae.getSource()==this.getJButtonRemove()) {
			
			this.getFileChooser().cancelSelection();
			currentImageIcon = ComponentTypeDialog.createImageIcon(null, null);
			
		} else {
			
			if (ae.getActionCommand().equals(EDIT)) {
				// --- Set the directory to the default project directory or the last selected folder 
				Project project = ctsDialog.getProject();
	            if (Application.getGlobalInfo().getLastSelectedFolderAsString().startsWith(project.getProjectFolderFullPath())) {
	            	// --- last selected folder is a sub folder of the project folder ---
	            	this.getFileChooser().setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());	           
	            } else {
	            	// --- set current directory as the project folder ------------------	
	            	this.getFileChooser().setCurrentDirectory(new File(project.getProjectFolderFullPath()));
	            }
	            
	            if (currentImageIcon!=null && this.currentImageIcon.getDescription()!=null) {
	            	// --- If file exists make it selected in the file chooser ----------
	            	String relativePath = this.currentImageIcon.getDescription();
	            	if (relativePath.startsWith("/" + project.getProjectFolder())) {
	            		relativePath = relativePath.substring(project.getProjectFolder().length()+1);
	            	}
	            	String checkFilePath = PathHandling.getPathName4LocalOS(project.getProjectFolderFullPath() + relativePath);
	            	File checkFile = new File(checkFilePath);
	            	if (checkFile.exists()==true) {
	            		this.getFileChooser().setSelectedFile(checkFile);
	            	}
	            }
	            
	            int returnVal = this.getFileChooser().showDialog(button, Language.translate("Choose Icon", Language.EN));
	            // - - Wait for the end of the dialog - - - - - - -
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	// --- Chosen a file ------------------------------------------------
		            Application.getGlobalInfo().setLastSelectedFolder(fileChooser.getCurrentDirectory());
	            
	            	File file = this.getFileChooser().getSelectedFile();	               
	                String filePath = file.getAbsolutePath();
	                
	                // --- Checking the prefix of the chosen file path ------------------
	                if (filePath.startsWith(project.getProjectFolderFullPath())) {
	                	// --- The image is inside project folder or its sub folder -----
	                	String path = filePath.replace(project.getProjectFolderFullPath(), "");
	                    // --- Constructing the relative resource path ------------------
	                	path = "/"  + path.replace(File.separatorChar, '/');
	                	// --- Updating the image Icon of the table cell ----------------
	                	currentImageIcon = ComponentTypeDialog.createImageIcon(path, path);
	                } else{	                
	                	// --- The image is not inside the project folder ---------------	
	                	String msg   = Language.translate("The image should be in the "+ project.getProjectFolder() + " folder.", Language.EN);
	                	String title = Language.translate("Warning", Language.EN);
	                	JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);	 	        			
	                }
	            } 
	            this.getFileChooser().setSelectedFile(null);
			}
            // --- Make the renderer reappear. ------------------------------------------
            fireEditingStopped(); 
        }
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		currentImageIcon = (ImageIcon) value;
		return button;
	}
	
	/**
	 * Gets the file chooser for the selection of an image.
	 * @return the file chooser
	 */
	private JFileChooser getFileChooser() {
		
		if (this.fileChooser==null) {
			
			if (this.ctsDialog !=null) {
				this.ctsDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));	
			}
			
			// --- Set up the dialog that the button brings up. -----
	        fileChooser = new JFileChooser();
	        fileChooser.addChoosableFileFilter(new ImageFilterAll());
	        fileChooser.setAcceptAllFileFilterUsed(false);

		    // --- Add custom icons for file types. -----------------
	        fileChooser.setFileView(new ImageFileView());

		    // --- Add the preview pane. ----------------------------
	        fileChooser.setAccessory(new ImagePreview(fileChooser));
	        
	        // --- Add a button in order to remove current image ----
	        JPanel panel3_1 = (JPanel) fileChooser.getComponent(3);
	        JPanel panel3_2 = (JPanel) panel3_1.getComponent(3);
	        panel3_2.add(this.getJButtonRemove(), 1);
	        
	        if (this.ctsDialog!=null) {
	        	this.ctsDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));	
			}
		}
		return fileChooser;
	}
	
	/**
	 * Returns the JButton remove.
	 * @return the JButton remove
	 */
	private JButton getJButtonRemove() {
		if (jButtonRemove==null) {
			jButtonRemove = new JButton();
			jButtonRemove.setText(Language.translate("Remove current image", Language.EN));
			jButtonRemove.addActionListener(this);	
		}
		return jButtonRemove;
	}
	
}
