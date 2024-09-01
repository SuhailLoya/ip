import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * The Lumina class represents a simple chatbot.
 * It uses predefined set of responses and can be extended for more complex behaviour
 */
public class Lumina {

    private static int luminaCount = 0;
    private static final String ECHO_EXIT_STRING = "bye";
    private static final String ECHO_LIST_STRING = "list";
    private static final String ECHO_MARK_TASK_STRING = "mark";
    private static final String ECHO_UNMARK_TASK_STRING = "unmark";
    private static final String ECHO_TODO_TASK = "todo";
    private static final String ECHO_DEADLINE_TASK = "deadline";
    private static final String ECHO_EVENT_TASK = "event";
    private static final String ECHO_DELETE_TASK = "delete";
    private static final int indentWidth = 2;

    // task description and whether the task is done
    private ArrayList<Task> tasks;

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
        tasks = new ArrayList<>();
        luminaCount++;
        loadData();
    }

    private void createDataDirectoryAndFileIfNotExists() {

        // get relative and absolute Path to data directory
        Path relativePath = Paths.get("./data");
        Path absolutePath = relativePath.toAbsolutePath().normalize();
        System.out.println(absolutePath);

        // declare fileName and it's path
        String fileName = "data.txt";
        Path filePath = absolutePath.resolve(fileName);

        // create the directory and file if it does not exist
        try {
            if (Files.notExists(absolutePath)) {
                // Create the directory if it doesn't exist
                Files.createDirectory(absolutePath);
            }

            if (Files.notExists(filePath)) {
                // Create the file if it doesn't exist
                Files.createFile(filePath);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private ArrayList<Task> readData() {
        ArrayList<Task> retTasks = new ArrayList<>();

        Path path = Paths.get("./data/data.txt");

        try (Stream<String> lines = Files.lines(path)) {
            retTasks = lines.map(this::parseDataLine).
                    filter(Objects::nonNull).
                    collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return retTasks;
    }

    private Task parseDataLine(String line) {
        String[] parts = line.split(" \\| ");
        LuminaException luminaException = new LuminaException(
                String.format("Corrupt data entry: %s", line)
        );
        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task = null;
        try {
            switch (type) {
                case "T":
                    if (parts.length != 3) {
                        throw luminaException;
                    }
                    task = new TodoTask(description, isDone);
                    break;
                case "D":
                    if (parts.length != 4) {
                        throw luminaException;
                    }
                    String byDateTime = parts[3].trim();
                    task = new DeadlineTask(description, byDateTime, isDone);
                    break;
                case "E":
                    if (parts.length != 5) {
                        throw luminaException;
                    }
                    String startDateTime = parts[3].trim();
                    String endDateTime = parts[4].trim();
                    task = new EventTask(description, startDateTime, endDateTime, isDone);
                    break;
                default:
                    throw luminaException;

            }
        } catch (LuminaException e) {
            System.err.println(e.getMessage());
        }

        return task;
    }

    /**
     * Loads all valid data onto the current list of tasks from data.txt
     */
    private void loadData() {
        createDataDirectoryAndFileIfNotExists();
        tasks = readData();
    }

    private String indentMessage(String msg) {
        String[] lines = msg.split("\n");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < indentWidth; j++) {
                builder.append(" ");
            }
            builder.append(lines[i]);
            if (i < lines.length - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private void printSeparator() {
        System.out.println("__________________________________________________________________");
    }

    private void printMessage(String msg) {
        this.printSeparator();
        System.out.println(indentMessage(msg));
        this.printSeparator();
    }

    private void greet() {
        this.printMessage("Hello! I'm Lumina\n" +
                "What can I do for you?");
    }

    private void exit() {
        this.printMessage("Bye. Hope to see you again soon!");
        System.exit(0);
    }

    private void addTask(Task task) {
        StringBuilder addTaskMessage = new StringBuilder();
        addTaskMessage.append("Got it. I've added this task:\n");
        addTaskMessage.append(indentMessage(task.toString())).append("\n");
        this.tasks.add(task);
        addTaskMessage.append(String.format("Now you have %d tasks in the list.", tasks.size()));
        this.printMessage(addTaskMessage.toString());
    }

    private void listTasks() {
        StringBuilder listedTaskMessage = new StringBuilder();
        listedTaskMessage.append("Here are the tasks in your list:\n");
        for (int i = 0; i < this.tasks.size(); i++) {
            listedTaskMessage.append(Integer.toString(i + 1)).append(".");
            listedTaskMessage.append(this.tasks.get(i));
            if (i < this.tasks.size() - 1) {
                listedTaskMessage.append("\n");
            }
        }
        this.printMessage(listedTaskMessage.toString());
    }

    private void markTaskDone(int index) throws LuminaException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LuminaException("Oh no! Lumina detected index out of bounds! Please try again");
        }
        StringBuilder taskDoneMessage = new StringBuilder();
        taskDoneMessage.append("Nice! I've marked this task as done:\n");
        this.tasks.get(index).markAsDone();
        taskDoneMessage.append(indentMessage(this.tasks.get(index).toString()));
        this.printMessage(taskDoneMessage.toString());
    }

    private void markTaskNotDone(int index) throws LuminaException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LuminaException("Oh no! Lumina detected index out of bounds! Please try again");
        }
        StringBuilder taskNotDoneMessage = new StringBuilder();
        taskNotDoneMessage.append("OK, I've marked this task as not done yet:\n");
        this.tasks.get(index).markAsNotDone();
        taskNotDoneMessage.append(indentMessage(this.tasks.get(index).toString()));
        this.printMessage(taskNotDoneMessage.toString());
    }



    private void handleTodoTask(String msg) throws LuminaException{
        String[] msgSplit = msg.split(" ");
        if(msgSplit.length < 2) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your ToDo Task!" +
                    " Please try again");
        }
        if(!msgSplit[0].equals(ECHO_TODO_TASK)) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your ToDo Task!" +
                    " Please try again");
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < msgSplit.length; i++) {
            builder.append(msgSplit[i]);
            if(i < msgSplit.length - 1) {
                builder.append(" ");
            }
        }
        Task task = new TodoTask(builder.toString());
        this.addTask(task);

    }

    private void handleDeadlineTask(String msg) throws LuminaException{
        String[] msgSplit = msg.split(" ");
        if(msgSplit.length < 4) { // need desc and bydatetime
            throw new LuminaException("Oh no! Lumina detected invalid format for your Deadline Task!" +
                    " Please try again");
        }
        if(!msgSplit[0].equals(ECHO_DEADLINE_TASK)) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your Deadline Task!" +
                    " Please try again");
        }

        StringBuilder builder = new StringBuilder();
        String desc = "", byDateTime = "";
        boolean by = false;

        for(int i = 1; i < msgSplit.length; i++) {
            if(msgSplit[i].equals("/by")) {
                if(by) {
                    throw new LuminaException("Oh no! Lumina detected invalid format for your Deadline Task!" +
                            " Please try again");
                }
                by = true;
                desc = builder.toString();
                builder = new StringBuilder();
                continue;
            }
            builder.append(msgSplit[i]);
            if (i < msgSplit.length - 1) {
                builder.append(" ");
            }
        }

        if (!by) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your Deadline Task!" +
                    " Please try again");
        }
        byDateTime = builder.toString();
        if (desc.trim().isEmpty() || byDateTime.trim().isEmpty()) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your Deadline Task!" +
                    " Please try again");
        }
        Task task = new DeadlineTask(desc, byDateTime);
        this.addTask(task);
    }

    private void deleteTask(int index) throws LuminaException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LuminaException("Oh no! Lumina detected index out of bounds! Please try again");
        }
        StringBuilder deleteTaskMessage = new StringBuilder();
        deleteTaskMessage.append("Noted. I've removed this task:\n");
        deleteTaskMessage.append(indentMessage(this.tasks.get(index).toString()));
        deleteTaskMessage.append("\n");
        this.tasks.remove(index);
        deleteTaskMessage.append(String.format("Now you have %d tasks in the list.", tasks.size()));
        this.printMessage(deleteTaskMessage.toString());
    }

    private void handleEventTask(String msg) throws LuminaException{
        String[] msgSplit = msg.split(" ");
        if(msgSplit.length < 6) { // need desc, startDateTime, endDateTime
            throw new LuminaException("Oh no! Lumina detected invalid format for your Event Task!" +
                    " Please try again");
        }
        if(!msgSplit[0].equals(ECHO_EVENT_TASK)) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your Event Task!" +
                    " Please try again");
        }

        enum Type {
            DESC, FROM, TO
        }

        Type currentType = Type.DESC;

        StringBuilder builder = new StringBuilder();
        String desc = "", startDateTime = "", endDateTime = "";
        boolean from = false;
        boolean to = false;
        for(int i = 1; i < msgSplit.length; i++) {
            if(msgSplit[i].equals("/from")) {
                if (from) {
                    throw new LuminaException("Oh no! Lumina detected invalid format for your Event Task!" +
                            " Please try again");
                }
                switch (currentType) {
                    case DESC:
                        desc = builder.toString();
                        break;
                    case FROM:
                        startDateTime = builder.toString();
                        break;
                    case TO:
                        endDateTime = builder.toString();
                        break;
                }
                builder = new StringBuilder();
                from = true;
                currentType = Type.FROM;
                continue;
            }
            if (msgSplit[i].equals("/to")) {
                if (to) {
                    throw new LuminaException("Oh no! Lumina detected invalid format for your Event Task!" +
                            " Please try again");
                }
                switch (currentType) {
                    case DESC:
                        desc = builder.toString();
                        break;
                    case FROM:
                        startDateTime = builder.toString();
                        break;
                    case TO:
                        endDateTime = builder.toString();
                        break;
                }
                builder = new StringBuilder();
                to = true;
                currentType = Type.TO;
                continue;
            }
            builder.append(msgSplit[i]);
            if (i < msgSplit.length - 1) {
                builder.append(" ");
            }
        }

        if (!from || !to) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your Event Task!" +
                    " Please try again");
        }
        switch (currentType) {
            case DESC:
                desc = builder.toString();
                break;
            case FROM:
                startDateTime = builder.toString();
                break;
            case TO:
                endDateTime = builder.toString();
                break;
        }
        if (desc.trim().isEmpty() || startDateTime.trim().isEmpty() || endDateTime.trim().isEmpty()) {
            throw new LuminaException("Oh no! Lumina detected invalid format for your Event Task!" +
                    " Please try again");
        }
        Task task = new EventTask(desc, startDateTime, endDateTime);
        this.addTask(task);
    }

    private void echo(Scanner sc) {
        String msg;
        while(true) {
            System.out.println();
            msg = sc.nextLine();

            if (msg.equals(Lumina.ECHO_EXIT_STRING)) {
                this.exit();
                break;
            }
            if (msg.equals(Lumina.ECHO_LIST_STRING)) {
                this.listTasks();
                continue;
            }
            // first check unmark since mark contains unmark
            if (msg.contains(Lumina.ECHO_UNMARK_TASK_STRING)) {
                try {
                    String[] msgSplit = msg.split(" ");
                    if (msgSplit.length == 2) {
                        int taskIndex = Integer.parseInt(msgSplit[1]) - 1; // 0 indexed
                        this.markTaskNotDone(taskIndex);
                    } else {
                        throw new LuminaException("Oh no! Lumina detected unexpected number of parameters in " +
                                "your command! Please try again");
                    }
                } catch(LuminaException e) {
                    this.printMessage(e.getMessage());
                }
                continue;
            }
            if (msg.contains(Lumina.ECHO_MARK_TASK_STRING)) {
                try {
                    String[] msgSplit = msg.split(" ");
                    if (msgSplit.length == 2) {
                        int taskIndex = Integer.parseInt(msgSplit[1]) - 1; // 0 indexed
                        this.markTaskDone(taskIndex);
                    } else {
                        throw new LuminaException("Oh no! Lumina detected unexpected number of parameters in " +
                                "your command! Please try again");
                    }
                } catch(LuminaException e) {
                    this.printMessage(e.getMessage());
                }
                continue;
            }
            if (msg.contains(Lumina.ECHO_TODO_TASK)) {
                try {
                    this.handleTodoTask(msg);
                } catch(LuminaException e) {
                    this.printMessage(e.getMessage());
                }
                continue;
            }
            if (msg.contains(Lumina.ECHO_EVENT_TASK)) {
                try {
                    this.handleEventTask(msg);
                } catch (LuminaException e) {
                    this.printMessage(e.getMessage());
                }
                continue;
            }
            if (msg.contains(Lumina.ECHO_DEADLINE_TASK)) {
                try {
                    this.handleDeadlineTask(msg);
                } catch (LuminaException e) {
                    this.printMessage(e.getMessage());
                }
                continue;
            }
            if (msg.contains(Lumina.ECHO_DELETE_TASK)) {
                try {
                    String[] msgSplit = msg.split(" ");
                    if (msgSplit.length == 2) {
                        int taskIndex = Integer.parseInt(msgSplit[1]) - 1; // 0 indexed
                        this.deleteTask(taskIndex);
                    } else {
                        throw new LuminaException("Oh no! Lumina detected unexpected number of parameters in " +
                                "your command! Please try again");
                    }
                } catch(LuminaException e) {
                    this.printMessage(e.getMessage());
                }
                continue;
            }

            // exception
            this.printMessage("Oh no! I don't know what to do with this command!");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Lumina lumina = new Lumina();
        lumina.greet();
        lumina.echo(sc);
    }
}
