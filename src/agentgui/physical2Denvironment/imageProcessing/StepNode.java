/**
 * It represents a node which is used for path finding
 * 
 * @author Tim
 *
 */
 
package agentgui.physical2Denvironment.imageProcessing;

import java.util.LinkedList;



public class StepNode {
	
	private double distance;
	private double distanceToOrginal;
	private double total_distance;
	private int xIndex;
	private int YIndex;
    private boolean stoppInvestigating=false;
	private int gridIndex=-1;
	private StepNode parent; // For Backtracing
	private int direction;
	private LinkedList<StepNode> furtherPossiblities=new LinkedList<StepNode>();
	private float x,y; // Position to be moved
	
	/**
	 * Adds nodes to a list
	 * @param stepNode
	 */
	public void addNode(StepNode stepNode)
	{
		furtherPossiblities.add(stepNode);
	}
	
	
	/**
	 * Returns all Nodes which are part of this nodes
	 * @return
	 */
	public LinkedList<StepNode> getFurtherPossiblities() {
		return furtherPossiblities;
	}

	
	/**
	 * Returns the distance value
	 * @return The distances value
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * Sets the distances value
	 * @param distance The distance to the target
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	/**
	 * Could be used for depth or breadth searches. It can be used to stop investigating the current node
	 * @return true or false
	 */
	public boolean isStoppInvestigating() {
		return stoppInvestigating;
	}
	
	/** Set the value to true or false
	 * @param stoppInvestigating
	 */
	public void setStoppInvestigating(boolean stoppInvestigating) {
		this.stoppInvestigating = stoppInvestigating;
	}
	
	/**
	 * Returns the parent of the node 
	 * @return The parent of the node
	 */
	public StepNode getParent() {
		return parent;
	}
	
	/**
	 * Sets the parent of a node 
	 * @param parent 
	 */
	public void setParent(StepNode parent) {
		this.parent = parent;
	}
	
	/*
	 * Returns the direction (left ,right...)
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}
	
	/** Sets the direction
	 * @param direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	/** Returns the X position of the node
	 * @return The x value
	 */
	public float getX() {
		return x;
	}
	/**
	 * Sets the x position of the node 
	 * @param x the x position
	 */
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * returns the y position 
	 * @return the y position
	 */
	public float getY() {
		return y;
	}
	/** Sets the y position of the node
	 * @param y the y position
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 *  Prints the x,y, direction and the x/y Index
	 */
	public void print()
	{
		System.out.println("X:"+x +",Y"+y+"direction:"+this.direction +",XINDEX:"+xIndex +"," + this.YIndex);
	}
		

	/**
	 * Returns the distance to starting point
	 * @return the distance to the starting point
	 */
	public double getDistanceToOrginal() {
		return distanceToOrginal;
	}

	/**
	 * Sets  distance to the starting point
	 * @param distanceToOrginal
	 */
	public void setDistanceToOrginal(double distanceToOrginal) {
		this.distanceToOrginal = distanceToOrginal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		StepNode tmp=(StepNode) obj;
		boolean flag= tmp.getXIndex()==this.getXIndex()&&tmp.getYIndex()==this.getYIndex()&&this.gridIndex==tmp.gridIndex;
		return flag;
	}
	
	/**
	 * Checks if even the parents of a nodes are equal
	 * @param tmp
	 * @return
	 */
	public boolean is_deep_equals(StepNode tmp)
	{
	 boolean flag=tmp.getXIndex()==this.getXIndex()&&tmp.getYIndex()==this.getYIndex();	
	 if(this.getParent()!=null&&tmp.getParent()!=null)
	 {
		
		 boolean flag2=tmp.getParent().getXIndex()==this.getParent().getXIndex()&&tmp.getParent().getYIndex()==this.getParent().getYIndex();
	
		 return flag2&&flag;
	 }
	 if(this.parent==null&&tmp.parent==null)
	 {
	
		 return flag;
	 }
	 if(this.parent==null&&tmp.parent!=null)
	 {
		 
		 return flag;
	 }
	 if(this.parent!=null&&tmp.parent==null)
	 {
		
		return false;
	   	 
	 }
	 
	 return false;
	}

	/**
	 * Returns the combined distance of the distance to the start and to the target.
	 * @return the combined distance of the distance to the start and to the target
	 */
	public double getTotal_distance() {
		return total_distance;
	}

	/**
	 * Sets the total distance
	 * @param total_distance
	 */
	public void setTotal_distance(double total_distance) {
		this.total_distance = total_distance;
	}
	

	/**
	 * Can be used if the path finding algorithm uses a grid. Returns the Y Index of the grid
	 * @return
	 */
	public int getYIndex() {
		return YIndex;
	}

	/**
	 *  Can be used if the path finding algorithm uses a grid. Sets the Y Index of the grid
	 * @param index
	 */
	public void setYIndex(int index) {
		YIndex = index;
	}
	/**
	 * Can be used if the path finding algorithm uses a grid. Returns the X Index of the grid
	 * @return
	 */
	public int getXIndex() {
		return xIndex;
	}

	/**
	 *  Can be used if the path finding algorithm uses a grid. Sets the X Index of the grid
	 * @param index
	 */
	public void setXIndex(int index) {
		xIndex = index;
	}

	
	/**
	 * Compares just the location of the node
	 * @param obj which must be a stepnode
	 * @return
	 */
	public boolean is_geo_equals(Object obj)
	{
		StepNode stepNode=(StepNode) obj;
		return this.x==stepNode.x&&this.y==stepNode.y;
	}
}
