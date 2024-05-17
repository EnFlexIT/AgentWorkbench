package de.enflexit.common.swing;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.formdev.flatlaf.ui.FlatTabbedPaneUI;

/**
 * The Class AwbFlatLafTabbedPaneUI.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbFlatLafTabbedPaneUI extends FlatTabbedPaneUI implements AwbTabbedPaneHeaderPainter {
	 
	private boolean tabHeaderVisible = true;
	
	/**
     * Create a UI.
     * @param c a component
     * @return a UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new AwbFlatLafTabbedPaneUI();
    }
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.AwbTabbedPaneHeaderPainter#setTabHeaderVisible(boolean)
	 */
	@Override
	public void setTabHeaderVisible(boolean tabHeaderVisible) {
		this.tabHeaderVisible = tabHeaderVisible;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.AwbTabbedPaneHeaderPainter#isTabHeaderVisible()
	 */
	@Override
	public boolean isTabHeaderVisible() {
		return tabHeaderVisible;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintText(java.awt.Graphics, int, java.awt.Font, java.awt.FontMetrics, int, java.lang.String, java.awt.Rectangle, boolean)
	 */
	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
		if (this.isTabHeaderVisible()==true) {
			super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorder(java.awt.Graphics, int, int)
	 */
	@Override
	protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
		if (this.isTabHeaderVisible()==true) {
			super.paintContentBorder(g, tabPlacement, selectedIndex);
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintTabBorder(java.awt.Graphics, int, int, int, int, int, int, boolean)
	 */
	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		if (this.isTabHeaderVisible()==true) {
			super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabAreaHeight(int, int, int)
	 */
	@Override
	protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabAreaWidth(int, int, int)
	 */
	@Override
	protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabAreaWidth(tabPlacement, vertRunCount, maxTabWidth);
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabHeight(int, int, int)
	 */
	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabWidth(int, int, java.awt.FontMetrics)
	 */
	@Override
	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
		}
		return 0;
	}

	
}
