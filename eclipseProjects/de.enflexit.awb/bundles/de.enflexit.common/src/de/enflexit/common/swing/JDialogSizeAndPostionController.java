package de.enflexit.common.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * The class JFrameSizeAndPostionController allows to permanently check the position
 * of a window and to react on situations where the window is not properly displayed
 * (works especially also for {@link JFrame} and {@link JDialog}). 
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JDialogSizeAndPostionController {

	public enum JDialogPosition {
		ParentTopLeft,
		ParentTopRight,
		ParentBottomLeft,
		ParentBottomRight,
		ParentCenter,
		ScreenTopLeft,
		ScreenTopRight,
		ScreenBottomLeft,
		ScreenBottomRight,
		ScreenCenter
	}
	
	private static boolean isDebug = true;
	
	
	// ------------------------------------------------------------------------
	// --- From here, sizing methods that turned out to be not required ------- 
	// ------------------------------------------------------------------------	
	/**
	 * Sets the size of the JDialog according to graphics scale and based 
	 * on the specified dimension (assumed as 100%).
	 *
	 * @param jDialog the JDialog to set the size for
	 * @param dimension the dimension
	 */
	protected static void setJDialogSizeAccordingToGraphicsScale(JDialog jDialog, Dimension dimension) {
		if (jDialog==null || dimension==null) return;
		setJDialogSizeAccordingToGraphicsScale(jDialog, dimension.width, dimension.height);
	}
	/**
	 * Sets the size of the JDialog according to graphics scale and based 
	 * on the specified dimension (assumed as 100%).
	 *
	 * @param jDialog the JDialog to set the size for
	 * @param width the width
	 * @param height the height
	 */
	protected static void setJDialogSizeAccordingToGraphicsScale(JDialog jDialog, int width, int height) {

		if (jDialog==null) return;

		try {
			// --- Get x- and y-scale to multiply with ------------------------
			double scaleX = jDialog.getGraphicsConfiguration().getDefaultTransform().getScaleX();
			double scaleY = jDialog.getGraphicsConfiguration().getDefaultTransform().getScaleY();
			jDialog.setSize((int)(width*scaleX), (int)(height*scaleY));
			
		} catch (Exception ex) {
			ex.printStackTrace();
			// --- As ultima ratio backup solution ----------------------------
			jDialog.setSize(width, height);
		}
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, positioning methods for JDialogs ------------------------ 
	// ------------------------------------------------------------------------	
	/**
	 * Sets the specified JDialog to the specified position on the current screen.
	 *
	 * @param jDialog the JDialog to place
	 * @param position the position indicator
	 */
	public static void setJDialogPositionOnScreen(JDialog jDialog, JDialogPosition position) {

		if (jDialog==null || position==null) return;
		
		try {
			// --- Which location to use? -------------------------------
			switch (position) {
			case ParentTopLeft:
			case ParentTopRight:
			case ParentBottomLeft:
			case ParentBottomRight:
			case ParentCenter:
				setJDialogPositionRelativeToParent(jDialog, position);
				break;
				
			case ScreenTopLeft:
			case ScreenBottomRight:
			case ScreenBottomLeft:
			case ScreenTopRight:
			case ScreenCenter:
				setJDialogPositionRelativeToScreen(jDialog, position);
				break;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns the backup dialog position if no parent is found.
	 *
	 * @param position the position
	 * @return the backup dialog position if no parent is found
	 */
	private static JDialogPosition getBackupDialogPositionIfNoParentWasFound(JDialogPosition position) {
		
		JDialogPosition backupPosition = JDialogPosition.ScreenCenter;
		switch (position) {
		case ParentTopLeft:
			backupPosition = JDialogPosition.ScreenTopLeft;
			break;
		case ParentTopRight:
			backupPosition = JDialogPosition.ScreenTopRight;
			break;
		case ParentBottomLeft:
			backupPosition = JDialogPosition.ScreenBottomLeft;
			break;
		case ParentBottomRight:
			backupPosition = JDialogPosition.ScreenBottomRight;
			break;
		case ParentCenter:
			backupPosition = JDialogPosition.ScreenCenter;
			break;

		default:
			break;
		}
		return backupPosition;
	}
	
	/**
	 * Sets the JDialog position relative to parent and according to the specified position.
	 *
	 * @param jDialog the JDialog to place
	 * @param position the JDialogPosition to apply
	 */
	private static void setJDialogPositionRelativeToParent(JDialog jDialog, JDialogPosition position) {
		
		// --- Check if the JDialog has a parent dialog -----------------------
		Container parentContainer = jDialog.getParent();
		if (parentContainer==null) {
			// --- Get the backup position and set relative to screen ---------
			JDialogPosition backupPosition  = getBackupDialogPositionIfNoParentWasFound(position);
			setJDialogPositionRelativeToScreen(jDialog, backupPosition);
			return;
		}
		
		// --------------------------------------------------------------------
		// --- Continue as intended -------------------------------------------
		// --------------------------------------------------------------------
		
		// --- Get information about current state ----------------------------
		Dimension dialogSize = jDialog.getSize();
		Point parentLocation = parentContainer.getLocation();
		Rectangle parentRectangle = parentContainer.getBounds();
		
		int dialogWidth  = (int) (dialogSize.width);
		int dialogHeight = (int) (dialogSize.height);
		
		int parentWidth  = (int) (parentRectangle.width);
		int parentHeight = (int) (parentRectangle.height);
		
		int newX = parentLocation.x;
		int newY = parentLocation.y;
		
		switch (position) {
		case ParentTopLeft:
			// --- Nothing to do ------
			break;
		case ParentTopRight:
			newX = newX + parentWidth - dialogWidth;
			break;
		case ParentBottomLeft:
			newY = newY + parentHeight - dialogHeight;
			break;
		case ParentBottomRight:
			newX = newX + parentWidth - dialogWidth;
			newY = newY + parentHeight - dialogHeight;
			break;
		case ParentCenter:
			newX = newX + parentWidth/2  - dialogWidth/2;
			newY = newY + parentHeight/2 - dialogHeight/2;
			break;

		default:
			break;
		}
		
		if (isDebug==true) {
			// --- Get Graphics information ---------------------------------------
			GraphicsConfiguration gc = jDialog.getGraphicsConfiguration();
			GraphicsDevice graphicsDevice = gc.getDevice();
			
			Point screenPositionTL = getScreenPosition(graphicsDevice, parentLocation);
			Rectangle screenBounds = graphicsDevice.getDefaultConfiguration().getBounds();
			
			System.out.println("Parent Location: " + getPointAsString(parentLocation) + ", Screen-Location: " + getPointAsString(screenPositionTL) + ", Device Bounds (" + graphicsDevice.getIDstring() + "): " + getSizeAsString(screenBounds));
			System.out.println("Dialog Size: " + getSizeAsString(dialogSize) + ", Parent Size: " + getSizeAsString(parentRectangle) + " new x: " + newX + ", newY: " + newY);
			System.out.println();
		}
		
		
		// --- Finally set the new location -----------------------------------
		jDialog.setLocation(newX, newY);
	}
	/**
	 * Sets the JDialog position relative to the current screen and according to the specified position.
	 *
	 * @param jDialog the JDialog to place
	 * @param position the JDialogPosition to apply
	 */
	private static void setJDialogPositionRelativeToScreen(JDialog jDialog, JDialogPosition position) {
		
//		TODO
		
	}
	
	/**
	 * Returns the screen position for the specified frame position 
	 *
	 * @param framePosition the frame position
	 * @return the screen position
	 */
	private static Point getScreenPosition(GraphicsDevice graphicsDevice, Point framePosition) {
		
		// --- Create screen position based on current frame position ---------
		Point screenPosition = new Point(framePosition);

		// --- Get current device bounds --------------------------------------
		Rectangle deviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
		
		// --- Subtract the x/y position of the device ------------------------
		screenPosition.x -= deviceBounds.x;
		screenPosition.y -= deviceBounds.y;
		return screenPosition;
	}
	
	/**
	 * Returns the screen position for the specified frame position 
	 *
	 * @param screenPosition the frame position
	 * @return the screen position
	 */
	private static Point getDialogPosition(GraphicsDevice graphicsDevice, Point screenPosition) {
		
		// --- Create screen position based on current frame position ---------
		Point dialogLocation = new Point(screenPosition);

		// --- Get current device bounds --------------------------------------
		Rectangle deviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
		
		// --- Subtract the x/y position of the device ------------------------
		dialogLocation.x += deviceBounds.x;
		dialogLocation.y += deviceBounds.y;

		// --- Clip negative values... (???) ----------------------------------
		//if (screenPosition.x < 0) screenPosition.x *= -1;
		//if (screenPosition.y < 0) screenPosition.y *= -1;
		return dialogLocation;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, methods for debug printing ------------------------------ 
	// ------------------------------------------------------------------------
	/**
	 * Returns the specified point as string.
	 * @param point the point
	 * @return the point as string
	 */
	private static String getPointAsString(Point point) {
		return "[" + point.x + ", " + point.y + "]";
	}
	/**
	 * Returns the size of the specified Rectangle as string.
	 * @param rectangle the rectangle
	 * @return the point as string
	 */
	private static String getSizeAsString(Rectangle rectangle) {
		return "[" + rectangle.width + ", " + rectangle.height + "]";
	}
	/**
	 * Returns the size of the specified Dimension as string.
	 * @return the point as string
	 */
	private static String getSizeAsString(Dimension dimension) {
		return "[" + dimension.width + ", " + dimension.height + "]";
	}
	
}
