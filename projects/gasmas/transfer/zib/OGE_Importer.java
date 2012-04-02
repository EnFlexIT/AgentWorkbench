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
package gasmas.transfer.zib;

import gasmas.transfer.zib.net.GasNetwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.controller.NetworkModelFileImporter;
import agentgui.envModel.graph.networkModel.NetworkModel;

public class OGE_Importer extends NetworkModelFileImporter {

    
	/**
	 * Instantiates a new OGE / ZIB GraphFileImporter.
	 * @param generalGraphSettings4MAS the GeneralGraphSettings4MAS of the current project
	 */
	public OGE_Importer(GeneralGraphSettings4MAS generalGraphSettings4MAS, String fileTypeExtension, String fileTypeDescription) {
		super(generalGraphSettings4MAS, fileTypeExtension, fileTypeDescription);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.GraphFileImporter#importGraphFromFile(java.io.File)
	 */
	@Override
	public NetworkModel importGraphFromFile(File graphFile) {
		
		GasNetwork gasNetwork=null;
		try {
			JAXBContext context = JAXBContext.newInstance("gasmas.transfer.zib.net");
			Unmarshaller unmarshaller = context.createUnmarshaller(); 
			FileInputStream fileInputStream = new FileInputStream(graphFile);
			gasNetwork = (GasNetwork)unmarshaller.unmarshal(fileInputStream);
			fileInputStream.close();
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		System.out.println(gasNetwork.getValue().getInformation().getTitle());
		
		
		
		return null;
	}

	
}
