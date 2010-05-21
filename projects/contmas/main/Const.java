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

import contmas.ontology.BlockAddress;
import contmas.ontology.Phy_Position;
import contmas.ontology.Phy_Size;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class Const{
	public static Float CONTAINER_WIDTH=2.438F; //8ft
	public static Float CONTAINER_HEIGHT=2.591F; // 8ft 6inch
	public static Float CONTAINER_HEIGHT_HC=2.900F; // 9ft 6inch
	public static Float CONTAINER_LENGTH_20FT=6.058F; //20ft
	public static Float CONTAINER_LENGTH_40FT=12.192F; //40ft

	public static Float LENGTH_SPACING_STRADDLE=0.0F;
	public static Float LENGTH_SPACING_BLOCK=0.0F;
	public static Float WIDTH_SPACING_STRADDLE=CONTAINER_WIDTH;
	public static Float WIDTH_SPACING_BLOCK=0.0F;

	public static Phy_Size getPhySizeContainer(){
		Phy_Size phyDimensions=new Phy_Size();
		phyDimensions.setPhy_width(CONTAINER_LENGTH_40FT);
		phyDimensions.setPhy_height(CONTAINER_WIDTH);
		return phyDimensions;
	}

	public static Phy_Size getPhySpacerStraddle(){
		Phy_Size phyDimensions=new Phy_Size();
		phyDimensions.setPhy_width(LENGTH_SPACING_STRADDLE);
		phyDimensions.setPhy_height(WIDTH_SPACING_STRADDLE);
		return phyDimensions;
	}

	public static Phy_Size getPhySpacerBlock(){
		Phy_Size phyDimensions=new Phy_Size();
		phyDimensions.setPhy_width(LENGTH_SPACING_BLOCK);
		phyDimensions.setPhy_height(WIDTH_SPACING_BLOCK);
		return phyDimensions;
	}

	public static Phy_Position getDisplayPositionBlock(BlockAddress address){
		Phy_Position phyPosition=new Phy_Position();
		Phy_Size contSize=getPhySizeContainer();
		Phy_Size spacer=getPhySpacerBlock();

//		System.out.println("BlockAddress="+blockAddressToString(address)+"");

		
		phyPosition.setPhy_x(((address.getX_dimension()) * contSize.getPhy_width()) + (address.getX_dimension() * spacer.getPhy_width()));
		phyPosition.setPhy_y(((address.getY_dimension()) * contSize.getPhy_height()) + (address.getY_dimension() * spacer.getPhy_height()));
		
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
		if(in!=null){
		out+="x=";
		out+=in.getX_dimension();
		out+="; y=";
		out+=in.getY_dimension();
		out+="; z=";
		out+=in.getZ_dimension();
		} else{
			out+="[NULL!]";
		}
		return out;
	}
	
	public static String positionToString(Phy_Position in){
		String out="";
		if(in!=null){
		out+="x=";
		out+=in.getPhy_x();
		out+="; y=";
		out+=in.getPhy_y();
		} else{
			out+="[NULL!]";
		}
		return out;
	}
}
