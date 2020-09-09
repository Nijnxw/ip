package duke.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import duke.commands.CommandResult;
import duke.data.task.Task;
import duke.utils.Messages;

/**
 * UI of the application.
 */
public class Ui {

    /** Offset required to convert between 1-indexing and 0-indexing.  */
    public static final int DISPLAYED_INDEX_OFFSET = 1;

    /** A platform independent line separator. */
    private static final String LS = System.lineSeparator();

    /** A decorative prefix added to the beginning of lines printed by AddressBook */
    private static final String LINE_PREFIX = "| ";

    private static final String DIVIDER = "---------------------------------------------------------------------------";

    /** Format of indexed list item */
    private static final String MESSAGE_INDEXED_LIST_ITEM = "\t%1$d. %2$s";

    private final Scanner in;
    private final PrintStream out;

    /** Starts the UI of the application. */
    public Ui() {
        this(System.in, System.out);
    }

    /**
     * Starts the UI of the application.
     */
    public Ui(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    /**
     * Returns true if the user input line should be ignored.
     * Input should be ignored if it is parsed as a comment, is only whitespace, or is empty.
     *
     * @param rawInputLine full raw user input line.
     * @return true if the entire user input line should be ignored.
     */
    private boolean shouldIgnore(String rawInputLine) {
        return rawInputLine.trim().isEmpty();
    }

    /**
     * Prompts for the command and reads the text entered by the user.
     * Ignores empty, pure whitespace, and comment lines.
     * Echos the command back to the user.
     * @return command (full line) entered by the user
     */
    public String getUserCommand() {
        out.print(LINE_PREFIX + "Enter command or type 'help': ");
        String fullInputLine = in.nextLine();

        // silently consume all ignored lines
        while (shouldIgnore(fullInputLine)) {
            fullInputLine = in.nextLine();
        }

        // showToUser("[Command entered: " + fullInputLine + "]\n");
        return fullInputLine;
    }

    /** Shows message(s) to the user */
    public void showToUser(String... message) {
        for (String m : message) {
            out.println(LINE_PREFIX + m.replace("\n", LS + LINE_PREFIX));
        }
    }

    /**
     * Generates and prints the failed message if the application fails to start.
     */
    public void showInitFailedMessage() {
        showToUser(Messages.MESSAGE_INIT_FAILED, DIVIDER, DIVIDER);
    }

    public String getInitFailedMessage() {
        return Messages.MESSAGE_INIT_FAILED;
    }

    /**
     * Generates and prints the welcome message upon the start of the application.
     */
    public void showWelcomeMessage() {
        showToUser(DIVIDER, Messages.MESSAGE_WELCOME, DIVIDER);
    }

    public String getWelcomeMessage() {
        return Messages.MESSAGE_WELCOME;
    }

    /**
     * Generates and prints the goodbye message upon the end of the application.
     */
    public void showGoodbyeMessage() {
        showToUser(Messages.MESSAGE_GOODBYE, DIVIDER);
    }

    public String getGoodbyeMessage() {
        return Messages.MESSAGE_GOODBYE;
    }

    /**
     * Shows a list of tasks to the user, formatted as an indexed list.
     */
    public void showTaskListView(List<? extends Task> taskList) {
        final List<String> formattedTasks = new ArrayList<>();
        taskList.forEach(task -> formattedTasks.add(task.toString()));
        showToUsersAsIndexedList(formattedTasks);
    }

    /**
     * Returns a string of a list of tasks to the user, formatted as an indexed list.
     */
    public String getTaskListView(List<? extends Task> taskList) {
        final List<String> formattedTasks = new ArrayList<>();
        taskList.forEach(task -> formattedTasks.add(task.toString()));
        return getIndexedListForViewing(formattedTasks);
    }

    /**
     * Shows the result of a command execution to the user. Includes additional formatting to demarcate different
     * command execution segments.
     */
    public void showResultToUser(CommandResult result) {
        final Optional<List<? extends Task>> tasks = result.getRelevantTasks();
        tasks.ifPresent(this::showTaskListView);
        showToUser(result.feedbackToUser, DIVIDER);
    }

    /**
     * Shows the result of a command execution to the user. Includes additional formatting to demarcate different
     * command execution segments.
     */
    public String getResultToUser(CommandResult result) {
        final Optional<List<? extends Task>> tasks = result.getRelevantTasks();
        StringBuilder sb = new StringBuilder();
        tasks.ifPresent(list -> sb.append(getTaskListView(list)));
        sb.append(result.feedbackToUser);
        return sb.toString();
    }

    /** Shows a list of strings to the user, formatted as an indexed list. */
    private void showToUsersAsIndexedList(List<String> list) {
        showToUser(getIndexedListForViewing(list), DIVIDER);
    }

    /** Formats a list of strings as a viewable indexed list. */
    private static String getIndexedListForViewing(List<String> listItems) {
        final StringBuilder formatted = new StringBuilder();
        int displayIndex = DISPLAYED_INDEX_OFFSET;
        for (String listItem : listItems) {
            formatted.append(getIndexedListItem(displayIndex, listItem)).append("\n");
            displayIndex++;
        }
        return formatted.toString();
    }

    /**
     * Formats a string as a viewable indexed list item.
     *
     * @param visibleIndex visible index for this listing
     */
    private static String getIndexedListItem(int visibleIndex, String listItem) {
        return String.format(MESSAGE_INDEXED_LIST_ITEM, visibleIndex, listItem);
    }

}
