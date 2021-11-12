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
package org.agentgui.gui.swt.dialogs;

import java.io.File;

import org.agentgui.gui.AwbProjectNewOpenDialog;
import org.agentgui.gui.swt.SWTResourceManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;


/**
 * The Class AwbProjectNewOpenDialog is used to create, open or delete a projects.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectNewOpen extends Dialog implements AwbProjectNewOpenDialog {
	
	private final File projectsRootDir = new File(Application.getGlobalInfo().getPathProjects());
	
	private Shell currentShell;
	private String title = "Create / Open / Delete Project";
	private ProjectAction currentAction;
	
	private boolean isCancled;
	private boolean isExportBeforeDelete = true;
	
	private String projectName;
	private String projectDirectory;
	
	private Label labelProjectName;
	private Text textProjectName;
	
	private Label labelProjectDirectory;
	private Text textProjectDirectory;
	
	private Label seperatorHorizontal;

	private Label labelProjectsAvailable;
	private TreeViewer treeViewer;
	private ITreeContentProvider treeViewerContentProvider;
	private ILabelProvider treeViewerLabelProvider;
	private ISelectionChangedListener treeViewerISelectionChangedListener;
	
	private ModifyListener modifyListenerTextProjectName;
	private Button buttonExportBeforeDelete;
	
	
	/**
	 * Instantiates a new project new open dialog.
	 *
	 * @param parentShell the parent shell
	 * @param titel the title of this dialog
	 * @param currentAction the current {@link ProjectAction}
	 */
	public ProjectNewOpen(Shell parentShell, String title, ProjectAction currentAction) {
		super(parentShell);
		this.title = title;
		this.currentAction = currentAction;
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        this.currentShell = newShell;
        this.currentShell.setText(this.title);
        this.currentShell.getDisplay().addFilter(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
					event.doit = false;
					cancelPressed();
					break;

				default:
					break;
				}
			}
		});
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
     */
    @Override
    protected Point getInitialSize() {
        return new Point(500, 400);
    }
    
    /* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isSetVisible) {
		if (isSetVisible==true) {
			this.open();
		} else {
			this.close();
		}
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
    protected Control createDialogArea(Composite parent) {
        
		GridLayout gl_container = new GridLayout(2, false);
        gl_container.marginTop = 5;
        gl_container.marginLeft = 5;
        gl_container.marginRight = 5;
        gl_container.marginBottom = 0;
        gl_container.horizontalSpacing = 5;
        gl_container.verticalSpacing = 10;
        
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(gl_container);
        
        
        this.labelProjectName = new Label(container, SWT.NONE);
        this.labelProjectName.setText("Project Name");
        this.labelProjectName.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
        
        this.textProjectName = new Text(container, SWT.BORDER);
        if (this.projectName!=null) this.textProjectName.setText(this.projectName);
        this.textProjectName.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
        this.textProjectName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.textProjectName.addModifyListener(this.getModifyListenerTextProjectName());
        

        this.labelProjectDirectory = new Label(container, SWT.NONE);
        this.labelProjectDirectory.setText("Project Directory");
        this.labelProjectDirectory.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
        
        this.textProjectDirectory = new Text(container, SWT.BORDER);
        if (this.projectDirectory!=null) this.textProjectDirectory.setText(this.projectDirectory);
        this.textProjectDirectory.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
        this.textProjectDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        
        this.seperatorHorizontal = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_seperatorHorizontal = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
        gd_seperatorHorizontal.verticalIndent = 5;
        this.seperatorHorizontal.setLayoutData(gd_seperatorHorizontal);
        
        
        this.labelProjectsAvailable = new Label(container, SWT.NONE);
        this.labelProjectsAvailable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        this.labelProjectsAvailable.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
        this.labelProjectsAvailable.setText("Available Projects");
        
        this.treeViewer = new TreeViewer(container, SWT.BORDER);
        this.treeViewer.getTree().setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
        this.treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        this.treeViewer.setContentProvider(this.getTreeViewerContentProvider());
        this.treeViewer.setLabelProvider(this.getTreeViewerLabelProvider());
        this.treeViewer.addSelectionChangedListener(this.getTreeViewerISelectionChangedListener());
        this.treeViewer.setInput("root");
        this.treeViewer.expandAll();

        
        if (this.currentAction==ProjectAction.DeleteProject) {
        	this.buttonExportBeforeDelete = new Button(container, SWT.CHECK);
            this.buttonExportBeforeDelete.setForeground(org.agentgui.gui.swt.SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
            this.buttonExportBeforeDelete.setFont(org.agentgui.gui.swt.SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
            this.buttonExportBeforeDelete.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
            this.buttonExportBeforeDelete.setText("Export project before deleting");
            this.buttonExportBeforeDelete.setSelection(this.isExportBeforeDelete);
            this.buttonExportBeforeDelete.addSelectionListener(new SelectionAdapter() {
            	@Override
            	public void widgetSelected(SelectionEvent event) {
            		Button btn = (Button) event.getSource();
            		ProjectNewOpen.this.setExportBeforeDelete(btn.getSelection());
            	}
    		});	
        }
        
        
        String okButtonText = "";
		switch (this.currentAction) {
		case NewProject:
			okButtonText = "OK";
			this.textProjectName.setEnabled(true);
			this.textProjectDirectory.setEnabled(true);
			break;
		case OpenProject:
			okButtonText = "Öffnen";
			this.textProjectName.setEnabled(false);
			this.textProjectDirectory.setEnabled(false);
			break;
		case ExportProject:
			okButtonText = "Exportieren";
			this.textProjectName.setEnabled(false);
			this.textProjectDirectory.setEnabled(false);
			break;
		case DeleteProject:
			okButtonText = "Löschen";
			this.textProjectName.setEnabled(false);
			this.textProjectDirectory.setEnabled(false);
			break;
		}

		okButtonText = Language.translate(okButtonText);
//		Button buttonOK = getButton(IDialogConstants.OK_ID);
//		buttonOK.setText(okButtonText);

//		Button buttonCancel = getButton(IDialogConstants.CANCEL_ID);
//		buttonCancel.setText(Language.translate("Abbruch"));		

        return container;
    }
	
	/**
	 * Returns the tree viewer content provider.
	 * @return the tree viewer content provider
	 */
	private ITreeContentProvider getTreeViewerContentProvider() {
		if (treeViewerContentProvider==null) {
			treeViewerContentProvider = new ITreeContentProvider() {
				@Override
				public boolean hasChildren(Object element) {
					File elementFile = (File) element;
					if (elementFile!=projectsRootDir) return false;
					File[] childrenFiles = elementFile.listFiles();
					if (childrenFiles==null || childrenFiles.length==0) {
						return false;
					}
					return true;
				}
				@Override
				public Object getParent(Object element) {
					File elementFile = (File) element;
					return elementFile.getParentFile();
				}
				
				@Override
				public Object[] getElements(Object inputElement) {
					return new Object[] {projectsRootDir};
				}
				
				@Override
				public Object[] getChildren(Object parentElement) {
					File parentFile = (File) parentElement;
					return parentFile.listFiles();
				}
			};
		}
		return treeViewerContentProvider;
	}
	
	
	/**
	 * Returns the tree viewer label provider.
	 * @return the tree viewer label provider
	 */
	private ILabelProvider getTreeViewerLabelProvider() {
		if (treeViewerLabelProvider==null) {
			treeViewerLabelProvider = new ILabelProvider() {
				
				@Override
				public void removeListener(ILabelProviderListener listener) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public boolean isLabelProperty(Object element, String property) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void dispose() {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void addListener(ILabelProviderListener listener) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public String getText(Object element) {
					File elementFile = (File) element;
					return elementFile.getName();
				}
				
				@Override
				public Image getImage(Object element) {
					Image image = null;
					File elementFile = (File) element;
					if (elementFile==projectsRootDir) {
						image = GlobalInfo.getInternalSWTImage("MBopen.png");
					} else {
						image = GlobalInfo.getInternalSWTImage("AgentGUIGreen32.png");
					}
					return image;
				}
			};
		}
		return treeViewerLabelProvider;
	}
	/**
	 * Return the tree viewer I selection changed listener.
	 * @return the tree viewer I selection changed listener
	 */
	public ISelectionChangedListener getTreeViewerISelectionChangedListener() {
		if (treeViewerISelectionChangedListener==null) {
			treeViewerISelectionChangedListener = new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					textProjectDirectory.setText("");
					if (event.getSelection().isEmpty()==false && event.getSelection() instanceof TreeSelection) {
						TreeSelection selection = (TreeSelection) event.getSelection();
						Object firstSelection = selection.getFirstElement();
						if (firstSelection!=null) {
							File fileSelected = (File) firstSelection;
							if (fileSelected!=projectsRootDir) {
								textProjectDirectory.setText(fileSelected.getName());
							}
						}
					} else {
						System.err.println(ProjectNewOpen.class.getSimpleName() + "Unknown TreeSelection!");
					}
				}
			};
		}
		return treeViewerISelectionChangedListener;
	}
	
	/**
	 * Gets the modify listener text project name.
	 * @return the modify listener text project name
	 */
	private ModifyListener getModifyListenerTextProjectName() {
		if (modifyListenerTextProjectName==null) {
			modifyListenerTextProjectName = new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent me) {
					
					String suggest = textProjectName.getText();;
					// --- CleanUp ----------------------------------
					suggest = suggest.toLowerCase();
					suggest = suggest.replaceAll("  ", " ");
					suggest = suggest.replace(" ", "_");
					suggest = suggest.replace("ä", "ae");
					suggest = suggest.replace("ö", "oe");
					suggest = suggest.replace("ü", "ue");
					
					// --- Check character --------------------------
					String regExp = "[a-z;_]";
					String suggestNew = "";
					for (int i = 0; i < suggest.length(); i++) {
						String singleChar = "" + suggest.charAt(i);
						if (singleChar.matches(regExp)==true) {
							suggestNew = suggestNew + singleChar;	
						}						
				    }
					suggest = suggestNew;
					suggest = suggest.replaceAll("__", "_");
					
					// --- Set to maximal length --------------------
					if (suggest.length()>20) {
						suggest = suggest.substring(0, 20);
					}
					textProjectDirectory.setText(suggest);
				}
			};
		}
		return modifyListenerTextProjectName;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	@Override
	protected void cancelPressed() {
		this.isCancled = true;
		super.cancelPressed();
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return this.isCancled;
	}
	
	/**
	 * Sets the export before delete.
	 * @param isExportBeforeDelete the new export before delete
	 */
	private void setExportBeforeDelete(boolean isExportBeforeDelete) {
		this.isExportBeforeDelete = isExportBeforeDelete;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#isExportBeforeDelete()
	 */
	@Override
	public boolean isExportBeforeDelete() {
		return this.isExportBeforeDelete;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		this.isCancled = false;
		this.setFormToLocalVariables();
		if (this.isProjectError()==true) return;
		super.okPressed();
	}
	/**
	 * Sets the form values to the local variables.
	 */
	private void setFormToLocalVariables() {
		// --- Set value of project name --------
		if (textProjectName.getText()==null || textProjectName.getText().isEmpty()) {
			this.setProjectName(null);
		} else {
			this.setProjectName(textProjectName.getText());
		}
		// --- Set value of project directory ---
		if (textProjectDirectory.getText()==null || textProjectDirectory.getText().isEmpty()) {
			this.setProjectDirectory(null);
		} else {
			this.setProjectDirectory(textProjectDirectory.getText());
		}
	}
	
	/**
	 * Check for project errors.
	 * @return true or false
	 */
	public boolean isProjectError () {

		String newLine = Application.getGlobalInfo().getNewLineSeparator();
		String projectName = this.getProjectName();
		String projectDirectory = this.getProjectDirectory();
		
		boolean projectError = false;
		String projectErrorSource = null;
		String msgTitle = null;
		String msgText = null;
		
		if (this.currentAction==ProjectAction.NewProject) {
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// +++ Create new Project +++++++++++++++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// ----------------------------------------------------
			// --- Get project name -------------------------------		
			if (projectError==false && (projectName==null || projectName.length()==0 )) {
				projectErrorSource = "ProName";
				projectError = true;
			}
			// ----------------------------------------------------
			// --- Check project directory ------------------------		
			if (projectError==false && (projectDirectory==null || projectDirectory.length()==0) ) {
				projectErrorSource = "ProFolder";
				projectError = true;
			}

			// ----------------------------------------------------
			// --- Configure / correct directory name -------------
			if (projectDirectory!=null) {
				projectDirectory = projectDirectory.trim();
				projectDirectory = projectDirectory.toLowerCase();
				projectDirectory = projectDirectory.replaceAll(" ", "_");
				while (projectDirectory.contains( "__" )) {
					projectDirectory = projectDirectory.replaceAll("__", "_");	
				}
				this.setProjectDirectory(projectDirectory);
				this.textProjectDirectory.setText(projectDirectory);
			}
			
			// ----------------------------------------------------
			// --- Check for regular expression -------------------
			String regExp = "[a-z_]{3,}";
			if (projectError==false && projectDirectory.matches(regExp)==false) {
				projectErrorSource = "ProFolderRegEx";
				projectError = true;
			}
			// ----------------------------------------------------
			// --- Does the project directory already exists? -----
			if (projectError==false) {
				String[] IDEProjects = Application.getGlobalInfo().getProjectSubDirectories();
				if (IDEProjects!=null) {
					for ( String Pro : IDEProjects ) {
						if ( Pro.equalsIgnoreCase(projectDirectory) ) {
							projectErrorSource = "ProFolderDouble";
							projectError = true;	
							break;
						}			
					}
				}
			}
			// ----------------------------------------------------
			// --- Test: Create new project directory -------------
			if (projectError==false) {
				String newDirPath = Application.getGlobalInfo().getPathProjects() + projectDirectory;
				File newDirFile = new File(newDirPath);
				if (newDirFile.isDirectory()) {
					projectErrorSource = "ProFolderDouble";
					projectError = true;
				} else {
					if (newDirFile.mkdir()==false) {
						projectErrorSource = "ProFolderCreate";
						projectError = true;	
					}
				}
			}		
			
			// ----------------------------------------------------
			// --- Show Error-Msg, if an error occurs -------------
			if (projectError==true) {
				if ( projectErrorSource == "ProName" ) {
					msgTitle = Language.translate("Fehler - Projektname !");
					msgText = Language.translate(
								 "Bitte geben Sie einen Projektnamen an!" + newLine + newLine + 
								 "Zulässig sind beliebige Zeichen " + newLine +
								 "sowie Leerzeichen." );			
				
				} else if ( projectErrorSource == "ProFolder" ) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !");
					msgText = Language.translate(
								 "Bitte geben Sie ein korrektes Projektverzeichnis an!" + newLine + newLine + 
								 "Zulässig sind beliebige Zeichen (Kleinbuchstaben), die den " + newLine +
								 "Konventionen für Verzeichnisse in Ihrem Betriebssystems" + newLine +
								 "entsprechen. " + newLine + newLine +
								 "Leerzeichen sind nicht zulässig!" );			
				
				} else if ( projectErrorSource == "ProFolderRegEx" ) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !" );
					msgText = Language.translate(
							 "Der gewählte Bezeichner für das Projektverzeichnis enthält" + newLine +  
							 "unzulässige oder zu wenige Zeichen! " + newLine + newLine +
							 "Es werden min. 3 bis max. 20 Zeichen benötigt:" + newLine +
							 "Erlaubt sind nur Kleinbuchstaben. Umlaute und" + newLine +
							 "Leerzeichen sind nicht zulässig (verwenden Sie " + newLine +
							 "stattdessen _ 'Unterstrich').");
				
				} else if ( projectErrorSource == "ProFolderDouble" ) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !" );
					msgText = Language.translate(
							 "Das von Ihnen gewählte Projektverzeichnis wird bereits verwendet!" + newLine + newLine + 
							 "Bitte wählen Sie einen anderen Namen für Ihr Verzeichnis" );
				
				} else if ( projectErrorSource == "ProFolderCreate" ) {
					msgTitle = Language.translate("Fehler - Projektverzeichnis !" );
					msgText = Language.translate("Das Verzeichnis konnte nicht aangelegt werden !" );
				}
				MessageDialog.openError(this.getShell(), msgTitle, msgText);
			}
			
		} else {
			
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// +++ Open an existing Project +++++++++++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// --- Check project directory --------------------------
			if (projectError==false && (projectDirectory==null || projectDirectory.length()==0) ) {
				projectErrorSource = "ProFolder";
				projectError = true;
			}
			// --- Check project file -------------------------------
			if (projectError==false) {		
				String xmlLFileName = Application.getGlobalInfo().getPathProjects() + projectDirectory + File.separator + Application.getGlobalInfo().getFileNameProject();
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
				MessageDialog.openError(this.getShell(), msgTitle, msgText);
			}
		}

		return projectError;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#setProjectName(java.lang.String)
	 */
	@Override
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#getProjectName()
	 */
	@Override
	public String getProjectName() {
		return this.projectName;
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#setProjectFolder(java.lang.String)
	 */
	@Override
	public void setProjectDirectory(String projectDirectory) {
		this.projectDirectory = projectDirectory;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectNewOpenDialog#getProjectFolder()
	 */
	@Override
	public String getProjectDirectory() {
		return this.projectDirectory;
	}	

}
