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
package agentgui.simulationService.load.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import agentgui.core.config.GlobalInfo;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;

/**
 * This class is used in order to display the load on a single container in a modular way.  
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemLoadSingle extends JPanel {

	private static final long serialVersionUID = -6464252322954864779L;

	public static final int loadPanelHeight = 85;
	
	private final ImageIcon iconGreen = GlobalInfo.getInternalImageIcon("StatGreen.png");
	private final ImageIcon iconRed = GlobalInfo.getInternalImageIcon("StatRed.png");

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
	 * This is the default constructor.
	 */
	public SystemLoadSingle() {
		super();
		initialize();
	}

	/**
	 * This method initialises this.
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
		jLabelMemCaption.setText("RAM");
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
		GridBagConstraints gbcJLabelThreshold = new GridBagConstraints();
		gbcJLabelThreshold.gridx = 0;
		gbcJLabelThreshold.insets = new Insets(10, 10, 0, 0);
		gbcJLabelThreshold.anchor = GridBagConstraints.NORTHWEST;
		gbcJLabelThreshold.gridy = 0;
		jLabelThreshold = new JLabel();
		jLabelThreshold.setText("");
		jLabelThreshold.setPreferredSize(new Dimension(16, 16));
		jLabelThreshold.setIcon(iconGreen);
		
		this.setSize(540, SystemLoadSingle.loadPanelHeight);
		this.setLayout(new GridBagLayout());
		this.add(jLabelThreshold, gbcJLabelThreshold);
		this.add(this.getJLoadCPU(), gridBagConstraints1);
		this.add(this.getJLoadMemory(), gridBagConstraints2);
		this.add(this.getJLoadJVM(), gridBagConstraints3);
		this.add(jLabelNoThreads, gridBagConstraints4);
		this.add(jLabelContainerName, gridBagConstraints5);
		this.add(jLabelNodeDescription, gridBagConstraints6);
		this.add(jLabelNoAgents, gridBagConstraints7);
		this.add(jLabelCPUCaption, gridBagConstraints11);
		this.add(jLabelMemCaption, gridBagConstraints21);
		this.add(jLabelHeapCaption, gridBagConstraints31);
	}

	/**
	 * This method initializes jLoadCPU.
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
	 * This method initializes jLoadMemory.
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
	 * This method initializes jLoadJVM.
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

	/**
	 * Sets the visible AWT safe.
	 * @param aFlag the new visible 
	 */
	public void setVisibleAWTsafe(final boolean aFlag) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setVisible(aFlag);
			}
		});
	}
	/**
	 * Update view AWT safe.
	 *
	 * @param containerName the container name
	 * @param nD the NodeDescription
	 * @param benchmarkValue the benchmark value
	 * @param pL the PlatformLoad
	 * @param noAg the number of agents
	 */
	public void updateViewAWTsafe(final String containerName, final NodeDescription nD, final float benchmarkValue, final PlatformLoad pL, final Integer noAg) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

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
				
				// --- Configure description ------------------------
				String jvmPID = "[" + nD.getJvmPID() + "]";
				
				OSInfo os = nD.getOsInfo();
				String opSys = os.getOs_name() + " (" + os.getOs_version() + ") -  " + jvmPID;
				
				PlatformPerformance pP = nD.getPlPerformace();
				String perform = pP.getCpu_processorName() + ": ";
				perform = perform.replaceAll("  ", " ");
				perform+= "<br>" + pP.getCpu_numberOfLogicalCores() + " (" + pP.getCpu_numberOfPhysicalCores() + ") x "+ pP.getCpu_speedMhz() + "MHz [" + pP.getMemory_totalMB() + " MB RAM]";
				
				String bench = benchmarkValue + " Mflops";
				
				String description = "<HTML><BODY>" + opSys + "<br>" + perform + "<br>" + bench + "</BODY></HTML>";
				jLabelNodeDescription.setText(description);
				
				repaint();
			}
		});
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
