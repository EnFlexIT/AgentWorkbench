package gui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import application.Application;
import application.Language;

public class CoreWindowConsole extends JEditorPane  {

		class TextAreaOutputStream extends OutputStream{
			private CoreWindowConsole textArea;
			private Boolean error=false;

			TextAreaOutputStream(CoreWindowConsole textArea, Boolean error){
				super();
				this.error=error;
				this.textArea=textArea;
			}

			@Override
			public void write(byte[] b,int off,int len) throws IOException{
				this.textArea.appendText(new String(b,off,len),error);
			}

			@Override
			public void write(byte[] b) throws IOException{
				this.write(b,0,b.length);
			}

			@Override
			public void write(int b) throws IOException{
				this.write(new byte[] {(byte) b});
			}
		}

	private static final long serialVersionUID = 1L;

	private MutableAttributeSet black;
	private MutableAttributeSet red;
	private AttributeSet attribute;
	
	public CoreWindowConsole() {
		
		// --- Ausgabefarben definieren ---
		black = new SimpleAttributeSet();
	    StyleConstants.setForeground(black, Color.black);
	    StyleConstants.setFontFamily(black, "Courier");
	    StyleConstants.setFontSize(black, 11);
	    red = new SimpleAttributeSet();
	    StyleConstants.setForeground(red, Color.red);
	    StyleConstants.setFontFamily(red, "Courier");
	    StyleConstants.setFontSize(red, 11);
	    
	    this.setContentType("text/html");
	    this.setFont(new Font("Dialog", Font.PLAIN, 11));
		this.setBackground(new Color(255, 255, 255));
		this.setToolTipText( Language.translate("Konsole: kann ein- bzw. ausgeblendet werden") );
		this.setEditable(false);
		
		// ------------------------------------------------
		// --- In Case, that the Global Variable --------
		// --- 'LocalAppUseInternalConsole' --------
		// --- in Class GlobalInfo is set to FALSE --------
		// ------------------------------------------------
		if (Application.RunInfo.AppUseInternalConsole() == false) {
			String Msg = Language.translate("Die Konsolenausgabe erfolgt nicht über die Anwendung") + " " + Application.RunInfo.AppTitel();
			this.appendText(Msg);
			return;
		}
		// ------------------------------------------------

		// --- Out-Ausgaben abfangen ----------------------
		System.setOut(new PrintStream(new TextAreaOutputStream(this,false)));
		System.setErr(new PrintStream(new TextAreaOutputStream(this,true)));
	}
	
	private void appendText(String Text, boolean Error) {
		// --- Fügt aktuellen Meldungen in der Konsole an --- 
	    if (Error == false ) {
	    	attribute = black;
		} else {
			attribute = red;
		}			
		Document doc = this.getDocument();
		try {
			doc.insertString(doc.getLength(), Text, attribute);
			this.setCaretPosition(doc.getLength());
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
	private void appendText(String Text) {
		appendText(Text, false);
	}
}