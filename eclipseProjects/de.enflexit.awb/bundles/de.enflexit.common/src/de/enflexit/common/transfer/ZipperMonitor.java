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
package de.enflexit.common.transfer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.enflexit.common.Language;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * The ZipperMonitor class will be used within the Zipper-class of this package.<br>
 * It will show the progress of zipping or unzipping a folder structure and will<br>
 * be closed after the process ends up.
 * 
 * @see Zipper
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ZipperMonitor extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private String appName;
	private Image iconImage;
	private String lookAndFeelClassName;

	private JPanel jContentPane;
	
	private JLabel jLabelProcess;
	private JProgressBar jProgressBarProcess;
	
	private JLabel jLabelSingleFile;
	private JButton jButtonCancel;
	private boolean canceled = false;
	
	private JLabel jLabelDummy;
	

	/**
	 * Constructor of this class.
	 *
	 * @param owner the Frame from which the dialog is displayed
	 * @param applicationName the current application name
	 * @param iconImage the icon image that can be used as a decorator for the ZipperMonitor
	 * @param lookAndFeelClassName the look and feel class name or reference
	 */
	public ZipperMonitor(Frame owner, String applicationName, Image iconImage, String lookAndFeelClassName) {
		super(owner);
		this.appName = applicationName;
		this.iconImage = iconImage;
		this.lookAndFeelClassName = lookAndFeelClassName;
		this.initialize();
	}
	/**
	 * Initialize
	 */
	private void initialize() {
		
		this.setSize(542, 194);
		this.setContentPane(getJContentPane());
		
		if (this.appName!=null) {
			this.setTitle(this.appName + ": Zip-Monitor");	
		} else {
			this.setTitle("Zip-Monitor");
		}
		this.setIconImage(this.iconImage);
		this.setAlwaysOnTop(true);
		this.setLookAndFeel();
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setPreferredSize(this.getSize());
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		
		
		// --- Translate ----
		this.getJButtonCancel().setText(Language.translate("Abbruch"));
		
	}
	
	/**
	 * Sets the look and feel of the dialog similar to the core application window
	 */
	private void setLookAndFeel() {
		
		// --- Some exit options --------------------------
		if (this.lookAndFeelClassName==null) return;
		
		String currLookAndFeelClassName = UIManager.getLookAndFeel().getClass().getName();
		if (this.lookAndFeelClassName.equals(currLookAndFeelClassName)==true) return;

		// --- Try to set the look and feel ---------------
		try {
			UIManager.setLookAndFeel(this.lookAndFeelClassName);
			SwingUtilities.updateComponentTreeUI(this);				
		} catch (Exception ex) {
			System.err.println("Cannot install " + this.lookAndFeelClassName + " on this platform:" + ex.getMessage());
		}	
	}
	
	/**
	 * This method initialises jContentPane
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
	 * This method initialises jProgressBarBenchmark	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBarProcess() {
		if (jProgressBarProcess == null) {
			jProgressBarProcess = new JProgressBar();
			jProgressBarProcess.setPreferredSize(new Dimension(200, 22));
		}
		return jProgressBarProcess;
	}
	
	/**
	 * This method can be used in order to set the maximum number of files to be handled .
	 * @param numberOfFiles the number of files to handle
	 */
	public void setNumberOfFilesMax(int numberOfFiles) {
		jProgressBarProcess.setMinimum(0);
		jProgressBarProcess.setMaximum(numberOfFiles);
	}
	/**
	 * With this method it can be indicated, that the next file is in progress.<br> 
	 * The progress bar will change then.
	 */
	public void setNumberNextFile() {
		jProgressBarProcess.setValue(jProgressBarProcess.getValue()+1);
	}
	/**
	 * With this method it can be textual shown, what the process is doing in the moment  
	 * @param zip Use 'true' if you are packing to a zip; otherwise 'false'
	 * @param srcName The current file on which we are working in the moment 
	 */
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
	
	/**
	 * Set the file you are currently working on here.
	 * @param fileName the currents job file name
	 */
	public void setCurrentJobFile(String fileName) {
		File file = new File(fileName);
		jLabelSingleFile.setText(Language.translate("Datei:") + " '" + file.getName() + "':");
	}
	
	/**
	 * This method initialises jButtonCancel	
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
	 * Use this in order to indicate, that the process has to be stopped
	 * @param canceld true or false !
	 */
	public void setCanceled(boolean canceld) {
		this.canceled = canceld;
	}
	/**
	 * If the packing or unpacking process was interrupted by the user, this method will return true 
	 * @return Returns true or false ;-)
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * The action method coming from the ActionListener
	 */
	@Override
	public void actionPerformed(ActionEvent act) {

		Object actor = act.getSource();
		if (actor.equals(this.getJButtonCancel())) {
			this.setCanceled(true);
		}
	}

	
} 
