package gasmas.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;

public class ExitAdapter extends NetworkComponentAdapter implements ActionListener {

	private JMenuItem jMenueItemExitClose;
	private JMenuItem jMenueItemExitFailure;
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		return new ExitDataModelAdapter();
	}

	@Override
	public Vector<JMenuItem> getJPopupMenuElements() {
		Vector<JMenuItem> menuItems = new Vector<JMenuItem>();
		menuItems.add(this.getJMenuItemExitFailure());	
		menuItems.add(this.getJMenuItemExitClose());
		return menuItems;
	}
	
	private JMenuItem getJMenuItemExitClose() {
		if (this.jMenueItemExitClose==null) {
			this.jMenueItemExitClose = new JMenuItem();
			this.jMenueItemExitClose.setText("Close Exit");
			this.jMenueItemExitClose.addActionListener(this);
		}
		return this.jMenueItemExitClose;
	}
	
	private JMenuItem getJMenuItemExitFailure() {
		if (this.jMenueItemExitFailure==null) {
			this.jMenueItemExitFailure = new JMenuItem();
			this.jMenueItemExitFailure.setText("Set Exit Failure");
			this.jMenueItemExitFailure.addActionListener(this);
		}
		return this.jMenueItemExitFailure;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String netCompID = this.networkComponent.getId();
		String netCompType = this.networkComponent.getType();
		
		Object aeSource = ae.getSource();
		if (aeSource==this.getJMenuItemExitFailure()) {
			JOptionPane.showMessageDialog(null, "Example for an Exit-Failure !", "Exit: Failure " + netCompID + "(" + netCompType + ") !", JOptionPane.ERROR_MESSAGE);
			// --- Do something ---
			
			
		} else if (aeSource==this.getJMenuItemExitClose() ) {
			JOptionPane.showMessageDialog(null, "Example for an Exit-Close operation !", "Exit: Close " + netCompID + "(" + netCompType + ") !", JOptionPane.INFORMATION_MESSAGE);
			// --- Do something ---
		}
		
	}

}
