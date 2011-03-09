package agentgui.physical2Denvironment.imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.ontology.Size;
import agentgui.physical2Denvironment.ontology.StaticObject;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.utils.EnvironmentWrapper;

/** 
 * This class provides the functionality to calculate a way from point A to Point B based on the
 * astar algoritmn. The used heuristic is the distance between the current position and the starting point and
 * the distance to the destination point.
 * 
 * @author Tim Lewen
 */

public class ImageHelper { // extends Agent {

	private static final long serialVersionUID = -9171029311830052421L;

	public static final int DIRECTION_FORWARD=0;
	public static final int DIRECTION_BACKWARD=1;
	public static final int DIRECTION_LEFT=2;
	public static final int DIRECTION_RIGHT=3;
	public static final int DIRECTION_UP_LEFT=4;
	public static final int DIRECTION_UP_RIGHT=5;
	public static final int DIRECTION_DOWN_LEFT=6;
	public static final int DIRECTION_DOWN_RIGHT=7;
	public static boolean READ_WORLD=false;
	int divideHeight;
	int divideWidth;
	double lastDistance=-20;
	ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> collition=new ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode>();
	private BufferedImage evn=null;
	private BufferedImage plan=null;
	
	boolean [][][] sub_screen=null;
	int endung=0;
	private float firstX;
	private float firstY;
	boolean first=false;
	boolean igoreNoDistanceChange=false;
	java.util.HashMap<Integer,Integer> dif=new java.util.HashMap <Integer,Integer>();
	int counter=0;
	int [] lastDirections=new int[4];
	int [] comParelastDirections=new int[4];
	int compareDirectionsCounter=5;
	int directionsCounter=0;
	
	Position from=null;
	Position to=null;
	String fromID="";
	String toID="";
	EnvironmentProviderHelper helper;
	
	
	public ImageHelper(String fromID,String toID,	EnvironmentProviderHelper helper )
	{
		this.fromID=fromID;
		this.toID=toID;
		this.helper=helper;
	
	}
	
	public ImageHelper() {
		
	}
	
	
	private void saveSVG(File svgFile, Document svgDoc){
		if(svgDoc != null){
			try {
			
				//System.out.println(Language.translate("Speichere SVG nach ")+" "+svgFile.getName());
				if(!svgFile.exists()){
					svgFile.createNewFile();
				}
				FileWriter fw = new FileWriter(svgFile);
				PrintWriter writer = new PrintWriter(fw);
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				writer.write("<!DOCTYPE svg PUBLIC '");
				writer.write(SVGConstants.SVG_PUBLIC_ID);
				writer.write("' '");
				writer.write(SVGConstants.SVG_SYSTEM_ID);
				writer.write("'>\n\n");
				



				SVGTranscoder t = new SVGTranscoder();
				t.transcode(new TranscoderInput(svgDoc), new TranscoderOutput(writer));
				writer.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	private agentgui.physical2Denvironment.imageProcessing.StepNode createHelpNode(int direction)
	{
		agentgui.physical2Denvironment.imageProcessing.StepNode stepNode=new agentgui.physical2Denvironment.imageProcessing.StepNode();
		return stepNode;
		
	}
	
	
     private void deleteNodes(Document doc,Node node)
     {
    	 NodeList list=node.getChildNodes();
    	// System.out.println("List Size:"+list.getLength());
    	 for(int i=0;i<list.getLength();i++)
    	 {
    	 	 node.removeChild(list.item(i));
    	 }
    	  if(list.getLength()!=0)
    	  {
    		  this.deleteNodes(doc, node);
    	  }
     }
	


		
		
		
		
		
		
	
	
	private double getDistance(float target_x,float target_y,float x,float y)
	{
		float xPosDiff=Math.abs(target_x-x);
		float yPosDiff=Math.abs(target_y-y);
	 return	Math.abs( Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff));
	}
	
	private agentgui.physical2Denvironment.imageProcessing.StepNode fillNode(Element e,agentgui.physical2Denvironment.imageProcessing.StepNode parent,float target_x,float target_y,int ownDirection)
	{
		agentgui.physical2Denvironment.imageProcessing.StepNode result=new agentgui.physical2Denvironment.imageProcessing.StepNode();	
		float x=Float.parseFloat(e.getAttribute("x"));
		float y=Float.parseFloat(e.getAttribute("y"));
		double first_distance=this.getDistance(target_x, target_y, x, y);
		result.setDirection(ownDirection);
		result.setDistance(first_distance);
		result.setParent(parent);
		result.setStoppInvestigating(false);
		result.setX(x);
		result.setY(y);
		result.setDistanceToOrginal(0.0d); //TODO
		return result;

	}

	private float getManhattanDistance(float target_x,float target_y,float x,float y)
	{
		float dx = Math.abs(target_x - x);
	    float dy = Math.abs(target_y- y);
	    float heuristic = dx+dy;
	    return heuristic;
	}
	private agentgui.physical2Denvironment.imageProcessing.StepNode fillNode(float x, float y,agentgui.physical2Denvironment.imageProcessing.StepNode parent,float target_x,float target_y,int ownDirection,float width,float height,int startXIndex,int startYIndex)
	{
	

		
		final int targetXIndex= (int) (target_x/width);
		final int targetYIndex=(int) (target_y/height);
		
		final  int currentXIndex=(int) (x/width);
		final  int currentYIndex= (int) (y/height);
		
		int costf=this.getVertOrHorGCost(startXIndex, startYIndex, currentXIndex, currentYIndex);
		int costh=this.getHCost(startXIndex, startYIndex, currentXIndex, currentYIndex);
		
		
		agentgui.physical2Denvironment.imageProcessing.StepNode result=new agentgui.physical2Denvironment.imageProcessing.StepNode();	
		
		 double first_distance= this.getManhattanDistance(target_x, target_y, x, y);          //this.getDistance(target_x, target_y, x, y);
		double manhattan_distance_to_origin=this.getManhattanDistance(x, y,parent.getX(), parent.getY());
		result.setDirection(ownDirection);
		//float xPosDiff = Math.abs(x - target_x);
		//float yPosDiff = Math.abs(y- target_y);

		// Calculate distance between start and destination Position 
		//float first_distance = (float) Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff);
		 //xPosDiff = Math.abs(x - parent.getX());
		//yPosDiff = Math.abs(y- parent.getY());
		 //float distance_to_origin=(float) Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff);
		result.setDistance(first_distance+manhattan_distance_to_origin);
		result.setParent(parent);
		result.setStoppInvestigating(false);
		result.setX(x);
		result.setY(y);
		double distance=this.getDistance( x,y,firstX,firstY);
		double totalDistance=this.getDistance(x, y, parent.getX(), parent.getY());
		float dx = Math.abs(target_x - x);
	    float dy = Math.abs(target_y- y);
	    float heuristic = dx+dy;
		result.setDistanceToOrginal(distance); 
		
		result.setTotal_distance(heuristic);
		return result;

	}
	private agentgui.physical2Denvironment.imageProcessing.StepNode fillNode(float x, float y,agentgui.physical2Denvironment.imageProcessing.StepNode parent,int ownDirection,int gCost,int hCost,int xIndex,int yIndex)
	{
		agentgui.physical2Denvironment.imageProcessing.StepNode result=new agentgui.physical2Denvironment.imageProcessing.StepNode();	
		
	
		result.setDirection(ownDirection);
	
		result.setParent(parent);
		result.setStoppInvestigating(false);
		result.setX(x);
		result.setY(y);
		result.setDistanceToOrginal(gCost);
		result.setDistance(hCost);
		result.setTotal_distance(gCost+hCost);
		result.setXIndex(xIndex);
		result.setYIndex(yIndex);
		return result;

	}
	private boolean handle_subgrids(boolean vert,int targetXIndex,int targetYIndex,int currentXIndex,int currentYIndex,int listXIndex,int listYIndex,agentgui.physical2Denvironment.imageProcessing.StepNode neighbour,agentgui.physical2Denvironment.imageProcessing.StepNode current,float width,float height,ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> openList,ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> closedList)
	{
		System.out.println("CurrentIndex:"+currentXIndex + "," + currentYIndex);
		int widthfactor=(int) (width/4);
		int heightfactor=(int) (height/4);
		float difference_width=Math.abs(width/4);
		float difference_height=Math.abs(height/4);
		boolean neighbourCheck=false;
		boolean check=false;
		int gCost;
		int hCost;
		// We know that's actually not possible go use this squerre so we check sub squeres
		
		
		// Check above
		
		if(currentYIndex-1>=0)
		{
			// Check 2 sub squerres in other main squerre
			for(int i=8;i<16;i++)
			{
			neighbourCheck=this.sub_screen[currentXIndex][currentYIndex-1][i];
			if(!neighbourCheck)
			{
				
				//System.out.println("Problem bei:"+currentXIndex +","+(currentYIndex-1) +","+i);
			//	System.out.println("Breche bei nach oben gucken ab!");
				break;
			}
				
			}
			
		}
		
		
		if(neighbourCheck)
		{
			for(int i=0;i<8;i++)
			{
			check=this.sub_screen[currentXIndex][currentYIndex][i];
			  if(!check)
			  {
				 // System.out.println("Finde oben im eigenen Squerre nichts!");
				  break;
			  }
			}
			if(check)
			{
				System.out.println("Bingo gefunden Y-1! 8-16");
				if(vert)
				{
				gCost= this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex-1);
				}
				else
				{
				gCost= this.getDiagGCost(listXIndex, listYIndex, currentXIndex, currentYIndex-1);	
				}
			    hCost=this.getHCost(currentXIndex, currentYIndex-1,targetXIndex,targetYIndex);
			//float xInsertValue=0;
	
			    float xInsertValue=(currentXIndex)*width;
			    float  yInsertValue=(currentYIndex*height)-(difference_height*2);
			    neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_LEFT,gCost,hCost,currentXIndex,currentYIndex);
			    neighbour.setGridIndex(0);
			    checkAndReact(current, neighbour, openList, closedList);
			    //return true;
			}
		}
		
		
		
		if(currentYIndex+1<divideHeight)
		{
			for(int i=0;i<8;i++)
			{
			neighbourCheck=this.sub_screen[currentXIndex][currentYIndex+1][i];
			if(!neighbourCheck)
			{
				//System.out.println("Breche bei nach unten gucken ins andere Squerre gucken ab!");
			}
				break;
			}
			
		}
		
		
		if(neighbourCheck)
		{
			for(int i=8;i<16;i++)
			{
			check=this.sub_screen[currentXIndex][currentYIndex][i];
			  if(!check)
			  {
				 
				  break;
			  }
			}
			if(check)
			{
				System.out.println("Bingo Y+1 0-8");
				if(vert)
				{
				gCost= this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);
				}
				else
				{
				gCost= this.getDiagGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);	
				}
			    hCost=this.getHCost(currentXIndex, currentYIndex+1,targetXIndex,targetYIndex);
			//float xInsertValue=0;
	
			    float xInsertValue=(currentXIndex)*width;
			    float  yInsertValue=(currentYIndex*height)+(2*difference_height);
			    neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_LEFT,gCost,hCost,currentXIndex,currentYIndex);
			    neighbour.setGridIndex(1);
			    checkAndReact(current, neighbour, openList, closedList);
			   // return true;
			}
		}
	
		
		if(currentXIndex-1>=0)
		{
			// Think about the height!!!!!
			check=false;
			if(currentYIndex+1<divideHeight) // Steel from above
			{
				  for(int i=10;i<16;i++) // Check left 
				  {
					  if(i!=12 && i!=13)
					  {
					  neighbourCheck=this.sub_screen[currentXIndex-1][currentYIndex][i];
					  }
					  if(!neighbourCheck)
					  {
						  break;
					  }
				  }
			}
			else
			{
				neighbourCheck=false;
			}
			 if(neighbourCheck)  // Check
			  {
					  for(int i=8;i<14;i++) // Check left Squere and stelle 1 row
					  {
						  if(i!=10&&i!=11)
						  {
						  check=this.sub_screen[currentXIndex][currentYIndex][i];
						  }
						  if(!check)
						  {
							  break;
						  }
					  }  
				  }
				  neighbourCheck=false;
				  if(currentYIndex+1<divideHeight)
				  {
					  if(check)
					  {
					  
					  for(int i=2;i<7;i++) // Check left Squere and stelle 1 row
					  {
						  if(i!=4&&i!=5)
						  {
						  neighbourCheck=this.sub_screen[currentXIndex-1][currentYIndex+1][i];
						  }
						  if(!neighbourCheck)
						  {
							  break;
						  }
					  }    
				  	}
				  }
				  
				  if(neighbourCheck)
				  {
					  for(int i=0;i<6;i++) // Check left Squere and stelle 1 row
					  {
						  if(i!=2&&i!=3)
						  {
						  neighbourCheck=this.sub_screen[currentXIndex][currentYIndex+1][i];
						  }
						  if(!neighbourCheck)
						  {
							  break;
						  }
					  }  
					    if(neighbourCheck)
					    {
					    	System.out.println("Treffer!!!");
					    	if(vert)
							{
							gCost= this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);
							}
							else
							{
							gCost= this.getDiagGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);	
							}
						    hCost=this.getHCost(currentXIndex, currentYIndex+1,targetXIndex,targetYIndex);
						//float xInsertValue=0;
				
						    float xInsertValue=(currentXIndex-1)*width+(2*difference_width);
						    float  yInsertValue=(currentYIndex*height)+(2*difference_height);
						    System.out.println("Neue Koordinaten:"+xInsertValue +"," + yInsertValue);
						    neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_LEFT,gCost,hCost,currentXIndex-1,currentYIndex);
						    neighbour.setGridIndex(2);
						    checkAndReact(current, neighbour, openList, closedList);
					    }
					  
				  }	
				  if(currentXIndex+1<divideWidth)
					{
						// Think about the height!!!!!
						check=false;
						if(currentYIndex-1>=0) // Steel from above
						{
							  for(int i=0;i<6;i++) // Check left Squere and stelle 1 row
							  {
								  if(i!=2&&i!=3)
								  {
								  neighbourCheck=this.sub_screen[currentXIndex+1][currentYIndex][i];
								  }
								  if(!neighbourCheck)
								  {
									  break;
								  }
							  }
						}
						else
						{
							neighbourCheck=false;
						}
						 if(neighbourCheck)  // Check
						  {
								  for(int i=2;i<8;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=4&&i!=5)
									  {
									  check=this.sub_screen[currentXIndex][currentYIndex][i];
									  }
									  if(!check)
									  {
										  break;
									  }
								  }  
							 }
							  neighbourCheck=false;
							  if(currentYIndex-1>=0)
							  {
								  if(check)
								  {
								  
								  for(int i=8;i<14;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=10&i!=11)
									  {
									  neighbourCheck=this.sub_screen[currentXIndex+1][currentYIndex-1][i];
									  }
									  if(!neighbourCheck)
									  {
										  break;
									  }
								  }    
							  	}
							  }
							  
							  if(neighbourCheck)
							  {
								  for(int i=10;i<16;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=12&&i!=13)
									  {
									  neighbourCheck=this.sub_screen[currentXIndex][currentYIndex-1][i];
									  }
									  if(!neighbourCheck)
									  {
										  break;
									  }
								  }  
								    if(neighbourCheck)
								    {
								    	System.out.println("Treffer II!!!");
								    	if(vert)
										{
										gCost= this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);
										}
										else
										{
										gCost= this.getDiagGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);	
										}
									    hCost=this.getHCost(currentXIndex, currentYIndex+1,targetXIndex,targetYIndex);
									//float xInsertValue=0;
							
									    float xInsertValue=(currentXIndex)*width;
									    float  yInsertValue=(currentYIndex-1*height)+(2*difference_height);
									    System.out.println("Neue Koordinaten:"+xInsertValue +"," + yInsertValue);
									    neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_LEFT,gCost,hCost,currentXIndex-1,currentYIndex);
									    neighbour.setGridIndex(3);
									    checkAndReact(current, neighbour, openList, closedList);
								    }
								  
							  }	 
					}	  
				  
				  
				
				  if(currentXIndex+1<divideWidth)
					{
						// Think about the height!!!!!
						check=false;
						if(currentYIndex+1<divideHeight) // Steel from above
						{
							  for(int i=8;i<14;i++) // Check left Squere and stelle 1 row
							  {
								  if(i!=10&&i!=11)
								  {
								  neighbourCheck=this.sub_screen[currentXIndex+1][currentYIndex][i];
								  }
								  if(!neighbourCheck)
								  {
									  break;
								  }
							  }
						}
						else
						{
							neighbourCheck=false;
						}
						 if(neighbourCheck)  // Check
						  {
								  for(int i=10;i<16;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=12&&i!=13)
									  {
									  check=this.sub_screen[currentXIndex][currentYIndex][i];
									  }
									  if(!check)
									  {
										  break;
									  }
								  }  
							 }
							  neighbourCheck=false;
							  if(currentYIndex<divideHeight)
							  {
								  if(check)
								  {
								  
								  for(int i=0;i<6;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=2&&i!=3)
									  {
									  neighbourCheck=this.sub_screen[currentXIndex+1][currentYIndex+1][i];
									  }
									  if(!neighbourCheck)
									  {
										  break;
									  }
								  }    	
							  	}
							  }
							  
							  if(neighbourCheck)
							  {
								  for(int i=2;i<8;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=4&&i!=5)
									  {
									  neighbourCheck=this.sub_screen[currentXIndex][currentYIndex+1][i];
									  }
									  if(!neighbourCheck)
									  {
										  break;
									  }
								  }  
								    if(neighbourCheck)
								    {
								    	System.out.println("Treffer III!!!");
								    	if(vert)
										{
										gCost= this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);
										}
										else
										{
										gCost= this.getDiagGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);	
										}
									    hCost=this.getHCost(currentXIndex, currentYIndex+1,targetXIndex,targetYIndex);
									//float xInsertValue=0;
							
									    float xInsertValue=(currentXIndex)*width;
									    float  yInsertValue=(currentYIndex*height)+(2*difference_height);
									    System.out.println("Neue Koordinaten:"+xInsertValue +"," + yInsertValue);
									    neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_LEFT,gCost,hCost,currentXIndex-1,currentYIndex);
									    neighbour.setGridIndex(4);
									    checkAndReact(current, neighbour, openList, closedList);
								    }
								  
							  }	 
					}	  
				  if(currentXIndex-1>=0)
					{
						// Think about the height!!!!!
						check=false;
						if(currentYIndex-1>0) // Steel from above
						{
							  for(int i=0;i<6;i++) // Check left Squere and stelle 1 row
							  {
								  if(i!=2&&i!=5)
								  {
								  neighbourCheck=this.sub_screen[currentXIndex-1][currentYIndex][i];
								  }
								  if(!neighbourCheck)
								  {
									  break;
								  }
							  }
						}
						else
						{
							neighbourCheck=false;
						}
						 if(neighbourCheck)  // Check
						  {
								  for(int i=2;i<8;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=4&&i!=5)
									  {
									  check=this.sub_screen[currentXIndex][currentYIndex][i];
									  }
									  if(!check)
									  {
										  break;
									  }
								  }  
							 }
							  neighbourCheck=false;
							  if(currentYIndex<divideHeight)
							  {
								  if(check)
								  {
								  
								  for(int i=10;i<16;i++) // Check left Squere and stelle 1 row
								  {
									 if(i!=12&&i!=13)
									 {
									  neighbourCheck=this.sub_screen[currentXIndex-1][currentYIndex-1][i];
									 }
									  if(!neighbourCheck)
									  {
										  break;
									  }
								  }    
							  	}
							  }
							  
							  if(neighbourCheck)
							  {
								  for(int i=8;i<14;i++) // Check left Squere and stelle 1 row
								  {
									  if(i!=10 && i!=11)
									  {
									  neighbourCheck=this.sub_screen[currentXIndex][currentYIndex-1][i];
									  }
									  if(!neighbourCheck)
									  {
										  break;
									  }
								  }  
								    if(neighbourCheck)
								    {
								    	System.out.println("TrefferIV!!");
								    	if(vert)
										{
										gCost= this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);
										}
										else
										{
										gCost= this.getDiagGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);	
										}
									    hCost=this.getHCost(currentXIndex, currentYIndex+1,targetXIndex,targetYIndex);
									//float xInsertValue=0;
							
									    float xInsertValue=(currentXIndex)*width;
									    float  yInsertValue=( (currentYIndex-1)*height)+(2*difference_height);
									    System.out.println("Neue Koordinaten:"+xInsertValue +"," + yInsertValue);
									    neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_LEFT,gCost,hCost,currentXIndex-1,currentYIndex);
									    neighbour.setGridIndex(4);
									    checkAndReact(current, neighbour, openList, closedList);
								    }
								  
							  }	 
				  
					}
				  
				  
				  
				  
				  
					}
			
		return false;
	}
	
	private int getVertOrHorGCost(int startIndexX,int startIndexY, int currentIndexX, int currentIndexY)
	{
		//System.out.println("Vert");
		//System.out.println("StartIndex:"+startIndexX +"," +startIndexY +":,"+currentIndexX +","+ currentIndexY);
		int val=Math.abs(startIndexX-currentIndexX)+Math.abs(startIndexY-currentIndexY);
		//System.out.println("Val:"+(val*10));
		return val*10;
	}
	
	private void checkAndReact(agentgui.physical2Denvironment.imageProcessing.StepNode current, agentgui.physical2Denvironment.imageProcessing.StepNode neighbour,ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> openList, ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> closedList)
	{
		if(openList.contains(neighbour))
		{
//			System.out.println("----------------------------");
//			System.out.println("Current G:"+current.getDistanceToOrginal());
//			System.out.println("Neighbour G:" + neighbour.getDistanceToOrginal());
			if(!this.smaller(current, neighbour))
			{
				int cost=10;//this.getVertOrHorGCost(current.getXIndex(),current.getYIndex(), neighbour.getXIndex(), neighbour.getYIndex());
				
				if(neighbour.getDistanceToOrginal()+cost<current.getDistanceToOrginal())
				{
				  if(neighbour.getParent().is_deep_equals(current))
				  {
					  //neighbour.setParent(current.getParent());
					 // neighbour.setParent(null);
					  //neighbour.setParent(neighbour.getParent().getParent());
					//System.out.println("Neighbour nicht ändert");
				  }
				  else
				  {
					//  System.out.println("Neighbour nicht angepasst");
				  }
//				System.out.println("Change parent!");
//			    System.out.println("Former Parent Index:" + current.getXIndex() +"," + current.getYIndex());
//			    System.out.println("Parent Index:" + neighbour.getXIndex() +"," + neighbour.getYIndex());
//			    
//			    System.out.println("Former Parent Pos:" + current.getX() +"," + current.getY());
//			    System.out.println("Parent Index:" + neighbour.getX() +"," + neighbour.getY());
			    
				//current.setParent(neighbour);
				current.setDistanceToOrginal(neighbour.getDistanceToOrginal()+cost);
				current.setTotal_distance(current.getDistanceToOrginal()+current.getDistance());
				}
			
			}
//			System.out.println("---------------------");
				
			
		}
		else
		{
			if(!closedList.contains(neighbour))
			{
			
				openList.add(neighbour);
			
			}
			
		}
		
		
		
		
	}
	
	
	private int getDiagGCost(int startIndexX,int startIndexY, int currentIndexX, int currentIndexY)
	{
		int val=Math.abs(startIndexX-currentIndexX)+Math.abs(startIndexY-currentIndexY);
		//System.out.println("Val:"+(val*14));
		return val*14;
	}
	
	private int getHCost(int startIndexX,int startIndexY, int currentIndexX, int currentIndexY)
	{
	//	System.out.print("H");
		//System.out.println("sTRA:"+startIndexX +"," +startIndexY +":,"+currentIndexX +","+ currentIndexY);
		int val=Math.abs(startIndexX-currentIndexX)+Math.abs(startIndexY-currentIndexY);
		//System.out.println("Val:"+(val*10));
		return val*10;
	}
	
	private agentgui.physical2Denvironment.imageProcessing.StepNode withoutGrid(String id,float target_x,float target_y,float width,float height,String target_id , int direction , float lookAhead, agentgui.physical2Denvironment.imageProcessing.StepNode parent,int pixel) throws Exception
	{
	
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
		//datasrc.Node start=this.fillNode(xStart, yStart, parent, target_x, target_y, -1);
		agentgui.physical2Denvironment.imageProcessing.StepNode current=parent;
		closedList.add(current);
		while(this.getDistance(target_x, target_y, currentXPos, currentYPos)>10)
		{
			//System.out.println("Distance:"+this.getDistance(target_x, target_y, currentXPos, currentYPos));
			//System.out.println("currentXPos:"+currentXPos +","+currentYPos);
			//System.out.println("OpenList Size: "+ openList.size());
			//System.out.println("Direction:"+current.getDirection());
			
			
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
					 //System.out.println("left ist not in list!!");
					 if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_LEFT, Color.gray.getRGB()))
					  { 
						// System.out.println("LeftAllowed");
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"black");	
					  	  openList.add(new_current);
					  	 // System.out.println("Füge Links hinzu:"+(currentXPos-lookAhead) +","+currentYPos);
					   }
					 else
					 {
						 if(new_current!=null)
						 {
						 this.drawLineToSave("l", String.valueOf(new_current.getX()), String.valueOf(width), String.valueOf(new_current.getY()), String.valueOf(height), doc,true,"red");	
					 
						 }
					 }
					
				}
				 else
				 {
					// System.out.println("Links würde gehen aber verhindert");
				 }
			}
			// Right
			 new_current=null;
			if(currentXPos+lookAhead<worldWidth)
			{
				  if(!this.isInList(openList, closedList, currentXPos+lookAhead, currentYPos,ImageHelper.DIRECTION_RIGHT))
			       {
					  new_current=this.fillNode(currentXPos+lookAhead, currentYPos, current, target_x, target_y, ImageHelper.DIRECTION_RIGHT,width,height,listXIndex,listYIndex);
					 // System.out.println("Right is not in list");
					  	if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_RIGHT, Color.gray.getRGB()))
					  	{
					  	//	System.out.println("Right allowed");
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
				  else
				  {
					 // System.out.println("Rechts würde gehen aber verhindert");
				  }
						
			}
			
			// Forward
			 new_current=null;
			if(currentYPos+lookAhead<worldHeight)
			{
				  if(!this.isInList(openList, closedList, currentXPos, currentYPos+lookAhead,ImageHelper.DIRECTION_FORWARD))
			       {
					  new_current=this.fillNode(currentXPos, currentYPos+lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_FORWARD,width,height,listXIndex,listYIndex);
					 // System.out.println("Forward is not in list");
					  	if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_FORWARD, Color.gray.getRGB()))
					  	{
					  	//	System.out.println("Forward ist allowed");
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
				  else
				  {
					 // System.out.println("Vorwärts würde gehen aber verhindert");
				  }
						
			}
			// Backwards
			 new_current=null;
			if(currentYPos-lookAhead>=0)
			{
				  if(!this.isInList(openList, closedList, currentXPos, currentYPos-lookAhead , ImageHelper.DIRECTION_BACKWARD))
			       {
					  new_current=this.fillNode(currentXPos, currentYPos-lookAhead, current, target_x, target_y, ImageHelper.DIRECTION_BACKWARD,width,height,listXIndex,listYIndex);
					  //System.out.println("Backwards is not in list");
					  	if(this.tranformAndCheckImage(this.evn, currentXPos, currentYPos, width, height, lookAhead, ImageHelper.DIRECTION_BACKWARD, Color.gray.getRGB()))
					  	{
					  			//System.out.println("Backwards is allowed");
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
				  else
				  {
					 // System.out.println("Backwards würde gehen aber verhindert");
				  }
						
			}
			
			
			//openList.remove(current);
			closedList.add(current);
		    int index=this.getMinimumDistance(openList);
		    //System.out.println("Neuer Index:"+index);
		    if(index==-1)
		    {
		    
		    	if(lookAhead-2<0)
		    	{
		    		System.out.println("Fehler");
		    		return current;
		    	}
		    	else
		    	{
		    	System.out.println("Neue Rekurstion!");
		    	
				SVGSafe sg=new SVGSafe();
				sg.write("WayPath"+counter+".svg", doc);
		    	return this.withoutGrid(id, target_x, target_y, width, height, target_id, direction, lookAhead-2, parent, pixel);
		    	}
		    	
		    	
		    	//return current;
		    }
		   
		    
		    
		    current=openList.get(index);
		    
			openList.remove(index);
			 currentXPos=current.getX();
			currentYPos=current.getY();
			
			if(this.getDistance(target_x, target_y, currentXPos, currentYPos)<=10)
			{
				System.out.println("Treffer!");
				SVGSafe sg=new SVGSafe();
				sg.write("WayPath"+counter+".svg", doc);
			  return current;
			}
			
			
			
		}
		  return current;
		
	}
	
	private boolean isInList(ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> open,ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> closed, float x,float y,int direction)
	{
		for(int i=0;i<open.size();i++)
		{
			if(open.get(i).getX()==x&&open.get(i).getY()==y)
			{
				//System.out.println("Vorhanden in open!");
				return true;
			}
		}
		for(int i=0;i<closed.size();i++)
		{
			if(closed.get(i).getX()==x&&closed.get(i).getY()==y)
			{
				//System.out.println("Vorhanden in Closed");
				return true;
			}
		}
		return false;
	}

	
	private agentgui.physical2Denvironment.imageProcessing.StepNode originalAStar(String id,float target_x,float target_y,float width,float height,String target_id , int direction , float lookAhead, agentgui.physical2Denvironment.imageProcessing.StepNode parent,int pixel) throws Exception
	{
		
	
		Document doc=helper.getSVGDoc();
		float worldWidth=helper.getEnvironment().getRootPlayground().getSize().getWidth();
		float worldHeight=helper.getEnvironment().getRootPlayground().getSize().getHeight();
		divideWidth=(int) (worldWidth/width);
		 divideHeight= (int) (worldHeight/height);
		 float yValue=0;
		 float xValue=0-width;
		 System.out.println("LookAhead:"+lookAhead);
		 System.out.println("World Width:"+worldWidth);
		 System.out.println("World Height:"+worldHeight);
		System.out.println("DiveWidth:"+divideWidth);
		System.out.println("Dive Height:"+divideHeight);
		System.out.println("Width:"+width);
		System.out.println("Height:"+height);
		int lastIndex=0;
		float paintX=0.0f;
		float paintY=0.0f;
		
		
		boolean [][] screen=new boolean[divideWidth+1][divideHeight+1];
		sub_screen=new boolean[divideWidth+1][divideHeight+1][16];
		int widthfactor=(int) (width/4);
		int heightfactor=(int) (height/4);
		System.out.println("Widhfacot:"+widthfactor);
		System.out.println("HeightFactor:"+heightfactor);
		float difference_width=Math.abs(width/4);
		System.out.println("Difference_width:"+difference_width);
		float difference_height=Math.abs(height/4);
		System.out.println("Difference_Height:"+difference_height);
		for(int xStart=0;xStart<=divideWidth;xStart++)
		{
			xValue+=width;
			//System.out.println("XValue:"+xValue);
			 yValue=0-height;
			 paintX+=width;
			
		
			for(int yStart=0;yStart<=divideHeight;yStart++)
			{
			
				yValue+=height;
				int sub_screen_counter=0;
				//System.out.println("XPos im Array:"+xStart);
				//System.out.println("YPos im Array:"+ yStart);
				//System.out.println("YValue:"+yValue);
				//System.out.println("XValue:"+xValue);
			
			     paintY+=height;
			    //this.drawLineToSave("l"+xStart+"+"+yStart, String.valueOf(paintX), String.valueOf(paintX), "0.0", String.valueOf(worldHeight), doc,false,"black");
				//this.drawLineToSave("r"+xStart+"+"+yStart, "0.0", String.valueOf(worldWidth), String.valueOf(paintY), String.valueOf(paintY), doc,false,"black");	
			
				
			    screen[xStart][yStart]=this.tranformAndCheckImage(this.evn,xValue,yValue,width,height, 0,ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			    this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf(xValue), String.valueOf(width), String.valueOf(yValue), String.valueOf(height), doc,false,"blue");
			    
			    sub_screen[xStart][yStart][0]=this.tranformAndCheckImage(this.evn,xValue,yValue,widthfactor,heightfactor,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			    if(sub_screen[xStart][yStart][0])
			   {
			    this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf(xValue), String.valueOf(widthfactor), String.valueOf(yValue), String.valueOf(heightfactor), doc,false,"red");	
			    }
			    
			    sub_screen[xStart][yStart][1]=this.tranformAndCheckImage(this.evn,xValue+difference_width,yValue,difference_width,heightfactor,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			    if(sub_screen[xStart][yStart][1])
			    {
			    this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf( (xValue+difference_width)), String.valueOf(difference_width), String.valueOf(yValue), String.valueOf(heightfactor), doc,false,"red");	
			     }
			   sub_screen[xStart][yStart][2]=this.tranformAndCheckImage(this.evn,xValue+difference_width*2,yValue,difference_width,heightfactor,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
		       if(sub_screen[xStart][yStart][2])
		       {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*2)), String.valueOf(difference_width), String.valueOf(yValue), String.valueOf(heightfactor), doc,false,"red");	
			   }
			    
		       sub_screen[xStart][yStart][3]=this.tranformAndCheckImage(this.evn,(xValue+difference_width*3),yValue,difference_width,heightfactor,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
		      if(sub_screen[xStart][yStart][2])
			  {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf( (xValue+difference_width*3)), String.valueOf(difference_width), String.valueOf(yValue), String.valueOf(heightfactor), doc,false,"red");	
			   }
			    
			    
			    
			    
			    sub_screen[xStart][yStart][4]=this.tranformAndCheckImage(this.evn,xValue,yValue+difference_height,widthfactor,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			    if(sub_screen[xStart][yStart][4])
			    {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue)), String.valueOf(widthfactor), String.valueOf( (yValue+difference_height)), String.valueOf(difference_height), doc,false,"red");	
			    }
			    
			    sub_screen[xStart][yStart][5]=this.tranformAndCheckImage(this.evn,xValue+difference_width*1,yValue+difference_height,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			     if(sub_screen[xStart][yStart][5])
			    {
			    this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*1)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height)), String.valueOf(difference_height), doc,false,"red");	
			    }
			    sub_screen[xStart][yStart][6]=this.tranformAndCheckImage(this.evn,xValue+difference_width*2,yValue+difference_height,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			    if(sub_screen[xStart][yStart][6])
			     {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*2)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height)), String.valueOf(difference_height), doc,false,"red");	
			     }
					    
			    sub_screen[xStart][yStart][7]=this.tranformAndCheckImage(this.evn,xValue+difference_width*3,yValue+difference_height,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
		         if(sub_screen[xStart][yStart][7])
			     {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*3)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height)), String.valueOf(difference_height), doc,false,"red");	
			     }
						          
				    
		       sub_screen[xStart][yStart][8]=this.tranformAndCheckImage(this.evn,xValue,yValue+difference_height*2,widthfactor,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			     if(sub_screen[xStart][yStart][8])
			    {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue)), String.valueOf(widthfactor), String.valueOf( (yValue+difference_height*2)), String.valueOf(difference_height), doc,false,"red");	
			    }
					
			    
			    sub_screen[xStart][yStart][9]=this.tranformAndCheckImage(this.evn,xValue+difference_width*1,yValue+difference_height*2,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
				 if(sub_screen[xStart][yStart][9])
			     {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*1)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height*2)), String.valueOf(difference_height), doc,false,"red");	
			     }
			   sub_screen[xStart][yStart][10]=this.tranformAndCheckImage(this.evn,xValue+difference_width*2,yValue+difference_height*2,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			   if(sub_screen[xStart][yStart][10])
			     {
			    this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*2)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height*2)), String.valueOf(difference_height), doc,false,"red");	
			     }
			   
			    sub_screen[xStart][yStart][11]=this.tranformAndCheckImage(this.evn,xValue+difference_width*3,yValue+difference_height*2,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			     if(sub_screen[xStart][yStart][11])
			 		    {
			     this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*3)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height*2)), String.valueOf(difference_height), doc,false,"red");	
			     }	    
		
			    
			    sub_screen[xStart][yStart][12]=this.tranformAndCheckImage(this.evn,xValue,yValue+difference_height*3,widthfactor,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
				 if(sub_screen[xStart][yStart][12])
				   {
				    this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue)), String.valueOf(widthfactor), String.valueOf( (yValue+difference_height*3)), String.valueOf(difference_height), doc,false,"red");	
				   }
			    
			    sub_screen[xStart][yStart][13]=this.tranformAndCheckImage(this.evn,xValue+difference_width*1,yValue+difference_height*3,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
				 if(sub_screen[xStart][yStart][13])
				   {
			    this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*1)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height*3)), String.valueOf(difference_height), doc,false,"red");	
			    }
			   sub_screen[xStart][yStart][14]=this.tranformAndCheckImage(this.evn,xValue+difference_width*2,yValue+difference_height*3,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			   if(sub_screen[xStart][yStart][14])
			 	    {
			   this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*2)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height*3)), String.valueOf(difference_height), doc,false,"red");	
			   }
			    sub_screen[xStart][yStart][15]=this.tranformAndCheckImage(this.evn,xValue+difference_width*3,yValue+difference_height*3,difference_width,difference_height,0, ImageHelper.DIRECTION_FORWARD,Color.gray.getRGB());
			    if(sub_screen[xStart][yStart][15])
					    {
			   this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf((xValue+difference_width*3)), String.valueOf(difference_width), String.valueOf( (yValue+difference_height*3)), String.valueOf(difference_height), doc,false,"red");	
			    }	    
		    
				
			    
			
				
				
				boolean val=screen[xStart][yStart];
				if(!val)
				{
			 //this.drawLineToSave("l"+xValue+"+"+yStart, String.valueOf(xValue), String.valueOf(width), String.valueOf(yValue), String.valueOf(height), doc,false,"red");		
			//	  this.drawLineToSave("l"+xStart+"+"+yStart, String.valueOf(xValue), String.valueOf(width), String.valueOf(yValue), String.valueOf(height), doc,true);	
				}
				else
				{
				
				//	  this.drawLineToSave("l"+xStart+"+"+yStart, String.valueOf(xValue), String.valueOf( (xValue+width)), String.valueOf(yValue), String.valueOf((yValue+height)), doc,false);
					// this.drawLineToSave("l"+xStart+"+"+yStart, String.valueOf(xValue), String.valueOf(width), String.valueOf(yValue), String.valueOf(height), doc,false,"red");		
				}
					
					
				}
				
		}
		
			
		SVGSafe sg=new SVGSafe();
		sg.write("RasterOhneSubBild.svg", doc);

		boolean end=false;
		
		
		ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> openList=new ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode>();
		ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode> closedList=new ArrayList<agentgui.physical2Denvironment.imageProcessing.StepNode>();
		// Calculate pos
		final int listXIndex= (int) (parent.getX()/width);
		final int listYIndex=  (int) (parent.getY()/height);
		
		final int targetXIndex= (int) (target_x/width);
		final int targetYIndex=(int) (target_y/height);
		System.out.println("TargetXIndex:"+targetXIndex);
		System.out.println("TargetYindex:"+targetYIndex);
		int currentXIndex=listXIndex;
		int currentYIndex=listYIndex;
		System.out.println("CurretIndex:"+currentXIndex+ "," + currentYIndex);
		if(screen[targetXIndex][targetYIndex])
		{
			System.out.println("Ziel erreichbar!");
		}
		else
		{
		System.out.println("Ziel nicht erreichbar!");	
		}
		if(screen[listXIndex][listYIndex])
		{
			System.out.println("Begin erreichbar!");
		}
		else
		{
		System.out.println("Begin nicht erreichbar!");	
		}
		
		
		agentgui.physical2Denvironment.imageProcessing.StepNode current=parent;
		
		agentgui.physical2Denvironment.imageProcessing.StepNode neighbour=null;
		boolean ende=false;
		closedList.add(parent);
		
			while(!ende)
		{
				counter++;
				//System.out.println("Current TOal Distance"+current.getTotal_distance());
				////System.out.println(currentXIndex +","+currentYIndex);
				//System.out.println("Size Open List:"+openList.size());
				//System.out.println("CLosed List:"+closedList.size());
			     System.out.println("Counter:"+counter);
							
		
				if(currentXIndex==targetXIndex&&currentYIndex==targetYIndex)
				{
					System.out.println("Gefunden ziel gefunden!");
					//SVGSafe sg=new SVGSafe();
					 // sg.write("Check.svg", doc);
					  ende=true;
					  return current;
				}
		// Left
		if(currentXIndex-1>=0)
		{
			if(screen[currentXIndex-1][currentYIndex])
				
			{
				int gCost= this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex-1, currentYIndex);
				int hCost=this.getHCost(currentXIndex-1, currentYIndex,targetXIndex,targetYIndex);
				float xInsertValue=(currentXIndex-1)*width;
  				float yInsertValue=currentYIndex*height;
			  neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_LEFT,gCost,hCost,currentXIndex-1,currentYIndex);
				checkAndReact(current, neighbour, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
			
			}
			else
			{
				this.handle_subgrids(true,targetXIndex, targetYIndex, currentXIndex-1, currentYIndex, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
			
				
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");
			}
			
			
			
		}
		//RIGHT
		if(currentXIndex+1<divideWidth)
		{
			if(screen[currentXIndex+1][currentYIndex])
			{
				int gCost=this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex+1, currentYIndex);
				int hCost=this.getHCost(currentXIndex+1, currentYIndex,targetXIndex,targetYIndex);
  				//float xInsertValue=current.getX()+width+ObjectToWidthDifference;
  				//float yInsertValue= current.getY();
  				
  				float xInsertValue=(currentXIndex+1)*width;
  				float yInsertValue=currentYIndex*height;
				neighbour=this.fillNode(xInsertValue,yInsertValue, current, DIRECTION_RIGHT,gCost,hCost,currentXIndex+1,currentYIndex);
				checkAndReact(current, neighbour, openList, closedList);
			//	this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
			}
			else
			{
				this.handle_subgrids(true,targetXIndex, targetYIndex, currentXIndex+1, currentYIndex, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");
			}
			
		}
		// BEHIND
		if(currentYIndex-1>=0)
		{
			if(screen[currentXIndex][currentYIndex-1])
			{
				int gCost=this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex-1);
				int hCost=this.getHCost(currentXIndex, currentYIndex-1,targetXIndex,targetYIndex);
				float xInsertValue=currentXIndex*width;
  				float yInsertValue=(currentYIndex-1)*height;
				neighbour=this.fillNode(xInsertValue, yInsertValue, current,DIRECTION_BACKWARD, gCost,hCost,currentXIndex,currentYIndex-1 );
				checkAndReact(current, neighbour, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
			}
			else
			{
				this.handle_subgrids(true,targetXIndex, targetYIndex, currentXIndex, currentYIndex-1, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
			//	this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");
			}
			
			
			
		}
		// Forward
		if(currentYIndex+1<divideHeight)
		{
			if(screen[currentXIndex][currentYIndex+1])
			{
				int gCost=this.getVertOrHorGCost(listXIndex, listYIndex, currentXIndex, currentYIndex+1);
				int hCost=this.getHCost(currentXIndex, currentYIndex+1,targetXIndex,targetYIndex);
				float xInsertValue=(currentXIndex)*width;
  				float yInsertValue=(currentYIndex+1)*height;
  				neighbour=this.fillNode(xInsertValue, yInsertValue, current, DIRECTION_BACKWARD, gCost,hCost,currentXIndex,currentYIndex+1);
				checkAndReact(current, neighbour, openList, closedList);
			
			
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
			}
			else
			{
			this.handle_subgrids(true,targetXIndex, targetYIndex, currentXIndex, currentYIndex+1, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");
			}
			
			
		}
		
		if(currentYIndex-1>=0&&currentXIndex-1>=0)
		{
			if(screen[currentXIndex-1][currentYIndex-1]&&screen[currentXIndex][currentYIndex-1]&&screen[currentXIndex-1][currentYIndex])
			{
				int gCost=this.getDiagGCost(listXIndex, listYIndex, currentXIndex-1, currentYIndex-1);
				int hCost=this.getHCost(currentXIndex-1, currentYIndex-1,targetXIndex,targetYIndex);
				float xInsertValue=(currentXIndex-1)*width;
  				float yInsertValue=(currentYIndex-1)*height;
  				
				neighbour=this.fillNode(xInsertValue,yInsertValue, current, 5, gCost , hCost,currentXIndex-1,currentYIndex-1);
				checkAndReact(current, neighbour, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
			}
			else
			{
				//this.handle_subgrids(targetXIndex, targetYIndex, currentXIndex-1, currentYIndex-1, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");
			}
			
		}
		
		if(currentXIndex+1<divideWidth&&currentYIndex+1<divideHeight)
		{
			if(screen[currentXIndex+1][currentYIndex+1]&&screen[currentXIndex+1][currentYIndex]&&screen[currentXIndex][currentYIndex+1])
			{
				int gCost=this.getDiagGCost(listXIndex, listYIndex, currentXIndex+1, currentYIndex+1);
				int hCost=this.getHCost(currentXIndex+1, currentYIndex+1,targetXIndex,targetYIndex);
				float xInsertValue=(currentXIndex+1)*width;
  				float yInsertValue=(currentYIndex+1)*height;
  		
				neighbour=this.fillNode(xInsertValue, yInsertValue, current, 6, gCost, hCost,currentXIndex+1,currentYIndex+1);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
				checkAndReact(current, neighbour, openList, closedList);
			}
			else
			{
				
				//this.handle_subgrids(targetXIndex, targetYIndex, currentXIndex+1, currentYIndex+1, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");
			}
		}
		
		if(currentXIndex-1>=0&&currentYIndex+1<divideHeight)
		{
			if(screen[currentXIndex-1][currentYIndex+1]&&screen[currentXIndex-1][currentYIndex]&&screen[currentXIndex][currentYIndex+1])
			{
				int gCost=this.getDiagGCost(listXIndex, listYIndex, currentXIndex-1, currentYIndex+1);
				int hCost=this.getHCost(currentXIndex-1, currentYIndex+1,targetXIndex,targetYIndex);
				float xInsertValue=(currentXIndex-1)*width;
  				float yInsertValue=(currentYIndex+1)*height;
  				agentgui.physical2Denvironment.imageProcessing.StepNode stepNode=this.createHelpNode(7);
				neighbour=this.fillNode(xInsertValue, yInsertValue, current,7,gCost,hCost,currentXIndex-1,currentYIndex+1);
			//	this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
				checkAndReact(current, neighbour, openList, closedList);
					
			}
			else
			{
		       //this.handle_subgrids(targetXIndex, targetYIndex, currentXIndex-1, currentYIndex+1, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
			//	this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");
			}
		}
		
		
		if(currentXIndex+1<divideWidth&&currentYIndex-1>=0)
		{
			if(screen[currentXIndex+1][currentYIndex-1]&&screen[currentXIndex][currentYIndex-1]&&screen[currentXIndex+1][currentYIndex])
			{
				int gCost=this.getDiagGCost(listXIndex, listYIndex, currentXIndex+1, currentYIndex-1);
				int hCost=this.getHCost(currentXIndex+1, currentYIndex-1,targetXIndex,targetYIndex);
				float xInsertValue=(currentXIndex+1)*width;
  				float yInsertValue=(currentYIndex-1)*height;
  				agentgui.physical2Denvironment.imageProcessing.StepNode stepNode=this.createHelpNode(8);
				neighbour=this.fillNode(xInsertValue,yInsertValue ,current,8,gCost,hCost,currentXIndex+1,currentYIndex-1);
			    checkAndReact(current, neighbour, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"blue");	
			}
			else
			{
				//this.handle_subgrids(targetXIndex, targetYIndex, currentXIndex+1, currentYIndex-1, listXIndex, listYIndex, neighbour, current, width, height, openList, closedList);
				//this.drawLineToSave("rect", String.valueOf(neighbour.getX()), String.valueOf(width), String.valueOf(current.getY()), String.valueOf((yValue+height)), doc,true,"yellow");	
			}
		}
	
		
		 
		 closedList.add(current);
		 int index=this.getMinimumCost(openList);
		 if(index==-1)
		 {
			 System.out.println("Die Liste ist leer!");
			 //SVGSafe sg=new SVGSafe();
			  //sg.write("Check.svg", doc);
			  index= this.getMinimumCost(closedList);
			  return closedList.get(index);
					  
			  
		
		 }
		 agentgui.physical2Denvironment.imageProcessing.StepNode result=openList.get(index);	
	    // System.out.println("Result Distance:"+ result.getTotal_distance());
	 	//System.out.println("Size vorher Remove new Element:"+openList.size());
	     openList.remove(index);
	    // System.out.println("Size nacher:"+openList.size());
	     closedList.add(result);
	     current=result;
	     xValue=current.getX();
	     yValue=current.getY();
	     System.out.println("Index:"+current.getXIndex() +"," + current.getYIndex());
	     //this.drawLineToSave("rect", String.valueOf(xValue), String.valueOf(width), String.valueOf(yValue), String.valueOf(height), doc,false,"blue");
	     if(counter==9993)
	     { //SVGSafe sg=new SVGSafe();
	    	 //sg.write("Check.svg", doc);
	    	 return current;
	     }
	   		
	     currentXIndex=current.getXIndex();
		 currentYIndex=current.getYIndex();
		
		 
			
		
	
		}
			agentgui.physical2Denvironment.imageProcessing.StepNode returnNode=null;
			while(current.getParent()!=null)
			{
				if(current.getXIndex()!=targetXIndex&&current.getY()!=targetYIndex)
				{
					current=current.getParent();
				}
				else
				{
					return current;
				}
			}
			 return current;
	}
		
	private boolean smaller(agentgui.physical2Denvironment.imageProcessing.StepNode current, agentgui.physical2Denvironment.imageProcessing.StepNode neighbour)
	{
		  
			  return current.getDistanceToOrginal()<neighbour.getDistanceToOrginal();
			  
	
	}
	
		
	private int getMinimumCost(ArrayList <agentgui.physical2Denvironment.imageProcessing.StepNode> list)
	{
		int min=Integer.MAX_VALUE;
		int result=-1;
		for(int i=0;i<list.size();i++)
		{
			if(min>list.get(i).getTotal_distance())
			{
				min=(int) list.get(i).getTotal_distance();
			   	result=i;
			   	//System.out.println("Min:"+min);
			}
		}
		return result;
	}
		
		
		
		
		
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
			   	//System.out.println("Min:"+min);
			}
		}
		return result;
	}
		
	
	
	
	
	public agentgui.physical2Denvironment.imageProcessing.StepNode createPlanImage(String id,String target_id , int direction , float lookAhead) throws Exception
	{
		this.createManipulatedWorld();
	
		Document doc=helper.getSVGDoc();
		System.out.println("URI:"+doc.getBaseURI());
		Element target=doc.getElementById(target_id);
		float target_x=Float.parseFloat(target.getAttribute("x"));
		float target_y=Float.parseFloat(target.getAttribute("y"));
		System.out.println("Target X:"+target_x);
		System.out.println("Target Y:"+target_y);
		Element self=doc.getElementById(id);
		final float width=Float.parseFloat(self.getAttribute("width"));
		//System.out.println("Width:"+width);
		final float height=Float.parseFloat(self.getAttribute("height"));
		float x=Float.parseFloat(self.getAttribute("x"));
		float y=Float.parseFloat(self.getAttribute("y"));
		System.out.println("Self Id:"+id);
		 this.firstX=x;
		 this.firstY=y;
		double first_distance=this.getDistance(target_x, target_y, x, y);
		//System.out.println("FirstDistance:"+first_distance);
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
			
			
		 System.out.println("Firx X:"+x);
		 System.out.println("First Y:"+y);
		 root.setParent(null);
		// datasrc.DataSource.add(root);
		 //this.draw_way(id, x, y, width, height, 0, 0, target_id);
		 //this.draw_way(id, x, y,width,height ,0,0.0f, target_id);
		int color=this.getPixelsOnce(plan, x, y, width, height);
		agentgui.physical2Denvironment.imageProcessing.StepNode targetNode= this.withoutGrid(id, target_x, target_y, width, height, target_id, direction, 10.0f,root , -1) ;   //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, -1);          //      //this.originalAStar(id, target_x, target_y, width, height, target_id, direction, lookAhead, root, color) ;        //handle_subNodes_brute(id,target_x, target_y,  width, height, target_id, direction, lookAhead,root,color);
		 
		System.out.println("Fertig!!!!!");
		//System.out.println("Node:"+targetNode.getX() +"," +targetNode.getY());
		//System.out.println("Final Distance:"+targetNode.getDistance());
		//System.out.println("Target Pos:"+target_x +","+target_y);
		
		
	
	return targetNode;
		
	}
	
	
	
	
	
	
	
	
	
	

	
	
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
	public void print(NodeList node)
	{
		for(int i=0;i<node.getLength();i++)
		{
			this.print(node.item(i));
		}
	}
	public void print(Node node)
	{
		System.out.println("Parent Node name:"+node.getParentNode().getLocalName());
		System.out.println("Node name:"+node.getNodeName());
		System.out.println("Value:"+ node.getNodeValue());
		System.out.println("");
		if(node.getChildNodes().getLength()>0)
		{
			System.out.println("Ebene tiefer!");
			this.print(node.getChildNodes());
		}
	
		
	}
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
	

	private int getPixelsOnce(BufferedImage plan, float startx, float starty, float width, float height)
	{
	    // System.out.println(plan.getRGB( (int) startx+1,  (int) starty+1));
	     return Color.gray.getRGB();
		
		//return plan.getRGB( (int) startx+1, (int) starty+1);
	
		
		 
	}
	

	
	public boolean tranformAndCheckImage(BufferedImage world, float startx, float starty, float width, float height,float lookAhead ,int direction, int color) throws Exception
	{
	
		 int yExit= 0;
	    int  xExit=0;
		
		
	
		switch(direction) 
		{
		case ImageHelper.DIRECTION_FORWARD: 
			
			
			if(starty+lookAhead+height>helper.getEnvironment().getRootPlayground().getSize().getHeight() ||startx+width>helper.getEnvironment().getRootPlayground().getSize().getWidth())			{
				//System.out.println("Fehler:"+(starty+lookAhead+height)+ "wäre zu hoch");
				//System.out.println("Fehler:"+(startx+lookAhead+width)+ "wäre zu hoch");
				return false;
			}
			else
			{
				   yExit=(int) Math.floor( starty+lookAhead+height);
			      starty=starty+lookAhead;
			      xExit=(int) Math.floor(startx+width);
			      //System.out.println("StartY:"+starty);
			      //System.out.println("EndY:"+yExit);
			      //System.out.println("StartX:"+startx);
			      //System.out.println("EndX:"+xExit);
			    
			}
		
			//}
			
	
		break;
		case ImageHelper.DIRECTION_BACKWARD:
				
	
			
			 if(starty-lookAhead<0)   //||startx+width>helper.getEnvironment().getRootPlayground().getSize().getWidth())
			 {
				// System.out.println("Backward not possible");
				return false; 
			 }
			//System.out.println("Generat Backward Data");
			 if(counter>60)
			 {
			 //System.out.println("StartY vorher:"+starty);
			 }
			 yExit=(int) Math.floor( starty-lookAhead+height);
			 starty=starty-lookAhead;
			 xExit=(int) Math.floor(startx+width);
			 if(counter>60)
			 {
			//System.out.println("Height:"+height);
			//System.out.println("StartY:"+starty);
			//System.out.println("Y:Exit:"+yExit);
			 }
		
		
		break;
		
		case ImageHelper.DIRECTION_LEFT: 
			
			
				 if(startx-lookAhead<0)
				 {
					 //System.out.println("LEft not possble");
					return false;
				 }
				 xExit= (int) Math.floor(startx-lookAhead+width);
				 startx=startx-lookAhead;
				 yExit= (int) Math.floor(starty+height);
				
				 
				// System.out.println("Generata Left data");
				   
						
			break;
		case ImageHelper.DIRECTION_RIGHT:
		
		
				  if(startx+lookAhead+width>helper.getEnvironment().getRootPlayground().getSize().getWidth())
				  {
					// System.out.println("Right not possible");
					return false;
				  }
				
					//System.out.println("Generate right");
				 
					xExit=(int) Math.floor(startx+lookAhead+width);
				 	startx=startx+lookAhead;
					yExit=(int) Math.floor(starty+height);
					
					
			break;

		case ImageHelper.DIRECTION_UP_LEFT:
			
			 if(startx-lookAhead<0||starty-lookAhead-height<0)
			  {
				// System.out.println("Right not possible");
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
				// System.out.println("Right not possible");
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
				// System.out.println("Right not possible");
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
				// System.out.println("Right not possible");
				return false;
			  }
			
			
			
			xExit= (int) Math.floor(startx+lookAhead+width);
			startx= (int) Math.floor(startx+lookAhead);
			yExit= (int) Math.floor(starty+lookAhead+height);
			starty= (int) Math.floor(starty+lookAhead);
			break;
			
			
		}
		//System.out.println("Word Width:"+ world.getWidth());
		//System.out.println("Word Height:"+ world.getHeight());
		 for(int y=(int) Math.floor(starty);y<=yExit;y++)
		 {
		 for(int x=(int)Math.floor(startx);x<=xExit;x++)
		 {
			
				
				 //add.setRGB(x, y, bug1.getRGB(x, y)+bug2.getRGB(x, y));
			 	//if(this.getPixel(x, y,data)!=null)
			 	///{
			 		// if(world.getRGB(x, y)==this.getPixel(x, y, data).getColor())
			 		// {
			 			//System.out.println("Counter:" + this.counter+ "hat eine Kollosition bei"+ x +"," +y);
			 if(world.getRGB(x, y)==color)
			 {	
				
					 return false;
			 }
	
			 	
		 }
		 }
		 //System.out.println("Return true");
	      return true;
	           
	}
	
	
	
	
	
	public ArrayList<Element> draw_way(String id,float x,float y,float width,float height ,int direction,float lookAhead,String target_id) throws Exception
	{
		ArrayList<Element> elements=new ArrayList<Element>();
		
		
		Document doc=helper.getSVGDoc();
	   
		System.out.println("Parent Node:"+doc.getElementsByTagName("rect").item(0).getParentNode().getNodeName());
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
			
			//for(float start=y;start< y+height+lookAhead&&;start+=height)
			//{
			 //this.paintYDirection(x, start, width, height, generator);
				//this.paintYDirection(x, start, width, height, generator);
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
			//}
			
	
		break;
		case ImageHelper.DIRECTION_BACKWARD:
	
		//for(float start=y+height;start> y-height-lookAhead;start-=height)
		//{
		 //this.paintYDirection(x, start, width, height, generator);
			
			 newElement=this.createNewElement("rect", x, y-lookAhead, null, width, height, id, doc);
			
			 if(y-lookAhead<0)
			 {
				 newElement=this.createNewElement("rect", x, -1.0f, null, width, height, id, doc);	 
			 }
			 node.appendChild(newElement);
			 elements.add(newElement);
		//}
		
		
		
		break;
		
		case ImageHelper.DIRECTION_LEFT: 
			
			//for(float start=x+width;start> x+width-lookAhead;start-=width)
			//(()){
			  
				//this.paintXDirection(y, start, width, height, generator);
				 newElement=this.createNewElement("rect", x-lookAhead, y, null, width, height, id, doc);
				 if(x-lookAhead<0)
				 {
					 newElement=this.createNewElement("rect", -1.0f, y, null, width, height, id, doc);	 
				 }
				 node.appendChild(newElement);
				 elements.add(newElement);
			//}	
			
			
			break;
		case ImageHelper.DIRECTION_RIGHT:
			//for(float start=x;start<x+width-+lookAhead;start+=width)
		//	{
			newElement=this.createNewElement("rect", x+lookAhead, y, null, width, height, id, doc);
				  if(x+lookAhead+width>helper.getEnvironment().getRootPlayground().getSize().getWidth())
				  {
					  newElement=this.createNewElement("rect", -1.0f, y, null, width, height, id, doc);  
				  }
				  	 
				 
				 node.appendChild(newElement);
				 elements.add(newElement);
			//}
			
			break;
		
			//doc.getFirstChild().getFirstChild().appendChild(newElement.getN)
			
			 
		
		}
		  
	    NodeList nodeList=doc.getElementsByTagName("rect");
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
	
	//
       
       // BufferedImage cpy=null;
        boolean flag=false;
        if(this.plan!=null)
        {
       // cpy=this.copyPixel(this.plan);
        flag=true;
        }
        //this.plan=(BufferedImage)svgimage.createBufferedImage();
       // SVGSafe saf=new SVGSafe();
        //saf.write("plan"+counter+".svg", doc);
         //ImageIO.write(this.plan, "jpg", new File("Plan"+counter+".jpg"));  //
         SVGSafe save=new SVGSafe();
        // System.out.println("plan"+counter+"_"+this.endung+".svg");
 		//save.write("plan.svg", doc);
 		//save.writeJPG("plan.svg");
 		
 		this.plan=ImageIO.read(new File("plan.jpg"));
        this.endung++;
       
      

	return elements;
		
	}
	
	 
	  public void drawLineToSave(String id,String x1,String width, String y1,String height,Document doc,boolean colored,String color)
	  {    /*
		    Element e = doc.createElement("line");
		    e.setAttribute("id", id);
			e.setAttribute("x1",x1);
			e.setAttribute("y1",y1);
			e.setAttribute("x2",x1);
			e.setAttribute("y2",y1);
			e.setAttribute("stroke", "black");
			e.setAttribute("stroke-width", "20");
			*/
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
			//	e.setAttribute("stroke-width","1");
			//	e.setAttribute("stroke",color);
				e.setAttribute("fill","none");
				e.setAttribute("stroke", color);
				
			}
			
					
			e.setAttribute("id", "rect");
			e.setAttribute("height", height);
			//String test="g";
			String test=doc.getElementsByTagName("rect").item(0).getParentNode().getNodeName();
		
			Node node=doc.getElementsByTagName("rect").item(0).getParentNode();             //this.findNode(doc.getElementsByTagName(test),test);
			node.appendChild(e);
			
			 
		  
		  
	  }
	  
	

	
	
	
	public synchronized BufferedImage createManipulatedWorld()
	{
		try
		{
		
			  if(!READ_WORLD)
			   {
		Document doc=helper.getSVGDoc();
		//System.out.println(helper.getEnvironment().getRootPlayground().getSize().getWidth()+","+helper.getEnvironment().getRootPlayground().getSize().getHeight());
		
		 EnvironmentWrapper envWrap = new EnvironmentWrapper(helper.getEnvironment());
		 Vector<StaticObject> obstacles=envWrap.getObstacles();
		 String neuFileName="manipulated_enviroment.svg";
	    Size size=helper.getEnvironment().getRootPlayground().getSize();
	 
	 
	    
	    Vector<ActiveObject> agents=envWrap.getAgents();
	    for(ActiveObject obj: agents)
	    {
	    	 
	           	Element element=doc.getElementById(obj.getId());
	           	if(element==null)
	           	{
	           		System.out.println("Element ist null");
	           	}
	           	else
	           	{
	           		System.out.println("Element not null!");
	           	}
	              Node tmp= element.getParentNode();
	              tmp.removeChild(element);

	           	System.out.println("Entfertn");
	    	
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
	    	//System.out.println("Old Val:"+oldVal);
	    	String search="fill:";
	    	int index=oldVal.indexOf(search);
	    	int end;
	    	if(index!=-1)
	    	{
	    	end=oldVal.indexOf(";");
	    	System.out.println("End:"+end);
	    	if(end==-1)
	    	{
	    		oldVal="fill:gray";
	    	}
	    	else
	    	{
	    	oldVal=oldVal.replaceFirst(oldVal.substring(index+search.length(),end), "gray");
	    	}
	    	//System.out.println("Old Val later!"+oldVal);
	    	
	    		    
	    	Attr style=element.getAttributeNode("style");
	    	style.setValue(oldVal);
	    	}
	    
	    	//helper.setSVGDoc(doc);
		  
			
		
			
		
		
	}
		
		
		
	    //transcoder.KEY_QUALITY=TranscodingHints.Key.
	    
	  //  t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(100));
	 
		SVGImage img=new SVGImage( (SVGDocument)doc);
		this.evn=(BufferedImage) img.createBufferedImage();
		 //ImageIO.write(this.evn, "jpg", new File("Orginal"+counter+".jpg"));  //
		SVGSafe save=new SVGSafe();
		save.write("myWorld.svg", doc);
		save.writeJPG("myworld.svg");
	   
		this.evn=ImageIO.read(new File("myWorld.jpg"));
		
		READ_WORLD=true;
		return evn;
			   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return null;
	}

	
}
		
		
	



