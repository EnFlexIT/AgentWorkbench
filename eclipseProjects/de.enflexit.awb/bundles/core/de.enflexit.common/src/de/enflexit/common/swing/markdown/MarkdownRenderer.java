/*
 * 
 */
package de.enflexit.common.swing.markdown;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The Class MarkdownRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MarkdownRenderer extends JPanel implements HyperlinkListener {

	private static final long serialVersionUID = -9112277424657199162L;

	private JScrollPane jScrollPane;
	private JEditorPane jEditorPaneHTMLViewer;
	
	/**
	 * Instantiates a new markdown renderer.
	 */
	public MarkdownRenderer() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(this.getJScrollPane());
	}
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(this.getJEditorPaneHTMLViewer());
		}
		return jScrollPane;
	}
	private JEditorPane getJEditorPaneHTMLViewer() {
		if (jEditorPaneHTMLViewer==null) {
			jEditorPaneHTMLViewer = new JEditorPane();
			jEditorPaneHTMLViewer.setEditable(false);
			jEditorPaneHTMLViewer.setContentType("text/html");
			jEditorPaneHTMLViewer.setFont(new Font("Dialog", Font.PLAIN, 12));
			jEditorPaneHTMLViewer.addHyperlinkListener(this);
		}
		return jEditorPaneHTMLViewer;
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent hlEv) {
		
		if (hlEv.getEventType() != HyperlinkEvent.EventType.ACTIVATED) return;
		
		JEditorPane srcPane = (JEditorPane) hlEv.getSource();
		if (hlEv instanceof HTMLFrameHyperlinkEvent) {
			HTMLDocument doc = (HTMLDocument) srcPane.getDocument();
			doc.processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) hlEv);
		} else {
			String url = hlEv.getURL().toString();
			this.browseURI(url);
		}
		
	}
	
	/**
	 * Will try to browse to the URI specified by the string (e.g. https://www.enflex.it).
	 * @param uri the URI as String
	 */
	private void browseURI(String uri) {
		try {
			URI linkURI = new URI(uri);
			Desktop.getDesktop().browse(linkURI);
		} catch (URISyntaxException | IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Can be used to set the text to be displayed.
	 * @param htmlTextToDiisplay the new text
	 */
	public void setText(String htmlTextToDiisplay) {
		this.getJEditorPaneHTMLViewer().setText(htmlTextToDiisplay);
		this.getJEditorPaneHTMLViewer().setCaretPosition(0);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.getJEditorPaneHTMLViewer().setFont(font);
	}
	
}
