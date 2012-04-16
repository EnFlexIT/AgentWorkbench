package agentgui.core.charts;

import java.awt.image.BufferedImage;
import java.util.Observer;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.Dataset;

/**
 * Generic chart tab class
 * @author Nils
 *
 */
public abstract class ChartTab extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -645894074023524911L;
	
	/**
	 * The chart
	 */
	protected JFreeChart chart = null;
	
	/**
	 * Create the chart panel that contains the chart
	 * @param dataset The data set to display
	 * @return The chart panel
	 */
	protected abstract ChartPanel createChartPanel(Dataset dataset);
	
	/**
	 * Creates a thumbnail of the chart
	 * @return The thumbnail
	 */
	public BufferedImage createChartThumb() {
		// Remove legend while creating the thumb
		LegendTitle legend = chart.getLegend();
		chart.removeLegend();
		BufferedImage thumb = chart.createBufferedImage(260, 175, 600, 400, null);
		
		chart.addLegend(legend);
		return thumb;
	}

}
