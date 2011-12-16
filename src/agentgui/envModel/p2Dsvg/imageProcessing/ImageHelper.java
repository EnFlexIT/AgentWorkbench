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
package agentgui.envModel.p2Dsvg.imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.Position;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;
import agentgui.envModel.p2Dsvg.ontology.StaticObject;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.utils.EnvironmentWrapper;


/**
 * The class provides methods for dynamic path finding. It also can detect collissions.
 *  * @author Tim Lewen - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class ImageHelper { 
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
	private boolean is_setup=false;
	int divideHeight;
	int divideWidth;
	double lastDistance=-20;
	private BufferedImage evn=null;
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
	boolean DEBUG=false;
	
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
	private agentgui.envModel.p2Dsvg.imageProcessing.StepNode fillNode(float x, float y,agentgui.envModel.p2Dsvg.imageProcessing.StepNode parent,float target_x,float target_y,int ownDirection,float width,float height,int startXIndex,int startYIndex)
	{
								
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode result=new agentgui.envModel.p2Dsvg.imageProcessing.StepNode();	
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
	private synchronized agentgui.envModel.p2Dsvg.imageProcessing.StepNode withoutGrid(String id,float target_x,float target_y,float width,float height, int direction , float lookAhead, agentgui.envModel.p2Dsvg.imageProcessing.StepNode parent,int pixel) throws Exception
	{
			
		System.out.println("Target in WITHOUTGRID:"+target_x+","+ target_y);
		
		ArrayList<agentgui.envModel.p2Dsvg.imageProcessing.StepNode> openList=new ArrayList<agentgui.envModel.p2Dsvg.imageProcessing.StepNode>();
		ArrayList<agentgui.envModel.p2Dsvg.imageProcessing.StepNode> closedList=new ArrayList<agentgui.envModel.p2Dsvg.imageProcessing.StepNode>();
		float worldWidth=helper.getEnvironment().getRootPlayground().getSize().getWidth();
		float worldHeight=helper.getEnvironment().getRootPlayground().getSize().getHeight();
	    counter++;
		Document doc=helper.getSVGDoc();
		// Calculate pos
		final float xStart= parent.getX();
		final float yStart= parent.getY();
		System.out.println("X Start:"+xStart);
		System.out.println("Y Start:"+yStart);
		float currentXPos=xStart;
		float currentYPos=yStart; 
		final int listXIndex= (int) (parent.getX()/lookAhead);
		final int listYIndex=  (int) (parent.getY()/lookAhead);
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode current=parent;
		closedList.add(current);
		// While you're not even close
		while(this.getDistance(target_x, target_y, currentXPos, currentYPos)>10) 
		{				
			
			// Up left
			agentgui.envModel.p2Dsvg.imageProcessing.StepNode new_current=null;
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
							 if(DEBUG)
							 {
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
							 }
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
							 if(DEBUG)
							 {
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
							 }
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
							 if(DEBUG)
							 {
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
							 }
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
							 if(DEBUG)
							 {
							this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
							 }
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
							 if(DEBUG)
							 {
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
							 }
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
								 if(DEBUG)
								 {
							 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
								 }
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
								 if(DEBUG)
								 {
								this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
								 }
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
								 if(DEBUG)
								 {
							 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
								 }
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
		    		
		    		System.out.println("Habe keinen Weg gefunden:" + id);
		    		if(DEBUG)
			    	{
			    	SVGSafe sg=new SVGSafe();
			    	String name="WayPath"+ id+counter;
					File f=new File(name+".svg");
					while(f.exists())
					{
						 counter++;
						 name="WayPath"+id+counter;
						 f=new File(name+".svg");
					}
					
					//sg.write(name+".svg", doc);
					System.out.println("Datei geschrieben!");
			    	}
		    		//current.setX(-666);
		    		//current.setY(-666);
		    		System.out.println("nichts gefunden");
		    		return null;//current;
		    	}
		    	//No path is found. Try too look less pixel ahead
		    	else
		    	{
		    	if(DEBUG)
		    	{
		    	}
				//System.out.println("Write File");
				//System.out.println("Rek with:"+ (lookAhead-2) +" ID:"+id);
				int faktor=2;
				if(lookAhead-faktor==0)
				{
					current.setX(-666);
		    		current.setY(-666);
		    		System.out.println("nichts gefunden");
		    		return null;
				}
				SVGSafe sg=new SVGSafe();
	
		    	String name="WayPath"+id+counter;
				File f=new File(name+".svg");
				while(f.exists())
				{
					 counter++;
					 name="WayPath"+ id+counter;
					 f=new File(name+".svg");
				}
				
				if(DEBUG)
				{
				sg.write(name+".svg", doc);
				}
				System.out.println("Neue Datei geschreieben");
				System.out.println("Neuer Rekursiver Aufruf");
				System.out.println("LookAhead-faktor:"+ (lookAhead-faktor));
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
				
		    	String name="WayPath"+id+counter;
				File f=new File(name+".svg");
				while(f.exists())
				{
					 counter++;
					 name="WayPath"+id+counter;
					 f=new File(name+".svg");
				}
				if(DEBUG)
				{
				sg.write(name+".svg", doc);
				}
				System.out.println("WayPath"+ id +""+counter+".svg gespeichert");
				System.out.println("Schreib!");
				  //System.out.println("ID vollkommen:" + id);
				  if(DEBUG)
			    	{
			 
					//System.out.println("Rekursion!");
					//sg.write("WayPath_"+id +","+counter+".svg", doc);
			    	}
				
			  return current;
			}
			
			
			
		}
		
		  //System.out.println("ID vollkommen:" + id);
		SVGSafe sg=new SVGSafe();
		
    	String name="WayPath"+ id +""+counter;
		File f=new File(name+".svg");
		while(f.exists())
		{
			 counter++;
			 name="WayPath"+ id +""+counter;
			 f=new File(name+".svg");
		}
		if(DEBUG)
		{
		sg.write(name+".svg", doc);
		}
		System.out.println("Datei geschrieben");
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
	private boolean isInList(ArrayList<agentgui.envModel.p2Dsvg.imageProcessing.StepNode> open,ArrayList<agentgui.envModel.p2Dsvg.imageProcessing.StepNode> closed, float x,float y,int direction)
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
	private int getMinimumDistance(ArrayList<agentgui.envModel.p2Dsvg.imageProcessing.StepNode> list)
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
	
	
	
	public synchronized agentgui.envModel.p2Dsvg.imageProcessing.StepNode createPlanImage(String id,String target_id , int direction , float lookAhead) throws Exception
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
		System.out.println("ID:"+id);
		this.evn=this.createManipulatedWorldPerAgent();
		Document doc=helper.getSVGDoc();
		Element target=doc.getElementById(target_id);
		float target_x=Float.parseFloat(target.getAttribute("x"));
		float target_y=Float.parseFloat(target.getAttribute("y"));
		System.out.println("FirstCall Target:"+target_x+","+target_y);
		
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=Float.parseFloat(self.getAttribute("x"));
		float y=Float.parseFloat(self.getAttribute("y"));
		//System.out.println(id + " x:" +x +"," + "Y:"+y);
		EnvironmentWrapper wrapper=new EnvironmentWrapper(this.helper.getEnvironment());
		System.out.println("MyPos:"+wrapper.getObjectById(id).getPosition().getXPos()+ "," + wrapper.getObjectById(id).getPosition().getYPos());
		System.out.println("Self X:"+x);
		System.out.println("Self Y:"+y);
		this.firstX=x;
		this.firstY=y;
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode root=new agentgui.envModel.p2Dsvg.imageProcessing.StepNode();
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
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	
	
	

	public synchronized agentgui.envModel.p2Dsvg.imageProcessing.StepNode createPlanImage(String id,Position own,String target_id , int direction , float lookAhead) throws Exception
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
		System.out.println("FirstCall Target:"+target_x+","+target_y);
		
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=own.getXPos();
		float y=own.getYPos();
		//System.out.println(id + " x:" +x +"," + "Y:"+y);
		EnvironmentWrapper wrapper=new EnvironmentWrapper(this.helper.getEnvironment());
		
		
		this.firstX=x;
		this.firstY=y;
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode root=new agentgui.envModel.p2Dsvg.imageProcessing.StepNode();
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
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	
	
	
	
	
	public synchronized agentgui.envModel.p2Dsvg.imageProcessing.StepNode createPlanImage(String id,Position target , int direction , float lookAhead) throws Exception
	{
		
		this.evn=this.createManipulatedWorldPerAgent();
		
		Document doc=helper.getSVGDoc();

		float target_x=target.getXPos();
		float target_y=target.getYPos();
		System.out.println("Target X:"+target_x +","+target_y);
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=Float.parseFloat(self.getAttribute("x"));
		float y=Float.parseFloat(self.getAttribute("y"));
		this.firstX=x;
		this.firstY=y;
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode root=new agentgui.envModel.p2Dsvg.imageProcessing.StepNode();
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
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	

	public synchronized agentgui.envModel.p2Dsvg.imageProcessing.StepNode createPlanImage(ArrayList<CombinedNameAndPos> otherAgent, Position own,String id ,String target , float lookAhead, double discreteTime) throws Exception
	{
		synchronized (this) {		
		this.evn=this.createManipulatedWorldWithAgents(otherAgent,id,discreteTime);
		if(this.evn==null)
		{
			System.out.println("Sollte nicht vorkommen!");
			return null;
		}
		}
		
		Document doc=helper.getSVGDoc();
		Element targetElement=doc.getElementById(target);
		float target_x=Float.parseFloat(targetElement.getAttribute("x"));
		float target_y=Float.parseFloat(targetElement.getAttribute("y"));  
		System.out.println("Target x:"+target_x);
		System.out.println("Target y:"+target_y);
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=own.getXPos();
		float y=own.getYPos();
		//self.setAttribute("x", String.valueOf(x));
		//self.setAttribute("y", String.valueOf(y));
		this.firstX=x;
		this.firstY=y;
		System.out.println("Own X:"+x +"," + "Y:"+y);
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode root=new agentgui.envModel.p2Dsvg.imageProcessing.StepNode();
		root.setX(x); // correct it //
		root.setY(y); // Correct it //
		final int listXIndex= (int) (root.getX()/lookAhead);
		final int listYIndex=  (int) (root.getY()/lookAhead);
		final int targetXIndex= (int) (target_x/lookAhead);
		final int targetYIndex=(int) (target_y/lookAhead);
		root.setDistanceToOrginal(0.0);
		root.setDistance(this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex));
		root.setTotal_distance((this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex)));
		 root.setParent(null);
		//int color=this.getPixelsOnce(plan, x, y, width, height);
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height,ImageHelper.DIRECTION_BACKWARD, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	public synchronized agentgui.envModel.p2Dsvg.imageProcessing.StepNode createPlanImage(ArrayList<CombinedNameAndPos> otherAgent, Position own,String id ,Position target , float lookAhead, double discreteTime) throws Exception
	{
		synchronized (this) {		
		this.evn=this.createManipulatedWorldWithAgents(otherAgent,id,discreteTime);
		if(this.evn==null)
		{
			System.out.println("Sollte nicht vorkommen!");
			return null;
		}
		}
		
		Document doc=helper.getSVGDoc();
	
		float target_x=target.getXPos();
		float target_y=target.getYPos();  
		System.out.println("Target x:"+target_x);
		System.out.println("Target y:"+target_y);
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=own.getXPos();
		float y=own.getYPos();
		//self.setAttribute("x", String.valueOf(x));
		//self.setAttribute("y", String.valueOf(y));
		this.firstX=x;
		this.firstY=y;
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode root=new agentgui.envModel.p2Dsvg.imageProcessing.StepNode();
		root.setX(x); // correct it //
		root.setY(y); // Correct it //
		final int listXIndex= (int) (root.getX()/lookAhead);
		final int listYIndex=  (int) (root.getY()/lookAhead);
		final int targetXIndex= (int) (target_x/lookAhead);
		final int targetYIndex=(int) (target_y/lookAhead);
		root.setDistanceToOrginal(0.0);
		root.setDistance(this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex));
		root.setTotal_distance((this.getHCost(listXIndex, listYIndex, targetXIndex, targetYIndex)));
		 root.setParent(null);
		//int color=this.getPixelsOnce(plan, x, y, width, height);
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height,ImageHelper.DIRECTION_BACKWARD, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 return targetNode;
		
	}
	
	
	
	
	
	
	
		
	
	public synchronized agentgui.envModel.p2Dsvg.imageProcessing.StepNode createPlanImage(String otherAgent,Position own,String id ,Position target , int direction , float lookAhead) throws Exception
	{
				
		synchronized (this) {
		if(!READ_WORLD&&!IS_IN_METHOD&FIRST_CALL)
		{
		FIRST_CALL=false;
		
		
		//this.evn=this.createManipulatedWorldWithAgents(otherAgent);
		}
		else
		{

		this.evn=ImageIO.read(new File("myWorld.jpg"));	
		}
			
		}
		
		while(!READ_WORLD)
		{
			
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
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode root=new agentgui.envModel.p2Dsvg.imageProcessing.StepNode();
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
		agentgui.envModel.p2Dsvg.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
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
	 * @return an element whith the set attributes
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
					 //e.printStackTrace();
					// System.out.println("Direction:"+direction);
					// System.out.println("StartX:"+startx);
					// System.out.println("XExit:"+xExit);
					// System.out.println("YExit:"+yExit);
					// System.out.println("X:"+x +"," + y +" ");
					
					 
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
	 * @return Creates a list with all elements which are being checked.
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
	    
 		
 		ImageIO.read(new File("plan.jpg"));
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
	 * @return The manipuated world saved in a BufferedImage
	 */
	public synchronized BufferedImage createManipulatedWorld()
	{
	
		try
		{
			if(!READ_WORLD&&!IS_IN_METHOD)
			{
			
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
	//	System.out.println("Setze Real_WORLD auf true!");
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
				Document doc=helper.getSVGDoc();			
				EnvironmentWrapper envWrap = new EnvironmentWrapper(helper.getEnvironment());
				Vector<StaticObject> obstacles=envWrap.getObstacles();
				Vector<ActiveObject> agents=envWrap.getAgents();
				Vector<PassiveObject> passiveObjects=envWrap.getPayloads();
				for(PassiveObject passiveObject: passiveObjects)
				{
					Element element=doc.getElementById(passiveObject.getId());
	 	           	Node tmp= element.getParentNode();
	 	           	tmp.removeChild(element);				
					
				}				
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
			System.out.println("Image geschrieben");
			this.evn=ImageIO.read(new File(name+".jpg"));
			return evn;	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Return null!");
			return null;
		}
	}
	public void setupEnviroment(String ownID,ArrayList<CombinedNameAndPos> otherAgent)	
	{
		this.evn=this.createManipulatedWorldWithAgents(otherAgent, ownID, 0.0);
		this.is_setup=true;
	}
	
	public Position sidestepLeft(String ownID,ArrayList<CombinedNameAndPos> otherAgent)
	{
	if(!this.is_setup)
	{
		this.setupEnviroment(ownID, otherAgent);
	}
	Document doc=helper.getSVGDoc();
	Element self=doc.getElementById(ownID);
	final float width=Float.parseFloat(self.getAttribute("width"));
	final float height=Float.parseFloat(self.getAttribute("height"));
	float x=0.0f;
	float y=0.0f;
	for(int i=0;i<otherAgent.size();i++)
	{
		if(otherAgent.get(i).name.equals(ownID))
		{
		  x=otherAgent.get(i).getPos().getXPos();
		  y=otherAgent.get(i).getPos().getYPos();
		}
	}
	
	     //Float.parseFloat(self.getAttribute("x"));
			//Float.parseFloat(self.getAttribute("y"));
	System.out.println("Own ID:"+ownID);
	System.out.println("X:"+x);
	System.out.println("Y:"+y);
	//System.out.println("Vergleich:"+otherAgent.get(0).getPos().getXPos()+","+otherAgent.get(0).getPos().getYPos()+","+otherAgent.get(0).getName());
	int lookAhead=10; 
	
	boolean found=false;
	float currentXpos=x;
	float currentYPos=y;
	Position result = new Position();
	// Left
	while(!found&&lookAhead>0)
	{
	 try {
		found=this.tranformAndCheckImage(this.evn, currentXpos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_LEFT, Color.gray.getRGB());
		if(found)
		{
		result.setXPos(currentXpos-lookAhead);
		result.setYPos(currentYPos);
		return result;
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	 lookAhead--;
	}
		return null;	
}
	
	public Position sidestepRight(String ownID,ArrayList<CombinedNameAndPos> otherAgent)
	{
	if(!this.is_setup)
	{
		this.setupEnviroment(ownID, otherAgent);
	}
	Document doc=helper.getSVGDoc();
	Element self=doc.getElementById(ownID);
	final float width=Float.parseFloat(self.getAttribute("width"));
	final float height=Float.parseFloat(self.getAttribute("height"));
	float x=Float.parseFloat(self.getAttribute("x"));
	float y=Float.parseFloat(self.getAttribute("y"));
	for(int i=0;i<otherAgent.size();i++)
	{
		if(otherAgent.get(i).name.equals(ownID))
		{
		  x=otherAgent.get(i).getPos().getXPos();
		  y=otherAgent.get(i).getPos().getYPos();
		}
	}
	int lookAhead=10; 
	
	boolean found=false;
	float currentXpos=x;
	float currentYPos=y;
	Position result = new Position();
	// Left
	while(!found&&lookAhead>0)
	{
	 try {
		found=this.tranformAndCheckImage(this.evn, currentXpos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_RIGHT, Color.gray.getRGB());
		if(found)
		{
		result.setXPos(currentXpos+lookAhead);
		result.setYPos(currentYPos);
		return result;
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	 lookAhead--;
	}
		return null;	
}	
	
	public Position sidesteppBackwards(String ownID,ArrayList<CombinedNameAndPos> otherAgent)
	{
	if(!this.is_setup)
	{
		this.setupEnviroment(ownID, otherAgent);
	}
	Document doc=helper.getSVGDoc();
	Element self=doc.getElementById(ownID);
	final float width=Float.parseFloat(self.getAttribute("width"));
	final float height=Float.parseFloat(self.getAttribute("height"));
	float x=Float.parseFloat(self.getAttribute("x"));
	float y=Float.parseFloat(self.getAttribute("y"));
	for(int i=0;i<otherAgent.size();i++)
	{
	
		if(otherAgent.get(i).name.equals(ownID))
		{
		  x=otherAgent.get(i).getPos().getXPos();
		  y=otherAgent.get(i).getPos().getYPos();
		}
	}
	int lookAhead=10; 
	
	boolean found=false;
	float currentXpos=x;
	float currentYPos=y;
	Position result = new Position();
	// Left
	while(!found&&lookAhead>0)
	{
	 try {
		found=this.tranformAndCheckImage(this.evn, currentXpos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_BACKWARD, Color.gray.getRGB());
		if(found)
		{
		result.setXPos(currentXpos);
		result.setYPos(currentYPos-lookAhead);
		return result;
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	 lookAhead--;
	}
		return null;	
}	
	
	
	
	
	public Position sidesteppForward(String ownID,ArrayList<CombinedNameAndPos> otherAgent)
	{
	if(!this.is_setup)
	{
		this.setupEnviroment(ownID, otherAgent);
	}
	Document doc=helper.getSVGDoc();
	Element self=doc.getElementById(ownID);
	final float width=Float.parseFloat(self.getAttribute("width"));
	final float height=Float.parseFloat(self.getAttribute("height"));
	float x=Float.parseFloat(self.getAttribute("x"));
	float y=Float.parseFloat(self.getAttribute("y"));
	for(int i=0;i<otherAgent.size();i++)
	{
	
		if(otherAgent.get(i).name.equals(ownID))
		{
		  x=otherAgent.get(i).getPos().getXPos();
		  y=otherAgent.get(i).getPos().getYPos();
		}
	}
	int lookAhead=10; 
	
	boolean found=false;
	float currentXpos=x;
	float currentYPos=y;
	Position result = new Position();
	// Left
	while(!found&&lookAhead>0)
	{
	 try {
		found=this.tranformAndCheckImage(this.evn, currentXpos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_FORWARD, Color.gray.getRGB());
		if(found)
		{
		result.setXPos(currentXpos);
		result.setYPos(currentYPos+lookAhead);
		return result;
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	 lookAhead--;
	}
		return null;	
}	
	
	
	
	public synchronized BufferedImage createManipulatedWorldWithAgents(ArrayList<CombinedNameAndPos> otherAgent , String id,double discreteTime)
	{
	
		try
		{			
				
				IS_IN_METHOD=true;
				Document doc=helper.getSVGDoc();
				
				
				
				EnvironmentWrapper envWrap = new EnvironmentWrapper(helper.getEnvironment());
		
				Vector<StaticObject> obstacles=envWrap.getObstacles();
				Vector<PassiveObject> passiveObjects=envWrap.getPayloads();
				for(PassiveObject passiveObject: passiveObjects)
				{
					Element element=doc.getElementById(passiveObject.getId());
	 	           	Node tmp= element.getParentNode();
	 	           	tmp.removeChild(element);				
					
				}
				
				for(int i=0;i<otherAgent.size();i++)
				{
				
					Element element=doc.getElementById(otherAgent.get(i).name);
					
					element.removeAttribute("x");
					element.setAttribute("x", String.valueOf(otherAgent.get(i).getPos().getXPos()));
					element.removeAttribute("y");
					element.setAttribute("y", String.valueOf(otherAgent.get(i).getPos().getYPos()));
				
							//System.out.println("Untersuche:"+ obj.getId());
							if((otherAgent.get(i).getName().equals(id)))
							{
								
		    	 	           	//ode tmp= element.getParentNode();
		    	 	           	//tmp.removeChild(element);
	    	 	           	
							}
							else
							{    /*
								Element elem=doc.getElementById(obj.getId());
								float otherX=Float.parseFloat(elem.getAttribute("x"));
								float otherY=Float.parseFloat(elem.getAttribute("y"));
											
								Element element=doc.getElementById(obj.getId());
								double distance=this.getDistance(selfX, selfY, otherX , otherY);
								distance=distance/10.0;
								double seconds = distance / ownSpeed; 
							/**
								if(seconds>discreteTime)
								{
									//System.out.println("Abstand zu weit weswegen der andere gelöscht werden kann!");
									//element.getParentNode().removeChild(element);
								}
								*/
								//else
								//{
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
					 
							//}
						}		
							}
	             }  
			NodeList list=doc.getElementsByTagName("rect");
			if(list!=null)
			{		
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
			String name=id+"myWorldAgentWithOther"+i;
			synchronized (this) {
				
			SVGSafe save=new SVGSafe();
			
			
			File f=new File(name+".svg");
			while(f.exists())
			{
				 i++;
				 name=id + "myWorldAgentWithOther"+i;
				 f=new File(name+".svg");
			}
			
			save.write(name+".svg", doc);
			save.writeJPG(name+".svg");
			this.evn=ImageIO.read(new File(name+".jpg"));
		
			/**
			for(int counter=0;i<otherAgent.size();counter++)
			{
		//		System.out.println("Other:"+otherAgent.get(counter));
				Element element=doc.getElementById(otherAgent.get(counter));
				element.setAttribute("x", oldX[counter]);
				element.setAttribute("y", oldY[counter]);
				
			}
			*/		
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
					
					boolean flag=false;
					reached=false; // We haven't reached the full distance
					double max=((speed)*(msInSeconds)); // Calculate the maximum distance we can walk. Result is in meter and seconds
					if(multiStep) // We need to take the difference
					{
						//System.out.println("Multi Step");
						double dif=Math.abs(msInSeconds-total_seconds+seconds);
						
						if(msInSeconds-total_seconds+seconds<0)
						{
							flag=true;
						}
						max=((speed)*(dif)); // Calculate the maximum distance we can walk is also in seconds
					}
					else
					{
					 total_seconds=msInSeconds+1;
					}
					if(!flag)
					{
					
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
				
					double newXValue=selfPos.getXPos()+correctXDif;
					double newYValue=selfPos.getYPos()+correctYDif;
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
		 	partSolution.add(add);
		    answer.setWayToDestination(partSolution);	
		    if(reached)
		    {
		    	lastIndex++;
		    	destPos=position.get(lastIndex);
		    
		    }
		    else
		    {
		    	destPos=lastPosition;
		    }
		
		    answer.setIndex(lastIndex);
			answer.setNextPosition(destPos);
			posUpdate.setCustomizedParameter(answer);
			posUpdate.setNewPosition(selfPos);
			return posUpdate; 
		  }
	
	public Stack<Position> orderAndMiddleCoordinates(StepNode way,float agentWidth,float agentHeight,float factor)
	{
		Stack<Position> pos=new Stack<Position>();
		if(way.getParent()==null)
		{
			Position p=new Position();
		 	float x=(way.getX()+agentWidth/2); // middle it
			float y=(way.getY()+agentHeight/2); // middle it
			p.setXPos(x/factor);
			p.setYPos(y/factor);
			
			pos.add(0,p);		
		}
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
	
	public HashMap<String, ArrayList<CombinedNameAndPos>> getDynamicCollision(HashMap<String,PositionUpdate> update)
	{
		try
		{
			HashMap<String, ArrayList<CombinedNameAndPos>> nameList = new HashMap<String, ArrayList<CombinedNameAndPos>>();
			// Let's find the name of the moving agents
			EnvironmentWrapper envWrapper=new EnvironmentWrapper(this.helper.getEnvironment());
			Vector<ActiveObject> agents=envWrapper.getAgents();
			for(int i=0;i<agents.size();i++)
			{
				ArrayList<CombinedNameAndPos> otherAgents=new ArrayList<CombinedNameAndPos>();
				Physical2DObject agent=agents.get(i);
				//
				
				PositionUpdate ownAgentPosition= update.get(agent.getId());
				if(ownAgentPosition != null) // Consider that agents have already reached their target therefore don't send any answer
				{
				
				ArrayList<Position>  ownAgentPositions=((Answer) ownAgentPosition.getCustomizedParameter()).getWayToDestination();
				for(int counter=0;counter<agents.size();counter++)
				{
					if(i!=counter)
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
						
						//	System.out.println(obj.getId() +" in Detection "+ obj.getPosition().getXPos()+","+obj.getPosition().getYPos());
						//	System.out.println(other.getId() +" in Detection "+ other.getPosition().getXPos()+","+other.getPosition().getYPos());
						
						boolean result=this.checkTimeBetween(obj, other);
						//System.out.println("Result von Überprüfung:"+result +" Iter:"+iter);
						//System.out.println("--------------------");
						if(result)
						{
							
							
							CombinedNameAndPos combined=new CombinedNameAndPos();
							combined.setName(agents.get(counter).getId());
							combined.setPos(obj.getPosition());
							otherAgents.add(combined);
							
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
				}
				//System.out.println("-------------");
				nameList.put(agent.getId(), otherAgents);
			
			}
			//System.out.println("End of call-----------------");
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
	



