### AWB-Example for a Database Usage

With this example, we demonstrate how you can create and use a database structure in the context of Agent.Workbench for your own purpose.

Therefore, and before you start, here some hints of things that you might find helpful:
- As nowadays a database could also be a NoSQL-database, we would like to state that we're working on relational databases here.  
  So it will be helpful that you know about database structures (first, second, and third normal forms of table structures in databases)  
  as well about SQL, in order to manipulate data within the database.   
- Agent.Workbench automates the initialization, creation and update of a database structure, as well as the establishment of a working connection.  
  This is done by the bundle *de.enflexit.db.hibernate*. As the bundle name indicates, the actual driver here is the object-relational mapping tool  
  Hibernate for which some background knowledge is essential. Using 'Hibernate' as search term, one can find a plenty amount of tutorials.
- JPA: The term stands for **Jakarta Persistence** (formally **Java Persistence API**) and describes a specification to manage relational data in Java.  
  It works with so called annotations that mark classes and attributes in Java as database objects, like tables or keys. Examples can be found in the  
  current bundle, such as in the classes *WebUser*, *WebGroup* and other (see package *de.enflexit.awb.samples.db.dataModel*). 
- *Annotations vs. Mapping Files:* As described, in this example, we're using annotated classes to map between table and object. Beside, it is possible  
  to use mapping files that 'explain' to Hibernate, how a class should be interpreted as a table. If your're interested or if it is necessary, you can  
  find its usage as an example in the bundle *de.enflexit.awb.bgSystem*. In this very simple case, a single class is mapped to a table.
  
How the current example works:
- To connect an individual database bundle with the Agent.Workbench Hibernate system, an OSGI-service needs to be implemented and registered to the   
  OSGI-platform. This is done by an entry in the MANIFEST.MF (see entry *Service-Component: OSGI-INF/HibernateDatabaseConnectionService.xml*), the  
  mentioned file */OSGI-INF/HibernateDatabaseConnectionService.xml* and a therein mapped implementation; the class *ExampleDatabaseConnectionService*.
- The class de.enflexit.awb.samples.db.ExampleDatabaseConnectionService implements a *HibernateDatabaseConnectionService* of Agent.Workbench.  
  It serves as a configurator for the desired database connection and objects, specifying the so-called Factory-ID, the internal location of mapping files (if required)  
  and the internal location of annotated class.
- The file ***hibernate.cfg.xml*** in package *de.enflexit.awb.samples.db.cfg*, represents a Hibernate configuration file for the handling of the connection or the factory respectively.  
  Here, operational Hibernate options, such as for encoding, threading and so on can be configured and specified. Options are explained in corresponding Hibernate tutorials.
- To work with objects and read or save them, one needs to implement corresponding read, edit or save methods. A generic example is given within the class  
  *de.enflexit.awb.samples.db.ExampleDatabaseHandler*. By initializing this class, methods like *dbSaveOrUpdate...*, *dbLoad...* or *dbDelete...* can be  
  used to save, update, read or delete objects to/from a database.

ToDo's to create your own AWB-database bundle
- Create a bundle structure similar to the example here (containing your own *HibernateDatabaseConnectionService* and corresponding files and file entries);
- Adjust the Factory-ID in your *HibernateDatabaseConnectionService* (it needs to be unique in a running AWB instance). The examples connection service may  
  serve as a kind of blueprint here.
- Create your own annotated classes. 

**The final Step: Connect to the desired database system during Agent.Workbench execution.**  
Using Hibernate allows you to use nearly any kind of relational database. That means, it dosn't matter if you want to use a MariaDB, MySQL, Postgres or  
an embedded Derby instance. Hibernate will manage the handling or mapping between object instances and table datasets. This valuable independence  
underlines why we decided to use Hibernate for our database interactions.
  
  
***
Find below the SQL - statement / script to delete the example table structure from a database:

`DROP TABLE ex_web_user_group_roles, ex_web_users_in_roles, ex_web_group_rights_in_roles, ex_web_user_rights_in_roles, ex_web_user, ex_web_user_right, ex_web_user_role, ex_web_group, ex_web_group_role, ex_web_group_right;`  

Depending on the database you're actually use, it might be that you have to delete further database objects, such as Sequences or Triggers. 