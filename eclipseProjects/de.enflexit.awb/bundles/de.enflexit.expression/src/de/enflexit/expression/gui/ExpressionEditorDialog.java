package de.enflexit.expression.gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import agentgui.core.application.Language;
import de.enflexit.common.swing.JDialogSizeAndPostionController;
import de.enflexit.common.swing.JDialogSizeAndPostionController.JDialogPosition;
import de.enflexit.expression.Expression;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Class ExpressionEditorDialog.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = -5820516711953973203L;
	
	private boolean canceled;
	
	private ExpressionEditorMainPanel jPanelExpressionPanel;

	private JPanel jPanelButtons;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	
	
	/**
	 * Instantiates a new expression editor dialog.
	 */
	public ExpressionEditorDialog() {
		this(null);
	}
	/**
	 * Instantiates a new expression editor dialog.
	 * @param expression the expression to edit
	 */
	public ExpressionEditorDialog(Expression expression) {
		this.initialize();
		this.setExpression(expression);
	}
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		
		this.setTitle("Expression Editor");
		this.setSize(900, 600);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		JDialogSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{1184, 0};
		gbl_mainPanel.rowHeights = new int[]{533, 28, 0};
		gbl_mainPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_mainPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gbl_mainPanel);
		
		GridBagConstraints gbc_jPanelExpressionPanel = new GridBagConstraints();
		gbc_jPanelExpressionPanel.fill = GridBagConstraints.BOTH;
		gbc_jPanelExpressionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_jPanelExpressionPanel.gridx = 0;
		gbc_jPanelExpressionPanel.gridy = 0;
		this.getContentPane().add(getJPanelExpressionPanel(), gbc_jPanelExpressionPanel);
		
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(5, 0, 20, 0);
		gbc_jPanelButtons.anchor = GridBagConstraints.NORTH;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 1;
		this.getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
	}

	private ExpressionEditorMainPanel getJPanelExpressionPanel() {
		if (jPanelExpressionPanel == null) {
			jPanelExpressionPanel = new ExpressionEditorMainPanel();
		}
		return jPanelExpressionPanel;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonOK = new GridBagConstraints();
			gbc_jButtonOK.insets = new Insets(0, 0, 0, 20);
			gbc_jButtonOK.gridx = 0;
			gbc_jButtonOK.gridy = 0;
			jPanelButtons.add(getJButtonOK(), gbc_jButtonOK);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(0, 20, 0, 0);
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	/**
	 * Gets the jButtonOK.
	 * @return the jButtonOK
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton(Language.translate("  OK  ",Language.EN));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setMinimumSize(new Dimension(100, 28));
			jButtonOK.setMaximumSize(new Dimension(100, 28));
			jButtonOK.setPreferredSize(new Dimension(100, 28));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	/**
	 * Gets the jButtonCancel.
	 * @return the jButtonCancel
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton(Language.translate("Cancel",Language.EN));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setMinimumSize(new Dimension(100, 28));
			jButtonCancel.setMaximumSize(new Dimension(100, 28));
			jButtonCancel.setPreferredSize(new Dimension(100, 28));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * Gets the expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		return this.getJPanelExpressionPanel().getExpression();
	}
	/**
	 * Sets the expression.
	 * @param expression the new expression
	 */
	public void setExpression(Expression expression) {
		this.getJPanelExpressionPanel().setExpression(expression);
	}
	
	/**
	 * Checks if the dialog was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonOK()) {
			this.canceled = false;
			this.setVisible(false);
			this.dispose();
		} else if (ae.getSource()==this.getJButtonCancel()) {
			this.canceled = true;
			this.setVisible(false);
			this.dispose();
		}
	}
}
