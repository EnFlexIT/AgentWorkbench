package agentgui.simulationService.load.threading;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

public class ThreadMeasureProtocolTab extends JPanel {

	private static final long serialVersionUID = -7315494195421538651L;
	private JLabel JLabelHeader;

	// --- Hier geht's weiter für den Hanno  ----!!!
	
	public ThreadMeasureProtocolTab() {
		
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_JLabelHeader = new GridBagConstraints();
		gbc_JLabelHeader.gridx = 0;
		gbc_JLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_JLabelHeader);
	}
	
	private JLabel getJLabelHeader() {
		if (JLabelHeader == null) {
			JLabelHeader = new JLabel("Hallo Hanno !!!");
		}
		return JLabelHeader;
	}
}
