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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;

import agentgui.core.config.GlobalInfo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Control;

/**
 * The ProjectEditor.
 */
public class ProjectEditor extends EditorPart {

	public static final String ID = "org.agentgui.core.part.projectEditor"; //$NON-NLS-1$
	
	private FormToolkit toolkit;
		
	private SashForm sashForm;
	private Tree tree;
	private TreeViewer treeViewer;
	private Button btnNewButton;
	private CTabFolder tabFolder;
	private CTabItem tabItem;
	private CTabItem tabItem_1;
	private CTabItem tbtmNewItem;
	private Composite composite;
	private CTabItem tabItem_2;
	private CTabFolder tabFolder_1;
	private Section sctnNewSection;
	private Section sctnNewSection_1;
	private CTabItem tabItem_3;

	
	public ProjectEditor() {
		setContentDescription("The project editor enables to edit projects");
		setPartName("Project Editor");
		setTitleImage(GlobalInfo.getInternalSWTImage("AgentGUIGreen16.png"));
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		this.toolkit = new FormToolkit(parent.getDisplay());

		this.sashForm = new SashForm(parent, SWT.NONE);
		
		
		this.treeViewer = new TreeViewer(this.sashForm, SWT.BORDER);
		this.tree = this.treeViewer.getTree();
		
		this.tabFolder = new CTabFolder(this.sashForm, SWT.BORDER);
		this.tabFolder.setTabPosition(SWT.BOTTOM);
		this.toolkit.adapt(this.tabFolder);
		this.toolkit.paintBordersFor(this.tabFolder);
		this.tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		this.tabItem = new CTabItem(this.tabFolder, SWT.NONE);
		this.tabItem.setText("New Item");

			ScrolledForm form2 = this.toolkit.createScrolledForm(this.tabFolder);
			this.tabItem.setControl(form2);
			this.toolkit.decorateFormHeading(form2.getForm());
			
				form2.setText("First Page: Eclipse Forms API Example");
				form2.getBody().setLayout(new GridLayout(1, false));
		
				this.sctnNewSection = this.toolkit.createSection(form2.getBody(), Section.TWISTIE | Section.TITLE_BAR);
				this.sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
				this.toolkit.paintBordersFor(this.sctnNewSection);
				this.sctnNewSection.setText("New Section");
				
				this.sctnNewSection_1 = this.toolkit.createSection(form2.getBody(), Section.TWISTIE | Section.TITLE_BAR);
				this.sctnNewSection_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1));
				this.toolkit.paintBordersFor(this.sctnNewSection_1);
				this.sctnNewSection_1.setText("New Section");
				
		
		
		this.tabItem_1 = new CTabItem(this.tabFolder, SWT.NONE);
		this.tabItem_1.setText("New Item");
		
				ScrolledForm form = this.toolkit.createScrolledForm(this.tabFolder);
				this.tabItem_1.setControl(form);
				this.toolkit.decorateFormHeading(form.getForm());
				
						form.setText("Second Page: Eclipse Forms API Example");
						form.getBody().setLayout(new GridLayout(1, false));
						
								this.btnNewButton = toolkit.createButton(form.getBody(), "New Button", SWT.NONE);
								
								this.tabItem_3 = new CTabItem(this.tabFolder, SWT.NONE);
								this.tabItem_3.setText("New Item");
								
								this.tbtmNewItem = new CTabItem(this.tabFolder, SWT.NONE);
								this.tbtmNewItem.setText("New Item");
								
								this.composite = new Composite(this.tabFolder, SWT.NONE);
								this.tbtmNewItem.setControl(this.composite);
								this.toolkit.paintBordersFor(this.composite);
								this.composite.setLayout(new GridLayout(1, false));
								
								this.tabItem_2 = new CTabItem(this.tabFolder, SWT.NONE);
								this.tabItem_2.setText("New Item");
								
								this.tabFolder_1 = new CTabFolder(this.tabFolder, SWT.BORDER);
								this.tabItem_2.setControl(this.tabFolder_1);
								this.toolkit.paintBordersFor(this.tabFolder_1);
								this.tabFolder_1.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

														
														
														
		this.sashForm.setWeights(new int[] { 3, 10 });	
		parent.setTabList(new Control[]{this.sashForm});
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// Initialize the editor part
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
