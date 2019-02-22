/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.graph.controller.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkModelAnalyzer;
import agentgui.envModel.graph.networkModel.NetworkModelAnalyzerListener;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

/**
 * Dialog for getting an analysis of the current NetworkModel.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkModelInformationDialog extends BasicGraphGuiJInternalFrame implements Observer, ActionListener, NetworkModelAnalyzerListener {

    private static final long serialVersionUID = -7481141098749690137L;
    
    private NetworkModelAnalyzer networkModelAnalyzer;
    
	private ComponentAdapter desktopAdapter;
	private JLabel jLabelHeader;
	private JScrollPane jScrollPaneOutput;
	private JTextArea jTextAreaOutput;
	private JButton jButtonRefresh;

	
	/**
     * Instantiates a new AddComponentDialog and displays it for the user.
     * @param graphController the GraphEnvironmentController
     */
    public NetworkModelInformationDialog(GraphEnvironmentController graphController) {
    	super(graphController);
		this.initialize();
		this.doNetworkModelAnalysis();
    }
    /**
     * This method initializes this
     */
    private void initialize() {
		
    	this.setTitle("Network Model Information");
		this.setTitle(Language.translate(this.getTitle(), Language.EN));
		
		this.setClosable(true);
		this.setMaximizable(false);
		this.setIconifiable(false);
		
		this.setAutoscrolls(true);
		this.setResizable(true);

		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
		ui.getNorthPane().remove(0);
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				doCloseAction();
			}
		});
		this.registerEscapeKeyStroke();

		
		GridBagLayout gridBagLayout = new GridBagLayout();
    	gridBagLayout.columnWidths = new int[]{0, 0, 0};
    	gridBagLayout.rowHeights = new int[]{0, 0, 0};
    	gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
    	gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
    	this.getContentPane().setLayout(gridBagLayout);
    	GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
    	gbc_jLabelHeader.insets = new Insets(10, 10, 0, 10);
    	gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
    	gbc_jLabelHeader.gridx = 0;
    	gbc_jLabelHeader.gridy = 0;
    	this.getContentPane().add(this.getJLabelHeader(), gbc_jLabelHeader);
    	GridBagConstraints gbc_jButtonRefresh = new GridBagConstraints();
    	gbc_jButtonRefresh.insets = new Insets(10, 0, 0, 10);
    	gbc_jButtonRefresh.gridx = 1;
    	gbc_jButtonRefresh.gridy = 0;
    	this.getContentPane().add(getJButtonRefresh(), gbc_jButtonRefresh);
    	GridBagConstraints gbc_jTextAreaOutput = new GridBagConstraints();
    	gbc_jTextAreaOutput.gridwidth = 2;
    	gbc_jTextAreaOutput.insets = new Insets(5, 10, 10, 10);
    	gbc_jTextAreaOutput.fill = GridBagConstraints.BOTH;
    	gbc_jTextAreaOutput.gridx = 0;
    	gbc_jTextAreaOutput.gridy = 1;
    	this.getContentPane().add(this.getJScrollPaneOutput(), gbc_jTextAreaOutput);
		
    	this.setDialogSize();

    	this.graphController.addObserver(this);
		
    	this.registerAtDesktopAndSetVisible();	
		this.graphDesktop.addComponentListener(this.getComponentAdapter4Desktop());
		
    }
    
    /**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
                doCloseAction();
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    /**
     * Returns a ComponentAdapter for the current desktop object.
     * @return the ComponentAdapter 
     */
    private ComponentAdapter getComponentAdapter4Desktop() {
    	if (this.desktopAdapter==null) {
    		desktopAdapter = new ComponentAdapter() {
    			@Override
    			public void componentResized(ComponentEvent ce) {
    				setDialogSize();
    			}
			};
    	}
    	return desktopAdapter;
    }
    /**
     * Sets the dialog size.
     */
    private void setDialogSize() {
    	
    	if (graphDesktop!=null) {
    		double scaleHeight = 0.65;
    		double scaleRatio = 0.8;
    		
    		int dialogWidth = (int) (this.graphDesktop.getSize().getHeight() * scaleHeight);
    		int dialogHeight = (int) (((double) dialogWidth) * scaleRatio); 
    		
    		int yPos = this.graphDesktop.getSize().height - dialogHeight;
    		this.setBounds(0, yPos, dialogWidth, dialogHeight);
    	
    	} else {
    		this.setSize(500, 400);
    	}
    }
    
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Analysis of the Network Model");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JButton getJButtonRefresh() {
		if (jButtonRefresh == null) {
			jButtonRefresh = new JButton();
			jButtonRefresh.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png"));
			jButtonRefresh.setToolTipText("Redo Analysis...");
			jButtonRefresh.setPreferredSize(new Dimension(26, 26));
			jButtonRefresh.setMinimumSize(new Dimension(26, 26));
			jButtonRefresh.addActionListener(this);
		}
		return jButtonRefresh;
	}
	private JScrollPane getJScrollPaneOutput() {
		if (jScrollPaneOutput==null) {
			jScrollPaneOutput = new JScrollPane(this.getJTextAreaOutput());
			jScrollPaneOutput.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		}
		return jScrollPaneOutput;
	}
	private JTextArea getJTextAreaOutput() {
		if (jTextAreaOutput == null) {
			jTextAreaOutput = new JTextArea();
		}
		return jTextAreaOutput;
	}
	
	/**
	 * Do close action.
	 */
	private void doCloseAction() {
		this.graphController.deleteObserver(this);
		this.setVisible(false);
		this.dispose();
	}
	/**
	 * Do network model analysis.
	 */
	private void doNetworkModelAnalysis() {
		this.getNetworkModelAnalyzer().reStartAnalysis();
	}
	/**
	 * Returns the network model analyzer.
	 * @return the network model analyzer
	 */
	private NetworkModelAnalyzer getNetworkModelAnalyzer() {
		if (networkModelAnalyzer==null) {
			networkModelAnalyzer = new NetworkModelAnalyzer(this.graphController);
			networkModelAnalyzer.addNetworkModelAnalyzerListener(this);
		}
		return networkModelAnalyzer;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelAnalyzerListener#onNetworkModelAnalysisExecuted()
	 */
	@Override
	public void onNetworkModelAnalysisStarted() {
		// TODO Auto-generated method stub
		System.out.println("NetworkModel Analysis started.");
	}
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelAnalyzerListener#onNetworkModelAnalysisFinalized()
	 */
	@Override
	public void onNetworkModelAnalysisFinalized() {
		// TODO Auto-generated method stub
		System.out.println("NetworkModel Analysis finalized.");
	}
	
    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
    	if (ae.getSource()==this.getJButtonRefresh()) {
    		this.doNetworkModelAnalysis();
    	}
    }
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		if (updateObject instanceof NetworkModelNotification) {
    		
    		NetworkModelNotification nmNotification = (NetworkModelNotification) updateObject;
    		switch (nmNotification.getReason()) {
    		case NetworkModelNotification.NETWORK_MODEL_ComponentTypeSettingsChanged:
    		case NetworkModelNotification.NETWORK_MODEL_Reload:
    		case NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel:
    		case NetworkModelNotification.NETWORK_MODEL_Component_Added:
    		case NetworkModelNotification.NETWORK_MODEL_Component_Removed:
    		case NetworkModelNotification.NETWORK_MODEL_EditComponentSettings:
    			this.doNetworkModelAnalysis();
    			break;
				
			default:
				break;
			}
    	}
		
	}
	
	
}