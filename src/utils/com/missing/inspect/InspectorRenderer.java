package com.missing.inspect;
/**
 * The InspectorRenderer is an interface for classes which can serve to
 * influence the output of inspector.
 * Consider simple classes like Point or your own, you might not want to have
 * them printed out in a verbose way such as toString does.
 * @author hm
 *
 */
public interface InspectorRenderer {
	/**
	 * true if it can render a certain instance, false if not
	 * @param object
	 * @return
	 */
	abstract boolean fitRenderer(Object object);
	/**
	 * should return a string showing the class as you like, say e.g. Point(0,0).
	 * @param object
	 * @return
	 */
	abstract String render(Object object);
}
