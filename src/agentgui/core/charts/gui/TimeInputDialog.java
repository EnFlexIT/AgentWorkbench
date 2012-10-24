package agentgui.core.charts.gui;

import javax.swing.SpinnerDateModel;

import java.awt.Window;

import javax.swing.JSpinner;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;

public class TimeInputDialog extends KeyInputDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5462652220701239016L;
	private JSpinner spTimeInput;
	private boolean canceled = false;

	/**
	 * Create the panel.
	 */
	public TimeInputDialog(Window owner, String title, String message) {
		super(owner);
		setTitle(title);
		setModal(true);
		
		initialize(message);
	}

	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	protected JSpinner getInputComponent() {
		if (spTimeInput == null) {
			Date date = new Date();
			date.setHours(0);
			date.setMinutes(0);
			SpinnerDateModel sdm = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
			spTimeInput = new JSpinner(sdm);
			JSpinner.DateEditor de = new JSpinner.DateEditor(spTimeInput, "HH:mm");
			spTimeInput.setEditor(de);
		}
		return spTimeInput;
	}
	
	public Long getValue(){
		Date date = (Date) spTimeInput.getValue();
		return date.getTime();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getBtnConfirm()){
			setVisible(false);
		}else if(e.getSource() == getBtnCancel()){
			canceled = true;
			setVisible(false);
		}
		
	}
	
	
}
