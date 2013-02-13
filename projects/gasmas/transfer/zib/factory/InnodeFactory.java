package gasmas.transfer.zib.factory;

import gasmas.ontology.GeoCoordinate;
import gasmas.ontology.Innode;
import gasmas.transfer.zib.net.GasNodeType;

public abstract class InnodeFactory {
	
	public static Innode newInstance(GasNodeType gasNodeType) {
		Innode innode = new Innode();
		setInnodeAttributes(innode, gasNodeType);
		return innode;
	}
	
	protected static void setInnodeAttributes(Innode innode, GasNodeType gasNodeType) {
		
		innode.setID(gasNodeType.getId());
		innode.setAlias(gasNodeType.getAlias());
		innode.setHeight(ValueTypeFactory.newInstance(gasNodeType.getHeight()));
		innode.setPressureMin(ValueTypeFactory.newInstance(gasNodeType.getPressureMin()));
		innode.setPressureMax(ValueTypeFactory.newInstance(gasNodeType.getPressureMax()));
		innode.setGeoCoordinate(newGeoCoordinateInstance(gasNodeType));
	}

	private static GeoCoordinate newGeoCoordinateInstance(GasNodeType gasNodeType) {
		GeoCoordinate geoCoordinate = new GeoCoordinate();	
		geoCoordinate.setGeoW(gasNodeType.getGeoGKRight().floatValue());
		geoCoordinate.setGeoX(gasNodeType.getGeoGKUp().floatValue());
		geoCoordinate.setGeoY(gasNodeType.getGeoWGS84Lat().floatValue());
		geoCoordinate.setGeoZ(gasNodeType.getGeoWGS84Long().floatValue());
		return geoCoordinate;
	}
	
	public static Object[] getInnodeAsDataModel4NetworkComponentAdapter(GasNodeType gasNodeType) {
		Innode innode = newInstance(gasNodeType);
		Object[] datModel = new Object[1]; 
		datModel[0] = innode;
		return datModel;
	}
}
