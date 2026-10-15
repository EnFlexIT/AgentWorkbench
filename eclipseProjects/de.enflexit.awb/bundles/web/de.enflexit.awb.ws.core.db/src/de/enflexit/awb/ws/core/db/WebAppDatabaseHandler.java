
package de.enflexit.awb.ws.core.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.ws.core.db.dataModel.JettySession;
import de.enflexit.awb.ws.core.db.dataModel.SiteContent;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenu;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenuContent;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenuTranslation;
import de.enflexit.awb.ws.core.exceptions.DatabaseException;
import de.enflexit.db.userManagement.UserManagementDatabaseHandler;
import de.enflexit.db.userManagement.dataModel.GroupRole;
import jakarta.persistence.TypedQuery;


/**
 * The Class WebAppDatabaseHandler can be used to .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class WebAppDatabaseHandler {
	
	private Session session;
	private UserManagementDatabaseHandler umDbHandler;
	
	private static Logger LOGGER = LoggerFactory.getLogger(WebAppDatabaseHandler.class);
	
	
	/**
	 * Instantiates a new database handler.
	 */
	public WebAppDatabaseHandler() { }
	/**
	 * Instantiates a new database handler.
	 * @param session the session instance to use
	 */
	public WebAppDatabaseHandler(Session session) {
		this.setSession(session);
	}
	
	/**
	 * Returns the current session instance.
	 * @return the session
	 */
	public Session getSession() {
		if (session==null) {
			session = WebAppDatabaseConnectionService.getInstance().getNewDatabaseSession();
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
		this.umDbHandler = null;
	}
	
	/**
	 * Returns the UserManagementDatabaseHandler.
	 * @return the user management database handler
	 */
	public UserManagementDatabaseHandler getUserManagementDatabaseHandler() {
		if (umDbHandler==null) {
			umDbHandler = new UserManagementDatabaseHandler(this.getSession());
		}
		return umDbHandler;
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

	// --------------------------------------------------------------
	// --- From here, generically working on concrete data ----------
	// --------------------------------------------------------------	
	/**
	 * Saves or updates the specified {@link entityInstance}.
	 * @param entityInstance the JettySession instance to save or update
	 */
	public <EntityInstance> boolean dbSaveOrUpdateEntityInstance(EntityInstance entityInstance) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.persist(entityInstance);
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
	 * Loads an entity instance by its ID from the database.
	 *
	 * @param <EntityClass> the generic entity instance to load
	 * @param entityClass the entity class
	 * @param entityID the entity ID
	 * @return the entity instance found in the database
	 */
	public <EntityInstance> EntityInstance dbLoadEntityInstance(Class<EntityInstance> entityClass, String entityID) {
		
		EntityInstance siteMenu =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				siteMenu = session.get(entityClass, entityID);
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return siteMenu;
	}
	
	/**
	 * Returns the list of all entity instance in the database table .
	 *
	 * @param <EntityInstance> the generic type
	 * @param entityClass the entity class
	 * @return the JettySession list
	 */
	public <EntityInstance> List<EntityInstance> dbLoadEntityInstanceList(Class<EntityInstance> entityClass) {
		return this.dbLoadEntityInstanceList("from " + entityClass.getSimpleName(), entityClass);
	}
	/**
	 * Returns the list of all entity instance in the database table.
	 *
	 * @param <EntityInstance> the generic type
	 * @param queryString the query string
	 * @param entityClass the entity class
	 * @return the JettySession list
	 */
	public <EntityInstance> List<EntityInstance> dbLoadEntityInstanceList(String queryString, Class<EntityInstance> entityClass) {
		
		List<EntityInstance> entityInstanceList =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				
				Query<EntityInstance> query = session.createQuery(queryString, entityClass);
				entityInstanceList = query.list();
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return entityInstanceList;
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
	
	
	// --------------------------------------------------------------
	// --- From here, working on concrete data ----------------------
	// --------------------------------------------------------------	
	/**
	 * Saves or updates the specified {@link BgSystemPlatform}.
	 * @param jettySession the JettySession instance to save or update
	 */
	public boolean saveOrUpdateJettySession(JettySession jettySession) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.persist(jettySession);
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
	 * Returns the JettySession with the specified contact agent.
	 * @param sessionId the session Id as String
	 * @return the JettySession found
	 */
	public JettySession getJettySession(String sessionId) {
		
		JettySession jettySession =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				jettySession = session.get(JettySession.class, sessionId);
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return jettySession;
	}
	
	/**
	 * Deletes the specified JettySession.
	 *
	 * @param jettySession the JettySession to delete
	 * @return true, if successful
	 */
	public boolean deleteJettySession(JettySession jettySession) {
		
		boolean successful = false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.remove(jettySession);
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
	 * Returns the list of all background system platforms.
	 * @return the JettySession list
	 */
	public List<JettySession> getJettySessionList() {
		
		List<JettySession> bgSysPlatformList =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				
				Query<JettySession> query = session.createQuery("from JettySessions", JettySession.class);
				bgSysPlatformList = query.list();
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return bgSysPlatformList;
	}
	
	/**
	 * Exchanges the list of {@link JettySession} in the database table by the specified list <br>
	 * (which is a 'clear table' + 'add all' action).
	 *
	 * @param jettySessionList the jetty session list
	 * @return true, if successful
	 */
	public boolean setJettySessionList(List<JettySession> jettySessionList) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				// --- Clear the table first ------------------------
				MutationQuery query = session.createMutationQuery("DELETE FROM JettySessions");
				query.executeUpdate();
				
				// --- Save all platforms out of the list -----------  
				for (int i = 0; i < jettySessionList.size(); i++) {
					session.persist(jettySessionList.get(i));
			        if (i % 100 == 0) {
			            session.flush();
			            session.clear();
			        }
				}
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

		
	// --------------------------------------------------------------------------------------------
	// --- Site Menu Operations -------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------

	/**
	 * Returns the menus which are visible for the specified roles. 
	 *
	 * @param userGroupRoleIds the user group role ids
	 * @param includePublicMenus true if menus without group restrictions should be included
	 * @return the menus for user roles
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws DatabaseException the database exception
	 */
	public List<SiteMenu> getMenusForUserRoles(Collection<Integer> userGroupRoleIds, boolean includePublicMenus) throws IOException, IllegalArgumentException, DatabaseException {
		
		if (userGroupRoleIds == null) {
			return Collections.emptyList();
		}
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}	
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			String sql;
			if (includePublicMenus == true) {
				sql = "SELECT DISTINCT sm.* " +
						"FROM site_menu sm " +
						"LEFT JOIN site_menu_group_roles smgr ON sm.id_site_menu = smgr.id_site_menu " +
						"WHERE smgr.id_group_role IN (:roleIds) " +
						"OR smgr.id_site_menu IS NULL " +
						"ORDER BY sm.position ASC";
			} else {
				
				sql = "SELECT DISTINCT sm.* " +
						"FROM site_menu sm " +
						"LEFT JOIN site_menu_group_roles smgr ON sm.id_site_menu = smgr.id_site_menu " +
						"WHERE smgr.id_group_role IN (:roleIds) " 
						+ "ORDER BY sm.position ASC";
			}

			NativeQuery<SiteMenu> query = session.createNativeQuery(sql, SiteMenu.class);
	    	query.setParameter("roleIds", userGroupRoleIds);			
			List<SiteMenu> menusForUser = query.getResultList();
			transaction.commit();
			return menusForUser;
			
		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while loading menus.", ex);
			return Collections.emptyList();

		} finally {
			session.clear();
		}
	}
	
	/**
	 * Persist a new menu
	 *
	 * @param menu the menu to persist
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void persistNewMenu(SiteMenu menu, Set<GroupRole> requiredRolesForAccess) throws DatabaseException, IOException, IllegalArgumentException {

		if (menu == null || requiredRolesForAccess == null) {
			throw new IllegalArgumentException("Menu and requiredRolesForAccess cannot be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			// --- Stays null if there is no parent -----------------------------------------------
			Integer parentMenuId = null;
			SiteMenu parentMenu = null;
			
			if (Boolean.FALSE.equals(menu.isHeadMenu())) {
				// --- If the new menu is not a head menu, there has to be a parent menu ----------
				parentMenu = session.get(SiteMenu.class, menu.getParentMenu().getId());

				if (parentMenu == null)
					throw new IllegalArgumentException("The specified parent menu could not be found");
				parentMenuId = parentMenu.getId();
			}
			menu.setParentMenu(parentMenu);
			menu.setAllowedGroupRoles(requiredRolesForAccess);
			
			// --- Find the highest available position within the same parent ---------------------
			int lastPosition = this.getMenuCountInParent(session, parentMenuId) +1;
			
			// --- If the requested position is < 1 default to last position ----------------------
			int requestedPosition = menu.getPosition() < 1 ? lastPosition : menu.getPosition();
			
			// --- Actual position of new menu is the lower of requested and last position --------
		    int actualPosition = Math.min(requestedPosition, lastPosition);
		    menu.setPosition(actualPosition);
		    
		    // --- Adjust other menus position values if new menu is not inserted at the end ------
		    if (actualPosition < lastPosition) {
		    	this.makeRoomForInsertedMenu(session, parentMenuId, actualPosition);
		    }

			session.persist(menu);
			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while persisting new menu.", ex);

		} finally {
			session.clear();
		}
	}

	/**
	 * Enables to update parent and position of a menu, as well as caption and translations.
	 *
	 * @param updatedMenu the menu
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void updateMenu(SiteMenu updatedMenu, Integer menuId) throws DatabaseException, IOException, IllegalArgumentException {

		if (updatedMenu == null || menuId == null) {
			throw new IllegalArgumentException("Menu to update and menu ID can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			// --- Check if the specified menu exists ---------------------------------------------
			SiteMenu existingMenu = session.get(SiteMenu.class, menuId);
			if (existingMenu == null) {
				throw new IllegalArgumentException("The specified menu could not be found.");
			}

			Integer oldParentId = existingMenu.getParentMenu() == null ? null : existingMenu.getParentMenu().getId();
			Integer newParentId = updatedMenu.getParentMenu() == null ? null : updatedMenu.getParentMenu().getId();

			
			// --- Check if the parent and /or position are going to change -----------------------
			boolean parentChanged = Objects.equals(oldParentId, newParentId) == false;
			boolean positionChanged = Objects.equals(existingMenu.getPosition(), updatedMenu.getPosition()) == false;

			int actualPosition = updatedMenu.getPosition();
			
			// --- If position or parent is going to change, make sure the new position is valid --
			if (positionChanged == true || parentChanged == true) {
				// --- Highest available position is the amount of menus within the parent --------
				Integer lastPosition = this.getMenuCountInParent(session, newParentId);
				if (parentChanged == true) {
					// --- If parent is changing, add one to account for the new menu -------------
					lastPosition = lastPosition +1;
				}
				/*If the requested position is < 1 it is set to 1. If the requested position is
				 * higher than the last possible position, the latter is chosen. Else the requested
				 * position will be used. */
				actualPosition = Math.max(1, Math.min(updatedMenu.getPosition(), lastPosition));
				updatedMenu.setPosition(actualPosition);
			}
			
			if (parentChanged == true) {
				// --- close gap in old  parent menu and make room in the new one -----------------
				this.closeGapAfterRemovedMenu(session, oldParentId, existingMenu.getPosition());
				this.makeRoomForInsertedMenu(session, newParentId, actualPosition);

			} else if (positionChanged == true) {
				// --- Only need to reorder within parent menu ------------------------------------
				this.reorderMenusWithinSameParent(session, oldParentId, existingMenu.getPosition(), actualPosition);
			}
			existingMenu.setCaption(updatedMenu.getCaption());
			existingMenu.setPosition(actualPosition);
			existingMenu.setHeadMenu(newParentId == null);
			existingMenu.setTranslations(updatedMenu.getTranslations());
			
			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while updateing the menu.", ex);

		} finally {
			session.clear();
		}
	}

	/**
	 * Update the required role to access the specified menu. Updates all sub-menus recursively.
	 *
	 * @param menuId the menu id
	 * @param requiredRolesForAccess the required roles for access
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void updateMenuPermissions(Integer menuId, Set<GroupRole> requiredRolesForAccess) throws DatabaseException, IOException, IllegalArgumentException {
		
		if (menuId == null || requiredRolesForAccess == null) {
			throw new IllegalArgumentException("Menu ID and roles can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			// --- Try to get the requested menu --------------------------------------------------
			SiteMenu menuToUpdate = session.get(SiteMenu.class, menuId);
			if (menuToUpdate == null) {
				throw new IllegalArgumentException("The specified menu could not be found.");
			}
			
			// --- Set the new roles required to access it ----------------------------------------
			menuToUpdate.setAllowedGroupRoles(requiredRolesForAccess);
			
			session.persist(menuToUpdate);
			this.cascadePermissionsToSubMenus(session, menuToUpdate, requiredRolesForAccess);
			
			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while updateing menu permissions.", ex);

		} finally {
			session.clear();
		}
	}
	/**
	 * Cascade permissions to sub menus.
	 *
	 * @param session the session
	 * @param parentMenu the parent menu
	 * @param roles the roles
	 */
	private void cascadePermissionsToSubMenus(Session session, SiteMenu parentMenu, Set<GroupRole> roles) {
	   
		// --- No more sub menus to update --------------------------------------------------------
		if (parentMenu.getChildMenus() == null || parentMenu.getChildMenus().isEmpty()) {
	        return;
	    }
	    
		// --- Set the roles for every child menu -------------------------------------------------
	    for (SiteMenu subMenu : parentMenu.getChildMenus()) {
	        subMenu.setAllowedGroupRoles(roles);
	        session.persist(subMenu);
	        
	        // --- Recursive call for the child menus' sub menus ----------------------------------
	        cascadePermissionsToSubMenus(session, subMenu, roles);
	    }
	}
	
	/**
	 * Delete menu including sub menus by id.
	 *
	 * @param menuId the id of the menu to delete
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void deleteMenuIncludingSubMenusById(Integer menuId) throws DatabaseException, IOException, IllegalArgumentException {
	
		if (menuId == null) {
			throw new IllegalArgumentException("Menu ID can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			// --- Check if the specified menu exists ---------------------------------------------
			SiteMenu menuToDelete = session.get(SiteMenu.class, menuId);
			if (menuToDelete == null) {
				throw new IllegalArgumentException("The specified menu could not be found.");
			}
			// --- If the menu is a head menu, the parent is null ---------------------------------
			Integer parentMenuId = menuToDelete.getParentMenu() == null ? null : menuToDelete.getParentMenu().getId();

			// --- Recursively delete the menu and its sub menus ----------------------------------
			this.deleteMenuAndSubMenus(menuToDelete, session);
			
			// --- Flush the changes before closing the gap ---------------------------------------
			session.flush();

			this.closeGapAfterRemovedMenu(session, parentMenuId, menuToDelete.getPosition());
			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while deleting menu hierarchy from database.", ex);

		} finally {
			session.clear();
		}
	}
	/**
	 * Recursively deletes the menu and all of its sub menus, as well as their SiteMenuContent assignments.
	 * Resulting orphaned content will be removed as well.
	 *
	 * @param menu the menu
	 * @param session the session
	 */
	private void deleteMenuAndSubMenus(SiteMenu menu, Session session) {
		
		if (menu == null) return;
		
		// --- recursive call for all sub menus of the passed menu --------------------------------
		for (SiteMenu childMenu : menu.getChildMenus()) {
			this.deleteMenuAndSubMenus(childMenu, session);
		}
		
		if (menu.getSiteMenuContent() != null) {
			Iterator<SiteMenuContent> iterator = menu.getSiteMenuContent().iterator();
			while (iterator.hasNext()) {
				
				SiteMenuContent assignment = iterator.next();
				
				// --- Remember the associated content to remove if orphaned ---------------------
				SiteContent content = assignment.getSiteContent();

				// --- Remove the assignment on the menu's side -----------------------------------
				iterator.remove();
				
				if (content != null && content.getSiteMenuContent() != null) {
					// --- Remove the assignment on the content's side ----------------------------
	                content.getSiteMenuContent().remove(assignment); 
	            }
				session.remove(assignment);
				this.deleteContentIfOrphaned(session, content);
			}
		}
		// --- remove the menu itself -------------------------------------------------------------
		session.remove(menu);
	}

	/**
	 * Adds the translation to the menu.
	 *
	 * @param menuId the menu id
	 * @param locale the locale
	 * @param translatedCaption the translated caption
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void addTranslationToMenu(Integer menuId, String locale, String translatedCaption) throws DatabaseException, IOException, IllegalArgumentException {
		
		if (menuId  == null || locale == null || locale.isBlank() == true || translatedCaption == null) {
			throw new IllegalArgumentException("Menu Id, locale or translatedCaption are missing.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		
		Transaction transaction = null;
		try {
			
			transaction = session.beginTransaction();
			
			// --- Check if the specified menu existis --------------------------------------------
			SiteMenu menuToUpdate = session.get(SiteMenu.class, menuId);
			if (menuToUpdate == null) {
				throw new IllegalArgumentException("The specified menu could not be found.");
			}
			
			Set<SiteMenuTranslation> translations = menuToUpdate.getTranslations();
			SiteMenuTranslation targetTranslation = null;
			
			for (SiteMenuTranslation translation : translations) {
				if (translation.getLocale().equalsIgnoreCase(locale)) {
					// --- There is already a translation for the locale  -------------------------
					targetTranslation = translation;
				}
			}
			
			if (targetTranslation != null) {
				// --- Translation exists already, just update the caption ------------------------
				targetTranslation.setCaptionTranslated(translatedCaption);
				
			} else {
				// --- There is currently no translation for the locale, add a new one ------------
				targetTranslation = new SiteMenuTranslation();
				targetTranslation.setSiteMenu(menuToUpdate);
				targetTranslation.setLocale(locale);
				targetTranslation.setCaptionTranslated(translatedCaption);
				translations.add(targetTranslation);
				session.persist(targetTranslation);
			}
			
			transaction.commit();


		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while updateing translation.", ex);

		} finally {
			session.clear();
		}
	}		
	
	// --------------------------------------------------------------------------------------------
	// --- Site Content Operations ----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------
	
	/**
	 * Returns the content element corresponding to the specified ID.
	 *
	 * @param elementID the id of the requested content element
	 * @return the requested content element
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public SiteContent getContentElementById(Integer elementID) throws DatabaseException, IOException, IllegalArgumentException {

		if (elementID == null) {
			throw new IllegalArgumentException("Element ID can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			SiteContent requestedContentElement = session.get(SiteContent.class, elementID);

			if (requestedContentElement == null) {
				throw new IllegalArgumentException("The specified content element could not be found.");
			}
			transaction.commit();
			return requestedContentElement;

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while reading content element.", ex);
			return null;

		} finally {
			session.clear();
		}
	}
	
	/**
	 * Persist the given content. The generated ID can be obtained through the passed 
	 * siteContent object.
	 *
	 * @param siteContent the new content to persist
	 * @param menuId the id of the menu which the content is added to
	 * @param targetPosition the one based position of the new content within the menu
	 * @throws IllegalArgumentException thrown when the specified menu can't be found
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DatabaseException 
	 */
	public void persistNewContentElement(SiteContent siteContent, Integer menuId, Integer targetPosition) throws IllegalArgumentException, IOException, DatabaseException {

		Session session = this.getSession();
		if (session == null) {
	        throw new IOException("Could not obtain a valid database session.");
	    }
		
		if (menuId == null || siteContent == null || targetPosition == null) {
			throw new IllegalArgumentException("Menu ID, target position and site content can't be null.");
		}
		
		if (targetPosition < 1) {
			throw new IllegalArgumentException("Target position must be > 0.");
		}

		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			SiteMenu dbMenu = session.get(SiteMenu.class, menuId);
			// --- Throw exception if there is no menu for the id ---------------------------------
			if (dbMenu == null) {
				throw new IllegalArgumentException("Menu ID " + menuId + " does not exist.");
			}
			// --- Persist the new content --------------------------------------------------------
			session.persist(siteContent);

			// --- Determine the highest possible position by counting the content ----------------
			Query<Long> query = session.createQuery("SELECT COUNT(smc) from SiteMenuContent smc WHERE smc.siteMenu.id = :menuId", Long.class);
			query.setParameter("menuId", menuId);
			Long lastPosition = query.getSingleResult() + 1;

			// --- The actual position is the lower of last Position and target position --------
			int actualPosition = (int) Math.min(lastPosition, targetPosition);

			/*
			 * increase the position of all the menu's content with greater or equal
			 * positions by one to avoid duplicate position entries if the requested
			 * position is not the last available one (otherwise unnecessary)
			 */
			if (actualPosition < lastPosition) {
				this.makeRoomForInsertedContent(session, menuId, actualPosition);
			}

			// --- Prepare and persist the junction table -----------------------------------------
			SiteMenuContent siteMenuContent = new SiteMenuContent();
			siteMenuContent.setContentListPosition(targetPosition);
			siteMenuContent.setSiteContent(siteContent);
			siteMenuContent.setSiteMenu(dbMenu);
			session.persist(siteMenuContent);
			transaction.commit();
				
		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while persisting new content element.", ex);

			} finally {
				session.clear();
			}
	}

	/**
	 * Enables to update the actual content of SiteContent, not it's position or assignments
	 *
	 * @param elementID the element ID
	 * @param dbSiteContent the db site content
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void updateContent(Integer elementID, SiteContent dbSiteContent) throws DatabaseException, IOException, IllegalArgumentException {

		if (elementID == null || dbSiteContent == null) {
			throw new IllegalArgumentException("Element ID and site content can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			if (dbSiteContent != null && this.getContentElementById(elementID) != null) {
				session.merge(dbSiteContent);
				transaction.commit();
			} else {
				throw new IllegalArgumentException("Invalid content or id.");
			}

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while updating content element.", ex);

		} finally {
			session.clear();
		}
	}

	/**
	 * Delete content element by id.
	 *
	 * @param elementId the element id
	 * @throws IOException 
	 * @throws DatabaseException 
	 */
	public void deleteContentElementById(Integer elementId) throws IllegalArgumentException, IOException, DatabaseException {

		if (elementId == null) {
			throw new IllegalArgumentException("Element ID can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			SiteContent contentToDelete = session.get(SiteContent.class, elementId);
			if (contentToDelete == null) {
				throw new IllegalArgumentException("ElementID " + elementId + " does not exist.");
			}
			// --- shift the content elements by -1 in all menus the content is associated with ---
			for (SiteMenuContent smc : contentToDelete.getSiteMenuContent()) {
				this.closeGapAfterRemovedContent(session, smc.getSiteMenu().getId(), smc.getContentListPosition());
			}
			session.remove(contentToDelete);
			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while deleting content element.", ex);

		} finally {
			session.clear();
		}
	}

	// --------------------------------------------------------------------------------------------
	// --- Menu-Content Assignment Operations -----------------------------------------------------
	// --------------------------------------------------------------------------------------------
	
	/**
	 * Assigns existing content to another menu.
	 *
	 * @param menuId the id of the menu which the content will be assigned to
	 * @param elementId the id of the content element to assign
	 * @param targetPosition the one based position at which the content should appear in the menu
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void assignContentToMenu(Integer menuId, Integer elementId, Integer targetPosition) throws DatabaseException, IOException, IllegalArgumentException {

		if (elementId == null || menuId == null || targetPosition == null) {
			throw new IllegalArgumentException("Element ID, menu ID and target position can't be null.");
		}
		
		Session session = this.getSession();

		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			// --- Check if the specified menu and content exist ----------------------------------
			SiteMenu targetMenu = session.get(SiteMenu.class, menuId);
			SiteContent contentToAssign = session.get(SiteContent.class, elementId);

			if (targetMenu == null) {
				throw new IllegalArgumentException("Menu with ID " + menuId + " could not be found");
			}
			if (contentToAssign == null) {
				throw new IllegalArgumentException("Content element with ID " + elementId + " could not be found");
			}
			// --- Shift the existing content positions -------------------------------------------
			this.makeRoomForInsertedContent(session, menuId, targetPosition);

			// --- Create the new content - menu relation -----------------------------------------
			SiteMenuContent newAssignment = new SiteMenuContent();
			newAssignment.setContentListPosition(targetPosition);
			newAssignment.setSiteContent(contentToAssign);
			newAssignment.setSiteMenu(targetMenu);
			targetMenu.getSiteMenuContent().add(newAssignment);
			contentToAssign.getSiteMenuContent().add(newAssignment);

			session.persist(newAssignment);
			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while assigning content to menu.", ex);

		} finally {
			session.clear();
		}
	}
	
	/**
	 * Removes the assignment of.
	 *
	 * @param menuId the menu id
	 * @param elementId the element id
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void removeAssignmentOf(Integer menuId, Integer elementId) throws DatabaseException, IOException, IllegalArgumentException {

		if (elementId == null || menuId == null) {
			throw new IllegalArgumentException("Element ID and menu ID can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}

		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			// --- Check if the element exists ----------------------------------------------------
			SiteContent elementToBeDetached = session.get(SiteContent.class, elementId);
			if (elementToBeDetached == null) {
				throw new IllegalArgumentException("The specified element could not be found.");
			}

			Iterator<SiteMenuContent> iterator = elementToBeDetached.getSiteMenuContent().iterator();

			while (iterator.hasNext()) {
				
				SiteMenuContent entry = iterator.next();
				SiteContent content = entry.getSiteContent();
				
				// --- Find the SiteMenuContent with the specified menuId -------------------------
				if (entry.getSiteMenu().getId().equals(menuId)) {
					this.closeGapAfterRemovedContent(session, menuId, entry.getContentListPosition());
					// --- Remove the assignment from the menu's side
					iterator.remove();
					
					// --- Remove the assignment from the Content's side --------------------------
					if (content.getSiteMenuContent() != null) {
				        content.getSiteMenuContent().remove(entry);
				    }
					session.remove(entry);
				}
				// --- If the content is not assigned to any menu, delete it -----------------------
				this.deleteContentIfOrphaned(session, content);
			}
			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while removing content assignment.", ex);

		} finally {
			session.clear();
		}
	}
	
	/**
	 * Update menu assignment and position.
	 *
	 * @param elementId the id of the content element to be updated
	 * @param sourceMenuId the id of the menu which the content element should be updated in
	 * @param targetMenuId the id of the menu which the content element should be moved to
	 * @param targetPosition the position within the target menu which the content element should be inserted at
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void updateMenuAssignmentAndPosition(Integer elementId, Integer sourceMenuId, Integer targetMenuId, Integer targetPosition) throws DatabaseException, IOException, IllegalArgumentException {

		if (elementId == null || sourceMenuId == null || targetMenuId == null || targetPosition == null) {
			throw new IllegalArgumentException("Element ID, source menu ID, target menu ID and target position can't be null.");
		}
		
		Session session = this.getSession();
		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			SiteContent existingContent = session.get(SiteContent.class, elementId);
			// --- Check if the specified content does exist --------------------------------------
			if (existingContent == null) {
				throw new IllegalArgumentException("The specified content element could not be found.");
			}
			// --- Check if the specified source menu does exist ----------------------------------
			SiteMenu targetMenu = session.get(SiteMenu.class, targetMenuId);
			if (targetMenu == null) {
				throw new IllegalArgumentException("The target menu could not be found");
			}

			if (Objects.equals(sourceMenuId, targetMenuId) == false) {
				// --- Case 1: Menu is going to change --------------------------------------------

				Iterator<SiteMenuContent> iterator = existingContent.getSiteMenuContent().iterator();
				while (iterator.hasNext()) {
					SiteMenuContent entry = iterator.next();
					
					if (entry.getSiteMenu().getId().equals(sourceMenuId)) {
						
						// --- Fix the positions of remaining content in the source menu ----------
						this.closeGapAfterRemovedContent(session, sourceMenuId, entry.getContentListPosition());
						// --- Remove the contentElement - sourceMenu relation --------------------
						iterator.remove();
						session.remove(entry);
					}
				}
				session.flush();

				this.makeRoomForInsertedContent(session, targetMenuId, targetPosition);
				// --- Create the content - targetMenu relation -----------------------------------
				SiteMenuContent newAssignment = new SiteMenuContent();
				newAssignment.setContentListPosition(targetPosition);
				newAssignment.setSiteMenu(targetMenu);
				newAssignment.setSiteContent(existingContent);
				existingContent.getSiteMenuContent().add(newAssignment);

				session.persist(newAssignment);

			} else {

				// --- Case 2: Change position within the same menu -------------------------------
				for (SiteMenuContent smc : existingContent.getSiteMenuContent()) {
					if (smc.getSiteMenu().getId().equals(sourceMenuId)) {
						Integer startingPosition = smc.getContentListPosition();
						reorderContentWithinSameMenu(session, sourceMenuId, startingPosition, targetPosition);
						smc.setContentListPosition(targetPosition);
						break;
					}
				}
			}

			transaction.commit();

		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while updating menu assignment and position.", ex);

		} finally {
			session.clear();
		}
	}

	/**
	 * Returns the list of content for the specified menuId
	 *
	 * @param menuId the menu id
	 * @return the content list of menu id
	 * @throws DatabaseException the database exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public List<SiteContent> getContentListOfMenuId(Integer menuId) throws DatabaseException, IOException, IllegalArgumentException {

		if (menuId == null) {
			throw new IllegalArgumentException("Menu ID can't be null.");
		}
		
		Session session = this.getSession();

		if (session == null) {
			throw new IOException("Could not obtain a valid database session.");
		}
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			SiteMenu menuToLoad = session.get(SiteMenu.class, menuId);
			if (menuToLoad == null) {
				throw new IllegalArgumentException("Menu with id " + menuId + " could not be found");
			}
			// --- Get the menus' associated content and sort it by position ----------------------
			List<SiteMenuContent> smcList = new ArrayList<>(menuToLoad.getSiteMenuContent());
			smcList.sort(Comparator.comparing(SiteMenuContent::getContentListPosition));
			
			List<SiteContent> contentList = new ArrayList<>();
			
			for (SiteMenuContent smc : smcList) {
				if (smc.getSiteContent() != null) {
					contentList.add(smc.getSiteContent());
				}
			}
			
			transaction.commit();
			return contentList;
		} catch (Exception ex) {
			this.doTransactionRollBack(transaction);
			this.rethrowExpectedOrDatabaseException("Error while reading menu content list.", ex);
			return Collections.emptyList();
			
		} finally {
			session.clear();
		}
	}
	
	// --------------------------------------------------------------------------------------------
	// --- Internal Shifting & Position Helpers ---------------------------------------------------
	// --------------------------------------------------------------------------------------------

	/**
	 * Make room for inserted content.
	 *
	 * @param session the session
	 * @param menuID the menu ID
	 * @param insertPosition the target position
	 */
	private void makeRoomForInsertedContent(Session session, Integer menuID, Integer insertPosition) {
		this.shiftContentPositions(session, menuID , insertPosition, true, 1);
	}
	
	/**
	 * Fixes the position values of content elements after one has been removed from that menu
	 *
	 * @param session should be called within an active session and transaction
	 * @param menuID the ID of the menu from which the content is removed
	 * @param removedPosition the former position of the content element
	 */
	private void closeGapAfterRemovedContent(Session session, Integer menuID, Integer removedPosition) {
		this.shiftContentPositions(session, menuID , removedPosition, false, -1);
	}
	
	/**
	 * Used to shift all content elements of a given menu with position > or >= the 
	 * fromPosition parameter by the specified amount. 
	 *
	 * @param session the session
	 * @param menuId the menu id
	 * @param fromPosition the position where something is inserted/ deleted
	 * @param isInclusive true (>=) if inserting , false (>) if deleting  an element
	 * @param amount the amount to shift (usually +1 or -1)
	 */
	private void shiftContentPositions(Session session, Integer menuId, Integer fromPosition, boolean isInclusive, Integer amount) {
	   
		if (session == null || menuId == null || fromPosition == null || amount == null) {
	        return;
	    }

	    String operator = isInclusive ? ">=" : ">";

	    String hql = "UPDATE SiteMenuContent smc " +
	                 "SET smc.contentListPosition = smc.contentListPosition + :amount " +
	                 "WHERE smc.siteMenu.id = :menuId AND smc.contentListPosition " + operator + " :fromPosition";

	    session.createMutationQuery(hql)
	           .setParameter("menuId", menuId)
	           .setParameter("fromPosition", fromPosition)
	           .setParameter("amount", amount)
	           .executeUpdate();
	}

	/**
	 * Repositions a content item within the same menu, shifting affected siblings accordingly.
	 * 
	 * @param menuId The ID of the menu holding the content.
	 * @param startingPosition The current/old list position.
	 * @param targetPosition The new desired list position.
	 * @param session The active Hibernate session.
	 */
	private void reorderContentWithinSameMenu(Session session, Integer menuId, Integer startingPosition, Integer targetPosition) {
	   
		if (startingPosition.equals(targetPosition)) {
	        return; // Nothing to move
	    }

	    String hql;
	    if (startingPosition > targetPosition) {
	        /* Moving towards the top/start: 
	         * Items between targetPosition and startingPosition - 1 shift DOWN (+1) */
	        hql = "UPDATE SiteMenuContent smc " +
	              "SET smc.contentListPosition = smc.contentListPosition + 1 " +
	              "WHERE smc.siteMenu.id = :menuId " +
	              "  AND smc.contentListPosition >= :targetPosition " +
	              "  AND smc.contentListPosition < :startingPosition";
	    } else {
	        /* Moving towards the bottom/end: 
	         * Items between startingPosition + 1 and targetPosition shift UP (-1) */
	        hql = "UPDATE SiteMenuContent smc " +
	              "SET smc.contentListPosition = smc.contentListPosition - 1 " +
	              "WHERE smc.siteMenu.id = :menuId " +
	              "  AND smc.contentListPosition > :startingPosition " +
	              "  AND smc.contentListPosition <= :targetPosition";
	    }

	    session.createMutationQuery(hql)
	           .setParameter("menuId", menuId)
	           .setParameter("startingPosition", startingPosition)
	           .setParameter("targetPosition", targetPosition)
	           .executeUpdate();
	}
	
	/**
	 * Make room for inserted menu.
	 *
	 * @param session the session
	 * @param parentMenuId the parent menu ID
	 * @param targetPosition the target position
	 */
	private void makeRoomForInsertedMenu(Session session, Integer parentMenuId, Integer insertPosition) {
		this.shiftMenuPositions(session, parentMenuId, insertPosition, true, 1);
	}
	
	/**
	 * Close gap after removed menu.
	 *
	 * @param session the session
	 * @param parentMenuId the parent menu ID
	 * @param removedPosition the target position
	 */
	private void closeGapAfterRemovedMenu(Session session, Integer parentMenuId, Integer removedPosition){
		this.shiftMenuPositions(session, parentMenuId, removedPosition, false, -1);
	}

	/**
	 * Shift menu positions.
	 *
	 * @param session the session
	 * @param parentMenuId the parent menu id
	 * @param targetPosition the target position
	 * @param isInclusive the is inclusive
	 * @param amount the amount
	 */
	private void shiftMenuPositions(Session session, Integer parentMenuId, Integer targetPosition, boolean isInclusive, Integer amount) {
	   
		if (session == null || targetPosition == null || amount == null) {
	        return;
	    }

	    String operator = isInclusive ? ">=" : ">";
	    String whereClause = parentMenuId == null? "WHERE sm.parentMenu.id IS NULL" : "WHERE sm.parentMenu.id = :parentMenuId";
	    
	    String hql = "UPDATE SiteMenu sm " +
	                 "SET sm.position = sm.position + :amount " +
	                 whereClause + " AND sm.position " + operator + " :targetPosition";

		MutationQuery query = session.createMutationQuery(hql);
		if (parentMenuId != null) {
			query.setParameter("parentMenuId", parentMenuId);
		}
		query.setParameter("targetPosition", targetPosition);
		query.setParameter("amount", amount);
		query.executeUpdate();
	}
	
	/**
	 * Reorder within same parent.
	 *
	 * @param parentId the parent id
	 * @param oldPos the old pos
	 * @param newPos the new pos
	 */
	private void reorderMenusWithinSameParent(Session session, Integer parentId, Integer oldPos, Integer newPos) {
		if (oldPos == null || newPos == null || oldPos.equals(newPos)) {
			return;
		}

		String whereClause = (parentId == null) ? "WHERE sm.parentMenu.id IS NULL" : "WHERE sm.parentMenu.id = :parentId";
		String hql;

		if (oldPos > newPos) {
			hql = "UPDATE SiteMenu sm SET m.position = sm.position + 1 " +
			      whereClause + " AND sm.position >= :newPos AND sm.position < :oldPos";
		} else {
			hql = "UPDATE SiteMenu sm SET sm.position = sm.position - 1 " +
			      whereClause + " AND sm.position > :oldPos AND sm.position <= :newPos";
		}

		MutationQuery query = session.createMutationQuery(hql);
		if (parentId != null) {
			query.setParameter("parentId", parentId);
		}
		query.setParameter("newPos", newPos);
		query.setParameter("oldPos", oldPos);
		query.executeUpdate();
	}
	
	/**
	 * Returns the amount of menus within the specified parent menu.
	 *
	 * @param session the session
	 * @param parentId the parent id
	 * @return the menu count in parent
	 */
	private int getMenuCountInParent(Session session, Integer parentId) {
	    
		String hql;
	    TypedQuery<Long> query;
	    
	    if (parentId == null) {
	        hql = "SELECT COUNT(m) FROM SiteMenu m WHERE m.parentMenu IS NULL";
	        query = session.createQuery(hql, Long.class);
	    } else {
	        hql = "SELECT COUNT(m) FROM SiteMenu m WHERE m.parentMenu.id = :parentId";
	        query = session.createQuery(hql, Long.class);
	        query.setParameter("parentId", parentId);
	    }
	    return query.getSingleResult().intValue();
	}
	
	// --------------------------------------------------------------
	// --- Helper Methods -------------------------------------------
	// --------------------------------------------------------------

	/**
	 * Used in all Dynamic Content API related methods to communicate to the rest
	 * controller what went wrong, differentiating between invalid arguments, connection issues 
	 * and unexpected errors.
	 *
	 * @param defaultMessage the default message
	 * @param ex the exception
	 * @throws IllegalArgumentException signals that a parameter was invalid (usually an ID which could not be found)
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DatabaseException Signals that something unexpected went wrong, so the error is being logged
	 */
	private void rethrowExpectedOrDatabaseException(String defaultMessage, Exception ex) throws IllegalArgumentException, IOException, DatabaseException {
		
		if (ex instanceof IllegalArgumentException illegalArgEx) {
			throw illegalArgEx;
			
		} else if (ex instanceof IOException ioEx) {
			throw ioEx;
			
		} else {
			LOGGER.error(defaultMessage, ex);
			throw new DatabaseException(defaultMessage, ex);
		}
	}
	
	/**
	 * Checks if the specified content is assigned to any menu and deletes it if not.
	 *
	 * @param session the session
	 * @param content the content
	 */
	private void deleteContentIfOrphaned(Session session, SiteContent content) {
	    
		if (content == null) {
	        return;
	    }
	    
	    if (content.getSiteMenuContent() == null || content.getSiteMenuContent().isEmpty()) {
	        session.remove(content);
	    }
	}
	
}