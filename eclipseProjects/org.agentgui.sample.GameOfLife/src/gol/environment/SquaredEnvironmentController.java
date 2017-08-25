package gol.environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.simulationService.environment.AbstractEnvironmentModel;
import agentgui.simulationService.environment.DisplaytEnvironmentModel;

/**
 * This is the controller for the squared gol.environment and an example 
 * that show how a customized gol.environment can be created.
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SquaredEnvironmentController extends EnvironmentController {

	public static final Integer EVENT_ENVIRONMENT_LOADED = 1;
	public static final Integer EVENT_ENVIRONMENT_UPDATED = 2;
	public static final Integer EVENT_ENVIRONMENT_SAVE = 3;
	
	private GameOfLifeDataModel myGolEnvironmentModel = new GameOfLifeDataModel();
	private String myCurrentSimSetupName = null;
	
	private AbstractEnvironmentModel abstractEnvironmentModel = null;
	
	
	/**
	 * Instantiates a new squared gol.environment controller.
	 */
	public SquaredEnvironmentController() { }
	
	/**
	 * Instantiates a new squared gol.environment controller.
	 * @param project the project
	 */
	public SquaredEnvironmentController(Project project) {
		super(project);
		if (this.getProject()!=null) {
			this.updateEnvironmentFileName();
			this.loadEnvironment();				
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#createEnvironmentPanel()
	 */
	@Override
	protected EnvironmentPanel createEnvironmentPanel() {
		return new SquaredEnvironmentGUI(this);
	}
	
	/**
	 * Gets the SquaredEnvironmentGUI
	 * @return the SquaredEnvironmentGUI
	 */
	public SquaredEnvironmentGUI getSquaredEnvironmentGUI() {
		return (SquaredEnvironmentGUI) this.getOrCreateEnvironmentPanel();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#handleSimSetupChange(agentgui.core.sim.setup.SimulationSetupsChangeNotification)
	 */
	@Override
	protected void handleSimSetupChange(SimulationSetupNotification sscn) {

		switch (sscn.getUpdateReason()) {
		case SIMULATION_SETUP_LOAD:
			// --- Update the my current file name --------
			this.updateEnvironmentFileName();
			// --- Load the current model -----------------
			this.loadEnvironment(); 
			break;
			
		case SIMULATION_SETUP_SAVED:
			this.saveEnvironment();
			break;
			
		case SIMULATION_SETUP_ADD_NEW:
			
			this.updateEnvironmentFileName();
			myGolEnvironmentModel = new GameOfLifeDataModel();
			
			this.getSquaredEnvironmentGUI().displayCurrentEnvironmentModel();
			this.saveEnvironment();
			setChanged();
			notifyObservers(EVENT_ENVIRONMENT_LOADED);
			break;

		case SIMULATION_SETUP_COPY:
			this.updateEnvironmentFileName();
			this.saveEnvironment();
			break;

		case SIMULATION_SETUP_REMOVE:
			// --- Remove the current gol.environment model --- 
			File componentFile = new File(this.getCurrentEnvironmentFileName());
			if(componentFile.exists()){
				componentFile.delete();
			}
			this.updateEnvironmentFileName();
			break;
			
		default:
			break;
		}

	}

	/**
	 * This method sets the baseFileName property and the SimulationSetup's environmentFileName according to the current SimulationSetup
	 */
	private void updateEnvironmentFileName(){
		myCurrentSimSetupName = this.getProject().getSimulationSetupCurrent();
		this.getCurrentSimulationSetup().setEnvironmentFileName(myCurrentSimSetupName+".xml");
	}
	
	/**
	 * Gets the current gol.environment file name.
	 * @return the current gol.environment file name
	 */
	private String getCurrentEnvironmentFileName() {
		if (myCurrentSimSetupName==null) {
			this.updateEnvironmentFileName();
		}
		return getEnvFolderPath() + myCurrentSimSetupName + ".xml";
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#loadEnvironment()
	 */
	@Override
	protected void loadEnvironment() {
		
		// --- Load gol.environment model -----------
		File componentFile = new File(this.getCurrentEnvironmentFileName());
		if(componentFile.exists()){
			try {
				GameOfLifeDataModel golModel = null;
				FileReader envReader = new FileReader(componentFile);
				
				JAXBContext context = JAXBContext.newInstance(GameOfLifeDataModel.class);
				Unmarshaller unmarsh = context.createUnmarshaller();
				golModel = (GameOfLifeDataModel) unmarsh.unmarshal(envReader);
				envReader.close();
				
				this.setDisplayEnvironmentModel(golModel);
				
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}				
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#saveEnvironment()
	 */
	@Override
	protected void saveEnvironment() {
		
		// --- update the internal gol.environment model ------
		this.getSquaredEnvironmentGUI().saveCurrentEnvironmentModel();
		setChanged();
		notifyObservers(EVENT_ENVIRONMENT_SAVE);
		
		try {
			// Save the network component definitions
			File componentFile = new File(this.getCurrentEnvironmentFileName());
			if(!componentFile.exists()){
				componentFile.createNewFile();
			}
			FileWriter environmentFileWriter = new FileWriter(componentFile); 
			
			JAXBContext context = JAXBContext.newInstance(GameOfLifeDataModel.class);
			Marshaller marsh = context.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marsh.marshal(myGolEnvironmentModel, environmentFileWriter);

			environmentFileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#setEnvironmentDataObject(java.lang.Object)
	 */
	@Override
	public void setDisplayEnvironmentModel(DisplaytEnvironmentModel displayEnvironmentModel) {
		
		GameOfLifeDataModel newGolModel = (GameOfLifeDataModel) displayEnvironmentModel;
		if (newGolModel==null) {
			
		} else if (this.myGolEnvironmentModel==null) {
			this.myGolEnvironmentModel = newGolModel;
			this.getSquaredEnvironmentGUI().displayCurrentEnvironmentModel();
			setChanged();
			notifyObservers(EVENT_ENVIRONMENT_LOADED);
			
		} else {
			if (this.myGolEnvironmentModel.getNumberOfColumns()==newGolModel.getNumberOfColumns() &&
				this.myGolEnvironmentModel.getNumberOfRows()   ==newGolModel.getNumberOfRows()) {
				this.myGolEnvironmentModel = newGolModel;
				this.getSquaredEnvironmentGUI().updateCurrentEnvironmentModel();
				setChanged();
				notifyObservers(EVENT_ENVIRONMENT_UPDATED);
				
			} else {
				this.myGolEnvironmentModel = newGolModel;
				this.getSquaredEnvironmentGUI().displayCurrentEnvironmentModel();
				setChanged();
				notifyObservers(EVENT_ENVIRONMENT_LOADED);
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentDataObject()
	 */
	@Override
	public DisplaytEnvironmentModel getDisplayEnvironmentModel() {
		return this.myGolEnvironmentModel;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getEnvironmentDataObjectCopy()
	 */
	@Override
	public DisplaytEnvironmentModel getDisplayEnvironmentModelCopy() {
		GameOfLifeDataModel envModelCopy = (GameOfLifeDataModel) this.myGolEnvironmentModel.getCopy();
		return envModelCopy;
	}

	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#setAbstractEnvironmentModel(java.lang.Object)
	 */
	@Override
	public void setAbstractEnvironmentModel(AbstractEnvironmentModel abstractEnvironmentModel) {
		this.abstractEnvironmentModel = abstractEnvironmentModel;
	}
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getAbstractEnvironmentModel()
	 */
	@Override
	public AbstractEnvironmentModel getAbstractEnvironmentModel() {
		return abstractEnvironmentModel;
	}
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#getAbstractEnvironmentModelCopy()
	 */
	@Override
	public AbstractEnvironmentModel getAbstractEnvironmentModelCopy() {
		return abstractEnvironmentModel;
	}


}
