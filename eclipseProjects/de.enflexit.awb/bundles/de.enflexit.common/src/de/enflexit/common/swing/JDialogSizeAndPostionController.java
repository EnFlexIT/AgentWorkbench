package de.enflexit.common.swing;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Window;

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
	
	private static boolean isDebug = false;
	
	
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
			if (isDebug==true) ex.printStackTrace();
			// --- As backup solution -----------------------------------------
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
	 * @param destinationPosition the destination position indicator
	 */
	public static void setJDialogPositionOnScreen(JDialog jDialog, JDialogPosition destinationPosition) {

		// --- Shorten tasks of this method? ----------------------------------
		if (jDialog==null) return;
		if (destinationPosition==null) {
			jDialog.setLocationRelativeTo(null);
			return;
		}
		
		try {
			// --- Which location to use? -------------------------------------
			switch (destinationPosition) {
			case ParentTopLeft:
			case ParentTopRight:
			case ParentBottomLeft:
			case ParentBottomRight:
			case ParentCenter:
				setJDialogPositionRelativeToParent(jDialog, destinationPosition);
				break;
				
			case ScreenTopLeft:
			case ScreenBottomRight:
			case ScreenBottomLeft:
			case ScreenTopRight:
			case ScreenCenter:
				setJDialogPositionRelativeToScreen(jDialog, destinationPosition);
				break;
			}
			
		} catch (Exception ex) {
			if (isDebug==true) ex.printStackTrace();
			// --- As backup solution -----------------------------------------
			jDialog.setLocationRelativeTo(null);
		}
	}
	
	/**
	 * Returns the backup dialog position if no parent is found.
	 *
	 * @param destinationPosition the destination position
	 * @return the backup dialog position if no parent is found
	 */
	private static JDialogPosition getBackupDialogPositionIfNoParentWasFound(JDialogPosition destinationPosition) {
		
		JDialogPosition backupPosition = JDialogPosition.ScreenCenter;
		switch (destinationPosition) {
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
	 * @param destinationPosition the JDialogPosition to apply
	 */
	private static void setJDialogPositionRelativeToParent(JDialog jDialog, JDialogPosition destinationPosition) {
		
		// --- Check if the JDialog has an owner Window -----------------------
		Window ownerWindow = getValidOwnerWindow(jDialog);
		if (ownerWindow==null) {
			// --- Get the backup position and set relative to screen ---------
			JDialogPosition backupPosition  = getBackupDialogPositionIfNoParentWasFound(destinationPosition);
			setJDialogPositionRelativeToScreen(jDialog, backupPosition);
			return;
		}
		
		// --- Get required information ---------------------------------------
		Point parentLocation = ownerWindow.getLocation();
		Rectangle parentRectangle = ownerWindow.getBounds();

		// --- Set position ---------------------------------------------------
		setJDialogPositionRelativeToScreen(jDialog, destinationPosition, parentLocation, parentRectangle);
	}
	
	
	/**
	 * Sets the JDialog position relative to the current screen and according to the specified position.
	 *
	 * @param jDialog the JDialog to place
	 * @param destinationPosition the JDialogPosition to apply
	 */
	private static void setJDialogPositionRelativeToScreen(JDialog jDialog, JDialogPosition destinationPosition) {
		
		// --- Define required information ------------------------------------
		Window ownerWindow = getValidOwnerWindow(jDialog);
		Point parentLocation = null;
		Rectangle parentRectangle = null;
		
		// --- Determine required information ---------------------------------  
		GraphicsDevice graphicsDevice = jDialog.getGraphicsConfiguration().getDevice();
		if (isDebug==true) {
			System.out.println("=> Positioning on Screen " + graphicsDevice.getIDstring());
		}
		
		// --- Do we have an owner window? ------------------------------------
		if (ownerWindow==null) {
			// --- No owner frame => Get pointer information of the mouse ----- 
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			if (pointerInfo!=null) {
				// --- Consider mouse position for the positioning ------------
				graphicsDevice = pointerInfo.getDevice();
				Point mouseLocation = pointerInfo.getLocation();
				Point mouseLocationOnScreen = getScreenPosition(graphicsDevice, mouseLocation);
				parentLocation = new Point(mouseLocation.x - mouseLocationOnScreen.x, mouseLocation.y - mouseLocationOnScreen.y);
			} else {
				parentLocation = getDialogPosition(graphicsDevice, new Point(0,0));
			}
			
		} else {
			// --- Find position based on owner frame -------------------------
			Point ownerLocation = ownerWindow.getLocation();
			Point ownerLocationOnScreen = getScreenPosition(graphicsDevice, ownerLocation);
			parentLocation = new Point(ownerLocation.x - ownerLocationOnScreen.x, ownerLocation.y - ownerLocationOnScreen.y);
			
		}
		
		// --- Define the parent rectangle based on current device ------------
		parentRectangle = graphicsDevice.getDefaultConfiguration().getBounds();
		
		// --- Set position ---------------------------------------------------
		setJDialogPositionRelativeToScreen(jDialog, destinationPosition, parentLocation, parentRectangle);
		
	}
	
	/**
	 * The actual doing method for setting the JDialog position relative to the current screen and according to the specified position.
	 *
	 * @param jDialog the JDialog to place
	 * @param destinationPosition the JDialogPosition to apply
	 * @param parentLocation the parent location
	 * @param parentRectangle the parent rectangle
	 */
	private static void setJDialogPositionRelativeToScreen(JDialog jDialog, JDialogPosition destinationPosition, Point parentLocation, Rectangle parentRectangle) {
		
		Dimension dialogSize = jDialog.getSize();
		int dialogWidth  = dialogSize.width;
		int dialogHeight = dialogSize.height;
		
		int parentWidth  = parentRectangle.width;
		int parentHeight = parentRectangle.height;
		
		int newX = parentLocation.x;
		int newY = parentLocation.y;
		
		switch (destinationPosition) {
		case ParentTopLeft:
		case ScreenTopLeft:
			// --- Nothing to do ------
			break;
		case ParentTopRight:
		case ScreenTopRight:
			newX = newX + parentWidth - dialogWidth;
			break;
		case ParentBottomLeft:
		case ScreenBottomLeft:
			newY = newY + parentHeight - dialogHeight;
			break;
		case ParentBottomRight:
		case ScreenBottomRight:
			newX = newX + parentWidth - dialogWidth;
			newY = newY + parentHeight - dialogHeight;
			break;
		case ParentCenter:
		case ScreenCenter:
			newX = newX + parentWidth/2  - dialogWidth/2;
			newY = newY + parentHeight/2 - dialogHeight/2;
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
	 * Return the valid owner window of the specified JDialog.
	 *
	 * @param jDialog the JDialog
	 * @return the valid owner window or <code>null</code>
	 */
	private static Window getValidOwnerWindow(JDialog jDialog) {
		
		Window ownerWindow = jDialog.getOwner();
		if (ownerWindow==null || (ownerWindow instanceof Frame && ownerWindow.isValid()==false && ownerWindow.isVisible()==false)) {
			// --- Most probably, we have the SwingUtilities.SharedOwnerFrame owner ---------------
			// --- => Possibly check all available windows by using "Window.getWindows()" ?? ------
			return null;
		}
		return ownerWindow;
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
