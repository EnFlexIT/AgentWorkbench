package de.enflexit.df.core.processing.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

/**
 * This class provides a dialog to configure data transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDialogDataTransformationConfiguration extends JDialog implements ActionListener{

	private static final long serialVersionUID = -1462269913159910976L;
	
	private JPanelTransformationGraphEditor parentEditorPanel;
	private JPanelDataTransformationConfiguration jPanelDataTransformationConfiguration;
	private JPanel jPanelButtons;
	private JButton jButtonApply;
	private JButton jButtonCancel;

	/**
	 * Instantiates a new j dialog data transformation configuration.
	 * @param owner the owner
	 * @param parentEditorPanel the parent editor panel
	 */
	public JDialogDataTransformationConfiguration(Window owner, JPanelTransformationGraphEditor parentEditorPanel) {
		super(owner);
		this.parentEditorPanel = parentEditorPanel;
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		getContentPane().add(getJPanelDataTransformationConfiguration(), BorderLayout.CENTER);
		getContentPane().add(getJPanelButtons(), BorderLayout.SOUTH);
		
		this.setTitle("Add data transformation");
		this.setSize(350, 450);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Gets the j panel data transforation configuration.
	 * @return the j panel data transforation configuration
	 */
	private JPanelDataTransformationConfiguration getJPanelDataTransformationConfiguration() {
		if (jPanelDataTransformationConfiguration == null) {
			jPanelDataTransformationConfiguration = new JPanelDataTransformationConfiguration(this.parentEditorPanel);
			jPanelDataTransformationConfiguration.getJComboBoxSelectTransformation().addActionListener(this);
		}
		return jPanelDataTransformationConfiguration;
	}
	
	/**
	 * Gets the j panel buttons.
	 * @return the j panel buttons
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonApply = new GridBagConstraints();
			gbc_jButtonApply.anchor = GridBagConstraints.EAST;
			gbc_jButtonApply.insets = new Insets(5, 0, 5, 10);
			gbc_jButtonApply.gridx = 0;
			gbc_jButtonApply.gridy = 0;
			jPanelButtons.add(getJButtonApply(), gbc_jButtonApply);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(5, 10, 5, 0);
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	
	/**
	 * Gets the j button apply.
	 *
	 * @return the j button apply
	 */
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton("Apply");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.addActionListener(this);
			jButtonApply.setEnabled(false);
		}
		return jButtonApply;
	}
	
	/**
	 * Gets the j button cancel.
	 *
	 * @return the j button cancel
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonApply()) {
			
			AbstractDataTransformation newTransformation = this.getJPanelDataTransformationConfiguration().getSelectedDataTransformation().getNewInstance();
			newTransformation.getInputNodes().addAll(this.getJPanelDataTransformationConfiguration().getSelectedInputNodes());
			
			this.parentEditorPanel.addNewDataTransformation(newTransformation);
			
			this.setVisible(false);
			this.dispose();
			
		} else if (ae.getSource()==this.getJButtonCancel()) {

			this.setVisible(false);
			this.dispose();
		} else if (ae.getSource()==this.getJPanelDataTransformationConfiguration().getJComboBoxSelectTransformation()) {
			// --- Apply is only possible if a transformation is selected ----- 
			boolean transformationSelected = (this.getJPanelDataTransformationConfiguration().getJComboBoxSelectTransformation().getSelectedItem()!=null);
			this.getJButtonApply().setEnabled(transformationSelected);
		}
	}
}
