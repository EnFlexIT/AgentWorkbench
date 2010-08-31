
package game_of_life.gui;

import game_of_life.agents.SimulationServiceControllerAgent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class GameOfLifeGUI extends JInternalFrame implements ActionListener {
	
	private static final long serialVersionUID = -3527934254643712099L;
	//--------- coordinates or number of agents -------------------------------------------
	public int nbRow;
	public int nbCol;
	//--------- Simulation environment ----------------------------------------------------
	private SimulationServiceControllerAgent controllerAgent = null;
	
	//public HashMap<String, Integer> localEnvModelInput = new HashMap<String, Integer>();
	public HashMap<String, Integer> localEnvModelOutput = new HashMap<String, Integer>();
	// ------- state of the game (running or pause)----------------------------------------
	public boolean gameRunning = false;
	public boolean gameReset = false;
	
	private final Color[] color = {Color.LIGHT_GRAY, Color.BLUE};
	// ------- size in pixel of every label ------------------------------------------------
	private final int size = 15;
	private final Dimension dim = new Dimension(size, size);

    // ------- the cells labels ------------------------------------------------------------
	private LifeLabel[][] label;
	// ------- timer that fires the next generation ----------------------------------------
	private Timer timer;
	// ------- generation counter ----------------------------------------------------------
	public JLabel generationLabel = new JLabel("Generation: 0");
	// ------- buttons for controlling the simulation --------------------------------------
	private JButton bClear = new JButton("Clear"), 
	                bPause = new JButton("Pause"), 
	                bStart = new JButton("Start");
	// ------- the slider for the speed -----------------------------------------------------
	public JSlider slider = new JSlider(0, 3000);	// 0 to 3000 milliseconds (5 seconds)
	// ------- control the marking of the cell ----------------------------------------------
	private boolean mouseDown = false;
	
	public GameOfLifeGUI(int nbRow, int nbCol, SimulationServiceControllerAgent contrAgent) {
		
		super("GameOfLife");
		// ------ coordinates of number of agents -------------------------------------------
		this.nbRow = nbRow;
		this.nbCol = nbCol;
		this.controllerAgent = contrAgent;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// ---- create the lables on the frame ---------------------------------------------
		label = new LifeLabel[nbRow][nbCol];
		for(int r = 0; r < nbRow; r++) {
			for(int c = 0; c < nbCol; c++) {
				String name = (r+"&"+c);
				label[r][c] = new LifeLabel(name);
			}
		}
		// ---- centralize panel with the labels, 1 & 1 are the gaps between the lables ----  
		JPanel panel = new JPanel(new GridLayout(nbRow, nbCol, 1, 1));
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// ---- add each label to the panel and add to each of them its neighbours ---------
		for(int r = 0; r < nbRow; r++) {
			for(int c = 0; c < nbCol; c++) {
				panel.add(label[r][c]);
			}
		}
		// ---- panel can now be added -----------------------------------------------------
		add(panel, BorderLayout.CENTER);
		
		// --- Create a new buffered JPanel with the specified layout manager --------------  
		panel = new JPanel(new GridLayout(1,3));
		
		// ---- another panel for the 3 buttons --------------------------------------------
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		bClear.addActionListener(this);
		buttonPanel.add(bClear);
		bPause.addActionListener(this);
		bPause.setEnabled(false);			// game is pause the pause button is disabled
		buttonPanel.add(bPause);
		bStart.addActionListener(this);
		buttonPanel.add(bStart);
		// ---- add the 3 buttons to the panel ---------------------------------------------
		panel.add(buttonPanel);
		// ---- the generation label -------------------------------------------------------
		generationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(generationLabel);
		// ---- slider for regulating the time ---------------------------------------------
		slider.setMajorTickSpacing(1000);
		slider.setMinorTickSpacing(250);
		slider.setPaintTicks(true);
		
		// ---- the labels for the Slider --------------------------------------------------
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		
		for(int i = 0; i <= 3; i++) {
			labelTable.put( new Integer( i * 1000 ), new JLabel("" + i) );
		}
		slider.setLabelTable( labelTable );
		slider.setPaintLabels(true);
		panel.add(slider);
		//------ add componets into the JFrame ----------------------------------------------
		add(panel, BorderLayout.SOUTH);
		// ----- put the frame to the specified location ------------------------------------
		setLocation(20, 20);
		// ----- force window to pass to the specified size ---------------------------------
		pack();
		//
		setVisible(true);
		// ---- timer for controlling simulation --------------------------------------------
		timer = new Timer(3000 - slider.getValue(), this);
	}
	// ------- Invoked when an action occurs. ----------------------------------------------- 
	public synchronized void actionPerformed(ActionEvent event) {
		// ----- test the JButtons first ----------------------------------------------------
		Object object = event.getSource();
		
		// ----- clear button all active bottons --------------------------------------------
		if(object == bClear) {
			timer.stop();					// stop timer
			gameRunning = false;			// flag gamme not running
			bPause.setEnabled(false);		// disable pause button
			bStart.setEnabled(true);			// enable go button
			controllerAgent.doActivate();
			controllerAgent.totalCounterTime = 0;
			controllerAgent.timeCounterStop = 0;
			controllerAgent.timeCounterStart = 0;
			controllerAgent.generation = 0;
			controllerAgent.doSuspend();
			
			// ------ clear all cells -------------------------------------------------------
			for(int r = 0; r < nbRow; r++) {
				for(int c = 0; c < nbCol; c++) {
					label[r][c].clear();
				}
			}
			// --- reset generation ---------------------------------------------------------
			generationLabel.setText("Generation: 0");
			return;
		}
		// ------ pause the simulation process -----------------------------------------------
		if(object == bPause) {
			timer.stop();					// stop timer
			gameRunning = false;			// flag not running
			bPause.setEnabled(false);		// disable myself
			bStart.setEnabled(true);			// enable go button
			controllerAgent.doSuspend();
			return;
		}
		// ----- start the simulation process -------------------------------------------------
		if(object == bStart) {
			bPause.setEnabled(true);				// enable pause button
			bStart.setEnabled(false);					// disable myself
			gameRunning = true;						// flag game is running
			controllerAgent.doActivate();
			System.out.println(" slider Value : "+slider.getValue());
			return;
		}
		// ---- the delay in milliseconds ----------------------------------------------------
		timer.setDelay(3000 - slider.getValue());
	}
	public void updateGUI(HashMap<String, Integer> localEnv){
		for(int r = 0; r < nbRow; r++) {
			for(int c = 0; c < nbCol; c++) {
				int value = localEnv.get(r+"&"+c);
				label[r][c].updateState(value);
			}
		}
		this.repaint();
	}
	// ---- A class that extends JLabel and listens mouse events ------------------------------ 
	class LifeLabel extends JLabel implements MouseListener {
		private static final long serialVersionUID = -3057761904128267595L;
		private int state;
		private String name; 
		LifeLabel(String name) {
			state = 0;			// Dead
			setOpaque(true);				// so color will be showed
			setBackground(color[0]);
			addMouseListener(this);			// to select new LIVE cells
			this.setPreferredSize(dim);
			this.name = name;
		}
		// ----- for changing state of lables -------------------------------------------------
		void updateState(int state) {
			this.state = state;
				setBackground(color[state]);
		}

		// ----- called when the game is reset/clear ------------------------------------------
		void clear() {
				this.state = 0;
				setBackground(color[0]);
				localEnvModelOutput.put(name, 0);
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
		// if the mouse enter a cell and it is down we make the cell alive
		public void mouseEntered(MouseEvent arg0) {
			if(mouseDown) {
				state = 1;
				setBackground(color[1]);	
				localEnvModelOutput.put(name, 1);
			}
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
		}
		// --- Invoked when a mouse button has been pressed on a component --------------------- 
		public void mousePressed(MouseEvent arg0) {
			mouseDown = true;
			state = 1;
			setBackground(color[1]);
			localEnvModelOutput.put(name, 1);
		}
		// ---- voked when a mouse button has been released on a component ---------------------
		public void mouseReleased(MouseEvent arg0) {
			mouseDown = false;
		}
		
		public int getState(){
			return state;
		}
	}
}