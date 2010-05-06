package contmas.agents;

import contmas.behaviours.listenForExternalCommand;

public class SimulationControlAgent extends ContainerAgent{
	/**
	 * 
	 */
	private static final long serialVersionUID=7513845096672969172L;

	public SimulationControlAgent(){
		super("simulation-control");
	}

	@Override
	protected void setup(){
		super.setup();
		this.addBehaviour(new listenForExternalCommand(this));
	}
}
