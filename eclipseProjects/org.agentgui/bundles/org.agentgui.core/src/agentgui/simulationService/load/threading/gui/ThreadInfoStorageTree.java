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
package agentgui.simulationService.load.threading.gui;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import agentgui.core.config.GlobalInfo;
import agentgui.simulationService.load.threading.ThreadDetail;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorage;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageAgent;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageCluster;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageContainer;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageJVM;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorageMachine;

/**
 * The Class ThreadInfoStorageTree.
 * 
 * Builds up a tree: cluster->machine->JVM->Container
 * Each clickable node displays details about
 * CPU utilization an thread times.
 * Filter for agents applicable.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageTree extends JTree implements ActionListener{
	
	private static final long serialVersionUID = 5311458634217524061L;
	
	private DefaultMutableTreeNode filteredRoot;	
	private DefaultMutableTreeNode filteredTextRoot;
	private DefaultMutableTreeNode lastNode;
	
	private JPopupMenu rightClickMenu;
	private JMenuItem viewSingle;
	private JMenuItem viewAgentClass;

	private HashMap<String, Boolean> leafSelected;
	private ThreadInfoStorage threadInfoStorage;
	
	private XYSeriesCollection seriesChartsTotal;
	private XYSeriesCollection seriesChartsDelta;
	
	private String expansionState;
	
	
	/**
	 * Instantiates a new thread info storage tree.
	 * @param threadInfoStorage the thread info storage
	 */
	public ThreadInfoStorageTree(ThreadInfoStorage threadInfoStorage){
		super(threadInfoStorage.getModel());
		this.threadInfoStorage = threadInfoStorage;
		this.lastNode = null;
		this.leafSelected = new HashMap<String, Boolean>();
		this.addMouseListener(new MyMouseListener());
		this.setCellRenderer(new MyTreeCellRenderer());
		this.filteredRoot = new DefaultMutableTreeNode();
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	/**
	 * Gets the thread protocol stack xy total series charts.
	 * @return the thread protocol stack xy series charts
	 */
	public XYSeriesCollection getSeriesChartsTotal(){
		if(seriesChartsTotal == null){
			seriesChartsTotal = new XYSeriesCollection(null);
		}
		
		return seriesChartsTotal;
	}
	/**
	 * Gets the thread protocol stack XY delta series charts .
	 * @return the thread protocol stack XY series charts
	 */
	public XYSeriesCollection getSeriesChartsDelta(){
		if(seriesChartsDelta == null){
			seriesChartsDelta = new XYSeriesCollection(null);
		}
		
		return seriesChartsDelta;
	}
	
	/**
	 * Gets the right click rightClickMenu.
	 * @return the right click rightClickMenu
	 */
	private JPopupMenu getRightClickMenu() {
		if (rightClickMenu == null) {
			rightClickMenu = new JPopupMenu();

			viewSingle = new JMenuItem("single view");
			viewAgentClass = new JMenuItem("class view");
			viewSingle.addActionListener(this);
			viewAgentClass.addActionListener(this);

			rightClickMenu.add(viewSingle);
			rightClickMenu.add(viewAgentClass);
		}
		return rightClickMenu;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {		
		
		if ((ae.getSource()==viewSingle) ||(ae.getSource()==viewAgentClass)){
			//--- right-click Menu ---
			
			XYSeriesCollection popupXYSeriesCollectionTotal = new XYSeriesCollection();
    	    XYSeriesCollection popupXYSeriesCollectionDelta = new XYSeriesCollection();
    	    XYSeries series = null;
			String className = "";
			String folderNamePrefix = "";
            Iterator<String> iteratorClass = null;
            Object obj = null;
            
            if(lastNode != null) {
        	    obj = lastNode.getUserObject();
        	    
        	    if(obj.getClass().toString().endsWith("ThreadInfoStorageAgent")){
        	    	ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)obj;
        	    	if(ae.getSource()==viewSingle){
        	    		folderNamePrefix = "Thread: ";
        	    		className = tia.getName();
        	    		iteratorClass = threadInfoStorage.getMapAgent().get(className).getXYSeriesMap().keySet().iterator();
        	    	}else{
        	    		if(ae.getSource()==viewAgentClass){
        	    			className =  tia.getClassName();	
        	    		}else{
	        	    		className =  ThreadDetail.UNKNOWN_AGENT_CLASSNAME;
	        	    	}
        	    		folderNamePrefix = "Class: ";
        	    		iteratorClass = threadInfoStorage.getMapAgentClass().get(className).getXYSeriesMap().keySet().iterator();
        	    	}
        	    }
            }
    	    
			if(iteratorClass != null){
	    	    while(iteratorClass.hasNext()){
	    	    	String next = iteratorClass.next();
	    	    	
		    		if(ae.getSource()==viewSingle){
		    			series = threadInfoStorage.getMapAgent().get(className).getXYSeriesMap().get(next);
		    		}else if(ae.getSource()==viewAgentClass){
		    			series = threadInfoStorage.getMapAgentClass().get(className).getXYSeriesMap().get(next);
		    		}
	    	    	if(next.contains("TOTAL")){
	    	    		popupXYSeriesCollectionTotal.addSeries(series);
	    	    	}else if(next.contains("DELTA")){
	    	    		popupXYSeriesCollectionDelta.addSeries(series);
	    	    	}
	    	    	
	    	    }
	    	    // --- create scroll-pane in pop-up window ---
	    	    new ThreadInfoStorageScrollPane(popupXYSeriesCollectionDelta, popupXYSeriesCollectionTotal, null, true, folderNamePrefix+className, null);
			}
		}
	}
	
	/**
	 * Checks if node is of class instance "ThreadInfoStorageAgent".
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	private boolean matchAgentClass(DefaultMutableTreeNode node) {
		Object obj = node.getUserObject();
		if(obj != null) {
		    if(obj.getClass().toString().endsWith("ThreadInfoStorageAgent")){
		    	ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)obj;
		    	if(tia.isAgent() == true){
		    		return true;
		    	}	    	
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
	private Object copyNode(DefaultMutableTreeNode OriginNode){
	    DefaultMutableTreeNode Copy = new DefaultMutableTreeNode(OriginNode.getUserObject());
	    if(OriginNode.isLeaf()){
	        return Copy;
	    }else{
	        int cc = OriginNode.getChildCount();
	        for(int i=0;i<cc;i++){
	            Copy.add((MutableTreeNode) copyNode((DefaultMutableTreeNode)OriginNode.getChildAt(i)));
	        }
	        return Copy;
	    }
	}
	
	/**
	 * Removes non-agents from DefaultMutableTreeNode.
	 *
	 * @param node the node
	 * @param key the key
	 */
	private void filterNode(DefaultMutableTreeNode node){
		
		Vector<DefaultMutableTreeNode> toDeleteVect= new Vector<DefaultMutableTreeNode>();
		
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = node.depthFirstEnumeration();
		if(e != null){
			while (e.hasMoreElements()){
				DefaultMutableTreeNode actualElement = e.nextElement();
				if (actualElement.isLeaf() == true && matchAgentClass(actualElement) == false) {
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
	 * Filter node by key.
	 *
	 * @param node the node
	 * @param key the key
	 */
	private void filterNodeByKey(DefaultMutableTreeNode node, String key){
			
			Vector<DefaultMutableTreeNode> toDeleteVect= new Vector<DefaultMutableTreeNode>();
			
			@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> e = node.breadthFirstEnumeration();
			if(e != null){
				while (e.hasMoreElements()){
					DefaultMutableTreeNode actualElement = e.nextElement();
					if (actualElement.isLeaf() == true && key.isEmpty() == false && actualElement.toString().toLowerCase().contains(key.toLowerCase()) == false) {
						//--- immediate removal invalidates the enumeration ---
		//				actualElement.removeFromParent();
						// --- remember for later removal ---
						toDeleteVect.add(actualElement); 
					}
					if(actualElement.isLeaf() == true && actualElement.toString().toLowerCase().contains(key.toLowerCase()) == true){
						//--- (re)expand collapsed parent if search string matches ---
						int level = actualElement.getLevel() -1;
						StringBuilder sb = new StringBuilder();
						sb.append(level -1).append(",");
						String levelString = sb.toString();
						if(expansionState.contains(levelString) == false){
							levelString = sb.append(level).append(",").toString();
							expansionState = expansionState.concat(levelString);
						}
					}
				}
			}
			//--- delete nodes ---
			for( int i=0; i < toDeleteVect.size(); i++){
				toDeleteVect.get(i).removeFromParent();
			}
		}
	
	/**
	 * Apply text filter.
	 */
	public void applyTextFilter(String filterText){
				
		DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) copyNode(filteredTextRoot);
		
		// --- save expansion state ---
		expansionState = saveExpansionState();
		
		// --- filter and update ---
		filterNodeByKey(tempNode, filterText);
		((DefaultTreeModel) getModel()).setRoot(tempNode);
		
		// --- restore expansion ---
		restoreExpansionState(expansionState);
	}
	
	/**
	 * Prepare text filter.
	 * 
	 * backup actual root node
	 */
	public void prepareTextFilter(){
		filteredTextRoot = (DefaultMutableTreeNode) copyNode((DefaultMutableTreeNode) getModel().getRoot());
	}
	/**
	 * Apply agent filter.
	 */
	public void applyAgentFilter(){
		
		// --- save expansion state ---
		expansionState = saveExpansionState();
		
		// --- filter and update ---
		filteredRoot = (DefaultMutableTreeNode) copyNode((DefaultMutableTreeNode) threadInfoStorage.getModel().getRoot());
		filterNode(filteredRoot);
		((DefaultTreeModel) getModel()).setRoot(filteredRoot);
		
		// --- restore expansion ---
		restoreExpansionState(expansionState);
	}
	
	/**
	 * Removes the agent filter.
	 */
	public void removeAgentFilter(){
		
		// --- save expansion state ---
		expansionState = saveExpansionState();
		
		// --- filter and update ---
		((DefaultTreeModel) getModel()).setRoot((DefaultMutableTreeNode) copyNode((DefaultMutableTreeNode) threadInfoStorage.getRootNode()));
		
		// --- restore expansion ---
		restoreExpansionState(expansionState);
	}
	
	public void sortThreads() {
		DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) copyNode((DefaultMutableTreeNode) getModel().getRoot());

		// --- save expansion state ---
		expansionState = saveExpansionState();

		// --- sort and update ---
		@SuppressWarnings("rawtypes")
		Enumeration e = tempNode.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
			if (node.isLeaf() == true) {
				sortNodesDescending((DefaultMutableTreeNode) node.getParent(),
						(DefaultTreeModel) getModel());
			}
		}

		((DefaultTreeModel) getModel()).setRoot(tempNode);

		// --- restore expansion ---
		restoreExpansionState(expansionState);
	}

	/**
	 * Sort nodes descending.
	 *
	 * @param node the node
	 * @param treeModel the tree model
	 */
	private void sortNodesDescending(DefaultMutableTreeNode node, DefaultTreeModel treeModel) {
		boolean unsorted = true;

		while (unsorted) {
			unsorted = false;
			for (int i = 0; i < node.getChildCount() - 1; i++) {
				ThreadInfoStorageAgent tiaFirst = (ThreadInfoStorageAgent) ((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject();
				ThreadInfoStorageAgent tiaSecond = (ThreadInfoStorageAgent) ((DefaultMutableTreeNode) node.getChildAt(i+1)).getUserObject();
				
				if (tiaFirst.getXYSeriesMap().get("TOTAL_CPU_SYSTEM_TIME").getMaxY() < tiaSecond.getXYSeriesMap().get("TOTAL_CPU_SYSTEM_TIME").getMaxY()) {
					treeModel.insertNodeInto((MutableTreeNode) node.getChildAt(i+1), node, i);
					unsorted = true;
				}
			}
		}
	}
	

	/**
	 * Save expansion state.
	 *
	 * @return the string
	 */
	private String saveExpansionState() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < ThreadInfoStorageTree.this.getRowCount(); i++) {
			if (ThreadInfoStorageTree.this.isExpanded(i)) {
				sb.append(i).append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * Restore expansion state.
	 *
	 * @param expansionState the expansion state
	 */
	private void restoreExpansionState(String expansionState) {
		String[] indexes = expansionState.split(",");
		if(indexes[0].isEmpty() == false){
			for (String st : indexes) {
	
				int row = Integer.parseInt(st);
				ThreadInfoStorageTree.this.expandRow(row);
			}
		}
	}
	
	
	/**
	 * The listener interface for receiving myMouse events.
	 * The class that is interested in processing a myMouse
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyMouseListener<code> method. When
	 * the myMouse event occurs, that object's appropriate
	 * method is invoked.
	 */
	class MyMouseListener implements MouseListener{
		
		private DefaultMutableTreeNode node;
		private String nodeName;
		private Iterator<String> iterator;
		private String folderNamePrefix;
		private XYSeriesCollection popupXYSeriesCollectionTotal;
		private XYSeriesCollection popupXYSeriesCollectionDelta;
		private XYSeriesCollection popupXYSeriesCollectionLoad;
		private TreePath path;
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e){
		}
			
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
        public void mousePressed (MouseEvent e){
			
			path =  getPathForLocation (e.getX(), e.getY());
            
            if (path != null) {
            	
            	node = (DefaultMutableTreeNode) path.getLastPathComponent();
                lastNode = node;
                Object userObject = node.getUserObject();
                nodeName = "";
                
                //--- determine class of user object-> Agent/Thread ? ---
        	    if(userObject.getClass().toString().endsWith("ThreadInfoStorageAgent")){
        	    	
	    	    	if (SwingUtilities.isRightMouseButton(e)){
		            	// --- show rightClickMenu on right-click on agents ---
		            	if (path != null) {
			                //--- determine class of user object ---
		            	    if(userObject.getClass().toString().endsWith("ThreadInfoStorageAgent")){
				                Rectangle pathBounds = getPathBounds ( path );
				                if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())){
				                	getRightClickMenu().show(ThreadInfoStorageTree.this, pathBounds.x, pathBounds.y + pathBounds.height);  
				                }
		            	    }
		            	}
		            }else if (e.getClickCount() == 2){
		            	// --- toggle thread/agent chart visibility on main chart ---
	        	    	ThreadInfoStorageAgent tis = (ThreadInfoStorageAgent)userObject;
	        	    	boolean isSelected = leafSelected.get(tis.getName()).booleanValue();
	        	    	leafSelected.put(tis.getName(),new Boolean(!isSelected));
	        	    	nodeName = tis.getName();
	        	    	
		                iterator = threadInfoStorage.getMapAgent().get(nodeName).getXYSeriesMap().keySet().iterator();
		                
		                if(threadInfoStorage.getMapAgent().get(nodeName) != null){
		                	
	                	    while(iterator.hasNext()){
	                	    	String next = iterator.next();
	                	    	XYSeries series = threadInfoStorage.getMapAgent().get(nodeName).getXYSeriesMap().get(next);
	                	    	
	                	    	if(next.contains("TOTAL")){
	                	    		//---toggle ---
	                    	    	if(getSeriesChartsTotal().indexOf(series.getKey()) == -1){
	                    	    		getSeriesChartsTotal().addSeries(series);
	                    	    	}else{
	                    	    		getSeriesChartsTotal().removeSeries(series);
	                    	    	}
	                	    	}else if(next.contains("DELTA")){
	                	    		//---toggle ---
	                    	    	if(getSeriesChartsDelta().indexOf(series.getKey()) == -1){
	                    	    		getSeriesChartsDelta().addSeries(series);
	                    	    	}else{
	                    	    		getSeriesChartsDelta().removeSeries(series);
	                    	    	}
	                    	    }
	                	    }								
						}
		            }
	    	    	return;
        	    }
        	    
	    		popupXYSeriesCollectionTotal = new XYSeriesCollection();
	    	    popupXYSeriesCollectionDelta = new XYSeriesCollection();
	    	    popupXYSeriesCollectionLoad = new XYSeriesCollection();
	    	    
	    	    Object extraObject = null;
	
	    	    if(userObject.getClass().toString().endsWith("ThreadInfoStorageCluster")){
	    	    	ThreadInfoStorageCluster tis = (ThreadInfoStorageCluster)userObject;
	    	    	nodeName = tis.getName();
	    	    	folderNamePrefix = "";
		    		
		            iterator = threadInfoStorage.getMapCluster().get(nodeName).getXYSeriesMap().keySet().iterator();
		            
		            if(threadInfoStorage.getMapCluster().get(nodeName) != null){
		            	
		        	    while(iterator.hasNext()){
		        	    	String next = iterator.next();
		        	    	XYSeries series = threadInfoStorage.getMapCluster().get(nodeName).getXYSeriesMap().get(next);
		        	    	if(next.contains("TOTAL")){
		        	    		popupXYSeriesCollectionTotal.addSeries(series);
		        	    	}else if(next.contains("DELTA")){
		        	    		popupXYSeriesCollectionDelta.addSeries(series);		    	                    	    		
		        	    	}else if(next.contains("LOAD")){
		        	    		popupXYSeriesCollectionLoad.addSeries(series);		    	                    	    		
		        	    	}
		        	    }
		            }	
			    }else if(userObject.getClass().toString().endsWith("ThreadInfoStorageMachine")){
	    	    	//---display series for container, JVM, machine or cluster in new window ---
			    	ThreadInfoStorageMachine tis = (ThreadInfoStorageMachine)userObject;
	    	    	nodeName = tis.getName();
		    		folderNamePrefix = "Machine: ";
		    		extraObject = threadInfoStorage.getMapMachine().get(nodeName);
		            iterator = threadInfoStorage.getMapMachine().get(nodeName).getXYSeriesMap().keySet().iterator();
		            
		            if(threadInfoStorage.getMapMachine().get(nodeName) != null){
		            	
		        	    while(iterator.hasNext()){
		        	    	String next = iterator.next();
		        	    	XYSeries series = threadInfoStorage.getMapMachine().get(nodeName).getXYSeriesMap().get(next);
		        	    	if(next.contains("TOTAL")){
		        	    		popupXYSeriesCollectionTotal.addSeries(series);
		        	    	}else if(next.contains("DELTA")){
		        	    		popupXYSeriesCollectionDelta.addSeries(series);		    	                    	    		
		        	    	}else if(next.contains("LOAD")){
		        	    		popupXYSeriesCollectionLoad.addSeries(series);		    	                    	    		
		        	    	}
		        	    }
		            }
				}else if(userObject.getClass().toString().endsWith("ThreadInfoStorageJVM")){
					ThreadInfoStorageJVM tis = (ThreadInfoStorageJVM)userObject;
	    	    	nodeName = tis.getName();
		    		folderNamePrefix = "JVM: ";
		    		
		            iterator = threadInfoStorage.getMapJVM().get(nodeName).getXYSeriesMap().keySet().iterator();
		            
		            if(threadInfoStorage.getMapJVM().get(nodeName) != null){
		            	
		        	    while(iterator.hasNext()){
		        	    	String next = iterator.next();
		        	    	XYSeries series = threadInfoStorage.getMapJVM().get(nodeName).getXYSeriesMap().get(next);
		        	    	if(next.contains("TOTAL")){
		        	    		popupXYSeriesCollectionTotal.addSeries(series);
		        	    	}else if(next.contains("DELTA")){
		        	    		popupXYSeriesCollectionDelta.addSeries(series);		    	                    	    		
		        	    	}else if(next.contains("LOAD")){
		        	    		popupXYSeriesCollectionLoad.addSeries(series);		    	                    	    		
		        	    	}
		        	    }
		        	}	
			    }else if(userObject.getClass().toString().endsWith("ThreadInfoStorageContainer")){
			    	ThreadInfoStorageContainer tis = (ThreadInfoStorageContainer)userObject;
	    	    	nodeName = tis.getName();
		    		folderNamePrefix = "Container: ";
		    		
		            iterator = threadInfoStorage.getMapContainer().get(nodeName).getXYSeriesMap().keySet().iterator();
		            
		            if(threadInfoStorage.getMapContainer().get(nodeName) != null){
		            	
		        	    while(iterator.hasNext()){
		        	    	String next = iterator.next();
		        	    	XYSeries series = threadInfoStorage.getMapContainer().get(nodeName).getXYSeriesMap().get(next);
		        	    	if(next.contains("TOTAL")){
		        	    		popupXYSeriesCollectionTotal.addSeries(series);
		        	    	}else if(next.contains("DELTA")){
		        	    		popupXYSeriesCollectionDelta.addSeries(series);		    	                    	    		
		        	    	}else if(next.contains("LOAD")){
		        	    		popupXYSeriesCollectionLoad.addSeries(series);		    	                    	    		
		        	    	}
		        	    }
		        	}	
        	    }
    	    	// --- create scroll-pane in pop-up window ---
        	    new ThreadInfoStorageScrollPane(popupXYSeriesCollectionDelta, popupXYSeriesCollectionTotal, popupXYSeriesCollectionLoad, true, folderNamePrefix+nodeName, extraObject);
    	    }
	    }
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e){	
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e){
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e){
		}		
	}	
	
	/**
	 * The Class MyTreeCellRenderer.
	 */
	class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		
	    private static final long serialVersionUID = 967937360839244309L;
	    
	    private boolean isSelected;
	    private Icon activeIcon;
	    private Icon inActiveIcon;
	    private Icon agentActiveIcon;
	    private Icon agentInActiveIcon;

	    /**
    	 * Instantiates a new my tree cell renderer.
    	 */
    	public MyTreeCellRenderer(){
    		isSelected = false;
	    	this.activeIcon = GlobalInfo.getInternalImageIcon("StatGreen.png");
	    	this.inActiveIcon = GlobalInfo.getInternalImageIcon("StatRed.png");
	    	this.agentActiveIcon= new ImageIcon(((GlobalInfo.getInternalImageIcon("AgentGUIGreen.png")).getImage()).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
	    	this.agentInActiveIcon= new ImageIcon(((GlobalInfo.getInternalImageIcon("AgentGUI.png")).getImage()).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
	    }

	    /* (non-Javadoc)
    	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
    	 */
    	@Override
	    public Component getTreeCellRendererComponent(JTree tree, Object value,
	            boolean sel, boolean expanded, boolean leaf, int row,
	            boolean hasFocus) {

	        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
	                row, hasFocus);
	        
	        Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
	        if(nodeObj != null){
		        if(nodeObj.getClass().toString().endsWith("ThreadInfoStorageAgent")){
	    	    	ThreadInfoStorageAgent tia = (ThreadInfoStorageAgent)nodeObj;
	    	    	if(leafSelected.containsKey(tia.getName()) == true){
	    	    		isSelected = leafSelected.get(tia.getName()).booleanValue();
	    	    	}else{
	    	    		isSelected = false;
	    	    		leafSelected.put(tia.getName(), new Boolean(false));    	    		
	    	    	}
	    	    	
	    	    	if(isSelected == true){
	    	    		if(tia.isAgent()){
	    	    			setIcon(agentActiveIcon);
	    	    		}else{
	    	    			setIcon(activeIcon);
	    	    		}
	    	    	}else{
	    	    		if(tia.isAgent()){
	    	    			setIcon(agentInActiveIcon);
	    	    		}else{
	    	    			setIcon(inActiveIcon);
	    	    		}
	    	    	}
	    	    	String[] classname = tia.getClassName().split("\\.");
	    	    	setToolTipText("Agent: " + classname[classname.length-1] + " [" +threadInfoStorage.getNoOfAgentsPerClass().get(tia.getClassName()) + "]");
	    	    }	
	    	}
	        return this;
	    }
	}
}
