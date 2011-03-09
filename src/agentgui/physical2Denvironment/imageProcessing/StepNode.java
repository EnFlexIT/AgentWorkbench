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
	
	StepNode parent; // For Backtracing
	int direction;
	LinkedList<StepNode> furtherPossiblities=new LinkedList<StepNode>();
	
	
	float x,y; // Position to be moved
	
	public void addNode(StepNode stepNode)
	{
		furtherPossiblities.add(stepNode);
	}
	
	public LinkedList<StepNode> getFurtherPossiblities() {
		return furtherPossiblities;
	}

	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public boolean isStoppInvestigating() {
		return stoppInvestigating;
	}
	public void setStoppInvestigating(boolean stoppInvestigating) {
		this.stoppInvestigating = stoppInvestigating;
	}
	public StepNode getParent() {
		return parent;
	}
	public void setParent(StepNode parent) {
		this.parent = parent;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void print()
	{
		System.out.println("X:"+x +",Y"+y+"direction:"+this.direction +",XINDEX:"+xIndex +"," + this.YIndex);
	}
	
	
	
	

	public double getDistanceToOrginal() {
		return distanceToOrginal;
	}

	public void setDistanceToOrginal(double distanceToOrginal) {
		this.distanceToOrginal = distanceToOrginal;
	}

	@Override
	public boolean equals(Object obj) {
		StepNode tmp=(StepNode) obj;
//		System.out.println(tmp.getX()==this.getX()&&tmp.getY()==this.getY());
		boolean flag= tmp.getXIndex()==this.getXIndex()&&tmp.getYIndex()==this.getYIndex()&&this.gridIndex==tmp.gridIndex;
		
		return flag;
	}
	
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

	public double getTotal_distance() {
		return total_distance;
	}

	public void setTotal_distance(double total_distance) {
		this.total_distance = total_distance;
	}
	

	public int getYIndex() {
		return YIndex;
	}

	public void setYIndex(int index) {
		YIndex = index;
	}

	public int getXIndex() {
		return xIndex;
	}

	public void setXIndex(int index) {
		xIndex = index;
	}

	public int getGridIndex() {
		return gridIndex;
	}

	public void setGridIndex(int gridIndex) {
		this.gridIndex = gridIndex;
	}
	
	public boolean is_geo_equals(Object obj)
	{
		StepNode stepNode=(StepNode) obj;
		return this.x==stepNode.x&&this.y==stepNode.y;
	}
}
