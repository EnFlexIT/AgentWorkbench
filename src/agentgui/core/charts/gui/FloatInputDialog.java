package agentgui.core.charts.gui;

import java.awt.Window;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class FloatInputDialog extends KeyInputDialog{
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// Swing components
	private JSpinner spFloatInput;
	
	public FloatInputDialog(Window owner, String title, String message) {
		super(owner);
		setTitle(title);
		setModal(true);
		
		initialize(message);
	}
	
	protected JSpinner getInputComponent() {
		if (spFloatInput == null) {
			spFloatInput = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Float.MAX_VALUE, 0.1));
		}
		return spFloatInput;
	}
	
	public Float getValue(){
		return ((Double) spFloatInput.getModel().getValue()).floatValue();
	}

}
