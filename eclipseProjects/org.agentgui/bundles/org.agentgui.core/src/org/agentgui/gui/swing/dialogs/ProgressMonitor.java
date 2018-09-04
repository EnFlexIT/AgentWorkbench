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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.agentgui.gui.AwbProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitor;

import de.enflexit.common.Language;


/**
 * The Class ProgressMonitor can be used in order to display the progress of 
 * a time consuming process as for example a download or other.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProgressMonitor implements ActionListener, AwbProgressMonitor, IProgressMonitor {

	private JDesktopPane parentDesktopPane;
	
	private String windowTitle = "Progress Monitor";
	private String headerText = "Progress";
	private String progressText = "Download";
	
	private ImageIcon iconImage;
	private String lookAndFeelClassName;
	
	private Window owner;
	private Container progressMonitorContainer;

	private JPanel jContentPane;
	private JLabel jLabelHeader;
	private JPanel jPanelDummy;
	
	private JLabel jLabelProgress;
	private JProgressBar jProgressBarDownload;
	private JButton jButtonCancel;

	private boolean allow2Cancel = true;
	private boolean canceled = false;
	
	
	/**
	 * Instantiates a new progress monitor. In case that no parent desktop pane is specified, the
	 * class will use a JDialog to display the progress. Otherwise a JInternalFrame will be used. 
	 *
	 * @param windowTitle the window title - may be <code>null</code>
	 * @param headerText the header text - may be <code>null</code>
	 * @param progressText the progress text - may be <code>null</code>
	 * @param iconImage the icon image - may be <code>null</code>
	 * @param parentDesktopPane the parent desktop pane - may be <code>null</code>
	 * @param lookAndFeelClassName the look and feel class reference - may be <code>null</code>
	 */
	public ProgressMonitor(String windowTitle, String headerText, String progressText, ImageIcon iconImage, JDesktopPane parentDesktopPane, String lookAndFeelClassName) {
		if (windowTitle!=null)  this.windowTitle = windowTitle;
		if (headerText!=null)   this.headerText = headerText;
		if (progressText!=null) this.progressText = progressText;
		this.lookAndFeelClassName = lookAndFeelClassName;
		this.iconImage = iconImage;
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProgressMonitor#setOwner(java.awt.Window)
	 */
	public void setOwner(Window ownerWindow) {
		this.owner = ownerWindow;
	}
	
	/**
	 * Returns the progress monitor container that is either a JDialog or a JInternFrame.
	 * @return the progress monitor container
	 */
	private Container getProgressMonitorContainer() {
		if (progressMonitorContainer==null) {
			
			Dimension defaultSize = new Dimension(570, 200);
			if (this.parentDesktopPane==null) {
				JDialog jDialog = new JDialog(this.owner);	
				jDialog.setSize(defaultSize);
				jDialog.setResizable(false);
				if (this.owner==null) {
					jDialog.setAlwaysOnTop(true);
				}
				jDialog.setTitle(this.windowTitle);
				if (this.iconImage!=null) {
					jDialog.setIconImage(this.iconImage.getImage());	
				}
				jDialog.setContentPane(this.getJContentPane());
				jDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				this.progressMonitorContainer = jDialog; 
				this.setLookAndFeel();	
					
			} else {
				JInternalFrame jInternalFrame = new JInternalFrame();
				jInternalFrame.setSize(defaultSize);
				jInternalFrame.setResizable(false);
				
				jInternalFrame.setTitle(this.windowTitle);
				if (this.iconImage!=null) {
					jInternalFrame.setFrameIcon(this.iconImage);	
				}
				jInternalFrame.setContentPane(this.getJContentPane());
				jInternalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				this.progressMonitorContainer = jInternalFrame;
			}
			
		}
		return progressMonitorContainer;
	}
	
	/**
	 * Sets the progress dialog visible.
	 * @param visible the new visible
	 */
	public void setVisible(boolean visible) {
		if (this.getProgressMonitorContainer() instanceof JDialog) {
			((JDialog) this.getProgressMonitorContainer()).setVisible(visible);
			// --- Center dialog ----------------
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
			int top = (screenSize.height - this.getProgressMonitorContainer().getHeight()) / 2; 
		    int left = (screenSize.width - this.getProgressMonitorContainer().getWidth()) / 2; 
		    this.getProgressMonitorContainer().setLocation(left, top);
		    
		} else if (this.getProgressMonitorContainer() instanceof JInternalFrame) {
			JInternalFrame internalFrame = ((JInternalFrame) this.getProgressMonitorContainer());
			if (visible==true) {
				this.parentDesktopPane.add(internalFrame, JDesktopPane.POPUP_LAYER);
				// --- Center dialog ------------				
				int top = (this.parentDesktopPane.getHeight() - internalFrame.getHeight()) / 2; 
			    int left = (this.parentDesktopPane.getWidth() - internalFrame.getWidth()) / 2; 
			    internalFrame.setLocation(left, top);
			    try {
			    	internalFrame.moveToFront();
			    	internalFrame.setSelected(true);
			    	internalFrame.repaint();
				} catch (PropertyVetoException ex) {
					ex.printStackTrace();
				}
			}
			internalFrame.setVisible(visible);
		}
	}
	/**
	 * Checks if is visible.
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return this.getProgressMonitorContainer().isVisible();
	}
	/**
	 * Validate the progress monitor.
	 */
	public void validate() {
		this.getProgressMonitorContainer().validate();
	}
	/**
	 * Repaint the progress monitor.
	 */
	public void repaint() {
		this.getProgressMonitorContainer().repaint();
	}
	/**
	 * Dispose the progress monitor.
	 */
	public void dispose() {
		if (this.getProgressMonitorContainer() instanceof JDialog) {
			((JDialog) this.getProgressMonitorContainer()).dispose();
		}
	}
	
	/**
	 * Sets the window title.
	 * @param windowTitle the new window title
	 */
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
		this.setTitle(this.windowTitle);
	}
	/**
	 * Sets the header text.
	 * @param headerText the new header text
	 */
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
		this.getJLabelHeader().setText("<html>" + headerText + "</html>");
	}
	/**
	 * Sets the progress text.
	 * @param progressText the new progress text
	 */
	public void setProgressText(String progressText) {
		this.progressText = progressText;
		this.getJLabelProgress().setText("<html>" + progressText + "</html>");
	}
	/**
	 * Can be used to set the progress in percent (0 - 100).
	 * @param progress the new progress in percent
	 */
	public void setProgress(int progress) {
		this.getJProgressBarDownload().setValue(progress);
		this.setTitle(ProgressMonitor.this.windowTitle + " (" + progress + "%)");
	}
	/**
	 * Sets the title.
	 * @param title the new title
	 */
	private void setTitle(String title) {
		Container progressCont = this.getProgressMonitorContainer();
		if (progressCont instanceof JDialog) {
			((JDialog) progressCont).setTitle(title);
		} else if (progressCont instanceof JInternalFrame) {
			((JInternalFrame) progressCont).setTitle(title);
		}
	}

	/**
	 * Sets the permission to cancel an action via the progress dialog.
	 */
	public void setAllow2Cancel(boolean allow2Cancel) {
		this.allow2Cancel = allow2Cancel;
		if (this.allow2Cancel==true) {
			this.getJButtonCancel().setEnabled(true);
		} else {
			this.getJButtonCancel().setEnabled(false);
		}
	}
	/**
	 * Checks if this dialog was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return this.canceled;
	}
	
	/**
	 * Sets the look and feel of the dialog similar to the current application window
	 */
	private void setLookAndFeel() {

		// --- Some exit options --------------------------
		if (this.lookAndFeelClassName==null) return;
		
		String currLookAndFeelClassName = UIManager.getLookAndFeel().getClass().getName();
		if (this.lookAndFeelClassName.equals(currLookAndFeelClassName)==true) return;

		// --- Try to set the look and feel ---------------
		try {
			UIManager.setLookAndFeel(this.lookAndFeelClassName);
			SwingUtilities.updateComponentTreeUI(this.getProgressMonitorContainer());				
		} catch (Exception ex) {
			System.err.println("Cannot install " + this.lookAndFeelClassName + " on this platform:" + ex.getMessage());
		}	
	}
	
	/**
	 * This method initializes jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridy = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(20, 0, 15, 0);
			gridBagConstraints3.gridy = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(0, 20, 5, 20);
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(0, 20, 0, 20);
			gridBagConstraints1.weighty = 0.0;
			gridBagConstraints1.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(15, 20, 10, 20);
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 0;
			
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(this.getJLabelHeader(), gridBagConstraints);
			jContentPane.add(this.getJProgressBarDownload(), gridBagConstraints1);
			jContentPane.add(this.getJLabelProgress(), gridBagConstraints2);
			jContentPane.add(this.getJButtonCancel(), gridBagConstraints3);
			jContentPane.add(this.getJPanelDummy(), gridBagConstraints11);
		}
		return jContentPane;
	}

	private JLabel getJLabelHeader() {
		if (jLabelHeader==null) {
			jLabelHeader = new JLabel();
			jLabelHeader.setText(this.headerText);
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 13));
			jLabelHeader.setPreferredSize(new Dimension(148, 28));
			jLabelHeader.setMinimumSize(new Dimension(148, 28));
			jLabelHeader.setMaximumSize(new Dimension(148, 28));
		}
		return jLabelHeader;
	}
	
	private JLabel getJLabelProgress() {
		if (jLabelProgress==null) {
			jLabelProgress = new JLabel();
			jLabelProgress.setText(this.progressText);
			jLabelProgress.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelProgress.setVerticalAlignment(JLabel.TOP);
			jLabelProgress.setVerticalTextPosition(JLabel.TOP);
			jLabelProgress.setPreferredSize(new Dimension(148, 36));
			jLabelProgress.setMinimumSize(new Dimension(148, 36));
			jLabelProgress.setMaximumSize(new Dimension(148, 36));
		}
		return jLabelProgress;
	}
	/**
	 * This method initializes jProgressBarDownload.
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBarDownload() {
		if (jProgressBarDownload == null) {
			jProgressBarDownload = new JProgressBar();
			jProgressBarDownload.setPreferredSize(new Dimension(148, 26));
			jProgressBarDownload.setMinimumSize(new Dimension(148, 26));
			jProgressBarDownload.setMaximumSize(new Dimension(148, 26));
			jProgressBarDownload.setStringPainted(true);
			jProgressBarDownload.setMinimum(0);
			jProgressBarDownload.setMaximum(100);
		}
		return jProgressBarDownload;
	}

	/**
	 * This method initializes jButtonCancel.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(Language.translate("Abbruch"));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setPreferredSize(new Dimension(100, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
		}
		return jPanelDummy;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonCancel()) {
			this.canceled=true;
			this.setVisible(false);
		}
	}

	// --------------------------------------------------------------
	// --- From here, the methods from the IProgressMonitor ---------
	// --------------------------------------------------------------
	private int totalWork;
	private int progress;
	@Override
	public void beginTask(String name, int totalWork) {
		this.setTaskName(name);
		this.totalWork = totalWork;
	}
	@Override
	public void done() {
		this.setProgress(this.totalWork);
		this.setVisible(false);
	}
	@Override
	public void setCanceled(boolean value) {
		if (value==true) {
			this.canceled=true;
			this.setVisible(false);
		}
	}
	@Override
	public void setTaskName(String name) {
		if (name!=null && name.isEmpty()==false) {
			this.setHeaderText(name);
		}
	}
	@Override
	public void subTask(String name) {
		if (name!=null && name.isEmpty()==false) {
			this.setProgressText(name);
		}
	}
	@Override
	public void worked(int work) {
		this.progress+=work;
		Long percentLong = Math.round(((double)this.progress/this.totalWork)*100.0);
		this.setProgress(percentLong.intValue());
	}
	@Override
	public void internalWorked(double work) { }

}  
