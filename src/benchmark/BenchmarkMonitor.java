package benchmark;

import gui.CoreWindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import application.Application;
import application.Language;

public class BenchmarkMonitor extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final static String PathImage = Application.RunInfo.PathImageIntern();
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();

	private JPanel jContentPane = null;
	private JPanel jPanelBottomRight = null;
	
	private JLabel jLabelBenchmark = null;
	private JPanel jPanelBottomLeft = null;
	private JLabel jLabelBenchmarkOldCaption = null;
	private JLabel jLabelBenchmarkOldValue = null;
	
	public JProgressBar jProgressBarBenchmark = null;
	public JButton jButtonSkip = null;
	public JButton jButtonSkipAllways = null;
	public boolean actionSkip = false;
	public boolean actionSkipAllways = false;
	

	/**
	 * @param owner
	 */
	public BenchmarkMonitor(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(485, 185);
		this.setContentPane(getJContentPane());
		
		this.setTitle(Application.RunInfo.AppTitel() + ": SciMark 2.0 - Benchmark");
		this.setIconImage(imageAgentGUI);
		
		this.setLookAndFeel();
		
		this.setDefaultCloseOperation(CoreWindow.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setPreferredSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		
		// --- Übersetzungen festelgen ----
		jLabelBenchmark.setText(Language.translate("Bitte warten! Benchmark wird durchgeführt ..."));
		jLabelBenchmarkOldCaption.setText(Language.translate("Alter Wert: "));
		jButtonSkip.setText(Language.translate("Überspringen"));
		jButtonSkipAllways.setText(Language.translate("Immer Überspringen"));
		
	}

	private void setLookAndFeel() {
		
		try {
			String lnfClassname = Application.RunInfo.AppLnF();
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lnfClassname);
				SwingUtilities.updateComponentTreeUI(this);				
		} 
		catch (Exception e) {
				System.err.println("Cannot install " + Application.RunInfo.AppLnF()
					+ " on this platform:" + e.getMessage());
		}		
	}
	
	public void setBenchmarkValue(Float benchmarkValue) {
		jLabelBenchmarkOldValue.setText( benchmarkValue +  " Mflops");
	}
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = GridBagConstraints.CENTER;
			gridBagConstraints11.insets = new Insets(0, 10, 20, 20);
			gridBagConstraints11.ipadx = 0;
			gridBagConstraints11.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.insets = new Insets(0, 20, 20, 10);
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 0.0;
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(20, 20, 0, 20);
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.gridy = 0;
			jLabelBenchmark = new JLabel();
			jLabelBenchmark.setText("Bitte warten! Benchmark wird durchgeführt ...");
			jLabelBenchmark.setFont(new Font("Dialog", Font.BOLD, 14));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(15, 20, 20, 20);
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 1;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJProgressBarBenchmark(), gridBagConstraints);
			jContentPane.add(jLabelBenchmark, gridBagConstraints1);
			jContentPane.add(getJPanelBottomLeft(), gridBagConstraints2);
			jContentPane.add(getJPanelBottomRight(), gridBagConstraints11);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jProgressBarBenchmark	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBarBenchmark() {
		if (jProgressBarBenchmark == null) {
			jProgressBarBenchmark = new JProgressBar();
			jProgressBarBenchmark.setPreferredSize(new Dimension(400, 26));
		}
		return jProgressBarBenchmark;
	}

	/**
	 * This method initializes jPanelBottomLeft	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottomLeft() {
		if (jPanelBottomLeft == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.NORTH;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints3.gridy = 0;
			jLabelBenchmarkOldValue = new JLabel();
			jLabelBenchmarkOldValue.setText(".... Mflops");
			jLabelBenchmarkOldCaption = new JLabel();
			jLabelBenchmarkOldCaption.setText("Alter Wert: ");
			jLabelBenchmarkOldCaption.setFont(new Font("Dialog", Font.BOLD, 12));
			jPanelBottomLeft = new JPanel();
			jPanelBottomLeft.setLayout(new GridBagLayout());
			jPanelBottomLeft.add(jLabelBenchmarkOldCaption, gridBagConstraints6);
			jPanelBottomLeft.add(jLabelBenchmarkOldValue, gridBagConstraints3);
		}
		return jPanelBottomLeft;
	}

	/**
	 * This method initializes jButtonSkip	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSkip() {
		if (jButtonSkip == null) {
			jButtonSkip = new JButton();
			jButtonSkip.setText("Überspringen");
			jButtonSkip.setPreferredSize(new Dimension(134, 24));
			jButtonSkip.setFont(new Font("Dialog", Font.BOLD, 10));
			jButtonSkip.addActionListener(this);
		}
		return jButtonSkip;
	}

	/**
	 * This method initializes jButtonSkipAllways	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSkipAllways() {
		if (jButtonSkipAllways == null) {
			jButtonSkipAllways = new JButton();
			jButtonSkipAllways.setText("Immer Überspringen");
			jButtonSkipAllways.setFont(new Font("Dialog", Font.BOLD, 10));
			jButtonSkipAllways.addActionListener(this);
		}
		return jButtonSkipAllways;
	}

	/**
	 * This method initializes jPanelBottomRight	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottomRight() {
		if (jPanelBottomRight == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = -1;
			gridBagConstraints5.gridy = -1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints4.gridy = -1;
			jPanelBottomRight = new JPanel();
			jPanelBottomRight.setLayout(new GridBagLayout());
			jPanelBottomRight.add(getJButtonSkip(), gridBagConstraints4);
			jPanelBottomRight.add(getJButtonSkipAllways(), gridBagConstraints5);
		}
		return jPanelBottomRight;
	}

	@Override
	public void actionPerformed(ActionEvent act) {

		Object actor = act.getSource();
		if (actor.equals(jButtonSkip)) {
			this.actionSkip = true;
			jButtonSkip.setEnabled(false);
			jButtonSkipAllways.setEnabled(false);
		} else if (actor.equals(jButtonSkipAllways)) {
			this.actionSkipAllways = true;
			jButtonSkip.setEnabled(false);
			jButtonSkipAllways.setEnabled(false);
		}
	}

	
}  //  @jve:decl-index=0:visual-constraint="59,16"
