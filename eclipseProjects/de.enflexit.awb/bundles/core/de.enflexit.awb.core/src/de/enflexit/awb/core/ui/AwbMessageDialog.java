package de.enflexit.awb.core.ui;

import java.awt.HeadlessException;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;


/**
 * <code>AwbMessageDialog</code> makes it easy to pop up a standard dialog box that
 * prompts users for a value or informs them of something.
 * For information about using <code>AwbMessageDialog</code>, see
 * <a
 href="https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html">How to Make Dialogs</a>,
 * a section in <em>The Java Tutorial</em>.
 *
 * <p>
 *
 * While the <code>AwbMessageDialog</code>
 * class may appear complex because of the large number of methods, almost
 * all uses of this class are one-line calls to one of the static
 * <code>showXxxDialog</code> methods shown below:
 *
 * <table class="striped">
 * <caption>Common AwbMessageDialog method names and their descriptions</caption>
 * <thead>
 *   <tr>
 *     <th scope="col">Method Name
 *     <th scope="col">Description
 * </thead>
 * <tbody>
 *   <tr>
 *     <th scope="row">showConfirmDialog
 *     <td>Asks a confirming question, like yes/no/cancel.</td>
 *   <tr>
 *     <th scope="row">showInputDialog
 *     <td>Prompt for some input.
 *   <tr>
 *     <th scope="row">showMessageDialog
 *     <td>Tell the user about something that has happened.
 *   <tr>
 *     <th scope="row">showOptionDialog
 *     <td>The Grand Unification of the above three.
 * </tbody>
 * </table>
 *
 * Each of these methods also comes in a <code>showInternalXXX</code>
 * flavor, which uses an internal frame to hold the dialog box (see
 * {@link JInternalFrame}).
 * Multiple convenience methods have also been defined -- overloaded
 * versions of the basic methods that use different parameter lists.
 * <p>
 * All dialogs are modal. Each <code>showXxxDialog</code> method blocks
 * the caller until the user's interaction is complete.
 *
 * <table class="borderless">
 * <caption>Common dialog</caption>
 * <tr>
 *  <td style="background-color:#FFe0d0" rowspan=2>icon</td>
 *  <td style="background-color:#FFe0d0">message</td>
 * </tr>
 * <tr>
 *  <td style="background-color:#FFe0d0">input value</td>
 * </tr>
 * <tr>
 *   <td style="background-color:#FFe0d0" colspan=2>option buttons</td>
 * </tr>
 * </table>
 *
 * The basic appearance of one of these dialog boxes is generally
 * similar to the picture above, although the various
 * look-and-feels are
 * ultimately responsible for the final result.  In particular, the
 * look-and-feels will adjust the layout to accommodate the option pane's
 * <code>ComponentOrientation</code> property.
 * <br style="clear:all">
 * <p>
 * <b>Parameters:</b><br>
 * The parameters to these methods follow consistent patterns:
 * <blockquote>
 * <dl>
 * <dt>parentComponent<dd>
 * Defines the <code>Component</code> that is to be the parent of this
 * dialog box.
 * It is used in two ways: the <code>Frame</code> that contains
 * it is used as the <code>Frame</code>
 * parent for the dialog box, and its screen coordinates are used in
 * the placement of the dialog box. In general, the dialog box is placed
 * just below the component. This parameter may be <code>null</code>,
 * in which case a default <code>Frame</code> is used as the parent,
 * and the dialog will be
 * centered on the screen (depending on the {@literal L&F}).
 * <dt>message<dd>
 * A descriptive message to be placed in the dialog box.
 * In the most common usage, message is just a <code>String</code> or
 * <code>String</code> constant.
 * However, the type of this parameter is actually <code>Object</code>. Its
 * interpretation depends on its type:
 * <dl>
 * <dt>Object[]<dd>An array of objects is interpreted as a series of
 *                 messages (one per object) arranged in a vertical stack.
 *                 The interpretation is recursive -- each object in the
 *                 array is interpreted according to its type.
 * <dt>Component<dd>The <code>Component</code> is displayed in the dialog.
 * <dt>Icon<dd>The <code>Icon</code> is wrapped in a <code>JLabel</code>
 *               and displayed in the dialog.
 * <dt>others<dd>The object is converted to a <code>String</code> by calling
 *               its <code>toString</code> method. The result is wrapped in a
 *               <code>JLabel</code> and displayed.
 * </dl>
 * <dt>messageType<dd>Defines the style of the message. The Look and Feel
 * manager may lay out the dialog differently depending on this value, and
 * will often provide a default icon. The possible values are:
 * <ul>
 * <li><code>ERROR_MESSAGE</code>
 * <li><code>INFORMATION_MESSAGE</code>
 * <li><code>WARNING_MESSAGE</code>
 * <li><code>QUESTION_MESSAGE</code>
 * <li><code>PLAIN_MESSAGE</code>
 * </ul>
 * <dt>optionType<dd>Defines the set of option buttons that appear at
 * the bottom of the dialog box:
 * <ul>
 * <li><code>DEFAULT_OPTION</code>
 * <li><code>YES_NO_OPTION</code>
 * <li><code>YES_NO_CANCEL_OPTION</code>
 * <li><code>OK_CANCEL_OPTION</code>
 * </ul>
 * You aren't limited to this set of option buttons.  You can provide any
 * buttons you want using the options parameter.
 * <dt>options<dd>A more detailed description of the set of option buttons
 * that will appear at the bottom of the dialog box.
 * The usual value for the options parameter is an array of
 * <code>String</code>s. But
 * the parameter type is an array of <code>Objects</code>.
 * A button is created for each object depending on its type:
 * <dl>
 * <dt>Component<dd>The component is added to the button row directly.
 * <dt>Icon<dd>A <code>JButton</code> is created with this as its label.
 * <dt>other<dd>The <code>Object</code> is converted to a string using its
 *              <code>toString</code> method and the result is used to
 *              label a <code>JButton</code>.
 * </dl>
 * <dt>icon<dd>A decorative icon to be placed in the dialog box. A default
 * value for this is determined by the <code>messageType</code> parameter.
 * <dt>title<dd>The title for the dialog box.
 * <dt>initialValue<dd>The default selection (input value).
 * </dl>
 * </blockquote>
 * <p>
 * When the selection is changed, <code>setValue</code> is invoked,
 * which generates a <code>PropertyChangeEvent</code>.
 * <p>
 * If a <code>AwbMessageDialog</code> has configured to all input
 * <code>setWantsInput</code>
 * the bound property <code>AwbMessageDialog.INPUT_VALUE_PROPERTY</code>
 *  can also be listened
 * to, to determine when the user has input or selected a value.
 * <p>
 * When one of the <code>showXxxDialog</code> methods returns an integer,
 * the possible values are:
 * <ul>
 * <li><code>YES_OPTION</code>
 * <li><code>NO_OPTION</code>
 * <li><code>CANCEL_OPTION</code>
 * <li><code>OK_OPTION</code>
 * <li><code>CLOSED_OPTION</code>
 * </ul>
 * <b>Examples:</b>
 * <dl>
 * <dt>Show an error dialog that displays the message, 'alert':
 * <dd><code>
 * AwbMessageDialog.showMessageDialog(null, "alert", "alert", AwbMessageDialog.ERROR_MESSAGE);
 * </code>
 * <dt>Show an internal information dialog with the message, 'information':
 * <dd><pre>
 * AwbMessageDialog.showInternalMessageDialog(frame, "information",
 *             "information", AwbMessageDialog.INFORMATION_MESSAGE);
 * </pre>
 * <dt>Show an information panel with the options yes/no and message 'choose one':
 * <dd><pre>AwbMessageDialog.showConfirmDialog(null,
 *             "choose one", "choose one", AwbMessageDialog.YES_NO_OPTION);
 * </pre>
 * <dt>Show an internal information dialog with the options yes/no/cancel and
 * message 'please choose one' and title information:
 * <dd><pre>AwbMessageDialog.showInternalConfirmDialog(frame,
 *             "please choose one", "information",
 *             AwbMessageDialog.YES_NO_CANCEL_OPTION, AwbMessageDialog.INFORMATION_MESSAGE);
 * </pre>
 * <dt>Show a warning dialog with the options OK, CANCEL, title 'Warning', and
 * message 'Click OK to continue':
 * <dd><pre>
 * Object[] options = { "OK", "CANCEL" };
 * AwbMessageDialog.showOptionDialog(null, "Click OK to continue", "Warning",
 *             AwbMessageDialog.DEFAULT_OPTION, AwbMessageDialog.WARNING_MESSAGE,
 *             null, options, options[0]);
 * </pre>
 * <dt>Show a dialog asking the user to type in a String:
 * <dd><code>
 * String inputValue = AwbMessageDialog.showInputDialog("Please input a value");
 * </code>
 * <dt>Show a dialog asking the user to select a String:
 * <dd><pre>
 * Object[] possibleValues = { "First", "Second", "Third" };<br>
 * Object selectedValue = AwbMessageDialog.showInputDialog(null,
 *             "Choose one", "Input",
 *             AwbMessageDialog.INFORMATION_MESSAGE, null,
 *             possibleValues, possibleValues[0]);
 * </pre>
 * </dl>
 * <b>Direct Use:</b><br>
 * To create and use an <code>AwbMessageDialog</code> directly, the
 * standard pattern is roughly as follows:
 * <pre>
 *     AwbMessageDialog pane = new AwbMessageDialog(<i>arguments</i>);
 *     pane.set<i>.Xxxx(...); // Configure</i>
 *     JDialog dialog = pane.createDialog(<i>parentComponent, title</i>);
 *     dialog.show();
 *     Object selectedValue = pane.getValue();
 *     if(selectedValue == null)
 *       return CLOSED_OPTION;
 *     <i>//If there is <b>not</b> an array of option buttons:</i>
 *     if(options == null) {
 *       if(selectedValue instanceof Integer)
 *          return ((Integer)selectedValue).intValue();
 *       return CLOSED_OPTION;
 *     }
 *     <i>//If there is an array of option buttons:</i>
 *     for(int counter = 0, maxCounter = options.length;
 *        counter &lt; maxCounter; counter++) {
 *        if(options[counter].equals(selectedValue))
 *        return counter;
 *     }
 *     return CLOSED_OPTION;
 * </pre>
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more
 * information see <a
 * href="package-summary.html#threading">Swing's Threading
 * Policy</a>.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 */
public abstract class AwbMessageDialog {
  

    /**
     * Indicates that the user has not yet selected a value.
     */
    public static final Object      UNINITIALIZED_VALUE = "uninitializedValue";

    //
    // Option types
    //

    /**
     * Type meaning Look and Feel should not supply any options -- only
     * use the options from the <code>AwbMessageDialog</code>.
     */
    public static final int         DEFAULT_OPTION = -1;
    /** Type used for <code>showConfirmDialog</code>. */
    public static final int         YES_NO_OPTION = 0;
    /** Type used for <code>showConfirmDialog</code>. */
    public static final int         YES_NO_CANCEL_OPTION = 1;
    /** Type used for <code>showConfirmDialog</code>. */
    public static final int         OK_CANCEL_OPTION = 2;

    //
    // Return values.
    //
    /** Return value from class method if YES is chosen. */
    public static final int         YES_OPTION = 0;
    /** Return value from class method if NO is chosen. */
    public static final int         NO_OPTION = 1;
    /** Return value from class method if CANCEL is chosen. */
    public static final int         CANCEL_OPTION = 2;
    /** Return value form class method if OK is chosen. */
    public static final int         OK_OPTION = 0;
    /** Return value from class method if user closes window without selecting
     * anything, more than likely this should be treated as either a
     * <code>CANCEL_OPTION</code> or <code>NO_OPTION</code>. */
    public static final int         CLOSED_OPTION = -1;

    //
    // Message types. Used by the UI to determine what icon to display,
    // and possibly what behavior to give based on the type.
    //
    /** Used for error messages. */
    public static final int  ERROR_MESSAGE = 0;
    /** Used for information messages. */
    public static final int  INFORMATION_MESSAGE = 1;
    /** Used for warning messages. */
    public static final int  WARNING_MESSAGE = 2;
    /** Used for questions. */
    public static final int  QUESTION_MESSAGE = 3;
    /** No icon is used. */
    public static final int   PLAIN_MESSAGE = -1;

    /** Bound property name for <code>icon</code>. */
    public static final String      ICON_PROPERTY = "icon";
    /** Bound property name for <code>message</code>. */
    public static final String      MESSAGE_PROPERTY = "message";
    /** Bound property name for <code>value</code>. */
    public static final String      VALUE_PROPERTY = "value";
    /** Bound property name for <code>option</code>. */
    public static final String      OPTIONS_PROPERTY = "options";
    /** Bound property name for <code>initialValue</code>. */
    public static final String      INITIAL_VALUE_PROPERTY = "initialValue";
    /** Bound property name for <code>type</code>. */
    public static final String      MESSAGE_TYPE_PROPERTY = "messageType";
    /** Bound property name for <code>optionType</code>. */
    public static final String      OPTION_TYPE_PROPERTY = "optionType";
    /** Bound property name for <code>selectionValues</code>. */
    public static final String      SELECTION_VALUES_PROPERTY = "selectionValues";
    /** Bound property name for <code>initialSelectionValue</code>. */
    public static final String      INITIAL_SELECTION_VALUE_PROPERTY = "initialSelectionValue";
    /** Bound property name for <code>inputValue</code>. */
    public static final String      INPUT_VALUE_PROPERTY = "inputValue";
    /** Bound property name for <code>wantsInput</code>. */
    public static final String      WANTS_INPUT_PROPERTY = "wantsInput";

    /** Icon used in pane. */
    protected transient Icon                  icon;
    /** Message to display. */
    protected transient Object                message;
    /** Options to display to the user. */
    protected transient Object[]              options;
    /** Value that should be initially selected in <code>options</code>. */
    protected transient Object                initialValue;
    /** Message type. */
    protected int                   messageType;
    /**
     * Option type, one of <code>DEFAULT_OPTION</code>,
     * <code>YES_NO_OPTION</code>,
     * <code>YES_NO_CANCEL_OPTION</code> or
     * <code>OK_CANCEL_OPTION</code>.
     */
    protected int                   optionType;
    /** Currently selected value, will be a valid option, or
     * <code>UNINITIALIZED_VALUE</code> or <code>null</code>. */
    protected transient Object                value;
    /** Array of values the user can choose from. Look and feel will
     * provide the UI component to choose this from. */
    protected transient Object[]              selectionValues;
    /** Value the user has input. */
    protected transient Object                inputValue;
    /** Initial value to select in <code>selectionValues</code>. */
    protected transient Object                initialSelectionValue;
    /** If true, a UI widget will be provided to the user to get input. */
    protected boolean                         wantsInput;


    /**
     * Shows a question-message dialog requesting input from the user. The
     * dialog uses the default frame, which usually means it is centered on
     * the screen.
     *
     * @param message the <code>Object</code> to display
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @return user's input
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Object message) {
        return AwbMessageDialog.showInputDialog(null, message, null);
    }

    /**
     * Shows a question-message dialog requesting input from the user, with
     * the input value initialized to <code>initialSelectionValue</code>. The
     * dialog uses the default frame, which usually means it is centered on
     * the screen.
     *
     * @param message the <code>Object</code> to display
     * @param initialSelectionValue the value used to initialize the input
     *                 field
     * @return user's input
     * @since 1.4
     */
    public static String showInputDialog(Object message, Object initialSelectionValue) {
        return AwbMessageDialog.showInputDialog(null, message, initialSelectionValue);
    }

   
    /**
     * Shows a question-message dialog requesting input from the user and
     * parented to <code>parentComponent</code>. The input value will be
     * initialized to <code>initialSelectionValue</code>.
     * The dialog is displayed on top of the <code>Component</code>'s
     * frame, and is usually positioned below the <code>Component</code>.
     *
     * @param parentComponent  the parent <code>Component</code> for the
     *          dialog
     * @param message the <code>Object</code> to display
     * @param initialSelectionValue the value used to initialize the input
     *                 field
     * @return user's input
     * @since 1.4
     */
    public static String showInputDialog(Object parentComponent, Object message, Object initialSelectionValue) {
        return (String)AwbMessageDialog.showInputDialog(parentComponent, message, UIManager.getString("OptionPane.inputDialogTitle", Locale.getDefault()), QUESTION_MESSAGE, null, null, initialSelectionValue);
    }

    /**
     * Shows a dialog requesting input from the user parented to
     * <code>parentComponent</code> with the dialog having the title
     * <code>title</code> and message type <code>messageType</code>.
     *
     * @param parentComponent  the parent <code>Component</code> for the
     *                  dialog
     * @param message  the <code>Object</code> to display
     * @param title    the <code>String</code> to display in the dialog
     *                  title bar
     * @param messageType the type of message that is to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *                  <code>INFORMATION_MESSAGE</code>,
     *                  <code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *                  or <code>PLAIN_MESSAGE</code>
     * @return user's input
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Object parentComponent, Object message, String title, int messageType) {
        return (String)AwbMessageDialog.showInputDialog(parentComponent, message, title, messageType, null, null, null);
    }

    /**
     * Prompts the user for input in a blocking dialog where the
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
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static Object showInputDialog(Object parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {
    	return AgentWorkbenchUiManager.getInstance().showInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
    }

    
    /**
     * Brings up an information-message dialog titled "Message".
     *
     * @param parentComponent determines the <code>Frame</code> in
     *          which the dialog is displayed; if <code>null</code>,
     *          or if the <code>parentComponent</code> has no
     *          <code>Frame</code>, a default <code>Frame</code> is used
     * @param message   the <code>Object</code> to display
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Object parentComponent, Object message) {
    	AwbMessageDialog.showMessageDialog(parentComponent, message, UIManager.getString("OptionPane.messageDialogTitle", Locale.getDefault()), INFORMATION_MESSAGE);
    }

    /**
     * Brings up a dialog that displays a message using a default
     * icon determined by the <code>messageType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code>
     *          in which the dialog is displayed; if <code>null</code>,
     *          or if the <code>parentComponent</code> has no
     *          <code>Frame</code>, a default <code>Frame</code> is used
     * @param message   the <code>Object</code> to display
     * @param title     the title string for the dialog
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *                  <code>INFORMATION_MESSAGE</code>,
     *                  <code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *                  or <code>PLAIN_MESSAGE</code>
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Object parentComponent, Object message, String title, int messageType) {
        AwbMessageDialog.showMessageDialog(parentComponent, message, title, messageType, null);
    }

    /**
     * Brings up a dialog displaying a message, specifying all parameters.
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
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Object parentComponent, Object message, String title, int messageType, Icon icon) {
    	 AgentWorkbenchUiManager.getInstance().showMessageDialog(parentComponent, message, title, messageType, icon);
    }

    /**
     * Brings up a dialog with the options <i>Yes</i>,
     * <i>No</i> and <i>Cancel</i>; with the
     * title, <b>Select an Option</b>.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                  dialog is displayed; if <code>null</code>,
     *                  or if the <code>parentComponent</code> has no
     *                  <code>Frame</code>, a
     *                  default <code>Frame</code> is used
     * @param message   the <code>Object</code> to display
     * @return an integer indicating the option selected by the user
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Object parentComponent, Object message) {
        return AwbMessageDialog.showConfirmDialog(parentComponent, message, UIManager.getString("OptionPane.titleText"), YES_NO_CANCEL_OPTION);
    }

    /**
     * Brings up a dialog where the number of choices is determined
     * by the <code>optionType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                  dialog is displayed; if <code>null</code>,
     *                  or if the <code>parentComponent</code> has no
     *                  <code>Frame</code>, a
     *                  default <code>Frame</code> is used
     * @param message   the <code>Object</code> to display
     * @param title     the title string for the dialog
     * @param optionType an int designating the options available on the dialog:
     *                  <code>YES_NO_OPTION</code>,
     *                  <code>YES_NO_CANCEL_OPTION</code>,
     *                  or <code>OK_CANCEL_OPTION</code>
     * @return an int indicating the option selected by the user
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Object parentComponent, Object message, String title, int optionType) {
        return AwbMessageDialog.showConfirmDialog(parentComponent, message, title, optionType, QUESTION_MESSAGE);
    }

    /**
     * Brings up a dialog where the number of choices is determined
     * by the <code>optionType</code> parameter, where the
     * <code>messageType</code>
     * parameter determines the icon to display.
     * The <code>messageType</code> parameter is primarily used to supply
     * a default icon from the Look and Feel.
     *
     * @param parentComponent determines the <code>Frame</code> in
     *                  which the dialog is displayed; if <code>null</code>,
     *                  or if the <code>parentComponent</code> has no
     *                  <code>Frame</code>, a
     *                  default <code>Frame</code> is used.
     * @param message   the <code>Object</code> to display
     * @param title     the title string for the dialog
     * @param optionType an integer designating the options available
     *                   on the dialog: <code>YES_NO_OPTION</code>,
     *                  <code>YES_NO_CANCEL_OPTION</code>,
     *                  or <code>OK_CANCEL_OPTION</code>
     * @param messageType an integer designating the kind of message this is;
     *                  primarily used to determine the icon from the pluggable
     *                  Look and Feel: <code>ERROR_MESSAGE</code>,
     *                  <code>INFORMATION_MESSAGE</code>,
     *                  <code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *                  or <code>PLAIN_MESSAGE</code>
     * @return an integer indicating the option selected by the user
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Object parentComponent, Object message, String title, int optionType, int messageType) {
        return AwbMessageDialog.showConfirmDialog(parentComponent, message, title, optionType, messageType, null);
    }

    /**
     * Brings up a dialog with a specified icon, where the number of
     * choices is determined by the <code>optionType</code> parameter.
     * The <code>messageType</code> parameter is primarily used to supply
     * a default icon from the look and feel.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                  dialog is displayed; if <code>null</code>,
     *                  or if the <code>parentComponent</code> has no
     *                  <code>Frame</code>, a
     *                  default <code>Frame</code> is used
     * @param message   the Object to display
     * @param title     the title string for the dialog
     * @param optionType an int designating the options available on the dialog:
     *                  <code>YES_NO_OPTION</code>,
     *                  <code>YES_NO_CANCEL_OPTION</code>,
     *                  or <code>OK_CANCEL_OPTION</code>
     * @param messageType an int designating the kind of message this is,
     *                  primarily used to determine the icon from the pluggable
     *                  Look and Feel: <code>ERROR_MESSAGE</code>,
     *                  <code>INFORMATION_MESSAGE</code>,
     *                  <code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *                  or <code>PLAIN_MESSAGE</code>
     * @param icon      the icon to display in the dialog
     * @return an int indicating the option selected by the user
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Object parentComponent, Object message, String title, int optionType, int messageType, Icon icon) {
        return AwbMessageDialog.showOptionDialog(parentComponent, message, title, optionType, messageType, icon, null, null);
    }

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
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showOptionDialog(Object parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
    	return AgentWorkbenchUiManager.getInstance().showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);
    }
	
}
