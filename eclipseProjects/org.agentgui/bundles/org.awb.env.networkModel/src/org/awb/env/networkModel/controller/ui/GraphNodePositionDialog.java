package org.awb.env.networkModel.controller.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import de.enflexit.common.SerialClone;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * The Class GraphNodePositionDialog can be used in order to configure a GraphNode position.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphNodePositionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7891622905468578296L;
	
	private Point2D graphNodePositionOld;
	private boolean isCancelAction;
	
	private GraphNodePositionPanel graphNodePositionPanel;
	private JPanel jPanelOkCancel;
	private JButton jButtonOk;
	private JButton jButtonCancel;


	/**
	 * Instantiates a new GraphNodePositionDialog.
	 * @param owner the owner window 
	 * @wbp.parser.constructor
	 */
	public GraphNodePositionDialog(Window owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Instantiates a new dialog to configure a geographical coordinate .
	 * @param owner the owner window
	 * @param geoCoordinate the geo coordinate
	 */
	public GraphNodePositionDialog(Window owner, Point2D geoCoordinate) {
		super(owner);
		this.setGraphNodePosition(geoCoordinate);
		this.initialize();
	}	

	/**
	 * Returns the current geographical  coordinate.
	 * @return the geographical coordinate
	 */
	public Point2D getGraphNodePosition() {
		if (this.isCancelAction==true) {
			return this.graphNodePositionOld;
		}
		return this.getJPanelGraphNodePosition().getGraphNodePosition();
	}
	/**
	 * Sets the graph node position.
	 * @param graphNodePosition the new graph node position
	 */
	public void setGraphNodePosition(Point2D graphNodePosition) {
		this.graphNodePositionOld = SerialClone.clone(graphNodePosition);
		this.getJPanelGraphNodePosition().setGraphNodePosition(SerialClone.clone(graphNodePosition));
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {

		this.setTitle("GraphNode Position");
		this.setSize(400, 270);
		this.setModal(true);
		this.getContentPane().add(this.getJPanelGraphNodePosition(), BorderLayout.CENTER);
		this.getContentPane().add(this.getJPanelOkCancel(), BorderLayout.SOUTH);
		this.registerEscapeKeyStroke();
		
		// --- Catch close Dialog -----------------------------------
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				doCancelAction();
			}
		});
		
		// --- Set Dialog position ----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);		
		
		this.setVisible(true);
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			doCancelAction();
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	/**
	 * Gets the JPanel to configure the geo coordinate.
	 * @return the JPanel to configure the geo coordinate.
	 */
	private GraphNodePositionPanel getJPanelGraphNodePosition() {
		if (graphNodePositionPanel == null) {
			graphNodePositionPanel = new GraphNodePositionPanel();
		}
		return graphNodePositionPanel;
	}
	/**
	 * Gets the JPanel for the OK and cancel action.
	 * @return the JPanel for the OK and cancel action
	 */
	private JPanel getJPanelOkCancel() {
		if (jPanelOkCancel == null) {
			jPanelOkCancel = new JPanel();
			GridBagLayout gbl_jPanelOkCancel = new GridBagLayout();
			gbl_jPanelOkCancel.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelOkCancel.rowHeights = new int[]{0, 0};
			gbl_jPanelOkCancel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelOkCancel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelOkCancel.setLayout(gbl_jPanelOkCancel);
			GridBagConstraints gbc_buttonOk = new GridBagConstraints();
			gbc_buttonOk.insets = new Insets(5, 20, 20, 0);
			gbc_buttonOk.gridx = 0;
			gbc_buttonOk.gridy = 0;
			jPanelOkCancel.add(getJButtonOK(), gbc_buttonOk);
			GridBagConstraints gbc_buttonCancel = new GridBagConstraints();
			gbc_buttonCancel.anchor = GridBagConstraints.NORTH;
			gbc_buttonCancel.insets = new Insets(5, 0, 0, 20);
			gbc_buttonCancel.gridx = 1;
			gbc_buttonCancel.gridy = 0;
			jPanelOkCancel.add(getJButtonCancel(), gbc_buttonCancel);
		}
		return jPanelOkCancel;
	}
	/**
	 * Gets the button ok.
	 * @return the button ok
	 */
	private JButton getJButtonOK() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setPreferredSize(new Dimension(100, 26));
			jButtonOk.setMaximumSize(jButtonOk.getPreferredSize());
			jButtonOk.setMinimumSize(jButtonOk.getPreferredSize());
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	/**
	 * Gets the button cancel.
	 * @return the button cancel
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setPreferredSize(new Dimension(100, 26));
			jButtonCancel.setMinimumSize(jButtonCancel.getPreferredSize());
			jButtonCancel.setMaximumSize(jButtonCancel.getPreferredSize());
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonOK()) {
			// --- OK-Action ------------------------------
			this.doOkAction();
		} else if (ae.getSource()==this.getJButtonCancel()) {
			// --- Cancel-Action --------------------------
			this.doCancelAction();
		}
	}
	/**
	 * Does the OK action.
	 */
	private void doOkAction() {
		this.isCancelAction = false;
		this.setVisible(false);
	}
	/**
	 * Does the cancel action.
	 */
	private void doCancelAction() {
		this.isCancelAction = true;
		this.setVisible(false);
	}
	/**
	 * Return, if the editing was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return this.isCancelAction;
	}
	
}
