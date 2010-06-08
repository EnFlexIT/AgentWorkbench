/**
 * @author Hanno - Felix Wagner, 21.05.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.main;

import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTree;

import contmas.agents.ContainerAgent;
import contmas.de.unidue.stud.sehawagn.contmas.config.Constants;
import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class EnvironmentHelper{
	public static Phy_Size getPhySizeContainer(){
		Phy_Size phyDimensions=new Phy_Size();
		phyDimensions.setPhy_width(Constants.CONTAINER_LENGTH_40FT);
		phyDimensions.setPhy_height(Constants.CONTAINER_WIDTH);
		return phyDimensions;
	}

	public static Phy_Size getPhySpacerStraddle(){
		Phy_Size phyDimensions=new Phy_Size();
		phyDimensions.setPhy_width(Constants.LENGTH_SPACING_STRADDLE);
		phyDimensions.setPhy_height(Constants.WIDTH_SPACING_STRADDLE);
		return phyDimensions;
	}

	public static Phy_Size getPhySpacerBlock(){
		Phy_Size phyDimensions=new Phy_Size();
		phyDimensions.setPhy_width(Constants.LENGTH_SPACING_BLOCK);
		phyDimensions.setPhy_height(Constants.WIDTH_SPACING_BLOCK);
		return phyDimensions;
	}

	public static Phy_Position getDisplayPositionBlock(BlockAddress address){
		Phy_Position phyPosition=new Phy_Position();
		Phy_Size contSize=getPhySizeContainer();
		Phy_Size spacer=getPhySpacerBlock();

//		System.out.println("BlockAddress="+blockAddressToString(address)+"");

		phyPosition.setPhy_x(((address.getX_dimension()) * contSize.getPhy_width()) + ((address.getX_dimension()) + 1 * spacer.getPhy_width()));
		phyPosition.setPhy_y(((address.getY_dimension()) * contSize.getPhy_height()) + ((address.getY_dimension()) + 1 * spacer.getPhy_height()));

//		System.out.println("getDisplayPositionBlock "+positionToString(phyPosition));

		return phyPosition;
	}

	public static Phy_Position addPositions(Phy_Position a,Phy_Position b){
		Phy_Position added=new Phy_Position();

		if(a == null){
			System.out.println("a null");
		}else if(b == null){
			System.out.println("b null");
		}else{

			added.setPhy_x(a.getPhy_x() + b.getPhy_x());
			added.setPhy_y(a.getPhy_y() + b.getPhy_y());
		}
		return added;
	}

	public static String blockAddressToString(BlockAddress in){
		String out="";
		if(in != null){
			out+="x=";
			out+=in.getX_dimension();
			out+="; y=";
			out+=in.getY_dimension();
			out+="; z=";
			out+=in.getZ_dimension();
		}else{
			out+="[NULL!]";
		}
		return out;
	}

	public static String positionToString(Phy_Position in){
		String out="";
		if(in != null){
			out+="x=";
			out+=in.getPhy_x();
			out+="; y=";
			out+=in.getPhy_y();
		}else{
			out+="[NULL!]";
		}
		return out;
	}

	public static Phy_Position getZeroPosition(){
		Phy_Position zero=new Phy_Position();
		zero.setPhy_x(0.0F);
		zero.setPhy_y(0.0F);
		return zero;
	}

	/*
		 * Assumes, that subDomain lies_in containingDomain or containingDomain has_subdomain subDomain transitively
		 */
	public static Phy_Position getPositionRelativeTo(Phy_Position positionInSubDomain,Domain subDomain,Domain containingDomain){
		if(subDomain.getId().equals(containingDomain.getId())){
			return positionInSubDomain;
			//			return getZeroPosition();
		}

		//TODO so SOMETHING with containingDomain. Some kind of check or so.
		Phy_Position a=positionInSubDomain;
		Phy_Position b=subDomain.getIs_in_position();
		Phy_Position insidePosition=addPositions(a,b);

		return insidePosition;
	}

	public static Domain findRootDomain(Domain accessPoint){
		Domain liesIn=accessPoint.getLies_in();
		if(liesIn == null){
			return accessPoint;
		}else{
			return findRootDomain(liesIn);
		}
	}

	public static void printTOInfo(TransportOrder call,ContainerAgent a){

		//		echoStatus("Starts at: " + call.getStarts_at().getAbstract_designation());
		//		echoStatus("Ends at: " + call.getEnds_at().getAbstract_designation());

		Phy_Size startSize=call.getStarts_at().getAbstract_designation().getHas_size();
		Phy_Position startPos=call.getStarts_at().getAbstract_designation().getIs_in_position();

		Phy_Size endSize=call.getEnds_at().getAbstract_designation().getHas_size();
		Phy_Position endPos=call.getEnds_at().getAbstract_designation().getIs_in_position();

		String startSizeStr="";
		String startPosStr="";
		String endSizeStr="";
		String endPosStr="";

		try{
			startSizeStr="width=" + startSize.getPhy_width() + ", height:" + startSize.getPhy_height();
			startPosStr=positionToString(startPos);
		}catch(NullPointerException e){
			a.echoStatus("Bad start: size=" + startSize + "; pos=:" + startPos);
		}

		try{
			endSizeStr="width=" + endSize.getPhy_width() + ", height:" + endSize.getPhy_height();
			endPosStr=positionToString(endPos);
		}catch(NullPointerException e){
			a.echoStatus("Bad end: size=" + endSize + "; pos=:" + endPos);
		}

		a.echoStatus("startSize: " + startSizeStr + "; startPos:" + startPosStr + "; endSize:" + endSizeStr + "; endPos:" + endPosStr);

	}

	public static Phy_Position getManhattanPosition(Phy_Position from,Phy_Position to,Float dtg){
		Float distance=EnvironmentHelper.getManhattanDistance(from,to) - dtg;
		Phy_Position interpolatedPosition=new Phy_Position();
		Float deltaX=to.getPhy_x() - from.getPhy_x();
		Float deltaY=to.getPhy_y() - from.getPhy_y();
		Float calcX;
		Float calcY;
		if(distance <= Math.abs(deltaX)){
			calcX=distance;
			calcY=0F;
		}else{
			calcX=Math.abs(deltaX);
			calcY=distance - Math.abs(deltaX);
		}

		if(deltaX < 0){
			calcX= -calcX;
		}
		if(deltaY < 0){
			calcY= -calcY;
		}
		interpolatedPosition.setPhy_x(from.getPhy_x() + calcX);
		interpolatedPosition.setPhy_y(from.getPhy_y() + calcY);

		//		echoStatus("interpolated position "+positionToString(interpolatedPosition));

		return interpolatedPosition;
	}

	public static Float getManhattanDistance(Phy_Position from,Phy_Position to){
		Float deltaX=from.getPhy_x() - to.getPhy_x();
		Float deltaY=from.getPhy_y() - to.getPhy_y();

		return Math.abs(deltaX) + Math.abs(deltaY);
	}

	public static Phy_Position getManhattanTurningPoint(Phy_Position curPos,Phy_Position to){
		//		Float deltaY=Math.abs(Math.abs(to.getPhy_y()) - Math.abs(curPos.getPhy_y()));
		Phy_Position turningPoint=new Phy_Position();

		turningPoint.setPhy_x(curPos.getPhy_x());
		turningPoint.setPhy_y(to.getPhy_y());
		//		turningPoint=getManhattanPosition(curPos,to,deltaY);

		return turningPoint;
	}

	public static Domain findClosestCommonDomain(Domain a,Domain b){

		return a; //TODO algorithm
	}

	public static JTree expandTree(JTree inputTree){
		for(Integer i=0;i < inputTree.getRowCount();i++){
			inputTree.expandRow(i);
		}
		return inputTree;
	}

	public static HashMap<Vector<Integer>, String> convertLoading(BayMap map,List tocStateList){
		HashMap<Vector<Integer>, String> loadMap=new HashMap<Vector<Integer>, String>();
		for(Iterator iter=tocStateList.iterator();iter.hasNext();){
			TOCHasState tocState=(TOCHasState) iter.next();
			String bicCode=tocState.getSubjected_toc().getTransports().getBic_code();
			TransportOrderChainState state=tocState.getState();
			if(state instanceof Holding){
				BlockAddress address=state.getAt_address();
				Vector<Integer> addressVec=new Vector<Integer>();
				addressVec.add(address.getX_dimension());
				addressVec.add(address.getY_dimension());
				addressVec.add(address.getZ_dimension());
				loadMap.put(addressVec,bicCode);
			}
		}
		return loadMap;
	}
}
