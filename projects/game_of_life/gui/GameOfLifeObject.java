package game_of_life.gui;

import game_of_life.gameOfLifeAgent;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

//A class that extends JLabel but also check for the neigbour 
// when asked to do so

public class GameOfLifeObject extends JLabel implements MouseListener {

	// Instance variables
	// ////////////////////////////////
	private boolean finnished;

	// Constructors
	// ////////////////////////////////
	private gameOfLifeAgent aGOL;
	
	  // Array Variables
    //////////////////////////////////
	public static final Color[] color = { Color.LIGHT_GRAY, Color.BLUE};

	// if the mouse is down or not
	public static boolean mouseDown = false;

	public GameOfLifeObject(gameOfLifeAgent gameOfLifeAgent) {

		aGOL = gameOfLifeAgent;
		setOpaque(true); // so color will be showed
		addMouseListener(this); // to select new LIVE cells
		this.setPreferredSize(GameOfLifeGUI.dim);
		
	}

	public void setFinished(boolean fini){
		finnished = fini;
	}
	
	public boolean getFinished(boolean fini){
           return finnished;
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	// if the mouse enter a cell and it is down we make the cell alive
	public void mouseEntered(MouseEvent arg0) {

		if (mouseDown) {
            
			//background of object is blue
			setBackground(1);
			
			//state of agent has been changed
			if(aGOL!=null)
			aGOL.setMyCurrentState(1);
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	// if the mouse is pressed on a cell you register the fact that it is down
	// and make that cell alive
	public void mousePressed(MouseEvent arg0) {

		mouseDown = true;
		
		setBackground(1);
		
		//state of agent has been changed
		if(aGOL!=null)
		aGOL.setMyCurrentState(1);
		
	}

	// turn off the fact that the cell is down
	public void mouseReleased(MouseEvent arg0) {
		mouseDown = false;
	}

   //set the background of the game of live
	public void setBackground(int state) {
		setBackground(color[state]);
	}

	public gameOfLifeAgent getAgentGOL() {
         
		return aGOL;
	}
}