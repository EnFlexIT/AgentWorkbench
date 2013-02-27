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
package gasmas.compStat;

import gasmas.ontology.CompStat;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassWidget;

/**
 * The Class CompressorStationWidget.
 */
public class CompressorStationWidget extends OntologyClassWidget {

	private static final long serialVersionUID = -8424882669417358903L;

	private CompStat compressorStationModel = null;
	
	/**
	 * Instantiates a new compressor station widget.
	 *
	 * @param dynForm the dyn form
	 * @param startArgIndex the start arg index
	 */
	public CompressorStationWidget(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#setOntologyClassInstance(java.lang.Object)
	 */
	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.compressorStationModel = (CompStat) objectInstance;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.ontologies.gui.OntologyClassEditorJPanel#getOntologyClassInstance()
	 */
	@Override
	public Object getOntologyClassInstance() {
		return this.compressorStationModel;
	}

}
