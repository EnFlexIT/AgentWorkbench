package database;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

import application.Application;
import application.Language;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;


public class DBConnection {
	
	private String newLine = Application.RunInfo.AppNewLineString();
	private String sql = null;
		
	public boolean hasErrors = false;
	
	private Connection connection = null;
	public Error dbError = new Error();
	
	private String dbHost = null;
	private String dbName = null;
	private String dbUser = null;
	private String dbPswd = null;
	
	/**
	 * Constructor of this class
	 */
	public DBConnection() {
		
		if (this.connect()==false) {
			hasErrors=true;
			return;
		}
		if (this.dbIsAvailable()) {
			this.setConnection2Database(dbName);
		} else {
			if (this.dbCreate()==false) {
				hasErrors = true;
				return;	
			}
		}
	}
	
	/**
	 * Creates the required Database for the server.master-Agent
	 * @return
	 */
	private boolean dbCreate(){
		
		String msg = ""; 
		msg += "Die Datenbank für den Server-Master ist nicht vorhanden." + newLine;
		msg += "Soll versucht werden diese nun anzulegen?"; 
		this.dbError.setText( msg );
		this.dbError.setHead( "Datenbank nicht vorhanden!" );
		this.dbError.setErrNumber( -1 );
		this.dbError.setErr(true);
		if ( this.dbError.showQuestion() == JOptionPane.NO_OPTION ) return false;	
		
		
		// ---------------------------------------------------------------------------------
		// --- Hier steht die Standard-Konfiguration der Datenbank für den Server-Master ---
		// ---------------------------------------------------------------------------------
		System.out.println( Language.translate("Erzeuge die Datenbank für den Server-Master ... ") );

		// --- Create DB --------------------------
		sql = "CREATE DATABASE " + dbName + " DEFAULT CHARACTER SET utf8";
		if ( this.getSqlExecuteUpdate(sql) == false ) return false;
		
		// --- Set Connection to Database ---------
		this.setConnection2Database(dbName);
		
		// --- Create TB 'server' -----------------
		sql  = "CREATE TABLE platforms (" +
				"id_platform int(11) NOT NULL AUTO_INCREMENT," +
				"contact_agent varchar(255) CHARACTER SET utf8 NOT NULL," +
				"platform_name varchar(255) CHARACTER SET utf8 NOT NULL," +
				"is_server tinyint(4)," +
				"ip varchar(50) CHARACTER SET utf8," +
				"url varchar(255) CHARACTER SET utf8," +
				"jade_port int(11)," +
				"http4mtp varchar(255) CHARACTER SET utf8," +
				"cpu_vendor varchar(255)," +
				"cpu_model varchar(255)," +
				"cpu_n int(3)," +
				"cpu_speed_mhz int(11)," +
				"memory_total_mb int(11)," +
				"online_since DATETIME," +
				"last_contact_at DATETIME," +
				"local_online_since DATETIME," +
				"local_last_contact_at DATETIME," +
				"currently_available tinyint(4)," +
			   "PRIMARY KEY (id_platform)," +
			   "UNIQUE KEY contact_agent (contact_agent) " +
			   ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Agent.GUI available platforms'";
		if ( this.getSqlExecuteUpdate(sql) == false ) return false;
		
		// ---------------------------------------------------------------------------------
		System.out.println( Language.translate("Datenbank erfolgreich angelegt!") );
		return true;
		
	}
	
	/**
	 * Checks if the required database is available
	 * @return
	 */
	private boolean dbIsAvailable() {

		String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + dbName + "'";
		ResultSet res = getSqlResult4ExecuteQuery(sql);
		if (res==null) {
			return false;
		} else {
			try {
				res.last();
				if ( res.getRow()==0 ) {
					return false;
				} else {
					return true;
				}				
			} catch (SQLException e) {
				e.printStackTrace();
				this.dbError.setErrNumber( e.getErrorCode() );
				this.dbError.setHead( "Fehler beim Auslesen der Datensätzen!" );
				this.dbError.setText( e.getLocalizedMessage() );
				this.dbError.setErr(true);
				this.dbError.show();
				return false;
			}
		}
	}
	
	/**
	 * Does the setCatalog-method for the current connection
	 * @param database
	 */
	private void setConnection2Database(String database) {
		
		try {
			connection.setCatalog(database);
		} catch (SQLException e) {
			e.printStackTrace();
			this.dbError.setText(e.getLocalizedMessage());
			this.dbError.setErrNumber( e.getErrorCode() );
			this.dbError.setHead( "Fehler bei der Ausführung von 'executeUpdate'!" );
			this.dbError.setErr(true);
			this.dbError.show();
		}
		
	}

	/**
	 * This Method executes a SQL-Statement (Create, Insert, Update)in
	 * the database and returns true if this was successful 
	 * @param sqlStmt
	 * @return
	 */
	public boolean getSqlExecuteUpdate(String sqlStmt) {
		
		Statement state = getNewStatement();
		if (state!=null) {
			try {
				state.executeUpdate(sqlStmt);
				return true;
			} catch (SQLException e) {
				//e.printStackTrace();
				String msg = e.getLocalizedMessage() + newLine;
				msg += sqlStmt;
				this.dbError.setText(msg);
				this.dbError.put2Clipboard(sqlStmt);
				this.dbError.setErrNumber( e.getErrorCode() );
				this.dbError.setHead( "Fehler bei der Ausführung von 'executeUpdate'!" );
				this.dbError.setErr(true);
				this.dbError.show();
			}
		}
		return false;
	}
	
	/**
	 * Returns a ResultSet - Object for a SQL-Statement
	 * @param sqlStmt
	 * @return com.mysql.jdbc.ResultSet
	 */
	public ResultSet getSqlResult4ExecuteQuery(String sqlStmt) {
		
		ResultSet res = null;
		Statement state = this.getNewStatement();
		if (state!=null) {
			try {
				res = (ResultSet) state.executeQuery(sqlStmt);
			} catch (SQLException e) {
				//e.printStackTrace();
				String msg = e.getLocalizedMessage() + newLine;
				msg += sqlStmt;
				this.dbError.setText(msg);
				this.dbError.put2Clipboard(sqlStmt);
				this.dbError.setErrNumber( e.getErrorCode() );
				this.dbError.setHead( "Fehler bei der Ausführung von 'executeQuery'!" );
				this.dbError.setErr(true);
				this.dbError.show();
			}
		}		
		return res;
		
	}
	
	/**
	 * This method returns a new Statement-Onject for a further
	 * handling of SQL-Interaction (e. g. for an executeUpdate)
	 * @return com.mysql.jdbc.Statement
	 */
	public Statement getNewStatement() {
		try {
			return (Statement) connection.createStatement();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println( "Code: " + e.getErrorCode() );
			this.dbError.setErrNumber( e.getErrorCode() );
			this.dbError.setHead( "Fehler beim Erzeugen einer neuen Statement-Instanz!" );
			this.dbError.setText( e.getLocalizedMessage() );
			this.dbError.setErr(true);
			this.dbError.show();
			return null;
		}
	}
	
	/**
	 * This method initializes the Connection to the Database-Server
	 * defined in the Application-Options
	 * @return
	 */
	private boolean connect() {
		
		String configuredHost = Application.RunInfo.getServerMasterDBHost();
		if (configuredHost==null) {
			
			String msg = ""; 
			msg += "Fehlende Angaben über den Datenbank-Host." + newLine;
			msg += "Bitte überprüfen Sie die Datenbank-Einstellungen!";
			this.dbError.setText( msg );			
			this.dbError.setHead( "Fehler in der Datenbank-Konfiguration!" );
			this.dbError.setErrNumber( -1 );
			this.dbError.setErr(true);
			this.dbError.show();			
			return false;
		}

		dbName = Application.RunInfo.getServerMasterDBName();
		dbUser = Application.RunInfo.getServerMasterDBUser();
		dbPswd = Application.RunInfo.getServerMasterDBPswd(); 
		if ( configuredHost.contains(":") ) {
			// --- Port wurde explizit angegeben --------------------
			dbHost = "jdbc:mysql://" + configuredHost + "/";
		} else {
			// --- Port wurde nicht angegeben, Standard verwenden ---
			dbHost = "jdbc:mysql://" + configuredHost + ":3306/";
		}
		
		try {
			
			Properties props = new Properties();
			props.setProperty("user", dbUser);
			props.setProperty("password", dbPswd);
			props.setProperty("useUnicode","true");
			props.setProperty("characterEncoding","UTF-8");
			props.setProperty("connectionCollation","utf8_general_ci");
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = (Connection) DriverManager.getConnection(dbHost, props);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			this.dbError.setText( e.getLocalizedMessage() );
			this.dbError.setErr(true);
		} catch (InstantiationException e) {
			e.printStackTrace();
			this.dbError.setText( e.getLocalizedMessage() );
			this.dbError.setErr(true);
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			this.dbError.setText( e.getLocalizedMessage() );
			this.dbError.setErr(true);
		} catch (SQLException e) {
			//e.printStackTrace();
			this.dbError.setErrNumber( e.getErrorCode() );
			this.dbError.setText( e.getLocalizedMessage() );
			this.dbError.setErr(true);
		}
		
		// --- Show Error if there ----------------------------------
		if (this.dbError.isErr()) {
			this.dbError.setHead( "Fehler beim Verbindungsaufbau mit Datenbank-Server!" );
			this.dbError.show();
			return false;
		}
		return true;		
	}
	
	// ------------------------------
	// --- Start Sub-Class ----------
	// ------------------------------
	/**
	 * This Sub-Class handels the Errors which ocur during the SQL-Interactions 
	 */
	public class Error {
		
		private Integer errNumber = 0;
		private String errHead = null;
		private String errText = null;
		private boolean err = false;
		
		private String msg = "";
		
		public Error () {
			//------
		}
		
		/**
		 * Sets the Message which will be shown in an Error-Dialog
		 */
		private void setMessage() {
			
			if (errNumber.equals(0) ) {
				// --- NICHT-SQL-Fehler [System] ----------
				msg  = errText + newLine;
			} else  if (errNumber.equals(-1) ) {
				// --- NICHT-SQL-Fehler [own] -------------
				msg  = Language.translate(errText) + newLine;
			} else {
				// --- SQL-Fehler -------------------------
				msg  = Language.translate("MySQL-Fehler ") + errNumber + ":" + newLine;
				msg += errText;				
				msg = formatText(msg);				
			}		
			errHead = Language.translate(errHead);
		}
		/**
		 * shows the current Error-Message an OptionPane-MessageDialog
		 */
		public void show() {
			this.setMessage();
			JOptionPane.showMessageDialog(null, msg, errHead, JOptionPane.OK_OPTION);
			this.resetError();
		}
		/**
		 * shows the current Message and returns the User-Answer
		 * @return int
		 */
		public int showQuestion() {
			this.setMessage();
			int answer = JOptionPane.showConfirmDialog(null, msg, errHead, JOptionPane.YES_NO_OPTION); 
			this.resetError();
			return answer;			
		}	
		/**
		 * Thie Method resets the current Err-Object to a non-Error State
		 */
		private void resetError(){
			errNumber = 0;
			errHead = null;
			errText = null;
			err = false;
			msg = "";
		}
		/**
		 * Here a Error-Numer (for SQL) can be set
		 * @param newErrNumber
		 */
		public void setErrNumber(Integer newErrNumber) {
			errNumber = newErrNumber;
		}
		/**
		 * Here the Dialog-Title of the JOptionPane can be set
		 * @param newErrHead
		 */
		public void setHead(String newErrHead) {
			errHead = newErrHead;
		}
		/**
		 * Here, the Text inside of the JOptionPane can be set 
		 * @param newErrText
		 */
		public void setText(String newErrText) {
			errText = newErrText;
		}
		/**
		 * Here, an indicator for an error can be set 
		 * @param err
		 */
		public void setErr(boolean err) {
			this.err = err;
		}
		/**
		 * This method return, if there is an Error or Not
		 * @return boolean
		 */
		public boolean isErr() {
			return err;
		}
		/**
		 * This method puts the value of 'toClipboard' to the Clipboard
		 * In case of an SQL-Error, the SQL-Statement will be placed in
		 * such a way and can be used in an external application.
		 * @param toClipboard
		 */
		public void put2Clipboard(String toClipboard) {
			StringSelection data = new StringSelection(toClipboard);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data, data);
		}
		/**
		 * In case of an SQL-Error the Text can be very long.
		 * This Method checks a Text and will reduce the width
		 * of the Text (shown in the JOptionpane) to a maximum
		 * width of 100 characters by adding line breaks. 
		 * @param currText
		 * @return
		 */
		private String formatText(String currText) {
			
			String newTextArr[] = currText.split(" ");
			String newText = "";
			String line = "";		
			
			for (int i = 0; i < newTextArr.length; i++) {
				line += newTextArr[i] + " ";
				if ( line.length() >= 100  ) {
					newText += line + newLine;
					line = "";
				}
			}
			newText += line;
			return newText;
			
		}
	}
	// ------------------------------
	// --- End Sub-Class ------------
	// ------------------------------
	
	
	/**
	 * This method converts an DB-Integer to a Java-boolean
	 */
	public boolean dbInteger2Bool(Integer intValue) {
		if (intValue.equals(0)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * This method converts an DB-boolean to an Java-Integer
	 * @param booleanValue
	 * @return
	 */
	public Integer dbBool2Integer(boolean booleanValue) {
		if (booleanValue==true) {
			return -1;
		} else {
			return 0;
		}
	}
	
	
}
