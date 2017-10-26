package org.agentgui.gui.swt.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
//import javafx.embed.swt.FXCanvas;
//import org.eclipse.swt.layout.GridData;

public class ConsoleView {
	
	private Label lblConsoleView;
//	private FXCanvas fxCanvas;

	public ConsoleView() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		this.lblConsoleView = new Label(parent, SWT.NONE);
		this.lblConsoleView.setText("Console View");
//		
//		this.fxCanvas = new FXCanvas(parent, SWT.NONE);
//		this.fxCanvas.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
//		this.fxCanvas.setLayout(new GridLayout(1, false));
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
