package agentgui.core.charts.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassEditorJDialog;
/**
 * Abstract super class for chart implementations of OntologyClassEditorJDialog
 * @author Nils
 *
 */
public abstract class ChartEditorJDialog extends OntologyClassEditorJDialog {

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
	protected JButton btnApply;
	protected JButton btnCancel;
	
	public ChartEditorJDialog(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		setModal(true);
		this.contentPane.add(getButtonPane(), BorderLayout.SOUTH);
		setSize(600, 450);
		
	}
	
	protected JPanel getButtonPane(){
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.add(getBtnApply());
			buttonPane.add(getBtnCancel());
		}
		return buttonPane;
	}
	
	protected JButton getBtnApply(){
		if(btnApply == null){
			btnApply = new JButton("OK");
			btnApply.addActionListener(this.contentPane);
		}
		return btnApply;
	}
	
	protected JButton getBtnCancel(){
		if(btnCancel == null){
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this.contentPane);
		}
		return btnCancel;
	}

}
