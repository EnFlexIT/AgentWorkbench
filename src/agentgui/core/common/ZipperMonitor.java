package agentgui.core.common;


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

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.CoreWindow;
import java.awt.Color;
import java.io.File;

public class ZipperMonitor extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final static String PathImage = Application.RunInfo.PathImageIntern();
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();

	private JPanel jContentPane = null;
	
	private JLabel jLabelProcess = null;
	private JProgressBar jProgressBarProcess = null;
	
	private JLabel jLabelSingleFile = null;
	private JButton jButtonCancel = null;
	private boolean canceled = false;
	
	private JLabel jLabelDummy = null;
	

	/**
	 * @param owner
	 */
	public ZipperMonitor(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(542, 194);
		this.setContentPane(getJContentPane());
		
		this.setTitle(Application.RunInfo.getApplicationTitle() + ": Zip-Monitor");
		this.setIconImage(imageAgentGUI);
		
		this.setLookAndFeel();
		
		this.setDefaultCloseOperation(CoreWindow.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setPreferredSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		
		// --- Übersetzungen festelgen ----
		jButtonCancel.setText(Language.translate("Abbruch"));
		
	}

	private void setLookAndFeel() {
		
		String lnfClassname = Application.RunInfo.AppLnF();
		try {
			if (lnfClassname == null) {
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}	
			UIManager.setLookAndFeel(lnfClassname);
			SwingUtilities.updateComponentTreeUI(this);				
		} 
		catch (Exception e) {
				System.err.println("Cannot install " + lnfClassname + " on this platform:" + e.getMessage());
		}		
	}
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.gridy = 5;
			jLabelDummy = new JLabel();
			jLabelDummy.setText("");
			jLabelDummy.setPreferredSize(new Dimension(100, 10));
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(15, 20, 0, 20);
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 2;
			jLabelSingleFile = new JLabel();
			jLabelSingleFile.setText("Datei:");
			jLabelSingleFile.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(15, 0, 0, 10);
			gridBagConstraints2.gridy = 4;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(20, 20, 0, 20);
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			jLabelProcess = new JLabel();
			jLabelProcess.setText("Entpacke Verzeichnis ...");
			jLabelProcess.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(10, 20, 0, 20);
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 1;
			
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJProgressBarProcess(), gridBagConstraints);
			jContentPane.add(jLabelProcess, gridBagConstraints1);
			jContentPane.add(getJButtonCancel(), gridBagConstraints2);
			jContentPane.add(jLabelSingleFile, gridBagConstraints3);
			jContentPane.add(jLabelDummy, gridBagConstraints4);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jProgressBarBenchmark	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBarProcess() {
		if (jProgressBarProcess == null) {
			jProgressBarProcess = new JProgressBar();
			jProgressBarProcess.setPreferredSize(new Dimension(200, 22));
		}
		return jProgressBarProcess;
	}
	public void setNumberOfFilesMax(int nuberOfFiles) {
		jProgressBarProcess.setMinimum(0);
		jProgressBarProcess.setMaximum(nuberOfFiles);
	}
	public void setNumberNextFile() {
		jProgressBarProcess.setValue(jProgressBarProcess.getValue()+1);
	}
	public void setProcessDescription(boolean zip, String srcName) {
		
		int maxLength = 60;
		String shortNameOfFileOrFolder = null;
		
		if (srcName.length()<=maxLength) {
			shortNameOfFileOrFolder = srcName;
			shortNameOfFileOrFolder = " '" + srcName + "':";
		} else {
			shortNameOfFileOrFolder = srcName.substring(srcName.length()-maxLength , srcName.length());
			shortNameOfFileOrFolder = " '..." + shortNameOfFileOrFolder + "':"; 
		}

		if (zip==true) {
			jLabelProcess.setText(Language.translate("Packe") + shortNameOfFileOrFolder);
		} else {
			jLabelProcess.setText(Language.translate("Entpacke") + shortNameOfFileOrFolder);
		}
	}
	public void setCurrentJobFile(String fileName) {
		File file = new File(fileName);
		jLabelSingleFile.setText(Language.translate("Datei:") + " '" + file.getName() + "':");
	}
	
	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Abbruch");
			jButtonCancel.setPreferredSize(new Dimension(134, 28));
			jButtonCancel.setForeground(new Color(204, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	/**
	 * @param canceld the canceld to set
	 */
	public void setCanceled(boolean canceld) {
		this.canceled = canceld;
	}
	/**
	 * @return the canceld
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	@Override
	public void actionPerformed(ActionEvent act) {

		Object actor = act.getSource();
		if (actor.equals(jButtonCancel)) {
			this.setCanceled(true);
		}
	}

	
}  //  @jve:decl-index=0:visual-constraint="59,16"
