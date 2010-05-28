package contmas.de.unidue.stud.sehawagn;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.*;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class BayMapRenderer extends JApplet{
	private static final long serialVersionUID=3992617966815924216L;

	//some container constants
	public static Float CONTAINER_WIDTH=2.438F;
	public static Float CONTAINER_HEIGHT=2.591F;
	public static Float CONTAINER_LENGTH_40FT=12.192F; //40ft

	public static Float LENGTH_SPACING_STRADDLE=0.0F;
	public static Float WIDTH_SPACING_STRADDLE=CONTAINER_WIDTH / 4;

	private static Float CONTAINER_LENGTH=CONTAINER_LENGTH_40FT;
	private static Float LENGTH_SPACING=LENGTH_SPACING_STRADDLE;
	private static Float WIDTH_SPACING=WIDTH_SPACING_STRADDLE;

	private static Color3f GRID_COLOR=new Color3f(0.1f,0.8f,0.1f);
	private static Color3f PLANE_COLOR=new Color3f(1.0f,1.0f,1.0f);

	private static Color3f BOX_COLOR=new Color3f(1.0f,0.0f,0.0f);

	private static Float calcBayMapPhyX(Integer xCount){
		return (xCount * CONTAINER_LENGTH) + (xCount * LENGTH_SPACING);
	}

	private static Float calcBayMapPhyY(Integer yCount){
		return (yCount * CONTAINER_WIDTH) + ((yCount + 1) * WIDTH_SPACING);
	}

	private static Float calcBayMapPhyZ(Integer zCount){
		return (zCount * CONTAINER_HEIGHT);
	}

	private TransformGroup bayMapGroup;

	private Shape3D createXYGridX(Integer countX,Integer countZ){
		LineArray gridGeom=new LineArray(countZ * 2,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		Float offset=0.0f;
		for(Integer c=0;c < countZ * 2;c+=2){

			gridGeom.setCoordinate(c + 0,new Point3f(0.0f,offset,0.0f));
			gridGeom.setCoordinate(c + 1,new Point3f(calcBayMapPhyX(countX),offset,0.0f));

			offset+=CONTAINER_HEIGHT;
		}

		for(int i=0;i < countZ * 2;i++){
			gridGeom.setColor(i,GRID_COLOR);
		}

		return new Shape3D(gridGeom);
	}

	private Shape3D createXYGridY(Integer countX,Integer countZ){
		LineArray gridGeom=new LineArray(countX * 2,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		Float offset=0.0f;
		for(Integer c=0;c < countX * 2;c+=2){

			gridGeom.setCoordinate(c + 0,new Point3f(offset,0.0f,0.0f));
			gridGeom.setCoordinate(c + 1,new Point3f(offset,calcBayMapPhyZ(countZ),0.0f));

			offset+=CONTAINER_LENGTH;
		}

		for(int i=0;i < countX * 2;i++){
			gridGeom.setColor(i,GRID_COLOR);
		}
		return new Shape3D(gridGeom);
	}

	private Shape3D createXZGridX(Integer countX,Integer countY){
		LineArray landGeom=new LineArray((countY * 4) + 2,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		Float offset=0.0f;
		for(Integer c=0;c < countY * 4;c+=4){

			landGeom.setCoordinate(c + 0,new Point3f(0.0f,0.0f,offset));
			landGeom.setCoordinate(c + 1,new Point3f(calcBayMapPhyX(countX),0.0f,offset));

			landGeom.setCoordinate(c + 2,new Point3f(0.0f,0.0f,offset + WIDTH_SPACING));
			landGeom.setCoordinate(c + 3,new Point3f(calcBayMapPhyX(countX),0.0f,offset + WIDTH_SPACING));

			offset+=CONTAINER_WIDTH + WIDTH_SPACING;
		}

		landGeom.setCoordinate((countY * 4) + 0,new Point3f(0.0f,0.0f,offset));
		landGeom.setCoordinate((countY * 4) + 1,new Point3f(calcBayMapPhyX(countX),0.0f,offset));

		for(int i=0;i < (countY * 4) + 2;i++){
			landGeom.setColor(i,GRID_COLOR);
		}
		return new Shape3D(landGeom);
	}

	private Shape3D createXZGridZ(Integer countX,Integer countY){
		LineArray landGeom=new LineArray(countX * 2,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		Float offset=0.0f;
		for(Integer c=0;c < countX * 2;c+=2){

			landGeom.setCoordinate(c + 0,new Point3f(offset,0.0f,0.0f));
			landGeom.setCoordinate(c + 1,new Point3f(offset,0.0f,calcBayMapPhyY(countY)));

			offset+=CONTAINER_LENGTH;
		}

		for(int i=0;i < countX * 2;i++){
			landGeom.setColor(i,GRID_COLOR);
		}
		return new Shape3D(landGeom);
	}

	private Shape3D createYZGridY(Integer countY,Integer countZ){
		LineArray landGeom=new LineArray((countY * 4) + 2,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		Float offset=0.0f;
		for(Integer c=0;c < countY * 4;c+=4){

			landGeom.setCoordinate(c + 0,new Point3f(0.0f,0.0f,offset));
			landGeom.setCoordinate(c + 1,new Point3f(0.0f,calcBayMapPhyZ(countZ),offset));

			landGeom.setCoordinate(c + 2,new Point3f(0.0f,0.0f,offset + WIDTH_SPACING));
			landGeom.setCoordinate(c + 3,new Point3f(0.0f,calcBayMapPhyZ(countZ),offset + WIDTH_SPACING));

			offset+=CONTAINER_WIDTH + WIDTH_SPACING;
		}

		landGeom.setCoordinate((countY * 4) + 0,new Point3f(0.0f,0.0f,offset));
		landGeom.setCoordinate((countY * 4) + 1,new Point3f(0.0f,calcBayMapPhyZ(countZ),offset));

		for(int i=0;i < (countY * 4) + 2;i++){
			landGeom.setColor(i,GRID_COLOR);
		}
		return new Shape3D(landGeom);
	}

	private Shape3D createYZGridZ(Integer countY,Integer countZ){
		LineArray landGeom=new LineArray(countZ * 2,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		Float offset=0.0f;
		for(Integer c=0;c < countZ * 2;c+=2){

			landGeom.setCoordinate(c + 0,new Point3f(0.0f,offset,0.0f));
			landGeom.setCoordinate(c + 1,new Point3f(0.0f,offset,calcBayMapPhyY(countY)));

			offset+=CONTAINER_HEIGHT;
		}

		for(int i=0;i < countZ * 2;i++){
			landGeom.setColor(i,GRID_COLOR);
		}

		return new Shape3D(landGeom);
	}

	private class GraphPlanes extends Shape3D{

		private GraphPlanes(float l,float w,float d,Boolean wireFrame){
			Integer polygonMode;
			Color3f color;
			Appearance appear=new Appearance();

			if(wireFrame){
				polygonMode=PolygonAttributes.POLYGON_LINE;
				color=GRID_COLOR;
			}else{
				polygonMode=PolygonAttributes.POLYGON_FILL;
				color=PLANE_COLOR;
			}
			this.setGeometry(this.graphPlaneGeometry(l,w,d,color));

			PolygonAttributes polyAttrib=new PolygonAttributes();
			polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
			polyAttrib.setPolygonMode(polygonMode);
			appear.setPolygonAttributes(polyAttrib);
			this.setAppearance(appear);
		}

		private Geometry graphPlaneGeometry(float l,float w,float h,Color3f color){
			QuadArray qa;
			int N=12;

			Point3f coords[]=new Point3f[N];
			Color3f colors[]=new Color3f[N];

			coords[0]=new Point3f(l,0,0);
			coords[1]=new Point3f(0,0,0);
			coords[2]=new Point3f(0,0,w);
			coords[3]=new Point3f(l,0,w);

			colors[0]=color;
			colors[1]=color;
			colors[2]=color;
			colors[3]=color;

			coords[4]=coords[0];
			coords[5]=coords[1];
			coords[6]=new Point3f(0,h,0);
			coords[7]=new Point3f(l,h,0);

			colors[4]=color;
			colors[5]=color;
			colors[6]=color;
			colors[7]=color;

			coords[8]=coords[1];
			coords[9]=coords[2];
			coords[10]=new Point3f(0,h,w);
			coords[11]=new Point3f(0,h,0);

			colors[8]=color;
			colors[9]=color;
			colors[10]=color;
			colors[11]=color;

			qa=new QuadArray(N,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
			qa.setCoordinates(0,coords);
			qa.setColors(0,colors);

			return qa;
		}
	}

	public BayMapRenderer(){
		super();
		constructBayMapRendering(2,3,4); //BayMap-Dimensions: x=length, y=width, z=height
	}

	private Canvas3D canvas;
	
	public Canvas getCanvas(){
		return canvas;
	}
	
	private void constructBayMapRendering(Integer xCount,Integer yCount,Integer zCount){
		this.setLayout(new BorderLayout());
		GraphicsConfiguration config=SimpleUniverse.getPreferredConfiguration();

		canvas=new Canvas3D(config);
		this.add("Center",canvas);

		BranchGroup scene=this.createSceneGraph(xCount,yCount,zCount);

		// SimpleUniverse is a Convenience Utility class
		SimpleUniverse simpleU=new SimpleUniverse(canvas);
		/*
		TransformGroup vpTrans=simpleU.getViewingPlatform().getViewPlatformTransform();
		MouseZoom myMouseZoom = new MouseZoom(MouseBehavior.INVERT_INPUT);
		myMouseZoom.setTransformGroup(vpTrans);
		myMouseZoom.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
		scene.addChild(myMouseZoom);
		*/
		// Let Java 3D perform optimizations on this scene graph.
		scene.compile();

		//set activation radius to manipulate from distance, may need this later
		//		simpleU.getViewingPlatform().getViewPlatform().setActivationRadius(300f);

		// This will move the ViewPlatform back a bit so the objects in the scene can be viewed.
		//		simpleU.getViewingPlatform().setNominalViewingTransform();

		//Set view distance even farther away (10m)
		Transform3D t3d=new Transform3D();
		t3d.lookAt(new Point3d(0,0,40),new Point3d(0,0,0),new Vector3d(0,1,0));
		t3d.invert();
		simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(t3d);

		//set back clip distance so things don't disappear, may need this later
		//		simpleU.getViewer().getView().setBackClipDistance(300);

		simpleU.addBranchGraph(scene);
	}

	private BranchGroup createSceneGraph(Integer x,Integer y,Integer z){

		BranchGroup objRoot=new BranchGroup();

		// Create the transform group node and initialize it to the
		// identity.  Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime.  Add it to the
		// root of the subgraph.
		bayMapGroup=new TransformGroup();
		bayMapGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bayMapGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		objRoot.addChild(bayMapGroup);

		bayMapGroup.addChild(new GraphPlanes(calcBayMapPhyX(x),calcBayMapPhyY(y),calcBayMapPhyZ(z),true)); //wireframe
		bayMapGroup.addChild(new GraphPlanes(calcBayMapPhyX(x),calcBayMapPhyY(y),calcBayMapPhyZ(z),false)); //planes

		bayMapGroup.addChild(createXYGridX(x,z));
		bayMapGroup.addChild(createXYGridY(x,z));
		bayMapGroup.addChild(createXZGridX(x,y));
		bayMapGroup.addChild(createXZGridZ(x,y));
		bayMapGroup.addChild(createYZGridY(y,z));
		bayMapGroup.addChild(createYZGridZ(y,z));
		createContainerAt(0,0,0);
		createContainerAt(1,1,1);

		/*
				new Transform3D();
				Alpha rotationAlpha=new Alpha( -1,4000);
		
				RotationInterpolator rotator=new RotationInterpolator(rotationAlpha,objSpin);
				BoundingSphere bounds=new BoundingSphere(new Point3d(0.0,0.0,0.0),10.0);
				rotator.setSchedulingBounds(bounds);
				objSpin.addChild(rotator);
				*/

		MouseRotate myMouseRotate=new MouseRotate();
		myMouseRotate.setTransformGroup(bayMapGroup);
		myMouseRotate.setSchedulingBounds(new BoundingSphere());
		objRoot.addChild(myMouseRotate);

		MouseZoom myMouseZoom=new MouseZoom(MouseBehavior.INVERT_INPUT);
		myMouseZoom.setTransformGroup(bayMapGroup);
		myMouseZoom.setSchedulingBounds(new BoundingSphere());
		objRoot.addChild(myMouseZoom);

		return objRoot;
	}

	private TransformGroup createContainer(){
		Appearance appear=new Appearance();
		appear.setColoringAttributes(new ColoringAttributes(BOX_COLOR,ColoringAttributes.SHADE_FLAT));

		Box box=new Box(CONTAINER_LENGTH / 2,CONTAINER_HEIGHT / 2,CONTAINER_WIDTH / 2,appear);
		TransformGroup boxWrap=new TransformGroup();
		boxWrap.addChild(box);

		Transform3D t3d=new Transform3D();
		Vector3f moveVect=new Vector3f(CONTAINER_LENGTH / 2,CONTAINER_HEIGHT / 2,CONTAINER_WIDTH / 2);
		t3d.setTranslation(moveVect);
		boxWrap.setTransform(t3d);
		return boxWrap;
	}

	private TransformGroup moveContainerTo(TransformGroup container,Integer x,Integer y,Integer z){ //BayMapBlockAddress coordinates (phy)
		TransformGroup moveWrap=new TransformGroup();
		moveWrap.addChild(container);

		Transform3D t3d=new Transform3D();
		Vector3f moveVect=new Vector3f((x * CONTAINER_LENGTH) + ((x + 1) * LENGTH_SPACING),z * CONTAINER_HEIGHT,(y * CONTAINER_WIDTH) + ((y + 1) * WIDTH_SPACING));
		t3d.setTranslation(moveVect);
		moveWrap.setTransform(t3d);
		return moveWrap;
	}
	
	public void createContainerAt(Integer x,Integer y,Integer z){
		bayMapGroup.addChild(moveContainerTo(createContainer(),x,y,z));
	}
}