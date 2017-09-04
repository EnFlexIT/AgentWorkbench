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
package agentgui.envModel.graph.components;

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

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.imaging.ImageFileView;
import agentgui.core.gui.imaging.ImageFilterAll;
import agentgui.core.gui.imaging.ImagePreview;
import agentgui.core.project.Project;
import de.enflexit.common.PathHandling;

/**
 * Table cell editor for displaying the file chooser dialog for selecting
 * ImageIcon for a network component type.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class TableCellEditor4Image extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1601832987481564552L;
	
	/** The Constant EDIT. */
    protected static final String EDIT = "edit";
    
    private ComponentTypeDialog ctsDialog = null;
    /** Current project. */
    private Project project= null;

    /** Current ImageIcon of the selected Cell. */
	private ImageIcon currentImageIcon;
	/** Button to be displayed in the cell during editing. */
	private JButton button;
	/** File chooser for selecting the image. */
	private JFileChooser fileChooser;
    private JButton jButtonRemove;
    
    /**
     * Constructor.
     *
     * @param project The current project to be passed by the parent
     */
	public TableCellEditor4Image(ComponentTypeDialog ctsDialog, Project project) {
		
		this.ctsDialog = ctsDialog;
		this.project = project;
		
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
			currentImageIcon = ctsDialog.createImageIcon(null, null);
			
		} else {
			
			if (ae.getActionCommand().equals(EDIT)) {
	            
				// --- set the directory to the default project directory or the last selected folder 
	            if(Application.getGlobalInfo().getLastSelectedFolderAsString().startsWith(project.getProjectFolderFullPath())){
	            	// --- last selected folder is a sub folder of the project folder ---
	            	this.getFileChooser().setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());	           
	            } else{
	            	// --- set current directory as the project folder ------------------	
	            	this.getFileChooser().setCurrentDirectory(new File(project.getProjectFolderFullPath()));
	            }
	            
	            if (currentImageIcon!=null && this.currentImageIcon.getDescription()!=null) {
	            	// --- If file exists make it selected in the file chosser ----------
	            	String fileDesc = this.currentImageIcon.getDescription().substring(1).replace(project.getProjectFolder(), "").substring(1);
	            	String filePath = project.getProjectFolderFullPath() + fileDesc;
	            	filePath = PathHandling.getPathName4LocalOS(filePath);
	            	File testFile = new File(filePath);
	            	if (testFile.exists()==true) {
	            		this.getFileChooser().setSelectedFile(testFile);
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
	                if(filePath.startsWith(project.getProjectFolderFullPath())){
	                	// --- The image is inside project folder or its sub folder -----
	                	String path = filePath.replace(project.getProjectFolderFullPath(), "");
	                    // --- Constructing the relative resource path ------------------
	                	path = "/"+ project.getProjectFolder() + "/"  + path.replace(File.separatorChar, '/');
	                	// --- Updating the image Icon of the table cell ----------------
	                	currentImageIcon = ctsDialog.createImageIcon(path, path);
	                } else{	                
	                	// --- The image is not inside the project folder ---------------	
	                	String msg   = Language.translate("The image should be in the "+project.getProjectFolder()+" folder.", Language.EN);
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
