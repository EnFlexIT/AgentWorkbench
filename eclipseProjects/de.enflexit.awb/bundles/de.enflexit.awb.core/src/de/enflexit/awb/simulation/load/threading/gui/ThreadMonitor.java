package de.enflexit.awb.simulation.load.threading.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import de.enflexit.awb.simulation.BundleHelper;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;
import de.enflexit.awb.simulation.load.threading.ThreadProtocolVector;
import de.enflexit.awb.simulation.load.threading.storage.ThreadInfoStorage;

/**
 * The Class SingleThreadMonitor.
 * 
 * Displays detailed information about thread/agent load.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMonitor extends JFrame {

	private static final long serialVersionUID = -5535823475614083190L;
	
	private LoadMeasureAgent myAgent;
	
	private JTabbedPane tabbedPane;
	private ThreadProtocolVector threadProtocolVector;
	private ThreadInfoStorage threadInfoStorage;
	
	private ThreadMonitorProtocolTableTab jPanelMeasureProtocol;
	private ThreadMonitorDetailTreeTab jPanelMeasureTreeDetail;
	private ThreadMonitorMetricsTableTab jPanelMeasureMetrics;

	private ThreadMonitorToolBar toolBar;

	
	/**
	 * Instantiates a new thread measure dialog.
	 *
	 * @param agent the agent
	 */
	public ThreadMonitor(LoadMeasureAgent agent) {
		this.myAgent = agent;
		this.threadProtocolVector = this.myAgent.getThreadProtocolVector();
		this.threadInfoStorage = this.myAgent.getThreadInfoStorage();
		
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setSize(1000, 562);
		
		this.setIconImage(BundleHelper.getImageIcon("awb16.png").getImage());
	    this.setTitle("Agent.Workbench: Thread Monitor");
		
		// --- Add a WindowsListener --------------------------------
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		
		// --- Set the content pane ---------------------------------
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(getThreadMeasureToolBar(), BorderLayout.NORTH);
		
		// --- Set Dialog position ----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);			
	    
	    this.setVisible(true);
	}
	

	/**
	 * Gets the tabbed pane.
	 * @return the tabbed pane
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setFont(new Font("Dialog", Font.BOLD, 13));
			tabbedPane.addTab("Thread Protocol", null, getJPanelMeasureProtocol(), null);
			tabbedPane.addTab("Thread Details", null, getJPanelMeasureTreeDetail(), null);
			tabbedPane.addTab("Thread Metrics", null, getJPanelMeasureMetrics(), null);
		}
		return tabbedPane;
	}
	/**
	 * Gets the j panel measure protocol.
	 * @return the j panel measure protocol
	 */
	public ThreadMonitorProtocolTableTab getJPanelMeasureProtocol() {
		if (jPanelMeasureProtocol == null) {
			jPanelMeasureProtocol = new ThreadMonitorProtocolTableTab(threadProtocolVector);
		}
		return jPanelMeasureProtocol;
	}
	/**
	 * Gets the j panel measure tree detail.
	 * @return the j panel measure tree detail
	 */
	public ThreadMonitorDetailTreeTab getJPanelMeasureTreeDetail() {
		if (jPanelMeasureTreeDetail == null) {
			jPanelMeasureTreeDetail = new ThreadMonitorDetailTreeTab(threadInfoStorage);
		}
		return jPanelMeasureTreeDetail;
	}
	/**
	 * Gets the j panel measure metrics.
	 * @return the j panel measure metrics
	 */
	public ThreadMonitorMetricsTableTab getJPanelMeasureMetrics() {
		if (jPanelMeasureMetrics == null) {
			jPanelMeasureMetrics = new ThreadMonitorMetricsTableTab(threadInfoStorage);
		}
		return jPanelMeasureMetrics;
	}
	
	/**
	 * Gets the thread measure tool bar.
	 *
	 * @return the thread measure tool bar
	 */
	private ThreadMonitorToolBar getThreadMeasureToolBar() {
		if (toolBar == null) {
			toolBar = new ThreadMonitorToolBar(this.myAgent);
		}
		return toolBar;
	}
}
