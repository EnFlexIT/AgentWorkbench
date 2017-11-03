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
package org.agentgui.gui.swt.preferences.pages;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

public class PreferencePageProgramStart extends FieldEditorPreferencePage {
	
	private RadioGroupFieldEditor executionModeFieldEditor;
	private ComboFieldEditor protocolFieldEditor;
	private StringFieldEditor urlFieldEditor;
	private StringFieldEditor jadePortFieldEditor;
	private StringFieldEditor mtpPortFieldEditor;

	public PreferencePageProgramStart() {
		super(GRID);
	}

	@Override
	protected void createFieldEditors() {
		this.addField(this.getExecutionModeFieldEditor());
		this.addField(this.getProtocolFieldEditor());
		this.addField(this.getUrlFieldEditor());
		this.addField(this.getJadePortFieldEditor());
		this.addField(this.getMtpPortFieldEditor());
	}
	
	/**
	 * Gets the execution mode field editor.
	 *
	 * @return the execution mode field editor
	 */
	private RadioGroupFieldEditor getExecutionModeFieldEditor(){
		
		if(this.executionModeFieldEditor == null){
			String preferenceName = "programstart.executionmode";
			String preferenceLabel = "Start Agent Workbench as:";
			String[][] labelsAndValues = {
					{"Application", "runAsApplication"},
					{"Background System (Master/Slave)", "runAsServer"},
					{"Service / Embedded System Agent", "runAsEmbeddedSystemAgent"}};
			
			executionModeFieldEditor = new RadioGroupFieldEditor(preferenceName, preferenceLabel, 1, labelsAndValues, this.getFieldEditorParent());
		}
		
		return executionModeFieldEditor;
	}
	
	private ComboFieldEditor getProtocolFieldEditor(){
		if(this.protocolFieldEditor == null){
			String preferenceName = "programstart.mainServerProtocol";
			String preferenceLabel = "Protocol";
			String[][] entriesAndValues = {{"HTTP", "http"}, {"HTTPS", "https"}};
			
			protocolFieldEditor = new ComboFieldEditor(preferenceName, preferenceLabel, entriesAndValues, getFieldEditorParent());
		}
		return this.protocolFieldEditor;
	}

	private StringFieldEditor getUrlFieldEditor() {
		if(urlFieldEditor == null){
			String preferenceName = "programstart.mainServerUrl";
			String preferenceLabel = "URL / IP";
			
			urlFieldEditor = new StringFieldEditor(preferenceName, preferenceLabel, getFieldEditorParent());
		}
		return urlFieldEditor;
	}

	private StringFieldEditor getJadePortFieldEditor() {
		if(jadePortFieldEditor == null){
			String preferenceName = "programstart.mainServerJadePort";
			String preferenceLabel = "JADE Port";
			
			jadePortFieldEditor = new StringFieldEditor(preferenceName, preferenceLabel, getFieldEditorParent());
		}
		return jadePortFieldEditor;
	}

	private StringFieldEditor getMtpPortFieldEditor() {
		if(mtpPortFieldEditor == null){
			String preferenceName = "programstart.mainServerMtpPort";
			String preferenceLabel = "MTP Port";
			
			mtpPortFieldEditor = new StringFieldEditor(preferenceName, preferenceLabel, getFieldEditorParent());
		}
		return mtpPortFieldEditor;
	}

}
