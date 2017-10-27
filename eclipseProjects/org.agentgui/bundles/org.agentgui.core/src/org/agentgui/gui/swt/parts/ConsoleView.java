package org.agentgui.gui.swt.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import org.eclipse.swt.layout.GridData;

public class ConsoleView {
	
	private Label lblConsoleView;
	private FXCanvas fxCanvas;

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
		
		
		this.fxCanvas = new FXCanvas(parent, SWT.NONE);
		this.fxCanvas.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		this.fxCanvas.setLayout(new GridLayout(1, false));
		
		
		GridDataFactory.fillDefaults().grab(true, true).span(3, 1).applyTo(this.fxCanvas);
		 
		// create the root layout pane
		BorderPane layout = new BorderPane();
		 
		// create a Scene instance
		// set the layout container as root
		// set the background fill to the background color of the shell
		Scene scene = new Scene(layout, Color.rgb(
		    parent.getShell().getBackground().getRed(),
		    parent.getShell().getBackground().getGreen(),
		    parent.getShell().getBackground().getBlue()));
		 
		
		javafx.scene.control.Label output = new javafx.scene.control.Label();
		output.setText("Ich bin ein fx-Element");
		layout.setCenter(output);
		
		// set the Scene to the FXCanvas
		this.fxCanvas.setScene(scene);
		
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
