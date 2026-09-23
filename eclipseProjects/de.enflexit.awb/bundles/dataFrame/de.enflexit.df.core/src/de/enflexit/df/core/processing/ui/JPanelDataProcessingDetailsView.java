package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import de.enflexit.df.core.processing.ProcessingDataSource;

import java.awt.Font;

/**
 * Details view panel for {@link ProcessingDataSource}s. Will contain the Graph UI for editing data processing flows.   
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelDataProcessingDetailsView extends JPanel {

	private static final long serialVersionUID = -1206393060851933798L;
	private JLabel jLabelPlaceHolder;
	
	/**
	 * Instantiates a new j panel data processing details view.
	 */
	public JPanelDataProcessingDetailsView() {
		initialize();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJLabelPlaceHolder(), BorderLayout.CENTER);
	}

	/**
	 * Gets the j label place holder.
	 * @return the j label place holder
	 */
	private JLabel getJLabelPlaceHolder() {
		if (jLabelPlaceHolder == null) {
			jLabelPlaceHolder = new JLabel("The data processing graph UI will be shown here");
			jLabelPlaceHolder.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelPlaceHolder.setVerticalAlignment(SwingConstants.TOP);
		}
		return jLabelPlaceHolder;
	}
}
