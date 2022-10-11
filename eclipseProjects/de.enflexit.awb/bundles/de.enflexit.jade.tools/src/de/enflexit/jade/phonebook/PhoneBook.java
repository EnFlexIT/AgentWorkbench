package de.enflexit.jade.phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import de.enflexit.jade.phonebook.search.PhoneBookSearchFilter;
import jade.core.AID;

/**
 * Generic implementation of a phone book for agent systems.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <T> the generic type
 */
@XmlRootElement
public class PhoneBook<T extends AbstractPhoneBookEntry>{
	
	private static final String DEFAULT_PHONEBOOK_FILENAME = "Phonebook.xml";
	
	@XmlTransient
	private File phoneBookFile;
	@XmlTransient
	private AID ownerAID;
	
	@XmlElementWrapper(name = "phoneBookEntries")
	TreeMap<String, T> phoneBookContent;
	
	private Vector<PhoneBookListener<T>> phoneBookListeners;
	
	private boolean enableNotifications = true;
	
	/**
	 * Instantiates a new phone book. Can't be persisted unless either the phoneBookFile or the ownerAID is set.
	 */
	public PhoneBook() {}

	/**
	 * Instantiates a new phone book, that is persisted in the specified file.
	 * @param phoneBookFile the phone book file
	 */
	public PhoneBook(File phoneBookFile) {
		this.phoneBookFile = phoneBookFile;
	}

	/**
	 * Instantiates a new phone book, that is persisted in the working directory of the specified agent.
	 * @param ownerAID the agent owning this phone book
	 */
	public PhoneBook(AID ownerAID) {
		this.ownerAID = ownerAID;
	}

	/**
	 * Gets the phone book content.
	 * @return the phone book content
	 */
	private TreeMap<String, T> getPhoneBookContent() {
		if (phoneBookContent==null) {
			phoneBookContent = new TreeMap<String, T>();
		}
		return phoneBookContent;
	}
	
	/**
	 * Adds the provided entry to the phonebook if it is valid. The phonebook will be stored afterwards.
	 * @param entry the entry
	 * @return true, if successful
	 */
	public boolean addEntry(T entry) {
		return this.addEntry(entry, true);
	}
	
	/**
	 * Adds the provided entry to the phone book if it is valid.
	 * @param entry the entry
	 * @param persist specifies if the phonebook will be saved afterwards.
	 * @return true, if successful
	 */
	public boolean addEntry(T entry, boolean persist) {
		if (entry.isValid()) {
			this.getPhoneBookContent().put(entry.getUniqueIdentifier(), entry);
			if (persist==true) {
				this.save();
			}
			this.notifyAdded(entry);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addEntries(List<T> entries) {
		// --- Pause notifications --------------
		this.enableNotifications = false;
		for (int i=0; i<entries.size(); i++) {
			this.addEntry(entries.get(i), false);
		}
		
		this.enableNotifications = true;
		this.notifyAdded(null);
		
		return true;
	}
	
	/**
	 * Gets the entry with the specified identifier.
	 * @param identifier the identifier
	 * @return the entry
	 */
	public T getEntry(String identifier) {
		return this.getPhoneBookContent().get(identifier);
	}
	
	/**
	 * Gets the entries.
	 * @param searchFilter the search filter
	 * @return the entries
	 */
	public ArrayList<T> getEntries(PhoneBookSearchFilter<T> searchFilter){
		ArrayList<T> resultList = new ArrayList<T>();
		for (T entry : this.getPhoneBookContent().values()) {
			if (searchFilter.matches(entry)) {
				resultList.add(entry);
			}
		}
		return resultList;
	}
	
	/**
	 * Removes the entry with the specified identifier from the phohebook. The phonebook will be stored afterwards.
	 * @param identifier the identifier
	 */
	public void removeEntry(String identifier) {
		T entryToRemove = this.getEntry(identifier);
		if (entryToRemove!=null) {
			this.removeEntry(entryToRemove, true);
		}
	}
	
	/**
	 * Removes the specified entry from the phonebook. The phonebook will be stored afterwards.
	 * @param entry the entry to be removed
	 */
	public void removeEntry(T entry) {
		this.removeEntry(entry, true);
	}
	
	/**
	 * Removes the specified entry from the phonebook.
	 * @param entry the entry to be removed
	 * @param persist specifies if the phone book will be saved afterwards
	 */
	public void removeEntry(T entry, boolean persist) {
		this.getPhoneBookContent().remove(entry.getUniqueIdentifier());
		if (persist==true) {
			this.save();
		}
		
		this.notifyRemoved(entry);
	}
	
	/**
	 * Searches for phone book entries that match the provided search filter.
	 * @param searchFilter the search filter
	 * @return the array list
	 */
	public ArrayList<T> searchEntries(PhoneBookSearchFilter<T> searchFilter){
		ArrayList<T> resultList = new ArrayList<T>();
		for (T entry : this.getPhoneBookContent().values()) {
			if (searchFilter.matches(entry)) {
				resultList.add(entry);
			}
		}
		return resultList;
	}

	/**
	 * Gets the phone book file.
	 * @return the phone book file
	 */
	@XmlTransient
	public File getPhoneBookFile() {
		if (this.phoneBookFile==null) {
			Path phoneBookFilePath = this.getPhonebookFilePath();
			if (phoneBookFilePath!=null) {
				this.phoneBookFile = phoneBookFilePath.toFile();
			}
		}
		return this.phoneBookFile;
	}
	
	/**
	 * Gets the phonebook file path.
	 * @return the phonebook file path
	 */
	private Path getPhonebookFilePath() {
		Path phonebookFilePath = null;
		
		Project currProject = Application.getProjectFocused();
		if (currProject!=null && this.ownerAID!=null) {
			phonebookFilePath = Path.of(currProject.getProjectAgentWorkingFolderFullPath()).resolve(ownerAID.getLocalName()).resolve(DEFAULT_PHONEBOOK_FILENAME); 
			if (Files.notExists(phonebookFilePath.getParent())) {
				try {
					Files.createDirectories(phonebookFilePath.getParent());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("[" +this.getClass().getSimpleName() + "] Insufficient data for generating phonebook file path. A project must be loaded and an owner AID must be specified!");
		}
			
		return phonebookFilePath;
	}

	/**
	 * Sets the phone book file.
	 * @param phoneBookFile the new phone book file
	 */
	public void setPhoneBookFile(File phoneBookFile) {
		this.phoneBookFile = phoneBookFile;
	}
	
	/**
	 * Gets the owner AID.
	 * @return the owner AID
	 */
	@XmlTransient
	public AID getOwnerAID() {
		return ownerAID;
	}

	/**
	 * Sets the owner AID.
	 * @param ownerAID the new owner AID
	 */
	public void setOwnerAID(AID ownerAID) {
		this.ownerAID = ownerAID;
	}

	/**
	 * Saves this phone book instance to an XML file.
	 */
	public void save() {
		if (this.getPhoneBookFile()!=null) {
			saveAs(this, this.getPhoneBookFile());
		}
	}
	
	public static <T extends AbstractPhoneBookEntry> boolean saveAs(PhoneBook<T> phoneBook, File phoneBookFile) {
		
		boolean successful = false;
		if (phoneBookFile!=null) {

			FileWriter fileWriter = null;
			try {
				// --- Save the PhoneBook to file ---------
				Class<?> phoneBookClass = phoneBook.getClass();
				Class<?> entryClass = phoneBook.getPhoneBookContent().values().iterator().next().getClass();
				JAXBContext pbc = JAXBContext.newInstance(phoneBookClass, entryClass);
				Marshaller pbm = pbc.createMarshaller();
				pbm.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				pbm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

				// --- Write to XML-File ------------------
				fileWriter = new FileWriter(phoneBookFile);
				pbm.marshal(phoneBook, fileWriter);
				successful = true;
				
			} catch (JAXBException | IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (fileWriter!=null)  fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return successful;
	}
	
	/**
	 * Load phone book.
	 * @param phoneBookFile the phone book file
	 * @return the phone book
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AbstractPhoneBookEntry> PhoneBook<T> loadPhoneBook(File phoneBookFile, Class<T> classInstance) {
		
		PhoneBook<T> pb = null;
		if (phoneBookFile.exists()) {
			// --- Check if the directory is available ----
			// --- Load the PhoneBook from file -------
			FileReader fileReader = null;
			try {
				fileReader = new FileReader(phoneBookFile);
				JAXBContext pbc = JAXBContext.newInstance(PhoneBook.class, classInstance);
				Unmarshaller um = pbc.createUnmarshaller();
				pb = (PhoneBook<T>) um.unmarshal(fileReader);
				pb.setPhoneBookFile(phoneBookFile);

			} catch (FileNotFoundException | JAXBException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (fileReader!=null) fileReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
				
		}
		return pb;
	}
	
	/**
	 * Gets the listeners.
	 * @return the listeners
	 */
	private Vector<PhoneBookListener<T>> getPhoneBookListeners() {
		if (phoneBookListeners==null) {
			phoneBookListeners = new Vector<>();
		}
		return phoneBookListeners;
	}
	
	/**
	 * Notifies the registered listeners about an added phone book entry.
	 * @param addedEntry the added entry
	 */
	private void notifyAdded(T addedEntry) {
		if (this.enableNotifications==true) {
			for (PhoneBookListener<T> listener : this.getPhoneBookListeners()) {
				listener.phoneBookEntryAdded(addedEntry);
			}
		}
	}
	
	/**
	 * Notifies the registered listeners about a removed phone book entry.
	 * @param removedEntry the removed entry
	 */
	private void notifyRemoved(T removedEntry) {
		if (this.enableNotifications==true) {
			for (PhoneBookListener<T> listener : this.getPhoneBookListeners()) {
				listener.phoneBookEntryRemoved(removedEntry);
			}
		}
	}
	
	/**
	 * Adds a phone book listener.
	 * @param listener the listener
	 */
	public void addPhoneBookListener(PhoneBookListener<T> listener) {
		if (this.getPhoneBookListeners().contains(listener)==false) {
			this.getPhoneBookListeners().add(listener);
		}
	}
	
	/**
	 * Removes a phone book listener.
	 * @param listener the listener
	 */
	public void removePhoneBookListener(PhoneBookListener<T> listener) {
		if (this.getPhoneBookListeners().contains(listener)==true) {
			this.getPhoneBookListeners().remove(listener);
		}
	}
	
	/**
	 * Gets the aid for the agent with the specified local name. May be null if not found.
	 * @param localName the local name
	 * @return the aid for local name
	 */
	public AID getAidForLocalName(String localName) {
		for (T entry : this.getPhoneBookContent().values()) {
			if (entry.getAgentAID().getLocalName().equals(localName)) {
				return entry.getAgentAID();
			}
		}
		
		return null;
	}
}
