package agentgui.core.charts.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import agentgui.core.ontologies.gui.DynForm;
/**
 * Abstract super class for dialogs for chart viewing and editing. 
 * ChartEditorDialogs are containers for ChartEditorJPanel implementations for 
 * the corresponding type of chart, all functionality should be implemented in
 * the panel.
 * 
 * @author Nils
 *
 */
public abstract class ChartEditorJDialog extends JDialog implements ActionListener{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1820851101239120387L;
	/**
	 * The Dialog is just a container for a ChartEditorJPanel implementation that "does the work"
	 */
	protected ChartEditorJPanel contentPane;
	
	// Swing components
	protected JPanel buttonPane;
	protected JButton btnClose;
	
	protected DynForm dynForm = null;  //  @jve:decl-index=0:
	protected int startArgIndex = -1;
	
	public ChartEditorJDialog(DynForm dynForm, int startArgIndex) {
		
		this.dynForm = dynForm;
		this.startArgIndex = startArgIndex;
		
		this.setContentPane(this.getContentPane());
		this.getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
		
		setModal(true);
		setSize(600, 450);
		
	}
	
	protected JPanel getButtonPane(){
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.add(getBtnClose());
		}
		return buttonPane;
	}
	
	protected JButton getBtnClose(){
		if(btnClose == null){
			btnClose = new JButton("Close");
			btnClose.addActionListener(this);
		}
		return btnClose;
	}
	

	/**
	 * Gets the dialogs content pane, which must be a ChartEditorJPanel implementation. 
	 */
	public abstract ChartEditorJPanel getContentPane();

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == getBtnClose()){
			this.setVisible(false);
		}
	}
	
	

}
