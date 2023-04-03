package gol.environment;

import java.util.HashMap;

import jakarta.xml.bind.annotation.XmlRootElement;

import agentgui.simulationService.environment.DisplaytEnvironmentModel;

/**
 * The Class SquaredEnvironmentModel.
 */
@XmlRootElement
public class GameOfLifeDataModel extends DisplaytEnvironmentModel {

	private static final long serialVersionUID = -6120241455973578020L;

	private HashMap<String, Integer> golHash = new HashMap<String, Integer>();
	private int numberOfColumns = 0;
	private int numberOfRows = 0;
	private int numberOfAgents = 0;
	
	/**
	 * Instantiates a new game of life data model.
	 */
	public GameOfLifeDataModel() {
	}
	
	@Override
	public DisplaytEnvironmentModel getCopy() {
		GameOfLifeDataModel copy = new GameOfLifeDataModel();
		HashMap<String, Integer> hashCopy = new HashMap<String, Integer>(this.golHash);
		copy.setGolHash(hashCopy);
		copy.setNumberOfColumns(numberOfColumns);
		copy.setNumberOfRows(numberOfRows);
		copy.setNumberOfAgents(numberOfAgents);
		return copy;	
	}
	
	/**
	 * Gets the gol hash.
	 * @return the golArray
	 */
	public HashMap<String, Integer> getGolHash() {
		return golHash;
	}
	/**
	 * Sets the gol hash.
	 * @param golHash the gol hash
	 */
	public void setGolHash(HashMap<String, Integer> golHash) {
		this.golHash = golHash;
	}

	/**
	 * Gets the number of columns.
	 * @return the numberOfColumns
	 */
	public int getNumberOfColumns() {
		return numberOfColumns;
	}
	/**
	 * Sets the number of columns.
	 * @param numberOfColumns the numberOfColumns to set
	 */
	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	/**
	 * Gets the number of rows.
	 * @return the numberOfRows
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}
	/**
	 * Sets the number of rows.
	 * @param numberOfRows the numberOfRows to set
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	/**
	 * Gets the number of gol.SimService.agents.
	 * @return the numberOfAgents
	 */
	public int getNumberOfAgents() {
		return numberOfAgents;
	}
	/**
	 * Sets the number of gol.SimService.agents.
	 * @param numberOfAgents the numberOfAgents to set
	 */
	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}


}
