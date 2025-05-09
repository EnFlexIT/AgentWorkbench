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

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * The Class AwbLookAndFeelAdjustments.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbLookAndFeelAdjustments {

	public final static String DEFAUL_LOOK_AND_FEEL_CLASS = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	public final static String LIGHT_LOOK_AND_FEEL_CLASS  = FlatLightLaf.class.getName();
	public final static String DARK_LOOK_AND_FEEL_CLASS   = FlatDarkLaf.class.getName();
	
	private static boolean isFurtherLaFToInstall = true;
	private static boolean isFurtherLaFInstalled = false;
	
	
	/**
	 * Returns all installed look and feels.
	 * @return the installed look and feels
	 */
	public static List<AwbLookAndFeelInfo> getAvailableLookAndFeels() {
		
		// --- Install further LookAndFeels --------------------------
		if (isFurtherLaFToInstall==true && isFurtherLaFInstalled==false) {
			AwbLookAndFeelAdjustments.installLookAndFeels();
		}
		
		// --- Sort list of installed LookAndFeels -------------------
		LookAndFeelInfo[] lafArray = UIManager.getInstalledLookAndFeels();
		Arrays.sort(lafArray, new Comparator<LookAndFeelInfo>() {
			@Override
			public int compare(LookAndFeelInfo laf1, LookAndFeelInfo laf2) {
				return laf1.getName().compareTo(laf2.getName());
			}
		});
		
		// --- Prepare the list of available LookAndFeels -----------
		List<AwbLookAndFeelInfo> lafList = new ArrayList<>();
		for (LookAndFeelInfo lafInfo : lafArray) {
			if (lafInfo.getClassName().equals(FlatLightLaf.class.getName())==true) {
				lafList.add(new AwbLookAndFeelInfo(lafInfo, "Light Theme"));
			} else if (lafInfo.getClassName().equals(FlatDarkLaf.class.getName())==true) {
				lafList.add(new AwbLookAndFeelInfo(lafInfo, "Dark Theme"));
			} else if (lafInfo.getClassName().equals(DEFAUL_LOOK_AND_FEEL_CLASS)==true) {
				lafList.add(new AwbLookAndFeelInfo(lafInfo, "Nimbus (Legacy)"));
			}
		}
		return lafList;
	}

	/**
	 * Installs further look and feels.
	 */
	private static void installLookAndFeels() {
		
		try {
			FlatLightLaf.installLafInfo();
			FlatDarkLaf.installLafInfo();
			
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
			}
			if (invoker!=null) {
				SwingUtilities.updateComponentTreeUI(invoker);
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
	 * Checks if the current Look and feel is a dark look and feel.
	 * @return true, if is dark look and feel
	 */
	public static boolean isDarkLookAndFeel() {
		
		boolean isDark = false;
		if (UIManager.getLookAndFeel().getClass().getName().equals(FlatDarkLaf.class.getName())==true) {
			isDark = true;
		}
		return isDark;
	}
	
	/**
	 * Does the LookAndFeel adjustments, depending on the currently configured LookAndFeel.
	 */
	public static void doLookAndFeelAdjustments() {
		
		if (GraphicsEnvironment.isHeadless()==true) return;
		
		// --- By default, remove custom ClassLoader ------
		UIManager.getLookAndFeelDefaults().remove("ClassLoader");
		
		// --- Do LookAndFee specific settings ------------
		String lafClassName = UIManager.getLookAndFeel().getClass().getName();
		if (lafClassName.equals(DEFAUL_LOOK_AND_FEEL_CLASS)==true) {
			// --- Nimbus ---------------------------------
			doLookAndFeelAdjustmentsForNimbus();
		} else if (lafClassName.startsWith("com.formdev.flatlaf.")==true) {
			// --- FlatLaF --------------------------------
			doLookAndFeelAdjustmentsForFlatLaf();
		}
	}

	/**
	 * Do look and feel adjustments for the FlatLaF LookAndFeel.
	 */
	private static void doLookAndFeelAdjustmentsForFlatLaf() {
		
		UIManager.getLookAndFeelDefaults().put("TabbedPaneUI", AwbTabbedPaneUI_FlatLaf.class.getName());
		UIManager.getLookAndFeelDefaults().put("ClassLoader", AwbLookAndFeelAdjustments.class.getClassLoader());
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
		UIManager.getLookAndFeelDefaults().put("TabbedPaneUI", AwbTabbedPaneUI_Nimbus.class.getName());
		
	}

	
	// ------------------------------------------------------------------
	// --- Some general help functions for the current LookAndFeel ------
	// ------------------------------------------------------------------	
	/**
	 * Prints the current look and feel properties.
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
	
}
