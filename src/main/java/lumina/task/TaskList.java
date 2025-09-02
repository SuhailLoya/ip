package lumina.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lumina.exception.LuminaException;
import lumina.parser.Parser;
import lumina.ui.Ui;

/**
 * Manages a list of tasks and handles various operations such as adding,
 * listing, and marking tasks as done or not done.
 */
public class TaskList {

    // tasks
    private ArrayList<Task> tasks;
    private Ui ui;
    private Parser parser;

    /**
     * Constructs a TaskList with the specified Ui and Parser.
     *
     * @param ui the Ui instance for displaying messages to the user
     * @param parser the Parser instance for parsing inputs
     */
    public TaskList(Ui ui, Parser parser) {
        this.ui = ui;
        this.parser = parser;
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list and prints a confirmation message.
     *
     * @param task the Task to be added to the list
     * @return String response
     */
    public String addTask(Task task) {
        this.tasks.add(task);
        StringBuilder addTaskMessage = new StringBuilder();
        addTaskMessage.append("Got it. I've added this task:\n");
        addTaskMessage.append(ui.indentMessage(task.toString())).append("\n");
        addTaskMessage.append(String.format("Now you have %d tasks in the list.", tasks.size()));
        return addTaskMessage.toString();
    }

    /**
     * Lists all tasks in the list and returns the response
     *
     * @return String response to listTasks
     */
    public String listTasks() {
        StringBuilder listedTaskMessage = new StringBuilder();
        listedTaskMessage.append("Here are the tasks in your list:\n");
        for (int i = 0; i < this.tasks.size(); i++) {
            listedTaskMessage.append(Integer.toString(i + 1)).append(".");
            listedTaskMessage.append(this.tasks.get(i));
            if (i < this.tasks.size() - 1) {
                listedTaskMessage.append("\n");
            }
        }
        return listedTaskMessage.toString();
    }

    /**
     * Converts all tasks in the list to a list of strings for saving purposes.
     *
     * @return an ArrayList of strings representing the save strings
     */
    public ArrayList<String> toLines() {
        return tasks.stream()
                .map(Task::saveString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Marks the specified task as done and prints a confirmation message.
     *
     * @param msg Message indicating the index of the task to be marked as done
     * @return String representing response to msg.
     * @throws LuminaException if the index is out of bounds
     */
    public String markTaskDone(String msg) throws LuminaException {

        String[] msgSplit = msg.split(" ");
        if (msgSplit.length != 2) {
            throw new LuminaException("Oh no! Lumina detected "
                    + "unexpected number of parameters in your command! "
                    + "Please try again");
        }
        int index = Integer.parseInt(msgSplit[1]) - 1; // 0 indexed

        if (index < 0 || index >= this.tasks.size()) {
            throw new LuminaException("Oh no! Lumina detected index out of bounds! Please try again");
        }
        StringBuilder taskDoneMessage = new StringBuilder();
        taskDoneMessage.append("Nice! I've marked this task as done:\n");
        this.tasks.get(index).markAsDone();
        taskDoneMessage.append(ui.indentMessage(this.tasks.get(index).toString()));
        return taskDoneMessage.toString();
    }

    /**
     * Marks the specified task as not done and prints a confirmation message.
     *
     * @param msg Indicating the index of the task to be marked as not done
     * @return String response to message.
     * @throws LuminaException if the index is out of bounds
     */
    public String markTaskNotDone(String msg) throws LuminaException {
        String[] msgSplit = msg.split(" ");
        if (msgSplit.length != 2) {
            throw new LuminaException("Oh no! Lumina detected "
                    + "unexpected number of parameters in your command! "
                    + "Please try again");
        }
        int index = Integer.parseInt(msgSplit[1]) - 1; // 0 indexed

        if (index < 0 || index >= this.tasks.size()) {
            throw new LuminaException("Oh no! Lumina detected index out of bounds! "
                    + "Please try again");
        }
        StringBuilder taskNotDoneMessage = new StringBuilder();
        taskNotDoneMessage.append("OK, I've marked this task as not done yet:\n");
        this.tasks.get(index).markAsNotDone();
        taskNotDoneMessage.append(ui.indentMessage(this.tasks.get(index).toString()));
        return taskNotDoneMessage.toString();
    }

    /**
     * Handles the creation of a Todo task based on the provided message.
     * The message should be in the format: todo `description`
     *
     * @param msg the message containing the task description
     * @return String response to msg
     * @throws LuminaException if the message format is invalid
     */
    public String handleTodoTask(String msg) throws LuminaException {
        String[] msgSplit = msg.split(" ");
        if (msgSplit.length < 2) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your "
                    + "ToDo Task! Please try again");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < msgSplit.length; i++) {
            builder.append(msgSplit[i]);
            if (i < msgSplit.length - 1) {
                builder.append(" ");
            }
        }
        Task task = new TodoTask(builder.toString());
        String resp = this.addTask(task);
        return resp;
    }

    /**
     * Handles the creation of a Deadline task based on the provided message.
     * The message should be in the format: "deadline `description` /by `dateTime`"
     *
     * @param msg the message containing the task description and deadline
     * @return String response to msg
     * @throws LuminaException if the message format is invalid or missing required parts
     */

    public String handleDeadlineTask(String msg) throws LuminaException {
        Pattern pattern = Pattern.compile("^\\S+\\s+(.*?)\\s+/by\\s+(.*)$");
        Matcher matcher = pattern.matcher(msg);

        if (!matcher.matches()) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your "
                    + "Deadline Task! Please try again");
        }
        String desc = matcher.group(1).trim();
        String byDateTime = matcher.group(2).trim();

        if (desc.isEmpty() || byDateTime.isEmpty()) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your "
                    + "Deadline Task! Please try again");
        }

        LocalDate byDateObject = parser.parseDateString(byDateTime);
        Task task = new DeadlineTask(desc, byDateObject);
        return this.addTask(task);
    }

    /**
     * Deletes a task from the list based on the specified index and prints a confirmation message.
     *
     * @param msg indicating the index of the task to be deleted.
     * @return String response to msg
     * @throws LuminaException if the index is out of bounds
     */
    public String deleteTask(String msg) throws LuminaException {

        String[] msgSplit = msg.split(" ");
        if (msgSplit.length != 2) {
            throw new LuminaException("Oh no! Lumina detected "
                    + "unexpected number of parameters in your command! "
                    + "Please try again");
        }
        int index = Integer.parseInt(msgSplit[1]) - 1; // 0 indexed

        if (index < 0 || index >= this.tasks.size()) {
            throw new LuminaException("Oh no! Lumina detected index out of bounds! "
                    + "Please try again");
        }
        StringBuilder deleteTaskMessage = new StringBuilder();
        deleteTaskMessage.append("Noted. I've removed this task:\n");
        deleteTaskMessage.append(ui.indentMessage(this.tasks.get(index).toString()));
        deleteTaskMessage.append("\n");
        this.tasks.remove(index);
        deleteTaskMessage.append(String.format("Now you have %d tasks in the list.",
                tasks.size()));
        return deleteTaskMessage.toString();
    }

    /**
     * Searches for tasks in the list that contain the specified search string in their description.
     *
     * @param msg the message containing search string
     * @return String response
     */
    public String findTasks(String msg) throws LuminaException {
        // Define the regex pattern to match the first word followed by any whitespace
        Pattern pattern = Pattern.compile("^\\S+\\s*(.*)$");
        Matcher matcher = pattern.matcher(msg);

        String searchString;
        if (!matcher.find()) {
            // if it doesn't match throw exception
            throw new LuminaException("Oh no! invalid find command! Please try again");
        }
        searchString = matcher.group(1);

        StringBuilder findTasksMessage = new StringBuilder();
        findTasksMessage.append("Here are the matching tasks in your list:\n");
        int count = 1;
        for (int i = 0; i < this.tasks.size(); i++) {
            if (this.tasks.get(i).findInDescription(searchString)) {
                if (count > 1) {
                    findTasksMessage.append("\n");
                }
                findTasksMessage.append(Integer.toString(count)).append(".");
                findTasksMessage.append(this.tasks.get(i));
                count++;
            }
        }
        return findTasksMessage.toString();
    }

    /**
     * Searches for tasks in the list that are upcoming
     *
     * @return String response
     */
    public String remindTasks() throws LuminaException {

        StringBuilder findTasksMessage = new StringBuilder();
        findTasksMessage.append("Here are the upcoming tasks in your list:\n");
        int count = 1;
        for (int i = 0; i < this.tasks.size(); i++) {
            if (this.tasks.get(i).isUpcoming()) {
                if (count > 1) {
                    findTasksMessage.append("\n");
                }
                findTasksMessage.append(Integer.toString(count)).append(".");
                findTasksMessage.append(this.tasks.get(i));
                count++;
            }
        }
        return findTasksMessage.toString();
    }

    /**
     * Handles the creation of an Event task based on the provided message.
     * The message should be in the format: "event `description` /from `startDateTime` /to `endDateTime`"
     *
     * @param msg the message containing the task description, start date/time, and end date/time
     * @return String response to msg.
     * @throws LuminaException if the message format is invalid or missing required parts
     */
    public String handleEventTask(String msg) throws LuminaException {
        String[] msgSplit = msg.split(" ");
        Pattern pattern = Pattern.compile("^\\S+\\s+(.*?)\\s+/from\\s+(.*?)\\s+/to\\s+(.*)$");
        Matcher matcher = pattern.matcher(msg);

        if (!matcher.matches()) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your "
                    + "Event Task! Please try again");
        }

        String desc = matcher.group(1).trim();
        String startDateTime = matcher.group(2).trim();
        String endDateTime = matcher.group(3).trim();

        if (desc.isEmpty() || startDateTime.isEmpty() || endDateTime.isEmpty()) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your "
                    + "Event Task! Please try again");
        }

        LocalDate startDateObject = parser.parseDateString(startDateTime);
        LocalDate endDateObject = parser.parseDateString(endDateTime);

        Task task = new EventTask(desc, startDateObject, endDateObject);
        return this.addTask(task);
    }


    /**
     * Sets the data for the task list.
     *
     * @param data an ArrayList of tasks to set
     */
    public void setData(ArrayList<Task> data) {
        tasks = data;
    }
}
