package de.enflexit.common.ontology;

import java.awt.Color;
import java.awt.Image;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.UIManager;

import de.enflexit.common.classLoadService.BaseClassLoadServiceUtility;
import de.enflexit.common.ontology.gui.OntologyClassVisualisation;

/**
 * The Class OntologyVisualisationConfiguration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyVisualisationConfiguration {
	
	// --- General settings for title, icon and owner frame ------------------- 
	private static String applicationTitle = "Ontology Visualization";
	private static Window ownerWindow;
	private static Image applicationIconImage;
	
	// --- Known OntologyClassVisualisation's of Agent.GUI --------------------
	private static Vector<OntologyClassVisualisation> knownOntologyClassVisualisation;

	// --- Some parameter for the color settings ------------------------------ 
	private static boolean debug = false;
	private static boolean colorPropertiesAlreadyPrinted = false;

	
	/**
	 * Returns the application title.
	 * @return the application title
	 */
	public static String getApplicationTitle() {
		return applicationTitle;
	}
	/**
	 * Sets the application title.
	 * @param newApplicationTitle the new application title
	 */
	public static void setApplicationTitle(String newApplicationTitle) {
		applicationTitle = newApplicationTitle;
	}
	
	/**
	 * Returns the owner window.
	 * @return the owner window
	 */
	public static Window getOwnerWindow() {
		return ownerWindow;
	}
	/**
	 * Sets the owner window.
	 * @param ownerWindow the new owner window
	 */
	public static void setOwnerWindow(Window newOwnerWindow) {
		ownerWindow = newOwnerWindow;
	}

	/**
	 * Returns the current application image icon.
	 * @return the application image icon
	 */
	public static Image getApplicationIconImage() {
		return applicationIconImage;
	}
	/**
	 * Sets the application image icon.
	 * @param applicationIconImage the new application image icon
	 */
	public static void setApplicationIconImage(Image applicationIconImage) {
		OntologyVisualisationConfiguration.applicationIconImage = applicationIconImage;
	}
	
	
	// ------------------------------------------------------------------------
	// ---- Methods for OntologyClassVisualisations ---------------------------
	// ------------------------------------------------------------------------
	/**
	 * Register an OntologyClassVisualisation.
	 * @param classNameOfOntologyClassVisualisation the class name of the OntologyClassVisualisation
	 */
	public static OntologyClassVisualisation registerOntologyClassVisualisation(String classNameOfOntologyClassVisualisation) {
		
		OntologyClassVisualisation ontoClassVisualisation = null;
		try {
			if (isOntologyClassVisualisation(classNameOfOntologyClassVisualisation)==false) {
				ontoClassVisualisation = (OntologyClassVisualisation) BaseClassLoadServiceUtility.newInstance(classNameOfOntologyClassVisualisation);
				getKnownOntologyClassVisualisations().add(ontoClassVisualisation);	
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ontoClassVisualisation;
	}
	/**
	 * Unregister an OntologyClassVisualisation.
	 * @param classNameOfOntologyClassVisualisation the class name of the OntologyClassVisualisation
	 */
	public static void unregisterOntologyClassVisualisation(String classNameOfOntologyClassVisualisation) {
		if (isOntologyClassVisualisation(classNameOfOntologyClassVisualisation)==true) {
			OntologyClassVisualisation ontoClassVisualisation = getOntologyClassVisualisation(classNameOfOntologyClassVisualisation);
			getKnownOntologyClassVisualisations().add(ontoClassVisualisation);	
		}
	}
	/**
	 * Unregister an OntologyClassVisualisation.
	 * @param ontologyClassVisualisation the OntologyClassVisualisation to unregister
	 */
	public static void unregisterOntologyClassVisualisation(OntologyClassVisualisation ontologyClassVisualisation) {
		if (isOntologyClassVisualisation(ontologyClassVisualisation)==true) {
			getKnownOntologyClassVisualisations().add(ontologyClassVisualisation);	
		}
	}
	
	/**
	 * Returns the known ontology class visualizations.
	 * @return the Vector of known ontology class visualizations
	 */
	public static Vector<OntologyClassVisualisation> getKnownOntologyClassVisualisations() {
		if (knownOntologyClassVisualisation==null) {
			knownOntologyClassVisualisation = new Vector<OntologyClassVisualisation>();
		}
		return knownOntologyClassVisualisation;
	}
	
	/**
	 * Checks if a given object can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param checkObject the object to check
	 * @return true, if the given Object is ontology class visualisation
	 */
	public static boolean isOntologyClassVisualisation(Object checkObject) {
		if (checkObject==null) return false;
		return isOntologyClassVisualisation(checkObject.getClass());
	}
	/**
	 * Checks if a given class can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param checkClass the class to check
	 * @return true, if the given Object is ontology class visualisation
	 */
	public static boolean isOntologyClassVisualisation(Class<?> checkClass) {
		if (checkClass==null) return false;
		return isOntologyClassVisualisation(checkClass.getName());
	}
	/**
	 * Checks class, given by its name, can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param className the object to check
	 * @return true, if the given className is ontology class visualisation
	 */
	public static boolean isOntologyClassVisualisation(String className) {
		
		boolean isVisClass = false;
		
		Vector<OntologyClassVisualisation> ontoClassViss = getKnownOntologyClassVisualisations();
		for (int i=0; i<ontoClassViss.size(); i++) {
			OntologyClassVisualisation ontoClassVis = ontoClassViss.get(i);
			String visClassName = ontoClassVis.getOntologyClass().getName();
			if (visClassName.equals(className)) {
				isVisClass=true;
				break;
			}
		}
		return isVisClass;
	}
	
	/**
	 * Returns the OntologyClassVisualisation for a given object.
	 *
	 * @param checkObject the check object
	 * @return the ontology class visualization
	 */
	public static OntologyClassVisualisation getOntologyClassVisualisation(Class<?> checkObject) {
		return getOntologyClassVisualisation(checkObject.getName());
	}
	/**
	 * Returns the OntologyClassVisualisation for a given class, specified by its name.
	 *
	 * @param className the class name
	 * @return the OntologyClassVisualisation
	 */
	public static OntologyClassVisualisation getOntologyClassVisualisation(String className) {
		
		OntologyClassVisualisation ontoClassVisFound = null;
		Vector<OntologyClassVisualisation> ontoClassViss = getKnownOntologyClassVisualisations();
		for (int i=0; i<ontoClassViss.size(); i++) {
			OntologyClassVisualisation ontoClassVis = ontoClassViss.get(i);
			String compareWith = ontoClassVis.getOntologyClass().getName();
			if (compareWith.equals(className)==true) {
				ontoClassVisFound=ontoClassVis;
				break;
			}
		}
		return ontoClassVisFound;
	}
	
	// ------------------------------------------------------------------------
	// ---- Methods for color settings ----------------------------------------
	// ------------------------------------------------------------------------
	/** The Class Colors. */
	public final static class Colors {

		/** The Constant TB_BACKGROUND. */
		public final static Color TB_BACKGROUND = Color.WHITE;
		/** The Constant TB_ALTERNATEROWCOLOR. */
		public final static Color TB_ALTERNATEROWCOLOR = new Color(242, 242, 242);
		/** The Constant TB_HIGHLIGHT. */
		public final static Color TB_HIGHLIGHT = new Color(57, 105, 138);

		/** The Constant TB_TEXTFOREGROUND. */
		public final static Color TB_TEXTFOREGROUND = new Color(35, 35, 36);
		/** The Constant TB_TEXTFOREGROUND_SELECTED. */
		public final static Color TB_TEXTFOREGROUND_SELECTED = Color.WHITE;

		/**
		 * Sets the color for a component that is located in a cell of a JTable.
		 *
		 * @param comp the JComponent
		 * @param row the row
		 * @param isSelected the is selected
		 */
		public static void setTableCellRendererColors(JComponent comp, int row, boolean isSelected) {

			// --- do the settings --------------
			comp.setOpaque(true);
			if (isSelected == true) {
				comp.setForeground(Colors.TB_TEXTFOREGROUND_SELECTED);
				comp.setBackground(Colors.TB_HIGHLIGHT);

			} else {
				comp.setForeground(Colors.TB_TEXTFOREGROUND);
				if (row%2==0) {
					comp.setBackground(Colors.TB_BACKGROUND);
				} else {
					comp.setBackground(Colors.TB_ALTERNATEROWCOLOR);			
				}
			}

			// --- In case of debugging ---------
			if (debug==false) {
				return;

			}
			if (colorPropertiesAlreadyPrinted==false) {
				List<String> colors = new ArrayList<String>();
				for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
					if (entry.getValue() instanceof Color) {
						colors.add((String) entry.getKey()); // all the keys are strings
					}
				}
				Collections.sort(colors);
				for (String name : colors) {
					System.out.println(name);
				}
				
				// --- Print the current color setting ----
				printColorSetting();

				colorPropertiesAlreadyPrinted = true;
			}

		}
		
		/**
		 * Prints the color settings for table cells.
		 */
		public static void printColorSetting() {
			
			Color color = TB_BACKGROUND;
			System.out.println("TB_BACKGROUND => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");

			color = TB_ALTERNATEROWCOLOR;
			System.out.println("TB_ALTERNATEROWCOLOR => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");
			
			color = TB_HIGHLIGHT;
			System.out.println("TB_HIGHLIGHT => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");

			color = TB_TEXTFOREGROUND;
			System.out.println("TB_TEXTFOREGROUND => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");
			
			color = TB_TEXTFOREGROUND_SELECTED;
			System.out.println("TB_TEXTFOREGROUND_SELECTED => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");
			
		}
		

	} // end color sub class

	
}
