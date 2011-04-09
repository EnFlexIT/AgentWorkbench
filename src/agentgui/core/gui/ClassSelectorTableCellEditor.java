package agentgui.core.gui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ClassSelectorTableCellEditor extends AbstractCellEditor implements TableCellEditor{
	
	private ClassSelector classSelector = null;
	
	private String currentClass = null;

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -445634898566639002L;
	
	public ClassSelectorTableCellEditor(Frame owner, Class<?> clazz2Search4, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description){
		classSelector = new ClassSelector(owner, clazz2Search4, clazz2Search4CurrentValue, clazz2Search4DefaultValue, clazz2Search4Description);
		JButton btnOk = classSelector.getJButtonOK(); 
		btnOk.removeActionListener(btnOk.getActionListeners()[0]);
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("CellEditor Listener");
//				currentClass = classSelector.getClassSelected();
//				fireEditingStopped();
				
				classSelector.handleOkClick();
				currentClass = classSelector.getClassSelected();
				fireEditingStopped();
			}
		});
		classSelector.getJButtonCancel().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingCanceled();
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		return currentClass;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		currentClass = (String)value;
		JButton btn = new JButton();
		btn.setText(currentClass);
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				classSelector.setVisible(true);
				
			}
		});
		return btn;
	}
	
	

}
