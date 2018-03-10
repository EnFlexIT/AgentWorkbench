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
package org.agentgui.gui.swt.project;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.agentgui.gui.AwbProjectEditorWindow;
import org.agentgui.gui.swing.project.ProjectWindowTab;
import org.agentgui.gui.swt.AppModelId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.project.Project;

/**
 * The ProjectEditor is used as project editor within the SWT environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectEditor extends EditorPart implements AwbProjectEditorWindow {

	public static final String ID = AppModelId.PART_ORG_AGENTGUI_CORE_PART_PROJECTEDITOR;
	
	private Shell currShell;
	private MPart currMPart;
	private Project currProject;
	
	private SashForm sashForm;
	
	private Tree tree;
	private TreeViewer treeViewer;
	private CTabFolder tabFolder;
	private CTabItem tabItem;
	private CTabItem tabItem_1;

	
	/**
	 * Instantiates a new project editor.
	 *
	 * @param mPart the MPart that uses the current ProjectEditor as 'Object'
	 * @param shell the current shell
	 */
	@Inject
	public ProjectEditor(MPart mPart, Shell shell) {
		if (mPart!=null) {
			this.currMPart = mPart;
			this.currShell = shell;
			this.currProject = (Project) this.currMPart.getTransientData().get(Project.class.getName());
			this.currMPart.setLabel(this.currProject.getProjectName());
			this.currMPart.setTooltip(this.currProject.getProjectName());
		}
		this.setPartName("Project Editor");
		this.setContentDescription("The project editor enables to edit projects");
		this.setTitleImage(GlobalInfo.getInternalSWTImage("AgentGUIGreen16.png"));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// TODO Initialize the editor part
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	@PostConstruct
	public void createPartControl(Composite parent) {
		
		this.sashForm = new SashForm(parent, SWT.NONE);
		
		this.treeViewer = new TreeViewer(this.sashForm, SWT.BORDER);
		this.tree = this.treeViewer.getTree();
		
		this.tabFolder = new CTabFolder(this.sashForm, SWT.BORDER);
		this.tabFolder.setTabPosition(SWT.BOTTOM);
		this.tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		this.tabItem = new CTabItem(this.tabFolder, SWT.NONE);
		this.tabItem.setText("New Item");
		
		this.tabItem_1 = new CTabItem(this.tabFolder, SWT.NONE);
		this.tabItem_1.setText("New Item");
														
		this.sashForm.setWeights(new int[] { 3, 10 });
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		this.setFocus();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO the Save operation
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return this.getProject().isUnsaved();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// Nothing to do here !
	}
	
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#getProject()
	 */
	@Override
	public Project getProject() {
		return this.currProject;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#getUserFeedbackForClosingProject(java.lang.String, java.lang.String)
	 */
	@Override
	public ProjectCloseUserFeedback getUserFeedbackForClosingProject(String msgTitle, String msgText, Object parentVisualizationComponent) {
		
		ProjectCloseUserFeedback userFeedback = null;
		
		String answerYES = Language.translate("Yes", Language.EN);
		String answerNO = Language.translate("No", Language.EN);
		String answerCANCEL = Language.translate("Cancel", Language.EN);
		int result = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, this.currShell, msgTitle, msgText, SWT.NONE, new String[] {answerYES, answerNO, answerCANCEL});
		switch (result) {
		case 0:
			userFeedback = ProjectCloseUserFeedback.SaveProject;
			break;
		case 1:
			userFeedback = ProjectCloseUserFeedback.DoNotSaveProject;
			break;
		case 2:
			userFeedback = ProjectCloseUserFeedback.CancelCloseAction;
			break;
		}
		System.out.println("=> " + userFeedback.toString());
		return userFeedback;
	}
	
	
	@Override
	public void addDefaultTabs() {
		// TODO Auto-generated method stub
	}

	@Override
	public void moveToFront() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setMaximized() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setFocus2Tab(String tabName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setViewForDeveloperOrEndUser() {
		// TODO Auto-generated method stub
	}

	@Override
	public ProjectWindowTab getTabForSubPanels(String superPanelName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateStartTab() {
		// TODO Auto-generated method stub
	}

	@Override
	public void showErrorMessage(String msgText, String msgHead) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addProjectTab(ProjectWindowTab projectWindowTab) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addProjectTab(ProjectWindowTab projectWindowTab, int indexPosition) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeProjectTab(ProjectWindowTab projectWindowTab) {
		// TODO Auto-generated method stub
	}
	
}
