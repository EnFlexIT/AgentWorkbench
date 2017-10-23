package agentgui.core.project.transfer.gui;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer<String> {

	private static final long serialVersionUID = 6526933726761540148L;

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus) {
		this.setText(value);
		this.setSelected(isSelected);
		this.setEnabled(list.isEnabled());
		return this;
	}


}
