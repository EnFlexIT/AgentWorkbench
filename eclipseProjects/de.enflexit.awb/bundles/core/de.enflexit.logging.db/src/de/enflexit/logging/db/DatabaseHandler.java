package de.enflexit.logging.db;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import de.enflexit.logging.db.dataModel.LoggingEvent;



/**
 * The Class ExampleDatabaseHandler can be used to .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class DatabaseHandler {
	
	private Session session;

	/**
	 * Instantiates a new database handler.
	 */
	public DatabaseHandler() { }
	/**
	 * Instantiates a new database handler.
	 * @param session the session instance to use
	 */
	public DatabaseHandler(Session session) {
		this.setSession(session);
	}
	
	/**
	 * Returns the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		if (session==null) {
			session = LoggingDatabaseConnectionService.getInstance().getNewDatabaseSession();
		}
		return session;
	}
	/**
	 * Sets the current session instance.
	 * @param session the new session
	 */
	public void setSession(Session session) {
		if (this.session!=null) {
			if (session==null) {
				this.session.close();
			} else {
				if (this.session!=session) {
					this.session.close();
				}
			}
		}
		this.session = session;
	}
	/**
	 * Disposes this database handler by closing the database session.
	 */
	public void dispose() {
		this.setSession(null);
	}
	
	/**
	 * Does a transaction roll back.
	 * @param transaction the transaction
	 */
	private void doTransactionRollBack(Transaction transaction) {
		
		try {
			if (transaction!=null) {
				transaction.rollback();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			// --- Dispose session to renew handler state - 
			this.dispose();
		}
	}

	/**
	 * Deletes the specified entity instance.
	 *
	 * @param <EntityInstance> the generic type
	 * @param entityInstance the entity instance
	 * @return true, if successful
	 */
	public <EntityInstance> boolean dbDeleteEntityInstance(EntityInstance entityInstance) {
		
		boolean successful = false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.remove(entityInstance);
				session.flush();
				transaction.commit();
				successful = true;
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
				successful = false;
			} finally {
				session.clear();
			}
		}
		return successful;
	}

	/**
	 * Returns the logging events in between from and to
	 *
	 * @param from the starting time
	 * @param to the ending time
	 * @return the logging events in between
	 */
	public List<LoggingEvent> getLoggingEventsInBetween(long from, long to) {

		Session session = this.getSession();
		if (session != null) {
			Transaction transaction = null;
			
			try {
				transaction = session.beginTransaction();
				Query<LoggingEvent> query = session.createQuery("from LoggingEvent e " + "where e.timestmp >= :from and e.timestmp < :to " + "order by e.timestmp", LoggingEvent.class);

				query.setParameter("from", from);
				query.setParameter("to", to);

				List<LoggingEvent> logs = query.list();
				transaction.commit();
				return logs;
				
			} catch (Exception ex) {
				transaction.rollback();
				ex.printStackTrace();
				
			} finally {
				session.clear();
			}
		}
		return null;
	}

	/**
	 * Checks if there are for logs in between the specified times.
	 *
	 * @param from the time from
	 * @param to the time to
	 * @return true, if successful
	 */
	public boolean hasLogsInBetween(long from, long to) {

		boolean hasLogsInBetween = false;
		Session session = this.getSession();
		if (session != null) {
			Transaction transaction = null;
			
			try {
				transaction = session.beginTransaction();
				
				// --- Original approach of Daniel --------------------------------------
//				String sql = "SELECT 1 FROM logging_event WHERE timestmp >= :from AND timestmp < :to";
//				NativeQuery<?> query = session.createNativeQuery(sql);
//				query.setParameter("from", from);
//				query.setParameter("to", to);
//				query.setMaxResults(1);
//				
//				List<?> result = query.getResultList();
//				hasLogsInBetween = result.size() > 0;
				
				// --- As alternative solution: will always return one result set -------
				String sql = "SELECT COUNT(*) FROM logging_event WHERE timestmp >= :from AND timestmp < :to";
				NativeQuery<Long> countQuery = session.createNativeQuery(sql, Long.class);
				countQuery.setParameter("from", from);
				countQuery.setParameter("to", to);
				Long nLogs = countQuery.getSingleResult();
				hasLogsInBetween = nLogs>0; 
				
				transaction.commit();

			} catch (Exception ex) {
				transaction.rollback();
				ex.printStackTrace();

			} finally {
				session.clear();
			}
		}
		return hasLogsInBetween;
	}
	
	/**
	 * Returns the oldest log date.
	 *
	 * @return the oldest log date
	 */
	public LocalDate getOldestLogDate() {
		Session session = this.getSession();
		if (session != null) {
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				Query<Long> query = session.createQuery("SELECT MIN(e.timestmp) FROM LoggingEvent e", Long.class);
				Long oldestTimestamp = query.getSingleResult();
				transaction.commit();
				return oldestTimestamp == null ? null : Instant.ofEpochMilli(oldestTimestamp).atZone(ZoneId.systemDefault()).toLocalDate();
				
			} catch (Exception ex) {
				transaction.rollback();
				ex.printStackTrace();
				
			} finally {
				session.clear();
			}
		}
		return null;
	}
	
	/**
	 * Returns the latest log date.
	 *
	 * @return the latest log date
	 */
	public LocalDate getLatestLogDate() {
		Session session = this.getSession();
		if (session != null) {
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				Query<Long> query = session.createQuery("SELECT MAX(e.timestmp) FROM LoggingEvent e", Long.class);
				Long latestTimestamp = query.getSingleResult();
				transaction.commit();
				if (latestTimestamp != null) {
					return Instant.ofEpochMilli(latestTimestamp).atZone(ZoneId.systemDefault()).toLocalDate();
				}
				
			} catch (Exception ex) {
				transaction.rollback();
				ex.printStackTrace();
				
			} finally {
				session.clear();
			}
		}
		return null;
	}
	
	/**
	 * Returns the clean up limit id.
	 *
	 * @param dayLimit the max amount of days an entry should be kept
	 * @return the clean up limit id
	 */
	public Long getCleanUpLimitId(int dayLimit) {

		Session session = this.getSession();
		if (session != null) {
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				long timeStampLimit = System.currentTimeMillis() - Duration.of(dayLimit, ChronoUnit.DAYS).toMillis();
				Query<Long> query = session.createQuery("SELECT MAX(e.eventId) FROM LoggingEvent e WHERE e.timestmp < :timeStampLimit", Long.class);
				query.setParameter("timeStampLimit", timeStampLimit);
				Long limitIdByDays = query.getSingleResult();
				transaction.commit();
				return limitIdByDays;

			} catch (Exception ex) {
				transaction.rollback();
				ex.printStackTrace();
				
			} finally {
				session.clear();
			}
		}
		return null;
	}
	
	
	/**
	 * Deletes all entries where event_id <= limitId
	 *
	 * @param limitId the limit id
	 * @return true, if successful
	 */
	public boolean cleanUpByLimitId(Long limitId) {
		
		if (limitId == null || limitId == 0) return true;
		
		boolean successful = false;
		Session session = this.getSession();
		if (session != null) {
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				
				// --- Delete from child tables first ---------------------------------------------
				MutationQuery query = session.createMutationQuery("DELETE FROM loggingEventException WHERE  event_id <= :limitId");
				query.setParameter("limitId", limitId);
				query.executeUpdate();
				
				query = session.createMutationQuery("DELETE FROM properties WHERE  eventId <= :limitId");
				query.setParameter("limitId", limitId);
				query.executeUpdate();
				
				query = session.createMutationQuery("DELETE FROM LoggingEvent e WHERE e.eventId <= :limitId");
				query.setParameter("limitId", limitId);
				query.executeUpdate();
				
				transaction.commit();
				successful = true;
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				successful = false;
				ex.printStackTrace();
				
			} finally {
				session.clear();
			}
		}
		return successful;
	}
	
}