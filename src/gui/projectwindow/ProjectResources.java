package gui.projectwindow;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import application.Project;
import java.awt.Dimension;

public class ProjectResources extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;

	private Project currProjet = null;
	
	/**
	 * This is the default constructor
	 */
	public ProjectResources(Project cp) {
		super();
		currProjet = cp;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(742, 380);
		this.setLayout(new GridBagLayout());
	}

	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
