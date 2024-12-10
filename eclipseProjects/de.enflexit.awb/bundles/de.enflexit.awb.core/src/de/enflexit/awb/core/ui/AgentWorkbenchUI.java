package de.enflexit.awb.core.ui;

import javax.swing.Icon;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.ProjectsLoaded.ProjectAction;

/**
 * The Interface definition for an UI of AgentWorkbench.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AgentWorkbenchUI {

	/**
	 * Has to return the name of the implementation.
	 * @return the implementation name
	 */
	public String getImplementationName();
	
	/**
	 * Will be called to Initialize the current UI-implementation.
	 * Should not necessarily show a visualization.
	 */
	public void initialize();

	/**
	 * May initiate and return an OS tray icon.
	 * @return the tray icon
	 */
	public AwbTrayIcon getTrayIcon();
	
	/**
	 * Has to return the AwbMainWindow instance.
	 * @return the main window
	 */
	public AwbMainWindow getMainWindow();

	/**
	 * Has to return the AwbConsole instance.
	 *
	 * @param isForLocalConsoleOutput the indicator if it is for local console output
	 * @return the console
	 */
	public AwbConsole getConsole(boolean isForLocalConsoleOutput);

	/**
	 * Has to return a dialog console for consoles.
	 * @return the console dialog
	 */
	public AwbConsoleDialog getConsoleDialog();
	
	/**
	 * Has to open a project interaction dialog for open, create, export or delete a project.
	 *
	 * @param actionTitel the action title 
	 * @param action the action
	 * @return the project interaction dialog
	 */
	public AwbProjectInteractionDialog getProjectInteractionDialog(String actionTitel, ProjectAction action);
	
	/**
	 * Has to return the project editor window.
	 *
	 * @param project the project
	 * @return the project editor window
	 */
	public AwbProjectWindow getProjectWindow(Project project);
	
	/**
	 * Should creates a project window tab for the .
	 *
	 * @param currProject the current project instance
	 * @param displayType_DEV_or_USER the display type DE V or USER
	 * @param tabTitle the tab title
	 * @param toolTipText the tool tip text
	 * @param icon the icon to be used for the project window tab
	 * @param displayComponent the display component
	 * @param parentsName the parents component name
	 * @return the AwbProjectWindowTab
	 */
	public AwbProjectWindowTab createProjectWindowTab(Project currProject, int displayType_DEV_or_USER, String tabTitle, String toolTipText, Icon icon, Object displayComponent, String parentsName);
	
	/**
	 * Has to return a progress monitor .
	 *
	 * @param windowTitle the window title
	 * @param headerText the header text
	 * @param progressText the progress text
	 * @return the progress monitor
	 */
	public AwbProgressMonitor getProgressMonitor(String windowTitle, String headerText, String progressText);
	
	/**
	 * Has to return a monitor UI to visualize the running system benchmark.
	 * @return the benchmark monitor
	 */
	public AwbBenchmarkMonitor getBenchmarkMonitor();
	
	
	/**
	 * Depending on the availability of the {@link AwbMainWindow}, should open a modal or a standalone about dialog.
	 */
	public AwbAboutDialog showModalAboutDialog();

	/**
	 * Depending on the availability of the {@link AwbMainWindow}, should display a modal or a standalone options dialog.
	 *
	 * @param categoryToFocus the category to focus
	 * @return the awb database dialog
	 */
	public AwbOptionsDialog showModalOptionsDialog(String categoryToFocus);

	/**
	 * Depending on the availability of the {@link AwbMainWindow}, should display a modal or a  standalon translation dialog.
	 * @return the awb translation dialog
	 */
	public AwbTranslationDialog showModalTranslationDialog();
	
	/**
	 * Depending on the availability of the {@link AwbMainWindow}, should open a modal or a standalone database configuration dialog.
	 * @param factoryID the Hibernate factory ID to focus 
	 */
	public AwbDatabaseDialog showModalDatabaseDialog(String factoryID);
	
	
	 /**
     * Has to prompt the user for input in a blocking dialog where the
     * initial selection, possible selections, and all other options can
     * be specified. The user will able to choose from
     * <code>selectionValues</code>, where <code>null</code> implies the
     * user can input
     * whatever they wish, usually by means of a <code>JTextField</code>.
     * <code>initialSelectionValue</code> is the initial value to prompt
     * the user with. It is up to the UI to decide how best to represent
     * the <code>selectionValues</code>, but usually a
     * <code>JComboBox</code>, <code>JList</code>, or
     * <code>JTextField</code> will be used.
     *
     * @param parentComponent  the parent <code>Component</code> for the
     *                  dialog
     * @param message  the <code>Object</code> to display
     * @param title    the <code>String</code> to display in the
     *                  dialog title bar
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *                  <code>INFORMATION_MESSAGE</code>,
     *                  <code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *                  or <code>PLAIN_MESSAGE</code>
     * @param icon     the <code>Icon</code> image to display
     * @param selectionValues an array of <code>Object</code>s that
     *                  gives the possible selections
     * @param initialSelectionValue the value used to initialize the input
     *                 field
     * @return user's input, or <code>null</code> meaning the user
     *                  canceled the input
     */
	public Object showInputDialog(Object parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue);

	/**
     * Has to bring up a dialog displaying a message, specifying all parameters.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                  dialog is displayed; if <code>null</code>,
     *                  or if the <code>parentComponent</code> has no
     *                  <code>Frame</code>, a
     *                  default <code>Frame</code> is used
     * @param message   the <code>Object</code> to display
     * @param title     the title string for the dialog
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *                  <code>INFORMATION_MESSAGE</code>,
     *                  <code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *                  or <code>PLAIN_MESSAGE</code>
     * @param icon      an icon to display in the dialog that helps the user
     *                  identify the kind of message that is being displayed
     */
	public void showMessageDialog(Object parentComponent, Object message, String title, int messageType, Icon icon);

	/**
 	 * Brings up a dialog with a specified icon, where the initial
 	 * choice is determined by the <code>initialValue</code> parameter and
 	 * the number of choices is determined by the <code>optionType</code>
 	 * parameter.
 	 * <p>
 	 * If <code>optionType</code> is <code>YES_NO_OPTION</code>,
 	 * or <code>YES_NO_CANCEL_OPTION</code>
 	 * and the <code>options</code> parameter is <code>null</code>,
 	 * then the options are
 	 * supplied by the look and feel.
 	 * <p>
 	 * The <code>messageType</code> parameter is primarily used to supply
 	 * a default icon from the look and feel.
 	 *
 	 * @param parentComponent determines the <code>Frame</code>
 	 *                  in which the dialog is displayed;  if
 	 *                  <code>null</code>, or if the
 	 *                  <code>parentComponent</code> has no
 	 *                  <code>Frame</code>, a
 	 *                  default <code>Frame</code> is used
 	 * @param message   the <code>Object</code> to display
 	 * @param title     the title string for the dialog
 	 * @param optionType an integer designating the options available on the
 	 *                  dialog: <code>DEFAULT_OPTION</code>,
 	 *                  <code>YES_NO_OPTION</code>,
 	 *                  <code>YES_NO_CANCEL_OPTION</code>,
 	 *                  or <code>OK_CANCEL_OPTION</code>
 	 * @param messageType an integer designating the kind of message this is,
 	 *                  primarily used to determine the icon from the
 	 *                  pluggable Look and Feel: <code>ERROR_MESSAGE</code>,
 	 *                  <code>INFORMATION_MESSAGE</code>,
 	 *                  <code>WARNING_MESSAGE</code>,
 	 *                  <code>QUESTION_MESSAGE</code>,
 	 *                  or <code>PLAIN_MESSAGE</code>
 	 * @param icon      the icon to display in the dialog
 	 * @param options   an array of objects indicating the possible choices
 	 *                  the user can make; if the objects are components, they
 	 *                  are rendered properly; non-<code>String</code>
 	 *                  objects are
 	 *                  rendered using their <code>toString</code> methods;
 	 *                  if this parameter is <code>null</code>,
 	 *                  the options are determined by the Look and Feel
 	 * @param initialValue the object that represents the default selection
 	 *                  for the dialog; only meaningful if <code>options</code>
 	 *                  is used; can be <code>null</code>
 	 * @return an integer indicating the option chosen by the user,
 	 *                  or <code>CLOSED_OPTION</code> if the user closed
 	 *                  the dialog
 	 */
	public int showOptionDialog(Object parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue);
	
}
