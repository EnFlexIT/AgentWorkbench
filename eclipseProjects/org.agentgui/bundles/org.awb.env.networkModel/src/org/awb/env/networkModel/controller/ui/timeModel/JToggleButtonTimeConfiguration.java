package org.awb.env.networkModel.controller.ui.timeModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiTools;

import agentgui.core.application.Language;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetup.CHANGED;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDateBased;

/**
 * The Class JToggleButtonTimeConfiguration enables to configure the time range 
 * of a time {@link TimeModelDateBased} within the graph and network environment.
 * .
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class JToggleButtonTimeConfiguration extends JToggleButton implements ActionListener, Observer {

	private static final long serialVersionUID = -5883334849164999437L;

	private GraphEnvironmentController graphController;
	
	/**
	 * Instantiates a new JButton for the time configuration.
	 * @param graphController the graph controller
	 */
	public JToggleButtonTimeConfiguration(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		this.registerObserver();
		this.initiate();
	}

	/**
	 * Returns the current project.
	 * @return the project
	 */
	private Project getProject() {
		return this.graphController.getProject();
	}
	
	/**
	 * Returns the current {@link TimeModel}.
	 * @return the time model
	 */
	private TimeModel getTimeModel() {
		return this.graphController.getTimeModel();
	}
	
	/**
	 * Register local observer.
	 */
	private void registerObserver() {
		Project project = this.getProject();
		if (project!=null) {
			project.addObserver(this);
		}
	}
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		Project project = this.getProject();
		if (updateObject.equals(Project.CHANGED_TimeModelClass)) {
			// --- Changes in the TimeModel class of the project ----   
			if (project.getTimeModelClass()==null) {
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
			
		} else if (updateObject instanceof CHANGED) {
			// --- Change inside the simulation setup ---------------
			CHANGED changed = (CHANGED) updateObject;
			switch (changed) {
			case TimeModelSettings:
				System.out.println(this.getClass().getSimpleName() + " TimeModel was reconfigured ");
				break;

			default:
				break;
			} 
		}
		
	}
	/* (non-Javadoc)
	 * @see javax.swing.AbstractButton#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		if (isEnabled==false) this.setSelected(isEnabled);
		this.updateToolTip();
	}
	/**
	 * Updates the tool tip.
	 */
	private void updateToolTip() {
		TimeModel timeModel = this.getTimeModel();
		String tmDescription = "No TimeModel was specified for the project!";
		if (timeModel!=null) {
			tmDescription = this.getTimeModel().getClass().getSimpleName();
		}
		this.setToolTipText(Language.translate("Zeit-Konfiguration") + ": " + tmDescription);
	}
	
	
	/**
	 * Initiates this button.
	 */
	private void initiate() {
		this.setIcon(new ImageIcon(this.getClass().getResource(GraphGlobals.getPathImages() + "Clock.png")));;
		this.setPreferredSize(BasicGraphGuiTools.JBUTTON_SIZE);
		this.addActionListener(this);
		this.updateToolTip();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// TODO Auto-generated method stub
		System.out.println(this.getClass().getSimpleName() + " was pressed");
		
	}
}
