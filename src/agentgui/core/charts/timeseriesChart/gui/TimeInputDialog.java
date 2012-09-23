package agentgui.core.charts.timeseriesChart.gui;

import javax.swing.JDialog;
import javax.swing.SpinnerDateModel;

import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JSpinner;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;

public class TimeInputDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5462652220701239016L;
	private JLabel lblMessage;
	private JSpinner spTimeInput;
	private boolean canceled = false;
	private JButton btnConfirm;
	private JButton btnCancel;

	/**
	 * Create the panel.
	 */
	public TimeInputDialog(Window owner, String title, String message) {
		super(owner);
		setTitle(title);
		setModal(true);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_lblMessage = new GridBagConstraints();
		gbc_lblMessage.insets = new Insets(0, 0, 5, 0);
		gbc_lblMessage.gridx = 0;
		gbc_lblMessage.gridy = 0;
		gbc_lblMessage.anchor = GridBagConstraints.EAST;
		getContentPane().add(getLblMessage(message), gbc_lblMessage);
		GridBagConstraints gbc_spTimeInput = new GridBagConstraints();
		gbc_spTimeInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_spTimeInput.insets = new Insets(0, 0, 5, 0);
		gbc_spTimeInput.gridx = 1;
		gbc_spTimeInput.gridy = 0;
		getContentPane().add(getSpTimeInput(), gbc_spTimeInput);
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.insets = new Insets(0, 0, 0, 5);
		gbc_btnConfirm.gridx = 0;
		gbc_btnConfirm.gridy = 1;
		getContentPane().add(getBtnConfirm(), gbc_btnConfirm);
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.gridx = 1;
		gbc_btnCancel.gridy = 1;
		getContentPane().add(getBtnCancel(), gbc_btnCancel);

		pack();
		setVisible(true);
	}

	private JLabel getLblMessage(String message) {
		if (lblMessage == null) {
			lblMessage = new JLabel(message);
		}
		return lblMessage;
	}
	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	private JSpinner getSpTimeInput() {
		if (spTimeInput == null) {
			SpinnerDateModel sdm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
			spTimeInput = new JSpinner(sdm);
			JSpinner.DateEditor de = new JSpinner.DateEditor(spTimeInput, "HH:mm");
			spTimeInput.setEditor(de);
		}
		return spTimeInput;
	}
	
	public Long getTimestamp(){
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
	
	
	private JButton getBtnConfirm() {
		if (btnConfirm == null) {
			btnConfirm = new JButton("Apply");
			btnConfirm.addActionListener(this);
		}
		return btnConfirm;
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
}
