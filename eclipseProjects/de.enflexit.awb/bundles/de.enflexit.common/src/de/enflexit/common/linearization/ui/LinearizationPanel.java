package de.enflexit.common.linearization.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.enflexit.common.BundleHelper;
import de.enflexit.common.linearization.Linearization;
import java.awt.FlowLayout;

/**
 * The Class LinearizationPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LinearizationPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 7367197262728781067L;
	
	public static String ACTION_COMMAND_ADD_VARIABLE = "Add-Variable";
	public static String ACTION_COMMAND_ADD_FORMULA = "Add-Formula";
	public static String ACTION_COMMAND_REMOVE_FORMULA = "Remove-Formula";
	
	private Linearization linearization;

	private JLabel jLabelHeaderVariables;
	private JPanel jPanelVariablesUsed;
	private JButton jButtonAddVariable;
	
	private JLabel jLabelHeaderFormulas;
	private JScrollPane jScrollPaneFormulas;
	private JTable jTableFormulas;
	private JScrollPane jScrollPaneVariablesUsed;
	private JButton jButtonAddFormula;
	private JButton jButtonRemoveFormula;
	
	private ActionListener externalActionListener;
	
	
	/**
	 * Instantiates a new linearization panel.
	 */
	public LinearizationPanel() {
		this.initialize();
	}
	/**
	 * Initializes the panel.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 62, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeaderVariables = new GridBagConstraints();
		gbc_jLabelHeaderVariables.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderVariables.gridx = 0;
		gbc_jLabelHeaderVariables.gridy = 0;
		add(getJLabelHeaderVariables(), gbc_jLabelHeaderVariables);
		GridBagConstraints gbc_jScrollPaneVariablesUsed = new GridBagConstraints();
		gbc_jScrollPaneVariablesUsed.gridwidth = 2;
		gbc_jScrollPaneVariablesUsed.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneVariablesUsed.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneVariablesUsed.gridx = 0;
		gbc_jScrollPaneVariablesUsed.gridy = 1;
		add(getJScrollPaneVariablesUsed(), gbc_jScrollPaneVariablesUsed);
		GridBagConstraints gbc_jButtonAddVariable = new GridBagConstraints();
		gbc_jButtonAddVariable.insets = new Insets(5, 5, 0, 0);
		gbc_jButtonAddVariable.anchor = GridBagConstraints.NORTH;
		gbc_jButtonAddVariable.gridx = 2;
		gbc_jButtonAddVariable.gridy = 1;
		add(getJButtonAddVariable(), gbc_jButtonAddVariable);
		GridBagConstraints gbc_jLabelHeaderFormulas = new GridBagConstraints();
		gbc_jLabelHeaderFormulas.insets = new Insets(10, 0, 0, 0);
		gbc_jLabelHeaderFormulas.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderFormulas.gridx = 0;
		gbc_jLabelHeaderFormulas.gridy = 2;
		add(getJLabelHeaderFormulas(), gbc_jLabelHeaderFormulas);
		GridBagConstraints gbc_jButtonAddFormula = new GridBagConstraints();
		gbc_jButtonAddFormula.insets = new Insets(10, 5, 0, 0);
		gbc_jButtonAddFormula.gridx = 1;
		gbc_jButtonAddFormula.gridy = 2;
		add(getJButtonAddFormula(), gbc_jButtonAddFormula);
		GridBagConstraints gbc_jButtonRemoveFormula = new GridBagConstraints();
		gbc_jButtonRemoveFormula.insets = new Insets(10, 5, 0, 0);
		gbc_jButtonRemoveFormula.gridx = 2;
		gbc_jButtonRemoveFormula.gridy = 2;
		add(getJButtonRemoveFormula(), gbc_jButtonRemoveFormula);
		GridBagConstraints gbc_jScrollPaneFormulas = new GridBagConstraints();
		gbc_jScrollPaneFormulas.gridwidth = 3;
		gbc_jScrollPaneFormulas.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneFormulas.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneFormulas.gridx = 0;
		gbc_jScrollPaneFormulas.gridy = 3;
		add(getJScrollPaneFormulas(), gbc_jScrollPaneFormulas);
	}

	private JLabel getJLabelHeaderVariables() {
		if (jLabelHeaderVariables == null) {
			jLabelHeaderVariables = new JLabel("Used Variables");
			jLabelHeaderVariables.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderVariables;
	}
	private JScrollPane getJScrollPaneVariablesUsed() {
		if (jScrollPaneVariablesUsed == null) {
			jScrollPaneVariablesUsed = new JScrollPane();
			jScrollPaneVariablesUsed.setViewportView(getJPanelVariablesUsed());
		}
		return jScrollPaneVariablesUsed;
	}
	private JPanel getJPanelVariablesUsed() {
		if (jPanelVariablesUsed == null) {
			jPanelVariablesUsed = new JPanel();
			jPanelVariablesUsed.setBackground(new Color(255, 255, 255));
			jPanelVariablesUsed.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		}
		return jPanelVariablesUsed;
	}
	private JButton getJButtonAddVariable() {
		if (jButtonAddVariable == null) {
			jButtonAddVariable = new JButton();
			jButtonAddVariable.setToolTipText("Add variable ...");
			jButtonAddVariable.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAddVariable.setPreferredSize(new Dimension(26, 26));
			jButtonAddVariable.setActionCommand(ACTION_COMMAND_ADD_VARIABLE);
			jButtonAddVariable.addActionListener(this);
		}
		return jButtonAddVariable;
	}
	
	
	private JLabel getJLabelHeaderFormulas() {
		if (jLabelHeaderFormulas == null) {
			jLabelHeaderFormulas = new JLabel("List of linear coefficients and their boundaries");
			jLabelHeaderFormulas.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderFormulas;
	}
	private JButton getJButtonAddFormula() {
		if (jButtonAddFormula == null) {
			jButtonAddFormula = new JButton();
			jButtonAddFormula.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAddFormula.setToolTipText("Add linear equation");
			jButtonAddFormula.setPreferredSize(new Dimension(26, 26));
			jButtonAddFormula.setActionCommand(ACTION_COMMAND_ADD_FORMULA);
			jButtonAddFormula.addActionListener(this);
		}
		return jButtonAddFormula;
	}
	private JButton getJButtonRemoveFormula() {
		if (jButtonRemoveFormula == null) {
			jButtonRemoveFormula = new JButton();
			jButtonRemoveFormula.setIcon(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonRemoveFormula.setToolTipText("Remove selected linear equation");
			jButtonRemoveFormula.setPreferredSize(new Dimension(26, 26));
			jButtonRemoveFormula.setActionCommand(ACTION_COMMAND_REMOVE_FORMULA);
			jButtonRemoveFormula.addActionListener(this);
		}
		return jButtonRemoveFormula;
	}
	private JScrollPane getJScrollPaneFormulas() {
		if (jScrollPaneFormulas == null) {
			jScrollPaneFormulas = new JScrollPane();
			jScrollPaneFormulas.setViewportView(getJTableFormulas());
		}
		return jScrollPaneFormulas;
	}
	private JTable getJTableFormulas() {
		if (jTableFormulas == null) {
			jTableFormulas = new JTable();
			jTableFormulas.setFillsViewportHeight(true);
		}
		return jTableFormulas;
	}
	
	
	/**
	 * Returns the linearization.
	 * @return the linearization
	 */
	public Linearization getLinearization() {
		return linearization;
	}
	/**
	 * Sets the linearization.
	 * @param linearization the new linearization
	 */
	public void setLinearization(Linearization linearization) {
		this.linearization = linearization;
	}
	
	/**
	 * Sets the specified ActionListener to the JButton for the add action.
	 * @param newActionListener the new action listener to add action
	 */
	public void setActionListenerToAddVariableAction(ActionListener newActionListener) {
		
		// --- Which ActionListener is currently used ---------------
		ActionListener alUsed = this.externalActionListener!=null ? this.externalActionListener : this;
		
		// --- Remove previously used ActionListener ---------------- 
		this.getJButtonAddVariable().removeActionListener(alUsed);
		
		// --- Apply the new ActionListener -------------------------
		ActionListener alToUse = newActionListener;  
		this.externalActionListener = newActionListener;
		if (alToUse==null) alToUse = this;
		
		// --- Add the selected ActionListener ---------------------- 
		this.getJButtonAddVariable().addActionListener(alToUse);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	
		if (ae.getSource()==this.getJButtonAddVariable()) {
			
			String newVariableID = null;
			
			String title = "Add new linearization variable";
			String msg = "Please, specify a new variable for the linearization!";
			newVariableID = JOptionPane.showInputDialog(this, msg, title, JOptionPane.QUESTION_MESSAGE);
			// TODO
			
		} else if (ae.getSource()==this.getJButtonAddFormula()) {
			
			
		} else if (ae.getSource()==this.getJButtonRemoveFormula()) {
			
		}
		
	}
	
	
}
