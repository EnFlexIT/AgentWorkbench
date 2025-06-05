package de.enflexit.common.swing.markdown;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * The Class MarkdownEditViewer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MarkdownEditViewer extends JTabbedPane implements ChangeListener {

	private static final long serialVersionUID = -2718347234886103071L;

	private MarkdownRenderer markdownRenderer;
	private MarkdownEditor markdownEditor;

	private AttributeProvider attributeProvider;
	
	/**
	 * Instantiates a new markdown edit viewer.
	 */
	public MarkdownEditViewer() {
		this(null);
	}
	/**
	 * Instantiates a new markdown edit viewer.
	 * @param attributeProvider the attribute provider
	 */
	public MarkdownEditViewer(AttributeProvider attributeProvider) {
		this.setAttributeProvider(attributeProvider);
		this.initialize();
	}
	
	/**
	 * Initializes the markdown view.
	 */
	private void initialize() {

		this.setTabPlacement(BOTTOM);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setFont(new Font("Dialog", Font.BOLD, 12));
		
		this.addTab(" View ", this.getMarkdownRenderer());
		this.addTab(" Edit (Markdown) ", this.getMarkdownEditor());
		this.addChangeListener(this);
		
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent ce) {
		
		if (this.getSelectedIndex()==0) {
			// --- Switched to HTML view ------------------
			String markdownText = this.getMarkdownEditor().getText();
			String htmlText = this.toHTML(markdownText);
			this.getMarkdownRenderer().setText(htmlText);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isFocusOwner()
	 */
	@Override
	public boolean isFocusOwner() {
		return this.getMarkdownEditor().isFocusOwner();
	}
	
	/**
	 * Gets the markdown renderer.
	 * @return the markdown renderer
	 */
	private MarkdownRenderer getMarkdownRenderer() {
		if (markdownRenderer==null) {
			markdownRenderer = new MarkdownRenderer();
		}
		return markdownRenderer;
	}
	/**
	 * Gets the markdown editor.
	 * @return the markdown editor
	 */
	private MarkdownEditor getMarkdownEditor() {
		if (markdownEditor==null) {
			markdownEditor = new MarkdownEditor();
		}
		return markdownEditor;
	}
	
	/**
	 * Gets the document.
	 *
	 * @return the document
	 */
	public Document getDocument() {
		return this.getMarkdownEditor().getDocument();
	}
	
	/**
	 * Returns the current text.
	 * @return the text
	 */
	public String getText() {
		return this.getMarkdownEditor().getText();
	}
	/**
	 * Sets the text.
	 * @param markdownText the new markDownText to display
	 */
	public void setText(String markdownText) {
		this.getMarkdownRenderer().setText(this.toHTML(markdownText));
		this.getMarkdownEditor().setText(markdownText);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		super.setFont(new Font("Dialog", Font.BOLD, 12));
		this.getMarkdownRenderer().setFont(font);
		this.getMarkdownEditor().setFont(font);
	}
	
	/**
	 * To HTML.
	 *
	 * @param markdownText the markdown text
	 * @return the string
	 */
	private String toHTML(String markdownText) {
		
		if (markdownText==null) return null;
		
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdownText);
		
		HtmlRenderer renderer = null;
		if (this.getAttributeProvider()==null) {
			renderer = HtmlRenderer.builder().build();
		} else {
			renderer = HtmlRenderer.builder().attributeProviderFactory(new AttributeProviderFactory() {
				public AttributeProvider create(AttributeProviderContext context) {
					return MarkdownEditViewer.this.getAttributeProvider();
				}
			}).build();
		}
		return renderer.render(document); 
	}

	/**
	 * Sets a attribute provider for rendering mardown to HTML.
	 * @param attributeProvider the new attribute provider
	 */
	public void setAttributeProvider(AttributeProvider attributeProvider) {
		this.attributeProvider = attributeProvider;
	}
	/**
	 * Return the specified attribute provider.
	 * @return the attribute provider
	 */
	public AttributeProvider getAttributeProvider() {
		return attributeProvider;
	}
	
}
