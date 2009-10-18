package mas.projects.contmas.agents;
import jade.gui.GuiEvent;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 */

/**
 * @author Hanno - Felix Wagner
 *
 */
public class ControlGUI extends JInternalFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton ButtonCreateShip = null;
	private JLabel ShipLabelX = null;
	private JTextField TFShipX = null;
	private JLabel ShipLabelY = null;
	private JTextField TFShipY = null;
	private JTextField TFShipZ = null;
	private JLabel ShipLabelZ = null;
	private JLabel ShipLabelName = null;
	private JTextField TFShipName = null;
	private ControlGUIAgent myAgent;
	/**
	 * This is the default constructor
	 */
	public ControlGUI(ControlGUIAgent a) {
		super();
		myAgent=a;
		initialize();

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(394, 261);
		this.setContentPane(getJContentPane());
		this.setTitle("Hafenmeister");
/*		this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent e) {
	            shutDown();
	         }
	      });
	      */
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			ShipLabelName = new JLabel();
			ShipLabelName.setBounds(new Rectangle(113, 31, 131, 16));
			ShipLabelName.setText("Schiffsname");
			ShipLabelZ = new JLabel();
			ShipLabelZ.setBounds(new Rectangle(19, 98, 54, 16));
			ShipLabelZ.setText("Größe Z");
			ShipLabelY = new JLabel();
			ShipLabelY.setBounds(new Rectangle(18, 64, 54, 16));
			ShipLabelY.setText("Größe Y");
			ShipLabelX = new JLabel();
			ShipLabelX.setText("Größe X");
			ShipLabelX.setBounds(new Rectangle(15, 35, 56, 16));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getTFShipX(), null);
			jContentPane.add(getButtonCreateShip(), null);
			jContentPane.add(ShipLabelX, null);
			jContentPane.add(ShipLabelY, null);
			jContentPane.add(getTFShipY(), null);
			jContentPane.add(getTFShipZ(), null);
			jContentPane.add(ShipLabelZ, null);
			jContentPane.add(ShipLabelName, null);
			jContentPane.add(getTFShipName(), null);
		}
		return jContentPane;
		
	}

	/**
	 * This method initializes ButtonCreateShip	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonCreateShip() {
		if (ButtonCreateShip == null) {
			ButtonCreateShip = new JButton();
			
			ButtonCreateShip.setBounds(new Rectangle(111, 100, 134, 20));
			ButtonCreateShip.setText("Schiff erzeugen");
			ButtonCreateShip.addActionListener(this);
		}
		return ButtonCreateShip;
	}

	/**
	 * This method initializes TFShipX	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTFShipX() {
		if (TFShipX == null) {
			TFShipX = new JTextField();
			TFShipX.setBounds(new Rectangle(77, 33, 24, 27));
			TFShipX.setText("3");
		}
		return TFShipX;
	}

	/**
	 * This method initializes TFShipY	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTFShipY() {
		if (TFShipY == null) {
			TFShipY = new JTextField();
			TFShipY.setBounds(new Rectangle(78, 65, 25, 27));
			TFShipY.setText("2");
		}
		return TFShipY;
	}

	/**
	 * This method initializes TFShipZ	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTFShipZ() {
		if (TFShipZ == null) {
			TFShipZ = new JTextField();
			TFShipZ.setBounds(new Rectangle(79, 98, 25, 25));
			TFShipZ.setText("1");
		}
		return TFShipZ;
	}

	/**
	 * This method initializes TFShipName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTFShipName() {
		if (TFShipName == null) {
			TFShipName = new JTextField();
			TFShipName.setBounds(new Rectangle(116, 66, 118, 26));
			TFShipName.setText("MS Schiff");
		}
		return TFShipName;
	}

	public void actionPerformed(ActionEvent ae) {
		 if (ae.getSource() == ButtonCreateShip) {
			 GuiEvent ge = new GuiEvent(this, 1);
			 ge.addParameter(TFShipName.getText());
			 ge.addParameter(TFShipX.getText());
			 ge.addParameter(TFShipY.getText());
			 ge.addParameter(TFShipZ.getText());
			 myAgent.postGuiEvent(ge);
		 }		
	}
	void shutDown() {
	// -----------------  Control the closing of this gui
    int rep = JOptionPane.showConfirmDialog(this, "Wirklich schließen?", myAgent.getLocalName(),JOptionPane.YES_NO_CANCEL_OPTION);
    	if (rep == JOptionPane.YES_OPTION) {
    		GuiEvent ge = new GuiEvent(this, -1);
    		myAgent.postGuiEvent(ge);
	    }
	}


}  //  @jve:decl-index=0:visual-constraint="30,15"
