package de.enflexit.awb.simulation.environment.time;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.awb.core.environment.TimeModelController;
import de.enflexit.language.Language;

/**
 * The Class TimeModelStrokeConfiguration.
 * 
 * @see TimeModelStroke
 * @see TimeModelController
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStrokeConfiguration extends JPanel4TimeModelConfiguration implements DocumentListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	private JLabel jLabelCounterStop;
	private JLabel jLabelCounterStart;
	private JTextField jTextFieldCounterStart;
	private JTextField jTextFieldCounterStop;
	private JLabel jLabelHeader1;
	private JLabel jLabelHeader2;
	
	private boolean enabledChangeListener = true;
	
	/**
	 * Instantiates a new time model stroke configuration.
	 */
	public TimeModelStrokeConfiguration() {
		this.initialize();
	}
	/**
	 * Initializes this JPanel.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0};
		gridBagLayout.columnWidths = new int[] { 0, 0, 0};
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(400, 350);
		
		GridBagConstraints gbcJLabelHeader1 = new GridBagConstraints();
		gbcJLabelHeader1.gridx = 0;
		gbcJLabelHeader1.gridy = 0;
		gbcJLabelHeader1.anchor = GridBagConstraints.WEST;
		gbcJLabelHeader1.insets = new Insets(10, 10, 5, 0);
		gbcJLabelHeader1.gridwidth = 2;

		GridBagConstraints gbcJLabelHeader2 = new GridBagConstraints();
		gbcJLabelHeader2.gridx = 0;
		gbcJLabelHeader2.gridy = 1;
		gbcJLabelHeader2.insets = new Insets(0, 10, 0, 0);
		gbcJLabelHeader2.anchor = GridBagConstraints.WEST;
		gbcJLabelHeader2.gridwidth = 2;
		
		GridBagConstraints gbcJLabelCounterStart = new GridBagConstraints();
		gbcJLabelCounterStart.gridx = 0;
		gbcJLabelCounterStart.gridy = 2;
		gbcJLabelCounterStart.insets = new Insets(10, 10, 0, 0);
		gbcJLabelCounterStart.anchor = GridBagConstraints.WEST;

		GridBagConstraints gbcJTextFieldCounterStart = new GridBagConstraints();
		gbcJTextFieldCounterStart.anchor = GridBagConstraints.WEST;
		gbcJTextFieldCounterStart.gridx = 1;
		gbcJTextFieldCounterStart.gridy = 2;
		gbcJTextFieldCounterStart.weightx = 1.0;
		gbcJTextFieldCounterStart.insets = new Insets(10, 10, 0, 0);
		
		GridBagConstraints gbcJLabelCounterStop = new GridBagConstraints();
		gbcJLabelCounterStop.gridx = 0;
		gbcJLabelCounterStop.gridy = 3;
		gbcJLabelCounterStop.insets = new Insets(10, 10, 0, 0);
		gbcJLabelCounterStop.anchor = GridBagConstraints.WEST;
		
		GridBagConstraints gbcJTextFiledCounterStop = new GridBagConstraints();
		gbcJTextFiledCounterStop.anchor = GridBagConstraints.WEST;
		gbcJTextFiledCounterStop.gridx = 1;		
		gbcJTextFiledCounterStop.gridy = 3;
		gbcJTextFiledCounterStop.weightx = 1.0;
		gbcJTextFiledCounterStop.insets = new Insets(10, 10, 0, 0);

		
		
		jLabelHeader1 = new JLabel();
		jLabelHeader1.setText("TimeModelStroke");
		jLabelHeader1.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabelHeader2 = new JLabel();
		jLabelHeader2.setText("Einfach zählendes Zeitmodell (z. B. 1 ... 9999)");
		jLabelHeader2.setText(Language.translate(jLabelHeader2.getText()));
		jLabelCounterStart = new JLabel();
		jLabelCounterStart.setText("Start bei:");
		jLabelCounterStart.setText(Language.translate(jLabelCounterStart.getText()));
		jLabelCounterStart.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelCounterStop = new JLabel();
		jLabelCounterStop.setText("Stop bei:");
		jLabelCounterStop.setText(Language.translate(jLabelCounterStop.getText()));
		jLabelCounterStop.setFont(new Font("Dialog", Font.BOLD, 12));
		
		this.add(jLabelHeader1, gbcJLabelHeader1);
		this.add(jLabelHeader2, gbcJLabelHeader2);
		this.add(jLabelCounterStart, gbcJLabelCounterStart);
        this.add(getJTextFieldCounterStart(), gbcJTextFieldCounterStart);
        this.add(jLabelCounterStop, gbcJLabelCounterStop);
        this.add(getJTextFieldCounterStop(), gbcJTextFiledCounterStop);
        			
	}

	/**
	 * This method initializes jTextFieldCounterStart1	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCounterStart() {
		if (jTextFieldCounterStart == null) {
			jTextFieldCounterStart = new JTextField();
			jTextFieldCounterStart.setPreferredSize(new Dimension(120, 26));
			jTextFieldCounterStart.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String singleChar = Character.toString(charackter);
					if (singleChar.matches("[0-9]") == false) {
						kT.consume();	
						return;
					}
				 }				 
			});
			jTextFieldCounterStart.getDocument().addDocumentListener(this);
		}
		return jTextFieldCounterStart;
	}

	/**
	 * This method initializes jTextFieldCounterStop	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCounterStop() {
		if (jTextFieldCounterStop == null) {
			jTextFieldCounterStop = new JTextField();
			jTextFieldCounterStop.setPreferredSize(new Dimension(120, 26));
			jTextFieldCounterStop.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String singleChar = Character.toString(charackter);
					if (singleChar.matches("[0-9]") == false) {
						kT.consume();	
						return;
					}
				 }				 
			});
			jTextFieldCounterStop.getDocument().addDocumentListener(this);
		}
		return jTextFieldCounterStop;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.DisplayJPanel4Configuration#setTimeModel(de.enflexit.awb.simulation.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		TimeModelStroke tms = null;
		if (timeModel==null) {
			tms = new TimeModelStroke();
		} else {
			tms = (TimeModelStroke) timeModel;
		}
		this.enabledChangeListener = false;
		this.getJTextFieldCounterStart().setText(((Integer)tms.getCounterStart()).toString());
		this.getJTextFieldCounterStop().setText(((Integer)tms.getCounterStop()).toString());
		this.enabledChangeListener = true;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.DisplayJPanel4Configuration#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {
		
		int counterStart = 0;
		int counterStop = 0;
		
		String counterStartString = this.getJTextFieldCounterStart().getText();
		String counterStopString  = this.getJTextFieldCounterStop().getText();
		
		if (counterStartString==null || counterStartString.length()==0) {
			counterStart = 0;	
		} else {
			counterStart = Integer.parseInt(counterStartString);
		}
		if (counterStopString==null || counterStopString.length()==0) {
			counterStop = 0;	
		} else {
			counterStop = Integer.parseInt(counterStopString);
		}

		// --- Prepare return value ----------------------- 
		TimeModelStroke tms = null;
		if (this.getTimeModelController().getTimeModel() instanceof TimeModelStroke) {
			tms = (TimeModelStroke) this.getTimeModelController().getTimeModel();
		} else {
			tms = new TimeModelStroke();
		}
		tms.setCounterStart(counterStart);
		tms.setCounterStop(counterStop);
		return tms;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.saveTimeModelStrokeToSimulationSetup();
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		this.saveTimeModelStrokeToSimulationSetup();
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		this.saveTimeModelStrokeToSimulationSetup();
	}
	
	/**
	 * Saves the current {@link TimeModelStroke} to the simulation setup.
	 */
	private void saveTimeModelStrokeToSimulationSetup() {
		if (this.enabledChangeListener==true) {
			this.saveTimeModelToSimulationSetup();
		}
	}
	
}  
