/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
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
