package game_of_life.SimService.agents;

import game_of_life.gui.GameOfLifeGUI;
import jade.core.AID;
import jade.core.ServiceException;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JDesktopPane;

import agentgui.core.application.Application;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModelStroke;

public class SimulationManager extends SimulationManagerAgent {

	private static final long serialVersionUID = 670062003421753868L;

	//------------- set GUI co-ordinates ------------------------------------------------
	private Object agentArgs[];
	private int cRow;
	private int cCol;

	//------------ JInternalframe -------------------------------------------------------
	private GameOfLifeGUI gui;

	//------------ Environment for Agents -----------------------------------------------
	public HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
	public HashMap<String, Integer> localEnvModelNew = new HashMap<String, Integer>();
	
	@SuppressWarnings("unused") private long timeStepStart;
	@SuppressWarnings("unused")	private long timeStepStop;
	@SuppressWarnings("unused")	private long timeStepTotal;
	@SuppressWarnings("unused")	private double timeStepTotal_ms;
	
	@SuppressWarnings("unchecked")
	protected void setup() {
		super.setup();
		
		// ----- get the arguments/coordinates of agents --------------------------------
		agentArgs = this.getArguments();
		cRow = (Integer) agentArgs[0];
		cCol = (Integer) agentArgs[1];
		localEnvModel = (HashMap<String, Integer>) agentArgs[2];
		
		// ---------- start and show GUI ------------------------------------------------
		gui = new GameOfLifeGUI(cRow, cCol, this);
		gui.bClear.setEnabled(false);
		gui.bPause.setEnabled(false);
		gui.bStart.setEnabled(false);
		gui.setResizable(true);
		gui.setMaximizable(true);
		
		JDesktopPane desptop = Application.ProjectCurr.projectDesktop;
		desptop.add(gui);
		desptop.getDesktopManager().maximizeFrame(gui);
				
		gui.bClear.setEnabled(true);
		gui.bPause.setEnabled(false);
		gui.bStart.setEnabled(true);
		
	}

	public void setRunSimulation(boolean run) {
		if (run==true) {
			this.addSimulationBehavior();
		} else {
			this.removeSimulationBehaviour();
		}
	}
	
	@Override
	protected void takeDown() {
		gui.doDefaultCloseAction();
		gui.dispose();			
		gui = null;
		super.takeDown();
	}
	
	@Override
	public EnvironmentModel getInitialEnvironmentModel() {

		EnvironmentModel envModel = new EnvironmentModel();
		TimeModelStroke tm = new TimeModelStroke();
		envModel.setTimeModel(tm);
		envModel.setAbstractEnvironment(this.localEnvModel);
		return envModel;
	}

	@Override
	public void doSingleSimulationSequennce() {

		// --- ggf. den Verzögerungs-Slider berücksichtigen -------------------
		if (gui.slider.getValue()>0) {
			doWait(gui.slider.getValue());
		}
		
		// --- Ggf. den Outgoing-Speicher der GUI lesen -----------------------
		if (gui.localEnvModelOutput.size()!=0) {
			String[] fieldArray = new String[gui.localEnvModelOutput.size()];
			fieldArray = gui.localEnvModelOutput.keySet().toArray(fieldArray);
			for (int i = 0; i < fieldArray.length; i++) {
				String key = fieldArray[i];
				Integer value = gui.localEnvModelOutput.get(key);
				localEnvModel.put(key, value);					
			}
			gui.localEnvModelOutput.clear();
		}
		this.getEnvironmentModel().setAbstractEnvironment(localEnvModel);
		
		
		// --- Simulationsschritt durchführen ---------------------------------
		try {
			this.resetEnvironmentInstanceNextParts();
			this.stepSimulation(localEnvModel.size());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void proceedAgentAnswers(Hashtable<AID, Object> agentAnswers) {

		// --- Neues Umgebungsmodell definieren ---------------------------
		localEnvModelNew = new HashMap<String, Integer>();
		AID[] agentArray = new AID[agentAnswers.size()];
		agentArray = agentAnswers.keySet().toArray(agentArray);
		for (int i = 0; i < agentArray.length; i++) {
			Integer value = (Integer) agentAnswers.get(agentArray[i]);  
			localEnvModelNew.put(agentArray[i].getLocalName(), value);
		}
		gui.updateGUI(localEnvModelNew);
		localEnvModel = localEnvModelNew;
		
		this.doNextSimulationStep();
		
	}

	
}
