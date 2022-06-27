package de.enflexit.awb.ws.ui.server;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import agentgui.core.application.Application;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyAttribute;
import de.enflexit.awb.ws.core.JettyConstants;
import de.enflexit.awb.ws.core.SSLJettyConfiguration;
import de.enflexit.common.crypto.KeyStoreType;
import de.enflexit.common.swing.KeyAdapter4Numbers;

/**
 * The Class JTableSettingsServerCellRenderEditor.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JTableSettingsServerCellRenderEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private static final long serialVersionUID = -2718291658390367180L;

	private JTable editorJTable;
	private JettyAttribute<?> editorJettyAttribute;
	
	private JCheckBox jCheckBox;
	private JLabel jLabel;
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		JettyAttribute<?> jettyAttribute = (JettyAttribute<?>) value;
		JettyConstants jettyConstant = jettyAttribute.getJettyConstant();
		
		JComponent comp = null;
		JComponent compColumn1 = (JComponent) table.getCellRenderer(row, 0);
		
		if (jettyConstant!=null) {
			// --- Check for special editor types first ------------- 
			switch (jettyConstant) {
			case SSL_KEYSTORE:
				String keyStoreRelativePath = (String) jettyAttribute.getValue();
				if (keyStoreRelativePath!=null && keyStoreRelativePath.isBlank()==false) {
					// --- File in properties directory -------------
					File keyStoreFile = SSLJettyConfiguration.getKeyStoreFileFromRelativePath(keyStoreRelativePath);
					int cutAt = keyStoreFile.getParentFile().getParentFile().getAbsolutePath().length();
					String keyStorePathShort = "." + keyStoreFile.getAbsolutePath().substring(cutAt);
					comp = this.getJLabel(keyStorePathShort, keyStoreRelativePath);
				}
				break;
				
			case SSL_PASSWORD:
			case SSL_KEYPASSWORD:
				String password = (String)jettyAttribute.getValue();
				if (password!=null && password.isBlank()==false) {
					password = "*************";
				}
				comp = this.getJLabel(password);
				break;
				
			default:
				break;
			}
			
			// --- Check for type in jetty constants ----------------
			if (comp==null) {
				if (jettyConstant.getTypeClass().equals(Boolean.class)) {
					comp = this.getJCheckBox((boolean) jettyAttribute.getValue());
				} else if (jettyConstant.getTypeClass().equals(Integer.class)) {
					comp = this.getJLabel(((Integer) jettyAttribute.getValue()) + "");
				} else if (jettyConstant.getTypeClass().equals(String.class)) {
					comp = this.getJLabel((String) jettyAttribute.getValue());
				}
			}
		}
		
		
		// --- Still no component defined? --------------------------
		if (comp==null) {
			comp = this.getJLabel(jettyAttribute.getValue().toString());
		}

		// --- Set colors -------------------------------------------
		comp.setOpaque(compColumn1.isOpaque());
		comp.setForeground(compColumn1.getForeground());
		comp.setBackground(compColumn1.getBackground());
		if (row!=table.getSelectedRow() && row % 2==0) {
			comp.setBackground(Color.white);
		}
		return comp;
	}
	
	private JLabel getJLabel(String text) {
		return this.getJLabel(text, null);
	}
	private JLabel getJLabel(String text, String toolTipText) {
		if (jLabel==null) {
			jLabel = new JLabel();
		}
		jLabel.setText(text);
		if (toolTipText!=null && toolTipText.isBlank()==false) {
			jLabel.setToolTipText(toolTipText);
		}
		return jLabel;
	}
	
	private JCheckBox getJCheckBox(boolean isSelected) {
		if (jCheckBox==null) {
			jCheckBox = new JCheckBox();
		}
		jCheckBox.setSelected(isSelected);
		return jCheckBox;
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		this.editorJTable = table;
		this.editorJettyAttribute = (JettyAttribute<?>) value;
		JettyConstants jettyConstant = this.editorJettyAttribute.getJettyConstant();
		
		JComponent comp = null;

		// --- Check for special editor types first ----------------- 
		switch (jettyConstant) {
		case SSL_KEYSTORE:
			this.editKeyStoreFile(this.editorJettyAttribute, table.getParent().getParent());
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JTableSettingsServerCellRenderEditor.this.fireEditingStopped();
					JTableSettingsServerCellRenderEditor.this.editorJTable.repaint();
				}
			});
			break;

		case SSL_KEYSTORETYPE:
			comp = this.createEditorJComboBox(this.editorJettyAttribute);
			break;

		case SSL_PASSWORD:
		case SSL_KEYPASSWORD:
			comp = this.createEditorJPasswordFiled((String) this.editorJettyAttribute.getValue());
			break;
		
		default:
			break;
		}
		
		// --- Check for type in jetty constants --------------------
		if (comp==null) {
			if (jettyConstant.getTypeClass().equals(Boolean.class)) {
				comp = this.createEditorJCheckBox((boolean) this.editorJettyAttribute.getValue());
			} else if (jettyConstant.getTypeClass().equals(Integer.class)) {
				comp = this.createEditorJTextFieldInteger(((Integer) this.editorJettyAttribute.getValue()));
			} else if (jettyConstant.getTypeClass().equals(String.class)) {
				comp = this.createEditorJTextFieldString((String) this.editorJettyAttribute.getValue());
			}
			// --- Still no component? ------------------------------
			if (comp==null) {
				comp = this.getJLabel(this.editorJettyAttribute.getValue().toString());
			}
		}

		// --- Set colors -------------------------------------------
		comp.setOpaque(false);
		comp.setForeground(Color.BLACK);
		comp.setBackground(Color.WHITE);
		return comp;

	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.editorJettyAttribute;
	}
	
	/**
	 * Edit the keystore file attribute.
	 *
	 * @param jettyAttribute the jetty attribute
	 * @param parentContainer the parent container
	 */
	private void editKeyStoreFile(JettyAttribute<?> jettyAttribute, Container parentContainer) {
		
		if (jettyAttribute.getJettyConstant()!=JettyConstants.SSL_KEYSTORE) return;
		
		// --- Get properties directory -----------------------------
		String propertiesPath = BundleHelper.getPathProperties();
		File propertiesDir = new File(propertiesPath); 
		
		// --- Get keystore file ------------------------------------
		String keyStoreRelativePath = (String) jettyAttribute.getValue();
		File keyStoreFile = (keyStoreRelativePath==null || keyStoreRelativePath.isBlank()) ? null : SSLJettyConfiguration.getKeyStoreFileFromRelativePath(keyStoreRelativePath); 
		
		// --- Prepare file description -----------------------------
		String[] fileExtenArray = KeyStoreType.getAllFileExtensions();
		String fileDescription  = "Keystore File (*." + String.join("*., ", fileExtenArray) + ")";
		
		JFileChooser jFileChooserKeyStoreFile = new JFileChooser(keyStoreFile);
		jFileChooserKeyStoreFile.setDialogTitle("Please, select the keystore file to use.");
		jFileChooserKeyStoreFile.setFileFilter(new FileNameExtensionFilter(fileDescription, KeyStoreType.getAllFileExtensions()));
		jFileChooserKeyStoreFile.setCurrentDirectory(propertiesDir);

		// --- Let the user select the keystore file ----------------  
		if (jFileChooserKeyStoreFile.showOpenDialog(parentContainer) == JFileChooser.APPROVE_OPTION) {
			// --- Check the selected file --------------------------
			keyStoreFile = jFileChooserKeyStoreFile.getSelectedFile();
			if (keyStoreFile!=null) {
				String title = "Wrong selection of Keystore file!";
				String message = "";
				if (keyStoreFile.exists()==false) {
					// --- Does the file exists? --------------------
					message = "The selected Keystore file does not exists!";
					JOptionPane.showMessageDialog(parentContainer, message, title, JOptionPane.ERROR_MESSAGE);
					
				} else if (keyStoreFile.getParentFile().equals(propertiesDir)==false) {
					// --- File within properties folder? -----------
					message = "The selected Keystore is not located in the " + Application.getApplicationTitle() + " properties directory\n";
					message+= "Please, select the Keystore out of that directory!";
					JOptionPane.showMessageDialog(parentContainer, message, title, JOptionPane.ERROR_MESSAGE);
					
				} else {
					// --- Save keystore file -----------------------
					jettyAttribute.setValue(SSLJettyConfiguration.getKeyStoreRelativePath(keyStoreFile));
					// --- Check the file extension -----------------
					String fileName = keyStoreFile.getName();
					String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
					KeyStoreType keyStoreType = KeyStoreType.getKeyStoreTypeByFileExtension(fileExtension);

					// --- Check current keystore type in table ----- 
					JettyAttribute<?> jaKeyStoreType = this.getJettyAttributeFromTable(JettyConstants.SSL_KEYSTORETYPE);
					if (jaKeyStoreType.getValue()==null || jaKeyStoreType.getValue().equals(keyStoreType.getType())==false) {
						jaKeyStoreType.setValue(keyStoreType.getType());
					}
				}
			}
		}
	}
	
	/**
	 * Searches in the table for the specified JettyAttribute.
	 *
	 * @param jcSearch the jetty constants
	 * @return the jetty attribute
	 */
	private JettyAttribute<?> getJettyAttributeFromTable(JettyConstants jcSearch) {
		if (jcSearch==null) return null;
		for (int i = 0; i < this.editorJTable.getRowCount(); i++) {
			JettyAttribute<?> jaTabel = (JettyAttribute<?>) this.editorJTable.getValueAt(i, 1);
			if (jaTabel.getJettyConstant()==jcSearch) return jaTabel;
		}
		return null;
	}
	
	
	
	private JCheckBox createEditorJCheckBox(boolean isSelected) {
		
		JCheckBox jCheckBox = new JCheckBox();
		jCheckBox.setSelected(isSelected);
		jCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JCheckBox checkBox = (JCheckBox) ae.getSource();
				Boolean newValue = checkBox.isSelected();
				@SuppressWarnings("unchecked")
				JettyAttribute<Boolean> jaBoolean = (JettyAttribute<Boolean>) JTableSettingsServerCellRenderEditor.this.editorJettyAttribute;
				jaBoolean.setValue(newValue);
				fireEditingStopped();
			}
		});
		return jCheckBox;
	}
	
	private JTextField createEditorJTextFieldString(String text) {
		JTextField jTextFieldString = new JTextField();
		jTextFieldString.setText(text);
		jTextFieldString.setBorder(BorderFactory.createEmptyBorder());
		jTextFieldString.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			@Override
			public void insertUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			private void updateJettyAttribute(DocumentEvent de) {
				String newValue = JTableSettingsServerCellRenderEditor.this.getText(de.getDocument());
				@SuppressWarnings("unchecked")
				JettyAttribute<String> jaString = (JettyAttribute<String>) JTableSettingsServerCellRenderEditor.this.editorJettyAttribute;
				jaString.setValue(newValue==null ? null : newValue.trim());
			}
		});
		return jTextFieldString;
	}
	
	private JPasswordField createEditorJPasswordFiled(String password) {
		JPasswordField jPasswordField = new JPasswordField();
		jPasswordField.setText(password);
		jPasswordField.setBorder(BorderFactory.createEmptyBorder());
		jPasswordField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			@Override
			public void insertUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			private void updateJettyAttribute(DocumentEvent de) {
				String newValue = JTableSettingsServerCellRenderEditor.this.getText(de.getDocument());
				@SuppressWarnings("unchecked")
				JettyAttribute<String> jaString = (JettyAttribute<String>) JTableSettingsServerCellRenderEditor.this.editorJettyAttribute;
				jaString.setValue(newValue==null ? null : newValue.trim());
			}
		});
		return jPasswordField;
	}
	
	private JTextField createEditorJTextFieldInteger(Integer number) {
		JTextField jTextFieldInteger = new JTextField();
		jTextFieldInteger.setText(number.toString());
		jTextFieldInteger.setBorder(BorderFactory.createEmptyBorder());
		jTextFieldInteger.addKeyListener(new KeyAdapter4Numbers(false));
		jTextFieldInteger.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			@Override
			public void insertUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
				this.updateJettyAttribute(de);
			}
			private void updateJettyAttribute(DocumentEvent de) {
				String newValueString = JTableSettingsServerCellRenderEditor.this.getText(de.getDocument());
				Integer newValue = 0;
				if (newValueString!=null && newValueString.isEmpty()==false) {
					try {
						newValue = Integer.parseInt(newValueString);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				@SuppressWarnings("unchecked")
				JettyAttribute<Integer> jaString = (JettyAttribute<Integer>) JTableSettingsServerCellRenderEditor.this.editorJettyAttribute;
				jaString.setValue(newValue);
				
			}
		});
		return jTextFieldInteger;
	}
	private String getText(Document doc) {
		String txt;
		try {
			txt = doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			txt = null;
		}
		return txt;
	}

	private JComboBox<String> createEditorJComboBox(JettyAttribute<?> jettyAttribute) {
		
		Object [] optionArray = jettyAttribute.getJettyConstant().getPossibleValues();
		String currentSelection = jettyAttribute.getValue()==null ? jettyAttribute.getJettyConstant().getDefaultValue().toString() : jettyAttribute.getValue().toString(); 
		
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
		for (Object option : optionArray) {
			comboModel.addElement(option.toString());
		}
		
		JComboBox<String> jComboBox = new JComboBox<>(comboModel);
		jComboBox.setSelectedItem(currentSelection);
		jComboBox.setBorder(BorderFactory.createEmptyBorder());
		jComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JComboBox<?> comboBox = (JComboBox<?>) ae.getSource();
				String newValue = (String) comboBox.getSelectedItem();
				@SuppressWarnings("unchecked")
				JettyAttribute<String> jaBoolean = (JettyAttribute<String>) JTableSettingsServerCellRenderEditor.this.editorJettyAttribute;
				jaBoolean.setValue(newValue);
				fireEditingStopped();
			}
		});
		return jComboBox;
	}
	
	
}
