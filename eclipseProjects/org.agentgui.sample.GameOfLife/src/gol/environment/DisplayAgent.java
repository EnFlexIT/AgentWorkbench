package gol.environment;

import jade.core.Location;
import agentgui.core.environment.EnvironmentController;
import agentgui.simulationService.agents.AbstractDisplayAgent;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelContinuous;
import agentgui.simulationService.time.TimeModelDiscrete;
import agentgui.simulationService.time.TimeModelStroke;

/**
 * The Class DisplayAgent.
 */
public class DisplayAgent extends AbstractDisplayAgent {

	private static final long serialVersionUID = -766291673903767678L;

	private SquaredEnvironmentController mySquaredEnvironmentController = null;
	private int generationCounter=0;
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		
		// --- Show the number of generations -------------------------------------------
		this.generationCounter++;
		this.getSquaredEnvironmentController().getSquaredEnvironmentGUI().setGeneration(this.generationCounter);	

	}
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.setSquaredEnvironmentController(null);
		super.takeDown();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		this.setSquaredEnvironmentController(null);
		super.beforeMove();
		
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
		// --- Show the number of generations -------------
		this.generationCounter++;
		this.getSquaredEnvironmentController().getSquaredEnvironmentGUI().setGeneration(this.generationCounter);
	}
	
	@Override
	public void setMigration(Location newLocation) {
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#createNewEnvironmentController()
	 */
	@Override
	protected EnvironmentController createNewEnvironmentController() {
		return new SquaredEnvironmentController();
	}
	/**
	 * Gets the squared gol.environment controller.
	 * @return the mySquaredEnvironmentController
	 */
	public SquaredEnvironmentController getSquaredEnvironmentController() {
		if (this.mySquaredEnvironmentController==null) {
			this.mySquaredEnvironmentController = (SquaredEnvironmentController)this.getEnvironmentController();	
		}
		return mySquaredEnvironmentController;
	}
	/**
	 * Sets the squared gol.environment controller.
	 * @param mySquaredEnvironmentController the mySquaredEnvironmentController to set
	 */
	public void setSquaredEnvironmentController(SquaredEnvironmentController mySquaredEnvironmentController) {
		this.mySquaredEnvironmentController = mySquaredEnvironmentController;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	public void onEnvironmentStimulus() {

		if (this.myEnvironmentModel.getDisplayEnvironment()!=null) {
			
			TimeModel timeModel = this.myEnvironmentModel.getTimeModel();
			SquaredEnvironmentGUI seg = this.getSquaredEnvironmentController().getSquaredEnvironmentGUI();
			
			if (seg.isInEditing()==false) {
				// --- Panel displayed without user interaction ---------------
				this.getSquaredEnvironmentController().setEnvironmentModel(this.myEnvironmentModel);

				// --------------------------------------------------------------------------------
				// --- Set the current TimeModel to the user display ------------------------------
				if (timeModel instanceof TimeModelStroke) {
					this.setTimeModelDisplay(timeModel);
					Integer counter = ((TimeModelStroke) timeModel).getCounter();
					seg.setGeneration(counter);
					
				} else if (timeModel instanceof TimeModelDiscrete) {
					this.setTimeModelDisplay(timeModel);
					this.generationCounter++;
					seg.setGeneration(this.generationCounter);
					
				} else if (timeModel instanceof TimeModelContinuous) {
					this.setTimeModelDisplay(timeModel);
					this.generationCounter++;
					seg.setGeneration(this.generationCounter);
					
				} else {
					this.generationCounter++;
					seg.setGeneration(this.generationCounter);
				}
				
			} else {
				// --- User interaction occurred - editing finished ? ---------
				if (seg.wasEdited()==true) {
					// --- Get the edited version of the gol.environment model ----
					if (timeModel instanceof TimeModelStroke) {
						((TimeModelStroke)this.myEnvironmentModel.getTimeModel()).setCounter(seg.getGeneration());
					}
					
					this.myEnvironmentModel.setDisplayEnvironment(seg.getDataModelFromGUI());
					this.getSquaredEnvironmentController().setEnvironmentModel(myEnvironmentModel);
					
					this.sendManagerNotification(myEnvironmentModel);
					
					// --- Reset edit flag to false --------------------------
					seg.setInEditing(false);
					seg.setEdited(false);
				}
			}
		}
		
	}
	
}
