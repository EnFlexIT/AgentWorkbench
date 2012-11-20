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
			Calendar workCalendar = Calendar.getInstance();
			workCalendar.setTime(new Date());
			workCalendar.set(Calendar.HOUR, 0);
			workCalendar.set(Calendar.MINUTE, 0);
			spTimeInput = new JSpinner(new SpinnerDateModel(workCalendar.getTime(), null, null, Calendar.HOUR_OF_DAY));
			spTimeInput.setEditor(new JSpinner.DateEditor(spTimeInput, "HH:mm"));
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
