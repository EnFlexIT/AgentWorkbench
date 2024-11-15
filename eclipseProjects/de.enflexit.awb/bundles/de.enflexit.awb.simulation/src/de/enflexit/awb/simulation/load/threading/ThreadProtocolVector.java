package de.enflexit.awb.simulation.load.threading;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * The Class ThreadProtocolVector is used to handle several {@link ThreadProtocol} instances.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
@XmlRootElement
public class ThreadProtocolVector extends Vector<ThreadProtocol> {

	private static final long serialVersionUID = -6007682527796979437L;
	
	/** The table model. */
	private DefaultTableModel tableModel;
	
	/**
	 * Instantiates a new thread protocol vector.
	 */
	public ThreadProtocolVector() {
		super();
	}
	
	/**
	 * Gets the table model for this {@link ThreadProtocolVector}.
	 * @return the table model
	 */
	public DefaultTableModel getTableModel() {
		
		if (tableModel==null) {
			
			Vector<String> header = new Vector<String>();
			header.add("PID");
			header.add("Thread Name");
			header.add("Class");
			header.add("System Time [ms]");
			header.add("User Time [ms]");
			
			tableModel = new DefaultTableModel(null, header){

				private static final long serialVersionUID = 1L;

				/* (non-Javadoc)
				 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
				 */
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int column){
					if (column >= 0 && column <= getColumnCount()) {
						return getValueAt(0, column).getClass();
					} else {
						return Object.class;
					}
				}
			};
			// --- Necessary for preventing sorter from throwing error about empty row
			addTableModelRow("", null);
		}
		return tableModel;
	}
	
	
	/**
	 * Adds a table model row.
	 * @param pid the process ID
	 * @param threadDetail the thread time
	 */
	private void addTableModelRow(String pid, ThreadDetail threadDetail) {
		
		if (threadDetail == null) {
			threadDetail = new ThreadDetail();
		}

		// --- Set the class name entry -----------------------------
		String className = threadDetail.getClassName();
		if (className.equals(ThreadDetail.UNKNOWN_THREAD_CLASSNAME) || className.equals(ThreadDetail.UNKNOWN_AGENT_CLASSNAME)) {
			String[] classNameSplitArray = className.split("\\.");
			className = classNameSplitArray[classNameSplitArray.length-1];
		}
		
		// --- Create row vector ------------------------------------
		Vector<Object> row = new Vector<Object>();
		row.add(pid);
		row.add(threadDetail);
		row.add(className);
		row.add(threadDetail.getSystemTime());
		row.add(threadDetail.getUserTime());
		
		// --- Add row to table model -------------------------------
		this.getTableModel().addRow(row);
	}
	
	/**
	 * Clears the table model.
	 */
	private void clearTableModel() {
		while (this.getTableModel().getRowCount()>0) {
			this.getTableModel().removeRow(0);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(ThreadProtocol threadProtocol) {
		// --- Add to the local vector ------------------------------
		boolean done = super.add(threadProtocol);
		// --- Add the new Thread Times to the table model ----------
		String pid = threadProtocol.getProcessID();
		for (int i = 0; i < threadProtocol.getThreadDetails().size(); i++) {
			this.addTableModelRow(pid, threadProtocol.getThreadDetails().get(i));
		}
		return done;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		this.clearTableModel();
	}
	
	/**
	 * Returns the current time stamp of this protocol vector.
	 * @return the time stamp
	 */
	public long getTimestamp() {
		if (this.size()==0) {
			return 0; 
		} else {
			return this.get(0).getTimestamp();
		}
	}
}
