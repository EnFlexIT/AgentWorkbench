package gasmas.ontology.factory;

import gasmas.ontology.GeoCoordinate;
import gasmas.ontology.Innode;
import gasmas.transfer.zib.net.GasNodeType;

public abstract class InnodeFactory {
	
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
}
