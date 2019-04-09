package agentgui.envModel.graph.networkModel;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

/**
 * The Class GraphEdgeShapeConfiguration serves as super class for specific edge shape configurationss.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @param <T> the generic type
 */
public abstract class GraphEdgeShapeConfiguration<T extends Shape> {

	/**
	 * Returns the actual edge shape.
	 *
	 * @param graphNodeFrom the graph node from
	 * @param graphNodeTo the graph node to
	 * @return the shape
	 */
	public abstract T getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo);

	/**
	 * Returns a copy of the current configuration.
	 * @return the copy
	 */
	protected abstract GraphEdgeShapeConfiguration<?> getCopy();
	
	/**
	 * Returns the configuration as consistent string.
	 * @return the configuration as string
	 */
	public abstract String getConfigurationAsString();
	
	/**
	 * Sets the configuration from the specified string.
	 *
	 * @param stringConfiguration the configuration string 
	 * @return the graph edge shape configuration
	 */
	public abstract void setConfigurationFromString(String stringConfiguration);
	
	
	
	/**
	 * The Class QuadCurveConfiguration.
	 */
	public class QuadCurveConfiguration extends GraphEdgeShapeConfiguration<QuadCurve2D> {

		private QuadCurve2D quadCurve2D;
		
		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getConfigurationAsString()
		 */
		@Override
		public String getConfigurationAsString() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#setConfigurationFromString(java.lang.String)
		 */
		@Override
		public void setConfigurationFromString(String stringConfiguration) {
			// TODO Auto-generated method stub
		}
		
		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getShape()
		 */
		@Override
		public QuadCurve2D getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
			if (quadCurve2D==null) {
				quadCurve2D = new QuadCurve2D.Double();
				// --- Set default parameter ----
				quadCurve2D.setCurve(0.0, 0.0, 0.5, 0.5, 1.0, 0.0);
			}
			return quadCurve2D;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getCopy()
		 */
		@Override
		protected GraphEdgeShapeConfiguration<?> getCopy() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * The Class PolyLineConfiguration.
	 */
	public class PolylineConfiguration extends GraphEdgeShapeConfiguration<GeneralPath> {

		private GeneralPath generalPath;
		
		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getConfigurationAsString()
		 */
		@Override
		public String getConfigurationAsString() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#setConfigurationFromString(java.lang.String)
		 */
		@Override
		public void setConfigurationFromString(String stringConfiguration) {
			// TODO Auto-generated method stub
		}
		
		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getShape()
		 */
		@Override
		public GeneralPath getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
			if (generalPath==null) {
				generalPath = new GeneralPath();
				// --- Set default parameter ----
				generalPath.moveTo(0.0f, 0.0f);
				generalPath.lineTo(0.2f, 0.3f);
				generalPath.lineTo(0.3f, 50.3f);
				generalPath.lineTo(0.5f, -50.5f);
				generalPath.lineTo(0.8f, 40.3f);
				generalPath.lineTo(1.0f, 0.0f);
			}
			return generalPath;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getCopy()
		 */
		@Override
		protected GraphEdgeShapeConfiguration<?> getCopy() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/**
	 * The Class OrthogonalConfiguration.
	 */
	public class OrthogonalConfiguration extends GraphEdgeShapeConfiguration<Line2D> {

		private Line2D line;
		
		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getConfigurationAsString()
		 */
		@Override
		public String getConfigurationAsString() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#setConfigurationFromString(java.lang.String)
		 */
		@Override
		public void setConfigurationFromString(String stringConfiguration) {
			// TODO Auto-generated method stub
		}
		
		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getShape()
		 */
		@Override
		public Line2D getShape(GraphNode graphNodeFrom, GraphNode graphNodeTo) {
			if (line==null) {
				line = new Line2D.Double(0.0, 0.0, 1.0, 0.0);
			}
			return line;
		}

		/* (non-Javadoc)
		 * @see agentgui.envModel.graph.networkModel.GraphEdgeShapeConfiguration#getCopy()
		 */
		@Override
		protected GraphEdgeShapeConfiguration<?> getCopy() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
