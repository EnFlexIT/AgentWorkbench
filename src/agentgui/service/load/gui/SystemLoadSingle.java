package mas.service.load.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import mas.service.distribution.ontology.OSInfo;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.distribution.ontology.PlatformPerformance;
import mas.service.load.LoadInformation.NodeDescription;
import application.Application;
import java.awt.Font;

public class SystemLoadSingle extends JPanel {

	private static final long serialVersionUID = 1L;
	
	final static String PathImage = Application.RunInfo.PathImageIntern();
	private final ImageIcon iconGreen = new ImageIcon( this.getClass().getResource( PathImage + "StatGreen.png") );  //  @jve:decl-index=0:
	private final ImageIcon iconRed = new ImageIcon( this.getClass().getResource( PathImage + "StatRed.png") );  //  @jve:decl-index=0:

	private JLabel jLabelThreshold = null;
	private JProgressBar jLoadCPU = null;
	private JProgressBar jLoadMemory = null;
	private JProgressBar jLoadJVM = null;
	private JLabel jLabelNoThreads = null;
	private JLabel jLabelContainerName = null;
	private JLabel jLabelNodeDescription = null;
	private JLabel jLabelNoAgents = null;

	private JLabel jLabelCPUCaption = null;

	private JLabel jLabelMemCaption = null;

	private JLabel jLabelHeapCaption = null;

	/**
	 * This is the default constructor
	 */
	public SystemLoadSingle() {
		super();
		initialize();
	}

	/**
	 * This method initialises this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 2;
		gridBagConstraints31.insets = new Insets(5, 0, 10, 2);
		gridBagConstraints31.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints31.gridy = 2;
		jLabelHeapCaption = new JLabel();
		jLabelHeapCaption.setText("Heap");
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 2;
		gridBagConstraints21.insets = new Insets(5, 0, 0, 2);
		gridBagConstraints21.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints21.gridy = 1;
		jLabelMemCaption = new JLabel();
		jLabelMemCaption.setText("Mem.");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 2;
		gridBagConstraints11.insets = new Insets(10, 0, 0, 2);
		gridBagConstraints11.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints11.gridy = 0;
		jLabelCPUCaption = new JLabel();
		jLabelCPUCaption.setText("CPU");
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 1;
		gridBagConstraints7.insets = new Insets(5, 10, 0, 0);
		gridBagConstraints7.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints7.gridy = 2;
		jLabelNoAgents = new JLabel();
		jLabelNoAgents.setText("xxxx Agents");
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 4;
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.insets = new Insets(10, 10, 0, 10);
		gridBagConstraints6.gridheight = 3;
		gridBagConstraints6.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints6.gridy = 0;
		jLabelNodeDescription = new JLabel();
		jLabelNodeDescription.setText("NodeDescription");
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.insets = new Insets(10, 10, 0, 10);
		gridBagConstraints5.gridheight = 1;
		gridBagConstraints5.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints5.gridy = 0;
		jLabelContainerName = new JLabel();
		jLabelContainerName.setText("MainContainer");
		jLabelContainerName.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelContainerName.setPreferredSize(new Dimension(90, 16));
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 1;
		gridBagConstraints4.insets = new Insets(5, 10, 0, 0);
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.gridy = 1;
		jLabelNoThreads = new JLabel();
		jLabelNoThreads.setText("xxxx Threads");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 3;
		gridBagConstraints3.insets = new Insets(5, 0, 10, 0);
		gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints3.gridy = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 3;
		gridBagConstraints2.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints2.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 3;
		gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.gridy = 0;
		jLabelThreshold = new JLabel();
		jLabelThreshold.setText("");
		jLabelThreshold.setPreferredSize(new Dimension(16, 16));
		jLabelThreshold.setIcon(iconGreen);
		this.setSize(539, 85);
		this.setLayout(new GridBagLayout());
		this.add(jLabelThreshold, gridBagConstraints);
		this.add(getJLoadCPU(), gridBagConstraints1);
		this.add(getJLoadMemory(), gridBagConstraints2);
		this.add(getJLoadJVM(), gridBagConstraints3);
		this.add(jLabelNoThreads, gridBagConstraints4);
		this.add(jLabelContainerName, gridBagConstraints5);
		this.add(jLabelNodeDescription, gridBagConstraints6);
		this.add(jLabelNoAgents, gridBagConstraints7);
		this.add(jLabelCPUCaption, gridBagConstraints11);
		this.add(jLabelMemCaption, gridBagConstraints21);
		this.add(jLabelHeapCaption, gridBagConstraints31);
	}

	/**
	 * This method initializes jLoadCPU	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJLoadCPU() {
		if (jLoadCPU == null) {
			jLoadCPU = new JProgressBar();
			jLoadCPU.setPreferredSize(new Dimension(150, 18));
			jLoadCPU.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLoadCPU.setStringPainted(true);
			jLoadCPU.setToolTipText("CPU-Load of the System");
		}
		return jLoadCPU;
	}

	/**
	 * This method initializes jLoadMemory	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJLoadMemory() {
		if (jLoadMemory == null) {
			jLoadMemory = new JProgressBar();
			jLoadMemory.setPreferredSize(new Dimension(150, 18));
			jLoadMemory.setStringPainted(true);
			jLoadMemory.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLoadMemory.setToolTipText("Memory-Load of the System");
		}
		return jLoadMemory;
	}

	/**
	 * This method initializes jLoadJVM	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJLoadJVM() {
		if (jLoadJVM == null) {
			jLoadJVM = new JProgressBar();
			jLoadJVM.setPreferredSize(new Dimension(150, 18));
			jLoadJVM.setStringPainted(true);
			jLoadJVM.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLoadJVM.setToolTipText("Memory Heap JVM");
		}
		return jLoadJVM;
	}

	public void updateView( String containerName, NodeDescription nD, float benchmarkValue, PlatformLoad pL, Integer noAg ) {

		jLabelContainerName.setText(containerName);
		if (pL.getLoadExceeded()==0) {
			jLabelThreshold.setIcon(iconGreen);
		} else {
			jLabelThreshold.setIcon(iconRed);
		}
		jLoadCPU.setValue((int) pL.getLoadCPU());
		jLoadMemory.setValue((int) pL.getLoadMemorySystem());
		jLoadJVM.setValue((int) pL.getLoadMemoryJVM());
		jLabelNoThreads.setText( pL.getLoadNoThreads() + " Threads");
		jLabelNoAgents.setText(noAg + " Agents");
		
		// --- Beschreibung einstellen --------------------
		String jvmPID = " [" + nD.getJvmPID() + "]";
		
		OSInfo os = nD.getOsInfo();
		String opSys = os.getOs_name() + " " + os.getOs_version() + jvmPID;
		
		PlatformPerformance pP = nD.getPlPerformace();
		String perform = pP.getCpu_vendor() + ": " + pP.getCpu_model();
		perform = perform.replaceAll("  ", " ");
		perform+= "<br>" + pP.getCpu_numberOf() + " x "+ pP.getCpu_speedMhz() + "MHz [" + pP.getMemory_totalMB() + " MB RAM]";
		
		String bench = benchmarkValue + " Mflops";
		
		String description = "<HTML><BODY>" + opSys + "<br>" + perform + "<br>" + bench + "</BODY></HTML>";
		jLabelNodeDescription.setText(description);
		
}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
