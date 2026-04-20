package de.enflexit.df.core.workbook;

import de.enflexit.common.StringHelper;

/**
 * The Class DataWorkbookLocation.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbookLocation {
	
	private String dataWorkbookClassName;
	private String dataWorkbookLocation;
	
	/**
	 * Instantiates a new data workbook description.
	 *
	 * @param dataWorkbookClassName the data workbook class
	 * @param dataWorkbookLocation the data workbook location
	 */
	public DataWorkbookLocation(Class<? extends DataWorkbook> dataWorkbookClass, String dataWorkbookLocation) {
		this(dataWorkbookClass.getName(), dataWorkbookLocation);
	}
	/**
	 * Instantiates a new data workbook description.
	 *
	 * @param dataWorkbookClassName the data workbook class name
	 * @param dataWorkbookLocation the data workbook location
	 */
	public DataWorkbookLocation(String dataWorkbookClassName, String dataWorkbookLocation) {
		this.setDataWorkbookClassName(dataWorkbookClassName);
		this.setDataWorkbookLocation(dataWorkbookLocation);
	}

	
	/**
	 * Returns the data workbook type.
	 * @return the data workbook type
	 */
	public String getDataWorkbookClassName() {
		return dataWorkbookClassName;
	}
	/**
	 * Sets the data workbook type.
	 * @param dataWorkbookClassName the new data workbook class name
	 */
	public void setDataWorkbookClassName(String dataWorkbookClassName) {
		this.dataWorkbookClassName = dataWorkbookClassName;
	}

	/**
	 * Returns the data workbook location.
	 * @return the data workbook location
	 */
	public String getDataWorkbookLocation() {
		return dataWorkbookLocation;
	}
	/**
	 * Sets the data workbook location.
	 * @param dataWorkbookLocation the new data workbook location
	 */
	public void setDataWorkbookLocation(String dataWorkbookLocation) {
		this.dataWorkbookLocation = dataWorkbookLocation;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null || compObj instanceof DataWorkbookLocation==false) return false;
		if (compObj==this) return true;
		
		DataWorkbookLocation compLocation = (DataWorkbookLocation)compObj;
		if (StringHelper.isEqualString(compLocation.getDataWorkbookClassName(), this.getDataWorkbookClassName())==false) return false;
		if (StringHelper.isEqualString(compLocation.getDataWorkbookLocation(), this.getDataWorkbookLocation())==false) return false;
		
		return true;
	}
}
