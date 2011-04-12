package agentgui.core.gui.components;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ClassNameListCellRenderer implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList arg0, Object arg1,
			int arg2, boolean arg3, boolean arg4) {
		JLabel rendererComponent = new JLabel();
		String simpleClassName = "";
		if(arg1 != null){
			String className = (String) arg1;
			int simpleNameStart = className.lastIndexOf(".");
			if(simpleNameStart > -1){
				simpleClassName = className.substring(simpleNameStart+1);
			}
		}
		
		rendererComponent.setText(simpleClassName);
		return rendererComponent;
	}

}
