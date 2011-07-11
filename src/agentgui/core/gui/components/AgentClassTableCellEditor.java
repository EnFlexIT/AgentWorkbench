/**
 * 
 */
package agentgui.core.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.gui.AgentSelector;

/**
 * @author Satyadeep
 *
 */
public class AgentClassTableCellEditor extends AbstractCellEditor
implements TableCellEditor,
ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1937780991527069423L;
	JButton button;
	AgentSelector agentSelector;
	String currentAgentClass;
	protected static final String EDIT = "edit";
	/**
	 * 
	 */
	public AgentClassTableCellEditor() {
		button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        agentSelector = new AgentSelector(Application.MainWindow);
	}
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return currentAgentClass;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            button.setText(currentAgentClass);
            
            agentSelector.setVisible(true);    
            if (agentSelector.isCanceled()==false) {
            	Object[] selected = agentSelector.getSelectedAgentClasses();
    			if(selected != null && selected.length > 0){
    				AgentClassElement agentClass = (AgentClassElement) selected[0];
    				currentAgentClass = agentClass.getElementClass().getName();
    			}
            }
            //Make the renderer reappear.
            fireEditingStopped();

        } else { //User pressed dialog's "OK" button.
        	
        }
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table,
								            Object value,
								            boolean isSelected,
								            int row,
								            int column) {
		currentAgentClass = (String) value;
		return button;
	}

}
