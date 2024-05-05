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
package org.agentgui.gui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import agentgui.core.config.GlobalInfo;

/**
 * The Class AwbUiAdjustments.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbLookAndFeelAdjustments {

	public enum AwbLook {
		Default,
		Light,
		Dark
	}
	
	private static AwbLook awbLook = AwbLook.Default;
	
	/**
	 * Returns the current AWB look.
	 * @return the AWB look
	 * @see AwbLook
	 */
	public static AwbLook getAwbLook() {
		return awbLook;
	}
	/**
	 * Sets the new AWB look.
	 * @param newAwbLook the new AWB look
	 */
	public static void setAwbLook(AwbLook newAwbLook) {
		awbLook = newAwbLook;
	}
	
	/**
	 * Sets the look and feel to the swing UI Manager.
	 *
	 * @param lnfClassName the new look and feel
	 * @param invoker the visual invoker of this method
	 */
	public static void setLookAndFeel(String lnfClassName, Component invoker) {
		
		if (GraphicsEnvironment.isHeadless()==true) return;
		if (lnfClassName==null || lnfClassName.isEmpty()==true) return;
		
		try {
			// --- Get the old look and feel first ------------------
			LookAndFeel lnfOld = UIManager.getLookAndFeel();
			String lnfClassNameOld = lnfOld.getClass().getName();
			if (lnfClassName.equals(lnfClassNameOld)==false) {
				UIManager.setLookAndFeel(lnfClassName);
				doLookAndFeelAdjustments();
				if (invoker!=null) {
					SwingUtilities.updateComponentTreeUI(invoker);
				}
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException lnfEx) {
			System.err.println("Cannot install " + lnfClassName + " on this platform:" + lnfEx.getMessage());
			//lnfEx.printStackTrace();
		}
	}
	
	/**
	 * Does the LookAndFeel adjustments, depending on the currently configured LookAndFeel.
	 */
	public static void doLookAndFeelAdjustments() {
		
		if (GraphicsEnvironment.isHeadless()==true) return;
		
		LookAndFeel lnf = UIManager.getLookAndFeel();
		if (lnf.getClass().getName().equals(GlobalInfo.DEFAUL_LOOK_AND_FEEL_CLASS)==true) {
			doLookAndFeelAdjustmentsForNimbus();
		}
	}

	/**
	 * Do look and feel adjustments for the Nimbus LookAndFeel.
	 */
	private static void doLookAndFeelAdjustmentsForNimbus() {
		
		// ----------------------------------------------------------------------------------------------------------------------
		// --- From https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/lookandfeel/color.html
		// ----------------------------------------------------------------------------------------------------------------------
		// --- From Oracle These three base colors, nimbusBase, nimbusBlueGrey, and control, will address most of your needs. 
		// --- See a full list of color keys and their default values on the Nimbus Defaults page:
		// --- https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/lookandfeel/_nimbusDefaults.html#primary
		// ----------------------------------------------------------------------------------------------------------------------
		// --- New splash color: new Color(20, 130, 172)
		switch (getAwbLook()) {
		case Light:
			UIManager.put("nimbusBase", new Color(240, 240, 240));
			UIManager.put("nimbusBlueGrey", new Color(240, 240, 240));
			UIManager.put("control", new Color(240, 240, 240));
			
			UIManager.put("MenuBar:Menu[Selected].textForeground", new Color(20, 130, 172));
			
			
			break;
			
		case Dark:
			
			break;

		default:
			break;
		}
		
		// --- Do adjustments for TabbedPane ------------------------ 
		UIManager.put("TabbedPane.focus", Color.GRAY);
		
		UIManager.put("TabbedPane.highlight", Color.WHITE);
		UIManager.put("TabbedPane.lightHighlight", Color.LIGHT_GRAY);
		
		UIManager.put("TabbedPane.shadow", Color.LIGHT_GRAY);
		UIManager.put("TabbedPane.darkShadow", Color.LIGHT_GRAY);

		// --- Do adjustments for ProgressBar -----------------------
		AwbProgressBarPainter painter = new AwbProgressBarPainter(new Color(161, 198, 231), new Color(91, 155, 213), Color.GRAY);
		UIManager.getLookAndFeelDefaults().put("ProgressBar[Enabled].foregroundPainter", painter);
		UIManager.getLookAndFeelDefaults().put("ProgressBar[Enabled+Finished].foregroundPainter", painter);		
		
	}

}
