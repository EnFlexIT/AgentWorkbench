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
package agentgui.simulationService.load.threading;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import agentgui.core.application.Application;

import javax.swing.SwingConstants;

/**
 * The Class ThreadMeasureDetailTab.
 * 
 * Displays detailed information about thread/agent load.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMeasureDetailTab extends JPanel implements ActionListener {

	private static final long serialVersionUID = -7315494195421538651L;
	
	private ThreadInfoStorage threadInfoStorage;
	private JScrollPane leftScrollPane;
	private ThreadInfoScrollPane rightScrollPane;
	
	private JPanel JPanelFilter;
	private JRadioButton jRadioButtonNoFilter;
	private JRadioButton jRadioButtonFilterAgents;
	
	private JTree jTreeThreadInfoStorage;
	private JSplitPane splitPane;
	
	private XYSeriesCollection ThreadInfoStorageXYSeriesChartsTotal;
	private XYSeriesCollection ThreadInfoStorageXYSeriesChartsDelta;
	
	private DefaultMutableTreeNode filteredRoot;
	private DefaultMutableTreeNode unfilteredRoot;
	public final String NON_AGENTS   = "all-non-agent-classes"; 
	
	private JPopupMenu menu;
	private JMenuItem viewSingle;
	private JMenuItem viewAgentClass;
	private DefaultMutableTreeNode lastNode = null;
	
	/**
	 * Instantiates a new thread measure detail tab.
	 *
	 * @param threadProtocolVector the thread protocol vector
	 */
	public ThreadMeasureDetailTab(ThreadInfoStorage threadInfoStorage) {
		this.threadInfoStorage = threadInfoStorage;
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setLayout(new BorderLayout(0, 0));
		this.add(getSplitPane(), BorderLayout.CENTER);
		this.add(getJPanelFilter(), BorderLayout.SOUTH);
		threadInfoStorage.getTreeModel().addTreeModelListener(new MyTreeModelListener());
	}
	
	/**
	 * Gets the j panel filter.
	 * @return the j panel filter
	 */
	private JPanel getJPanelFilter() {
		if (JPanelFilter == null) {
			JPanelFilter = new JPanel();
			JPanelFilter.setBorder(null);
			
			// --- Configure Button Group -----------------
			ButtonGroup bg = new ButtonGroup();
			bg.add(getJRadioButtonNoFilter());
			bg.add(getJRadioButtonFilterAgents());
			
			// --- Set default values -----------------------
			this.getJRadioButtonNoFilter().setSelected(true);
			this.getJRadioButtonFilterAgents().setSelected(false);
			GridBagLayout gbl_JPanelFilter = new GridBagLayout();
			gbl_JPanelFilter.columnWidths = new int[] {50, 50, 400};
			gbl_JPanelFilter.rowHeights = new int[]{23, 0};
			gbl_JPanelFilter.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_JPanelFilter.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			JPanelFilter.setLayout(gbl_JPanelFilter);
			GridBagConstraints gbc_jRadioButtonNoFilter = new GridBagConstraints();
			gbc_jRadioButtonNoFilter.fill = GridBagConstraints.BOTH;
			gbc_jRadioButtonNoFilter.insets = new Insets(0, 0, 0, 5);
			gbc_jRadioButtonNoFilter.gridx = 0;
			gbc_jRadioButtonNoFilter.gridy = 0;
			JPanelFilter.add(getJRadioButtonNoFilter(), gbc_jRadioButtonNoFilter);
			GridBagConstraints gbc_jRadioButtonFilterAgents = new GridBagConstraints();
			gbc_jRadioButtonFilterAgents.fill = GridBagConstraints.BOTH;
			gbc_jRadioButtonFilterAgents.gridx = 1;
			gbc_jRadioButtonFilterAgents.gridy = 0;
			JPanelFilter.add(getJRadioButtonFilterAgents(), gbc_jRadioButtonFilterAgents);
		}
		return JPanelFilter;
	}
	
	/**
	 * Gets the j radio button no filter.
	 * @return the j radio button no filter
	 */
	private JRadioButton getJRadioButtonNoFilter() {
		if (jRadioButtonNoFilter == null) {
			jRadioButtonNoFilter = new JRadioButton("No Filter");
			jRadioButtonNoFilter.setHorizontalAlignment(SwingConstants.LEFT);
			jRadioButtonNoFilter.setToolTipText("Show all threads");
			jRadioButtonNoFilter.addActionListener(this);
		}
		return jRadioButtonNoFilter;
	}
	
	/**
	 * Gets the j radio button for filtering agents.
	 * @return the j radio button filter agents
	 */
	private JRadioButton getJRadioButtonFilterAgents() {
		if (jRadioButtonFilterAgents == null) {
			jRadioButtonFilterAgents = new JRadioButton("Filter for Agents");
			jRadioButtonFilterAgents.setHorizontalAlignment(SwingConstants.LEFT);
			jRadioButtonFilterAgents.setToolTipText("Show only agent threads");
			jRadioButtonFilterAgents.addActionListener(this);
		}
		return jRadioButtonFilterAgents;
	}
	
	/**
	 * Gets the right click menu.
	 * @return the right click menu
	 */
	private JPopupMenu getRightClickMenu(){
		if(this.menu == null){
			this.menu = new JPopupMenu();
			
			this.viewSingle = new JMenuItem("single view");
			this.viewAgentClass = new JMenuItem("class view");
			this.viewSingle.addActionListener(this);
			this.viewAgentClass.addActionListener(this);
			
			this.menu.add(viewSingle);
			this.menu.add(viewAgentClass);	        
			 
		}
		return this.menu;
		
	}
	
	/**
	 * Gets the thread protocol stack xy total series charts.
	 * @return the thread protocol stack xy series charts
	 */
	private XYSeriesCollection getThreadInfoStorageXYSeriesChartsTotal(){
		
		if (ThreadInfoStorageXYSeriesChartsTotal == null){
			ThreadInfoStorageXYSeriesChartsTotal = new XYSeriesCollection(null);
		}
		
		return ThreadInfoStorageXYSeriesChartsTotal;
		
	}
	/**
	 * Gets the thread protocol stack xy delta series charts .
	 * @return the thread protocol stack xy series charts
	 */
	private XYSeriesCollection getThreadInfoStorageXYSeriesChartsDelta(){
		
		if (ThreadInfoStorageXYSeriesChartsDelta == null){
			ThreadInfoStorageXYSeriesChartsDelta = new XYSeriesCollection(null);
		}
		
		return ThreadInfoStorageXYSeriesChartsDelta;
		
	}
	

	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {		
		//TODO: save and restore expansionState does not work...hack with ..expandRow 0-3
		if(ae.getSource()==this.getJRadioButtonNoFilter()){
			// --- Remove Filter ----------------

			DefaultTreeModel model = threadInfoStorage.getTreeModel();
			
			model.setRoot(this.unfilteredRoot);
			model.nodeChanged(this.unfilteredRoot);
			jTreeThreadInfoStorage.expandRow(0);
			jTreeThreadInfoStorage.expandRow(1);
			jTreeThreadInfoStorage.expandRow(2);
			jTreeThreadInfoStorage.expandRow(3);

		}else if(ae.getSource()==this.getJRadioButtonFilterAgents()){
			// --- copy root node ---
			DefaultTreeModel model = threadInfoStorage.getTreeModel();
			
			@SuppressWarnings("unused")
			Enumeration<TreePath> expansionState = saveExpansionState(this.getJTreeThreadInfoStorage());	
			
			removeNonAgentsFromNode(this.filteredRoot);
			model.setRoot(this.filteredRoot);
//			restoreExpansionState(getJTreeThreadInfoStorage(), expansionState);	
			jTreeThreadInfoStorage.expandRow(0);
			jTreeThreadInfoStorage.expandRow(1);
			jTreeThreadInfoStorage.expandRow(2);
			jTreeThreadInfoStorage.expandRow(3);
//			model.reload();
			
		}else if ((ae.getSource()==viewSingle) ||(ae.getSource()==viewAgentClass)){
			//--- right-click Menu ---
			
			XYSeriesCollection popupXYSeriesCollectionTotal = new XYSeriesCollection();
    	    XYSeriesCollection popupXYSeriesCollectionDelta = new XYSeriesCollection();
    	    XYSeries series = null;
			String className = "";
            Iterator<String> iteratorClass = null;
    		
			if(ae.getSource()==viewSingle){
				if (lastNode != null) {
					className = lastNode.toString();
	                iteratorClass = threadInfoStorage.getMapAgent().get(className).getXYSeriesMap().keySet().iterator();
				}	
			}else if(ae.getSource()==viewAgentClass){
				if (lastNode != null) {
            	    Object obj = lastNode.getUserObject();

            	    if(obj.getClass().toString().endsWith("ThreadInfoStorageAgent")){
            	    	ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)obj;
            	    	className =  tia.getClassName();
            	    }else{
            	    	className =  NON_AGENTS;
            	    }
            	    
            	    iteratorClass = threadInfoStorage.getMapAgentClass().get(className).getXYSeriesMap().keySet().iterator();
	            }
			}
    	    
    	    while(iteratorClass.hasNext()){
    	    	String next = iteratorClass.next();
    	    	
	    		if(ae.getSource()==viewSingle){
	    			series = threadInfoStorage.getMapAgent().get(className).getXYSeries(next);
	    		}else if(ae.getSource()==viewAgentClass){
	    			series = threadInfoStorage.getMapAgentClass().get(className).getXYSeries(next);
	    		}
    	    	if(next.contains("TOTAL")){
    	    		popupXYSeriesCollectionTotal.addSeries(series);
    	    	}else if(next.contains("DELTA")){
    	    		popupXYSeriesCollectionDelta.addSeries(series);
    	    	}
    	    	
    	    }
    	    // --- create scroll-pane in pop-up window ---
    	    new ThreadInfoScrollPane(popupXYSeriesCollectionDelta, popupXYSeriesCollectionTotal, true, className);
		}
	}
	
	/**
     * Save the expansion state of a tree.
     *
     * @param tree
     * @return expanded tree path as Enumeration
     */

    public Enumeration<TreePath> saveExpansionState(JTree tree) {
        return tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
    }
    
    /**
     * Restore expansion state of a tree.
     *
     * @param tree the tree
     * @param e the e
     */
    public void restoreExpansionState(JTree tree, Enumeration<TreePath> e){
    	if(e != null){
			while (e.hasMoreElements()) {
				TreePath treePath = e.nextElement();
                tree.expandPath(treePath);
         	}
		}
    }
    
	/**
	 * The listener interface for receiving myTreeModel events.
	 * The class that is interested in processing a myTreeModel
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyTreeModelListener<code> method. When
	 * the myTreeModel event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyTreeModelEvent
	 */
	class  MyTreeModelListener implements TreeModelListener {

		@Override
		public void treeNodesChanged(TreeModelEvent e) {
			
			DefaultTreeModel model = threadInfoStorage.getTreeModel();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
			unfilteredRoot = (DefaultMutableTreeNode) CopyNode(node);
			filteredRoot   = (DefaultMutableTreeNode) CopyNode(node);
		}

		@Override
		public void treeNodesInserted(TreeModelEvent e) {
		}

		@Override
		public void treeNodesRemoved(TreeModelEvent e) {
		}

		@Override
		public void treeStructureChanged(TreeModelEvent e) {
		}
	}
	
	/**
	 * Cches if node is of class instance "ThreadInfoStorageAgent".
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	private boolean matchAgentClass(DefaultMutableTreeNode node) {
		Object obj = node.getUserObject();
	    if(obj.getClass().toString().endsWith("ThreadInfoStorageAgent")){
	    	ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)obj;
	    	if(tia.isAgent() == true){
	    		return true;
	    	}	    	
	    }
        return false;
    }
	
	/**
	 * Gets a copy of DefaultMutableTreeNode.
	 *
	 * @param OriginNode the origin node
	 * @return the object
	 */
	private Object CopyNode(DefaultMutableTreeNode OriginNode){
	    DefaultMutableTreeNode Copy = new DefaultMutableTreeNode(OriginNode.getUserObject());
	    if(OriginNode.isLeaf()){
	        return Copy;
	    }else{
	        int cc = OriginNode.getChildCount();
	        for(int i=0;i<cc;i++){
	            Copy.add((MutableTreeNode) CopyNode((DefaultMutableTreeNode)OriginNode.getChildAt(i)));
	        }
	        return Copy;
	    }
	}
	
	/**
	 * Removes  non-agents from DefaultMutableTreeNode.
	 *
	 * @param node the node
	 */
	protected void removeNonAgentsFromNode(DefaultMutableTreeNode node){
		
		Vector<DefaultMutableTreeNode> toDeleteVect= new Vector<DefaultMutableTreeNode>();
		
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = node.depthFirstEnumeration();
		if(e != null){
			while (e.hasMoreElements()){
				DefaultMutableTreeNode actualElement = e.nextElement();
				if (matchAgentClass(actualElement) == false && actualElement.isLeaf() == true) {
					//--- immediate removal invalidates the enumeration ---
	//				actualElement.removeFromParent();
					// --- remember for later removal ---
					toDeleteVect.add(actualElement); 
				}
	          
	          
			}
		}
		//--- delete nodes ---
		for( int i=0; i < toDeleteVect.size(); i++){
			toDeleteVect.get(i).removeFromParent();
		}
	}
	
	/**
	 * Gets the jtree of ThreadInfoStorage.
	 * @return the j tree thread protocol vector
	 */
	private JTree getJTreeThreadInfoStorage() {
		if (jTreeThreadInfoStorage == null) {
			jTreeThreadInfoStorage = new JTree(threadInfoStorage.getTreeModel());
//			jTreeThreadInfoStorage.addTreeSelectionListener(new MyTreeSelectionListener());
			jTreeThreadInfoStorage.addMouseListener(new MyMouseListener());
			jTreeThreadInfoStorage.setCellRenderer(new MyTreeCellRenderer());
		}
		return jTreeThreadInfoStorage;
	}
	
	/**
	 * Gets the split pane.
	 * @return the split pane
	 */
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,getLeftScrollPane(),this.getRightScrollPane());			
		}
		return splitPane;
	}
	
	/**
	 * Gets the left scroll pane.
	 * @return the left scroll pane
	 */
	private JScrollPane getLeftScrollPane(){
		if (leftScrollPane == null) {
			leftScrollPane = new JScrollPane(getJTreeThreadInfoStorage());
			leftScrollPane.setMinimumSize(new Dimension(200,300));
		}
		return leftScrollPane;
		
	}
	
	/**
	 * Gets the right scroll pane.
	 * @return the right scroll pane
	 */
	private ThreadInfoScrollPane getRightScrollPane(){
		if (rightScrollPane == null) {
			rightScrollPane = new ThreadInfoScrollPane(this.getThreadInfoStorageXYSeriesChartsDelta(), this.getThreadInfoStorageXYSeriesChartsTotal(), false, "");
		}
		return rightScrollPane;
		
	}
	
	/**
	 * The listener interface for receiving myMouse events.
	 * The class that is interested in processing a myMouse
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyMouseListener<code> method. When
	 * the myMouse event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyMouseEvent
	 */
	class MyMouseListener implements MouseListener{
		
				@Override
				public void mouseClicked(MouseEvent e){
				}
					
				@Override
		        public void mousePressed (MouseEvent e){
					
		            if (SwingUtilities.isRightMouseButton(e)){
		            	// --- show menu on right-click ---
		                TreePath path = jTreeThreadInfoStorage.getPathForLocation (e.getX(), e.getY());
		                Rectangle pathBounds = jTreeThreadInfoStorage.getUI ().getPathBounds ( jTreeThreadInfoStorage, path );
		                if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())){
		                	getRightClickMenu().show(jTreeThreadInfoStorage, pathBounds.x, pathBounds.y + pathBounds.height);  
		                }
		            }else if(e.getClickCount() == 1){
						// --- toggle chart visibility ---
			            TreePath path = jTreeThreadInfoStorage.getPathForLocation(e.getX(), e.getY());
			            
			            if (path != null) {
//			                System.out.println(path.getLastPathComponent().toString());
			                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			                lastNode = node;
			                
			                // --- toogle flag ---
			                Object obj = lastNode.getUserObject();
		            	    if(obj.getClass().toString().endsWith("ThreadInfoStorageAgent")){
		            	    	ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)obj;
		            	    	tia.setSelected(!tia.isSelected());
		            	    }
							
							if(threadInfoStorage.getMapAgent().get(node.toString()) != null){
								
								String agentName = lastNode.toString();
    			                Iterator<String> iteratorAgent = threadInfoStorage.getMapAgent().get(agentName).getXYSeriesMap().keySet().iterator();
    			                
	                    	    while(iteratorAgent.hasNext()){
	                    	    	String next = iteratorAgent.next();
	                    	    	
	                    	    	//--- show charts according to display settings  --
	                    	    	if(next.contains("TOTAL")){
	                    	    		XYSeries series = threadInfoStorage.getMapAgent().get(agentName).getXYSeries(next);
	                    	    		
		                    	    	if(getThreadInfoStorageXYSeriesChartsTotal().indexOf(series.getKey()) == -1){
		                    	    		getThreadInfoStorageXYSeriesChartsTotal().addSeries(series);
		                    	    	}else{
		                    	    		getThreadInfoStorageXYSeriesChartsTotal().removeSeries(series);
		                    	    	}
	                    	    	}else if(next.contains("DELTA")){
	                    	    		XYSeries series = threadInfoStorage.getMapAgent().get(agentName).getXYSeries(next);
	                    	    		
		                    	    	if(getThreadInfoStorageXYSeriesChartsDelta().indexOf(series.getKey()) == -1){
		                    	    		getThreadInfoStorageXYSeriesChartsDelta().addSeries(series);
		                    	    	}else{
		                    	    		getThreadInfoStorageXYSeriesChartsDelta().removeSeries(series);
		                    	    	}
	                    	    		
	                    	    	}
	                    	    }								
							}
			            }
			        }

		        }
				@Override
				public void mouseReleased(MouseEvent e){
					
				}
				@Override
				public void mouseEntered(MouseEvent e){
					
				}
				@Override
				public void mouseExited(MouseEvent e){
					
				}		
	}	
	
	/**
	 * The Class MyTreeCellRenderer.
	 */
	class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		
	    private static final long serialVersionUID = 967937360839244309L;
	    private Icon activeIcon;

	    /**
    	 * Instantiates a new my tree cell renderer.
    	 */
    	public MyTreeCellRenderer(){
	    	final String PathImage = Application.getGlobalInfo().getPathImageIntern();
	    	this.activeIcon = new ImageIcon(getClass().getResource(PathImage + "StatGreen.png"));
	    }

	    @Override
	    public Component getTreeCellRendererComponent(JTree tree, Object value,
	            boolean sel, boolean expanded, boolean leaf, int row,
	            boolean hasFocus) {

	        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
	                row, hasFocus);
	        
	        Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();         

	        if(nodeObj.getClass().toString().endsWith("ThreadInfoStorageAgent")){
    	    	ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)nodeObj;
    	    	if(tia.isSelected()){
    	    		setIcon(activeIcon);
    	    	}else{
    	    		setIcon(null);
    	    	}
    	    }	        
	        return this;
	    }
	}
}
