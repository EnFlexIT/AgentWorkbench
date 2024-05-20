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
package de.enflexit.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * The Class AwbUiAdjustments.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbLookAndFeelAdjustments {

	public final static String DEFAUL_LOOK_AND_FEEL_CLASS = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

	public enum AwbLook {
		Default,
		Light,
		Dark
	}
	
	private static AwbLook awbLook = AwbLook.Default;
	
	private static boolean isFurtherLaFToInstall = false;
	private static boolean isFurtherLaFInstalled = false;
	
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
	 * Returns all installed look and feels.
	 * @return the installed look and feels
	 */
	public static LookAndFeelInfo[] getInstalledLookAndFeels() {
		
		// --- Install further LookAndFeels ---------------
		if (isFurtherLaFToInstall==true && isFurtherLaFInstalled==false) {
			AwbLookAndFeelAdjustments.installLookAndFeels();
		}
		
		// --- Sort list of installed LookAndFeels --------
		LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
		Arrays.sort(lookAndFeels, new Comparator<LookAndFeelInfo>() {
			@Override
			public int compare(LookAndFeelInfo laf1, LookAndFeelInfo laf2) {
				return laf1.getName().compareTo(laf2.getName());
			}
		});
		return lookAndFeels;
	}

	/**
	 * Installs further look and feels.
	 */
	private static void installLookAndFeels() {
		
		try {
			FlatLightLaf.installLafInfo();
			FlatDarculaLaf.installLafInfo();
			FlatDarkLaf.installLafInfo();
			FlatIntelliJLaf.installLafInfo();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			isFurtherLaFInstalled = true;
		}
	}
	
	
	/**
	 * Sets the look and feel to the swing UI Manager.
	 *
	 * @param lafClassNameNew the new look and feel
	 * @param invoker the visual invoker of this method
	 */
	public static void setLookAndFeel(String lafClassNameNew, Component invoker) {
		
		if (GraphicsEnvironment.isHeadless()==true) return;
		if (lafClassNameNew==null || lafClassNameNew.isEmpty()==true) return;
		
		String lafClassNameOld = null;
		try {
			// --- Check against old LookAndFeel first --------------
			lafClassNameOld = UIManager.getLookAndFeel().getClass().getName();
			if (lafClassNameNew.equals(lafClassNameOld)==false) {
				// --- Set the new LookAndFeel ----------------------
				UIManager.setLookAndFeel(lafClassNameNew);
				AwbLookAndFeelAdjustments.doLookAndFeelAdjustments();
				if (invoker!=null) {
					SwingUtilities.updateComponentTreeUI(invoker);
				}
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException lnfEx) {
			System.err.println("[" + AwbLookAndFeelAdjustments.class.getSimpleName() + "] Cannot install " + lafClassNameNew + " on this platform:" + lnfEx.getMessage());
			// --- Switch back to previous LookAndFeel --------------
			if (lafClassNameOld!=null) {
				try {
					UIManager.setLookAndFeel(lafClassNameOld);
					AwbLookAndFeelAdjustments.doLookAndFeelAdjustments();
					if (invoker!=null) {
						SwingUtilities.updateComponentTreeUI(invoker);
					}
					
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
					System.err.println("[" + AwbLookAndFeelAdjustments.class.getSimpleName() + "] Cannot revert to  " + lafClassNameOld + " on this platform:" + lnfEx.getMessage());
					ex.printStackTrace();
				}
			}

		}
	}
	
	/**
	 * Does the LookAndFeel adjustments, depending on the currently configured LookAndFeel.
	 */
	public static void doLookAndFeelAdjustments() {
		
		if (GraphicsEnvironment.isHeadless()==true) return;
		
		String lafClassName = UIManager.getLookAndFeel().getClass().getName();
		if (lafClassName.equals(DEFAUL_LOOK_AND_FEEL_CLASS)==true) {
			// --- Nimbus -----------------------
			doLookAndFeelAdjustmentsForNimbus();
		} else if (lafClassName.startsWith("com.formdev.flatlaf.")==true) {
			// --- FlatLaF ----------------------
			doLookAndFeelAdjustmentsForFlatLaf();
		}
	}

	/**
	 * Do look and feel adjustments for the FlatLaF LookAndFeel.
	 */
	private static void doLookAndFeelAdjustmentsForFlatLaf() {
		
		UIManager.getLookAndFeelDefaults().put("TabbedPaneUI", AwbFlatLafTabbedPaneUI.class.getName());
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
		// --- Splash color: new Color(20, 130, 172)
		// ----------------------------------------------------------------------------------------------------------------------
		
		switch (getAwbLook()) {
		case Light:
			AwbLookAndFeelAdjustments.doLookAndFeelAdjustmentsLightMode();
			break;
			
		case Dark:
			AwbLookAndFeelAdjustments.doLookAndFeelAdjustmentsDarkMode();
			break;

		default:
			AwbLookAndFeelAdjustments.doLookAndFeelAdjustmentsDefaultMode();
			break;
		}
	}

	/**
	 * Do look and feel adjustments for the default mode.
	 */
	private static void doLookAndFeelAdjustmentsDefaultMode() {
		
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
		
		// --- Do adjustments for TabbedPaneUI ----------------------
		UIManager.getLookAndFeelDefaults().put("TabbedPaneUI", AwbNimbusTabbedPaneUI.class.getName());
		
	}
	
	/**
	 * Do look and feel adjustments for the light mode.
	 */
	private static void doLookAndFeelAdjustmentsLightMode() {
		
		Color splashColor = new Color(20, 130, 172); 
		
		UIManager.put("nimbusBase", new Color(240, 240, 240));
		UIManager.put("nimbusBlueGrey", new Color(240, 240, 240));
		UIManager.put("control", new Color(240, 240, 240));
		UIManager.put("nimbusOrange", splashColor);

		UIManager.put("nimbusSelectionBackground", splashColor);
		TableCellColorHelper.setTableHighlightColor(splashColor);
		
		UIManager.put("MenuBar:Menu[Selected].textForeground", splashColor);
		UIManager.put("MenuItem[MouseOver].textForeground", splashColor);
		
		// --- Test area ----
		//UIManager.put("Tree.selectionBackground", new Color(20, 130, 172));
		//UIManager.put("Tree.textBackground", new Color(20, 130, 172));
		
//		UIManager.put("SplitPane:SplitPaneDivider[Enabled].backgroundPainter", new AwbSplitPaneDividerPainter(null, 1));
//		UIManager.put("SplitPane:SplitPaneDivider[Focused].backgroundPainter", new AwbSplitPaneDividerPainter(null, 2));
//		UIManager.put("SplitPane:SplitPaneDivider[Enabled].foregroundPainter", new AwbSplitPaneDividerPainter(null, 3));
//		UIManager.put("SplitPane:SplitPaneDivider[Enabled+Vertical].foregroundPainter", new AwbSplitPaneDividerPainter(null, 4));
		
	}
	
	/**
	 * Do look and feel adjustments for the dark mode.
	 */
	private static void doLookAndFeelAdjustmentsDarkMode() {
		
		
	}

	
	// ------------------------------------------------------------------
	// --- Some general help functions for the current LookAndFeel ------
	// ------------------------------------------------------------------	
	/**
	 * Prints the current look and feel properties.
	 *
	 * @param filterPhrases [optional] filter phrases
	 */
	public static void printCurrentLookAndFeelProperties(String ... filterPhrases) {
		
		List<String> propNameList = new ArrayList<String>();
		
		UIDefaults uiDefaults = UIManager.getLookAndFeel().getDefaults();
		for (Object key : uiDefaults.keySet()) {
			// --- Filter for String 'key' ----------------
			if (key instanceof String==false) continue;

			// --- Matches filter phrases ? ---------------
			String keyString = (String) key;
			if (isMatchingFilter(keyString, filterPhrases)) {
				propNameList.add((String)key);
			}
		}
		Collections.sort(propNameList);
		
		for (String key : propNameList) {
			Object value = uiDefaults.get(key);
			System.out.println(key + " \t\t " + value.toString());
		}
	}
	/**
	 * Checks if the specified key is matching the specified filter phrases.
	 *
	 * @param key the key
	 * @param filterPhrases the filter phrases
	 * @return true, if is matching filter
	 */
	private static boolean isMatchingFilter(String key, String ... filterPhrases) {
		
		if (key==null | key.length()==0) return false;
		if (filterPhrases==null || filterPhrases.length==0) return true;
		
		for (String filterPhrase : filterPhrases) {
			if (key.toLowerCase().contains(filterPhrase.toLowerCase())==true) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the current Look and feel is a dark look and feel.
	 * @return true, if is dark look and feel
	 */
	public static boolean isDarkLookAndFeel() {
		
		boolean isDark = false;
		boolean isDebug = false;
		
		// --- Get text colors from UiManager -------------
		List<String> colorNameList = new ArrayList<String>();
		
		UIDefaults uiDefaults = UIManager.getLookAndFeel().getDefaults();
		for (Object key : uiDefaults.keySet()) {
			
			// --- Filter for String 'text' ---------------
			if (key instanceof String==false) continue;
			String keyString = ((String)key).toLowerCase();
			if (keyString.contains("text")==false) continue;
			if (keyString.contains("background")==true) continue;
			if (keyString.contains("foreground")==true) continue;
			if (keyString.contains("disable")==true) continue;
			if (keyString.contains("textarea")==true) continue;
			if (keyString.contains("textfield")==true) continue;
			if (keyString.contains("textpane")==true) continue;
			if (keyString.contains("high")==true) continue;

			// --- Check if value is a color -------------- 
			Object value = uiDefaults.get(key);
			if (value instanceof Color==false) continue;
			
			colorNameList.add((String) key);
		}

		if (isDebug==true) {
			Collections.sort(colorNameList);
			System.out.println("Color named with text: " + colorNameList.size());
		}
		
		// --- Calculate average luminance ----------------
		double luminanceAvg = 0;
		for (String  colorName : colorNameList) {
			// --- Calculate simgle luminance -------------
			Color color = uiDefaults.getColor(colorName);
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			double luminance = 0.2126*r + 0.7152*g + 0.0722*b;
			luminanceAvg += luminance;
			if (isDebug==true) {
				boolean isDarkTextColor = luminance < 128 ? true : false;
				System.out.println("Color Name: " + colorName + ", RGB(" + r +", " + g + ", " + b + "), Luminance: " + luminance + ", is dark text color: " + isDarkTextColor);
			}
		}
		if (isDebug==true) System.out.println();

		// --- Final calculation --------------------------
		if (colorNameList.size()>0) {
			luminanceAvg = luminanceAvg / colorNameList.size();
			boolean isDarkAvgTextColor = luminanceAvg < 128 ? true : false;
			return !isDarkAvgTextColor;
		}
		return isDark;
	}
	
	
}
