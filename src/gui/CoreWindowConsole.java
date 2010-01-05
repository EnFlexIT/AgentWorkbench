package gui;

import java.awt.Color;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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

public class CoreWindowConsole extends JEditorPane implements Runnable {
	
	private static final long serialVersionUID = 1L;

	private Thread reader;
	private Thread reader2;
	private boolean quit;
					
	private final PipedInputStream pin  = new PipedInputStream(); 
	private final PipedInputStream pin2 = new PipedInputStream(); 

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
		this.setBackground(new Color(255, 255, 255));
		this.setToolTipText( Language.translate("Konsole: kann ein- bzw. ausgeblendet werden") );
		this.setEditable(false);
				
		// ------------------------------------------------
		// --- In Case, that the Global Variable   -------- 
		// --- 'LocalAppUseInternalConsole'        --------
		// --- in Class GlobalInfo is set to FALSE --------
		// ------------------------------------------------		
		if ( Application.RunInfo.AppUseInternalConsole() == false ) {
			String Msg = Language.translate( "Die Konsolenausgabe erfolgt nicht über die Anwendung") + " " + Application.RunInfo.AppTitel()  ;
			this.appendText( Msg );
			return;
		}
		// ------------------------------------------------
		
		// --- Out-Ausgaben abfangen ----------------------
		try	{
			PipedOutputStream pout = new PipedOutputStream(this.pin);
			System.setOut(new PrintStream(pout,true)); 
		} 
		catch (java.io.IOException io) {
			appendText( "Couldn't redirect STDOUT to this console\n"+io.getMessage() );
		}
		catch (SecurityException se) {
			appendText( "Couldn't redirect STDOUT to this console\n"+se.getMessage() );
	    } 

		// --- Err-Ausgaben abfangen ----------------------		
		try {
			PipedOutputStream pout2 = new PipedOutputStream(this.pin2);
			System.setErr(new PrintStream(pout2,true));
		} 
		catch (java.io.IOException io) {
			appendText( "Couldn't redirect STDERR to this console\n"+io.getMessage() );
		}
		catch (SecurityException se) {
			appendText( "Couldn't redirect STDERR to this console\n"+se.getMessage() );
	    } 		
			
		quit = false; // signals the Threads that they should exit
				
		// --- Starting two seperate threads to read from the PipedInputStreams				
		reader = new Thread(this);
		reader.setDaemon(true);	
		reader.start();	
		//
		reader2 = new Thread(this);	
		reader2.setDaemon(true);	
		reader2.start();		
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
	
	
	public synchronized void run() {
		try	{			
			while (Thread.currentThread()==reader) {
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin.available()!=0) {
					String input = this.readLine(pin);
					appendText(input);
				}
				if (quit) return;
			}
		
			while (Thread.currentThread()==reader2)	{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin2.available()!=0) {
					String input = this.readLine(pin2);
					appendText(input, true);				}
				if (quit) return;
			}			
		} catch (Exception e) {
			appendText( "\nConsole reports an Internal error.");
			appendText( "The error is: "+e);			
		}		
	}
	
	public synchronized String readLine(PipedInputStream in) throws IOException	{
		String input="";
		do {
			int available=in.available();
			if (available==0) break;
			byte b[]=new byte[available];
			in.read(b);
			input=input+new String(b,0,b.length);														
		}
		while( !input.endsWith("\n") &&  !input.endsWith("\r\n") && !quit);
		return input;
	}	
			
}