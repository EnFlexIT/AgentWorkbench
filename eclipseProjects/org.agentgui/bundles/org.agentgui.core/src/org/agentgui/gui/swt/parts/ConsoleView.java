package org.agentgui.gui.swt.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class ConsoleView {
	
	private Label lblConsoleView;

	public ConsoleView() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));
		
		this.lblConsoleView = new Label(parent, SWT.NONE);
		this.lblConsoleView.setText("AwbConsole View");
		
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
