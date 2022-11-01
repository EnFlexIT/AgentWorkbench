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
 */
@XmlRootElement
public class PhoneBook {
	
	private static final String DEFAULT_PHONEBOOK_FILENAME = "Phonebook.xml";
	
	@XmlTransient
	private File phoneBookFile;
	@XmlTransient
	private AID ownerAID;
	
	@XmlElementWrapper(name = "phoneBookEntries")
	TreeMap<String, AbstractPhoneBookEntry> phoneBookContent;
	
	private Vector<PhoneBookListener> phoneBookListeners;
	
	private boolean doPersist;
	
	private boolean enableNotifications = true;
	
	/**
	 * Instantiates a new phone book. Can't be persisted unless either the phoneBookFile or the ownerAID is set.
	 */
	public PhoneBook() {
		this(true);
	}
	
	/**
	 * Instantiates a new phone book. Can't be persisted unless either the phoneBookFile or the ownerAID is set.
	 * @param doPersist indicates if the phone book should be stored to a file
	 */
	public PhoneBook(boolean doPersist) {
		this.doPersist = doPersist;
	}
	
	/**
	 * Instantiates a new phone book, that is persisted in the specified file.
	 * @param phoneBookFile the phone book file
	 */
	public PhoneBook(File phoneBookFile) {
		this(phoneBookFile, true);
	}
	
	/**
	 * Instantiates a new phone book, that is persisted in the specified file.
	 * @param phoneBookFile the phone book file
	 * @param doPersist indicates if the phone book should be stored to a file
	 */
	public PhoneBook(File phoneBookFile, boolean doPersist) {
		this.doPersist = doPersist;
		this.phoneBookFile = phoneBookFile;
	}
	
	/**
	 * Instantiates a new phone book, that is persisted in the working directory of the specified agent.
	 * @param ownerAID the agent owning this phone book
	 */
	public PhoneBook(AID ownerAID) {
		this(ownerAID, true);
	}

	/**
	 * Instantiates a new phone book, that is persisted in the working directory of the specified agent.
	 * @param ownerAID the owner AID
	 * @param doPersist indicates if the phone book should be stored to a file
	 */
	public PhoneBook(AID ownerAID, boolean doPersist) {
		this.doPersist = doPersist;
		this.ownerAID = ownerAID;
	}

	/**
	 * Gets the phone book content.
	 * @return the phone book content
	 */
	private TreeMap<String, AbstractPhoneBookEntry> getPhoneBookContent() {
		if (phoneBookContent==null) {
			phoneBookContent = new TreeMap<String, AbstractPhoneBookEntry>();
		}
		return phoneBookContent;
	}
	
	/**
	 * Adds the provided entry to the phonebook if it is valid. The phonebook will be stored afterwards.
	 * @param entry the entry
	 * @return true, if successful
	 */
	public boolean addEntry(AbstractPhoneBookEntry entry) {
		return this.addEntry(entry, true);
	}
	
	/**
	 * Adds the provided entry to the phone book if it is valid.
	 * @param entry the entry
	 * @param persist specifies if the phonebook will be saved afterwards.
	 * @return true, if successful
	 */
	public boolean addEntry(AbstractPhoneBookEntry entry, boolean persist) {
		if (entry.isValid()) {
			this.getPhoneBookContent().put(entry.getLocalName(), entry);
			if (persist==true) {
				this.save();
			}
			this.notifyAdded(entry);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds multiple entries to the phone book.
	 * @param entries the entries
	 * @return true, if successful
	 */
	public boolean addEntries(List<? extends AbstractPhoneBookEntry> entries) {
		// --- Pause notifications ------------------------
		this.enableNotifications = false;
		for (int i=0; i<entries.size(); i++) {
			// --- Don't save after every single entry ----
			this.addEntry(entries.get(i), false);
		}
		
		// --- Save after adding all entries --------------
		this.save();
		
		// --- Send one notification for all entries
		this.enableNotifications = true;
		this.notifyAdded(entries);
		
		
		return true;
	}
	
	/**
	 * Gets the entry with the specified identifier.
	 * @param identifier the identifier
	 * @return the entry
	 */
	public AbstractPhoneBookEntry getEntry(String identifier) {
		return this.getPhoneBookContent().get(identifier);
	}
	
	/**
	 * Gets all phone book entries.
	 * @return the entries
	 */
	public ArrayList<AbstractPhoneBookEntry> getEntries(){
		return new ArrayList<>(this.getPhoneBookContent().values());
	}
	/**
	 * Gets all phone book entries that match the specified filter.
	 * @param searchFilter the search filter
	 * @return the entries
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<AbstractPhoneBookEntry> getEntries(@SuppressWarnings("rawtypes") PhoneBookSearchFilter searchFilter){
		ArrayList<AbstractPhoneBookEntry> resultList = new ArrayList<>();
		for (AbstractPhoneBookEntry entry : this.getPhoneBookContent().values()) {
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
		AbstractPhoneBookEntry entryToRemove = this.getEntry(identifier);
		if (entryToRemove!=null) {
			this.removeEntry(entryToRemove, true);
		}
	}
	
	/**
	 * Removes the specified entry from the phonebook. The phonebook will be stored afterwards.
	 * @param entry the entry to be removed
	 */
	public void removeEntry(AbstractPhoneBookEntry entry) {
		this.removeEntry(entry, true);
	}
	
	/**
	 * Removes the specified entry from the phonebook.
	 * @param entry the entry to be removed
	 * @param persist specifies if the phone book will be saved afterwards
	 */
	public void removeEntry(AbstractPhoneBookEntry entry, boolean persist) {
		this.getPhoneBookContent().remove(entry.getLocalName());
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
	@SuppressWarnings("unchecked")
	public ArrayList<? extends AbstractPhoneBookEntry> searchEntries(@SuppressWarnings("rawtypes") PhoneBookSearchFilter searchFilter){
		ArrayList<AbstractPhoneBookEntry> resultList = new ArrayList<AbstractPhoneBookEntry>();
		for (AbstractPhoneBookEntry entry : this.getPhoneBookContent().values()) {
			if (searchFilter.matches(entry)) {
				resultList.add(entry);
			}
		}
		return resultList;
	}
	
	/**
	 * Gets the phone book entry for the agent with the specified AID.
	 * @param aid the aid
	 * @return the phone book entry
	 */
	public AbstractPhoneBookEntry getEntryForAID(AID aid) {
		return this.getPhoneBookContent().get(aid.getLocalName());
	}
	
	/**
	 * Gets the phone book entry for the agent with the specified local name.
	 * @param localName the local name
	 * @return the phone book entry
	 */
	public AbstractPhoneBookEntry getEntryForLocalName(String localName) {
		return this.getPhoneBookContent().get(localName);
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
	 * Checks if is do persist.
	 * @return true, if is do persist
	 */
	public boolean isDoPersist() {
		return doPersist;
	}
	/**
	 * Sets the do persist.
	 * @param doPersist the new do persist
	 */
	public void setDoPersist(boolean doPersist) {
		this.doPersist = doPersist;
	}

	/**
	 * Saves this phone book instance to an XML file.
	 */
	public void save() {
		if (this.doPersist == true && this.getPhoneBookFile()!=null) {
			saveAs(this, this.getPhoneBookFile());
		}
	}
	
	/**
	 * Saves the provided phone book to the specified file
	 * @param <T> the generic type
	 * @param phoneBook the phone book
	 * @param phoneBookFile the phone book file
	 * @return true, if successful
	 */
	public static boolean saveAs(PhoneBook phoneBook, File phoneBookFile) {
		
		boolean successful = false;
		if (phoneBookFile!=null) {

			FileWriter fileWriter = null;
			try {
				// --- Save the PhoneBook to file ---------
				Class<?> phoneBookClass = phoneBook.getClass();
				//TODO add method to find all classes of entries
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
	 *
	 * @param phoneBookFile the phone book file
	 * @param phoneBookEntryClassInstance the phone book entry class instance
	 * @return the phone book
	 */
	public static PhoneBook loadPhoneBook(File phoneBookFile, Class<?> phoneBookEntryClassInstance) {
		
		PhoneBook pb = null;
		if (phoneBookFile.exists()) {
			// --- Check if the directory is available ----
			// --- Load the PhoneBook from file -------
			FileReader fileReader = null;
			try {
				fileReader = new FileReader(phoneBookFile);
				JAXBContext pbc = JAXBContext.newInstance(PhoneBook.class, phoneBookEntryClassInstance);
				Unmarshaller um = pbc.createUnmarshaller();
				pb = (PhoneBook) um.unmarshal(fileReader);
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
	private Vector<PhoneBookListener> getPhoneBookListeners() {
		if (phoneBookListeners==null) {
			phoneBookListeners = new Vector<>();
		}
		return phoneBookListeners;
	}
	
	/**
	 * Notifies the registered listeners about an added phone book entry.
	 * @param addedEntry the added entry
	 */
	private void notifyAdded(AbstractPhoneBookEntry addedEntry) {
		ArrayList<AbstractPhoneBookEntry> entriesList = new ArrayList<>();
		entriesList.add(addedEntry);
		this.notifyAdded(entriesList);
	}
	
	/**
	 * Notifies the registered listeners about added phone book entries.
	 * @param addedEntries the added entries
	 */
	private void notifyAdded(List<? extends AbstractPhoneBookEntry> addedEntries) {
		if (this.enableNotifications==true) {
			PhoneBookEvent addedEvent = new PhoneBookEvent(PhoneBookEvent.Type.ENTRIES_ADDED, addedEntries);
			for (PhoneBookListener listener : this.getPhoneBookListeners()) {
				listener.handlePhoneBookEvent(addedEvent);
			}
		}
	}
	
	/**
	 * Notifies the registered listeners about a removed phone book entry.
	 * @param removedEntry the removed entry
	 */
	private void notifyRemoved(AbstractPhoneBookEntry removedEntry) {
		ArrayList<AbstractPhoneBookEntry> entriesList = new ArrayList<>();
		entriesList.add(removedEntry);
		this.notifyRemoved(entriesList);
	}
	
	/**
	 * Notifies the registered listeners about removed phone book entries.
	 * @param removedEntries the removed entries
	 */
	private void notifyRemoved(List<AbstractPhoneBookEntry> removedEntries) {
		if (this.enableNotifications==true) {
			PhoneBookEvent removedEvent = new PhoneBookEvent(PhoneBookEvent.Type.ENTRIES_REMOVED, removedEntries);
			for (PhoneBookListener listener : this.getPhoneBookListeners()) {
				listener.handlePhoneBookEvent(removedEvent);
			}
		}
	}
	
	
	
	
	/**
	 * Adds a phone book listener.
	 * @param listener the listener
	 */
	public void addPhoneBookListener(PhoneBookListener listener) {
		if (this.getPhoneBookListeners().contains(listener)==false) {
			this.getPhoneBookListeners().add(listener);
		}
	}
	
	/**
	 * Removes a phone book listener.
	 * @param listener the listener
	 */
	public void removePhoneBookListener(PhoneBookListener listener) {
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
		for (AbstractPhoneBookEntry entry : this.getPhoneBookContent().values()) {
			if (entry.getAgentAID().getLocalName().equals(localName)) {
				return entry.getAgentAID();
			}
		}
		
		return null;
	}
}
