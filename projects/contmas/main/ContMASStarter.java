/**
 * @author Hanno - Felix Wagner, 02.03.2010 Copyright 2010 Hanno - Felix Wagner
 *         This file is part of ContMAS. ContMAS is free software: you can
 *         redistribute it and/or modify it under the terms of the GNU Lesser
 *         General Public License as published by the Free Software Foundation,
 *         either version 3 of the License, or (at your option) any later
 *         version. ContMAS is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         Lesser General Public License for more details. You should have
 *         received a copy of the GNU Lesser General Public License along with
 *         ContMAS. If not, see <http://www.gnu.org/licenses/>.
 */

package contmas.main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import contmas.agents.ControlGUIAgent;

public class ContMASStarter{

	/**
	 * @param args
	 */
	public static void main(String[] args){

		Runtime JADERuntime=Runtime.instance();

		String ServiceList=new String();
		ServiceList=ServiceList.concat("jade.core.event.NotificationService;");
		ServiceList=ServiceList.concat("jade.core.messaging.TopicManagementService;");

		Profile mainContainerProfile=new ProfileImpl();
		mainContainerProfile.setParameter(Profile.SERVICES,ServiceList);

		AgentContainer c=JADERuntime.createMainContainer(mainContainerProfile);

		try{
			AgentController a=c.acceptNewAgent("rma",new rma());
			a.start();

			a=c.acceptNewAgent("ControlGUIAgent",new ControlGUIAgent());
			a.start();
		}catch(StaleProxyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
