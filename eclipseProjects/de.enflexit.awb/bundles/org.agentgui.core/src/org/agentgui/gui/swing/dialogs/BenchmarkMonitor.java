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
package org.agentgui.gui.swing.dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.agentgui.gui.AwbBenchmarkMonitor;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;

/**
 * This JDialog is used to display the progress of the benchmark during its runtime.<br>
 * Depending on the context, the benchmark can sometimes be skipped. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BenchmarkMonitor extends JDialog implements ActionListener, AwbBenchmarkMonitor {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Image imageAgentGUI = GlobalInfo.getInternalImageAwbIcon16();

	private JPanel jContentPane = null;
	private JPanel jPanelBottomRight = null;
	private JPanel jPanelBottomLeft = null;
	
	private JLabel jLabelBenchmark = null;
	private JLabel jLabelBenchmarkOldCaption = null;
	private JLabel jLabelBenchmarkOldValue = null;
	
	public JProgressBar jProgressBarBenchmark = null;
	
	public JButton jButtonSkip = null;
	public JButton jButtonSkipAlways = null;
	
	public boolean actionSkip = false;
	public boolean actionSkipAllways = false;

	
	/**
	 * Constructor of this class.
	 * @param owner the owner frame 
	 */
	public BenchmarkMonitor(Frame owner) {
		super(owner);
		initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setSize(506, 185);
		this.setContentPane(getJContentPane());
		
		this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": SciMark 2.0 - Benchmark");
		this.setIconImage(imageAgentGUI);
		
		this.setLookAndFeel();
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setPreferredSize(this.getSize());
		this.setLocationRelativeTo(null);
		
		// --- Translations ----
		jLabelBenchmark.setText(Language.translate("Bitte warten! Der Benchmark wird durchgeführt ..."));
		jLabelBenchmarkOldCaption.setText(Language.translate("Alter Wert: "));
		jButtonSkip.setText(Language.translate("Überspringen"));
		jButtonSkipAlways.setText(Language.translate("Immer überspringen"));
		
	}

	/**
	 * Sets the look and feel of the JDialog similar to the main application window.
	 */
	private void setLookAndFeel() {
		
		String lnfClassName = Application.getGlobalInfo().getAppLookAndFeelClassName();
		if (lnfClassName==null) return;
		
		String currLookAndFeelClassName = UIManager.getLookAndFeel().getClass().getName();
		if (lnfClassName.equals(currLookAndFeelClassName)==true) return;
		
		try {
			UIManager.setLookAndFeel(lnfClassName);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Cannot install " + lnfClassName + " on this platform:" + e.getMessage());
		}	
	}
	
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

	private JProgressBar getJProgressBarBenchmark() {
		if (jProgressBarBenchmark == null) {
			jProgressBarBenchmark = new JProgressBar();
			jProgressBarBenchmark.setPreferredSize(new Dimension(400, 26));
		}
		return jProgressBarBenchmark;
	}

	private JPanel getJPanelBottomLeft() {
		if (jPanelBottomLeft == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.NORTH;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints3.gridy = 0;
			jLabelBenchmarkOldValue = new JLabel();
			jLabelBenchmarkOldValue.setText(".......... Mflops");
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
	private JButton getJButtonSkipAlways() {
		if (jButtonSkipAlways == null) {
			jButtonSkipAlways = new JButton();
			jButtonSkipAlways.setText("Immer überspringen");
			jButtonSkipAlways.setFont(new Font("Dialog", Font.BOLD, 10));
			jButtonSkipAlways.addActionListener(this);
		}
		return jButtonSkipAlways;
	}

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
			jPanelBottomRight.add(getJButtonSkipAlways(), gridBagConstraints5);
		}
		return jPanelBottomRight;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent act) {

		Object actor = act.getSource();
		if (actor.equals(this.getJButtonSkip())) {
			this.actionSkip = true;
			jButtonSkip.setEnabled(false);
			jButtonSkipAlways.setEnabled(false);
			
		} else if (actor.equals(this.getJButtonSkipAlways())) {
			this.actionSkipAllways = true;
			jButtonSkip.setEnabled(false);
			jButtonSkipAlways.setEnabled(false);
		}
	}

	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#setEnableSkipButton(boolean)
	 */
	@Override
	public void setEnableSkipButton(boolean isEnabled) {
		this.getJButtonSkip().setEnabled(isEnabled);
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#isSkip()
	 */
	@Override
	public boolean isSkip() {
		return this.actionSkip;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#setEnableSkipAlwaysButton(boolean)
	 */
	@Override
	public void setEnableSkipAlwaysButton(boolean isEnabled) {
		this.getJButtonSkipAlways().setEnabled(isEnabled);
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#isSkipAlways()
	 */
	@Override
	public boolean isSkipAlways() {
		return this.actionSkipAllways;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#setProgressMinimum(int)
	 */
	@Override
	public void setProgressMinimum(int pMin) {
		this.getJProgressBarBenchmark().setMinimum(pMin);
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#setProgressMaximum(int)
	 */
	@Override
	public void setProgressMaximum(int pMax) {
		this.getJProgressBarBenchmark().setMaximum(pMax);
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#setProgressValue(int)
	 */
	@Override
	public void setProgressValue(int newValue) {
		this.getJProgressBarBenchmark().setValue(newValue);
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbBenchmarkMonitor#setBenchmarkValue(float)
	 */
	public void setBenchmarkValue(float benchmarkValue) {
		jLabelBenchmarkOldValue.setText(benchmarkValue +  " Mflops");
	}

}  
