/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.agents;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import javax.swing.JDesktopPane;

import contmas.main.AgentDesktop;
import contmas.main.ContMASContainer;
import contmas.ontology.BayMap;
import contmas.ontology.Domain;
import contmas.ontology.Sea;
import contmas.ontology.Ship;

public class ControlGUIAgent extends GuiAgent{

	private static final long serialVersionUID= -7176620366025244274L;
	private JDesktopPane canvas=null;
	private ControlGUI myGui=null;

	@Override
	protected void onGuiEvent(GuiEvent ev){
		int command=ev.getType();
		if(command == 1){
			AgentContainer c=this.getContainerController();
			try{
				String name=ev.getParameter(0).toString();
				//	            AgentController a = c.createNewAgent(name , "contmas.agents.ShipAgent", null );
				Ship ontologyRepresentation=new Ship();
				Domain habitat=new Sea();
				ontologyRepresentation.setLives_in(habitat);
				if((ev.getParameter(1).toString() != "") && (ev.getParameter(1).toString() != "") && (ev.getParameter(1).toString() != "")){
					BayMap loadBay=new BayMap();
					loadBay.setX_dimension(Integer.parseInt(ev.getParameter(1).toString()));
					loadBay.setY_dimension(Integer.parseInt(ev.getParameter(2).toString()));
					loadBay.setZ_dimension(Integer.parseInt(ev.getParameter(3).toString()));
					ontologyRepresentation.setContains(loadBay);
					ontologyRepresentation.setLength(Float.parseFloat(ev.getParameter(3).toString()));
				}
				//				habitat.setLies_in(terminalArea);
				AgentController a=c.acceptNewAgent(name,new ShipAgent(ontologyRepresentation));

				a.start();
			}catch(Exception e){
			}
		}else if(command == -1){
			this.doDelete();
			//System.exit(0);
		}

	}

	@Override
	protected void setup(){
		// Instanciate the gui

		/*
		Object[] args=this.getArguments();
		if((args != null) && (args[0] instanceof JDesktopPane)){
			this.canvas=(JDesktopPane) args[0];
		}
		if(this.canvas == null){
			ContMASContainer CMC=new ContMASContainer();
			CMC.setVisible(true);
			this.canvas=CMC.getJDesktopPane();
		}*/
		
		AgentDesktop ad = new AgentDesktop(); 
		this.canvas = ad.getDesktopPane();
		this.myGui=new ControlGUI(this);
		this.myGui.setVisible(true);

		this.canvas.add(this.myGui);

		AgentContainer c=this.getContainerController();
		try{
			AgentController a=c.createNewAgent("RandomGenerator","contmas.agents.RandomGeneratorAgent",null);
			a.start();
			a=c.createNewAgent("HarborMaster","contmas.agents.HarborMasterAgent",null);
			a.start();
		}catch(Exception e){

		}
	}

	@Override
	protected void takeDown(){
		this.myGui.dispose();
	}

}