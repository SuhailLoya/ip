package lumina.main;

import java.util.Scanner;

import lumina.exception.LuminaException;
import lumina.parser.Parser;
import lumina.storage.Storage;
import lumina.task.TaskList;
import lumina.ui.Ui;

/**
 * Represents a simple chatbot.
 * It uses predefined set of responses and can be extended for more complex behaviour
 */
public class Lumina {

    // Response
    /** Exit response for lumina */
    public static final String EXIT_RESPONSE = "Bye. Hope to see you again soon!";

    // counter for instances
    private static int luminaCount = 0;

    // commands
    private static final String ECHO_EXIT_STRING = "bye";
    private static final String ECHO_LIST_STRING = "list";
    private static final String ECHO_MARK_TASK_STRING = "mark";
    private static final String ECHO_UNMARK_TASK_STRING = "unmark";
    private static final String ECHO_TODO_TASK = "todo";
    private static final String ECHO_DEADLINE_TASK = "deadline";
    private static final String ECHO_EVENT_TASK = "event";
    private static final String ECHO_DELETE_TASK = "delete";
    private static final String ECHO_FIND_TASK = "find";
    private static final String ECHO_REMIND_TASK = "remind";
    private static final String DEFAULT_MESSAGE = "Oh no! I don't know what to "
        + "do with this command! Please try again";

    // composed objects
    private Ui ui;
    private Parser parser;
    private Storage storage;
    private TaskList taskList;

    /**
     * Constructor for the chatbot
     *
     * @throws IllegalStateException If already instantiated
     */
    public Lumina() {
        if (luminaCount > 0) {
            // since for now we only have one data file, we will prevent more instantiation
            // of more than once Lumina instance, to prevent data from being corrupted
            throw new IllegalStateException("Lumina has already been instantiated!");
        }

        assert luminaCount == 0 : "luminaCount should be 0";

        luminaCount++;
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(parser);
        taskList = new TaskList(ui, parser);

        storage.loadData(taskList);
    }

    /**
     * Returns lumina's response to a string msg.
     *
     * @param msg The input string.
     * @return Lumina's response
     */
    public String getResponse(String msg) {
        String command = msg.split(" ")[0].trim();

        String resp = "";
        try {
            switch (command) {
            case ECHO_EXIT_STRING:
                storage.saveData(taskList);
                resp = EXIT_RESPONSE;
                break;
            case ECHO_LIST_STRING:
                resp = taskList.listTasks();
                break;
            case ECHO_UNMARK_TASK_STRING:
                resp = taskList.markTaskNotDone(msg);
                break;
            case ECHO_MARK_TASK_STRING:
                resp = taskList.markTaskDone(msg);
                break;
            case ECHO_TODO_TASK:
                resp = taskList.handleTodoTask(msg);
                break;
            case ECHO_EVENT_TASK:
                resp = taskList.handleEventTask(msg);
                break;
            case ECHO_DEADLINE_TASK:
                resp = taskList.handleDeadlineTask(msg);
                break;
            case ECHO_DELETE_TASK:
                resp = taskList.deleteTask(msg);
                break;
            case ECHO_FIND_TASK:
                resp = taskList.findTasks(msg);
                break;
            case ECHO_REMIND_TASK:
                resp = taskList.remindTasks();
                break;
            default:
                resp = DEFAULT_MESSAGE;
            }
        } catch (LuminaException e) {
            resp = e.getMessage();
        }
        return resp;
    }

    private void echo(Scanner sc) {
        ui.greet();
        String msg;
        while (true) {
            System.out.println();
            msg = sc.nextLine();
            String resp = this.getResponse(msg);
            ui.printMessage(resp);
            if (resp.equals(EXIT_RESPONSE)) {
                ui.exit();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Lumina lumina = new Lumina();
        lumina.echo(sc);
    }
}
