/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.core.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.gui.imaging.ImageFileView;
import agentgui.core.gui.imaging.ImageFilter;
import agentgui.core.gui.imaging.ImagePreview;
import agentgui.core.gui.imaging.MissingIcon;

/**
 * Table cell editor for displaying the file chooser dialog for selecting
 * ImageIcon for a network component type.
 * @author Satyadeep - CSE - Indian Institute of Technology, Guwahati
 *
 */
public class ImageSelectorTableCellEditor extends AbstractCellEditor
implements TableCellEditor,
ActionListener {

	private static final long serialVersionUID = -1601832987481564552L;
	/**
	 * Current ImageIcon of the selected Cell
	 */
	ImageIcon currentImageIcon;
	/**
	 * Button to be displayed in the cell during editing.
	 */
	JButton button;
	/**
	 * File chooser for selecting the image
	 */
	JFileChooser fileChooser;
    protected static final String EDIT = "edit";
    /**
     * Current project
     */
    Project project= null;
    /**
	 * Constructor
	 * @param project The current project to be passed by the parent
	 */
	public ImageSelectorTableCellEditor(Project project) {
		this.project = project;
		
		//Creating the button
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);
		
		//Set up the dialog that the button brings up.
        fileChooser = new JFileChooser();
       
        //Add a custom file filter and disable the default
	    //(Accept All) file filter.
        fileChooser.addChoosableFileFilter(new ImageFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

	    //Add custom icons for file types.
        fileChooser.setFileView(new ImageFileView());

	    //Add the preview pane.
        fileChooser.setAccessory(new ImagePreview(fileChooser));       
	}
	
	/**
	 * Value to be returned to the table after editing
	 */
	@Override
	public Object getCellEditorValue() {
		return currentImageIcon;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		  	if (EDIT.equals(e.getActionCommand())) {
	            //The user has clicked the cell, so bring up the dialog.
	            button.setIcon(currentImageIcon);
	            //set the directory to the default project directory or the last selected folder 
	            if(Application.RunInfo.getLastSelectedFolderAsString().startsWith(project.getProjectFolderFullPath())){
	            //last selected folder is a sub folder of the project folder
	            	fileChooser.setCurrentDirectory(Application.RunInfo.getLastSelectedFolder());	           
	            }
	            else{
	            //set current directory as the project folder	
	            	fileChooser.setCurrentDirectory(new File(project.getProjectFolderFullPath()));
	            }
	            
	            int returnVal = fileChooser.showDialog(button, "Choose Icon");
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            //Choosen a file
		            Application.RunInfo.setLastSelectedFolder(fileChooser.getCurrentDirectory());
	            
	            	File file = fileChooser.getSelectedFile();	               
	                String filePath = file.getPath();
	                
	                //Checking the prefix of the choosen file path
	                if(filePath.startsWith(project.getProjectFolderFullPath())){
	                //The image is inside project folder or its subfolder
	                	String path = filePath.replace(project.getProjectFolderFullPath(), "");
	                    // Constructing the relative resource path
	                	path = "/" + project.getProjectFolder() +"/"+ path.replace(File.separatorChar, '/');
	                	//System.out.println(path);
	                	
	                	//Updating the image Icon of the table cell
	                	currentImageIcon = createImageIcon(path, path);
	                }
	                else{	                
	                //The image is not inside the project folder	
	                	JOptionPane.showMessageDialog(null,Language.translate("The image should be in the "+project.getProjectFolder()+" folder.", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 	        			
	                }
	            } 
	            fireEditingStopped(); //Make the renderer reappear.

	        } 
		  	else { //User pressed dialog's "OK" button.
		  		//System.out.println("choose button pressed");		  	
	        }
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		currentImageIcon = (ImageIcon) value;
		return button;
	}
	/** 
	 * Returns an ImageIcon, or a default MissingIcon(a red X) if image not found.
	 * @param path 
	 * @param description
	 * @return ImageIcon  
	 */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
		if(path!=null ){			
		    java.net.URL imgURL = getClass().getResource(path);
		    if (imgURL != null) {
		        return new ImageIcon(imgURL, description);
		    } else {
		        System.err.println("Couldn't find file: " + path);
		        return (new MissingIcon(description));
		    }
		}
		else{
		    return (new MissingIcon(description));		    
		}
			
	}
}
