package gol.environment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SquaredEnvironmentPlayGround extends JPanel {
	
	private static final long serialVersionUID = -2629908745240615598L;

	/** control the marking of the cell */
	private boolean mouseDown = false;
	private int stateWiper;
	private boolean inEditing = false;
	private boolean edited = false;
	
	/** coordinates or number of gol.SimService.agents */	
	private HashMap<String, LifeLabel> cells = new HashMap<String, LifeLabel>();
	private GameOfLifeDataModel golModel = null;
	
	private final int size = 15;
	private final Dimension dim = new Dimension(size, size);
	private final Color[] color = {Color.WHITE, Color.ORANGE};

	
	/**
	 * Instantiates a new squared gol.environment play ground.
	 * @param golModel the GameOfLifeDataModel 
	 */
	public SquaredEnvironmentPlayGround(GameOfLifeDataModel golModel) {
		
		super(new GridLayout(golModel.getNumberOfRows(), golModel.getNumberOfColumns(), 1, 1));
		this.golModel = golModel; 

		this.initialize();
		this.setEnvironmentHash(this.golModel.getGolHash());
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setBackground(Color.BLUE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// ---- create the labels on the frame and add each label to the panel --------------
		int nRow 	= this.golModel.getNumberOfRows();
		int nColumn = this.golModel.getNumberOfColumns();
		
		for(int r = 0; r < nRow; r++) {
			for(int c = 0; c < nColumn; c++) {
				String name = (r+"&"+c);
				LifeLabel cell = new LifeLabel(name);
				this.add(cell);
				cells.put(name, cell);
			}
		}
	}
	
	/**
	 * Returns the GoL model.
	 * @return the GameOfLifeDataModel
	 */
	public GameOfLifeDataModel getGolModel() {
		GameOfLifeDataModel displayedGolModel = new GameOfLifeDataModel();
		displayedGolModel.setNumberOfColumns(this.golModel.getNumberOfColumns());
		displayedGolModel.setNumberOfRows(this.golModel.getNumberOfRows());
		displayedGolModel.setNumberOfAgents(this.golModel.getNumberOfAgents());
		displayedGolModel.setGolHash(this.getEnvironmentHash());
		return displayedGolModel;
	}
	/**
	 * Sets the GoL model.
	 * @param golModel the GameOfLifeDataModel to set
	 */
	public void setGolModel(GameOfLifeDataModel golModel) {
		if (mouseDown==false) {
			this.golModel = golModel;
			this.setEnvironmentHash(this.golModel.getGolHash());
		}
	}

	/**
	 * Sets the gol.environment.
	 * @param golHash the GoL HashMap
	 */
	private void setEnvironmentHash(HashMap<String, Integer> golHash) {
	
		// --- Set visual representation if the user -- 
		// --- is not acting on the screen			 --
		String[] golArray = new String[golHash.size()];
		golArray = golHash.keySet().toArray(golArray);
		for (int i = 0; i < golArray.length; i++) {
			String key = golArray[i];
			Integer value = golHash.get(key);
			
			LifeLabel cell = cells.get(key);
			if (cell!=null) {
				cell.setState(value);	
			}
		}			
	}
	
	/**
	 * Returns the gol.environment.
	 * @return the gol.environment
	 */
	private HashMap<String, Integer> getEnvironmentHash() {
		
		HashMap<String, Integer> golHash = new HashMap<String, Integer>();
		
		String[] golArray = new String[cells.size()];
		golArray = cells.keySet().toArray(golArray);
		for (int i = 0; i < golArray.length; i++) {
			golHash.put(golArray[i], cells.get(golArray[i]).getState());
		}
		return golHash;
	}
	
	/**
	 * Clear all Game of Life cells.
	 */
	public void clear() {
		
		this.setInEditing(true);
		
		String[] golArray = new String[cells.size()];
		golArray = cells.keySet().toArray(golArray);
		for (int i = 0; i < golArray.length; i++) {
			cells.get(golArray[i]).clear();
		}

		this.setEdited(true);
	}
	
	/**
	 * Sets the in editing.
	 * @param inEditing the inEditing to set
	 */
	public void setInEditing(boolean inEditing) {
		this.inEditing = inEditing;
	}
	/**
	 * Checks if is in editing.
	 * @return the inEditing
	 */
	public boolean isInEditing() {
		return inEditing;
	}
	
	/**
	 * Sets the edited.
	 * @param edited the edited to set
	 */
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	/**
	 * Checks if is edited.
	 * @return the edited
	 */
	public boolean wasEdited() {
		return edited;
	}

	/**
	 * The Class LifeLabel extends JLabel and listens mouse events.
	 */
	private class LifeLabel extends JLabel implements MouseListener {

		private static final long serialVersionUID = -3057761904128267595L;
		
		private int state = 0;
		private String name; 
		
		public LifeLabel(String name) {
			this.name = name;
			this.setOpaque(true);				// so color will be showed
			this.setBackground(color[0]);
			this.addMouseListener(this);			// to select new LIVE cells
			this.setPreferredSize(dim);
			this.setToolTipText(name);
		}
		public String getName() {
			return this.name;
		}
		public int getState(){
			return state;
		}
		void setState(int newState) {
			this.state = newState;
			this.setBackground(color[newState]);
		}
		void clear() {
			this.setState(0);
			this.setBackground(color[0]);
		}
		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
		// if the mouse enter a cell and it is down we make the cell alive
		public void mouseEntered(MouseEvent arg0) {
			if(mouseDown) {
				if (stateWiper==0) {
					this.setBackground(color[0]);
					this.setState(0);
				} else {
					this.setBackground(color[1]);
					this.setState(1);
				}
			}
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
		}
		// --- Invoked when a mouse button has been pressed on a component --------------------- 
		public void mousePressed(MouseEvent arg0) {
			mouseDown = true;
			setInEditing(true);
			if (this.getBackground()==color[1]) {
				this.setBackground(color[0]);
				this.setState(0);
			} else {
				this.setBackground(color[1]);
				this.setState(1);
			}
			stateWiper = state;
		}
		// ---- Invoked when a mouse button has been released on a component -------------------
		public void mouseReleased(MouseEvent arg0) {
			mouseDown = false;
			setEdited(true);
		}
	}

}
