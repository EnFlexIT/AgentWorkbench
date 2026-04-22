package de.enflexit.df.core.workbook;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JFileChooser;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.FileSelection;
import de.enflexit.df.core.model.DataController;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * The Class DataWorkbook for XML files.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement
public class DataWorkbook4XML extends DataWorkbook {

	private static final long serialVersionUID = 5010880029903092936L;
	
	private transient File workbookXmlFile;
	
	
	/**
	 * Instantiates a new data workbook 4 XML.
	 */
	public DataWorkbook4XML() {
		this(null);
	}
	/**
	 * Instantiates a new data workbook 4 XML.
	 * @param workbookXmlFile the workbook xml file
	 */
	public DataWorkbook4XML(File workbookXmlFile) {
		super();
		this.setDataWorkbookFile(workbookXmlFile);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataWorkbookFile()
	 */
	@Override
	public File getDataWorkbookFile() {
		return workbookXmlFile;
	}
	/**
	 * Sets the workbook Json file.
	 * @param workbookJsonFile the new workbook Json file
	 */
	public void setDataWorkbookFile(File workbookXmlFile) {
		this.workbookXmlFile = workbookXmlFile;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataWorkbookLocation()
	 */
	@Override
	public DataWorkbookLocation getDataWorkbookLocation() {
		return new DataWorkbookLocation(this.getClass(), this.getDataWorkbookFile().getAbsolutePath());
	}
	/**
	 * Load from data work book location.
	 *
	 * @param dwLocation the dw location
	 * @return the data workbook
	 */
	public static DataWorkbook loadFromDataWorkbookLocation(DataWorkbookLocation dwLocation) {
		
		if (dwLocation==null || dwLocation.getDataWorkbookLocation()==null || dwLocation.getDataWorkbookLocation().isEmpty()==true) return null;

		File dwFile = new File(dwLocation.getDataWorkbookLocation());
		return loadFromFile(dwFile);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#save()
	 */
	@Override
	public boolean save() {
		return DataWorkbook4XML.saveToFile(this, this.getDataWorkbookFile());
	}
	/**
	 * Saves the specified DataWorkbook to a XML file.
	 *
	 * @param dataWorkbook the data workbook
	 * @param fileToSaveTo the file to save to
	 * @return true, if successful
	 */
	public static boolean saveToFile(DataWorkbook4XML dataWorkbook, File fileToSaveTo) {
		
		Writer writer = null;
		try {
			// --- Prepare Context and Marshaller -------------------
			JAXBContext pc = JAXBContext.newInstance(DataWorkbook4XML.class);
			Marshaller pm = pc.createMarshaller();
			pm.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// --- Write values to xml-File -------------------------
			writer = new FileWriter(fileToSaveTo);
			pm.marshal(dataWorkbook, writer);
			
			return true;
			
		} catch (JAXBException | IOException ex) {
			ex.printStackTrace();
		} finally {
			if (writer!=null) {
				try {
					writer.close();
				} catch (IOException ioEx) {
				}
			}
		}
		return false;
	}
	
	/**
	 * Loads a DataWorkbook from a XML file that is to be selected by the user.
	 *
	 * @param component the asking component owner window
	 * @return the data workbook 4 XML
	 */
	public static DataWorkbook4XML loadFromFile(Component component) {
		return DataWorkbook4XML.loadFromFile(OwnerDetection.getOwnerWindowForComponent(component));
	}
	/**
	 * Loads a DataWorkbook from a XML file that is to be selected by the user.
	 *
	 * @param owner the owner window
	 * @return the data workbook 4 XML
	 */
	public static DataWorkbook4XML loadFromFile(Window owner) {
		
		File xmlFile = FileSelection.selectXMLFile(owner, JFileChooser.OPEN_DIALOG, "Open Workbook", "Open XML DataWorkbook", null, null);
		if (xmlFile==null) return null;
		
		return DataWorkbook4XML.loadFromFile(xmlFile);
	}
	/**
	 * Loads the specified DataWorkbook to a XML file.
	 *
	 * @param fileToOpen the file to open
	 * @return the data workbook 4 XML
	 */
	public static DataWorkbook4XML loadFromFile(File fileToOpen) {
		
		DataWorkbook4XML dwb = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileToOpen);
			JAXBContext pc = JAXBContext.newInstance(DataWorkbook4XML.class);
			Unmarshaller um = pc.createUnmarshaller();
			dwb = (DataWorkbook4XML) um.unmarshal(fileReader);
			dwb.setDataWorkbookFile(fileToOpen);
			
		} catch (FileNotFoundException | JAXBException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (fileReader!=null) {
					fileReader.close();
				}
			} catch (IOException ioe) {
			}
		}
		return dwb;
	}
	
	/**
	 * Creates a XML DataWorkbok by asking for a storage location.
	 *
	 * @param dataController the data controller
	 * @param component the asking component owner window
	 * @return the DataWorkbook4XML
	 */
	public static DataWorkbook4XML create(DataController dataController, Component component) {
		return DataWorkbook4XML.create(dataController, OwnerDetection.getOwnerWindowForComponent( component));
	}
	/**
	 * Creates a XML DataWorkbok by asking for a storage location.
	 *
	 * @param dataController the data controller
	 * @param owner the owner window
	 * @return the DataWorkbook4XML
	 */
	public static DataWorkbook4XML create(DataController dataController, Window owner) {
		
		File xmlFile = FileSelection.selectXMLFile(owner, JFileChooser.SAVE_DIALOG, "Create Workbook", "Create XML DataWorkbook", null, null);
		if (xmlFile==null) return null;

		DataWorkbookLocation dwLocation = new DataWorkbookLocation(DataWorkbook4XML.class, xmlFile.getAbsolutePath());
		if (dataController.getDataWorkbookReminder().getDataWorkbookLocationList().contains(dwLocation)==true) {
			AwbMessageDialog.showMessageDialog(owner, "The Data Workbook '" + xmlFile.getName() + "' was already loaded to the data viewer and thus cannot be overwritten!", "Data Workbook already loaded!", AwbMessageDialog.ERROR_MESSAGE);
			return null;
		} 
		
		DataWorkbook4XML dwbXML = new DataWorkbook4XML(xmlFile);
		dwbXML.createRandomID();
		dwbXML.setName(xmlFile.getName());
		dwbXML.setDescription("Description of the XML DataWorkbook");
		dwbXML.save();
		return dwbXML;
	}
	
}
