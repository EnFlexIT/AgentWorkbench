package org.awb.env.networkModel.controller.ui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;

/**
 * The Class NetworkComponentSelectionDialog allows to select a NetworkComponent in a dialog.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class NetworkComponentSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -1406567922255154435L;
	
	private GraphEnvironmentController graphController;
	private boolean enableMultipleSelection;
	private List<NetworkComponentTableService> columnServicesToAdd;
	
	private boolean canceled;
	
	private NetworkComponentTablePanel jPanelNetworkComponentTable;
	private JPanel jPanelButtons;
	private JButton jButtonOk;
	private JButton jButtonCancel;

	
    /**
     * Instantiates a NetworkComponentSelectionDialog. To directly show the available network components, invoke {@link #reLoadNetworkComponents()}
     * or set an exclude list by using {@link #setExcludeList(Vector)} and call {@link #reLoadNetworkComponents()}.<br>
     * Alternatively, set the list of NetworkCompents by using {@link #setNetworkComponentList(List)}. These will be displayed immediately. 
	 *
	 * @param owner the owner window
	 * @param graphController the graph controller
	 * @param enableMultipleSelection the indicator to enable multiple selection
     * @wbp.parser.constructor
	 */
	public NetworkComponentSelectionDialog(Window owner, GraphEnvironmentController graphController, boolean enableMultipleSelection) {
		this(owner, graphController, enableMultipleSelection, null);
	}
    /**
     * Instantiates a NetworkComponentSelectionDialog. To directly show the available network components, invoke {@link #reLoadNetworkComponents()}
     * or set an exclude list by using {@link #setExcludeList(Vector)} and call {@link #reLoadNetworkComponents()}.<br>
     * Alternatively, set the list of NetworkCompents by using {@link #setNetworkComponentList(List)}. These will be displayed immediately. 
     *
     * @param owner the owner window
     * @param graphController the graph controller
     * @param enableMultipleSelection the indicator to enable multiple selection
     * @param columnServicesToAdd the table services to considered and to add further columns to the table
     */
	public NetworkComponentSelectionDialog(Window owner, GraphEnvironmentController graphController, boolean enableMultipleSelection, List<NetworkComponentTableService> columnServicesToAdd) {
		super(owner);
		this.graphController = graphController;
		this.enableMultipleSelection = enableMultipleSelection;
		this.columnServicesToAdd = columnServicesToAdd;
		this.initialize();
	}
	private void initialize() {
		
		this.setTitle("Network Component Selection");
		this.setModal(true);
		this.setSize(450, 500);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		
		this.registerEscapeKeyStroke();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				NetworkComponentSelectionDialog.this.setCanceled(true);
				NetworkComponentSelectionDialog.this.setVisible(false);
			}
		});
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelNetworkComponentTable = new GridBagConstraints();
		gbc_jPanelNetworkComponentTable.insets = new Insets(10, 10, 0, 10);
		gbc_jPanelNetworkComponentTable.fill = GridBagConstraints.BOTH;
		gbc_jPanelNetworkComponentTable.gridx = 0;
		gbc_jPanelNetworkComponentTable.gridy = 0;
		getContentPane().add(getJPanelNetworkComponentTable(), gbc_jPanelNetworkComponentTable);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(10, 10, 15, 10);
		gbc_jPanelButtons.fill = GridBagConstraints.BOTH;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 1;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	NetworkComponentSelectionDialog.this.setCanceled(true);
            	NetworkComponentSelectionDialog.this.setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	private NetworkComponentTablePanel getJPanelNetworkComponentTable() {
		if (jPanelNetworkComponentTable == null) {
			jPanelNetworkComponentTable = new NetworkComponentTablePanel(this.graphController, false, this.enableMultipleSelection, this.columnServicesToAdd);
			jPanelNetworkComponentTable.addMouseListener(new MouseAdapter() {
		    	@Override
		    	public void mouseClicked(MouseEvent me) {
		    		if (me.getClickCount()==2) {
		    			NetworkComponentSelectionDialog.this.getJButtonOk().doClick();
		    		}
		    	}
			});
			
		}
		return jPanelNetworkComponentTable;
	}
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			jPanelButtons.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("Ok");
			jButtonOk.setPreferredSize(new Dimension(80, 26));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	// ----------------------------------------------------------------------------------
	// --- From here, interface methods to the NetworkComponentTablePanel ----- Start ---
	// ----------------------------------------------------------------------------------
    /**
     * Returns the network component list that will be used for displaying network components.
     * @return the network component list
     */
    public List<NetworkComponent> getNetworkComponentList() {
    	return this.getJPanelNetworkComponentTable().getNetworkComponentList();
    }
    /**
     * Sets the network component list to be used for displaying {@link NetworkComponent}s.
     * @param netCompsToDisplay the new network component list
     */
    public void setNetworkComponentList(List<NetworkComponent> netCompsToDisplay) {
    	this.getJPanelNetworkComponentTable().setNetworkComponentList(netCompsToDisplay);
    }
   
    /**
     * Gets the exclude list.
     * @return the exclude list
     */
    public Vector<String> getExcludeList(){
    	return this.getJPanelNetworkComponentTable().getExcludeList();
    }
    /**
     * Sets the exclude list.
     * @param excludeList the new exclude list
     */
    public void setExcludeList(Vector<String> excludeList) {
    	this.getJPanelNetworkComponentTable().setExcludeList(excludeList);
    }

    /**
     * Reloads the network model.
     */
    public void reLoadNetworkComponents() {
    	this.getJPanelNetworkComponentTable().reLoadNetworkComponents();
    }
    
    /**
     * Returns the currently selected network component.
     * @return the currently selected network component
     */
	public NetworkComponent getSelectedNetworkComponent() {
    	return this.getJPanelNetworkComponentTable().getSelectedNetworkComponent();
    }
    /**
     * Returns the selected network components.
     * @return the selected network components
     */
    public Vector<NetworkComponent> getSelectedNetworkComponents(){
    	return this.getJPanelNetworkComponentTable().getSelectedNetworkComponents();
    }
    
    /**
     * Sets the currently selected network component.
     * @param networkComponent the network component to be selected
     */
    public void setSelectedNetworkComponent(NetworkComponent networkComponent) {
    	this.getJPanelNetworkComponentTable().setSelectedNetworkComponent(networkComponent);
    }
	// ----------------------------------------------------------------------------------
	// --- From here, interface methods to the NetworkComponentTablePanel ----- End -----
	// ----------------------------------------------------------------------------------
    
    
	/**
	 * Checks if the dialog action was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
	 * Sets the canceled.
	 * @param cannceld the new canceled
	 */
	private void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * Checks if is error.
	 * @return true, if is error
	 */
	private boolean isError() {
	
		boolean isError = false;
		
		return isError;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonCancel()) {
			this.setCanceled(true);
			this.setVisible(false);
			
		} else if (ae.getSource()==this.getJButtonOk()) {
			if (this.isError()==false) {
				this.setCanceled(false);
				this.setVisible(false);
			}
		}
	}
		
}
