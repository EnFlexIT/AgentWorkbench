package de.enflexit.common.swing;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The class JFrameSizeAndPostionController allows to permanently check the position
 * of a window and to react on situations where the window is not properly displayed
 * (works especially also for {@link JFrame} and {@link JDialog}). 
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JFrameSizeAndPostionController extends ComponentAdapter {

	private final int delayOfTimer = 300;
	private final int badPostionDistance = 10; 
	
	private boolean isDebug = true;
	
	private JFrame frameObserved;
	
	private Timer timer;
	private String graphicsDeviceID;
	
	/**
	 * Will initialize a new JFrameSizeAndPostionController.
	 * @param frameObserved the actual frame to observe
	 */
	public JFrameSizeAndPostionController(JFrame frameObserved) {
		if (frameObserved==null) {
			throw new NullPointerException("No JFrame was specified for the " + this.getClass().getSimpleName() + "!");
		}
		// --- Set local instances ------------------------
		this.frameObserved = frameObserved;
		this.frameObserved.addComponentListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent ce) {
		this.getTimer().restart();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentAdapter#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent ce) {
		this.getTimer().restart();
	}
	/**
	 * Returns the local swing {@link Timer}
	 * @return the timer
	 */
	private Timer getTimer() {
		if (timer==null) {
			timer = new Timer(this.delayOfTimer, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFrameSizeAndPostionController.this.checkWindowSizeAndPosition();
				}
			});
			timer.setRepeats(false);
		}
		return timer;
	}
	
	/**
	 * Can be used to set the current ID of the {@link GraphicsDevice} used. If the value is different
	 * to the previous settings, the method will return <code>true</code>; <code>false</code> otherwise. 
	 *
	 * @param graphicsDeviceID the graphics device ID
	 * @return true, if sets the graphics device ID
	 */
	private boolean setGraphicsDeviceID(String graphicsDeviceID) {
		if (this.graphicsDeviceID==null || this.graphicsDeviceID.contentEquals(graphicsDeviceID)==false) {
			this.graphicsDeviceID = graphicsDeviceID;
			return true;
		}
		return false;
	}
	/**
	 * Return the current ID of the {@link GraphicsDevice}	
	 * @return the current GraphicsGevice ID
	 */
	private String getGraphicsDeviceID() {
		return graphicsDeviceID;
	}
	
	/**
	 * Will be invoked to check the size and position of the local frame;  
	 */
	private void checkWindowSizeAndPosition() {

		// --- If frame is not visible, just return here ----------------------
		if (this.frameObserved.isVisible()==false) return;

		// --- Local debug area ----------------------------------------------- 
		if (this.isDebug==true) {
			Point screenPositionTL = this.getScreenPosition(this.getFramePositionTopLeft());
			Point screenPositionBR = this.getScreenPosition(this.getFramePositionBottomRight());
			Rectangle screenBounds = this.getDeviceBounds(this.getGraphicsDevice());

			System.out.println("[" + this.getClass().getSimpleName() + "] Location: " + this.getPointAsString(this.getFramePositionTopLeft()) + ", Screen-TL " + this.getPointAsString(screenPositionTL) + ", Screen-BR " + this.getPointAsString(screenPositionBR) + ", Device Bounds (" + this.getGraphicsDeviceID() + "): " + this.getSizeAsString(screenBounds));
		}

		// --- Check the current GraphicsDevice used -------------------------- 
		@SuppressWarnings("unused")
		boolean isChangedScreen = this.setGraphicsDeviceID(this.getGraphicsDevice().getIDstring());
		
		int xMovement = this.getCorrectionXMovement();
		int yMovement = this.getCorrectionYMovement();
		Dimension newSize = this.getCorrectionSize();

		if (yMovement>0 || newSize!=null) {
			// --- Correct size and/or position of the frame ------------------
			if (newSize!=null) this.frameObserved.setSize(newSize);
			if (yMovement!=0 || xMovement!=0) {
				int newXPos = this.frameObserved.getX() + xMovement;
				int newYPos = this.frameObserved.getY() + yMovement;
				this.frameObserved.setLocation(newXPos, newYPos);
			}
		}
		
		// --- Execute code in test area? -------------------------------------
		boolean isExecuteTestArea = false;
		if (isExecuteTestArea==true) {
			this.executeTestArea();
		}
	}
	/**
	 * Executes this test area.
	 */
	private void executeTestArea() {
		
		GraphicsConfiguration gc = this.getGraphicsDevice().getDefaultConfiguration();
		AffineTransform tx = gc.getDefaultTransform();
        double scaleX = tx.getScaleX();
        double scaleY = tx.getScaleY();
        System.out.printf("scaleX: %f, scaleY: %f\n", scaleX, scaleY); 
	}
	
	// ------------------------------------------------------------------------
	// --- From here, methods to determine necessary corrections -------------- 
	// ------------------------------------------------------------------------
	/**
	 * Return the x-value correction.
	 * @return the c correction
	 */
	private int getCorrectionXMovement() {
		Point screenPositionTL = this.getScreenPosition(this.getFramePositionTopLeft());
		if (screenPositionTL.x <= this.badPostionDistance *-1) {
			return screenPositionTL.x *-1;
		}
		return 0;
	}
	/**
	 * Return the y-value correction.
	 * @return the y correction
	 */
	private int getCorrectionYMovement() {
		Point screenPositionTL = this.getScreenPosition(this.getFramePositionTopLeft());
		if (screenPositionTL.y <= this.badPostionDistance *-1) {
			return screenPositionTL.y *-1;
		}
		return 0;
	}
	/**
	 * Returns the correction size of the current frame.
	 * @return the correction size
	 */
	private Dimension getCorrectionSize() {
		
		Dimension correctionSize = null;
		Rectangle windowBounds = this.getFrameBounds();
		Rectangle screenBounds = this.getDeviceBounds(this.getGraphicsDevice());
		if (this.isFrameMaximized()==false && ( windowBounds.width > screenBounds.width || windowBounds.height > screenBounds.height)) {
			correctionSize = new Dimension((int)(screenBounds.getWidth() * 0.95), (int) (screenBounds.getHeight() * 0.9));
		}
		
		// --- Local debug area -----------------------------------------------
		if (this.isDebug==true) {
			System.out.println("=> Device Size = " + this.getSizeAsString(screenBounds) + ", Frame Size " + this.getSizeAsString(windowBounds) + " => New Dimension: "  +(correctionSize!=null ? this.getSizeAsString(correctionSize) : "-"));
		}
		return correctionSize;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, methods to get necessary information -------------------- 
	// ------------------------------------------------------------------------
	/**
	 * Returns the GraphicsDevice currently used by the local frame.
	 * @return the graphics device
	 */
	private GraphicsDevice getGraphicsDevice() {
		return this.frameObserved.getGraphicsConfiguration().getDevice();
	}
	/**
	 * Returns the bound of the specified GraphicsDevice
	 * @param device the device
	 * @return the device bounds
	 */
	private Rectangle getDeviceBounds(GraphicsDevice device) {
		return device.getDefaultConfiguration().getBounds();
	}

	// ------------------------------------------------------------------------
	// --- From here, methods to get information about the current frame ------ 
	// ------------------------------------------------------------------------
	/**
	 * Returns the top-left frame position relative to the main graphics device.
	 * @return the frame position
	 */
	private Point getFramePositionTopLeft() {
		return this.frameObserved.getLocation();
	}
	/**
	 * Returns the bottom-right frame position relative to the main graphics device.
	 * @return the frame position
	 */
	private Point getFramePositionBottomRight() {
		return new Point(this.getFramePositionTopLeft().x + this.getFrameBounds().width, this.getFramePositionTopLeft().y + this.getFrameBounds().height);
	}
	/**
	 * Returns the current frame bounds .
	 * @return the frame position
	 */
	private Rectangle getFrameBounds() {
		return this.frameObserved.getBounds();
	}
	
	/**
	 * Checks if the current frame is maximized.
	 * @return true, if is frame maximized
	 */
	private boolean isFrameMaximized() {
		if (this.frameObserved.getExtendedState()==JFrame.MAXIMIZED_BOTH) return true;
		return false;
	}
	
	/**
	 * Returns the screen position for the specified frame position 
	 *
	 * @param framePosition the frame position
	 * @return the screen position
	 */
	private Point getScreenPosition(Point framePosition) {
		
		// --- Create screen position based on current frame position ---------
		Point screenPosition = new Point(framePosition);

		// --- Get current device bounds --------------------------------------
		Rectangle deviceBounds = this.getDeviceBounds(this.getGraphicsDevice());
		
		// --- Subtract the x/y position of the device ------------------------
		screenPosition.x -= deviceBounds.x;
		screenPosition.y -= deviceBounds.y;

		// --- Clip negative values... (???) ----------------------------------
		//if (screenPosition.x < 0) screenPosition.x *= -1;
		//if (screenPosition.y < 0) screenPosition.y *= -1;
		return screenPosition;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, methods for debug printing ------------------------------ 
	// ------------------------------------------------------------------------
	/**
	 * Returns the specified point as string.
	 * @param point the point
	 * @return the point as string
	 */
	private String getPointAsString(Point point) {
		return "[" + point.x + ", " + point.y + "]";
	}
	/**
	 * Returns the size of the specified Rectangle as string.
	 * @param rectangle the rectangle
	 * @return the point as string
	 */
	private String getSizeAsString(Rectangle rectangle) {
		return "[" + rectangle.width + ", " + rectangle.height + "]";
	}
	/**
	 * Returns the size of the specified Dimension as string.
	 *
	 * @return the point as string
	 */
	private String getSizeAsString(Dimension dimension) {
		return "[" + dimension.width + ", " + dimension.height + "]";
	}
	
}
