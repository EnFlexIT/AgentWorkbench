/** 
 * This class provides the functionality to calculate a way from point A to Point B based on the
 * astar algoritmn. The used heuristic is the distance between the current position and the starting point and
 * the distance to the destination point.
 * 
 * @author Tim Lewen
 * 
 */
package agentgui.physical2Denvironment.imageProcessing;

import jade.core.AID;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.ontology.PositionUpdate;
import agentgui.physical2Denvironment.ontology.StaticObject;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.utils.EnvironmentWrapper;



public class ImageHelper { 

	private static final long serialVersionUID = -9171029311830052421L;

	public static final int DIRECTION_FORWARD=0;
	public static final int DIRECTION_BACKWARD=1;
	public static final int DIRECTION_LEFT=2;
	public static final int DIRECTION_RIGHT=3;
	public static final int DIRECTION_UP_LEFT=4;
	public static final int DIRECTION_UP_RIGHT=5;
	public static final int DIRECTION_DOWN_LEFT=6;
	public static final int DIRECTION_DOWN_RIGHT=7;
	private static boolean READ_WORLD=false;
	private static boolean IS_IN_METHOD=false;
	private static boolean FIRST_CALL=true;
	int divideHeight;
	int divideWidth;
	double lastDistance=-20;
	private BufferedImage evn=null;
	private BufferedImage plan=null;
	int endung=0;
	private float firstX;
	private float firstY;
	boolean first=false;
	boolean igoreNoDistanceChange=false;
	int counter=0;
	int compareDirectionsCounter=5;
	int directionsCounter=0;
	Position from=null;
	Position to=null;
	String fromID="";
	String toID="";
	EnvironmentProviderHelper helper;
	
	
	/**
	 * @param fromID ID of the StartPoint
	 * @param toID   ID of the destination
	 * @param helper The enviromentHelper
	 */
	public ImageHelper(String fromID,String toID,	EnvironmentProviderHelper helper )
	{
	
		this.fromID=fromID;
		this.toID=toID;
		this.helper=helper;
	
	}
	
	public ImageHelper(EnvironmentProviderHelper helper)
	{
		this.helper=helper;
	}
	
	public ImageHelper() 
	{
		
	}
	
	public ImageHelper(Position destination, String ownID)
	{
		
	}
		
     /**
      *  Uses recursion to delete node and child nodes 
     * @param doc The SVG Document
     * @param node The node which should be deleted
     */
    private void deleteNodes(Document doc,Node node)
     {
    	 NodeList list=node.getChildNodes();
    	 for(int i=0;i<list.getLength();i++)
    	 {
    	 	 node.removeChild(list.item(i));
    	 }
    	  if(list.getLength()!=0)
    	  {
    		  this.deleteNodes(doc, node);
    	  }
     }
	

		
	
	
	/**
	 * It's used as an heuristic
	 * 
	 * @param target_x The X position of the target
	 * @param target_y The Y position of the target
	 * @param x Own X Position
	 * @param y Own Y Position
	 * @return
	 */
	private double getDistance(float target_x,float target_y,float x,float y)
	{
		float xPosDiff=Math.abs(target_x-x);
		float yPosDiff=Math.abs(target_y-y);
		return	Math.abs( Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff));
	}
	


	/**
	 * It's used as an heuristic
	 * 
	 * @param target_x The X position of the target
	 * @param target_y The Y position of the target
	 * @param x Own X Position
	 * @param y Own Y Position
	 * @return
	 */
	private float getManhattanDistance(float target_x,float target_y,float x,float y)
	{
		float dx = Math.abs(target_x - x);
	    float dy = Math.abs(target_y- y);
	    float heuristic = dx+dy;
	    return heuristic;
	}
	
	
	/**
	 * Returns a node for the open/closed List
	 * @param x Own X Position
	 * @param y Own Y Position 
	 * @param parent Parent node
	 * @param target_x Target X Position
	 * @param target_y Target Y Position
	 * @param ownDirection The directions the agent is moving to
	 * @param width Width of the agent
	 * @param height Height of the agent
	 * @param startXIndex Start X Index
	 * @param startYIndex Start Y Index
	 * @return
	 */
	private agentgui.physical2Denvironment.imageProcessing.StepNode fillNode(float x, float y,agentgui.physical2Denvironment.imageProcessing.StepNode parent,float target_x,float target_y,int ownDirection,float width,float height,int startXIndex,int startYIndex)
	{
								
		agentgui.physical2Denvironment.imageProcessing.StepNode result=new agentgui.physical2Denvironment.imageProcessing.StepNode();	
		double first_distance= this.getManhattanDistance(target_x, target_y, x, y);          //this.getDistance(target_x, target_y, x, y);
		double manhattan_distance_to_origin=this.getManhattanDistance(x, y,parent.getX(), parent.getY());
		result.setDirection(ownDirection);
		result.setDistance(first_distance+manhattan_distance_to_origin);
		result.setParent(parent);
		result.setStoppInvestigating(false);
		result.setX(x);
		result.setY(y);
		double distance=this.getDistance( x,y,firstX,firstY);
		float dx = Math.abs(target_x - x);
	    float dy = Math.abs(target_y- y);
	    float heuristic = dx+dy;
		result.setDistanceToOrginal(distance); 
		result.setTotal_distance(heuristic);
		return result;

	}

	
	
	/**
	 * Another possible heuristic
	 * @param startIndexX X startIndex in a grid
	 * @param startIndexY Y startIndex in a grid
	 * @param currentIndexX  Current index of the grid
	 * @param currentIndexY Current index of the grid
	 * @return
	 */
	private int getHCost(int startIndexX,int startIndexY, int currentIndexX, int currentIndexY)
	{
	
		int val=Math.abs(startIndexX-currentIndexX)+Math.abs(startIndexY-currentIndexY);
		return val*10;
	}
	
	
	/**
	 * Calculates the path
	 * @param id Agent ID
	 * @param target_x  Target X postion
	 * @param target_y Target Y position
	 * @param width width of the agent
	 * @param height height of the agent
	 * @param target_id ID of the target
	 * @param direction The direction the agent is moving
	 * @param lookAhead How many pixels should be looked ahead
	 * @param parent Parent Node
	 * @param pixel The color of the pixel
	 * @return
	 * @throws Exception
	 */
	private synchronized agentgui.physical2Denvironment.imageProcessing.StepNode withoutGrid(String id,float target_x,float target_y,float width,float height, int direction , float lookAhead, agentgui.physical2Denvironment.imageProcessing.StepNode parent,int pixel) throws Exception
	{
			
		while(this.evn==null)
		{
			
			System.out.println(id + " liest Welt nochmal");
			this.evn=ImageIO.read(new File("myWorld.jpg"));	
			
			
			
		}
		ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> openList=new ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode>();
		ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> closedList=new ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode>();
		float worldWidth=helper.getEnvironment().getRootPlayground().getSize().getWidth();
		float worldHeight=helper.getEnvironment().getRootPlayground().getSize().getHeight();
	    counter++;
		Document doc=helper.getSVGDoc();
		// Calculate pos
		final float xStart= parent.getX();
		final float yStart= parent.getY();
		float currentXPos=xStart;
		float currentYPos=yStart; 
		final int listXIndex= (int) (parent.getX()/lookAhead);
		final int listYIndex=  (int) (parent.getY()/lookAhead);
		agentgui.physical2Denvironment.imageProcessing.StepNode current=parent;
		closedList.add(current);
		// While you're not even close
		while(this.getDistance(target_x, target_y, currentXPos, currentYPos)>10) 
		{				
			
			// Up left
			agentgui.physical2Denvironment.imageProcessing.StepNode new_current=null;
			if(currentXPos-lookAhead>0&&currentYPos-lookAhead>0)
			{
				if(!this.isInList(openList, closedList, currentXPos-lookAhead, currentYPos-lookAhead,ImageHelper.DIRECTION_UP_LEFT))
			     {
					 new_current=this.fillNode(currentXPos-lookAhead, currentYPos-lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_UP_LEFT,width,height,listXIndex,listYIndex);
					 if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_UP_LEFT, Color.gray.getRGB()))
					  {	
						
					  	// openList.add(new_current);
					  }
					 else
					 {
						 if(new_current!=null)
						 {
						 //this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
					 
						 }
					 }
					
			     }
			}
			
			// Up Right
			 new_current=null;
			if(currentXPos+lookAhead<worldWidth&&currentYPos-lookAhead>=0)
			{
				if(!this.isInList(openList, closedList, currentXPos+lookAhead, currentYPos-lookAhead,ImageHelper.DIRECTION_UP_RIGHT))
			     {
					 new_current=this.fillNode(currentXPos+lookAhead, currentYPos-lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_UP_RIGHT,width,height,listXIndex,listYIndex);
					 if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_UP_RIGHT, Color.gray.getRGB()))
					  {	
						
					  	 //openList.add(new_current);
					  }
					 else
					 {
						 if(new_current!=null)
						 {
						// this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
					 
						 }
					 }
					
			     }
			}
			
			// Down left
			 new_current=null;
			if(currentXPos-lookAhead>=0&&currentYPos+lookAhead<=worldHeight)
			{
				if(!this.isInList(openList, closedList, currentXPos-lookAhead, currentYPos+lookAhead,ImageHelper.DIRECTION_DOWN_LEFT))
			     {
					new_current=this.fillNode(currentXPos-lookAhead, currentYPos+lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_DOWN_LEFT,width,height,listXIndex,listYIndex);
					 if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_DOWN_LEFT, Color.gray.getRGB()))
					  {	
					     
					  	 //openList.add(new_current);
					  }
					 else
					 {
						 if(new_current!=null)
						 {
					//	 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
					 
						 }
					 }
					
			     }
			}
			
			
			// DOWN right
			 new_current=null;
			if(currentXPos+lookAhead<=worldWidth&&currentYPos+lookAhead<=worldHeight)
			{
				if(!this.isInList(openList, closedList, currentXPos-lookAhead, currentYPos+lookAhead,ImageHelper.DIRECTION_DOWN_RIGHT))
			     {
					 new_current=this.fillNode(currentXPos+lookAhead, currentYPos+lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_DOWN_RIGHT,width,height,listXIndex,listYIndex);
					 if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_DOWN_RIGHT, Color.gray.getRGB()))
					  {	
						
					  	// openList.add(new_current);
					  }
					 else
					 {
						 if(new_current!=null)
						 {
					//	 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
					 
						 }
					 }
					 
					
			     }
			}	
			
			// Left
			 new_current=null;
			if(currentXPos-lookAhead>=0)
			{ 
				if(!this.isInList(openList, closedList, currentXPos-lookAhead, currentYPos,ImageHelper.DIRECTION_LEFT))
			     {
					 new_current=this.fillNode(currentXPos-lookAhead, currentYPos, current, target_x, target_y, ImageHelper.DIRECTION_LEFT,width,height,listXIndex,listYIndex);
					 if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_LEFT, Color.gray.getRGB()))
					  { 
						
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"black");	
					  	  openList.add(new_current);
					  
					   }
					 else
					 {
						 if(new_current!=null)
						 {
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
					 
						 }
					 }
					
				 }
				
			}
			// Right
			 new_current=null;
			if(currentXPos+lookAhead<worldWidth)
			{
				  if(!this.isInList(openList, closedList, currentXPos+lookAhead, currentYPos,ImageHelper.DIRECTION_RIGHT))
			       {
					  new_current=this.fillNode(currentXPos+lookAhead, currentYPos, current, target_x, target_y, ImageHelper.DIRECTION_RIGHT,width,height,listXIndex,listYIndex);
					  	if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_RIGHT, Color.gray.getRGB()))
					  	{
					  
					  		 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"black");	
					    	  openList.add(new_current);
					    }
					  	 else
						 {
							 if(new_current!=null)
							 {
							 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
						 
							 }
						 }
			       }
				 						
			}
			
			// Forward
			 new_current=null;
			if(currentYPos+lookAhead<worldHeight)
			{
				  if(!this.isInList(openList, closedList, currentXPos, currentYPos+lookAhead,ImageHelper.DIRECTION_FORWARD))
			       {
					  new_current=this.fillNode(currentXPos, currentYPos+lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_FORWARD,width,height,listXIndex,listYIndex);
					  	if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_FORWARD, Color.gray.getRGB()))
					  	{
					
					  		 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"black");
					    	  openList.add(new_current);
					    }
					  	 else
						 {
							 if(new_current!=null)
							 {
							 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
						 
							 }
						 }
			       }
				 
						
			}
			// Backwards
			 new_current=null;
			if(currentYPos-lookAhead>=0)
			{
				  if(!this.isInList(openList, closedList, currentXPos, currentYPos-lookAhead , ImageHelper.DIRECTION_BACKWARD))
			       {
					  new_current=this.fillNode(currentXPos, currentYPos-lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_BACKWARD,width,height,listXIndex,listYIndex);
					   	if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_BACKWARD, Color.gray.getRGB()))
					  	{
					  		
					  		 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"black");
					    	 openList.add(new_current);
					    }
					  	 else
						 {
							 if(new_current!=null)
							 {
							 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
						 
							 }
						 }
			       }
				 
						
			}			
		
			closedList.add(current);
		    int index=this.getMinimumDistance(openList);
		    if(index==-1)
		    {		    
		    	if(lookAhead-2<0)
		    	{
		    		System.out.println("LookAhead-2:"+ (lookAhead-2));
		    		System.out.println("Habe keinen Weg gefunden:" + id);
		    		return current;
		    	}
		    	//No path is found. Try too look less pixel ahead
		    	else
		    	{
		    	SVGSafe sg=new SVGSafe();
				//System.out.println("Rekursion!");
				sg.write("WayPath_"+id +","+counter+".svg", doc);
				//System.out.println("Write File");
				//System.out.println("Rek with:"+ (lookAhead-2) +" ID:"+id);
				int faktor=2;
				if(lookAhead-faktor==0)
				{
					faktor=1;
				}
		    	return this.withoutGrid(id, target_x, target_y, width, height, direction, lookAhead-faktor, parent, pixel);
		    	}
		     }		   
		    
		    
		    current=openList.get(index);
		    openList.remove(index);
			currentXPos=current.getX();
			currentYPos=current.getY();
			
			if(this.getDistance(target_x, target_y, currentXPos, currentYPos)<=10)
			{
			
				SVGSafe sg=new SVGSafe();
				//sg.write("WayPath"+counter+".svg", doc);
				  System.out.println("ID vollkommen:" + id);
			  return current;
			}
			
			
			
		}
		  System.out.println("ID vollkommen:" + id);
		  return current;
		
	}
	
	/**
	 * Checks if a node is already in a list
	 * @param open The list with the open Nodes
	 * @param closed The list with the closed NOdes
	 * @param x  X Position of the node
	 * @param y Y Position of the node
	 * @param direction The Direction of the node
	 * @return True or false
	 */
	private boolean isInList(ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> open,ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> closed, float x,float y,int direction)
	{
		for(int i=0;i<open.size();i++)
		{
			if(open.get(i).getX()==x&&open.get(i).getY()==y)
			{
				
				return true;
			}
		}
		for(int i=0;i<closed.size();i++)
		{
			if(closed.get(i).getX()==x&&closed.get(i).getY()==y)
			{
				
				return true;
			}
		}
		return false;
	}

	
		
		
	/**
	 * Searches for the node with lowest costs 
	 * @param list The open node list
	 * @return the node with the minimum costs
	 */
	private int getMinimumDistance(ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> list)
	{
		int min=Integer.MAX_VALUE;
		int result=-1;
		for(int i=0;i<list.size();i++)
		{
			if(min>list.get(i).getDistance())
			{
				min=(int) list.get(i).getDistance();
			   	result=i;
			   
			}
		}
		return result;
	}
		
		
	
	
	/**
	 * Creates the path
	 * @param id The ID of the agent
	 * @param target_id  The ID of the target
	 * @param direction  The direction
	 * @param lookAhead  Numbers of pixel
	 * @return target node of the path
	 * @throws Exception
	 */
	
	
	
	public synchronized agentgui.physical2Denvironment.imageProcessing.StepNode createPlanImage(String id,String target_id , int direction , float lookAhead) throws Exception
	{
		//System.out.println("Agent:"+id + " will create his own world!");
		/**
		synchronized (this) {
		if(!READ_WORLD&&!IS_IN_METHOD&FIRST_CALL)
		{
		FIRST_CALL=false;
		
		System.out.println(id + " erstell Welt");
		this.createManipulatedWorld();
		}
		else
		{
		System.out.println(id + " liest Welt");
		this.evn=ImageIO.read(new File("myWorld.jpg"));	
		}
			
		}
		
		while(!READ_WORLD)
		{
			System.out.println("Warte!"+id);
			Thread.sleep(50);
		}
	 */
		this.evn=this.createManipulatedWorldPerAgent();
		Document doc=helper.getSVGDoc();
		Element target=doc.getElementById(target_id);
		float target_x=Float.parseFloat(target.getAttribute("x"));
		float target_y=Float.parseFloat(target.getAttribute("y"));
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=Float.parseFloat(self.getAttribute("x"));
		float y=Float.parseFloat(self.getAttribute("y"));
		this.firstX=x;
		this.firstY=y;
		agentgui.physical2Denvironment.imageProcessing.StepNode root=new agentgui.physical2Denvironment.imageProcessing.StepNode();
		root.setX(x);
		root.setY(y);
		final int listXIndex= (int) (root.getX()/lookAhead);
		final int listYIndex=  (int) (root.getY()/lookAhead);
		final int targetXIndex= (int) (target_x/lookAhead);
		final int targetYIndex=(int) (target_y/lookAhead);
		root.setDistanceToOrginal(0.0);
		root.setDistance(this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex));
		root.setTotal_distance((this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex)));
		 root.setParent(null);
		//int color=this.getPixelsOnce(plan, x, y, width, height);
		agentgui.physical2Denvironment.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	
	
	public synchronized agentgui.physical2Denvironment.imageProcessing.StepNode createPlanImage(String id,Position target , int direction , float lookAhead) throws Exception
	{
		System.out.println("First Call:"+FIRST_CALL);
		
		synchronized (this) {
		if(!READ_WORLD&&!IS_IN_METHOD&FIRST_CALL)
		{
		FIRST_CALL=false;
		
		System.out.println(id + " erstell Welt");
		//this.createManipulatedWorldWithAgents(otherAgent);
		}
		else
		{
		System.out.println(id + " liest Welt");
		this.evn=ImageIO.read(new File("myWorld.jpg"));	
		}
			
		}
		
		while(!READ_WORLD)
		{
			System.out.println("Warte!"+id);
			Thread.sleep(50);
		}
		
		Document doc=helper.getSVGDoc();

		float target_x=target.getXPos();
		float target_y=target.getYPos();
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=Float.parseFloat(self.getAttribute("x"));
		float y=Float.parseFloat(self.getAttribute("y"));
		this.firstX=x;
		this.firstY=y;
		agentgui.physical2Denvironment.imageProcessing.StepNode root=new agentgui.physical2Denvironment.imageProcessing.StepNode();
		root.setX(x);
		root.setY(y);
		final int listXIndex= (int) (root.getX()/lookAhead);
		final int listYIndex=  (int) (root.getY()/lookAhead);
		final int targetXIndex= (int) (target_x/lookAhead);
		final int targetYIndex=(int) (target_y/lookAhead);
		root.setDistanceToOrginal(0.0);
		root.setDistance(this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex));
		root.setTotal_distance((this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex)));
		 root.setParent(null);
		//int color=this.getPixelsOnce(plan, x, y, width, height);
		agentgui.physical2Denvironment.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	

	public synchronized agentgui.physical2Denvironment.imageProcessing.StepNode createPlanImage(ArrayList<String> otherAgent,Position own,String id ,String target , float lookAhead) throws Exception
	{
		System.out.println("First Call:"+FIRST_CALL);
		
		synchronized (this) {
		
		this.evn=this.createManipulatedWorldWithAgents(otherAgent);
		}
		
		Document doc=helper.getSVGDoc();
		Element targetElement=doc.getElementById(target);
		float target_x=Float.parseFloat(targetElement.getAttribute("x"));
		float target_y=Float.parseFloat(targetElement.getAttribute("y"));
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=own.getXPos();
		float y=own.getYPos();
		this.firstX=x;
		this.firstY=y;
		agentgui.physical2Denvironment.imageProcessing.StepNode root=new agentgui.physical2Denvironment.imageProcessing.StepNode();
		root.setX(x);
		root.setY(y);
		final int listXIndex= (int) (root.getX()/lookAhead);
		final int listYIndex=  (int) (root.getY()/lookAhead);
		final int targetXIndex= (int) (target_x/lookAhead);
		final int targetYIndex=(int) (target_y/lookAhead);
		root.setDistanceToOrginal(0.0);
		root.setDistance(this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex));
		root.setTotal_distance((this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex)));
		 root.setParent(null);
		//int color=this.getPixelsOnce(plan, x, y, width, height);
		agentgui.physical2Denvironment.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height,ImageHelper.DIRECTION_BACKWARD, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	
	
	
	
	
	
	
	
	public synchronized agentgui.physical2Denvironment.imageProcessing.StepNode createPlanImage(String otherAgent,Position own,String id ,Position target , int direction , float lookAhead) throws Exception
	{
		System.out.println("First Call:"+FIRST_CALL);
		
		synchronized (this) {
		if(!READ_WORLD&&!IS_IN_METHOD&FIRST_CALL)
		{
		FIRST_CALL=false;
		
		System.out.println(id + " erstell Welt");
		this.evn=this.createManipulatedWorldWithAgents(otherAgent);
		}
		else
		{
		System.out.println(id + " liest Welt");
		this.evn=ImageIO.read(new File("myWorld.jpg"));	
		}
			
		}
		
		while(!READ_WORLD)
		{
			System.out.println("Warte!"+id);
			Thread.sleep(50);
		}
		Document doc=helper.getSVGDoc();

		float target_x=target.getXPos();
		float target_y=target.getYPos();
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=own.getXPos();
		float y=own.getYPos();
		this.firstX=x;
		this.firstY=y;
		agentgui.physical2Denvironment.imageProcessing.StepNode root=new agentgui.physical2Denvironment.imageProcessing.StepNode();
		root.setX(x);
		root.setY(y);
		final int listXIndex= (int) (root.getX()/lookAhead);
		final int listYIndex=  (int) (root.getY()/lookAhead);
		final int targetXIndex= (int) (target_x/lookAhead);
		final int targetYIndex=(int) (target_y/lookAhead);
		root.setDistanceToOrginal(0.0);
		root.setDistance(this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex));
		root.setTotal_distance((this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex)));
		 root.setParent(null);
		//int color=this.getPixelsOnce(plan, x, y, width, height);
		agentgui.physical2Denvironment.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	
	
	
	/**
	 * Return the a node with a specific node
	 * @param node The list of nodes
	 * @param nodeName The name which is searched
	 * @return  A node with a specific name
	 */
	private Node findNode(NodeList node, String nodeName)
	{
		Node result;
		for(int i=0;i<node.getLength();i++)
		{
		    result=this.findNode(node.item(i),nodeName);
		    if(result!=null)
		    {
		    	return result;
		    }
		}	
		return null;
	}
	
	
	/**
	 * Finds a specific node and returns the nide
	 * @param node The node which should be searched
	 * @param nodeName The name of the node
	 * @return The node
	 */
	private Node findNode(Node node ,String nodeName)
	{
		if(node.getParentNode().getLocalName().equals(nodeName))
		{
			return node.getParentNode();
		}
		if(node.getChildNodes().getLength()>0)
		{
			 return findNode(node.getChildNodes(),nodeName);
		}
		else
		{
			 return null;
	     }
	}
	
	/**
	 * Prints a nodelist
	 * @param node The nodelist which should be printed
	 */
	public void print(NodeList node)
	{
		for(int i=0;i<node.getLength();i++)
		{
			this.print(node.item(i));
		}
	}
	
	/**
	 *  Print a node and all children of a node
	 * @param node The node which should be printed
	 */
	public void print(Node node)
	{	
		if(node.getChildNodes().getLength()>0)
		{
			
			this.print(node.getChildNodes());
		}
		
	}
	
	/**
	 * Create a new node
	 * 
	 * @param nodename Name of Node which should be created
	 * @param x Value of the x position
	 * @param y Value of the y position
	 * @param transform  The value of the transformation attribute
	 * @param width Value of width Attribute
	 * @param height Value of height Attribue
	 * @param id  The id which should be set
	 * @param doc The svn Document
	 * @return
	 */
	public Element createNewElement(String nodename,float x,float y, String transform, float width,  float height, String id, Document doc)
	{
		Element e = doc.createElement(nodename);
		e.setAttribute("x", String.valueOf(x));
		e.setAttribute("y", String.valueOf(y));
		e.setAttribute("transform", transform);
		e.setAttribute("width", String.valueOf(width));
		e.setAttribute("style", "fill:gray;stroke:none");
		//System.out.println("Heigth:"+String.valueOf(height));
		e.setAttribute("height", String.valueOf(height));
		e.setAttribute("id", nodename);
	 
		return e;
		
	}	
	
	/**
	 * Collision detection
	 * @param world The image of the world
	 * @param startx Starting search point
	 * @param starty Starting search point
	 * @param width  width of the agent
	 * @param height height of the agent
	 * @param lookAhead How many pixel should be looked ahead
	 * @param direction Which direction should be looked?
	 * @param color Which color should be checked
	 * @return true or false depending on the collision
	 * @throws Exception
	 */
	public boolean tranformAndCheckImage(BufferedImage world, float startx, float starty, float width, float height,float lookAhead ,int direction, int color) throws Exception
	{	
		int yExit= 0;
	    int  xExit=0;
		switch(direction) 
		{
			case ImageHelper.DIRECTION_FORWARD: 
				if(starty+lookAhead+height>helper.getEnvironment().getRootPlayground().getSize().getHeight() ||startx+width>helper.getEnvironment().getRootPlayground().getSize().getWidth())	
				{
		
				return false;
				}
				else
				{
				  yExit=(int) Math.floor( starty+lookAhead+height);
			      starty=starty+lookAhead;
			      xExit=(int) Math.floor(startx+width);
				}
			 break;
			 
			case ImageHelper.DIRECTION_BACKWARD:
			   if(starty-lookAhead<0)   
			   {
				return false; 
			   }
			  yExit=(int) Math.floor( starty-lookAhead+height);
			  starty=starty-lookAhead;
			  xExit=(int) Math.floor(startx+width);
			  break;
		
			case ImageHelper.DIRECTION_LEFT: 
			   if(startx-lookAhead<0)
			   {
				return false;
			   }
			  xExit= (int) Math.floor(startx-lookAhead+width);
			  startx=startx-lookAhead;
			  yExit= (int) Math.floor(starty+height);
			  break;
			 
			case ImageHelper.DIRECTION_RIGHT:
			  if(startx+lookAhead+width>helper.getEnvironment().getRootPlayground().getSize().getWidth())
				 {					
					return false;
				 }
				xExit=(int) Math.floor(startx+lookAhead+width);
				startx=startx+lookAhead;
				yExit=(int) Math.floor(starty+height);
				break;

			case ImageHelper.DIRECTION_UP_LEFT:
		       if(startx-lookAhead<0||starty-lookAhead-height<0)
			   {
				return false;
			   }
			 xExit= (int) Math.floor(startx+width-lookAhead);
			 startx=startx-lookAhead;
			 yExit=(int) Math.floor(starty-lookAhead+height);
			 starty=(int) Math.floor(starty-lookAhead);
		     break;
				
			case ImageHelper.DIRECTION_UP_RIGHT:
			  if(startx+lookAhead+width>=helper.getEnvironment().getRootPlayground().getSize().getWidth()||starty-lookAhead-height<0)
			  {
				return false;
			  }
			 xExit=(int) Math.floor(startx+width+lookAhead);
			 startx=(int) Math.floor(startx+lookAhead);
			 yExit=(int) Math.floor(starty-lookAhead+height);
			 starty=(int) Math.floor(starty-lookAhead);
			 break;
			
			case ImageHelper.DIRECTION_DOWN_LEFT:
			 if(starty+lookAhead+height>=helper.getEnvironment().getRootPlayground().getSize().getHeight()||startx-lookAhead<0)
			 {
				return false;
			 }
			 xExit=(int) Math.floor(startx-lookAhead+width);
			 startx=(int) Math.floor(startx-lookAhead);
			 yExit=(int) Math.floor(starty+height+lookAhead);
			 starty= (int) Math.floor(starty+lookAhead);
			break;
			
			case ImageHelper.DIRECTION_DOWN_RIGHT:
			 if(startx+lookAhead+height>helper.getEnvironment().getRootPlayground().getSize().getWidth()||starty+lookAhead+height>helper.getEnvironment().getRootPlayground().getSize().getHeight())
			  {
				return false;
			  }
			 xExit= (int) Math.floor(startx+lookAhead+width)-1;
			 startx= (int) Math.floor(startx+lookAhead);
			 yExit= (int) Math.floor(starty+lookAhead+height)-1;
			 starty= (int) Math.floor(starty+lookAhead);
			 break;
						
		}
		 for(int y=(int) Math.floor(starty);y<=yExit;y++)
		 {
			 for(int x=(int)Math.floor(startx);x<=xExit;x++)
			 {
				 try
				 {
					 if(world.getRGB(x, y)==color)
					 {	
						 return false;
					 }
				 }
				 catch(Exception e)
						 {
					 e.printStackTrace();
					 System.out.println("X:"+x +"," + y +" ");
					 
						 }
			
			 	
			 }
		 }
		 return true;
	           
	}
		
	
	/**
	 * It can draw the actual position of an agent. Can be used for checking all the places which are available for the agent
	 * @param id  The Id attribut
	 * @param x X Pos
	 * @param y Y Pos
	 * @param width Width of the agent
	 * @param height Height of the agent
	 * @param direction Direction
	 * @param lookAhead How Many pixel ahead
	 * @param target_id targetID
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Element> draw_way(String id,float x,float y,float width,float height ,int direction,float lookAhead,String target_id) throws Exception
	{
		ArrayList<Element> elements=new ArrayList<Element>();
		Document doc=helper.getSVGDoc();
		NodeList list=doc.getElementsByTagName("g");
		list=list.item(0).getChildNodes();
		Element newElement=null;
		Node node=this.findNode(doc.getElementsByTagName("g"),"g");
		if(node!=null)
		{			
			this.deleteNodes(doc, node);
		}
		switch(direction) 
		{
			case ImageHelper.DIRECTION_FORWARD: 
				if(y+lookAhead+height>helper.getEnvironment().getRootPlayground().getSize().getHeight())
				{
				 newElement=this.createNewElement("rect", x, -1.0f, null, width, height, id, doc);	
				}
				else
				{
				 newElement=this.createNewElement("rect", x, y+lookAhead, null, width, height, id, doc);
				}
			 node.appendChild(newElement);
			 elements.add(newElement);
	         break;
	         
			case ImageHelper.DIRECTION_BACKWARD:
			   newElement=this.createNewElement("rect", x, y-lookAhead, null, width, height, id, doc);
			   if(y-lookAhead<0)
			   {
				 newElement=this.createNewElement("rect", x, -1.0f, null, width, height, id, doc);	 
			   }
			 node.appendChild(newElement);
			 elements.add(newElement);
		     break;
	
			case ImageHelper.DIRECTION_LEFT: 
			  newElement=this.createNewElement("rect", x-lookAhead, y, null, width, height, id, doc);
			  if(x-lookAhead<0)
			   {
				 newElement=this.createNewElement("rect", -1.0f, y, null, width, height, id, doc);	 
			   }
				 node.appendChild(newElement);
				 elements.add(newElement);
	             break;
		
				case ImageHelper.DIRECTION_RIGHT:
				  newElement=this.createNewElement("rect", x+lookAhead, y, null, width, height, id, doc);
		          if(x+lookAhead+width>helper.getEnvironment().getRootPlayground().getSize().getWidth())
			       {
				    newElement=this.createNewElement("rect", -1.0f, y, null, width, height, id, doc);  
				   }
				   node.appendChild(newElement);
	    		   elements.add(newElement);
				   break;
						 
		
		}
		
	    
	    for(int i=0;i<list.getLength();i++)
	    {
	    	Node svgNode=list.item(i);
	    
	    	if(svgNode.getParentNode().getNodeName().equals("svg"))
	    	{
	    		Node parent=svgNode.getParentNode();
	    		parent.removeChild(svgNode);
	    		
	    		break;
	    	}
	    	
	    }
	    
 		
 		this.plan=ImageIO.read(new File("plan.jpg"));
        this.endung++;
    	return elements;
		
	}
	
	 
	  /**
	   * Paints an area. Can be used for a graphical representation of the search
	 * @param id ID
	 * @param x1 X Pos
	 * @param width Width
	 * @param y1 Y Pos
	 * @param height Height
	 * @param doc SVNDocument
	 * @param colored Should be colored?
	 * @param color The color
	 */
	public void drawLineToSave(String id,String x1,String width, String y1,String height,Document doc,boolean colored,String color)
	  {    
		    Element e = doc.createElement("rect");
		    e.setAttribute("x", x1);
			e.setAttribute("y", y1);
			e.setAttribute("transform", "");
			e.setAttribute("width", String.valueOf(width));
			if(colored)
			{
			e.setAttribute("style", "fill:"+color+";stroke:none");
			}
			else
			{		
				e.setAttribute("fill","none");
				e.setAttribute("stroke", color);
			}
			e.setAttribute("id", "rect");
			e.setAttribute("height", height);
			Node node=doc.getElementsByTagName("rect").item(0).getParentNode();          
			node.appendChild(e);
	  } 
	  	
	
	/**
	 * Deletes every object of the SVG except the agent
	 * @return
	 */
	public synchronized BufferedImage createManipulatedWorld()
	{
	
		try
		{
			if(!READ_WORLD&&!IS_IN_METHOD)
			{
				System.out.println("Bin drin!");
				IS_IN_METHOD=true;
				Document doc=helper.getSVGDoc();
				EnvironmentWrapper envWrap = new EnvironmentWrapper(helper.getEnvironment());
				Vector<StaticObject> obstacles=envWrap.getObstacles();
				Vector<ActiveObject> agents=envWrap.getAgents();
				for(ActiveObject obj: agents)
				{
	    	 	        	Element element=doc.getElementById(obj.getId());
	    	 	           	Node tmp= element.getParentNode();
	    	 	           	tmp.removeChild(element);
	             }  
			NodeList list=doc.getElementsByTagName("rect");
			for(int i=0;i<list.getLength();i++)
			{
				Node node=list.item(i);
				if(node.getParentNode().getNodeName().equals("svg"))
				{
	    		Node parent=node.getParentNode();
	    		parent.removeChild(node);
	    		break;
				}	  	
			}
			
			for(StaticObject obj: obstacles)
			{		
				Element element=doc.getElementById(obj.getId());
				String oldVal=element.getAttributeNS(null, "style");
				String search="fill:";
				int index=oldVal.indexOf(search);
				int end;
				if(index!=-1)
				{
					end=oldVal.indexOf(";");
	    	    	if(end==-1)
	    	    	{
	    	    		oldVal="fill:gray";
	    	    	}
	    	    	else
	    	    	{
	    	    		oldVal=oldVal.replaceFirst(oldVal.substring(index+search.length(),end), "gray");
	    	    	}	        		    
	    	    	Attr style=element.getAttributeNode("style");
	    	    	style.setValue(oldVal);
	    	}
		 }
		//SVGImage img=new SVGImage( (SVGDocument)doc);
		//this.evn=(BufferedImage) img.createBufferedImage();
			synchronized (this) {
				if(!READ_WORLD)
				{	
			SVGSafe save=new SVGSafe();
			save.write("myWorld.svg", doc);
			save.writeJPG("myworld.svg");
				}
			
			
			}
		

	   }
			
		this.evn=ImageIO.read(new File("myWorld.jpg"));
		System.out.println("Setze Real_WORLD auf true!");
		READ_WORLD=true;
		return evn;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return null;
	}
	
 }
	
	
	public synchronized BufferedImage createManipulatedWorldPerAgent()
	{
	
		try
		{
			
				System.out.println("Bin drin!");
				Document doc=helper.getSVGDoc();
				EnvironmentWrapper envWrap = new EnvironmentWrapper(helper.getEnvironment());
				Vector<StaticObject> obstacles=envWrap.getObstacles();
				Vector<ActiveObject> agents=envWrap.getAgents();
				for(ActiveObject obj: agents)
				{
	    	 	        	Element element=doc.getElementById(obj.getId());
	    	 	           	Node tmp= element.getParentNode();
	    	 	           	tmp.removeChild(element);
	             }  
			NodeList list=doc.getElementsByTagName("rect");
			for(int i=0;i<list.getLength();i++)
			{
				Node node=list.item(i);
				if(node.getParentNode().getNodeName().equals("svg"))
				{
	    		Node parent=node.getParentNode();
	    		parent.removeChild(node);
	    		break;
				}	  	
			}
			
			for(StaticObject obj: obstacles)
			{		
				Element element=doc.getElementById(obj.getId());
				String oldVal=element.getAttributeNS(null, "style");
				String search="fill:";
				int index=oldVal.indexOf(search);
				int end;
				if(index!=-1)
				{
					end=oldVal.indexOf(";");
	    	    	if(end==-1)
	    	    	{
	    	    		oldVal="fill:gray";
	    	    	}
	    	    	else
	    	    	{
	    	    		oldVal=oldVal.replaceFirst(oldVal.substring(index+search.length(),end), "gray");
	    	    	}	        		    
	    	    	Attr style=element.getAttributeNode("style");
	    	    	style.setValue(oldVal);
	    	}
		 }
		//SVGImage img=new SVGImage( (SVGDocument)doc);
		//this.evn=(BufferedImage) img.createBufferedImage();
			
			SVGSafe save=new SVGSafe();
			int i=0;
			String name="myWorldAgent"+i;
			File f=new File(name+".svg");
			while(f.exists())
			{
				 i++;
				 name="myWorldAgent"+i;
				 f=new File(name+".svg");
			}
			save.write(name+".svg", doc);
			save.writeJPG(name+".svg");
			this.evn=ImageIO.read(new File(name+".jpg"));
			return evn;	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	

	public synchronized BufferedImage createManipulatedWorldWithAgents(String otherAgent)
	{
	
		try
		{
			if(!READ_WORLD&&!IS_IN_METHOD)
			{
				System.out.println("Bin drin!");
				IS_IN_METHOD=true;
				Document doc=helper.getSVGDoc();
				EnvironmentWrapper envWrap = new EnvironmentWrapper(helper.getEnvironment());
				Vector<StaticObject> obstacles=envWrap.getObstacles();
				Vector<ActiveObject> agents=envWrap.getAgents();
				for(ActiveObject obj: agents)
				{
							if(!obj.getId().equals(otherAgent))
							{
	    	 	        	Element element=doc.getElementById(obj.getId());
	    	 	           	Node tmp= element.getParentNode();
	    	 	           	tmp.removeChild(element);
							}
							else
							{
								Element element=doc.getElementById(obj.getId());
								String oldVal=element.getAttributeNS(null, "style");
								String search="fill:";
								int index=oldVal.indexOf(search);
								int end;
								if(index!=-1)
								{
									end=oldVal.indexOf(";");
					    	    	if(end==-1)
					    	    	{
					    	    		oldVal="fill:gray";
					    	    	}
					    	    	else
					    	    	{
					    	    		oldVal=oldVal.replaceFirst(oldVal.substring(index+search.length(),end), "gray");
					    	    	}	        		    
					    	    	Attr style=element.getAttributeNode("style");
					    	    	style.setValue(oldVal);
							}
							}
	             }  
			NodeList list=doc.getElementsByTagName("rect");
			for(int i=0;i<list.getLength();i++)
			{
				Node node=list.item(i);
				if(node.getParentNode().getNodeName().equals("svg"))
				{
	    		Node parent=node.getParentNode();
	    		parent.removeChild(node);
	    		break;
				}	  	
			}
			
			for(StaticObject obj: obstacles)
			{		
				Element element=doc.getElementById(obj.getId());
				String oldVal=element.getAttributeNS(null, "style");
				String search="fill:";
				int index=oldVal.indexOf(search);
				int end;
				if(index!=-1)
				{
					end=oldVal.indexOf(";");
	    	    	if(end==-1)
	    	    	{
	    	    		oldVal="fill:gray";
	    	    	}
	    	    	else
	    	    	{
	    	    		oldVal=oldVal.replaceFirst(oldVal.substring(index+search.length(),end), "gray");
	    	    	}	        		    
	    	    	Attr style=element.getAttributeNode("style");
	    	    	style.setValue(oldVal);
	    	}
		 }
		//SVGImage img=new SVGImage( (SVGDocument)doc);
		//this.evn=(BufferedImage) img.createBufferedImage();
			synchronized (this) {
				if(!READ_WORLD)
				{	
			SVGSafe save=new SVGSafe();
			int i=0;
			String name="myWorldAgent"+i;
			File f=new File(name+".svg");
			while(f.exists())
			{
				 i++;
				 name="myWorldAgent"+i;
				 f=new File(name+".svg");
			}
			save.write(name+".svg", doc);
			save.writeJPG(name+".svg");
				}
			
			
			}
		

	   }
			
		this.evn=ImageIO.read(new File("myWorld.jpg"));
		System.out.println("Setze Real_WORLD auf true!");
		READ_WORLD=true;
		return evn;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return null;
	}
	
 }
	private boolean is_insight(ArrayList<String> other, ActiveObject obj)
	{
		for(int i=0;i<other.size();i++)
		{
			if(!obj.getId().equals(other.get(i)))
			{
				return true;
			}
		}
			return false;
	
	}
					
	public synchronized BufferedImage createManipulatedWorldWithAgents(ArrayList<String> otherAgent)
	{
	
		try
		{
			
			
				IS_IN_METHOD=true;
				Document doc=helper.getSVGDoc();
				EnvironmentWrapper envWrap = new EnvironmentWrapper(helper.getEnvironment());
				Vector<StaticObject> obstacles=envWrap.getObstacles();
				Vector<ActiveObject> agents=envWrap.getAgents();
				for(ActiveObject obj: agents)
				{
							if(!is_insight(otherAgent,obj))
							{
						
	    	 	        	Element element=doc.getElementById(obj.getId());
	    	 	           	Node tmp= element.getParentNode();
	    	 	           	tmp.removeChild(element);
							}
							else
							{
					
								Element element=doc.getElementById(obj.getId());
								String oldVal=element.getAttributeNS(null, "style");
								String search="fill:";
								int index=oldVal.indexOf(search);
								int end;
								if(index!=-1)
								{
									end=oldVal.indexOf(";");
					    	    	if(end==-1)
					    	    	{
					    	    		oldVal="fill:gray";
					    	    	}
					    	    	else
					    	    	{
					    	    		oldVal=oldVal.replaceFirst(oldVal.substring(index+search.length(),end), "gray");
					    	    	}	        		    
					    	    	Attr style=element.getAttributeNode("style");
					    	    	style.setValue(oldVal);
							}
							}
	             }  
			NodeList list=doc.getElementsByTagName("rect");
			for(int i=0;i<list.getLength();i++)
			{
				Node node=list.item(i);
				if(node.getParentNode().getNodeName().equals("svg"))
				{
	    		Node parent=node.getParentNode();
	    		parent.removeChild(node);
	    		break;
				}	  	
			}
			
			for(StaticObject obj: obstacles)
			{		
				Element element=doc.getElementById(obj.getId());
				String oldVal=element.getAttributeNS(null, "style");
				String search="fill:";
				int index=oldVal.indexOf(search);
				int end;
				if(index!=-1)
				{
					end=oldVal.indexOf(";");
	    	    	if(end==-1)
	    	    	{
	    	    		oldVal="fill:gray";
	    	    	}
	    	    	else
	    	    	{
	    	    		oldVal=oldVal.replaceFirst(oldVal.substring(index+search.length(),end), "gray");
	    	    	}	        		    
	    	    	Attr style=element.getAttributeNode("style");
	    	    	style.setValue(oldVal);
	    	}
		 }
		//SVGImage img=new SVGImage( (SVGDocument)doc);
		//this.evn=(BufferedImage) img.createBufferedImage();
			int i=0;
			String name="myWorldAgent"+i;
			synchronized (this) {
				
			SVGSafe save=new SVGSafe();
			
			
			File f=new File(name+".svg");
			while(f.exists())
			{
				 i++;
				 name="myWorldAgent"+i;
				 f=new File(name+".svg");
			}
			
			save.write(name+".svg", doc);
			save.writeJPG(name+".svg");
			this.evn=ImageIO.read(new File(name+".jpg"));	
			
			}
		
		return evn;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return null;
	}
	
 }	
	public PositionUpdate calculateNextCoord(int lastIndex,Position selfPos, Position destPos, final double msInSeconds,float speed, Stack<Position> position)
	  {
	    Position newPos=new Position(); // Save the Position which is passed to the simulation Agent here
	    Position lastPosition=null;
	    Answer answer=new Answer();
		boolean reached=false;
		float total=0.0f;
		double total_time=0.0d;
		ArrayList<Position> partSolution=new ArrayList<Position>();
		float xPosDiff=0.0f; // Difference
		float yPosDiff=0.0f; // Difference
		double total_seconds=0.0d;
		 boolean multiStep=false; // Needed for calculation
			while(total_seconds<=msInSeconds)
			{
			
				xPosDiff = destPos.getXPos() - selfPos.getXPos();
				yPosDiff = destPos.getYPos() - selfPos.getYPos();
				double dist = (Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff)/10.0d); 
				double seconds = dist / speed; // seconds to get to the point. Important to calculate dist in meter because speed is also in meter!
				total_seconds=total_seconds+seconds;
				//System.out.println("Total Seconds:"+total_seconds);
				//System.out.println("Seconds:"+seconds);
				// It's possible to do more than one moment at one time so we add the seconds
				if(total_seconds<=msInSeconds) // it's smaller so we can contioue walking
				{
				  reached=true;
				  total+=dist;
				  total_time+=seconds;
				  multiStep=true;
				  lastIndex++;	// Increase the Index 
				  	if(lastIndex<position.size()) // Are we add the end?
				   	{   // We're not and adjust the positions
				   		selfPos.setXPos(destPos.getXPos());
				   		selfPos.setYPos(destPos.getYPos());
				   		destPos=position.get(lastIndex);
				   		newPos.setXPos(selfPos.getXPos());
				   		newPos.setYPos(selfPos.getYPos());
				   		Position add=new Position();
				   		add.setXPos(newPos.getXPos());
				   		add.setYPos(newPos.getYPos());
				 	    partSolution.add(add);			
				 	    System.out.println(" wird hinzugefügt" + newPos.getXPos() + ",y:" + newPos.getYPos());
				 	    // System.out.println("X Pos:"+newPos.getXPos() + ",y:" + newPos.getYPos());:"+ Pos:"+newPos.getXPos() + ",y:" + newPos.getYPos());
				   }
				   else
				   {
					  // we at the end
					   selfPos=position.get(position.size()-1);
					   newPos.setXPos(selfPos.getXPos());
					   newPos.setYPos(selfPos.getYPos());
					   partSolution.add(newPos);
					   total_seconds=msInSeconds;
				
					   lastIndex++;
					   Position add=new Position();
					   add.setXPos(newPos.getXPos());
				   	   add.setYPos(newPos.getYPos());
				 	   partSolution.add(add);
				 	   System.out.println(" wird hinzugefügt:"+newPos.getXPos() + ",y:" + newPos.getYPos());
				 	 
					   PositionUpdate posUpdate=new PositionUpdate();
					   posUpdate.setNewPosition(newPos);
					   answer.setSpeed(new Long((long)speed));
					   answer.setWayToDestination(partSolution);			
					   posUpdate.setCustomizedParameter(answer);							
					   //System.out.println("Ende!!");
					   return posUpdate;
				   }
				}
				else // The distance is too huge to do in one step
				{
					//System.out.println("zu groß!");
					boolean flag=false;
					reached=false; // We haven't reached the full distance
					double max=((speed)*(msInSeconds)); // Calculate the maximum distance we can walk. Result is in meter and seconds
					//System.out.println("Speed:"+speed);
					//System.out.println("Ms in Seconds:"+msInSeconds);
					if(multiStep) // We need to take the difference
					{
						//System.out.println("Multi Step");
						double dif=Math.abs(msInSeconds-total_seconds+seconds);
						
						total_time+=dif;
						if(msInSeconds-total_seconds+seconds<0)
						{
							flag=true;
						}
						max=((speed)*(dif)); // Calculate the maximum distance we can walk is also in seconds
					}
					else
					{
					 //  System.out.println("Kein MultiStep");
					   total_time+=msInSeconds;	
					   total_seconds=msInSeconds+1;
					}
					if(!flag)
					{
					
					total+=max;
					
					max*=10; // calculate into pixel
				
					
					multiStep=false;
					double correctXDif=0.0d;
					double correctYDif=0.0d;
					if(xPosDiff>0) // We move to the right
					{
						correctXDif=max;
					}
					if(xPosDiff<0) // We move the left
					{
						correctXDif=max*-1;
					}
					if(yPosDiff>0) // We move forward
					{
						correctYDif=max;
					}
					if(yPosDiff<0) // We move backs
					{
						correctYDif=max*-1;
					}
					//System.out.println("CorrectXDif:"+correctXDif);
				//	System.out.println("CorrectYDif:"+ correctYDif);
					double newXValue=selfPos.getXPos()+correctXDif;
					//System.out.println("NewXValue:"+newXValue);
	
					double newYValue=selfPos.getYPos()+correctYDif;
					//System.out.println("NewYValue:"+newYValue);
					newPos.setXPos( (float) newXValue);
					newPos.setYPos( (float) newYValue);
					lastPosition=new Position();
					lastPosition.setXPos(destPos.getXPos());
					lastPosition.setYPos(destPos.getYPos());
					
					selfPos.setXPos(newPos.getXPos());					   
				    selfPos.setYPos(newPos.getYPos());
				  	}
					else
					{
						lastPosition=new Position();
						lastPosition.setXPos(destPos.getXPos());
						lastPosition.setYPos(destPos.getYPos());
						reached=false;
					}				
				}				
			}
			PositionUpdate posUpdate=new PositionUpdate();
			posUpdate.setNewPosition(newPos);
			Position add=new Position();
			add.setXPos(newPos.getXPos());
			add.setYPos(newPos.getYPos());
			System.out.println("adde zur Liste:"+newPos.getXPos() + ",y:" + newPos.getYPos());
		 	partSolution.add(add);
			answer.setSpeed(new Long((long)speed));
			//System.out.println("Size of Partsolitin in getMiddle:"+partSolution.size());
		    answer.setWayToDestination(partSolution);	
		    //System.out.println("Set Last Index:"+ lastIndex);#
		    if(reached)
		    {
		    	lastIndex++;
		    	destPos=position.get(lastIndex);
		    
		    }
		    else
		    {
		    	destPos=lastPosition;
		    }
		//	System.out.println("Self Position after:"+selfPos.getXPos()+","+selfPos.getYPos());
			//System.out.println("Destination after:" +destPos.getXPos()+","+destPos.getYPos());
		//	System.out.println("Index:"+lastIndex);
		    answer.setIndex(lastIndex);
			answer.setNextPosition(destPos);
			posUpdate.setCustomizedParameter(answer);
			posUpdate.setNewPosition(selfPos);
			return posUpdate; 
		  }
	
	public Stack<Position> orderAndMiddleCoordinates(StepNode way,float agentWidth,float agentHeight,float factor)
	{
		Stack<Position> pos=new Stack<Position>();
		while(way.getParent()!=null) 
		{
			 	Position p=new Position();
				float x=(way.getX()+agentWidth/2); // middle it
				float y=(way.getY()+agentHeight/2); // middle it
				p.setXPos(x/factor);
				p.setYPos(y/factor);
				pos.add(0,p);	
				way=way.getParent();
		}
		return pos;
	}
	
	public HashMap<String, ArrayList<String>> getDynamicCollision(HashMap<String,PositionUpdate> update)
	{
		try
		{
			 HashMap<String, ArrayList<String>> nameList = new HashMap<String, ArrayList<String>>();
			// Let's find the name of the moving agents
			EnvironmentWrapper envWrapper=new EnvironmentWrapper(this.helper.getEnvironment());
			Vector<ActiveObject> agents=envWrapper.getAgents();
			for(int i=0;i<agents.size();i++)
			{
				ArrayList<String> otherAgents=new ArrayList<String>();
				Physical2DObject agent=agents.get(i);
				//
				
				PositionUpdate ownAgentPosition= update.get(agent.getId());
				if(ownAgentPosition != null) // Consider that agents have already reached their target therefore don't send any answer
				{
				
				ArrayList<Position>  ownAgentPositions=((Answer) ownAgentPosition.getCustomizedParameter()).getWayToDestination();
				for(int counter=i+1;counter<agents.size();counter++)
				{
					//System.out.println("Try to compare with:"+ agents.get(counter).getId());
					PositionUpdate otherAgentPosition= update.get(agents.get(counter).getId());
				    if(otherAgentPosition != null)
				    {
					ArrayList<Position>  otherPositions=((Answer) otherAgentPosition.getCustomizedParameter()).getWayToDestination();
					
					
					int max=Math.max(ownAgentPositions.size(), otherPositions.size());
					int min=Math.min(ownAgentPositions.size(), otherPositions.size());
					
					for(int iter=0;iter<max;iter++)
					{
						if(min!=0)
						{
						Physical2DObject obj=new Physical2DObject();
						Position pos=new Position();
						if(iter<ownAgentPositions.size())
						{
						obj.setPosition(ownAgentPositions.get(iter));
						}
						else
						{
							obj.setPosition(ownAgentPositions.get(min-1));	
						}
						obj.setSize(agent.getSize());
						Physical2DObject other=agents.get(counter);
						if(iter < otherPositions.size())
						{
						other.setPosition(otherPositions.get(iter));
						}
						else
						{
							other.setPosition(otherPositions.get(min-1));	
						}
						//System.out.println("--------------------");
						//System.out.println("Iter:"+iter);
						//System.out.println("Vergleiche:" + obj.getPosition().getXPos() + "," + obj.getPosition().getYPos());
						//System.out.println("Mit" + other.getPosition().getXPos() + "," + other.getPosition().getYPos());
						boolean result=this.checkTimeBetween(obj, other);
						//System.out.println("--------------------");
						if(result)
						{
							otherAgents.add(agents.get(counter).getId());
							
							break;
						}						
					}
						else
						{
							break;
						}
						
					}
				}
				}	
				}
				nameList.put(agent.getId(), otherAgents);
			
			}
			return nameList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
	}
     
	private boolean checkTimeBetween(Physical2DObject agent,Physical2DObject cmpr)
	{
		return agent.objectIntersects(cmpr)|| cmpr.objectIntersects(agent) || agent.objectContains(cmpr) || cmpr.objectContains(agent);
	}
	
	
}
	



