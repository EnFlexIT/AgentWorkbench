package agentgui.core.gui.projectwindow.simsetup;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.sim.setup.DistributionSetup;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.simulationService.load.LoadThresholdLevels;

public class Distribution extends JPanel implements ActionListener, Observer, KeyListener {

	private static final long serialVersionUID = 1L;
	private final static int DEFAULT_VALUES_4_DynBalancingClass = 0;
	private final static int DEFAULT_VALUES_4_ThresholdLevel = 1;

	private Project currProject = null;
	private SimulationSetup currSimSetup = null;  //  @jve:decl-index=0:
	private DistributionSetup currDistributionSetup = null; 
	private LoadThresholdLevels currUserThresholds = null;
	
	private StartSetupSelector jPanelSetupSelection = null;
	
	private JPanel jPanelStatic = null;
	private JPanel jPanelDynamic = null;
	private JPanel jPanelThreshold = null;
	private JPanel jPanelDummy = null;
	
	private JCheckBox jCheckBoxDoLoadStatic = null;
	private JCheckBox jCheckBoxDoLoadDynamic = null;
	private JCheckBox jCheckBoxThresholdDefinition = null;
	
	private JTextField jTextFieldAgentsExpected = null;
	private JTextField jTextFieldContainerExpected = null;
	private JTextField jTextFieldDynamicLoadClass = null;
	private JTextField jTextFieldCpuLow = null;
	private JTextField jTextFieldCpuHigh = null;
	private JTextField jTextFieldMemLow = null;
	private JTextField jTextFieldMemHigh = null;
	private JTextField jTextFieldThreadsHigh = null;
	private JTextField jTextFieldThreadsLow = null;
	
	private JLabel jLabelDynamicLoadClass = null;
	private JLabel jLabelAgentsExpected = null;
	private JLabel jLabelContainerExpected = null;
	
	private JLabel jLabelCPU = null;
	private JLabel jLabelMemory = null;
	private JLabel jLabelThreads = null;
	private JLabel jLabelCPUPercent = null;
	private JLabel jLabelMemPercent = null;
	private JLabel jLabelThreadsNo = null;
	private JLabel jLabelHigh = null;
	private JLabel jLabelLow = null;
	
	private JButton jButtonDefaultClass = null;
	private JButton jButtonCalcContainer = null;
	private JButton jButtonDefaultThreshold = null;
	private JLabel jLabelCalculation = null;


	/**
	 * This is the default constructor
	 */
	public Distribution(Project project) {
		super();
		currProject = project;
		currProject.addObserver(this);
		initialize();
		
		this.setupLoad();
		
		jCheckBoxDoLoadStatic.setText(Language.translate("Statische Lastverteilung aktivieren"));
		jCheckBoxDoLoadDynamic.setText(Language.translate("Dynamische Lastverteilung aktivieren"));
		jCheckBoxThresholdDefinition.setText(Language.translate("Eigene Auslastungsgrenzwerte verwenden"));
		
		jLabelDynamicLoadClass.setText(Language.translate("Java-Klasse für die dynamische Lastverteilung"));
		jLabelAgentsExpected.setText(Language.translate("Anzahl Agenten (erwartet)"));
		jLabelContainerExpected.setText(Language.translate("Anzahl Container"));

		jLabelCPU.setText(Language.translate("CPU-Auslastung"));
		jLabelMemory.setText(Language.translate("JVM Heap-Memory"));
		jLabelThreads.setText(Language.translate("N - Threads"));
		
		jButtonCalcContainer.setText(Language.translate("Anzahl Container berechnen"));
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
		gridBagConstraints111.gridx = 0;
		gridBagConstraints111.gridy = 0;
		gridBagConstraints111.weightx = 0.0;
		gridBagConstraints111.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints111.insets = new Insets(0, 0, 0, 0);
		
		GridBagConstraints gridBagConstraints101 = new GridBagConstraints();
		gridBagConstraints101.gridx = 0;
		gridBagConstraints101.fill = GridBagConstraints.NONE;
		gridBagConstraints101.weighty = 1.0;
		gridBagConstraints101.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints101.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints101.gridy = 7;
		jLabelCPU = new JLabel();
		jLabelCPU.setText("CPU-Auslastung");
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 0;
		gridBagConstraints51.insets = new Insets(5, 30, 0, 10);
		gridBagConstraints51.anchor = GridBagConstraints.WEST;
		gridBagConstraints51.fill = GridBagConstraints.NONE;
		gridBagConstraints51.gridy = 6;
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.insets = new Insets(15, 10, 0, 0);
		gridBagConstraints41.anchor = GridBagConstraints.WEST;
		gridBagConstraints41.gridy = 5;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = GridBagConstraints.NONE;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.insets = new Insets(5, 30, 0, 10);
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridy = 4;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.NONE;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.insets = new Insets(0, 30, 0, 10);
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.insets = new Insets(15, 10, 0, 0);
		gridBagConstraints1.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints.gridy = 1;
		this.setSize(617, 421);
		this.setLayout(new GridBagLayout());
		this.add(getJCheckBoxDoLoadStatic(), gridBagConstraints);
		this.add(getJCheckBoxDoLoadDynamic(), gridBagConstraints1);
		this.add(getJPanelStatic(), gridBagConstraints11);
		this.add(getJPanelDynamic(), gridBagConstraints2);
		this.add(getJCheckBoxThresholdDefinition(), gridBagConstraints41);
		this.add(getJPanelThreshold(), gridBagConstraints51);
		this.add(getJPanelDummy(), gridBagConstraints101);
		this.add(getJPanelSetupSelection(), gridBagConstraints111);
	}

	/**
	 * This method initializes jCheckBoxDoLoadStatic	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxDoLoadStatic() {
		if (jCheckBoxDoLoadStatic == null) {
			jCheckBoxDoLoadStatic = new JCheckBox();
			jCheckBoxDoLoadStatic.setText("Statische Lastverteilung aktivieren");
			jCheckBoxDoLoadStatic.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxDoLoadStatic.addActionListener(this);
		}
		return jCheckBoxDoLoadStatic;
	}

	/**
	 * This method initializes jCheckBoxDoLoadDynamic	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxDoLoadDynamic() {
		if (jCheckBoxDoLoadDynamic == null) {
			jCheckBoxDoLoadDynamic = new JCheckBox();
			jCheckBoxDoLoadDynamic.setText("Dynamische Lastverteilung aktivieren");
			jCheckBoxDoLoadDynamic.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxDoLoadDynamic.addActionListener(this);
		}
		return jCheckBoxDoLoadDynamic;
	}

	/**
	 * This method initializes jPanelStatic	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStatic() {
		if (jPanelStatic == null) {
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 4;
			gridBagConstraints27.anchor = GridBagConstraints.CENTER;
			gridBagConstraints27.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints27.fill = GridBagConstraints.NONE;
			gridBagConstraints27.gridy = 2;
			jLabelCalculation = new JLabel();
			jLabelCalculation.setText("");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 4;
			gridBagConstraints10.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints9.gridy = 2;
			jLabelContainerExpected = new JLabel();
			jLabelContainerExpected.setText("Anzahl Container");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints8.gridx = 3;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			gridBagConstraints7.gridy = 0;
			jLabelAgentsExpected = new JLabel();
			jLabelAgentsExpected.setText("Anzahl Agenten (erwartet)");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 3;
			gridBagConstraints6.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints6.weightx = 1.0;
			jPanelStatic = new JPanel();
			jPanelStatic.setLayout(new GridBagLayout());
			jPanelStatic.add(getJTextFieldAgentsExpected(), gridBagConstraints6);
			jPanelStatic.add(jLabelAgentsExpected, gridBagConstraints7);
			jPanelStatic.add(getJTextFieldContainerExpected(), gridBagConstraints8);
			jPanelStatic.add(jLabelContainerExpected, gridBagConstraints9);
			jPanelStatic.add(getJButtonCalcContainer(), gridBagConstraints10);
			jPanelStatic.add(jLabelCalculation, gridBagConstraints27);
		}
		return jPanelStatic;
	}

	/**
	 * This method initializes jPanelDynamic	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDynamic() {
		if (jPanelDynamic == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			gridBagConstraints5.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 0;
			jLabelDynamicLoadClass = new JLabel();
			jLabelDynamicLoadClass.setText("Java-Klasse für die dynamische Lastverteilung");
			jPanelDynamic = new JPanel();
			jPanelDynamic.setLayout(new GridBagLayout());
			jPanelDynamic.add(jLabelDynamicLoadClass, gridBagConstraints4);
			jPanelDynamic.add(getJTextFieldDynamicLoadClass(), gridBagConstraints3);
			jPanelDynamic.add(getJButtonDefaultClass(), gridBagConstraints5);
		}
		return jPanelDynamic;
	}

	/**
	 * This method initializes jTextFieldDynamicLoadClass	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDynamicLoadClass() {
		if (jTextFieldDynamicLoadClass == null) {
			jTextFieldDynamicLoadClass = new JTextField();
			jTextFieldDynamicLoadClass.setPreferredSize(new Dimension(350, 26));
			jTextFieldDynamicLoadClass.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent kR) {
					super.keyReleased(kR);
					currDistributionSetup.setDynamicLoadBalancingClass(jTextFieldDynamicLoadClass.getText().trim());
					currSimSetup.save();
				}
			});
		}
		return jTextFieldDynamicLoadClass;
	}

	/**
	 * This method initializes jButtonDefaultClass	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClass() {
		if (jButtonDefaultClass == null) {
			jButtonDefaultClass = new JButton();
			jButtonDefaultClass.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClass.setText("Default");
			jButtonDefaultClass.setActionCommand("DynLoadBalancingDefault");
			jButtonDefaultClass.addActionListener(this);
		}
		return jButtonDefaultClass;
	}

	/**
	 * This method initializes jTextFieldAgentsExpected	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldAgentsExpected() {
		if (jTextFieldAgentsExpected == null) {
			jTextFieldAgentsExpected = new JTextField();
			jTextFieldAgentsExpected.setPreferredSize(new Dimension(100, 26));
			jTextFieldAgentsExpected.addKeyListener(this);
		}
		return jTextFieldAgentsExpected;
	}

	/**
	 * This method initializes jTextFieldContainerExpected	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldContainerExpected() {
		if (jTextFieldContainerExpected == null) {
			jTextFieldContainerExpected = new JTextField();
			jTextFieldContainerExpected.setPreferredSize(new Dimension(100, 26));
			jTextFieldContainerExpected.addKeyListener(this);
		}
		return jTextFieldContainerExpected;
	}

	/**
	 * This method initializes jButtonCalcContainer	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCalcContainer() {
		if (jButtonCalcContainer == null) {
			jButtonCalcContainer = new JButton();
			jButtonCalcContainer.setActionCommand("CalcNumberOfContainer");
			jButtonCalcContainer.setText("Anzahl Container berechnen");
			jButtonCalcContainer.addActionListener(this);
		}
		return jButtonCalcContainer;
	}

	/**
	 * This method initializes jCheckBoxThresholdDefinition	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxThresholdDefinition() {
		if (jCheckBoxThresholdDefinition == null) {
			jCheckBoxThresholdDefinition = new JCheckBox();
			jCheckBoxThresholdDefinition.setText("Eigene Auslastungsgrenzwerte verwenden");
			jCheckBoxThresholdDefinition.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxThresholdDefinition.addActionListener(this);
		}
		return jCheckBoxThresholdDefinition;
	}

	/**
	 * This method initializes jPanelThreshold	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelThreshold() {
		if (jPanelThreshold == null) {
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 7;
			gridBagConstraints26.insets = new Insets(5, 20, 0, 0);
			gridBagConstraints26.gridy = 2;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 2;
			gridBagConstraints25.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints25.gridy = 1;
			jLabelLow = new JLabel();
			jLabelLow.setText("Low");
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 4;
			gridBagConstraints24.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints24.gridy = 1;
			jLabelHigh = new JLabel();
			jLabelHigh.setText("High");
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 6;
			gridBagConstraints23.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints23.gridy = 4;
			jLabelThreadsNo = new JLabel();
			jLabelThreadsNo.setText("-");
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 6;
			gridBagConstraints22.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints22.gridy = 3;
			jLabelMemPercent = new JLabel();
			jLabelMemPercent.setText("%");
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 6;
			gridBagConstraints21.insets = new Insets(0, 4, 0, 0);
			gridBagConstraints21.gridy = 2;
			jLabelCPUPercent = new JLabel();
			jLabelCPUPercent.setText("%");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints20.gridy = 4;
			gridBagConstraints20.weightx = 1.0;
			gridBagConstraints20.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints20.gridx = 2;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints19.gridy = 4;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints19.gridx = 4;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints18.gridy = 3;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints18.gridx = 4;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints17.gridy = 3;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints17.gridx = 2;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.NONE;
			gridBagConstraints16.gridy = 2;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints16.gridx = 4;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.NONE;
			gridBagConstraints15.gridy = 2;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints15.gridx = 2;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints14.gridy = 4;
			jLabelThreads = new JLabel();
			jLabelThreads.setText("N - Threads");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints13.gridy = 3;
			jLabelMemory = new JLabel();
			jLabelMemory.setText("JVM Heap-Memory");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = -1;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints12.gridy = 2;
			jPanelThreshold = new JPanel();
			jPanelThreshold.setLayout(new GridBagLayout());
			jPanelThreshold.add(jLabelCPU, gridBagConstraints12);
			jPanelThreshold.add(jLabelMemory, gridBagConstraints13);
			jPanelThreshold.add(jLabelThreads, gridBagConstraints14);
			jPanelThreshold.add(getJTextFieldCpuLow(), gridBagConstraints15);
			jPanelThreshold.add(getJTextFieldCpuHigh(), gridBagConstraints16);
			jPanelThreshold.add(getJTextFieldMemLow(), gridBagConstraints17);
			jPanelThreshold.add(getJTextFieldMemHigh(), gridBagConstraints18);
			jPanelThreshold.add(getJTextFieldThreadsHigh(), gridBagConstraints19);
			jPanelThreshold.add(getJTextFieldThreadsLow(), gridBagConstraints20);
			jPanelThreshold.add(jLabelCPUPercent, gridBagConstraints21);
			jPanelThreshold.add(jLabelMemPercent, gridBagConstraints22);
			jPanelThreshold.add(jLabelThreadsNo, gridBagConstraints23);
			jPanelThreshold.add(jLabelHigh, gridBagConstraints24);
			jPanelThreshold.add(jLabelLow, gridBagConstraints25);
			jPanelThreshold.add(getJButtonDefaultThreshold(), gridBagConstraints26);
		}
		return jPanelThreshold;
	}

	/**
	 * This method initializes jTextFieldCpuLow	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCpuLow() {
		if (jTextFieldCpuLow == null) {
			jTextFieldCpuLow = new JTextField();
			jTextFieldCpuLow.setPreferredSize(new Dimension(50, 26));
			jTextFieldCpuLow.addKeyListener(this);
		}
		return jTextFieldCpuLow;
	}

	/**
	 * This method initializes jTextFieldCpuHigh	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCpuHigh() {
		if (jTextFieldCpuHigh == null) {
			jTextFieldCpuHigh = new JTextField();
			jTextFieldCpuHigh.setPreferredSize(new Dimension(50, 26));
			jTextFieldCpuHigh.addKeyListener(this);
		}
		return jTextFieldCpuHigh;
	}

	/**
	 * This method initializes jTextFieldMemLow	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMemLow() {
		if (jTextFieldMemLow == null) {
			jTextFieldMemLow = new JTextField();
			jTextFieldMemLow.setPreferredSize(new Dimension(50, 26));
			jTextFieldMemLow.addKeyListener(this);
		}
		return jTextFieldMemLow;
	}

	/**
	 * This method initializes jTextFieldMemHigh	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMemHigh() {
		if (jTextFieldMemHigh == null) {
			jTextFieldMemHigh = new JTextField();
			jTextFieldMemHigh.setPreferredSize(new Dimension(50, 26));
			jTextFieldMemHigh.addKeyListener(this);
		}
		return jTextFieldMemHigh;
	}

	/**
	 * This method initializes jTextFieldThreadsHigh	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldThreadsHigh() {
		if (jTextFieldThreadsHigh == null) {
			jTextFieldThreadsHigh = new JTextField();
			jTextFieldThreadsHigh.setPreferredSize(new Dimension(50, 26));
			jTextFieldThreadsHigh.addKeyListener(this);
		}
		return jTextFieldThreadsHigh;
	}

	/**
	 * This method initializes jTextFieldThreadsLow	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldThreadsLow() {
		if (jTextFieldThreadsLow == null) {
			jTextFieldThreadsLow = new JTextField();
			jTextFieldThreadsLow.setPreferredSize(new Dimension(50, 26));
			jTextFieldThreadsLow.addKeyListener(this);
		}
		return jTextFieldThreadsLow;
	}

	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
			jPanelDummy.setPreferredSize(new Dimension(200, 26));
			jPanelDummy.setVisible(true);
		}
		return jPanelDummy;
	}

	/**
	 * This method initializes jButtonDefaultThreshold	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultThreshold() {
		if (jButtonDefaultThreshold == null) {
			jButtonDefaultThreshold = new JButton();
			jButtonDefaultThreshold.setActionCommand("ThresholdDefault");
			jButtonDefaultThreshold.setText("Default");
			jButtonDefaultThreshold.addActionListener(this);
		}
		return jButtonDefaultThreshold;
	}

	/**
	 * This method initializes jPanelSetupSelection	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSetupSelection() {
		if (jPanelSetupSelection == null) {
			jPanelSetupSelection = new StartSetupSelector(currProject);
		}
		return jPanelSetupSelection;
	}

	
	// --------------------------------------------------------------
	// --------------------------------------------------------------
	
	/**
	 * Sets the default values for the selected fields. Use constant
	 * named "DEFAULT_VALUES_4_...." from this class
	 */
	private void setDefaults(int values2Set) {
		
		switch (values2Set) {
		case DEFAULT_VALUES_4_DynBalancingClass:
			this.jTextFieldDynamicLoadClass.setText(agentgui.simulationService.agents.LoadBalancing.class.getName());
			this.currDistributionSetup.setDynamicLoadBalancingClass(this.jTextFieldDynamicLoadClass.getText());
			break;

		case DEFAULT_VALUES_4_ThresholdLevel:
			this.jCheckBoxThresholdDefinition.setSelected(false);
			this.currDistributionSetup.setUseUserThresholds(false);
			
			this.jTextFieldCpuLow.setText("5");
			this.currUserThresholds.setThCpuL(5);
			this.jTextFieldCpuHigh.setText("80");
			this.currUserThresholds.setThCpuH(80);
			
			this.jTextFieldMemLow.setText("5");
			this.currUserThresholds.setThMemoL(5);
			this.jTextFieldMemHigh.setText("80");
			this.currUserThresholds.setThMemoH(80);
			
			this.jTextFieldThreadsLow.setText("50");
			this.currUserThresholds.setThNoThreadsL(50);
			this.jTextFieldThreadsHigh.setText("1500");
			this.currUserThresholds.setThNoThreadsH(1500);
		}
		this.currSimSetup.save();
	}
	
	/**
	 * Loads the current DistributionSetup from the current project
	 */
	private void setupLoad() {
		
		// --- Das akuelle DefaultListModel laden ---------
		this.currSimSetup = currProject.simSetups.getCurrSimSetup();
		if ( currSimSetup==null ) {
			currProject.simSetups.setupLoadAndFocus(SimulationSetups.SIMULATION_SETUP_LOAD, currProject.simSetupCurrent, false);
			currSimSetup = currProject.simSetups.getCurrSimSetup();
		}
		currDistributionSetup = this.currSimSetup.getDistributionSetup();
		currUserThresholds = currDistributionSetup.getUserThresholds();
		
		this.jCheckBoxDoLoadStatic.setSelected(currDistributionSetup.isDoStaticLoadBalalncing());
		this.jTextFieldAgentsExpected.setText(((Integer)currDistributionSetup.getNumberOfAgents()).toString());
		this.jTextFieldContainerExpected.setText(((Integer)currDistributionSetup.getNumberOfContainer()).toString());
		
		this.jCheckBoxDoLoadDynamic.setSelected(currDistributionSetup.isDoDynamicLoadBalalncing());
		this.jTextFieldDynamicLoadClass.setText(currDistributionSetup.getDynamicLoadBalancingClass());
		
		this.jCheckBoxThresholdDefinition.setSelected(currDistributionSetup.isUseUserThresholds());
		this.jTextFieldCpuLow.setText(((Integer)currUserThresholds.getThCpuL()).toString());
		this.jTextFieldCpuHigh.setText(((Integer)currUserThresholds.getThCpuH()).toString());
		this.jTextFieldMemLow.setText(((Integer)currUserThresholds.getThMemoL()).toString());
		this.jTextFieldMemHigh.setText(((Integer)currUserThresholds.getThMemoH()).toString());
		this.jTextFieldThreadsLow.setText(((Integer)currUserThresholds.getThNoThreadsL()).toString());
		this.jTextFieldThreadsHigh.setText(((Integer)currUserThresholds.getThNoThreadsH()).toString());
		
		this.jLabelCalculation.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
	
		Object trigger = ae.getSource();
		
		if (trigger==jButtonCalcContainer) {
			// --- calculate the number of container required -------
			int noAgents = Integer.parseInt(jTextFieldAgentsExpected.getText());
			int noAgentsMax = Integer.parseInt(jTextFieldThreadsHigh.getText());
			int noContainer = (int) Math.ceil(((float)noAgents / (float)noAgentsMax)) + 1;
			jTextFieldContainerExpected.setText(((Integer)noContainer).toString());
			String calculation = "Math.ceil(" + noAgents + " / " + noAgentsMax + ") + 1 = " + noContainer;
			jLabelCalculation.setText(calculation);
			
			currDistributionSetup.setNumberOfAgents(noAgents);
			currDistributionSetup.setNumberOfContainer(noContainer);
						
		} else if (trigger==jButtonDefaultClass) { 
			this.setDefaults(DEFAULT_VALUES_4_DynBalancingClass);
		} else if (trigger==jButtonDefaultThreshold) {
			this.setDefaults(DEFAULT_VALUES_4_ThresholdLevel);
		} else if (trigger==jCheckBoxDoLoadStatic) {
			currDistributionSetup.setDoStaticLoadBalalncing(jCheckBoxDoLoadStatic.isSelected());
		} else if (trigger==jCheckBoxDoLoadDynamic) {
			currDistributionSetup.setDoDynamicLoadBalalncing(jCheckBoxDoLoadDynamic.isSelected());
		} else if (trigger==jCheckBoxThresholdDefinition) {
			currDistributionSetup.setUseUserThresholds(jCheckBoxThresholdDefinition.isSelected());
		} else {
			//System.err.println("Action nicht implementiert: " + ae.getActionCommand());
		}
		this.currSimSetup.save();
	}
	
	@Override
	public void update(Observable o, Object notifyObject) {
		
		if ( notifyObject.toString().equalsIgnoreCase("SimSetups")) {
			this.setupLoad();
		} else {
			//System.out.println( this.getClass().getName() + ": " + arg1.toString() );	
		}
	}

	@Override
	public void keyTyped(KeyEvent kT) {
		
		char charackter = kT.getKeyChar();
		String SngChar = Character.toString(charackter);
		// --- Numbers only !!! -----------------
		if (SngChar.matches( "[0-9]") == false ) {
			kT.consume();	
			return;
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent kT) {
		
		// --- work on the datamodel ------------
		JTextField sourceText = (JTextField) kT.getSource();
		int number = 0;
		try {
			number = Integer.parseInt(sourceText.getText());
		} catch (NumberFormatException e) {
			number = 0;
		}
		
		if (sourceText==jTextFieldAgentsExpected) {
			currDistributionSetup.setNumberOfAgents(number);
		} else if (sourceText==jTextFieldContainerExpected) {
			currDistributionSetup.setNumberOfContainer(number);
			
		} else if (sourceText==jTextFieldCpuLow) {
			currUserThresholds.setThCpuL(number);
		} else if (sourceText==jTextFieldCpuHigh) {
			currUserThresholds.setThCpuH(number);
			
		} else if (sourceText==jTextFieldMemLow) {
			currUserThresholds.setThMemoL(number);
		} else if (sourceText==jTextFieldMemHigh) {
			currUserThresholds.setThMemoH(number);
			
		} else if (sourceText==jTextFieldThreadsLow) {
			currUserThresholds.setThNoThreadsL(number);
		} else if (sourceText==jTextFieldThreadsHigh) {
			currUserThresholds.setThNoThreadsH(number);
			
		} else {
			//System.err.println("Textfeld nicht implementiert: " + sourceText.getName());
		}
		this.currSimSetup.save();
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
