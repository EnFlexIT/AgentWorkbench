package game_of_life.gui;

/*
The universe of the Game of Life is an infinite two-dimensional orthogonal grid of square cells, 
each of which is in one of two possible states, live or dead. 
Every cell interacts with its eight neighbors, which are the cells that are directly horizontally, 
vertically, or diagonally adjacent. At each step in time, the following transitions occur:

1.Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
2.Any live cell with more than three live neighbours dies, as if by overcrowding.
3.Any live cell with two or three live neighbours lives on to the next generation.
4.Any dead cell with exactly three live neighbours becomes a live cell.
The initial pattern constitutes the seed of the system. The first generation is created by applying the 
above rules simultaneously to every cell in the seed—births and deaths happen simultaneously, 
and the discrete moment at which this happens is sometimes called a tick 
(in other words, each generation is a pure function of the one before). 
The rules continue to be applied repeatedly to create further generations.
 */

import game_of_life.agent_controller.agentController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
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
import application.Application;

public class GameOfLifeGUI extends JInternalFrame implements ActionListener {


	public static int counterAgent;
	public static int testCounter;
	public static int totalMessagesReceived;
	public int totalNumberOfAgent;
	public boolean finished;
	int deadlock;

	// size in pixel of every label
	static final int size = 15;
	static final Dimension dim = new Dimension(size, size);

	// the slider for the speed
	JSlider slider = new JSlider(0, 5000); // 0 to 5000 milliseconds (5 seconds)

	// state of the game (running or pause)
	public static boolean gameRunning = false;
	
	public static agentController agentController;

	// the cells labels
	public GameOfLifeObject[][] objectController;
	
	// timer that fires the next feneration
	private Timer timer;
	public static JPanel panel;

	// generation counter
	private int generation = 0;
	private JLabel generationLabel = new JLabel("Generation: 0");

	// the 3 buttons
	private JButton bReset= new JButton("reset"), bStart = new JButton("Start"),bStop = new JButton("Stop");

	public int nbRow, nbCol;

	public GameOfLifeGUI(int nbRow, int nbCol, GameOfLifeObject controllers[][]) {

		/*
		 * The extended constructor JFrame has been Overwrite The new name is
		 * GameOfLife
		 */
		super("GameOfLife");

		//Frame coordinates
		this.nbCol = nbCol;
		this.nbRow = nbRow;

		this.objectController = controllers;
		
		// What should be done when user initiates close on the frame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// panel in the center with the labels
		// 1 & 1 are the gaps between the
		panel = new JPanel(new GridLayout(nbRow, nbCol, 1, 1));
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

	
		
		
		// add each label (not the one on the border) to the panel and 
		//add to each of them its neighbours
		for(int r = 0; r < nbRow; r++) {
			for(int c = 0; c < nbCol; c++) {
				
				if(this.objectController[r][c]!=null){
					
				panel.add(this.objectController[r][c]);
				
				}
				else {System.out.println(" Nullllll Object "+r+"&"+c);}
				
			}
		}
		
		// now the panel can be added
		add(panel, BorderLayout.CENTER);

		// the bottom panel with the buttons the generation label and the slider
		// this panel is formed grid panels
		panel = new JPanel(new GridLayout(1, 3));

		// another panel for the 3 buttons
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		bReset.addActionListener(this);
		buttonPanel.add(bReset);
		
		bStart.addActionListener(this);
		buttonPanel.add(bStart);
		
		bStop.addActionListener(this);
		buttonPanel.add(bStop);
		
		// add the 3 buttons to the panel
		panel.add(buttonPanel);
		// the generation label
		generationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(generationLabel);
		// the slider
		slider.setMajorTickSpacing(1000);
		slider.setMinorTickSpacing(250);
		slider.setPaintTicks(true);

		// the labels for the Slider
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();

		for (int i = 0; i <= 5; i++) {

			labelTable.put(new Integer(i * 1000), new JLabel("" + i));

		}
		
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);

		panel.add(slider);
		// in the JFrame
		add(panel, BorderLayout.SOUTH);

		// put the frame on
		setLocation(20, 20);

		// Forcing window to fit to layout
		pack();
		setVisible(true);

		
		//security variables
		counterAgent = 0;
		testCounter = 0;
		totalNumberOfAgent =( nbRow * nbCol ); 
        finished = true;
        deadlock = 0;
        totalMessagesReceived=0;
        
        
		// start the thread that run the cycles of life
		timer = new Timer(5000 - slider.getValue(), this);

	}

	// called by the Timer and the JButtons
	// this methode is constantly running
	public synchronized void actionPerformed(ActionEvent e) {

		// test the JButtons first
		Object obj = e.getSource();

		// the clear button
		if (obj == bReset) {

			timer.stop(); // stop timer
			gameRunning = false; // flag gamme not running
			bStart.setEnabled(true); // enable go button
			bReset.setEnabled(true); // disable clear button
			bReset.setEnabled(false);
			// clear all cells
			
			// reset generation number and its label
			generation = 0;
			generationLabel.setText("Generation: 0");

			return;
		}

		// the stop button
		if (obj == bStop) {
			//startGOL();
			timer.stop(); // stop timer
			//finished = false;//start the broadcasting process
			gameRunning = false; // flag not running
			bStart.setEnabled(true); // enable go button
			bStop.setEnabled(true);

			return;

		}	
	
		// the start button
		if (obj == bStart) {
			
			bStart.setEnabled(false); // disable myself
			bReset.setEnabled(true);
			bStop.setEnabled(true);
			
			//finished = true;//start the broadcasting process
			gameRunning = true; // flag game is running
			
			timer.setDelay(5000 - slider.getValue());
			timer.start();
			return;

		}

		// not a JButton so it is the timer
		// set the delay for the next time
		timer.setDelay(5000 - slider.getValue());

		
		// if the game is not running wait for next time
		if (!gameRunning)
			return;
		

		try {
			
		generationLabel.setText("Generation: " + generation);
         
		//to ensure that all agents have finished operating
		if(finished){
			
			// broadcast current state of Agent to neighbours
			agentController.broadCastCurrentState();
		
			//to ensure that all agents have finished operating
			finished = false;
		}
		
	
		
		//blocking further broadcast to ensure that all agents have finished operating
		if(totalNumberOfAgent <= (counterAgent+deadlock) ){
		
			++generation;
			counterAgent =0;
			deadlock=0;
			
			//total amount of messages received in this generation
			totalMessagesReceived=0;
			
			// change from grey to blue depending on current status
			agentController.ChangeStatus();
			
			finished=true;
		}
			
		//variable to prevent deadlock: replacess lost messages
		deadlock++;

		} catch (Exception e2) {
			System.out.println(" Problems");
		}
	}

	public void startGOL(){
		
		agentController.broadCastCurrentState();
		
	}
	
	public void startRandom() {

		for (int r = 0; r < nbRow; r++) {

			for (int c = 0; c < nbCol; c++) {

				// name of agent
				String AgentName = r + "&" + c;

				// verify if agent is active and deactivate agent
				if (!Application.JadePlatform.jadeAgentIsRunning(AgentName)) {

					if((int)(Math.random()*100)%2==0){
					
						//  Object [] obj = setMyNeighbours( r,  c);
							
						Application.JadePlatform.jadeAgentStart(r+"&"+c,
									"game_of_life.gameOfLifeAgent", null,Application.JadePlatform.MASmc
											.getName());
							objectController[r][c].setBackground(1);
							objectController[r][c].getAgentGOL().setMyCurrentState(1);
					}
					else {
	
						  //Object [] obj = setMyNeighbours( r,  c);
							
						Application.JadePlatform.jadeAgentStart(r+"&"+c,
									"game_of_life.gameOfLifeAgent", null,Application.JadePlatform.MASmc
											.getName());
					
					    objectController[r][c].setBackground(0);
					    objectController[r][c].getAgentGOL().setMyCurrentState(0);
					    
					}

				}
			}
		}
	}


	public void resetAll() {

		for (int r = 0; r < nbRow; r++) {
			for (int c = 0; c < nbCol; c++) {

					try {

						objectController[r][c].getAgentGOL().resetAll();

					} catch (Exception e) {

						System.out
								.println(" Error in the process of state of Agents: "
										+ e.getMessage());
					}
				}
			}
	}
}