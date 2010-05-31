package contmas.de.unidue.stud.sehawagn.contmas.monitor;

import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.util.*;

import javax.media.j3d.*;
import javax.swing.JApplet;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;

import contmas.main.Const;

public class BayMapRenderer extends JApplet{
	private static final long serialVersionUID=3992617966815924216L;

	//some container constants
	public static Float CONTAINER_WIDTH=Const.CONTAINER_WIDTH;
	public static Float CONTAINER_HEIGHT=Const.CONTAINER_HEIGHT;
	private static Float CONTAINER_LENGTH=Const.CONTAINER_LENGTH_40FT;
	private static Float LENGTH_SPACING=Const.LENGTH_SPACING_STRADDLE;
	private static Float WIDTH_SPACING=Const.WIDTH_SPACING_STRADDLE;

	private static Color3f GRID_COLOR=new Color3f(0.1f,0.8f,0.1f);
	private static Color3f PLANE_COLOR=new Color3f(1.0f,1.0f,1.0f);

	private static Color3f BOX_COLOR=new Color3f(1.0f,0.0f,0.0f);
	private static Color3f BOX_BORDER_COLOR=new Color3f(0.0f,0.0f,0.0f);


	private Boolean dummy=true;

	private Integer xCount;

	private Integer yCount;

	private Integer zCount;

	private BranchGroup scene;

	private TransformGroup bayMapGroup;

	private List<BranchGroup> allContainers=new ArrayList<BranchGroup>();

	private SimpleUniverse universe;

	private class BayMapBoundPlanes extends Shape3D{

		private BayMapBoundPlanes(float l,float w,float d,Boolean wireFrame){
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

	private static Float calcBayMapPhyX(Integer xCount){
		return (xCount * CONTAINER_LENGTH) + (xCount * LENGTH_SPACING);
	}

	private static Float calcBayMapPhyY(Integer yCount){
		return (yCount * CONTAINER_WIDTH) + ((yCount + 1) * WIDTH_SPACING);
	}

	private static Float calcBayMapPhyZ(Integer zCount){
		return (zCount * CONTAINER_HEIGHT);
	}

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

	public BayMapRenderer(){
		super();
	}

	public BayMapRenderer(Boolean dummy){
		this();
		this.dummy=dummy;
	}

	@Override
	public void init(){
		constructBayMapRendering(); //BayMap-Dimensions: x=length, y=width, z=height

		if(dummy){
			setBayMapDimensions(2,3,4); //set dummy rendering
			renderDummyLoading();
			renderBayMapBounds();
		}
	}
	
	public void init(boolean noIdea){
		
	}

	private void constructBayMapRendering(){
		GraphicsConfiguration config=SimpleUniverse.getPreferredConfiguration();

		Canvas3D canvas=new Canvas3D(config);
		this.add(canvas);

		bayMapGroup=new TransformGroup();
		bayMapGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bayMapGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		bayMapGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		bayMapGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		
		scene=new BranchGroup();
		scene.addChild(bayMapGroup);

		/*
		new Transform3D();
		Alpha rotationAlpha=new Alpha( -1,4000);

		RotationInterpolator rotator=new RotationInterpolator(rotationAlpha,objSpin);
		BoundingSphere bounds=new BoundingSphere(new Point3d(0.0,0.0,0.0),10.0);
		rotator.setSchedulingBounds(bounds);
		objSpin.addChild(rotator);
		*/

/*
MouseZoom myMouseZoom=new MouseZoom(MouseBehavior.INVERT_INPUT);
myMouseZoom.setTransformGroup(bayMapGroup);
myMouseZoom.setSchedulingBounds(new BoundingSphere());
objRoot.addChild(myMouseZoom);
*/

		// SimpleUniverse is a Convenience Utility class
		universe=new SimpleUniverse(canvas);

		TransformGroup vpTrans=universe.getViewingPlatform().getViewPlatformTransform();

		MouseZoom myMouseZoom=new MouseZoom();
		myMouseZoom.setTransformGroup(bayMapGroup);
		myMouseZoom.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
		scene.addChild(myMouseZoom);

		MouseRotate myMouseRotate=new MouseRotate();
		myMouseRotate.setTransformGroup(bayMapGroup);
		myMouseRotate.setSchedulingBounds(new BoundingSphere());
		scene.addChild(myMouseRotate);

		// Let Java 3D perform optimizations on this scene graph.
		scene.compile();

		//set activation radius to manipulate from distance, may need this later
		//		simpleU.getViewingPlatform().getViewPlatform().setActivationRadius(300f);

		// This will move the ViewPlatform back a bit so the objects in the scene can be viewed.
		//		simpleU.getViewingPlatform().setNominalViewingTransform();

		//Set view distance even farther away (10m)
		/*
		Transform3D t3d=new Transform3D();
		t3d.lookAt(new Point3d(0,0,40),new Point3d(0,0,0),new Vector3d(0,1,0));
		t3d.invert();
		*/
//		universe.getViewingPlatform().getViewPlatformTransform().setTransform(t3d);

		//set back clip distance so things don't disappear, may need this later
		//		simpleU.getViewer().getView().setBackClipDistance(300);

		universe.addBranchGraph(scene);
	}
	
	private void resetViewPosition(){
		Transform3D t3d=new Transform3D();
//		t3d.lookAt(new Point3d(calcBayMapPhyX(xCount)/2,calcBayMapPhyZ(zCount)+10,calcBayMapPhyX(xCount)+calcBayMapPhyY(yCount)),new Point3d(calcBayMapPhyX(xCount)/2,calcBayMapPhyZ(zCount)/2,calcBayMapPhyY(yCount)/2),new Vector3d(0,1,0)); //(position,target,noIdea)
		t3d.lookAt(new Point3d(calcBayMapPhyX(xCount)/2,calcBayMapPhyZ(zCount)+10,calcBayMapPhyX(xCount)+calcBayMapPhyY(yCount)),new Point3d(0,0,0),new Vector3d(0,1,0)); //(position,target,noIdea)

		t3d.invert();
		universe.getViewingPlatform().getViewPlatformTransform().setTransform(t3d);
		universe.getViewer().getView().setBackClipDistance(calcBayMapPhyX(xCount)+calcBayMapPhyY(yCount)+50);
		universe.getViewingPlatform().getViewPlatform().setActivationRadius(calcBayMapPhyX(xCount)+calcBayMapPhyY(yCount)+50);

	}

	private BranchGroup createBayMapBounds(){

		// Create the transform group node and initialize it to the
		// identity.  Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime.  Add it to the
		// root of the subgraph.
		BranchGroup bayMapBounds=new BranchGroup();

		bayMapBounds.addChild(new BayMapBoundPlanes(calcBayMapPhyX(xCount),calcBayMapPhyY(yCount),calcBayMapPhyZ(zCount),true)); //wireframe
		bayMapBounds.addChild(new BayMapBoundPlanes(calcBayMapPhyX(xCount),calcBayMapPhyY(yCount),calcBayMapPhyZ(zCount),false)); //planes

		bayMapBounds.addChild(createXYGridX(xCount,zCount));
		bayMapBounds.addChild(createXYGridY(xCount,zCount));
		bayMapBounds.addChild(createXZGridX(xCount,yCount));
		bayMapBounds.addChild(createXZGridZ(xCount,yCount));
		bayMapBounds.addChild(createYZGridY(yCount,zCount));
		bayMapBounds.addChild(createYZGridZ(yCount,zCount));

		bayMapBounds.compile();
		return bayMapBounds;
	}

	private TransformGroup createContainer(){
		Appearance appear=new Appearance();
		appear.setColoringAttributes(new ColoringAttributes(BOX_COLOR,ColoringAttributes.SHADE_FLAT));

		TransformGroup boxWrap=new TransformGroup();
		
		Box box=new Box(CONTAINER_LENGTH / 2,CONTAINER_HEIGHT / 2,CONTAINER_WIDTH / 2,appear);
		boxWrap.addChild(box);

		PolygonAttributes polyAttrib=new PolygonAttributes();
		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		appear=new Appearance();
		appear.setPolygonAttributes(polyAttrib);
		appear.setColoringAttributes(new ColoringAttributes(BOX_BORDER_COLOR,ColoringAttributes.SHADE_FLAT));
		box=new Box(CONTAINER_LENGTH / 2,CONTAINER_HEIGHT / 2,CONTAINER_WIDTH / 2,appear);
		boxWrap.addChild(box);
/*
        Text2D text2d = new Text2D("2D text in Java 3D", 
                new Color3f(0.0f, 0.0f, 1.0f), 
                "Helvetica", 250, Font.ITALIC);

        boxWrap.addChild(text2d);
                */
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

	public void setBayMapDimensions(Integer xCount,Integer yCount,Integer zCount){
		this.xCount=xCount;
		this.yCount=yCount;
		this.zCount=zCount;
	}

	public void createContainerAt(Integer x,Integer y,Integer z){
		BranchGroup newBranchGroup=new BranchGroup();
		newBranchGroup.addChild(moveContainerTo(createContainer(),x,y,z));
		newBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);

		allContainers.add(newBranchGroup);
		bayMapGroup.addChild(newBranchGroup);
	}

	public void renderBayMapBounds(){
		bayMapGroup.addChild(this.createBayMapBounds());
		
		Transform3D t3d=new Transform3D();
		Vector3f centerVect=new Vector3f(-(calcBayMapPhyX(xCount)/2),-(calcBayMapPhyZ(zCount)/2),-(calcBayMapPhyY(yCount)/2));
		t3d.setTranslation(centerVect);
		bayMapGroup.setTransform(t3d);
		
		resetViewPosition();
	}

	public void renderDummyLoading(){
		createContainerAt(0,0,0);
		createContainerAt(1,1,1);
	}

	public void flushLoading(){
		allContainers.iterator();
		for(Iterator<BranchGroup> iterator=allContainers.iterator();iterator.hasNext();){
			BranchGroup curContainer=(BranchGroup) iterator.next();
			curContainer.detach();
			iterator.remove();
		}
	}

	/**
	 * @param loading
	 */
	public void renderLoading(HashMap<Vector<Integer>, String> loading){
		if(loading != null){
			Set<Vector<Integer>> addressVecs=loading.keySet();
			for(Vector<Integer> curVector: addressVecs){
				createContainerAt(curVector.get(0),curVector.get(1),curVector.get(2));
			}
		}
	}
}