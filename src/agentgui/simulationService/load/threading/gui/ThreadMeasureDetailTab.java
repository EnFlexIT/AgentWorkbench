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
import java.util.HashMap;
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
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import agentgui.core.application.Application;
import agentgui.simulationService.load.threading.ThreadInfoStorage;
import agentgui.simulationService.load.threading.ThreadInfoStorageAgent;
import agentgui.simulationService.load.threading.ThreadInfoStorageCluster;
import agentgui.simulationService.load.threading.ThreadInfoStorageContainer;
import agentgui.simulationService.load.threading.ThreadInfoStorageJVM;
import agentgui.simulationService.load.threading.ThreadInfoStorageMachine;

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
	
	/** The non agents. */
	public final String NON_AGENTS   = "all-non-agent-classes";
	
	/** The thread info storage. */
	private ThreadInfoStorage threadInfoStorage;
	
	/** The left scroll pane. */
	private JScrollPane leftScrollPane;
	
	/** The right scroll pane. */
	private ThreadInfoScrollPane rightScrollPane;
	
	/** The J panel filter. */
	private JPanel JPanelFilter;
	
	/** The j radio button no filter. */
	private JRadioButton jRadioButtonNoFilter;
	
	/** The j radio button filter agents. */
	private JRadioButton jRadioButtonFilterAgents;
	
	/** The j tree thread info storage. */
	private JTree jTreeThreadInfoStorage;
	
	/** The split pane. */
	private JSplitPane splitPane;
	
	/** The Thread info storage xy series charts total. */
	private XYSeriesCollection ThreadInfoStorageXYSeriesChartsTotal;
	
	/** The Thread info storage xy series charts delta. */
	private XYSeriesCollection ThreadInfoStorageXYSeriesChartsDelta;
	
	/** The filtered root. */
	private DefaultMutableTreeNode filteredRoot;
	
	/** The unfiltered root. */
	private DefaultMutableTreeNode unfilteredRoot;
	 
	
	/** The right click menu. */
	private JPopupMenu rightClickMenu;
	
	/** The view single. */
	private JMenuItem viewSingle;
	
	/** The view agent class. */
	private JMenuItem viewAgentClass;
	
	/** The last node. */
	private DefaultMutableTreeNode lastNode = null;
	
	/** The leaf selected. */
	private HashMap<String, Boolean> leafSelected;
//	private Enumeration<TreePath> expansionState;
	
	/**
	 * Instantiates a new thread measure detail tab.
	 *
	 * @param threadInfoStorage the thread info storage
	 */
	public ThreadMeasureDetailTab(ThreadInfoStorage threadInfoStorage) {
		this.threadInfoStorage = threadInfoStorage;
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setLayout(new BorderLayout(0, 0));
		this.add(getSplitPane(), BorderLayout.CENTER);
		this.add(getJPanelFilter(), BorderLayout.SOUTH);
		threadInfoStorage.getTreeModel().addTreeModelListener(new MyTreeModelListener());
		leafSelected = new HashMap<String, Boolean>();
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
			getJRadioButtonNoFilter().setSelected(true);
			getJRadioButtonFilterAgents().setSelected(false);
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
	 * Gets the right click rightClickMenu.
	 * @return the right click rightClickMenu
	 */
	private JPopupMenu getRightClickMenu(){
		if(rightClickMenu == null){
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
	
	/**
	 * Gets the thread protocol stack xy total series charts.
	 * @return the thread protocol stack xy series charts
	 */
	private XYSeriesCollection getThreadInfoStorageXYSeriesChartsTotal(){
		if(ThreadInfoStorageXYSeriesChartsTotal == null){
			ThreadInfoStorageXYSeriesChartsTotal = new XYSeriesCollection(null);
		}
		
		return ThreadInfoStorageXYSeriesChartsTotal;
	}
	/**
	 * Gets the thread protocol stack xy delta series charts .
	 * @return the thread protocol stack xy series charts
	 */
	private XYSeriesCollection getThreadInfoStorageXYSeriesChartsDelta(){
		if(ThreadInfoStorageXYSeriesChartsDelta == null){
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
			// --- save expansion state ---
//			expansionState = saveExpansionState(getJTreeThreadInfoStorage());
			// --- restore unfiltered node ---
			DefaultTreeModel model = threadInfoStorage.getTreeModel();
			model.setRoot(unfilteredRoot);
			//--- backup unfiltered root-node ---
			model.nodeChanged(unfilteredRoot);
//			restoreExpansionState(getJTreeThreadInfoStorage(), expansionState);
			jTreeThreadInfoStorage.expandRow(0);
			jTreeThreadInfoStorage.expandRow(1);
			jTreeThreadInfoStorage.expandRow(2);
			jTreeThreadInfoStorage.expandRow(3);

		}else if(ae.getSource()==this.getJRadioButtonFilterAgents()){
			// --- apply filter ---
			// --- save expansion state ---
//			expansionState = saveExpansionState(getJTreeThreadInfoStorage());	
			//---filter---
			removeNonAgentsFromNode(filteredRoot);
			//--- set filtered node to model ---
			DefaultTreeModel model = threadInfoStorage.getTreeModel();
			model.setRoot(filteredRoot);
			//--- restore expansion ---
//			restoreExpansionState(getJTreeThreadInfoStorage(), expansionState);	
			jTreeThreadInfoStorage.expandRow(0);
			jTreeThreadInfoStorage.expandRow(1);
			jTreeThreadInfoStorage.expandRow(2);
			jTreeThreadInfoStorage.expandRow(3);
//			model.nodeChanged(this.filteredRoot);
//			model.reload();
			
		}else if ((ae.getSource()==viewSingle) ||(ae.getSource()==viewAgentClass)){
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
	        	    		className =  NON_AGENTS;
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
	    	    new ThreadInfoScrollPane(popupXYSeriesCollectionDelta, popupXYSeriesCollectionTotal, null, true, folderNamePrefix+className);
			}
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
//                tree.expandPath(treePath);
                //If it is not a leaf
                if(treePath.getLastPathComponent() != null && !tree.getModel().isLeaf(treePath.getLastPathComponent())){
                    TreePath currentPath = tree.getNextMatch(treePath.getLastPathComponent().toString(),0,Position.Bias.Forward );
                    tree.expandPath(currentPath);
                }
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
	 */
	class  MyTreeModelListener implements TreeModelListener {

		/* (non-Javadoc)
		 * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
		 */
		@Override
		public void treeNodesChanged(TreeModelEvent e) {
			// --- backup node ---
			DefaultTreeModel model = threadInfoStorage.getTreeModel();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
			unfilteredRoot = (DefaultMutableTreeNode) CopyNode(node);
			filteredRoot   = (DefaultMutableTreeNode) CopyNode(node);
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
		 */
		@Override
		public void treeNodesInserted(TreeModelEvent e) {
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
		 */
		@Override
		public void treeNodesRemoved(TreeModelEvent e) {
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
		 */
		@Override
		public void treeStructureChanged(TreeModelEvent e) {
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
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,getLeftScrollPane(),getRightScrollPane());			
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
			leftScrollPane.setMinimumSize(new Dimension(222,333));
		}
		return leftScrollPane;
		
	}
	
	/**
	 * Gets the right scroll pane.
	 * @return the right scroll pane
	 */
	private ThreadInfoScrollPane getRightScrollPane(){
		if (rightScrollPane == null) {
			rightScrollPane = new ThreadInfoScrollPane(getThreadInfoStorageXYSeriesChartsDelta(), getThreadInfoStorageXYSeriesChartsTotal(), null, false, "");
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
			
			path = jTreeThreadInfoStorage.getPathForLocation (e.getX(), e.getY());
            
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
				                Rectangle pathBounds = jTreeThreadInfoStorage.getUI ().getPathBounds ( jTreeThreadInfoStorage, path );
				                if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())){
				                	getRightClickMenu().show(jTreeThreadInfoStorage, pathBounds.x, pathBounds.y + pathBounds.height);  
				                }
		            	    }
		            	}
		            }else{// --- toggle thread/agent chart visibility on main chart ---
	        	    	ThreadInfoStorageAgent tis = (ThreadInfoStorageAgent)userObject;
	        	    	boolean isSelected = leafSelected.get(tis.toString()).booleanValue();
	        	    	leafSelected.put(tis.toString(),new Boolean(!isSelected));
	        	    	nodeName = tis.getName();
	        	    	
		                iterator = threadInfoStorage.getMapAgent().get(nodeName).getXYSeriesMap().keySet().iterator();
		                
		                if(threadInfoStorage.getMapAgent().get(nodeName) != null){
		                	
	                	    while(iterator.hasNext()){
	                	    	String next = iterator.next();
	                	    	XYSeries series = threadInfoStorage.getMapAgent().get(nodeName).getXYSeriesMap().get(next);
	                	    	
	                	    	if(next.contains("TOTAL")){
	                	    		//---toggle ---
	                    	    	if(getThreadInfoStorageXYSeriesChartsTotal().indexOf(series.getKey()) == -1){
	                    	    		getThreadInfoStorageXYSeriesChartsTotal().addSeries(series);
	                    	    	}else{
	                    	    		getThreadInfoStorageXYSeriesChartsTotal().removeSeries(series);
	                    	    	}
	                	    	}else if(next.contains("DELTA")){
	                	    		//---toggle ---
	                    	    	if(getThreadInfoStorageXYSeriesChartsDelta().indexOf(series.getKey()) == -1){
	                    	    		getThreadInfoStorageXYSeriesChartsDelta().addSeries(series);
	                    	    	}else{
	                    	    		getThreadInfoStorageXYSeriesChartsDelta().removeSeries(series);
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
        	    new ThreadInfoScrollPane(popupXYSeriesCollectionDelta, popupXYSeriesCollectionTotal, popupXYSeriesCollectionLoad, true, folderNamePrefix+nodeName);
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
	    	final String PathImage = Application.getGlobalInfo().getPathImageIntern();
	    	this.activeIcon = new ImageIcon(getClass().getResource(PathImage + "StatGreen.png"));
	    	this.inActiveIcon = new ImageIcon(getClass().getResource(PathImage + "StatRed.png"));
	    	this.agentActiveIcon= new ImageIcon(((new ImageIcon(getClass().getResource(PathImage + "AgentGUIGreen.png"))).getImage()).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
	    	this.agentInActiveIcon= new ImageIcon(((new ImageIcon(getClass().getResource(PathImage + "AgentGUI.png"))).getImage()).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
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
    	    	if(leafSelected.containsKey(tia.toString()) == true){
    	    		isSelected = leafSelected.get(tia.toString()).booleanValue();
    	    	}else{
    	    		isSelected = false;
    	    		leafSelected.put(tia.toString(), new Boolean(false));    	    		
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
    	    }	 
	        return this;
	    }
	}
}
