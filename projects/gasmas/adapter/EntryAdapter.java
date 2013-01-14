package gasmas.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;

public class EntryAdapter extends NetworkComponentAdapter implements ActionListener {

	private EntryDataModelAdapter entryDataModelAdapter=null;
	private Vector<JComponent> menuItems=null;
	private JMenuItem jMenueItemEntryClose=null;
	private JMenuItem jMenueItemEntryFailure=null;
	
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if(entryDataModelAdapter==null){
			entryDataModelAdapter = new EntryDataModelAdapter();
		}
		return entryDataModelAdapter;
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		if (menuItems==null) {
			menuItems = new Vector<JComponent>();
			menuItems.add(this.getJMenuItemEntryFailure());	
			menuItems.add(this.getJMenuItemEntryClose());
		}
		return menuItems;
	}
	
	private JMenuItem getJMenuItemEntryClose() {
		if (this.jMenueItemEntryClose==null) {
			this.jMenueItemEntryClose = new JMenuItem();
			this.jMenueItemEntryClose.setText("Close Entry");
			this.jMenueItemEntryClose.addActionListener(this);
		}
		return this.jMenueItemEntryClose;
	}
	
	private JMenuItem getJMenuItemEntryFailure() {
		if (this.jMenueItemEntryFailure==null) {
			this.jMenueItemEntryFailure = new JMenuItem();
			this.jMenueItemEntryFailure.setText("Set Entry Failure");
			this.jMenueItemEntryFailure.addActionListener(this);
		}
		return this.jMenueItemEntryFailure;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String netCompID = this.networkComponent.getId();
		String netCompType = this.networkComponent.getType();
		
		Object aeSource = ae.getSource();
		if (aeSource==this.getJMenuItemEntryFailure()) {
			JOptionPane.showMessageDialog(null, "Example for an Entry-Failure !", "Entry: Failure " + netCompID + "(" + netCompType + ") !", JOptionPane.ERROR_MESSAGE);
			// --- Do something ---
			
			
		} else if (aeSource==this.getJMenuItemEntryClose() ) {
			JOptionPane.showMessageDialog(null, "Example for an Entry-Close operation !", "Entry: Close " + netCompID + "(" + netCompType + ") !", JOptionPane.INFORMATION_MESSAGE);
			// --- Do something ---
		}
		
	}

}
