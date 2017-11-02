package org.agentgui.gui.swt.parts;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class SectionPart extends org.eclipse.ui.forms.SectionPart {
	private Label lblTestLabel;

	/**
	 * Create the SectionPart.
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
	public SectionPart(Composite parent, FormToolkit toolkit, int style) {
		super(parent, toolkit, style);
		createClient(getSection(), toolkit);
	}

	/**
	 * Fill the section.
	 */
	private void createClient(Section section, FormToolkit toolkit) {
		section.setText("My SectionPart");
		Composite container = toolkit.createComposite(section);
		section.setClient(container);
		container.setLayout(new ColumnLayout());
		
		this.lblTestLabel = toolkit.createLabel(container, "Test Label", SWT.NONE);
	}

}
