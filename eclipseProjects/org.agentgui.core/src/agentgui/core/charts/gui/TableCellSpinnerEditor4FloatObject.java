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

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;

import de.enflexit.common.swing.KeyAdapter4Numbers;

import javax.swing.SpinnerNumberModel;
/**
 * JSpinner-based table cell editor for Float objects
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 *
 */
public class TableCellSpinnerEditor4FloatObject extends BasicCellEditor{
	
	private static final long serialVersionUID = 7758086423044836617L;
	
	@Override
	public Object getCellEditorValue(){
		Object value = ((JSpinner)editorComponent).getValue();
		// If the value is a String or Double, convert it to Float
		if(value instanceof String){
			value = ((String)value).replace(",", ".");
			value = (Float.parseFloat(((String)value)));
		}else if(value instanceof Double){
			value = ((Double)value).floatValue();
		}
		
		return value;
	}

	@Override
	protected Component getEditorComponent(Object value) {
		if(editorComponent == null){
			editorComponent = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
			editorComponent.addKeyListener(this);
			DefaultEditor edit = (DefaultEditor) ((JSpinner)editorComponent).getEditor();
			edit.getTextField().addKeyListener(this);
			edit.getTextField().addKeyListener(new KeyAdapter4Numbers(true));
		}
		((JSpinner)editorComponent).setValue(value);
		return editorComponent;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.BasicCellEditor#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB){
			try {
				((JSpinner)editorComponent).commitEdit();
				
			} catch (ParseException e1) {
				DefaultEditor de = (DefaultEditor) ((JSpinner)editorComponent).getEditor();
				String text = de.getTextField().getText();
				if(text.matches("^\\d+(\\.\\d*)?")){
					((JSpinner)editorComponent).setValue(Float.parseFloat(text));
				}else{
					System.err.println("Invalid input - ignoring");
				}
			}
		}
		super.keyPressed(e);
	}
	
	
}
