package org.agentgui.gui.swt.preferences.pages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

public class DummyPage extends FieldEditorPreferencePage{

	public DummyPage() {
		super(GRID);
	}

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor("PATH", "&Directory preference:", getFieldEditorParent()));
		addField(new BooleanFieldEditor("BOOLEAN_VALUE", "&An example of a boolean preference", getFieldEditorParent()));
		addField(new RadioGroupFieldEditor("CHOICE", "An example of a multiple-choice preference", 1, new String[][] { { "&Choice 1", "choice1" },
            { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
		
		addField(new StringFieldEditor("MySTRING1", "A &text preference:",
                getFieldEditorParent()));
        addField(new StringFieldEditor("MySTRING2", "A &text preference:",
                getFieldEditorParent()));
	}

}
