package de.enflexit.common.swing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;

/**
 * The Class TableCellColorHelper.
 */
public final class TableCellColorHelper {

	private static boolean debug = false;
	private static boolean colorPropertiesAlreadyPrinted = false;
	
	private static boolean isEnabledTableCellColorHelper = true;
	
	/** The Constant TB_BACKGROUND. */
	public final static Color TB_BACKGROUND = Color.WHITE;
	/** The Constant TB_ALTERNATEROWCOLOR. */
	public final static Color TB_ALTERNATEROWCOLOR = new Color(242, 242, 242);
	/** The Constant TB_HIGHLIGHT. */
	public static Color TB_HIGHLIGHT = new Color(57, 105, 138);
	
	/** The Constant TB_TEXTFOREGROUND. */
	public final static Color TB_TEXTFOREGROUND = new Color(35, 35, 36);
	/** The Constant TB_TEXTFOREGROUND_SELECTED. */
	public final static Color TB_TEXTFOREGROUND_SELECTED = Color.WHITE;

	
	
	/**
	 * Sets the table highlight color.
	 * @param newColor the new table highlight color; set <code>null</code> for system default value
	 */
	public static void setTableHighlightColor(Color newColor) {
		if (newColor==null) {
			TB_HIGHLIGHT = new Color(57, 105, 138);
		} else {
			TB_HIGHLIGHT = newColor;
		}
	}
	
	
	/**
	 * Sets the color for a component that is located in a cell of a JTable.
	 *
	 * @param comp the JComponent to color
	 * @param row the current table row number 
	 * @param isSelected the indicator, if the row is selected
	 */
	public static void setTableCellRendererColors(JComponent comp, int row, boolean isSelected) {
		if (isEnabledTableCellColorHelper==false || AwbLookAndFeelAdjustments.isNimbusLookAndFeel()==false) return;
		TableCellColorHelper.setTableCellRendererColors(comp, row, isSelected, TableCellColorHelper.TB_BACKGROUND);
	}
	
	/**
	 * Sets the color for a component that is located in a cell of a JTable.
	 *
	 * @param comp the JComponent to color
	 * @param row the current table row number 
	 * @param isSelected the indicator, if the row is selected
	 * @param tableBgColor the table background color to use 
	 */
	public static void setTableCellRendererColors(JComponent comp, int row, boolean isSelected, Color tableBgColor) {

		if (isEnabledTableCellColorHelper==false || AwbLookAndFeelAdjustments.isNimbusLookAndFeel()==false) return;
		
		// --- do the settings --------------
		comp.setOpaque(true);
		if (isSelected == true) {
			comp.setForeground(TableCellColorHelper.TB_TEXTFOREGROUND_SELECTED);
			comp.setBackground(TableCellColorHelper.TB_HIGHLIGHT);

		} else {
			comp.setForeground(TableCellColorHelper.TB_TEXTFOREGROUND);
			if (row%2==0) {
				comp.setBackground(tableBgColor);
			} else {
				comp.setBackground(TableCellColorHelper.TB_ALTERNATEROWCOLOR);			
			}
		}

		// --- In case of debugging ---------
		if (debug==true) {
			
			if (colorPropertiesAlreadyPrinted==false) {
				colorPropertiesAlreadyPrinted = true;
				
				List<String> colors = new ArrayList<String>();
				for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
					if (entry.getValue() instanceof Color) {
						colors.add((String) entry.getKey()); // all keys are strings
					}
				}
				Collections.sort(colors);
				for (int i = 0; i < colors.size(); i++) {
					System.out.println(colors.get(i));
				}
				// --- Print the current color setting ----
				printColorSetting();
			}
		}

	}
	
	
	/**
	 * Sets the table back- and foreground according to the table definition.
	 *
	 * @param table the table
	 * @param component the component to be used for displaying or editing
	 * @param isSelected the is selected
	 */
	public static void setTableBackAndForeGroundToTableDefinition(JTable table, JComponent component, boolean isSelected) {

		if (table==null || component==null || AwbLookAndFeelAdjustments.isNimbusLookAndFeel()==true) return;
		if (isSelected) {
			component.setBackground(table.getSelectionBackground());
			component.setForeground(table.getSelectionForeground());
		} else {
			component.setBackground(table.getBackground());
			component.setForeground(table.getForeground());
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

}